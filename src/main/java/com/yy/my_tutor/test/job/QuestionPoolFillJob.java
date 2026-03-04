package com.yy.my_tutor.test.job;

import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.math.domain.Question;
import com.yy.my_tutor.math.mapper.QuestionMapper;
import com.yy.my_tutor.math.service.KnowledgePointService;
import com.yy.my_tutor.test.service.QuestionGenerateService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 题库预填充定时任务
 * 确保每个知识点 x 难度级别组合至少有指定数量的题目
 * 支持并行生成以加速批量填充
 */
@Slf4j
@Component
public class QuestionPoolFillJob {

    private static final int DEFAULT_TARGET_COUNT = 50;
    private static final int DEFAULT_BATCH_SIZE = 5;
    private static final int DEFAULT_PARALLEL = 1;
    private static final long SLEEP_BETWEEN_BATCHES_MS = 2000;
    private static final List<Integer> DIFFICULTY_LEVELS = Arrays.asList(1, 2, 3);
    private static final int QUESTION_TYPE = 1; // 单选题

    @Resource
    private KnowledgePointService knowledgePointService;

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private QuestionGenerateService questionGenerateService;

    // 异步补充：去重 map 和线程池
    private final ConcurrentHashMap<String, Boolean> pendingFills = new ConcurrentHashMap<>();
    private ExecutorService asyncFillExecutor;

    private volatile boolean running = false;
    private volatile boolean stopRequested = false;

    // 进度追踪（使用原子类保证并发安全）
    private volatile int totalCombinations = 0;
    private final AtomicInteger processedCombinations = new AtomicInteger(0);
    private final AtomicInteger totalGenerated = new AtomicInteger(0);
    private final AtomicInteger totalSkipped = new AtomicInteger(0);
    private final AtomicInteger totalFailed = new AtomicInteger(0);
    private volatile String currentKpName = "";
    private volatile int currentDifficulty = 0;
    private volatile long startTimeMs = 0;
    private volatile String lastError = null;

    @PostConstruct
    public void init() {
        asyncFillExecutor = Executors.newFixedThreadPool(2, r -> {
            Thread t = new Thread(r, "async-pool-fill");
            t.setDaemon(true);
            return t;
        });
    }

    @PreDestroy
    public void destroy() {
        if (asyncFillExecutor != null) {
            asyncFillExecutor.shutdownNow();
        }
    }

    /**
     * 题库预填充（已移除定时触发，仅供手动 API 调用）
     */
    public void scheduledFill() {
        fillQuestionPool(null, null, DEFAULT_TARGET_COUNT, DEFAULT_BATCH_SIZE, DEFAULT_PARALLEL);
    }

    /**
     * 执行题库填充（兼容旧接口）
     */
    public int fillQuestionPool(List<Integer> knowledgePointIds, List<Integer> difficultyLevels, int targetCount) {
        return fillQuestionPool(knowledgePointIds, difficultyLevels, targetCount, DEFAULT_BATCH_SIZE, DEFAULT_PARALLEL);
    }

    public int fillQuestionPool(List<Integer> knowledgePointIds, List<Integer> difficultyLevels,
                                int targetCount, int batchSize) {
        return fillQuestionPool(knowledgePointIds, difficultyLevels, targetCount, batchSize, DEFAULT_PARALLEL);
    }

    /**
     * 执行题库填充（支持并行）
     *
     * @param knowledgePointIds 指定的知识点ID列表，为null则全部
     * @param difficultyLevels  指定的难度级别列表，为null则全部(1,2,3)
     * @param targetCount       每个知识点x难度的目标数量
     * @param batchSize         每次AI调用生成的题目数
     * @param parallel          并行线程数
     * @return 本次填充的题目总数
     */
    public int fillQuestionPool(List<Integer> knowledgePointIds, List<Integer> difficultyLevels,
                                int targetCount, int batchSize, int parallel) {
        if (running) {
            log.warn("题库填充任务正在执行中，跳过本次触发");
            return 0;
        }

        running = true;
        stopRequested = false;
        resetProgress();

        try {
            log.info("===== 题库预填充任务开始 =====");

            List<KnowledgePoint> knowledgePoints = resolveKnowledgePoints(knowledgePointIds);
            List<Integer> levels = (difficultyLevels != null && !difficultyLevels.isEmpty())
                    ? difficultyLevels : DIFFICULTY_LEVELS;

            // 构建所有待处理的任务
            List<FillTask> tasks = new ArrayList<>();
            for (KnowledgePoint kp : knowledgePoints) {
                for (Integer level : levels) {
                    tasks.add(new FillTask(kp, level, targetCount, batchSize));
                }
            }

            totalCombinations = tasks.size();
            startTimeMs = System.currentTimeMillis();

            log.info("待处理组合数: {}, 知识点: {}, 难度级别: {}, 目标: {}, 批次: {}, 并行: {}",
                    tasks.size(), knowledgePoints.size(), levels, targetCount, batchSize, parallel);

            if (parallel <= 1) {
                // 串行执行
                executeSequential(tasks);
            } else {
                // 并行执行
                executeParallel(tasks, parallel);
            }

            log.info("===== 题库预填充任务完成，共生成 {} 道题目, 跳过 {}, 失败 {} =====",
                    totalGenerated.get(), totalSkipped.get(), totalFailed.get());
        } catch (Exception e) {
            log.error("题库预填充任务异常", e);
            lastError = e.getMessage();
        } finally {
            running = false;
            currentKpName = "";
            currentDifficulty = 0;
        }

        return totalGenerated.get();
    }

