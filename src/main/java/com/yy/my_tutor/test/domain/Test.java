package com.yy.my_tutor.test.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yy.my_tutor.math.domain.Grade;
import com.yy.my_tutor.math.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 测试实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Test implements Serializable {
    private Integer id;
    private String testName;
    private String testNameFr;
    private Integer gradeId;
    private String knowledgePointIds;
    private Integer totalQuestions;
    private Integer totalPoints;
    private Integer timeLimit;
    private Integer difficultyLevel;
    private Integer testType;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
    private String updateBy;
    private String deleteFlag;

    // 关联对象
    private Grade grade;
    private List<Question> questions;
}
