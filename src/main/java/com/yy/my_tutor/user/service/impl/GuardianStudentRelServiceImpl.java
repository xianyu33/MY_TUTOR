package com.yy.my_tutor.user.service.impl;

import com.yy.my_tutor.user.domain.GuardianStudentRel;
import com.yy.my_tutor.user.mapper.GuardianStudentRelMapper;
import com.yy.my_tutor.user.service.GuardianStudentRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GuardianStudentRelServiceImpl implements GuardianStudentRelService {

    @Autowired
    private GuardianStudentRelMapper relMapper;

    @Override
    public List<GuardianStudentRel> listByGuardian(Integer guardianId, Integer guardianType) {
        return relMapper.findByGuardian(guardianId, guardianType);
    }

    @Override
    public List<GuardianStudentRel> listByStudent(Integer studentId) {
        return relMapper.findByStudent(studentId);
    }

    @Override
    public GuardianStudentRel bind(Integer guardianId, Integer guardianType, Integer studentId, String relation, String operator) {
        GuardianStudentRel exists = relMapper.findUnique(guardianId, studentId);
        if (exists != null) {
            return exists;
        }
        GuardianStudentRel rel = new GuardianStudentRel();
        rel.setGuardianId(guardianId);
        rel.setGuardianType(guardianType);
        rel.setStudentId(studentId);
        rel.setRelation(relation);
        rel.setStartAt(new Date());
        rel.setCreateAt(new Date());
        rel.setUpdateAt(new Date());
        rel.setCreateBy(operator);
        rel.setUpdateBy(operator);
        rel.setDeleteFlag("0");
        relMapper.insert(rel);
        return rel;
    }

    @Override
    public boolean unbind(Long id) {
        return relMapper.logicalDelete(id) > 0;
    }

    @Override
    public GuardianStudentRel updateRelation(Long id, String relation, String operator) {
        GuardianStudentRel rel = new GuardianStudentRel();
        rel.setId(id);
        rel.setRelation(relation);
        rel.setUpdateBy(operator);
        rel.setUpdateAt(new Date());
        int cnt = relMapper.update(rel);
        return cnt > 0 ? rel : null;
    }
}


