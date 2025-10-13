package com.yy.my_tutor.test.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yy.my_tutor.math.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生测试答题详情实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentTestAnswer implements Serializable {
    private Integer id;
    private Integer testRecordId;
    private Integer studentId;
    private Integer questionId;
    private String questionContent;
    private String correctAnswer;
    private String studentAnswer;
    private Integer isCorrect;
    private Integer points;
    private Integer earnedPoints;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date answerTime;
    private Integer timeSpent;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;

    // 关联对象
    private Question question;
}


