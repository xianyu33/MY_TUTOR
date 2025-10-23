package com.yy.my_tutor.user.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 学习进度统计实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LearningProgressStats implements Serializable {
    
    /**
     * 学生ID
     */
    private Integer userId;
    
    /**
     * 年级等级
     */
    private Integer gradeLevel;
    
    /**
     * 年级名称
     */
    private String gradeName;
    
    /**
     * 总知识点数量
     */
    private Integer totalKnowledgePoints;
    
    /**
     * 未开始的知识点数量
     */
    private Integer notStartedCount;
    
    /**
     * 学习中的知识点数量
     */
    private Integer inProgressCount;
    
    /**
     * 已完成的知识点数量
     */
    private Integer completedCount;
    
    /**
     * 总体完成百分比
     */
    private BigDecimal overallCompletionPercentage;
    
    /**
     * 总学习时长（分钟）
     */
    private Integer totalStudyDuration;
    
    /**
     * 最后学习时间
     */
    private String lastStudyTime;
    
    /**
     * 学习进度分布
     */
    private ProgressDistribution distribution;
    
    /**
     * 学习进度分布内部类
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProgressDistribution implements Serializable {
        /**
         * 0-25%完成度的知识点数量
         */
        private Integer lowProgressCount;
        
        /**
         * 25-50%完成度的知识点数量
         */
        private Integer mediumProgressCount;
        
        /**
         * 50-75%完成度的知识点数量
         */
        private Integer highProgressCount;
        
        /**
         * 75-100%完成度的知识点数量
         */
        private Integer nearCompleteCount;
        
        /**
         * 100%完成的知识点数量
         */
        private Integer completeCount;
    }
}
