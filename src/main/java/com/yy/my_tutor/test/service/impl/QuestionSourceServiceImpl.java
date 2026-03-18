package com.yy.my_tutor.test.service.impl;

import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.math.domain.Question;
import com.yy.my_tutor.math.mapper.QuestionMapper;
import com.yy.my_tutor.math.service.KnowledgePointService;
import com.yy.my_tutor.test.job.QuestionPoolFillJob;
import com.yy.my_tutor.test.mapper.StudentTestAnswerMapper;
import com.yy.my_tutor.test.service.QuestionGenerateService;
import com.yy.my_tutor.test.service.QuestionSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 题目来源服务实现 — 从 AITestGenerateServiceImpl 提取的公共取题逻辑
 */
@Slf4j
@Service
public class QuestionSourceServiceImpl implements QuestionSourceService {

    private static final int LOW_STOCK_THRESHOLD = 10;
    private static final int REPLENISH_TARGET = 20;
    private static final int REPLENISH_BATCH_SIZE = 5;

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private KnowledgePointService knowledgePointService;

    @Resource
    private QuestionGenerateService questionGenerateService;

    @Resource
    private StudentTestAnswerMapper studentTestAnswerMapper;

    @Resource
    private QuestionPoolFillJob questionPoolFillJob;

    @Override
    public List<Question> getQuestions(Integer studentId, List<Integer> knowledgePointIds,
                                      int count, Integer difficultyLevel, Boolean saveToBank) {
        // 固定题目类型为单选
        final Integer questionType = 1;
        List<Question> result = new ArrayList<>();

        // 1. 查询学生已做过的题目ID
        List<Integer> doneQuestionIds = studentTestAnswerMapper.findDoneQuestionIdsByStudentId(studentId);
        if (doneQuestionIds == null) {
            doneQuestionIds = new ArrayList<>();
        }

        log.info("Student {} has done {} questions", studentId, doneQuestionIds.size());

        // 2. 从题库查询未做过的题目
        List<Question> availableQuestions;
        if (knowledgePointIds.size() == 1) {
            availableQuestions = questionMapper.findUndoneQuestionsByKnowledgePoint(
                    knowledgePointIds.get(0), difficultyLevel, doneQuestionIds);
        } else {
            availableQuestions = questionMapper.findUndoneQuestionsByKnowledgePoints(
                    knowledgePointIds, difficultyLevel, doneQuestionIds);
        }

        log.info("Found {} undone questions in question bank", availableQuestions != null ? availableQuestions.size() : 0);

        // 检测题库存量低，异步触发补充（不影响当前请求的返回）
        if (availableQuestions == null || availableQuestions.size() < LOW_STOCK_THRESHOLD) {
            for (Integer kpId : knowledgePointIds) {
                questionPoolFillJob.asyncFillForKnowledgePoint(kpId, difficultyLevel, REPLENISH_TARGET, REPLENISH_BATCH_SIZE);
            }
        }

        // 3. 如果题库中有足够的题目，直接使用
        if (availableQuestions != null && availableQuestions.size() >= count) {
            Collections.shuffle(availableQuestions);
            return availableQuestions.subList(0, count);
        }

        // 4. 题库题目不足，先添加所有未做过的题目
        if (availableQuestions != null && !availableQuestions.isEmpty()) {
            result.addAll(availableQuestions);
        }

        // 5. 从已做过的题中选间隔最久的补充（兜底复用）
        int stillNeed = count - result.size();
        if (stillNeed > 0 && !doneQuestionIds.isEmpty()) {
            log.info("Undone questions insufficient, trying to reuse {} least recently done questions", stillNeed);
            List<Question> leastRecentlyDone = questionMapper.findLeastRecentlyDoneQuestions(
                    knowledgePointIds, difficultyLevel, doneQuestionIds, studentId, stillNeed);
            if (leastRecentlyDone != null && !leastRecentlyDone.isEmpty()) {
                result.addAll(leastRecentlyDone);
                log.info("Reused {} least recently done questions as fallback", leastRecentlyDone.size());
            }
        }

        // 6. 如果仍不足，再实时AI生成兜底
        int needGenerate = count - result.size();
        if (needGenerate > 0) {
            log.info("Need to generate {} more questions via AI", needGenerate);

            // 为每个知识点生成题目
            int perPointGenerate = Math.max(1, needGenerate / knowledgePointIds.size());
            int remainderGenerate = needGenerate % knowledgePointIds.size();

            for (int i = 0; i < knowledgePointIds.size() && result.size() < count; i++) {
                Integer kpId = knowledgePointIds.get(i);
                KnowledgePoint kp = knowledgePointService.findKnowledgePointById(kpId);
                if (kp == null) {
                    continue;
                }

                int toGenerate = perPointGenerate + (i < remainderGenerate ? 1 : 0);
                toGenerate = Math.min(toGenerate, count - result.size());

                if (toGenerate <= 0) {
                    continue;
                }

                try {
                    List<Question> generated;
                    if (Boolean.TRUE.equals(saveToBank)) {
                        generated = questionGenerateService.generateAndSaveQuestions(
                                kp, toGenerate, difficultyLevel, questionType);
                    } else {
                        generated = questionGenerateService.generateQuestions(
                                kp, toGenerate, difficultyLevel, questionType);
                    }
                    result.addAll(generated);
                    log.info("Generated {} questions for knowledge point {}", generated.size(), kp.getPointName());
                } catch (Exception e) {
                    log.error("Failed to generate questions for knowledge point {}: {}",
                            kp.getPointName(), e.getMessage());
                }
            }
        }

        return result;
    }

