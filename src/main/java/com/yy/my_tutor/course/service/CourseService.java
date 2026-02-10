package com.yy.my_tutor.course.service;

import com.yy.my_tutor.course.domain.Course;

import java.util.List;

/**
 * 课程服务接口
 */
public interface CourseService {

    Course findById(Integer id);

    /**
     * 根据ID查询课程并填充指定语言的内容
     */
    Course findByIdWithContent(Integer id, String language);

    Course findByStudentAndKnowledgePoint(Integer studentId, Integer knowledgePointId);

    Course findByStudentKnowledgePointAndDifficulty(Integer studentId, Integer knowledgePointId, Integer difficultyLevel);

    List<Course> findByStudentId(Integer studentId);

    List<Course> findByKnowledgePointId(Integer knowledgePointId);

    Course save(Course course);

    Course update(Course course);

    boolean deleteById(Integer id);

    /**
     * 标记当前阶段完成
     */
    Course completeStage(Integer courseId);
}
