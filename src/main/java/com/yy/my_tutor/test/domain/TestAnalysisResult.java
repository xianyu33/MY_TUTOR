package com.yy.my_tutor.test.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 测试分析结果DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestAnalysisResult implements Serializable {
    
    /**
     * 测试记录ID
     */
    private Integer testRecordId;
    
    /**
     * 学生ID
     */
    private Integer studentId;
    
    /**
     * 测试ID
     */
    private Integer testId;
    
    /**
     * 总分
     */
    private Integer totalPoints;
    
    /**
     * 获得分数
     */
    private Integer earnedPoints;
    
    /**
     * 得分率
     */
    private BigDecimal scoreRate;
    
    /**
     * 正确率
     */
    private BigDecimal accuracyRate;
    
    /**
     * 总题目数
     */
    private Integer totalQuestions;
    
    /**
     * 正确答案数
     */
    private Integer correctAnswers;
    
    /**
     * 知识点得分统计
     */
    private List<KnowledgePointScore> knowledgePointScores;
    
    /**
     * 知识点分析（strong/needs improvement/weak）
     */
    private KnowledgePointAnalysis knowledgePointAnalysis;
    
    /**
     * 总体评价
     */
    private String overallComment;
    
    /**
     * 总体评价（法语）
     */
    private String overallCommentFr;
    
    /**
     * 学习建议
     */
    private String recommendations;
    
    /**
     * 学习建议（法语）
     */
    private String recommendationsFr;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
    
    /**
     * 知识点得分统计
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class KnowledgePointScore implements Serializable {
        /**
         * 知识点ID
         */
        private Integer knowledgePointId;
        
        /**
         * 知识点名称
         */
        private String knowledgePointName;
        
        /**
         * 知识点名称（法语）
         */
        private String knowledgePointNameFr;
        
        /**
         * 总分
         */
        private Integer totalPoints;
        
        /**
         * 获得分数
         */
        private Integer earnedPoints;
        
        /**
         * 得分率
         */
        private BigDecimal scoreRate;
        
        /**
         * 题目数量
         */
        private Integer questionCount;
        
        /**
         * 正确数量
         */
        private Integer correctCount;
    }
    
    /**
     * 知识点分析
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class KnowledgePointAnalysis implements Serializable {
        /**
         * 掌握较好的知识点（得分率 >= 80%）
         */
        private List<Integer> strongPoints;
        
        /**
         * 需要改进的知识点（得分率 50%-80%）
         */
        private List<Integer> needsImprovementPoints;
        
        /**
         * 薄弱知识点（得分率 < 50%）
         */
        private List<Integer> weakPoints;
        
        /**
         * 掌握较好的知识点摘要
         */
        private String strongSummary;
        
        /**
         * 需要改进的知识点摘要
         */
        private String needsImprovementSummary;
        
        /**
         * 薄弱知识点摘要
         */
        private String weakSummary;
    }
}

