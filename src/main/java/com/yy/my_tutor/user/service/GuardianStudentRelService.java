package com.yy.my_tutor.user.service;

import com.yy.my_tutor.user.domain.GuardianStudentRel;
import com.yy.my_tutor.user.domain.StudentDetailDTO;

import java.util.List;

public interface GuardianStudentRelService {
    List<GuardianStudentRel> listByGuardian(Integer guardianId, Integer guardianType);
    
    List<GuardianStudentRel> listByGuardianWithDetails(Integer guardianId, Integer guardianType);

    List<GuardianStudentRel> listByStudent(Integer studentId);

    GuardianStudentRel bind(Integer guardianId, Integer guardianType, Integer studentId, String relation, String operator);

    boolean unbind(Long id);

    GuardianStudentRel updateRelation(Long id, String relation, String operator);
    
    /**
     * 根据家长/老师ID和类型查询绑定的学生详细信息
     * @param guardianId 家长/老师ID
     * @param guardianType 类型：0-家长，1-老师
     * @return 学生详细信息列表
     */
    List<StudentDetailDTO> getStudentsWithDetailsByGuardian(Integer guardianId, Integer guardianType);
}


