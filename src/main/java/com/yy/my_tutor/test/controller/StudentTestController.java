package com.yy.my_tutor.test.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.test.domain.BatchAnswerRequest;
import com.yy.my_tutor.test.domain.GenerateTestRequest;
import com.yy.my_tutor.test.domain.StudentTestAnswer;
import com.yy.my_tutor.test.domain.StudentTestRecord;
import com.yy.my_tutor.test.domain.TestAnalysisResult;
import com.yy.my_tutor.test.domain.TestWithQuestionsDTO;
import com.yy.my_tutor.test.service.StudentTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 学生测试控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/student-test")
@CrossOrigin(origins = "*")
public class StudentTestController {
    
    @Autowired
    private StudentTestService studentTestService;
    
    /**
     * 为学生生成随机测试
     * @param request 生成测试请求
     *                优先使用categoryId和difficultyLevel（新方式）
     *                如果未提供，则使用knowledgePointIds或categoryIds（旧方式，向后兼容）
     * @return 测试记录
     */
    @PostMapping("/generate-random")
    public RespResult<TestWithQuestionsDTO> generateRandomTest(@RequestBody GenerateTestRequest request) {
        
        if (request.getStudentId() == null || request.getGradeId() == null) {
            return RespResult.error("学生ID和年级ID不能为空");
        }
        
        if (request.getQuestionCount() == null || request.getQuestionCount() <= 0) {
            return RespResult.error("题目数量必须大于0");
        }
        
        StudentTestRecord record;
        
        // 优先使用新的方式：根据知识类型和难度等级生成
        if (request.getCategoryId() != null && request.getDifficultyLevel() != null) {
            log.info("为学生 {} 生成随机测试（新方式），年级: {}, 分类ID: {}, 难度等级: {}, 题目数: {}", 
                    request.getStudentId(), request.getGradeId(), request.getCategoryId(), 
                    request.getDifficultyLevel(), request.getQuestionCount());
            
            record = studentTestService.generateRandomTestByCategoryAndDifficulty(
                    request.getStudentId(),
                    request.getGradeId(),
                    request.getCategoryId(),
                    request.getDifficultyLevel(),
                    request.getQuestionCount()
            );
        } else {
            // 向后兼容：使用旧方式
            log.info("为学生 {} 生成随机测试（旧方式），年级: {}, 知识点: {}, 分类: {}, 题目数: {}, 均匀难度分配: {}", 
                    request.getStudentId(), request.getGradeId(), request.getKnowledgePointIds(), 
                    request.getCategoryIds(), request.getQuestionCount(), request.getEqualDifficultyDistribution());
            
            record = studentTestService.generateRandomTestWithDistribution(
                    request.getStudentId(), 
                    request.getGradeId(), 
                    request.getKnowledgePointIds(),
                    request.getCategoryIds(),
                    request.getQuestionCount(),
                    request.getEqualDifficultyDistribution() != null && request.getEqualDifficultyDistribution()
            );
        }
        
        if (record != null) {
            // 获取测试详情（包含题目列表）
            TestWithQuestionsDTO testDetails = studentTestService.getTestWithQuestions(record.getId());
            return RespResult.success("生成随机测试成功", testDetails);
        } else {
            return RespResult.error("生成随机测试失败");
        }
    }
    
    /**
     * 开始测试
     */
    @PostMapping("/start")
    public RespResult<StudentTestRecord> startTest(
            @RequestParam Integer studentId,
            @RequestParam Integer testId) {
        
        log.info("学生 {} 开始测试 {}", studentId, testId);
        
        StudentTestRecord record = studentTestService.startTest(studentId, testId);
        if (record != null) {
            return RespResult.success("开始测试成功", record);
        } else {
            return RespResult.error("开始测试失败");
        }
    }
    
    /**
     * 提交答案
     */
    @PostMapping("/submit-answer")
    public RespResult<StudentTestAnswer> submitAnswer(
            @RequestParam Integer testRecordId,
            @RequestParam Integer questionId,
            @RequestParam String studentAnswer) {
        
        log.info("提交答案，测试记录: {}, 题目: {}, 答案: {}", testRecordId, questionId, studentAnswer);
        
        StudentTestAnswer answer = studentTestService.submitAnswer(testRecordId, questionId, studentAnswer);
        if (answer != null) {
            return RespResult.success("提交答案成功", answer);
        } else {
            return RespResult.error("提交答案失败");
        }
    }
    
