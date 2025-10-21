package com.yy.my_tutor.math.service;

import com.yy.my_tutor.math.domain.LearningProgress;

import java.util.List;

/**
 * 学习进度服务接口
 */
public interface LearningProgressService {
    
    /**
     * 查询用户的学习进度
     */
    List<LearningProgress> findLearningProgressByUserId(Integer userId);
    
    /**
     * 根据用户ID和知识点ID查询学习进度
     */
    LearningProgress findLearningProgressByUserAndKnowledge(Integer userId, Integer knowledgePointId);
    
    /**
     * 根据知识点ID查询学习进度
     */
    List<LearningProgress> findLearningProgressByKnowledgePointId(Integer knowledgePointId);
    
    /**
     * 根据学习状态查询进度
     */
    List<LearningProgress> findLearningProgressByStatus(Integer userId, Integer progressStatus);
    
    /**
     * 开始学习知识点
     */
    LearningProgress startLearning(Integer userId, Integer knowledgePointId);
    
    /**
     * 更新学习进度
     */
    LearningProgress updateLearningProgress(LearningProgress learningProgress);
    
    /**
     * 完成学习知识点
     */
    LearningProgress completeLearning(Integer userId, Integer knowledgePointId);
    
    /**
     * 更新学习进度状态
     */
    boolean updateProgressStatus(Integer userId, Integer knowledgePointId, Integer progressStatus);
    
    /**
     * 插入学习进度
     */
    LearningProgress insertLearningProgress(LearningProgress learningProgress);
}
