package com.yy.my_tutor.test.service.impl;

import com.yy.my_tutor.test.domain.StudentTestAnswer;
import com.yy.my_tutor.test.mapper.StudentTestAnswerMapper;
import com.yy.my_tutor.test.service.StudentTestAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 学生测试答题详情服务实现类
 */
@Service
public class StudentTestAnswerServiceImpl implements StudentTestAnswerService {
    
    @Autowired
    private StudentTestAnswerMapper testAnswerMapper;
    
    @Override
    public List<StudentTestAnswer> findAnswersByTestRecordId(Integer testRecordId) {
        return testAnswerMapper.findAnswersByTestRecordId(testRecordId);
    }
    
    @Override
    public StudentTestAnswer findAnswerByStudentAndQuestion(Integer studentId, Integer questionId) {
        return testAnswerMapper.findAnswerByStudentAndQuestion(studentId, questionId);
    }
    
    @Override
    public StudentTestAnswer submitAnswer(StudentTestAnswer testAnswer) {
        testAnswer.setAnswerTime(new Date());
        testAnswer.setCreateAt(new Date());
        
        int result = testAnswerMapper.insertTestAnswer(testAnswer);
        return result > 0 ? testAnswer : null;
    }
    
    @Override
    public List<StudentTestAnswer> batchSubmitAnswers(List<StudentTestAnswer> answers) {
        Date now = new Date();
        for (StudentTestAnswer answer : answers) {
            answer.setAnswerTime(now);
            answer.setCreateAt(now);
        }
        
        int result = testAnswerMapper.batchInsertTestAnswers(answers);
        return result > 0 ? answers : null;
    }
    
    @Override
    public StudentTestAnswer updateTestAnswer(StudentTestAnswer testAnswer) {
        testAnswer.setAnswerTime(new Date());
        
        int result = testAnswerMapper.updateTestAnswer(testAnswer);
        return result > 0 ? testAnswer : null;
    }
    
    @Override
    public boolean deleteTestAnswer(Integer id) {
        int result = testAnswerMapper.deleteTestAnswer(id);
        return result > 0;
    }
}
