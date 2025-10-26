package com.yy.my_tutor.math.service.impl;

import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.math.mapper.KnowledgePointMapper;
import com.yy.my_tutor.math.service.KnowledgePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 知识点服务实现类
 */
@Service
public class KnowledgePointServiceImpl implements KnowledgePointService {
    
    @Autowired
    private KnowledgePointMapper knowledgePointMapper;
    
    @Override
    public List<KnowledgePoint> findAllKnowledgePoints() {
        return knowledgePointMapper.findAllKnowledgePoints();
    }
    
    @Override
    public KnowledgePoint findKnowledgePointById(Integer id) {
        return knowledgePointMapper.findKnowledgePointById(id);
    }
    
    @Override
    public List<KnowledgePoint> findKnowledgePointsByGradeId(Integer gradeId) {
        return knowledgePointMapper.findKnowledgePointsByGradeId(gradeId);
    }
    
    @Override
    public List<KnowledgePoint> findKnowledgePointsByCategoryId(Integer categoryId) {
        return knowledgePointMapper.findKnowledgePointsByCategoryId(categoryId);
    }
    
    @Override
    public List<KnowledgePoint> findKnowledgePointsByGradeAndCategory(Integer gradeId, Integer categoryId) {
        return knowledgePointMapper.findKnowledgePointsByGradeAndCategory(gradeId, categoryId);
    }
    
    @Override
    public KnowledgePoint findKnowledgePointByCode(String pointCode) {
        return knowledgePointMapper.findKnowledgePointByCode(pointCode);
    }
    
    @Override
    public List<KnowledgePoint> findKnowledgePointsByDifficulty(Integer difficultyLevel) {
        return knowledgePointMapper.findKnowledgePointsByDifficulty(difficultyLevel);
    }
    
    @Override
    public KnowledgePoint addKnowledgePoint(KnowledgePoint knowledgePoint) {
        Date now = new Date();
        knowledgePoint.setCreateAt(now);
        knowledgePoint.setUpdateAt(now);
        knowledgePoint.setDeleteFlag("N");
        
        int result = knowledgePointMapper.insertKnowledgePoint(knowledgePoint);
        return result > 0 ? knowledgePoint : null;
    }
    
    @Override
    public KnowledgePoint updateKnowledgePoint(KnowledgePoint knowledgePoint) {
        knowledgePoint.setUpdateAt(new Date());
        
        int result = knowledgePointMapper.updateKnowledgePoint(knowledgePoint);
        return result > 0 ? knowledgePoint : null;
    }
    
    @Override
    public boolean deleteKnowledgePoint(Integer id) {
        int result = knowledgePointMapper.deleteKnowledgePoint(id);
        return result > 0;
    }
    
    @Override
    public List<KnowledgePoint> findKnowledgePointsByCategoryIds(List<Integer> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return knowledgePointMapper.findAllKnowledgePoints();
        }
        
        // 如果只有一个分类ID，使用原有方法
        if (categoryIds.size() == 1) {
            return knowledgePointMapper.findKnowledgePointsByCategoryId(categoryIds.get(0));
        }
        
        // 多个分类ID，需要合并结果
        List<KnowledgePoint> allPoints = new java.util.ArrayList<>();
        for (Integer categoryId : categoryIds) {
            List<KnowledgePoint> points = knowledgePointMapper.findKnowledgePointsByCategoryId(categoryId);
            if (points != null) {
                allPoints.addAll(points);
            }
        }
        
        return allPoints;
    }
}
