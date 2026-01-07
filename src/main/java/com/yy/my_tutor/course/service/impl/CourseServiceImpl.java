package com.yy.my_tutor.course.service.impl;

import com.yy.my_tutor.course.domain.Course;
import com.yy.my_tutor.course.mapper.CourseMapper;
import com.yy.my_tutor.course.service.CourseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 课程服务实现类
 */
@Service
public class CourseServiceImpl implements CourseService {

    @Resource
    private CourseMapper courseMapper;

    @Override
    public Course findById(Integer id) {
        return courseMapper.findById(id);
    }

    @Override
    public Course findByStudentAndKnowledgePoint(Integer studentId, Integer knowledgePointId) {
        return courseMapper.findByStudentAndKnowledgePoint(studentId, knowledgePointId);
    }

    @Override
    public Course findByStudentKnowledgePointAndDifficulty(Integer studentId, Integer knowledgePointId, Integer difficultyLevel) {
        return courseMapper.findByStudentKnowledgePointAndDifficulty(studentId, knowledgePointId, difficultyLevel);
    }

    @Override
    public List<Course> findByStudentId(Integer studentId) {
        return courseMapper.findByStudentId(studentId);
    }

    @Override
    public List<Course> findByKnowledgePointId(Integer knowledgePointId) {
        return courseMapper.findByKnowledgePointId(knowledgePointId);
    }

    @Override
    public Course save(Course course) {
        courseMapper.insert(course);
        return course;
    }

    @Override
    public Course update(Course course) {
        courseMapper.update(course);
        return courseMapper.findById(course.getId());
    }

    @Override
    public boolean deleteById(Integer id) {
        return courseMapper.deleteById(id) > 0;
    }
}
