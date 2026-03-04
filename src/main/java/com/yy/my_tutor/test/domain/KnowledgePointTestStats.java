package com.yy.my_tutor.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 某学生在某知识点下的测试统计（总题数、正确数）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KnowledgePointTestStats implements Serializable {
    private Integer knowledgePointId;
    private Integer totalQuestions;
    private Integer correctCount;
}
