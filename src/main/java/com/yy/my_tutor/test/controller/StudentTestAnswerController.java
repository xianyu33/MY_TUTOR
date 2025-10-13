package com.yy.my_tutor.test.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.test.domain.StudentTestAnswer;
import com.yy.my_tutor.test.service.StudentTestAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学生测试答题详情控制器
 */
@RestController
@RequestMapping("/api/test-answer")
@CrossOrigin(origins = "*")
public class StudentTestAnswerController {
    
    @Autowired
    private StudentTestAnswerService testAnswerService;
    
    /**
     * 查询测试记录的答题详情
     */
    @GetMapping("/record/{testRecordId}")
    public RespResult<List<StudentTestAnswer>> findAnswersByTestRecordId(@PathVariable Integer testRecordId) {
        List<StudentTestAnswer> answers = testAnswerService.findAnswersByTestRecordId(testRecordId);
        return RespResult.success(answers);
    }
    
    /**
     * 根据学生ID和题目ID查询答题记录
     */
    @GetMapping("/student/{studentId}/question/{questionId}")
    public RespResult<StudentTestAnswer> findAnswerByStudentAndQuestion(
            @PathVariable Integer studentId, @PathVariable Integer questionId) {
        StudentTestAnswer answer = testAnswerService.findAnswerByStudentAndQuestion(studentId, questionId);
        if (answer != null) {
            return RespResult.success(answer);
        } else {
            return RespResult.error("答题记录不存在");
        }
    }
    
    /**
     * 提交答案
     */
    @PostMapping("/submit")
    public RespResult<StudentTestAnswer> submitAnswer(@RequestBody StudentTestAnswer testAnswer) {
        StudentTestAnswer result = testAnswerService.submitAnswer(testAnswer);
        if (result != null) {
            return RespResult.success("答案提交成功", result);
        } else {
            return RespResult.error("答案提交失败");
        }
    }
    
    /**
     * 批量提交答案
     */
    @PostMapping("/batch-submit")
    public RespResult<List<StudentTestAnswer>> batchSubmitAnswers(@RequestBody List<StudentTestAnswer> answers) {
        List<StudentTestAnswer> result = testAnswerService.batchSubmitAnswers(answers);
        if (result != null && !result.isEmpty()) {
            return RespResult.success("批量提交成功", result);
        } else {
            return RespResult.error("批量提交失败");
        }
    }
    
    /**
     * 更新答题详情
     */
    @PutMapping("/update")
    public RespResult<StudentTestAnswer> updateTestAnswer(@RequestBody StudentTestAnswer testAnswer) {
        StudentTestAnswer result = testAnswerService.updateTestAnswer(testAnswer);
        if (result != null) {
            return RespResult.success("答题详情更新成功", result);
        } else {
            return RespResult.error("答题详情更新失败");
        }
    }
    
    /**
     * 删除答题详情
     */
    @DeleteMapping("/{id}")
    public RespResult<String> deleteTestAnswer(@PathVariable Integer id) {
        boolean result = testAnswerService.deleteTestAnswer(id);
        if (result) {
            return RespResult.success("答题详情删除成功");
        } else {
            return RespResult.error("答题详情删除失败");
        }
    }
}


