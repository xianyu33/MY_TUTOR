package com.yy.my_tutor.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 某条测试记录在指定知识点（集合）下的得分汇总
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestRecordKnowledgeScore implements Serializable {
    private Integer testRecordId;
    /** 该知识点相关题目的得分合计 */
    private Integer earnedPoints;
    /** 该知识点相关题目的满分合计 */
    private Integer totalPoints;
}
