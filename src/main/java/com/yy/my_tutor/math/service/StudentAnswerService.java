package com.yy.my_tutor.math.service;

import com.yy.my_tutor.math.domain.StudentAnswer;

import java.util.List;

/**
 * 学生答题记录服务接口
 */
public interface StudentAnswerService {
    
    /**
     * 查询用户的答题记录
     */
    List<StudentAnswer> findStudentAnswersByUserId(Integer userId);
    
    /**
     * 根据用户ID和问题ID查询答题记录
     */
    List<StudentAnswer> findStudentAnswersByUserAndQuestion(Integer userId, Integer questionId);
    
    /**
     * 根据知识点ID查询答题记录
     */
    List<StudentAnswer> findStudentAnswersByKnowledgePointId(Integer userId, Integer knowledgePointId);
    
    /**
     * 查询正确答题记录
     */
    List<StudentAnswer> findCorrectAnswersByUserId(Integer userId);
    
    /**
     * 查询错误答题记录
     */
    List<StudentAnswer> findIncorrectAnswersByUserId(Integer userId);
    
    /**
     * 提交答案
     */
    StudentAnswer submitAnswer(StudentAnswer studentAnswer);
    
    /**
     * 更新答题记录
     */
    StudentAnswer updateStudentAnswer(StudentAnswer studentAnswer);
    
    /**
     * 统计用户答题情况
     */
    int countAnswersByUserAndKnowledge(Integer userId, Integer knowledgePointId);
    
    /**
     * 统计用户正确答题数
     */
    int countCorrectAnswersByUserAndKnowledge(Integer userId, Integer knowledgePointId);
}
