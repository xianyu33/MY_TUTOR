package com.yy.my_tutor.math.service.impl;

import com.yy.my_tutor.math.domain.LearningStatistics;
import com.yy.my_tutor.math.mapper.LearningStatisticsMapper;
import com.yy.my_tutor.math.service.LearningStatisticsService;
import com.yy.my_tutor.math.service.StudentAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * 学习统计服务实现类
 */
@Service
public class LearningStatisticsServiceImpl implements LearningStatisticsService {
    
    @Autowired
    private LearningStatisticsMapper learningStatisticsMapper;
    
    @Autowired
    private StudentAnswerService studentAnswerService;
    
    @Override
    public List<LearningStatistics> findLearningStatisticsByUserId(Integer userId) {
        return learningStatisticsMapper.findLearningStatisticsByUserId(userId);
    }
    
    @Override
    public LearningStatistics findLearningStatisticsByUserAndKnowledge(Integer userId, Integer knowledgePointId) {
        return learningStatisticsMapper.findLearningStatisticsByUserAndKnowledge(userId, knowledgePointId);
    }
    
    @Override
    public List<LearningStatistics> findLearningStatisticsByMasteryLevel(Integer userId, Integer masteryLevel) {
        return learningStatisticsMapper.findLearningStatisticsByMasteryLevel(userId, masteryLevel);
    }
    
    @Override
    public LearningStatistics updateLearningStatistics(Integer userId, Integer knowledgePointId) {
        // 获取答题统计
        int totalQuestions = studentAnswerService.countAnswersByUserAndKnowledge(userId, knowledgePointId);
        int correctAnswers = studentAnswerService.countCorrectAnswersByUserAndKnowledge(userId, knowledgePointId);
        
        // 计算正确率
        BigDecimal accuracyRate = BigDecimal.ZERO;
        if (totalQuestions > 0) {
            accuracyRate = new BigDecimal(correctAnswers)
                .divide(new BigDecimal(totalQuestions), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
        }
        
        // 查找现有统计记录
        LearningStatistics existingStats = findLearningStatisticsByUserAndKnowledge(userId, knowledgePointId);
        
        if (existingStats != null) {
            // 更新现有记录
            existingStats.setTotalQuestions(totalQuestions);
            existingStats.setCorrectAnswers(correctAnswers);
            existingStats.setAccuracyRate(accuracyRate);
            existingStats.setUpdateAt(new Date());
            
            int result = learningStatisticsMapper.updateLearningStatistics(existingStats);
            return result > 0 ? existingStats : null;
        } else {
            // 创建新记录
            LearningStatistics newStats = new LearningStatistics();
            newStats.setUserId(userId);
            newStats.setKnowledgePointId(knowledgePointId);
            newStats.setTotalStudyTime(0);
            newStats.setTotalQuestions(totalQuestions);
            newStats.setCorrectAnswers(correctAnswers);
            newStats.setAccuracyRate(accuracyRate);
            newStats.setMasteryLevel(1); // 默认未掌握
            newStats.setLastStudyDate(new Date());
            newStats.setStudyDays(0);
            newStats.setCreateAt(new Date());
            newStats.setUpdateAt(new Date());
            newStats.setDeleteFlag("N");
            
            int result = learningStatisticsMapper.insertLearningStatistics(newStats);
            return result > 0 ? newStats : null;
        }
    }
    
    @Override
    public LearningStatistics calculateMasteryLevel(Integer userId, Integer knowledgePointId) {
        LearningStatistics stats = findLearningStatisticsByUserAndKnowledge(userId, knowledgePointId);
        if (stats == null) {
            return null;
        }
        
        // 根据正确率和答题数量计算掌握程度
        BigDecimal accuracyRate = stats.getAccuracyRate();
        int totalQuestions = stats.getTotalQuestions();
        
        int masteryLevel = 1; // 默认未掌握
        
        if (totalQuestions >= 10) { // 至少答10道题
            if (accuracyRate.compareTo(new BigDecimal("90")) >= 0) {
                masteryLevel = 3; // 熟练掌握
            } else if (accuracyRate.compareTo(new BigDecimal("70")) >= 0) {
                masteryLevel = 2; // 基本掌握
            }
        } else if (totalQuestions >= 5) { // 至少答5道题
            if (accuracyRate.compareTo(new BigDecimal("80")) >= 0) {
                masteryLevel = 2; // 基本掌握
            }
        }
        
        stats.setMasteryLevel(masteryLevel);
        stats.setUpdateAt(new Date());
        
        int result = learningStatisticsMapper.updateLearningStatistics(stats);
        return result > 0 ? stats : null;
    }
}
