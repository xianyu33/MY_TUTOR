package com.yy.my_tutor.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生详细信息DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDetailDTO implements Serializable {
    
    // 关系信息
    private Long relationId;
    private String relation;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date relationStartAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date relationEndAt;
    
    // 学生基本信息
    private Integer studentId;
    private String studentAccount;
    private String studentName;
    private String studentSex;
    private Integer studentAge;
    private String studentTel;
    private String studentCountry;
    private String studentEmail;
    private String studentGrade;
    private String studentRole;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date studentCreateAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date studentUpdateAt;
    
    // 学习进度信息（可扩展）
    private Integer totalTests;
    private Integer completedTests;
    private Double averageScore;
    
    // 各知识点类型的学习情况
    private java.util.List<CategoryLearningProgress> categoryLearningProgress;
    
    /**
     * 知识点类型学习情况
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CategoryLearningProgress implements Serializable {
        private Integer categoryId;
        private String categoryName;
        private String categoryNameFr;
        private Integer totalKnowledgePoints;
        private Integer completedKnowledgePoints;
        private Integer inProgressKnowledgePoints;
        private Integer notStartedKnowledgePoints;
        private java.math.BigDecimal overallProgress;
        private Integer easyCount;
        private Integer mediumCount;
        private Integer hardCount;
    }
}

