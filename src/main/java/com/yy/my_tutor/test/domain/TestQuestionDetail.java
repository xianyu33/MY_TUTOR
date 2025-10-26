package com.yy.my_tutor.test.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 测试题目详情DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestQuestionDetail implements Serializable {
    
    private Integer questionId;
    private Integer sortOrder;
    private Integer points;
    
    // 题目基本信息
    private String questionTitle;
    private String questionTitleFr;
    private String questionContent;
    private String questionContentFr;
    private String options;  // JSON string
    private String optionsFr;  // JSON string
    private String correctAnswer;
    private String correctAnswerFr;
    private String answerExplanation;
    private String answerExplanationFr;
    
    // 难度信息
    private Integer difficultyLevel;
    
    // 知识点信息
    private Integer knowledgePointId;
    private String knowledgePointName;
    private String knowledgePointNameFr;
    
    // 学生答题信息（可选）
    private String studentAnswer;
    private Boolean isCorrect;
    private Integer earnedPoints;
}
