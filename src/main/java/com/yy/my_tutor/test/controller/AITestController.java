package com.yy.my_tutor.test.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.test.domain.GenerateAITestRequest;
import com.yy.my_tutor.test.domain.GenerateAdaptiveTestRequest;
import com.yy.my_tutor.test.domain.TestWithQuestionsDTO;
import com.yy.my_tutor.test.service.AITestGenerateService;
import com.yy.my_tutor.test.service.AdaptiveTestService;
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

    @Resource
    private AdaptiveTestService adaptiveTestService;

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
     * 根据知识类型生成 AI 测试
     * POST /api/ai-test/generate-by-category
     *
     * 请求示例：
     * {
     *   "studentId": 1,
     *   "categoryId": 5,
     *   "gradeId": 3,                  // 可选
     *   "questionCount": 10,
     *   "difficultyLevel": 2,          // 必填，1-简单 2-中等 3-困难
     *   "saveToQuestionBank": true,    // 可选，默认true
     *   "testName": "Category Test",
     *   "testNameFr": "Test par Catégorie"
     * }
     *
     * @param request 生成请求
     * @return 测试详情（包含题目列表）
     */
    @PostMapping("/generate-by-category")
    public RespResult<TestWithQuestionsDTO> generateTestByCategory(
            @RequestBody GenerateAITestRequest request) {
        log.info("Generating AI test for category: {}", request);

        try {
            TestWithQuestionsDTO result = aiTestGenerateService.generateTestByCategory(request);
            return RespResult.success("AI测试生成成功", result);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return RespResult.error(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to generate AI test by category", e);
            return RespResult.error("AI测试生成失败: " + e.getMessage());
        }
    }

    /**
     * 生成自适应混合难度测验
     * POST /api/ai-test/generate-adaptive
     *
     * 请求示例：
     * {
     *   "studentId": 1,
     *   "categoryId": 5,
     *   "questionCount": 10,
     *   "gradeId": 3            // 可选
     * }
     *
     * 难度分配、上次测验记录、测试名称均由后台自动生成。
     *
     * @param request 自适应测验请求
     * @return 测试详情（包含混合难度题目列表）
     */
    @PostMapping("/generate-adaptive")
    public RespResult<TestWithQuestionsDTO> generateAdaptiveTest(
            @RequestBody GenerateAdaptiveTestRequest request) {
        log.info("Generating adaptive test: {}", request);

        try {
            TestWithQuestionsDTO result = adaptiveTestService.generateAdaptiveTest(request);
            return RespResult.success("自适应测验生成成功", result);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return RespResult.error(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to generate adaptive test", e);
            return RespResult.error("自适应测验生成失败: " + e.getMessage());
        }
    }

    /**
     * 按单个知识点生成自适应混合难度测验
     * POST /api/ai-test/generate-adaptive-by-kp
     *
     * 请求示例：
     * {
     *   "studentId": 1,
     *   "knowledgePointId": 100,
     *   "questionCount": 5,
     *   "gradeId": 3            // 可选
     * }
     *
     * @param request 自适应测验请求
     * @return 测试详情（包含混合难度题目列表）
     */
    @PostMapping("/generate-adaptive-by-kp")
    public RespResult<TestWithQuestionsDTO> generateAdaptiveTestByKnowledgePoint(
            @RequestBody GenerateAdaptiveTestRequest request) {
        log.info("Generating adaptive test by knowledge point: {}", request);

        try {
            TestWithQuestionsDTO result = adaptiveTestService.generateAdaptiveTestByKnowledgePoint(request);
            return RespResult.success("自适应测验生成成功", result);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return RespResult.error(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to generate adaptive test by knowledge point", e);
            return RespResult.error("自适应测验生成失败: " + e.getMessage());
        }
    }
}
