package com.yy.my_tutor.user.controller;

import com.alibaba.fastjson.JSON;
import com.yy.my_tutor.common.AESUtil;
import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.config.CustomException;
import com.yy.my_tutor.config.EmailUtil;
import com.yy.my_tutor.config.GoDaddyEmailSender;
import com.yy.my_tutor.config.RedisUtil;
import com.yy.my_tutor.user.domain.StudentSearchRequest;
import com.yy.my_tutor.user.domain.User;
import com.yy.my_tutor.user.mapper.UserMapper;
import com.yy.my_tutor.user.service.StudentRegistrationService;
import com.yy.my_tutor.user.service.UserService;
import com.yy.my_tutor.user.service.WelcomeEmailService;
import com.yy.my_tutor.user.service.EmailVerificationService;
import com.yy.my_tutor.util.CaptchaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentRegistrationService studentRegistrationService;

    @Autowired
    private WelcomeEmailService welcomeEmailService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private UserMapper userMapper;

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
     * 获取登录验证码
     */
    @PostMapping("/getLoginCaptcha")
    public RespResult<Map<String, String>> getLoginCaptcha() {
        String[] captchaData = CaptchaUtil.generateCaptchaWithImage();
        String captcha = captchaData[0];
        String captchaImage = captchaData[1];
        String captchaId = UUID.randomUUID().toString();
        String redisKey = "CAPTCHA:" + captchaId;
        // 验证码有效期5分钟
        redisUtil.set(redisKey, captcha, 5 * 60);

        Map<String, String> result = new HashMap<>();
        result.put("captchaId", captchaId);
        result.put("captchaImage", captchaImage);

        log.info("生成登录验证码: {}, ID: {}", captcha, captchaId);
        return RespResult.success("获取验证码成功", result);
    }


    /**
     * 用户登录
     */
    @PostMapping("/login")
    public RespResult<User> login(@RequestBody User userVo) {
        log.info("用户登录: {}", JSON.toJSONString(userVo));

        // 验证验证码
        if (userVo.getCaptchaId() == null || userVo.getCaptchaId().isEmpty()) {
            throw new CustomException("验证码ID不能为空");
        }
        if (userVo.getCaptcha() == null || userVo.getCaptcha().isEmpty()) {
            throw new CustomException("验证码不能为空");
        }

        String redisKey = "CAPTCHA:" + userVo.getCaptchaId();
        String storedCaptcha = redisUtil.get(redisKey);
        if (storedCaptcha == null) {
            throw new CustomException("验证码已过期，请重新获取");
        }

        if (!CaptchaUtil.validateCaptcha(userVo.getCaptcha(), storedCaptcha)) {
            throw new CustomException("验证码错误");
        }

        // 验证通过后删除验证码
        redisUtil.delete(redisKey);

        // 解密密码
        String decryptedPassword = AESUtil.decryptBase64(userVo.getPassword());
        userVo.setPassword(decryptedPassword);

        User user = userService.login(userVo.getUserAccount(), userVo.getPassword());

        if (user != null) {
            return RespResult.success("login success", user);
        }

        // 检查是否是未审批的老师
        User checkUser = userMapper.findByUserAccount(userVo.getUserAccount());
        if (checkUser != null && checkUser.getType() != null && checkUser.getType() == 1) {
            if (checkUser.getApprovalStatus() == null || checkUser.getApprovalStatus() == 0) {
                return RespResult.error("Your account has not been approved yet, Please wait for administrator approval.");
            }
        }

        // 检查邮箱是否未校验（仅对学生用户，且有邮箱的才需要校验）
        if (checkUser != null && "S".equals(checkUser.getRole())
                && checkUser.getEmail() != null && !checkUser.getEmail().isEmpty()) {
            if (checkUser.getEmailVerified() == null || checkUser.getEmailVerified() == 0) {
                return RespResult.error("Please verify your email first. A verification link has been sent to your email.");
            }
        }

        // 检查账号是否已过期
        if (checkUser != null && checkUser.getExpireTime() != null
                && checkUser.getExpireTime().before(new Date())) {
            return RespResult.error("Your account has expired. Please contact the administrator to renew.");
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
    @Transactional(rollbackFor = Exception.class)
    public RespResult<Boolean> register(@RequestBody User user) {
        log.info("用户注册: {}", user.getUserAccount());

        // 解密密码
        String decryptedPassword = AESUtil.decryptBase64(user.getPassword());
        user.setPassword(decryptedPassword);

        // 使用新的学生注册服务，自动分配课程和生成测试题
        boolean result = studentRegistrationService.registerStudentWithCoursesAndTest(user);
        if (result) {
            // register() 会把 emailVerified 设为 0，不能仅用 == null 判断
            if (needsEmailVerification(user)) {
                final String email = user.getEmail();
                final String username = user.getUsername();
                scheduleVerificationEmailAfterCommit(email, username);
                return RespResult.success("注册成功，已自动分配课程和生成测试题。请前往邮箱完成校验后登录", true);
            }
            return RespResult.success("注册成功，已自动分配课程和生成测试题", true);
        }
        return RespResult.error("注册失败，用户可能已存在");
    }

    private boolean needsEmailVerification(User user) {
        return StringUtils.hasText(user.getEmail()) && !Integer.valueOf(1).equals(user.getEmailVerified());
    }

    /** 事务提交后再发校验邮件，避免异步线程早于入库完成。 */
    private void scheduleVerificationEmailAfterCommit(String email, String username) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    emailVerificationService.sendVerificationEmailAsync(email, username);
                }
            });
        } else {
            emailVerificationService.sendVerificationEmailAsync(email, username);
        }
    }

    /**
     * 邮箱校验
     */
    @PostMapping("/verify-email")
    public RespResult<Boolean> verifyEmail(@RequestBody Map<String, String> params) {
        String code = params.get("code");
        if (!StringUtils.hasText(code)) {
            throw new CustomException("校验码不能为空");
        }

        boolean verified = emailVerificationService.verifyEmail(code);
        if (verified) {
            return RespResult.success("邮箱校验成功，现在可以登录了", true);
        }
        return RespResult.error("校验链接无效或已过期");
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
    @PostMapping("/findById")
    public RespResult<User> findById(@RequestBody User user) {
        log.info("查询用户: {}", JSON.toJSONString(user));
        User res = userService.find(user);
        if (res != null) {
            return RespResult.success(res);
        }
        return RespResult.error("用户不存在");
    }

    /**
     * 修改用户信息
     */
    @PostMapping("/edit")
    public RespResult<User> edit(@RequestBody User user) {
        log.info("修改用户信息: {}", JSON.toJSONString(user));
        userService.edit(user);
        return RespResult.success("更新成功");
    }

    /**
     * 修改登录账号与密码
     */
    @PostMapping("/changeCredentials")
    public RespResult<User> changeCredentials(@RequestBody User user) {
        log.info("修改账号密码: id={}, role={}, userAccount={}", user.getId(), user.getRole(), user.getUserAccount());
        User updated = userService.changeCredentials(user);
        return RespResult.success("账号密码修改成功", updated);
    }

    /**
     * 发送重置密码邮箱验证码
     */
    @PostMapping("/resetPasswordCode")
    public RespResult<Boolean> sendResetPasswordCode(@RequestBody User user) {
        log.info("发送重置密码验证码: userAccount={}", user.getUserAccount());
        userService.sendResetPasswordCode(user);
        return RespResult.success("验证码已发送至邮箱", true);
    }

    /**
     * 通过邮箱验证码重置密码
     */
    @PostMapping("/resetPassword")
    public RespResult<Boolean> resetPassword(@RequestBody User user) {
        log.info("重置密码: userAccount={}", user.getUserAccount());
        userService.resetPassword(user);
        return RespResult.success("密码重置成功", true);
    }

    /**
     * 根据名称动态查询学生列表（POST请求）
     * @param request 搜索请求参数
     * @return 学生列表
     */
    @PostMapping("/search/students")
    public RespResult<List<User>> searchStudents(@RequestBody User user) {
        log.info("搜索学生，关键字: {}", user);

        List<User> students = userService.findStudentsByName(user.getUsername());
        return RespResult.success(students);
    }
}
