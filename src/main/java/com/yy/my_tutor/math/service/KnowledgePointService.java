package com.yy.my_tutor.math.service;

import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.user.domain.KnowledgePointWithProgress;

import java.util.List;

/**
 * 知识点服务接口
 */
public interface KnowledgePointService {
    
    /**
     * 查询所有知识点
     */
    List<KnowledgePoint> findAllKnowledgePoints();
    
    /**
     * 根据ID查询知识点
     */
    KnowledgePoint findKnowledgePointById(Integer id);
    
    /**
     * 根据年级ID查询知识点
     */
    List<KnowledgePoint> findKnowledgePointsByGradeId(Integer gradeId);
    
    /**
     * 根据分类ID查询知识点
     */
    List<KnowledgePoint> findKnowledgePointsByCategoryId(Integer categoryId);
    
    /**
     * 根据年级和分类查询知识点
     */
    List<KnowledgePoint> findKnowledgePointsByGradeAndCategory(Integer gradeId, Integer categoryId);
    
    /**
     * 根据编码查询知识点
     */
    KnowledgePoint findKnowledgePointByCode(String pointCode);
    
    /**
     * 根据难度等级查询知识点
     */
    List<KnowledgePoint> findKnowledgePointsByDifficulty(Integer difficultyLevel);
    
    /**
     * 根据学生ID、年级ID和分类ID查询知识点列表（包含学习进度）
     */
    List<KnowledgePointWithProgress> findKnowledgePointsWithProgress(Integer userId, Integer gradeId, Integer categoryId);
    
    /**
     * 新增知识点
     */
    KnowledgePoint addKnowledgePoint(KnowledgePoint knowledgePoint);
    
    /**
     * 更新知识点
     */
    KnowledgePoint updateKnowledgePoint(KnowledgePoint knowledgePoint);
    
    /**
     * 删除知识点
     */
    boolean deleteKnowledgePoint(Integer id);
    
    /**
     * 根据分类ID列表查询知识点
     */
    List<KnowledgePoint> findKnowledgePointsByCategoryIds(List<Integer> categoryIds);

    /**
     * 根据分类ID和难度等级查询知识点
     */
    List<KnowledgePoint> findKnowledgePointsByCategoryAndDifficulty(Integer categoryId, Integer difficultyLevel);
}
