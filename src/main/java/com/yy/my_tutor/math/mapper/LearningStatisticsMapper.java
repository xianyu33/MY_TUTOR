package com.yy.my_tutor.math.mapper;

import com.yy.my_tutor.math.domain.LearningStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学习统计Mapper接口
 */
@Mapper
public interface LearningStatisticsMapper {
    
    /**
     * 查询用户的学习统计
     */
    List<LearningStatistics> findLearningStatisticsByUserId(@Param("userId") Integer userId);
    
    /**
     * 根据用户ID和知识点ID查询学习统计
     */
    LearningStatistics findLearningStatisticsByUserAndKnowledge(@Param("userId") Integer userId, @Param("knowledgePointId") Integer knowledgePointId);
    
    /**
     * 根据掌握程度查询学习统计
     */
    List<LearningStatistics> findLearningStatisticsByMasteryLevel(@Param("userId") Integer userId, @Param("masteryLevel") Integer masteryLevel);
    
    /**
     * 插入学习统计
     */
    int insertLearningStatistics(LearningStatistics learningStatistics);
    
    /**
     * 更新学习统计
     */
    int updateLearningStatistics(LearningStatistics learningStatistics);
    
    /**
     * 删除学习统计（逻辑删除）
     */
    int deleteLearningStatistics(@Param("id") Integer id);
    
    /**
     * 更新学习统计信息
     */
    int updateStatisticsData(@Param("userId") Integer userId, @Param("knowledgePointId") Integer knowledgePointId, 
                           @Param("totalStudyTime") Integer totalStudyTime, @Param("totalQuestions") Integer totalQuestions, 
                           @Param("correctAnswers") Integer correctAnswers, @Param("accuracyRate") java.math.BigDecimal accuracyRate);
}
