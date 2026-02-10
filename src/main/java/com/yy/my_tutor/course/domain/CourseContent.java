package com.yy.my_tutor.course.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程内容实体类（按语言存储）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseContent implements Serializable {
    private Integer id;
    private Integer courseId;
    private String language;
    private String courseTitle;

    /** 阶段1 Understand：概念定义、关键要素、简单示例、适用边界 */
    private String explanation;
    /** 阶段2 Apply：典型场景、复杂案例、实现步骤、常见错误 */
    private String examples;
    /** 阶段3 Master：步骤拆解、对比分析、原理理解、扩展应用 */
    private String keySummary;
    /** 阶段4 Evaluate：场景判断、方案选择、错误诊断、学习总结 */
    private String additionalInfo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
}
