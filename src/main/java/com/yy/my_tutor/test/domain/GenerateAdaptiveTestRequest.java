package com.yy.my_tutor.test.domain;

import lombok.Data;

/**
 * 自适应测验生成请求
 */
@Data
public class GenerateAdaptiveTestRequest {
    /** 学生ID（必填） */
    private Integer studentId;
    /** 知识类型ID（与 knowledgePointId 二选一） */
    private Integer categoryId;
    /** 知识点ID（与 categoryId 二选一） */
    private Integer knowledgePointId;
    /** 年级ID（可选） */
    private Integer gradeId;
    /** 题目数量（必填） */
    private Integer questionCount;
}
