package com.yy.my_tutor.course.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 生成课程请求参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateCourseRequest implements Serializable {
    /**
     * 知识点ID（必填）
     */
    private Integer knowledgePointId;

    /**
     * 学生ID（必填，用于查询测验报告判断难度）
     */
    private Integer studentId;

    /**
     * 难度级别（可选）：1-简单 2-中等 3-困难
     * 如果不传，则根据学生测验报告自动判断，没有测验记录则默认为1
     */
    private Integer difficultyLevel;

    /**
     * 语言（可选）：en-英文 fr-法语 both-双语（默认）
     */
    private String language = "both";
}
