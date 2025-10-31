package com.yy.my_tutor.math.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 查询知识点列表请求DTO（包含学习进度）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgePointQueryRequest implements Serializable {
    
    /**
     * 学生ID
     */
    private Integer studentId;
    
    /**
     * 年级ID
     */
    private Integer gradeId;
    
    /**
     * 知识点分类ID
     */
    private Integer categoryId;
}

