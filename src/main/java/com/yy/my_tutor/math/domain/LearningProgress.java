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
 * 学习进度实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LearningProgress implements Serializable {
    private Integer id;
    private Integer userId;
    private Integer knowledgePointId;
    private Integer progressStatus;
    private BigDecimal completionPercentage;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completeTime;
    private Integer studyDuration;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastStudyTime;
    private String notes;
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
