package com.yy.my_tutor.math.service.impl;

import com.yy.my_tutor.math.domain.LearningProgress;
import com.yy.my_tutor.math.mapper.LearningProgressMapper;
import com.yy.my_tutor.math.service.LearningProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 学习进度服务实现类
 */
@Service
public class LearningProgressServiceImpl implements LearningProgressService {
    
    @Autowired
    private LearningProgressMapper learningProgressMapper;
    
    @Override
    public List<LearningProgress> findLearningProgressByUserId(Integer userId) {
        return learningProgressMapper.findLearningProgressByUserId(userId);
    }
    
    @Override
    public LearningProgress findLearningProgressByUserAndKnowledge(Integer userId, Integer knowledgePointId) {
        return learningProgressMapper.findLearningProgressByUserAndKnowledge(userId, knowledgePointId);
    }
    
    @Override
    public List<LearningProgress> findLearningProgressByKnowledgePointId(Integer knowledgePointId) {
        return learningProgressMapper.findLearningProgressByKnowledgePointId(knowledgePointId);
    }
    
    @Override
    public List<LearningProgress> findLearningProgressByStatus(Integer userId, Integer progressStatus) {
        return learningProgressMapper.findLearningProgressByStatus(userId, progressStatus);
    }
    
    @Override
    public LearningProgress startLearning(Integer userId, Integer knowledgePointId) {
        // 检查是否已有学习进度记录
        LearningProgress existingProgress = findLearningProgressByUserAndKnowledge(userId, knowledgePointId);
        if (existingProgress != null) {
            // 更新为学习中状态
            existingProgress.setProgressStatus(2); // 学习中
            existingProgress.setStartTime(new Date());
            existingProgress.setLastStudyTime(new Date());
            existingProgress.setUpdateAt(new Date());
            
            int result = learningProgressMapper.updateLearningProgress(existingProgress);
            return result > 0 ? existingProgress : null;
        } else {
            // 创建新的学习进度记录
            LearningProgress newProgress = new LearningProgress();
            newProgress.setUserId(userId);
            newProgress.setKnowledgePointId(knowledgePointId);
            newProgress.setProgressStatus(2); // 学习中
            newProgress.setCompletionPercentage(BigDecimal.ZERO);
            newProgress.setStartTime(new Date());
            newProgress.setLastStudyTime(new Date());
            newProgress.setStudyDuration(0);
            newProgress.setCreateAt(new Date());
            newProgress.setUpdateAt(new Date());
            newProgress.setDeleteFlag("N");
            
            int result = learningProgressMapper.insertLearningProgress(newProgress);
            return result > 0 ? newProgress : null;
        }
    }
    
    @Override
    public LearningProgress updateLearningProgress(LearningProgress learningProgress) {
        learningProgress.setUpdateAt(new Date());
        
        int result = learningProgressMapper.updateLearningProgress(learningProgress);
        return result > 0 ? learningProgress : null;
    }
    
    @Override
    public LearningProgress completeLearning(Integer userId, Integer knowledgePointId) {
        LearningProgress progress = findLearningProgressByUserAndKnowledge(userId, knowledgePointId);
        if (progress != null) {
            progress.setProgressStatus(3); // 已完成
            progress.setCompletionPercentage(new BigDecimal("100.00"));
            progress.setCompleteTime(new Date());
            progress.setLastStudyTime(new Date());
            progress.setUpdateAt(new Date());
            
            int result = learningProgressMapper.updateLearningProgress(progress);
            return result > 0 ? progress : null;
        }
        return null;
    }
    
    @Override
    public boolean updateProgressStatus(Integer userId, Integer knowledgePointId, Integer progressStatus) {
        int result = learningProgressMapper.updateProgressStatus(userId, knowledgePointId, progressStatus);
        return result > 0;
    }
}
