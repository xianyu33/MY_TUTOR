package com.yy.my_tutor.math.mapper;

import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.user.domain.KnowledgePointWithProgress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 知识点Mapper接口
 */
@Mapper
public interface KnowledgePointMapper {
    
    /**
     * 查询所有知识点
     */
    List<KnowledgePoint> findAllKnowledgePoints();
    
    /**
     * 根据ID查询知识点
     */
    KnowledgePoint findKnowledgePointById(@Param("id") Integer id);
    
    /**
     * 根据年级ID查询知识点
     */
    List<KnowledgePoint> findKnowledgePointsByGradeId(@Param("gradeId") Integer gradeId);
    
    /**
     * 根据分类ID查询知识点
     */
    List<KnowledgePoint> findKnowledgePointsByCategoryId(@Param("categoryId") Integer categoryId);
    
    /**
     * 根据年级和分类查询知识点
     */
    List<KnowledgePoint> findKnowledgePointsByGradeAndCategory(@Param("gradeId") Integer gradeId, @Param("categoryId") Integer categoryId);
    
    /**
     * 根据编码查询知识点
     */
    KnowledgePoint findKnowledgePointByCode(@Param("pointCode") String pointCode);
    
    /**
     * 根据难度等级查询知识点
     */
    List<KnowledgePoint> findKnowledgePointsByDifficulty(@Param("difficultyLevel") Integer difficultyLevel);
    
    /**
     * 根据学生ID、年级ID和分类ID查询知识点列表（包含学习进度）
     */
    List<KnowledgePointWithProgress> findKnowledgePointsWithProgress(
            @Param("userId") Integer userId, 
            @Param("gradeId") Integer gradeId, 
            @Param("categoryId") Integer categoryId,
            @Param("knowledgePointId") Integer knowledgePointId);
    
    /**
     * 插入知识点
     */
    int insertKnowledgePoint(KnowledgePoint knowledgePoint);
    
    /**
     * 更新知识点
     */
    int updateKnowledgePoint(KnowledgePoint knowledgePoint);
    
    /**
     * 删除知识点（逻辑删除）
     */
    int deleteKnowledgePoint(@Param("id") Integer id);

    /**
     * 根据分类ID和难度等级查询知识点
     */
    List<KnowledgePoint> findKnowledgePointsByCategoryAndDifficulty(
            @Param("categoryId") Integer categoryId,
            @Param("difficultyLevel") Integer difficultyLevel);
}
