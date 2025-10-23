package com.yy.my_tutor.test.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.test.domain.StudentTestAnswer;
import com.yy.my_tutor.test.domain.StudentTestRecord;
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
     */
    @PostMapping("/generate-random")
    public RespResult<StudentTestRecord> generateRandomTest(
            @RequestParam Integer studentId,
            @RequestParam Integer gradeId,
            @RequestParam(defaultValue = "2") Integer difficultyLevel,
            @RequestParam(defaultValue = "10") Integer questionCount) {
        
        log.info("为学生 {} 生成随机测试，年级: {}, 难度: {}, 题目数: {}", studentId, gradeId, difficultyLevel, questionCount);
        
        StudentTestRecord record = studentTestService.generateRandomTestForStudent(studentId, gradeId, difficultyLevel, questionCount);
        if (record != null) {
            return RespResult.success("生成随机测试成功", record);
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
}
