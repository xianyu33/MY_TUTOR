package com.yy.my_tutor.math.service;

import com.yy.my_tutor.math.domain.Grade;

import java.util.List;

/**
 * 年级服务接口
 */
public interface GradeService {
    
    /**
     * 查询所有年级
     */
    List<Grade> findAllGrades();
    
    /**
     * 根据ID查询年级
     */
    Grade findGradeById(Integer id);
    
    /**
     * 根据年级等级查询年级
     */
    Grade findGradeByLevel(Integer gradeLevel);
    
    /**
     * 新增年级
     */
    Grade addGrade(Grade grade);
    
    /**
     * 更新年级
     */
    Grade updateGrade(Grade grade);
    
    /**
     * 删除年级
     */
    boolean deleteGrade(Integer id);
}
