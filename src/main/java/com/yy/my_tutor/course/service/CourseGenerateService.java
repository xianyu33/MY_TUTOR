package com.yy.my_tutor.course.service;

import com.yy.my_tutor.course.domain.Course;
import com.yy.my_tutor.course.domain.GenerateCourseRequest;

/**
 * 课程生成服务接口
 */
public interface CourseGenerateService {

    /**
     * 根据知识点生成课程
     *
     * @param request 生成课程请求参数
     * @return 生成的课程
     */
    Course generateCourse(GenerateCourseRequest request);

    /**
     * 根据学生测验报告判断知识点难度级别
     *
     * @param studentId        学生ID
     * @param knowledgePointId 知识点ID
     * @return 难度级别：1-简单 2-中等 3-困难
     */
    Integer determineDifficultyLevel(Integer studentId, Integer knowledgePointId);
}
