package com.yy.my_tutor.math.service.impl;

import com.yy.my_tutor.math.domain.LearningContent;
import com.yy.my_tutor.math.mapper.LearningContentMapper;
import com.yy.my_tutor.math.service.LearningContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 学习内容记录服务实现类
 */
@Service
public class LearningContentServiceImpl implements LearningContentService {
    
    @Autowired
    private LearningContentMapper learningContentMapper;
    
    @Override
    public List<LearningContent> findLearningContentByUserId(Integer userId) {
        return learningContentMapper.findLearningContentByUserId(userId);
    }
    
    @Override
    public List<LearningContent> findLearningContentByUserAndKnowledge(Integer userId, Integer knowledgePointId) {
        return learningContentMapper.findLearningContentByUserAndKnowledge(userId, knowledgePointId);
    }
    
    @Override
    public List<LearningContent> findLearningContentByType(Integer userId, Integer contentType) {
        return learningContentMapper.findLearningContentByType(userId, contentType);
    }
    
    @Override
    public List<LearningContent> findLearningContentByCompletionStatus(Integer userId, Integer completionStatus) {
        return learningContentMapper.findLearningContentByCompletionStatus(userId, completionStatus);
    }
    
    @Override
    public LearningContent addLearningContent(LearningContent learningContent) {
        Date now = new Date();
        learningContent.setCreateAt(now);
        learningContent.setUpdateAt(now);
        learningContent.setDeleteFlag("N");
        
        int result = learningContentMapper.insertLearningContent(learningContent);
        return result > 0 ? learningContent : null;
    }
    
    @Override
    public LearningContent updateLearningContent(LearningContent learningContent) {
        learningContent.setUpdateAt(new Date());
        
        int result = learningContentMapper.updateLearningContent(learningContent);
        return result > 0 ? learningContent : null;
    }
    
    @Override
    public LearningContent completeLearningContent(Integer contentId, Integer score, String feedback) {
        LearningContent content = new LearningContent();
        content.setId(contentId);
        content.setCompletionStatus(2); // 已完成
        content.setScore(new BigDecimal(score));
        content.setFeedback(feedback);
        content.setUpdateAt(new Date());
        
        int result = learningContentMapper.updateLearningContent(content);
        return result > 0 ? content : null;
    }
}
