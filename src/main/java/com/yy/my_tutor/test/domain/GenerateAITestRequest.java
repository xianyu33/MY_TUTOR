package com.yy.my_tutor.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI 生成测试的请求 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateAITestRequest {

    /**
     * 学生ID（必填）
     */
    private Integer studentId;

    /**
     * 知识点ID（单知识点测试时使用）
     */
    private Integer knowledgePointId;

    /**
     * 知识类型ID（按知识类型生成测试时使用）
     * 系统会查询该分类下的所有知识点
     */
    private Integer categoryId;

    /**
     * 年级ID（综合测试时可选，用于筛选知识点）
     */
    private Integer gradeId;

    /**
     * 题目数量（必填）
     */
    private Integer questionCount;

    /**
     * 难度等级（1-简单，2-中等，3-困难）
     * 可选，不传则根据学生测试分析报告自动确定
     */
    private Integer difficultyLevel;

    /**
     * 是否将 AI 生成的题目保存到题库
     * 默认 true
     */
    private Boolean saveToQuestionBank = true;

    /**
     * 测试名称（可选）
     */
    private String testName;

    /**
     * 测试名称（法语，可选）
     */
    private String testNameFr;
}
