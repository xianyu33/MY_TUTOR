package com.yy.my_tutor.user.mapper;

import com.yy.my_tutor.user.domain.GuardianStudentRel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GuardianStudentRelMapper {
    List<GuardianStudentRel> findByGuardian(@Param("guardianId") Integer guardianId,
                                           @Param("guardianType") Integer guardianType);

    List<GuardianStudentRel> findByStudent(@Param("studentId") Integer studentId);

    GuardianStudentRel findUnique(@Param("guardianId") Integer guardianId,
                                  @Param("studentId") Integer studentId);

    int insert(GuardianStudentRel rel);

    int update(GuardianStudentRel rel);

    int logicalDelete(@Param("id") Long id);
}