    /**
     * 按年级填充题库
     */
    public int fillQuestionPoolByGrade(Integer gradeId, List<Integer> difficultyLevels,
                                       int targetCount, int batchSize, int parallel) {
        List<KnowledgePoint> knowledgePoints = knowledgePointService.findKnowledgePointsByGradeId(gradeId);
        if (knowledgePoints == null || knowledgePoints.isEmpty()) {
            log.warn("年级 {} 下没有知识点", gradeId);
            return 0;
        }

        List<Integer> kpIds = new ArrayList<>();
        for (KnowledgePoint kp : knowledgePoints) {
            kpIds.add(kp.getId());
        }

        log.info("按年级填充: gradeId={}, 知识点数量={}, 并行={}", gradeId, kpIds.size(), parallel);
        return fillQuestionPool(kpIds, difficultyLevels, targetCount, batchSize, parallel);
    }

    /**
     * 串行执行
     */
    private void executeSequential(List<FillTask> tasks) {
        for (FillTask task : tasks) {
            if (stopRequested) {
                log.info("收到停止请求，终止填充任务");
                break;
            }
            currentKpName = task.kp.getPointName();
            currentDifficulty = task.difficultyLevel;
            executeSingleTask(task);
        }
    }

    /**
     * 并行执行
     */
    private void executeParallel(List<FillTask> tasks, int parallel) {
        ExecutorService executor = Executors.newFixedThreadPool(parallel,
                r -> {
                    Thread t = new Thread(r, "pool-fill-worker");
                    t.setDaemon(true);
                    return t;
                });

        try {
            List<Future<?>> futures = new ArrayList<>();
            for (FillTask task : tasks) {
                futures.add(executor.submit(() -> {
                    if (stopRequested) return;
                    currentKpName = task.kp.getPointName();
                    currentDifficulty = task.difficultyLevel;
                    executeSingleTask(task);
                }));
            }

            // 等待所有任务完成
            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (ExecutionException e) {
                    log.error("并行任务执行异常: {}", e.getMessage());
                }
            }
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * 执行单个知识点×难度组合的填充
     */
    private void executeSingleTask(FillTask task) {
        try {
            int generated = fillForKnowledgePointAndDifficulty(
                    task.kp, task.difficultyLevel, task.targetCount, task.batchSize);
            totalGenerated.addAndGet(generated);
            if (generated == 0) {
                totalSkipped.incrementAndGet();
            }
        } catch (Exception e) {
            totalFailed.incrementAndGet();
            lastError = String.format("知识点[%s]-难度[%d]: %s",
                    task.kp.getPointName(), task.difficultyLevel, e.getMessage());
            log.error("任务执行失败: {}", lastError);
        } finally {
            processedCombinations.incrementAndGet();
        }
    }

    private int fillForKnowledgePointAndDifficulty(KnowledgePoint kp, Integer difficultyLevel,
                                                    int targetCount, int batchSize) {
        int currentCount = questionMapper.countByKnowledgePointAndDifficulty(kp.getId(), difficultyLevel);
        int deficit = targetCount - currentCount;

        if (deficit <= 0) {
            log.debug("知识点[{}]-难度[{}] 已有{}题, 满足目标{}",
                    kp.getPointName(), difficultyLevel, currentCount, targetCount);
            return 0;
        }

        log.info("知识点[{}]-难度[{}] 当前{}题, 需补充{}题",
                kp.getPointName(), difficultyLevel, currentCount, deficit);

        int generated = 0;
        while (generated < deficit && !stopRequested) {
            int thisBatch = Math.min(batchSize, deficit - generated);
            try {
                // 先生成题目（不保存）
                List<Question> questions = questionGenerateService.generateQuestions(
                        kp, thisBatch, difficultyLevel, QUESTION_TYPE);

                // 设置来源为 BATCH 并保存
                for (Question question : questions) {
                    question.setGenerationSource("BATCH");
                    question.setCreateBy("system");
                    question.setUpdateBy("system");
                    questionMapper.insertQuestion(question);
                }

                generated += questions.size();
                log.info("知识点[{}]-难度[{}] 本批次生成{}题, 累计{}题",
                        kp.getPointName(), difficultyLevel, questions.size(), generated);

                if (generated < deficit) {
                    Thread.sleep(SLEEP_BETWEEN_BATCHES_MS);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("题库填充任务被中断");
                break;
            } catch (Exception e) {
                log.error("知识点[{}]-难度[{}] 生成题目失败: {}",
                        kp.getPointName(), difficultyLevel, e.getMessage());
                lastError = String.format("知识点[%s]-难度[%d]: %s",
                        kp.getPointName(), difficultyLevel, e.getMessage());
                break;
            }
        }

        return generated;
    }

    /**
     * 解析知识点列表
     */
    private List<KnowledgePoint> resolveKnowledgePoints(List<Integer> knowledgePointIds) {
        if (knowledgePointIds != null && !knowledgePointIds.isEmpty()) {
            List<KnowledgePoint> knowledgePoints = new ArrayList<>();
            for (Integer id : knowledgePointIds) {
                KnowledgePoint kp = knowledgePointService.findKnowledgePointById(id);
                if (kp != null) {
                    knowledgePoints.add(kp);
                }
            }
            return knowledgePoints;
        } else {
            return knowledgePointService.findAllKnowledgePoints();
        }
    }

    /**
     * 请求停止当前任务
     */
    public void requestStop() {
        if (running) {
            stopRequested = true;
            log.info("已请求停止题库填充任务");
        }
    }

    /**
     * 异步补充单个知识点+难度的题目
     * 使用 ConcurrentHashMap 去重，同一组合不会重复触发
     *
     * @param kpId            知识点ID
     * @param difficultyLevel 难度级别
     * @param targetCount     目标题数
     * @param batchSize       每批生成数
     */
    public void asyncFillForKnowledgePoint(Integer kpId, Integer difficultyLevel, int targetCount, int batchSize) {
        String key = kpId + "_" + difficultyLevel;
        if (pendingFills.putIfAbsent(key, true) != null) {
            log.debug("知识点[{}]-难度[{}] 补充已在队列中，跳过", kpId, difficultyLevel);
            return;
        }
        asyncFillExecutor.submit(() -> {
            try {
                KnowledgePoint kp = knowledgePointService.findKnowledgePointById(kpId);
                if (kp == null) {
                    log.warn("异步补充跳过: 知识点ID={} 不存在", kpId);
                    return;
                }
                int generated = fillForKnowledgePointAndDifficulty(kp, difficultyLevel, targetCount, batchSize);
                if (generated > 0) {
                    log.info("异步补充完成: 知识点[{}]-难度[{}], 生成{}题", kp.getPointName(), difficultyLevel, generated);
                }
            } catch (Exception e) {
                log.error("异步补充失败: kpId={}, difficulty={}, error={}", kpId, difficultyLevel, e.getMessage());
            } finally {
                pendingFills.remove(key);
            }
        });
    }

    public boolean isRunning() {
        return running;
    }

    /**
     * 获取当前进度信息
     */
    public FillProgress getProgress() {
        FillProgress progress = new FillProgress();
        progress.setRunning(running);
        progress.setTotalCombinations(totalCombinations);
        progress.setProcessedCombinations(processedCombinations.get());
        progress.setTotalGenerated(totalGenerated.get());
        progress.setTotalSkipped(totalSkipped.get());
        progress.setTotalFailed(totalFailed.get());
        progress.setCurrentKpName(currentKpName);
        progress.setCurrentDifficulty(currentDifficulty);
        progress.setLastError(lastError);

        int processed = processedCombinations.get();
        if (startTimeMs > 0) {
            long elapsed = System.currentTimeMillis() - startTimeMs;
            progress.setElapsedSeconds(elapsed / 1000);
            if (processed > 0 && totalCombinations > processed) {
                long avgMs = elapsed / processed;
                long remaining = avgMs * (totalCombinations - processed);
                progress.setEstimatedRemainingSeconds(remaining / 1000);
            }
        }

        if (totalCombinations > 0) {
            progress.setProgressPercent(Math.round(processed * 100.0 / totalCombinations * 10) / 10.0);
        }

        return progress;
    }

    private void resetProgress() {
        totalCombinations = 0;
        processedCombinations.set(0);
        totalGenerated.set(0);
        totalSkipped.set(0);
        totalFailed.set(0);
        currentKpName = "";
        currentDifficulty = 0;
        startTimeMs = 0;
        lastError = null;
    }

    /**
     * 内部任务封装
     */
    private static class FillTask {
        final KnowledgePoint kp;
        final int difficultyLevel;
        final int targetCount;
        final int batchSize;

        FillTask(KnowledgePoint kp, int difficultyLevel, int targetCount, int batchSize) {
            this.kp = kp;
            this.difficultyLevel = difficultyLevel;
            this.targetCount = targetCount;
            this.batchSize = batchSize;
        }
    }

    @Data
    public static class FillProgress {
        private boolean running;
        private int totalCombinations;
        private int processedCombinations;
        private int totalGenerated;
        private int totalSkipped;
        private int totalFailed;
        private String currentKpName;
        private int currentDifficulty;
        private double progressPercent;
        private long elapsedSeconds;
        private Long estimatedRemainingSeconds;
        private String lastError;
    }
}
