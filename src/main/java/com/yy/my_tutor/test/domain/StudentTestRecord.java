package com.yy.my_tutor.test.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 学生测试记录实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentTestRecord implements Serializable {
    private Integer id;
    private Integer studentId;
    private Integer testId;
    private String testName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;
    private Integer timeSpent;
    private Integer totalQuestions;
    private Integer answeredQuestions;
    private Integer correctAnswers;
    private Integer totalPoints;
    private Integer earnedPoints;
    private BigDecimal scorePercentage;
    private Integer testStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
    private String updateBy;
    private String deleteFlag;
    
    // 关联对象
    private Test test;
    private List<StudentTestAnswer> answers;
}


