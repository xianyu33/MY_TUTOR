package com.yy.my_tutor.test.service;

import com.yy.my_tutor.test.domain.KnowledgeMastery;

import java.util.List;

/**
 * 知识点掌握情况服务接口
 */
public interface KnowledgeMasteryService {
    
    /**
     * 查询学生的知识点掌握情况
     */
    List<KnowledgeMastery> findMasteryByStudentId(Integer studentId);
    
    /**
     * 根据学生ID和知识点ID查询掌握情况
     */
    KnowledgeMastery findMasteryByStudentAndKnowledge(Integer studentId, Integer knowledgePointId);
    
    /**
     * 根据掌握程度查询
     */
    List<KnowledgeMastery> findMasteryByLevel(Integer studentId, Integer masteryLevel);
    
    /**
     * 更新知识点掌握情况
     */
    KnowledgeMastery updateKnowledgeMastery(Integer studentId, Integer knowledgePointId);
    
    /**
     * 计算掌握程度
     */
    KnowledgeMastery calculateMasteryLevel(Integer studentId, Integer knowledgePointId);
    
    /**
     * 新增掌握情况
     */
    KnowledgeMastery addKnowledgeMastery(KnowledgeMastery mastery);
    
    /**
     * 更新掌握情况
     */
    KnowledgeMastery updateMastery(KnowledgeMastery mastery);
    
    /**
     * 删除掌握情况
     */
    boolean deleteKnowledgeMastery(Integer id);
}


