package com.yy.my_tutor.user.service;

import com.yy.my_tutor.user.domain.GuardianStudentRel;

import java.util.List;

public interface GuardianStudentRelService {
    List<GuardianStudentRel> listByGuardian(Integer guardianId, Integer guardianType);
    
    List<GuardianStudentRel> listByGuardianWithDetails(Integer guardianId, Integer guardianType);

    List<GuardianStudentRel> listByStudent(Integer studentId);

    GuardianStudentRel bind(Integer guardianId, Integer guardianType, Integer studentId, String relation, String operator);

    boolean unbind(Long id);

    GuardianStudentRel updateRelation(Long id, String relation, String operator);
}


