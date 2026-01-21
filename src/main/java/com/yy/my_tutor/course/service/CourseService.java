package com.yy.my_tutor.course.service;

import com.yy.my_tutor.course.domain.Course;

import java.util.List;

/**
 * 课程服务接口
 */
public interface CourseService {

    /**
     * 根据ID查询课程
     */
    Course findById(Integer id);

    /**
     * 根据学生ID和知识点ID查询课程
     */
    Course findByStudentAndKnowledgePoint(Integer studentId, Integer knowledgePointId);

    /**
     * 根据学生ID、知识点ID和难度级别查询课程
     */
    Course findByStudentKnowledgePointAndDifficulty(Integer studentId, Integer knowledgePointId, Integer difficultyLevel);

    /**
     * 根据学生ID查询课程列表
     */
    List<Course> findByStudentId(Integer studentId);

    /**
     * 根据知识点ID查询课程列表
     */
    List<Course> findByKnowledgePointId(Integer knowledgePointId);

    /**
     * 保存课程
     */
    Course save(Course course);

    /**
     * 更新课程
     */
    Course update(Course course);

    /**
     * 删除课程
     */
    boolean deleteById(Integer id);
}
