package com.yy.my_tutor.user.controller;

import com.alibaba.fastjson.JSON;
import com.yy.my_tutor.common.AESUtil;
import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.config.CustomException;
import com.yy.my_tutor.config.EmailUtil;
import com.yy.my_tutor.config.GoDaddyEmailSender;
import com.yy.my_tutor.config.RedisUtil;
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

    @Resource
    RedisUtil redisUtil;




    public static void main(String[] args) {
        System.out.println(AESUtil.decryptBase64("CH+8oJbnh+IfILiWQWY5OpfP5u2cbBEKWWVBg2bJ+NcLRPh3t8vxgQE2T1M2RRNzH9Sue8RgS4i14VES15YQM/9PlSxiWa0PJ8tI5B2i4iXl4gDYOswtWgxQGVuNCY5RYCQcGz75XoYSpC/Eybcbvz9N8g4QlL2pDBLGrBQW1aM/57QVmmsFMD6+ZXPAGXStQBxaCHa3Or9Z+3f4wD4y5XYVC6uMk6WuQmvcS68e4Z0etI9KrctCFF+70lLEcIBz"));
    }


    // 生成6位随机数字验证码
    public static String generateCode() {
        return JSON.toJSONString((int) ((Math.random() * 9 + 1) * 100000));
    }

    /**
     * 发送验证码
     */
    @PostMapping("/verificationCode")
    public RespResult<User> verificationCode(@RequestBody User userVo) {
        if (userVo.getEmail().isEmpty()) {
            throw new CustomException("邮箱不能为null");
        }
        String code = generateCode();
        String redisKey = getVerificationKey(userVo.getEmail());
        redisUtil.set(redisKey, code, 5 * 60);
//            EmailUtil.sendVerificationCode(userVo.getEmail(), code);
        GoDaddyEmailSender.send(userVo.getEmail(), code);
        return RespResult.success("发送成功");
    }


    private String getVerificationKey(String email) {
        String replace = ("CODE:" + email).replace("@", "_");
        return replace.replace(".", "_");
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
     * 校验验证码
     */
    @PostMapping("/verification")
    public RespResult<Boolean> verification(@RequestBody User user) {
        log.info("校验验证码: {}", user.getVerificationCode());
        Boolean b = redisUtil.hasKey(getVerificationKey(user.getEmail()));
        if (user.getVerificationCode().isEmpty() || !b) {
            throw new CustomException("验证码失效");
        }
        String redisKey = getVerificationKey(user.getEmail());
        String s = redisUtil.get(redisKey);

        if (null != s) {
            String substring = s.substring(1, s.length() - 1);
            if ( substring.equals(user.getVerificationCode())) {
                return RespResult.success("验证通过");
            } else {
                throw new CustomException("验证失败");
            }
        } else {
            throw new CustomException("验证失败");
        }
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
     * 用户注册
     */
    @PostMapping("/existAccount")
    public RespResult<Boolean> existAccount(@RequestBody User user) {
        log.info("用户注册: {}", user.getUserAccount());

        Boolean flag = userService.findByUserAccount(user);

        return flag ? RespResult.error("账号已存在") : RespResult.success("账号可使用");
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
