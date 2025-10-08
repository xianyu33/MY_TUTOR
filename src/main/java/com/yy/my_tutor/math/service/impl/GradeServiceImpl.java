package com.yy.my_tutor.math.service.impl;

import com.yy.my_tutor.math.domain.Grade;
import com.yy.my_tutor.math.mapper.GradeMapper;
import com.yy.my_tutor.math.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 年级服务实现类
 */
@Service
public class GradeServiceImpl implements GradeService {
    
    @Autowired
    private GradeMapper gradeMapper;
    
    @Override
    public List<Grade> findAllGrades() {
        return gradeMapper.findAllGrades();
    }
    
    @Override
    public Grade findGradeById(Integer id) {
        return gradeMapper.findGradeById(id);
    }
    
    @Override
    public Grade findGradeByLevel(Integer gradeLevel) {
        return gradeMapper.findGradeByLevel(gradeLevel);
    }
    
    @Override
    public Grade addGrade(Grade grade) {
        Date now = new Date();
        grade.setCreateAt(now);
        grade.setUpdateAt(now);
        grade.setDeleteFlag("N");
        
        int result = gradeMapper.insertGrade(grade);
        return result > 0 ? grade : null;
    }
    
    @Override
    public Grade updateGrade(Grade grade) {
        grade.setUpdateAt(new Date());
        
        int result = gradeMapper.updateGrade(grade);
        return result > 0 ? grade : null;
    }
    
    @Override
    public boolean deleteGrade(Integer id) {
        int result = gradeMapper.deleteGrade(id);
        return result > 0;
    }
}
