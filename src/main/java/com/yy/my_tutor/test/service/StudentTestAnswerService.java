package com.yy.my_tutor.test.service;

import com.yy.my_tutor.test.domain.StudentTestAnswer;

import java.util.List;

/**
 * 学生测试答题详情服务接口
 */
public interface StudentTestAnswerService {
    
    /**
     * 查询测试记录的答题详情
     */
    List<StudentTestAnswer> findAnswersByTestRecordId(Integer testRecordId);
    
    /**
     * 根据学生ID和题目ID查询答题记录
     */
    StudentTestAnswer findAnswerByStudentAndQuestion(Integer studentId, Integer questionId);
    
    /**
     * 提交答案
     */
    StudentTestAnswer submitAnswer(StudentTestAnswer testAnswer);
    
    /**
     * 批量提交答案
     */
    List<StudentTestAnswer> batchSubmitAnswers(List<StudentTestAnswer> answers);
    
    /**
     * 更新答题详情
     */
    StudentTestAnswer updateTestAnswer(StudentTestAnswer testAnswer);
    
    /**
     * 删除答题详情
     */
    boolean deleteTestAnswer(Integer id);
}


