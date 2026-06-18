package com.yy.my_tutor.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yy.my_tutor.payment.domain.entity.TeacherSeatLedger;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TeacherSeatLedgerMapper extends BaseMapper<TeacherSeatLedger> {

    @Select("SELECT COALESCE(SUM(change_count), 0) FROM teacher_seat_ledger " +
            "WHERE teacher_id = #{teacherId} AND delete_flag = 'N'")
    int availableSeats(@Param("teacherId") Integer teacherId);

    @Select("SELECT COUNT(*) FROM teacher_seat_ledger " +
            "WHERE teacher_id = #{teacherId} AND student_id = #{studentId} " +
            "AND type = 'ACTIVATE' AND delete_flag = 'N'")
    int countActivation(@Param("teacherId") Integer teacherId,
                        @Param("studentId") Integer studentId);
}
