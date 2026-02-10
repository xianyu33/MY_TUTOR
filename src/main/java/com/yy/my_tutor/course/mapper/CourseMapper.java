package com.yy.my_tutor.course.mapper;

import com.yy.my_tutor.course.domain.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程主表Mapper接口
 */
@Mapper
public interface CourseMapper {

    Course findById(@Param("id") Integer id);

    Course findByStudentAndKnowledgePoint(@Param("studentId") Integer studentId,
                                          @Param("knowledgePointId") Integer knowledgePointId);

    Course findByStudentKnowledgePointAndDifficulty(@Param("studentId") Integer studentId,
                                                     @Param("knowledgePointId") Integer knowledgePointId,
                                                     @Param("difficultyLevel") Integer difficultyLevel);

    List<Course> findByStudentId(@Param("studentId") Integer studentId);

    List<Course> findByKnowledgePointId(@Param("knowledgePointId") Integer knowledgePointId);

    int insert(Course course);

    int update(Course course);

    int deleteById(@Param("id") Integer id);
}
