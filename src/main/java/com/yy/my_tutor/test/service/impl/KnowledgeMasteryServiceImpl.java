package com.yy.my_tutor.test.service.impl;

import com.yy.my_tutor.test.domain.KnowledgeMastery;
import com.yy.my_tutor.test.mapper.KnowledgeMasteryMapper;
import com.yy.my_tutor.test.service.KnowledgeMasteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 知识点掌握情况服务实现类
 */
@Service
public class KnowledgeMasteryServiceImpl implements KnowledgeMasteryService {
    
    @Autowired
    private KnowledgeMasteryMapper masteryMapper;
    
    @Override
    public List<KnowledgeMastery> findMasteryByStudentId(Integer studentId) {
        return masteryMapper.findMasteryByStudentId(studentId);
    }
    
    @Override
    public KnowledgeMastery findMasteryByStudentAndKnowledge(Integer studentId, Integer knowledgePointId) {
        return masteryMapper.findMasteryByStudentAndKnowledge(studentId, knowledgePointId);
    }
    
    @Override
    public List<KnowledgeMastery> findMasteryByLevel(Integer studentId, Integer masteryLevel) {
        return masteryMapper.findMasteryByLevel(studentId, masteryLevel);
    }
    
    @Override
    public KnowledgeMastery updateKnowledgeMastery(Integer studentId, Integer knowledgePointId) {
        KnowledgeMastery mastery = masteryMapper.findMasteryByStudentAndKnowledge(studentId, knowledgePointId);
        
        if (mastery == null) {
            // 创建新的掌握情况记录
            mastery = new KnowledgeMastery();
            mastery.setStudentId(studentId);
            mastery.setKnowledgePointId(knowledgePointId);
            mastery.setTotalTests(1);
            mastery.setTotalQuestions(0);
            mastery.setCorrectAnswers(0);
            mastery.setAccuracyRate(BigDecimal.ZERO);
            mastery.setMasteryLevel(1);
            mastery.setLastTestTime(new Date());
            mastery.setCreateAt(new Date());
            mastery.setUpdateAt(new Date());
            mastery.setDeleteFlag("N");
            
            int result = masteryMapper.insertKnowledgeMastery(mastery);
            return result > 0 ? mastery : null;
        } else {
            // 更新现有记录
            mastery.setTotalTests(mastery.getTotalTests() + 1);
            mastery.setLastTestTime(new Date());
            mastery.setUpdateAt(new Date());
            
            int result = masteryMapper.updateKnowledgeMastery(mastery);
            return result > 0 ? mastery : null;
        }
    }
    
    @Override
    public KnowledgeMastery calculateMasteryLevel(Integer studentId, Integer knowledgePointId) {
        KnowledgeMastery mastery = masteryMapper.findMasteryByStudentAndKnowledge(studentId, knowledgePointId);
        
        if (mastery != null && mastery.getTotalQuestions() > 0) {
            // 根据正确率计算掌握程度
            BigDecimal accuracyRate = mastery.getAccuracyRate();
            Integer masteryLevel;
            
            if (accuracyRate.compareTo(new BigDecimal("80")) >= 0) {
                masteryLevel = 3; // 熟练掌握
            } else if (accuracyRate.compareTo(new BigDecimal("60")) >= 0) {
                masteryLevel = 2; // 基本掌握
            } else {
                masteryLevel = 1; // 未掌握
            }
            
            mastery.setMasteryLevel(masteryLevel);
            mastery.setUpdateAt(new Date());
            
            int result = masteryMapper.updateKnowledgeMastery(mastery);
            return result > 0 ? mastery : null;
        }
        
        return mastery;
    }
    
    @Override
    public KnowledgeMastery addKnowledgeMastery(KnowledgeMastery mastery) {
        Date now = new Date();
        mastery.setCreateAt(now);
        mastery.setUpdateAt(now);
        mastery.setDeleteFlag("N");
        
        int result = masteryMapper.insertKnowledgeMastery(mastery);
        return result > 0 ? mastery : null;
    }
    
    @Override
    public KnowledgeMastery updateMastery(KnowledgeMastery mastery) {
        mastery.setUpdateAt(new Date());
        
        int result = masteryMapper.updateKnowledgeMastery(mastery);
        return result > 0 ? mastery : null;
    }
    
    @Override
    public boolean deleteKnowledgeMastery(Integer id) {
        int result = masteryMapper.deleteKnowledgeMastery(id);
        return result > 0;
    }
}
