package com.yy.my_tutor.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 查询某学生某知识大类下知识点测试与学习统计的请求参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryKnowledgeStatsRequest implements Serializable {
    private Integer studentId;
    private Integer categoryId;
}
