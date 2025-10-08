package com.yy.my_tutor.math.service;

import com.yy.my_tutor.math.domain.LearningContent;

import java.util.List;

/**
 * 学习内容记录服务接口
 */
public interface LearningContentService {
    
    /**
     * 查询用户的学习内容记录
     */
    List<LearningContent> findLearningContentByUserId(Integer userId);
    
    /**
     * 根据用户ID和知识点ID查询学习内容
     */
    List<LearningContent> findLearningContentByUserAndKnowledge(Integer userId, Integer knowledgePointId);
    
    /**
     * 根据内容类型查询学习内容
     */
    List<LearningContent> findLearningContentByType(Integer userId, Integer contentType);
    
    /**
     * 根据完成状态查询学习内容
     */
    List<LearningContent> findLearningContentByCompletionStatus(Integer userId, Integer completionStatus);
    
    /**
     * 新增学习内容记录
     */
    LearningContent addLearningContent(LearningContent learningContent);
    
    /**
     * 更新学习内容记录
     */
    LearningContent updateLearningContent(LearningContent learningContent);
    
    /**
     * 完成学习内容
     */
    LearningContent completeLearningContent(Integer contentId, Integer score, String feedback);
}
