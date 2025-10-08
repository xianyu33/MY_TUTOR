package com.yy.my_tutor.math.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.math.domain.StudentAnswer;
import com.yy.my_tutor.math.service.StudentAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学生答题记录控制器
 */
@RestController
@RequestMapping("/api/math/answer")
@CrossOrigin(origins = "*")
public class StudentAnswerController {
    
    @Autowired
    private StudentAnswerService studentAnswerService;
    
    /**
     * 查询用户的答题记录
     */
    @GetMapping("/user/{userId}")
    public RespResult<List<StudentAnswer>> findStudentAnswersByUserId(@PathVariable Integer userId) {
        List<StudentAnswer> answers = studentAnswerService.findStudentAnswersByUserId(userId);
        return RespResult.success(answers);
    }
    
    /**
     * 根据用户ID和问题ID查询答题记录
     */
    @GetMapping("/user/{userId}/question/{questionId}")
    public RespResult<List<StudentAnswer>> findStudentAnswersByUserAndQuestion(
            @PathVariable Integer userId, @PathVariable Integer questionId) {
        List<StudentAnswer> answers = studentAnswerService.findStudentAnswersByUserAndQuestion(userId, questionId);
        return RespResult.success(answers);
    }
    
    /**
     * 根据知识点ID查询答题记录
     */
    @GetMapping("/user/{userId}/knowledge/{knowledgePointId}")
    public RespResult<List<StudentAnswer>> findStudentAnswersByKnowledgePointId(
            @PathVariable Integer userId, @PathVariable Integer knowledgePointId) {
        List<StudentAnswer> answers = studentAnswerService.findStudentAnswersByKnowledgePointId(userId, knowledgePointId);
        return RespResult.success(answers);
    }
    
    /**
     * 查询正确答题记录
     */
    @GetMapping("/user/{userId}/correct")
    public RespResult<List<StudentAnswer>> findCorrectAnswersByUserId(@PathVariable Integer userId) {
        List<StudentAnswer> answers = studentAnswerService.findCorrectAnswersByUserId(userId);
        return RespResult.success(answers);
    }
    
    /**
     * 查询错误答题记录
     */
    @GetMapping("/user/{userId}/incorrect")
    public RespResult<List<StudentAnswer>> findIncorrectAnswersByUserId(@PathVariable Integer userId) {
        List<StudentAnswer> answers = studentAnswerService.findIncorrectAnswersByUserId(userId);
        return RespResult.success(answers);
    }
    
    /**
     * 提交答案
     */
    @PostMapping("/submit")
    public RespResult<StudentAnswer> submitAnswer(@RequestBody StudentAnswer studentAnswer) {
        StudentAnswer result = studentAnswerService.submitAnswer(studentAnswer);
        if (result != null) {
            return RespResult.success("答案提交成功", result);
        } else {
            return RespResult.error("答案提交失败");
        }
    }
    
    /**
     * 更新答题记录
     */
    @PutMapping("/update")
    public RespResult<StudentAnswer> updateStudentAnswer(@RequestBody StudentAnswer studentAnswer) {
        StudentAnswer result = studentAnswerService.updateStudentAnswer(studentAnswer);
        if (result != null) {
            return RespResult.success("答题记录更新成功", result);
        } else {
            return RespResult.error("答题记录更新失败");
        }
    }
    
    /**
     * 统计用户答题情况
     */
    @GetMapping("/count/user/{userId}/knowledge/{knowledgePointId}")
    public RespResult<Integer> countAnswersByUserAndKnowledge(@PathVariable Integer userId, 
                                                            @PathVariable Integer knowledgePointId) {
        int count = studentAnswerService.countAnswersByUserAndKnowledge(userId, knowledgePointId);
        return RespResult.success(count);
    }
    
    /**
     * 统计用户正确答题数
     */
    @GetMapping("/count/correct/user/{userId}/knowledge/{knowledgePointId}")
    public RespResult<Integer> countCorrectAnswersByUserAndKnowledge(@PathVariable Integer userId, 
                                                                    @PathVariable Integer knowledgePointId) {
        int count = studentAnswerService.countCorrectAnswersByUserAndKnowledge(userId, knowledgePointId);
        return RespResult.success(count);
    }
}
