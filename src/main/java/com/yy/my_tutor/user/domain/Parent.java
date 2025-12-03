package com.yy.my_tutor.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Parent implements Serializable {
    private Integer id;
    private String userAccount;
    private String username;
    private String sex;
    private Integer age;
    private String password;
    private String tel;
    private String country;
    private String email;
    private String grade;
    private String school;
    /**
     * 0-家长 1-老师
     */
    private Integer type;
    /**
     * 审批状态：0-未审批，1-已审批
     */
    private Integer approvalStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
    private String updateBy;
    private String deleteFlag;
} 