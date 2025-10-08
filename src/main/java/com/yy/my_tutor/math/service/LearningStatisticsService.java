package com.yy.my_tutor.math.service;

import com.yy.my_tutor.math.domain.LearningStatistics;

import java.util.List;

/**
 * 学习统计服务接口
 */
public interface LearningStatisticsService {
    
    /**
     * 查询用户的学习统计
     */
    List<LearningStatistics> findLearningStatisticsByUserId(Integer userId);
    
    /**
     * 根据用户ID和知识点ID查询学习统计
     */
    LearningStatistics findLearningStatisticsByUserAndKnowledge(Integer userId, Integer knowledgePointId);
    
    /**
     * 根据掌握程度查询学习统计
     */
    List<LearningStatistics> findLearningStatisticsByMasteryLevel(Integer userId, Integer masteryLevel);
    
    /**
     * 更新学习统计信息
     */
    LearningStatistics updateLearningStatistics(Integer userId, Integer knowledgePointId);
    
    /**
     * 计算并更新掌握程度
     */
    LearningStatistics calculateMasteryLevel(Integer userId, Integer knowledgePointId);
}
