package com.yy.my_tutor.user.controller;

import com.alibaba.fastjson.JSON;
import com.yy.my_tutor.common.AESUtil;
import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.user.domain.User;
import com.yy.my_tutor.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        System.out.println(AESUtil.encryptBase64("123456"));
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
