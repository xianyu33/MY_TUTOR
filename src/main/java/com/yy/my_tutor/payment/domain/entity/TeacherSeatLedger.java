package com.yy.my_tutor.payment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("teacher_seat_ledger")
public class TeacherSeatLedger {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer teacherId;
    private Integer orderId;
    private Integer studentId;
    private Integer changeCount;
    private String type;
    private Date createAt;
    private String createBy;
    private Date updateAt;
    private String updateBy;
    private String deleteFlag;
}
