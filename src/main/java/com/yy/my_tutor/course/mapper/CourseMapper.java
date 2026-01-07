package com.yy.my_tutor.course.mapper;

import com.yy.my_tutor.course.domain.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程Mapper接口
 */
@Mapper
public interface CourseMapper {

    /**
     * 根据ID查询课程
     */
    Course findById(@Param("id") Integer id);

    /**
     * 根据学生ID和知识点ID查询课程
     */
    Course findByStudentAndKnowledgePoint(@Param("studentId") Integer studentId,
                                          @Param("knowledgePointId") Integer knowledgePointId);

    /**
     * 根据学生ID和知识点ID和难度级别查询课程
     */
    Course findByStudentKnowledgePointAndDifficulty(@Param("studentId") Integer studentId,
                                                     @Param("knowledgePointId") Integer knowledgePointId,
                                                     @Param("difficultyLevel") Integer difficultyLevel);

    /**
     * 根据学生ID查询课程列表
     */
    List<Course> findByStudentId(@Param("studentId") Integer studentId);

    /**
     * 根据知识点ID查询课程列表
     */
    List<Course> findByKnowledgePointId(@Param("knowledgePointId") Integer knowledgePointId);

    /**
     * 插入课程
     */
    int insert(Course course);

    /**
     * 更新课程
     */
    int update(Course course);

    /**
     * 逻辑删除课程
     */
    int deleteById(@Param("id") Integer id);
}