    /**
     * 完成测试
     */
    @PostMapping("/complete")
    public RespResult<StudentTestRecord> completeTest(@RequestParam Integer testRecordId) {
        log.info("完成测试，记录ID: {}", testRecordId);
        
        StudentTestRecord record = studentTestService.completeTest(testRecordId);
        if (record != null) {
            return RespResult.success("完成测试成功", record);
        } else {
            return RespResult.error("完成测试失败");
        }
    }
    
    /**
     * 查询学生的测试历史记录
     */
    @GetMapping("/history/{studentId}")
    public RespResult<List<StudentTestRecord>> getStudentTestHistory(
            @PathVariable Integer studentId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        log.info("查询学生 {} 的测试历史记录，页码: {}, 每页: {}", studentId, page, size);
        
        List<StudentTestRecord> records = studentTestService.getStudentTestHistory(studentId, page, size);
        return RespResult.success(records);
    }
    
    /**
     * 查询测试的详细答题情况
     */
    @GetMapping("/answer-details/{testRecordId}")
    public RespResult<List<StudentTestAnswer>> getTestAnswerDetails(@PathVariable Integer testRecordId) {
        log.info("查询测试记录 {} 的答题详情", testRecordId);
        
        List<StudentTestAnswer> answers = studentTestService.getTestAnswerDetails(testRecordId);
        return RespResult.success(answers);
    }
    
    /**
     * 获取学生测试统计报表
     */
    @GetMapping("/statistics/{studentId}")
    public RespResult<Map<String, Object>> getStudentTestStatistics(
            @PathVariable Integer studentId,
            @RequestParam(required = false) Integer gradeId) {
        
        log.info("查询学生 {} 的测试统计，年级: {}", studentId, gradeId);
        
        Map<String, Object> statistics = studentTestService.getStudentTestStatistics(studentId, gradeId);
        return RespResult.success(statistics);
    }
    
    /**
     * 获取知识点掌握情况
     */
    @GetMapping("/knowledge-mastery/{studentId}")
    public RespResult<List<Map<String, Object>>> getKnowledgeMastery(@PathVariable Integer studentId) {
        log.info("查询学生 {} 的知识点掌握情况", studentId);
        
        List<Map<String, Object>> mastery = studentTestService.getKnowledgeMastery(studentId);
        return RespResult.success(mastery);
    }
    
    /**
     * 获取正在进行的测试
     */
    @GetMapping("/ongoing/{studentId}")
    public RespResult<List<StudentTestRecord>> getOngoingTests(@PathVariable Integer studentId) {
        log.info("查询学生 {} 正在进行的测试", studentId);
        
        List<StudentTestRecord> ongoingTests = studentTestService.getOngoingTests(studentId);
        return RespResult.success(ongoingTests);
    }
    
    /**
     * 批量提交答案并生成分析报告
     * @param request 批量答题请求
     * @return 测试分析结果（包含知识点得分和分析）
     */
    @PostMapping("/batch-submit")
    public RespResult<TestAnalysisResult> batchSubmitAnswers(@RequestBody BatchAnswerRequest request) {
        log.info("批量提交答案，测试记录ID: {}, 答题数量: {}", 
                request.getTestRecordId(), 
                request.getAnswers() != null ? request.getAnswers().size() : 0);
        
        if (request.getTestRecordId() == null) {
            return RespResult.error("测试记录ID不能为空");
        }
        
        if (request.getAnswers() == null || request.getAnswers().isEmpty()) {
            return RespResult.error("答题列表不能为空");
        }
        
        try {
            TestAnalysisResult result = studentTestService.batchSubmitAnswersAndAnalyze(request);
            if (result != null) {
                return RespResult.success("提交成功并生成分析报告", result);
            } else {
                return RespResult.error("提交失败");
            }
        } catch (Exception e) {
            log.error("批量提交答案时发生异常: {}", e.getMessage(), e);
            return RespResult.error("提交失败: " + e.getMessage());
        }
    }
}
