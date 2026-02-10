package com.yy.my_tutor.course.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yy.my_tutor.math.domain.KnowledgePoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程主表实体类（不含内容，内容在 CourseContent）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Course implements Serializable {
    private Integer id;
    private Integer studentId;
    private Integer knowledgePointId;
    private Integer difficultyLevel;

    // 当前已生成阶段：0-未开始，1-4对应已生成的阶段
    private Integer currentStage;

    // 用户已完成的阶段：0-未完成，1-4对应已完成的阶段
    private Integer completedStage;

    // 审计字段
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
    private String updateBy;
    private String deleteFlag;

    // 关联对象
    private KnowledgePoint knowledgePoint;
    private CourseContent content;

    /**
     * 学习进度（不持久化，查询时计算）
     * progress = completedStage * 25
     */
    public Integer getProgress() {
        return (completedStage != null ? completedStage : 0) * 25;
    }
}
