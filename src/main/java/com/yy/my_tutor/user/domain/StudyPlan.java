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
 * 学习计划实体类
 * 计划名称 = 知识分类名称
 * 进度 = 该分类下已完成知识点数 / 总知识点数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudyPlan implements Serializable {

    /**
     * 计划ID（对应 student_category_binding.id）
     */
    private Integer id;

    /**
     * 学生ID
     */
    private Integer studentId;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 计划名称（分类名称）
     */
    private String planName;

    /**
     * 计划名称（法语）
     */
    private String planNameFr;

    /**
     * 分类编码
     */
    private String categoryCode;

    /**
     * 计划描述
     */
    private String description;

    /**
     * 计划描述（法语）
     */
    private String descriptionFr;

    /**
     * 年级ID
     */
    private Integer gradeId;

    /**
     * 年级名称
     */
    private String gradeName;

    /**
     * 图标URL
     */
    private String iconUrl;

    /**
     * 图标类名
     */
    private String iconClass;

    /**
     * 进度百分比（0.00 - 100.00）
     */
    private BigDecimal progressPercentage;

    /**
     * 该分类下总知识点数量
     */
    private Integer totalKnowledgePoints;

    /**
     * 已完成的知识点数量
     */
    private Integer completedKnowledgePoints;

    /**
     * 学习中的知识点数量
     */
    private Integer inProgressKnowledgePoints;

    /**
     * 未开始的知识点数量
     */
    private Integer notStartedKnowledgePoints;

    /**
     * 计划状态：1-未开始，2-进行中，3-已完成
     */
    private Integer planStatus;

    /**
     * 总学习时长（分钟）
     */
    private Integer totalStudyDuration;

    /**
     * 最后学习时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastStudyTime;

    /**
     * 开始学习时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completeTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
}
