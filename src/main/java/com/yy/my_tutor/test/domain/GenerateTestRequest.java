package com.yy.my_tutor.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 生成随机测试的请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateTestRequest {
    
    /**
     * 学生ID
     */
    private Integer studentId;
    
    /**
     * 年级ID
     */
    private Integer gradeId;
    
    /**
     * 知识点分类ID列表（可选，已废弃，建议使用knowledgePointIds）
     */
    private List<Integer> categoryIds;
    
    /**
     * 知识点ID列表（可选，优先使用）
     */
    private List<Integer> knowledgePointIds;
    
    /**
     * 题目数量
     */
    private Integer questionCount;
    
    /**
     * 是否均匀分配难度（默认false）
     */
    private Boolean equalDifficultyDistribution;
}

