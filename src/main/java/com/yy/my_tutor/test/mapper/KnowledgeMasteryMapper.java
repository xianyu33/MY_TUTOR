package com.yy.my_tutor.test.mapper;

import com.yy.my_tutor.test.domain.KnowledgeMastery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 知识点掌握情况Mapper接口
 */
@Mapper
public interface KnowledgeMasteryMapper {
    
    /**
     * 查询学生的知识点掌握情况
     */
    List<KnowledgeMastery> findMasteryByStudentId(@Param("studentId") Integer studentId);
    
    /**
     * 根据学生ID和知识点ID查询掌握情况
     */
    KnowledgeMastery findMasteryByStudentAndKnowledge(@Param("studentId") Integer studentId, @Param("knowledgePointId") Integer knowledgePointId);
    
    /**
     * 根据掌握程度查询
     */
    List<KnowledgeMastery> findMasteryByLevel(@Param("studentId") Integer studentId, @Param("masteryLevel") Integer masteryLevel);
    
    /**
     * 插入掌握情况
     */
    int insertKnowledgeMastery(KnowledgeMastery mastery);
    
    /**
     * 更新掌握情况
     */
    int updateKnowledgeMastery(KnowledgeMastery mastery);
    
    /**
     * 更新掌握程度
     */
    int updateMasteryLevel(@Param("studentId") Integer studentId, @Param("knowledgePointId") Integer knowledgePointId, @Param("masteryLevel") Integer masteryLevel);
    
    /**
     * 删除掌握情况（逻辑删除）
     */
    int deleteKnowledgeMastery(@Param("id") Integer id);
}


