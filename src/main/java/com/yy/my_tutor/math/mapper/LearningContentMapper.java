package com.yy.my_tutor.math.mapper;

import com.yy.my_tutor.math.domain.LearningContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学习内容记录Mapper接口
 */
@Mapper
public interface LearningContentMapper {
    
    /**
     * 查询用户的学习内容记录
     */
    List<LearningContent> findLearningContentByUserId(@Param("userId") Integer userId);
    
    /**
     * 根据用户ID和知识点ID查询学习内容
     */
    List<LearningContent> findLearningContentByUserAndKnowledge(@Param("userId") Integer userId, @Param("knowledgePointId") Integer knowledgePointId);
    
    /**
     * 根据内容类型查询学习内容
     */
    List<LearningContent> findLearningContentByType(@Param("userId") Integer userId, @Param("contentType") Integer contentType);
    
    /**
     * 根据完成状态查询学习内容
     */
    List<LearningContent> findLearningContentByCompletionStatus(@Param("userId") Integer userId, @Param("completionStatus") Integer completionStatus);
    
    /**
     * 插入学习内容记录
     */
    int insertLearningContent(LearningContent learningContent);
    
    /**
     * 更新学习内容记录
     */
    int updateLearningContent(LearningContent learningContent);
    
    /**
     * 删除学习内容记录（逻辑删除）
     */
    int deleteLearningContent(@Param("id") Integer id);
}
