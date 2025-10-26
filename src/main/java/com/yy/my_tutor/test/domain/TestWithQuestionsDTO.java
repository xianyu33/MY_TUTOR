package com.yy.my_tutor.test.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 带题目详情的测试记录DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestWithQuestionsDTO implements Serializable {
    
    // 测试记录基本信息
    private Integer id;
    private Integer studentId;
    private Integer testId;
    private String testName;
    private String testNameFr;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    
    private Integer timeLimit;
    private Integer totalQuestions;
    private Integer totalPoints;
    private Integer answeredQuestions;
    private Integer testStatus;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
    
    // 题目列表
    private List<TestQuestionDetail> questions;
    
    // 难度统计
    private Integer easyCount;
    private Integer mediumCount;
    private Integer hardCount;
}
