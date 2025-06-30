package com.yy.my_tutor.user.controller;

import com.alibaba.fastjson.JSON;
import com.yy.my_tutor.common.AESUtil;
import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.config.EmailUtil;
import com.yy.my_tutor.user.domain.User;
import com.yy.my_tutor.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    public static void main(String[] args) {
        System.out.println(AESUtil.decryptBase64("CH+8oJbnh+IfILiWQWY5OpfP5u2cbBEKWWVBg2bJ+NcLRPh3t8vxgQE2T1M2RRNzH9Sue8RgS4i14VES15YQM/9PlSxiWa0PJ8tI5B2i4iXl4gDYOswtWgxQGVuNCY5RYCQcGz75XoYSpC/Eybcbvz9N8g4QlL2pDBLGrBQW1aM/57QVmmsFMD6+ZXPAGXStQBxaCHa3Or9Z+3f4wD4y5XYVC6uMk6WuQmvcS68e4Z0etI9KrctCFF+70lLEcIBz"));
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public RespResult<User> login(@RequestBody User userVo) {
        log.info("用户登录: {}", JSON.toJSONString(userVo));

        // 解密密码
        String decryptedPassword = AESUtil.decryptBase64(userVo.getPassword());
        userVo.setPassword(decryptedPassword);

        User user = userService.login(userVo.getUserAccount(), userVo.getPassword());

//        try {
//            EmailUtil.sendVerificationCode("229268931@qq.com", "123123");
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }

        if (user != null) {
            return RespResult.success("登录成功", user);
        }

        return RespResult.error("用户名或密码错误");
    }


    /**
     * 用户注册
     */
    @PostMapping("/register")
    public RespResult<Boolean> register(@RequestBody User user) {
        log.info("用户注册: {}", user.getUserAccount());

        // 解密密码
        String decryptedPassword = AESUtil.decryptBase64(user.getPassword());
        user.setPassword(decryptedPassword);

        boolean result = userService.register(user);
        if (result) {
            return RespResult.success("注册成功", true);
        }
        return RespResult.error("注册失败，用户可能已存在");
    }

    /**
     * 新增用户
     */
    @PostMapping("/add")
    public RespResult<Boolean> addUser(@RequestBody User user) {
        log.info("新增用户: {}", user.getUserAccount());

        // 解密密码
        String decryptedPassword = AESUtil.decryptBase64(user.getPassword());
        user.setPassword(decryptedPassword);

        boolean result = userService.addUser(user);
        if (result) {
            return RespResult.success("新增成功", true);
        }
        return RespResult.error("新增失败");
    }


    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public RespResult<User> findById(@PathVariable Integer id) {
        log.info("查询用户: {}", id);
        User user = userService.findById(id);
        if (user != null) {
            return RespResult.success(user);
        }
        return RespResult.error("用户不存在");
    }
}
