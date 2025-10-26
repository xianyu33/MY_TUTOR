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
 * 增强的学科分类信息（包含学习进度统计）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryWithProgress implements Serializable {
    
    // 分类基本信息
    private Integer id;
    private String categoryName;
    private String categoryNameFr;
    private String categoryCode;
    private String description;
    private String descriptionFr;
    private Integer gradeId;
    private String iconUrl;
    private String iconClass;
    private Integer sortOrder;
    
    // 学习进度统计信息
    private Integer totalKnowledgePoints;          // 总知识点数
    private Integer completedKnowledgePoints;      // 已完成知识点数
    private Integer inProgressKnowledgePoints;     // 学习中的知识点数
    private Integer notStartedKnowledgePoints;     // 未开始的知识点数
    private BigDecimal overallProgress;            // 整体完成进度百分比
    
    // 难度分布统计
    private Integer easyCount;                      // 简单难度知识点数
    private Integer mediumCount;                    // 中等难度知识点数
    private Integer hardCount;                      // 困难难度知识点数
    private BigDecimal averageDifficulty;          // 平均难度
    
    // 学习时长统计
    private Integer totalStudyDuration;            // 总学习时长（分钟）
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastStudyTime;                    // 最后学习时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;                        // 开始学习时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completeTime;                     // 完成时间
    
    // 绑定状态
    private Integer bindingStatus;                 // 绑定状态：1-已绑定，2-学习中，3-已完成
    
    // 创建和更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
}

