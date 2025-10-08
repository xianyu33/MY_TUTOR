package com.yy.my_tutor.math.service.impl;

import com.yy.my_tutor.math.domain.StudentAnswer;
import com.yy.my_tutor.math.mapper.StudentAnswerMapper;
import com.yy.my_tutor.math.service.StudentAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 学生答题记录服务实现类
 */
@Service
public class StudentAnswerServiceImpl implements StudentAnswerService {
    
    @Autowired
    private StudentAnswerMapper studentAnswerMapper;
    
    @Override
    public List<StudentAnswer> findStudentAnswersByUserId(Integer userId) {
        return studentAnswerMapper.findStudentAnswersByUserId(userId);
    }
    
    @Override
    public List<StudentAnswer> findStudentAnswersByUserAndQuestion(Integer userId, Integer questionId) {
        return studentAnswerMapper.findStudentAnswersByUserAndQuestion(userId, questionId);
    }
    
    @Override
    public List<StudentAnswer> findStudentAnswersByKnowledgePointId(Integer userId, Integer knowledgePointId) {
        return studentAnswerMapper.findStudentAnswersByKnowledgePointId(userId, knowledgePointId);
    }
    
    @Override
    public List<StudentAnswer> findCorrectAnswersByUserId(Integer userId) {
        return studentAnswerMapper.findCorrectAnswersByUserId(userId);
    }
    
    @Override
    public List<StudentAnswer> findIncorrectAnswersByUserId(Integer userId) {
        return studentAnswerMapper.findIncorrectAnswersByUserId(userId);
    }
    
    @Override
    public StudentAnswer submitAnswer(StudentAnswer studentAnswer) {
        Date now = new Date();
        studentAnswer.setAnswerTime(now);
        studentAnswer.setCreateAt(now);
        studentAnswer.setUpdateAt(now);
        studentAnswer.setDeleteFlag("N");
        
        // 计算尝试次数
        List<StudentAnswer> existingAnswers = findStudentAnswersByUserAndQuestion(
            studentAnswer.getUserId(), studentAnswer.getQuestionId());
        studentAnswer.setAttemptCount(existingAnswers.size() + 1);
        
        int result = studentAnswerMapper.insertStudentAnswer(studentAnswer);
        return result > 0 ? studentAnswer : null;
    }
    
    @Override
    public StudentAnswer updateStudentAnswer(StudentAnswer studentAnswer) {
        studentAnswer.setUpdateAt(new Date());
        
        int result = studentAnswerMapper.updateStudentAnswer(studentAnswer);
        return result > 0 ? studentAnswer : null;
    }
    
    @Override
    public int countAnswersByUserAndKnowledge(Integer userId, Integer knowledgePointId) {
        return studentAnswerMapper.countAnswersByUserAndKnowledge(userId, knowledgePointId);
    }
    
    @Override
    public int countCorrectAnswersByUserAndKnowledge(Integer userId, Integer knowledgePointId) {
        return studentAnswerMapper.countCorrectAnswersByUserAndKnowledge(userId, knowledgePointId);
    }
}