    @Override
    public Map<Integer, List<Question>> getQuestionsByDifficulties(
            Integer studentId, List<Integer> knowledgePointIds,
            Map<Integer, Integer> difficultyCountMap, Boolean saveToBank) {

        final Integer questionType = 1;
        Map<Integer, List<Question>> resultMap = new LinkedHashMap<>();

        // 1. 一次查出学生已做题 ID（而不是每个难度查一次）
        List<Integer> doneQuestionIds = studentTestAnswerMapper.findDoneQuestionIdsByStudentId(studentId);
        if (doneQuestionIds == null) {
            doneQuestionIds = new ArrayList<>();
        }

        log.info("[Batch] Student {} has done {} questions, requesting {} difficulties",
                studentId, doneQuestionIds.size(), difficultyCountMap.size());

        // 收集所有需要异步补充的 (kpId, difficulty) 组合，最后统一触发
        List<int[]> asyncFillNeeded = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : difficultyCountMap.entrySet()) {
            int diffLevel = entry.getKey();
            int count = entry.getValue();
            List<Question> result = new ArrayList<>();

            if (count <= 0) {
                resultMap.put(diffLevel, result);
                continue;
            }

            // 2. 从题库查询未做过的题目
            List<Question> availableQuestions;
            if (knowledgePointIds.size() == 1) {
                availableQuestions = questionMapper.findUndoneQuestionsByKnowledgePoint(
                        knowledgePointIds.get(0), diffLevel, doneQuestionIds);
            } else {
                availableQuestions = questionMapper.findUndoneQuestionsByKnowledgePoints(
                        knowledgePointIds, diffLevel, doneQuestionIds);
            }

            // 检测低库存
            if (availableQuestions == null || availableQuestions.size() < LOW_STOCK_THRESHOLD) {
                for (Integer kpId : knowledgePointIds) {
                    asyncFillNeeded.add(new int[]{kpId, diffLevel});
                }
            }

            // 3. 题库充足，直接取
            if (availableQuestions != null && availableQuestions.size() >= count) {
                Collections.shuffle(availableQuestions);
                resultMap.put(diffLevel, new ArrayList<>(availableQuestions.subList(0, count)));
                continue;
            }

            // 4. 不足，先加所有未做过的
            if (availableQuestions != null && !availableQuestions.isEmpty()) {
                result.addAll(availableQuestions);
            }

            // 5. 复用间隔最久的已做题
            int stillNeed = count - result.size();
            if (stillNeed > 0 && !doneQuestionIds.isEmpty()) {
                List<Question> leastRecentlyDone = questionMapper.findLeastRecentlyDoneQuestions(
                        knowledgePointIds, diffLevel, doneQuestionIds, studentId, stillNeed);
                if (leastRecentlyDone != null && !leastRecentlyDone.isEmpty()) {
                    result.addAll(leastRecentlyDone);
                }
            }

            // 6. AI 生成兜底
            int needGenerate = count - result.size();
            if (needGenerate > 0) {
                log.info("[Batch] Need to generate {} questions via AI for difficulty {}", needGenerate, diffLevel);
                int perPoint = Math.max(1, needGenerate / knowledgePointIds.size());
                int remainder = needGenerate % knowledgePointIds.size();

                for (int i = 0; i < knowledgePointIds.size() && result.size() < count; i++) {
                    Integer kpId = knowledgePointIds.get(i);
                    KnowledgePoint kp = knowledgePointService.findKnowledgePointById(kpId);
                    if (kp == null) continue;

                    int toGenerate = Math.min(perPoint + (i < remainder ? 1 : 0), count - result.size());
                    if (toGenerate <= 0) continue;

                    try {
                        List<Question> generated;
                        if (Boolean.TRUE.equals(saveToBank)) {
                            generated = questionGenerateService.generateAndSaveQuestions(kp, toGenerate, diffLevel, questionType);
                        } else {
                            generated = questionGenerateService.generateQuestions(kp, toGenerate, diffLevel, questionType);
                        }
                        result.addAll(generated);
                    } catch (Exception e) {
                        log.error("[Batch] Failed to generate for kp {} difficulty {}: {}",
                                kp.getPointName(), diffLevel, e.getMessage());
                    }
                }
            }

            resultMap.put(diffLevel, result);
        }

        // 统一触发异步补充
        for (int[] pair : asyncFillNeeded) {
            questionPoolFillJob.asyncFillForKnowledgePoint(pair[0], pair[1], REPLENISH_TARGET, REPLENISH_BATCH_SIZE);
        }

        return resultMap;
    }
}
