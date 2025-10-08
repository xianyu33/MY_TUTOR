package com.yy.my_tutor.math.service;

import com.yy.my_tutor.math.domain.Question;

import java.util.List;

/**
 * 问题服务接口
 */
public interface QuestionService {
    
    /**
     * 查询所有问题
     */
    List<Question> findAllQuestions();
    
    /**
     * 根据ID查询问题
     */
    Question findQuestionById(Integer id);
    
    /**
     * 根据知识点ID查询问题
     */
    List<Question> findQuestionsByKnowledgePointId(Integer knowledgePointId);
    
    /**
     * 根据题目类型查询问题
     */
    List<Question> findQuestionsByType(Integer questionType);
    
    /**
     * 根据难度等级查询问题
     */
    List<Question> findQuestionsByDifficulty(Integer difficultyLevel);
    
    /**
     * 根据知识点和难度查询问题
     */
    List<Question> findQuestionsByKnowledgeAndDifficulty(Integer knowledgePointId, Integer difficultyLevel);
    
    /**
     * 随机获取指定数量的题目
     */
    List<Question> findRandomQuestions(Integer knowledgePointId, Integer limit);
    
    /**
     * 新增问题
     */
    Question addQuestion(Question question);
    
    /**
     * 更新问题
     */
    Question updateQuestion(Question question);
    
    /**
     * 删除问题
     */
    boolean deleteQuestion(Integer id);
}
