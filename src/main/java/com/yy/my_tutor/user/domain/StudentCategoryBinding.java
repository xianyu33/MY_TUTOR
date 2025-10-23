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
 * 学生知识点分类绑定关系实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentCategoryBinding implements Serializable {
    
    /**
     * 绑定关系ID
     */
    private Integer id;
    
    /**
     * 学生ID
     */
    private Integer studentId;
    
    /**
     * 知识点分类ID
     */
    private Integer categoryId;
    
    /**
     * 年级ID
     */
    private Integer gradeId;
    
    /**
     * 绑定状态：1-已绑定，2-学习中，3-已完成
     */
    private Integer bindingStatus;
    
    /**
     * 整体学习进度百分比
     */
    private BigDecimal overallProgress;
    
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
     * 学习笔记
     */
    private String notes;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
    
    /**
     * 创建人
     */
    private String createBy;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
    
    /**
     * 更新人
     */
    private String updateBy;
    
    /**
     * 删除标志：Y-已删除，N-未删除
     */
    private String deleteFlag;
    
    // 关联对象
    /**
     * 知识点分类信息
     */
    private com.yy.my_tutor.math.domain.KnowledgeCategory knowledgeCategory;
    
    /**
     * 年级信息
     */
    private com.yy.my_tutor.math.domain.Grade grade;
    
    /**
     * 学生信息
     */
    private com.yy.my_tutor.user.domain.User student;
    
    /**
     * 该分类下的知识点学习进度列表
     */
    private java.util.List<com.yy.my_tutor.math.domain.LearningProgress> learningProgressList;
}
