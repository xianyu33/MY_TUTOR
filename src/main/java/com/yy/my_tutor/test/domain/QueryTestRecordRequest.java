package com.yy.my_tutor.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询测试记录的请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryTestRecordRequest {
    
    /**
     * 知识类型ID（分类ID）
     */
    private Integer categoryId;
    
    /**
     * 知识点难度等级（1-简单，2-中等，3-困难）
     */
    private Integer difficultyLevel;
}

