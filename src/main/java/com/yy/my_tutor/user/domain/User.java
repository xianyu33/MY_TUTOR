package com.yy.my_tutor.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yy.my_tutor.chat.domain.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable {
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
    private String updateBy;
    private String deleteFlag;

    private String token;

    private String verificationCode;

    /**
     * 登录验证码
     */
    private String captcha;
    
    /**
     * 验证码ID，用于从Redis中获取验证码
     */
    private String captchaId;

    private List<User> children;

    private Integer parentId;

    /**
     * P-家长  S-学生
     */
    private String role;

    private List<ChatMessage> chatMessages;

}
