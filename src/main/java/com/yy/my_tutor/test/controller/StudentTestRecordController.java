package com.yy.my_tutor.test.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.test.domain.QueryTestRecordRequest;
import com.yy.my_tutor.test.domain.StudentTestRecord;
import com.yy.my_tutor.test.service.StudentTestRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学生测试记录控制器
 */
@RestController
@RequestMapping("/api/student-test-record")
@CrossOrigin(origins = "*")
public class StudentTestRecordController {
    
    @Autowired
    private StudentTestRecordService testRecordService;
    
    /**
     * 查询学生的测试记录
     */
    @GetMapping("/student/{studentId}")
    public RespResult<List<StudentTestRecord>> findTestRecordsByStudentId(@PathVariable Integer studentId) {
        List<StudentTestRecord> records = testRecordService.findTestRecordsByStudentId(studentId);
        return RespResult.success(records);
    }
    
    /**
     * 根据测试ID查询记录
     */
    @GetMapping("/test/{testId}")
    public RespResult<List<StudentTestRecord>> findTestRecordsByTestId(@PathVariable Integer testId) {
        List<StudentTestRecord> records = testRecordService.findTestRecordsByTestId(testId);
        return RespResult.success(records);
    }
    
    /**
     * 根据学生ID和测试ID查询记录
     */
    @GetMapping("/student/{studentId}/test/{testId}")
    public RespResult<StudentTestRecord> findTestRecordByStudentAndTest(
            @PathVariable Integer studentId, @PathVariable Integer testId) {
        StudentTestRecord record = testRecordService.findTestRecordByStudentAndTest(studentId, testId);
        if (record != null) {
            return RespResult.success(record);
        } else {
            return RespResult.error("测试记录不存在");
        }
    }
    
    /**
     * 查询进行中的测试记录
     */
    @GetMapping("/student/{studentId}/ongoing")
    public RespResult<List<StudentTestRecord>> findOngoingTestRecords(@PathVariable Integer studentId) {
        List<StudentTestRecord> records = testRecordService.findOngoingTestRecords(studentId);
        return RespResult.success(records);
    }
    
    /**
     * 根据学生ID和知识点ID查询历史测试记录
     */
    @GetMapping("/student/{studentId}/knowledge-point/{knowledgePointId}")
    public RespResult<List<StudentTestRecord>> findTestRecordsByStudentAndKnowledgePoint(
            @PathVariable Integer studentId,
            @PathVariable Integer knowledgePointId) {
        List<StudentTestRecord> records = testRecordService.findTestRecordsByStudentIdAndKnowledgePointIds(
                studentId, java.util.Collections.singletonList(knowledgePointId));
        return RespResult.success(records);
    }

    /**
     * 根据学生ID和知识类型、难度等级查询测试记录
     * 使用新的方式：根据知识类型和难度等级查询
     */
    @PostMapping("/student/{studentId}/knowledge-points")
    public RespResult<List<StudentTestRecord>> findTestRecordsByStudentIdAndKnowledgePoints(
            @PathVariable Integer studentId,
            @RequestBody QueryTestRecordRequest request) {
        
        if (request == null) {
            return RespResult.error("请求参数不能为空");
        }
        
        if (request.getCategoryId() == null || request.getDifficultyLevel() == null) {
            return RespResult.error("知识类型ID和难度等级不能为空");
        }
        
        List<StudentTestRecord> records = testRecordService.findTestRecordsByCategoryAndDifficulty(
                studentId, request.getCategoryId(), request.getDifficultyLevel());
        return RespResult.success(records);
    }
    
    /**
     * 开始测试
     */
    @PostMapping("/start")
    public RespResult<StudentTestRecord> startTest(@RequestParam Integer studentId, @RequestParam Integer testId) {
        StudentTestRecord record = testRecordService.startTest(studentId, testId);
        if (record != null) {
            return RespResult.success("测试开始", record);
        } else {
            return RespResult.error("测试开始失败");
        }
    }
    
    /**
     * 提交测试
     */
    @PostMapping("/submit/{testRecordId}")
    public RespResult<StudentTestRecord> submitTest(@PathVariable Integer testRecordId) {
        StudentTestRecord record = testRecordService.submitTest(testRecordId);
        if (record != null) {
            return RespResult.success("测试提交成功", record);
        } else {
            return RespResult.error("测试提交失败");
        }
    }
    
    /**
     * 更新测试记录
     */
    @PutMapping("/update")
    public RespResult<StudentTestRecord> updateTestRecord(@RequestBody StudentTestRecord testRecord) {
        StudentTestRecord result = testRecordService.updateTestRecord(testRecord);
        if (result != null) {
            return RespResult.success("测试记录更新成功", result);
        } else {
            return RespResult.error("测试记录更新失败");
        }
    }
    
    /**
     * 删除测试记录
     */
    @DeleteMapping("/{id}")
    public RespResult<String> deleteTestRecord(@PathVariable Integer id) {
        boolean result = testRecordService.deleteTestRecord(id);
        if (result) {
            return RespResult.success("测试记录删除成功");
        } else {
            return RespResult.error("测试记录删除失败");
        }
    }
}


