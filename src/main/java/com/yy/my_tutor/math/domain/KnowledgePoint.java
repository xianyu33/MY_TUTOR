package com.yy.my_tutor.math.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 知识点实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KnowledgePoint implements Serializable {
    private Integer id;
    private Integer gradeId;
    private Integer categoryId;
    private String pointName;
    private String pointCode;
    private String description;
    private String content;
    private Integer difficultyLevel;
    private Integer sortOrder;
    private String prerequisitePoints;
    private String learningObjectives;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
    private String updateBy;
    private String deleteFlag;
    
    // 关联对象
    private Grade grade;
    private KnowledgeCategory knowledgeCategory;
}
