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
 * AI生成课程内容实体类
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
    private String courseTitle;
    private String courseTitleFr;

    // 课程内容模块
    private String explanation;
    private String explanationFr;
    private String examples;
    private String examplesFr;
    private String keySummary;
    private String keySummaryFr;
    private String additionalInfo;
    private String additionalInfoFr;

    // 元数据
    private String generationSource;
    private String modelId;
    private String promptUsed;

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
}
