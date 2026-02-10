package com.yy.my_tutor.course.mapper;

import com.yy.my_tutor.course.domain.CourseContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程内容Mapper接口
 */
@Mapper
public interface CourseContentMapper {

    CourseContent findByCourseIdAndLanguage(@Param("courseId") Integer courseId,
                                            @Param("language") String language);

    List<CourseContent> findByCourseId(@Param("courseId") Integer courseId);

    int insert(CourseContent courseContent);

    int update(CourseContent courseContent);
}
