package com.yy.my_tutor.math.mapper;

import com.yy.my_tutor.math.domain.Grade;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 年级Mapper接口
 */
@Mapper
public interface GradeMapper {
    
    /**
     * 查询所有年级
     */
    List<Grade> findAllGrades();
    
    /**
     * 根据ID查询年级
     */
    Grade findGradeById(@Param("id") Integer id);
    
    /**
     * 根据年级等级查询年级
     */
    Grade findGradeByLevel(@Param("gradeLevel") Integer gradeLevel);
    
    /**
     * 插入年级
     */
    int insertGrade(Grade grade);
    
    /**
     * 更新年级
     */
    int updateGrade(Grade grade);
    
    /**
     * 删除年级（逻辑删除）
     */
    int deleteGrade(@Param("id") Integer id);
}
