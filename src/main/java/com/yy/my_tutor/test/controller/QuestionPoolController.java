package com.yy.my_tutor.test.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.math.mapper.QuestionMapper;
import com.yy.my_tutor.math.service.KnowledgePointService;
import com.yy.my_tutor.test.job.QuestionPoolFillJob;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * 题库管理控制器
 * 提供题库预填充的手动触发和储备统计接口
 */
@Slf4j
@RestController
@RequestMapping("/api/question-pool")
@CrossOrigin(origins = "*")
public class QuestionPoolController {

    @Resource
    private QuestionPoolFillJob questionPoolFillJob;

    @Resource
    private KnowledgePointService knowledgePointService;

    @Resource
    private QuestionMapper questionMapper;

    /**
     * 手动触发题库填充（异步执行）
     * POST /api/question-pool/fill
     *
     * 请求体示例：
     * {
     *   "gradeId": 8,              // 按年级填充（可选，与knowledgePointIds互斥）
     *   "knowledgePointIds": [437], // 指定知识点ID（可选）
     *   "difficultyLevels": [1,2,3], // 难度级别（可选，默认1,2,3）
     *   "targetCount": 20,         // 每个知识点x难度的目标题数（默认50）
     *   "batchSize": 5,            // 每次AI调用生成的题数（默认5）
     *   "parallel": 3              // 并行线程数（默认1，建议3-5）
     * }
     */
    @PostMapping("/fill")
    public RespResult<Map<String, Object>> fillQuestionPool(@RequestBody(required = false) FillRequest request) {
        if (questionPoolFillJob.isRunning()) {
            return RespResult.error("题库填充任务正在执行中，请稍后再试。可通过 GET /api/question-pool/fill/progress 查看进度");
        }

        final List<Integer> knowledgePointIds;
        final List<Integer> difficultyLevels;
        final int targetCount;
        final int batchSize;
        final Integer gradeId;
        final int parallel;

        if (request != null) {
            knowledgePointIds = request.getKnowledgePointIds();
            difficultyLevels = request.getDifficultyLevels();
            targetCount = (request.getTargetCount() != null && request.getTargetCount() > 0)
                    ? request.getTargetCount() : 50;
            batchSize = (request.getBatchSize() != null && request.getBatchSize() > 0)
                    ? request.getBatchSize() : 5;
            gradeId = request.getGradeId();
            parallel = (request.getParallel() != null && request.getParallel() > 0)
                    ? Math.min(request.getParallel(), 10) : 1;
        } else {
            knowledgePointIds = null;
            difficultyLevels = null;
            targetCount = 50;
            batchSize = 5;
            gradeId = null;
            parallel = 1;
        }

        // 参数校验：gradeId 和 knowledgePointIds 不能同时指定
        if (gradeId != null && knowledgePointIds != null && !knowledgePointIds.isEmpty()) {
            return RespResult.error("gradeId 和 knowledgePointIds 不能同时指定，请选择其一");
        }

        log.info("手动触发题库填充: gradeId={}, knowledgePointIds={}, difficultyLevels={}, targetCount={}, batchSize={}, parallel={}",
                gradeId, knowledgePointIds, difficultyLevels, targetCount, batchSize, parallel);

        // 异步执行填充任务
        Thread fillThread = new Thread(() -> {
            try {
                if (gradeId != null) {
                    questionPoolFillJob.fillQuestionPoolByGrade(gradeId, difficultyLevels, targetCount, batchSize, parallel);
                } else {
                    questionPoolFillJob.fillQuestionPool(knowledgePointIds, difficultyLevels, targetCount, batchSize, parallel);
                }
            } catch (Exception e) {
                log.error("异步填充任务异常", e);
            }
        }, "question-pool-fill");
        fillThread.setDaemon(true);
        fillThread.start();

        Map<String, Object> data = new HashMap<>();
        data.put("message", "题库填充任务已启动");
        data.put("progressUrl", "/api/question-pool/fill/progress");
        data.put("stopUrl", "/api/question-pool/fill/stop");

        return RespResult.success("任务已启动，请通过进度接口查看执行情况", data);
    }

    /**
     * 查询填充任务进度
     * GET /api/question-pool/fill/progress
     */
    @GetMapping("/fill/progress")
    public RespResult<QuestionPoolFillJob.FillProgress> getFillProgress() {
        return RespResult.success("查询成功", questionPoolFillJob.getProgress());
    }

    /**
     * 停止正在执行的填充任务
     * POST /api/question-pool/fill/stop
     */
    @PostMapping("/fill/stop")
    public RespResult<String> stopFill() {
        if (!questionPoolFillJob.isRunning()) {
            return RespResult.error("当前没有正在执行的填充任务");
        }
        questionPoolFillJob.requestStop();
        return RespResult.success("已发送停止请求，任务将在当前批次完成后停止");
    }

    /**
     * 查询题库储备统计
     * GET /api/question-pool/stats
     *
     * 支持参数：
     * - knowledgePointIds: 指定知识点ID（可选）
     * - gradeId: 按年级查询（可选）
     */
    @GetMapping("/stats")
    public RespResult<Map<String, Object>> getPoolStats(
            @RequestParam(required = false) List<Integer> knowledgePointIds,
            @RequestParam(required = false) Integer gradeId) {

        List<KnowledgePoint> knowledgePoints;
        if (knowledgePointIds != null && !knowledgePointIds.isEmpty()) {
            knowledgePoints = new ArrayList<>();
            for (Integer id : knowledgePointIds) {
                KnowledgePoint kp = knowledgePointService.findKnowledgePointById(id);
                if (kp != null) {
                    knowledgePoints.add(kp);
                }
            }
        } else if (gradeId != null) {
            knowledgePoints = knowledgePointService.findKnowledgePointsByGradeId(gradeId);
        } else {
            knowledgePoints = knowledgePointService.findAllKnowledgePoints();
        }

        List<Integer> difficultyLevels = Arrays.asList(1, 2, 3);
        List<Map<String, Object>> details = new ArrayList<>();
        int totalQuestions = 0;
        int sufficientCount = 0;
        int insufficientCount = 0;

        for (KnowledgePoint kp : knowledgePoints) {
            for (Integer level : difficultyLevels) {
                int count = questionMapper.countByKnowledgePointAndDifficulty(kp.getId(), level);
                totalQuestions += count;
                boolean sufficient = count >= 20;
                if (sufficient) {
                    sufficientCount++;
                } else {
                    insufficientCount++;
                }

                Map<String, Object> item = new LinkedHashMap<>();
                item.put("knowledgePointId", kp.getId());
                item.put("knowledgePointName", kp.getPointName());
                item.put("gradeId", kp.getGradeId());
                item.put("difficultyLevel", level);
                item.put("questionCount", count);
                item.put("sufficient", sufficient);
                details.add(item);
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalKnowledgePoints", knowledgePoints.size());
        result.put("totalCombinations", knowledgePoints.size() * 3);
        result.put("sufficientCount", sufficientCount);
        result.put("insufficientCount", insufficientCount);
        result.put("totalQuestions", totalQuestions);
        result.put("details", details);

        return RespResult.success("查询成功", result);
    }

    @Data
    public static class FillRequest {
        private Integer gradeId;
        private List<Integer> knowledgePointIds;
        private List<Integer> difficultyLevels;
        private Integer targetCount;
        private Integer batchSize;
        private Integer parallel;
    }
}
