package com.yy.my_tutor.test.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.test.domain.GenerateAITestRequest;
import com.yy.my_tutor.test.domain.TestWithQuestionsDTO;
import com.yy.my_tutor.test.service.AITestGenerateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * AI 测试生成控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/ai-test")
@CrossOrigin(origins = "*")
public class AITestController {

    @Resource
    private AITestGenerateService aiTestGenerateService;

    /**
     * 根据单个知识点生成 AI 测试
     * POST /api/ai-test/generate-single
     *
     * 请求示例：
     * {
     *   "studentId": 1,
     *   "knowledgePointId": 100,
     *   "questionCount": 5,
     *   "difficultyLevel": 2,          // 可选，不传则根据学生分析报告自动确定
     *   "questionType": 1,             // 可选，1-单选 2-多选 3-填空 4-计算
     *   "saveToQuestionBank": true,    // 可选，默认true
     *   "testName": "Test Name",       // 可选
     *   "testNameFr": "Nom du test"    // 可选
     * }
     *
     * @param request 生成请求
     * @return 测试详情（包含题目列表）
     */
    @PostMapping("/generate-single")
    public RespResult<TestWithQuestionsDTO> generateTestBySingleKnowledgePoint(
            @RequestBody GenerateAITestRequest request) {
        log.info("Generating AI test for single knowledge point: {}", request);

        try {
            TestWithQuestionsDTO result = aiTestGenerateService.generateTestBySingleKnowledgePoint(request);
            return RespResult.success("AI测试生成成功", result);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return RespResult.error(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to generate AI test", e);
            return RespResult.error("AI测试生成失败: " + e.getMessage());
        }
    }

    /**
     * 根据多个知识点生成综合 AI 测试
     * POST /api/ai-test/generate-comprehensive
     *
     * 请求示例：
     * {
     *   "studentId": 1,
     *   "knowledgePointIds": [100, 101, 102],
     *   "gradeId": 3,                  // 可选
     *   "questionCount": 10,
     *   "difficultyLevel": 2,          // 可选，默认中等难度
     *   "saveToQuestionBank": true,    // 可选，默认true
     *   "testName": "Comprehensive Test",
     *   "testNameFr": "Test Complet"
     * }
     *
     * @param request 生成请求
     * @return 测试详情（包含题目列表）
     */
    @PostMapping("/generate-comprehensive")
    public RespResult<TestWithQuestionsDTO> generateTestByMultipleKnowledgePoints(
            @RequestBody GenerateAITestRequest request) {
        log.info("Generating AI comprehensive test for knowledge points: {}", request);

        try {
            TestWithQuestionsDTO result = aiTestGenerateService.generateTestByMultipleKnowledgePoints(request);
            return RespResult.success("AI综合测试生成成功", result);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return RespResult.error(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to generate AI comprehensive test", e);
            return RespResult.error("AI综合测试生成失败: " + e.getMessage());
        }
    }
}
