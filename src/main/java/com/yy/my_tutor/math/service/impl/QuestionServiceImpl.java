package com.yy.my_tutor.math.service.impl;

import com.yy.my_tutor.math.domain.Question;
import com.yy.my_tutor.math.mapper.QuestionMapper;
import com.yy.my_tutor.math.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 问题服务实现类
 */
@Service
public class QuestionServiceImpl implements QuestionService {
    
    @Autowired
    private QuestionMapper questionMapper;
    
    @Override
    public List<Question> findAllQuestions() {
        return questionMapper.findAllQuestions();
    }
    
    @Override
    public Question findQuestionById(Integer id) {
        return questionMapper.findQuestionById(id);
    }
    
    @Override
    public List<Question> findQuestionsByKnowledgePointId(Integer knowledgePointId) {
        return questionMapper.findQuestionsByKnowledgePointId(knowledgePointId);
    }
    
    @Override
    public List<Question> findQuestionsByType(Integer questionType) {
        return questionMapper.findQuestionsByType(questionType);
    }
    
    @Override
    public List<Question> findQuestionsByDifficulty(Integer difficultyLevel) {
        return questionMapper.findQuestionsByDifficulty(difficultyLevel);
    }
    
    @Override
    public List<Question> findQuestionsByKnowledgeAndDifficulty(Integer knowledgePointId, Integer difficultyLevel) {
        return questionMapper.findQuestionsByKnowledgeAndDifficulty(knowledgePointId, difficultyLevel);
    }
    
    @Override
    public List<Question> findRandomQuestions(Integer knowledgePointId, Integer limit) {
        return questionMapper.findRandomQuestions(knowledgePointId, limit);
    }
    
    @Override
    public Question addQuestion(Question question) {
        Date now = new Date();
        question.setCreateAt(now);
        question.setUpdateAt(now);
        question.setDeleteFlag("N");
        
        int result = questionMapper.insertQuestion(question);
        return result > 0 ? question : null;
    }
    
    @Override
    public Question updateQuestion(Question question) {
        question.setUpdateAt(new Date());
        
        int result = questionMapper.updateQuestion(question);
        return result > 0 ? question : null;
    }
    
    @Override
    public boolean deleteQuestion(Integer id) {
        int result = questionMapper.deleteQuestion(id);
        return result > 0;
    }
}
