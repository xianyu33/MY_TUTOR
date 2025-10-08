package com.yy.my_tutor.math.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 学习统计实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LearningStatistics implements Serializable {
    private Integer id;
    private Integer userId;
    private Integer knowledgePointId;
    private Integer totalStudyTime;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private BigDecimal accuracyRate;
    private Integer masteryLevel;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastStudyDate;
    private Integer studyDays;
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
