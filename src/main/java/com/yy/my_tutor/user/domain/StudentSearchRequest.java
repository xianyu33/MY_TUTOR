package com.yy.my_tutor.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 学生搜索请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentSearchRequest implements Serializable {
    
    /**
     * 搜索关键字（学生姓名或账号，可选）
     */
    private String name;
    
    /**
     * 年级（可选）
     */
    private String grade;
    
    /**
     * 性别（可选）
     */
    private String sex;
    
    /**
     * 分页页码（可选）
     */
    private Integer pageNum;
    
    /**
     * 每页大小（可选）
     */
    private Integer pageSize;
}

