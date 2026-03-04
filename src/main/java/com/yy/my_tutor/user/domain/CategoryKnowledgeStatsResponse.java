package com.yy.my_tutor.user.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 某学生、某知识大类下所有知识点的测试结果与学习进度
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryKnowledgeStatsResponse implements Serializable {

    /** 知识大类ID */
    private Integer categoryId;
    /** 知识大类名称 */
    private String categoryName;
    private String categoryNameFr;
    /** 学生ID */
    private Integer studentId;
    /** 该大类下各知识点的测试正确率与学习百分比 */
    private List<KnowledgePointStatItem> items;

    /**
     * 单个知识点的测试结果 + 学习进度
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class KnowledgePointStatItem implements Serializable {
        private Integer knowledgePointId;
        private String pointName;
        private String pointNameFr;
        private String pointCode;
        private Integer sortOrder;

        /** 该知识点测试总题数 */
        private Integer totalQuestions;
        /** 正确题数 */
        private Integer correctCount;
        /** 正确率（0-100） */
        private BigDecimal correctRate;

        /** 学习进度百分比（0-100） */
        private BigDecimal completionPercentage;
        /** 学习状态：1-未开始，2-学习中，3-已完成 */
        private Integer progressStatus;

        /** 知识点难度等级：1-简单，2-中等，3-困难 */
        private Integer difficultyLevel;
        /** 难度名称（如：简单/中等/困难） */
        private String difficultyName;
    }
}
