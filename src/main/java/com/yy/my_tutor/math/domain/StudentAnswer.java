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
 * 学生答题记录实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentAnswer implements Serializable {
    private Integer id;
    private Integer userId;
    private Integer questionId;
    private Integer knowledgePointId;
    private String userAnswer;
    private Integer isCorrect;
    private BigDecimal score;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date answerTime;
    private Integer timeSpent;
    private Integer attemptCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
    private String updateBy;
    private String deleteFlag;
    
    // 关联对象
    private Question question;
    private KnowledgePoint knowledgePoint;
}
