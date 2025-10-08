package com.yy.my_tutor.math.mapper;

import com.yy.my_tutor.math.domain.LearningProgress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学习进度Mapper接口
 */
@Mapper
public interface LearningProgressMapper {
    
    /**
     * 查询用户的学习进度
     */
    List<LearningProgress> findLearningProgressByUserId(@Param("userId") Integer userId);
    
    /**
     * 根据用户ID和知识点ID查询学习进度
     */
    LearningProgress findLearningProgressByUserAndKnowledge(@Param("userId") Integer userId, @Param("knowledgePointId") Integer knowledgePointId);
    
    /**
     * 根据知识点ID查询学习进度
     */
    List<LearningProgress> findLearningProgressByKnowledgePointId(@Param("knowledgePointId") Integer knowledgePointId);
    
    /**
     * 根据学习状态查询进度
     */
    List<LearningProgress> findLearningProgressByStatus(@Param("userId") Integer userId, @Param("progressStatus") Integer progressStatus);
    
    /**
     * 插入学习进度
     */
    int insertLearningProgress(LearningProgress learningProgress);
    
    /**
     * 更新学习进度
     */
    int updateLearningProgress(LearningProgress learningProgress);
    
    /**
     * 删除学习进度（逻辑删除）
     */
    int deleteLearningProgress(@Param("id") Integer id);
    
    /**
     * 更新学习进度状态
     */
    int updateProgressStatus(@Param("userId") Integer userId, @Param("knowledgePointId") Integer knowledgePointId, @Param("progressStatus") Integer progressStatus);
}
