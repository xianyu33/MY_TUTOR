package com.yy.my_tutor.math.service.impl;

import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.math.mapper.KnowledgePointMapper;
import com.yy.my_tutor.math.service.KnowledgePointService;
import com.yy.my_tutor.test.job.QuestionPoolFillJob;
import com.yy.my_tutor.user.domain.KnowledgePointWithProgress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 知识点服务实现类
 */
@Slf4j
@Service
public class KnowledgePointServiceImpl implements KnowledgePointService {

    private static final int PREFILL_TARGET = 20;
    private static final int PREFILL_BATCH_SIZE = 5;

    @Autowired
    private KnowledgePointMapper knowledgePointMapper;

    @Resource
    private QuestionPoolFillJob questionPoolFillJob;
    
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
    public List<KnowledgePointWithProgress> findKnowledgePointsWithProgress(Integer userId, Integer gradeId, Integer categoryId, Integer knowledgePointId) {
        return knowledgePointMapper.findKnowledgePointsWithProgress(userId, gradeId, categoryId, knowledgePointId);
    }
    
    @Override
    public KnowledgePoint addKnowledgePoint(KnowledgePoint knowledgePoint) {
        Date now = new Date();
        knowledgePoint.setCreateAt(now);
        knowledgePoint.setUpdateAt(now);
        knowledgePoint.setDeleteFlag("N");

        int result = knowledgePointMapper.insertKnowledgePoint(knowledgePoint);
        if (result > 0) {
            // 新增知识点后，异步预填充3个难度级别的题目
            for (int difficulty : Arrays.asList(1, 2, 3)) {
                questionPoolFillJob.asyncFillForKnowledgePoint(
                        knowledgePoint.getId(), difficulty, PREFILL_TARGET, PREFILL_BATCH_SIZE);
            }
            log.info("新增知识点[{}]，已触发题库异步预填充", knowledgePoint.getPointName());
            return knowledgePoint;
        }
        return null;
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

    @Override
    public List<KnowledgePoint> findKnowledgePointsByCategoryAndDifficulty(Integer categoryId, Integer difficultyLevel) {
        return knowledgePointMapper.findKnowledgePointsByCategoryAndDifficulty(categoryId, difficultyLevel);
    }
}
