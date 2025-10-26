package com.yy.my_tutor.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 知识点详情（包含学生学习进度）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KnowledgePointWithProgress implements Serializable {
    
    // 知识点基本信息
    private Integer id;
    private Integer gradeId;
    private Integer categoryId;
    private String pointName;
    private String pointNameFr;
    private String pointCode;
    private String description;
    private String descriptionFr;
    private String content;
    private String contentFr;
    private String iconUrl;
    private String iconClass;
    private Integer difficultyLevel;
    private Integer sortOrder;
    private String learningObjectives;
    private String learningObjectivesFr;
    
    // 分类信息
    private String categoryName;
    private String categoryNameFr;
    private String categoryCode;
    
    // 年级信息
    private String gradeName;
    private Integer gradeLevel;
    
    // 学生学习进度
    private Integer progressStatus;              // 学习状态：1-未开始，2-学习中，3-已完成
    private BigDecimal completionPercentage;    // 完成百分比
    private Date startTime;                     // 开始学习时间
    private Date completeTime;                   // 完成学习时间
    private Integer studyDuration;               // 学习时长（分钟）
    private Date lastStudyTime;                  // 最后学习时间
    private String notes;                        // 学习笔记
    
    // 难度信息
    private String difficultyName;              // 难度名称：简单/中等/困难
    private String difficultyDescription;        // 难度描述
    
    // 创建和更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
}

