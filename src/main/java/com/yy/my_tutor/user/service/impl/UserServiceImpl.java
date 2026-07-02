package com.yy.my_tutor.user.service.impl;

import com.yy.my_tutor.common.AESUtil;
import com.yy.my_tutor.config.CustomException;
import com.yy.my_tutor.config.GoDaddyEmailSender;
import com.yy.my_tutor.config.RedisUtil;
import com.yy.my_tutor.security.JwtTokenUtil;
import com.yy.my_tutor.security.UserDetailsServiceImpl;
import com.yy.my_tutor.user.domain.User;
import com.yy.my_tutor.user.mapper.ParentMapper;
import com.yy.my_tutor.user.mapper.UserMapper;
import com.yy.my_tutor.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Resource
    private ParentMapper parentMapper;

    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private RedisUtil redisUtil;

    private String getResetPasswordKey(String email) {
        return ("RESET_PWD:" + email).replace("@", "_").replace(".", "_");
    }

    private String generateVerificationCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    @Override
    public User login(String userAccount, String password) {
        User user = userMapper.findByUserAccount(userAccount);
        if (user == null) {
            log.info("用户不存在: {}", userAccount);
            return null;
        }

        // 验证密码
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!Objects.equals(encryptedPassword, user.getPassword())) {
            log.info("密码错误: {}", userAccount);
            return null;
        }

        // 如果是老师（type=1），检查审批状态
        if (user.getType() != null && user.getType() == 1) {
            if (user.getApprovalStatus() == null || user.getApprovalStatus() == 0) {
                log.info("老师账号未审批，无法登录: {}", userAccount);
                return null;
            }
        }

        // 仅学生校验邮箱；家长/老师来自 parent 表，type、emailVerified 可能为 null
        if ("S".equals(user.getRole())
                && user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (user.getEmailVerified() == null || user.getEmailVerified() == 0) {
                log.info("学生邮箱未校验，无法登录: {}", userAccount);
                return null;
            }
        }

        // 检查账号是否已过期
        if (user.getExpireTime() != null && user.getExpireTime().before(new Date())) {
            log.info("账号已过期，无法登录: {}", userAccount);
            return null;
        }

        // 隐藏敏感信息
        user.setPassword(null);
        //增加 token
        String token = JwtTokenUtil.generateToken(user.getUsername());
        user.setToken(token);
        return user;
    }

    @Override
    public boolean register(User user) {
        if (user == null) {
            return false;
        }

        // 检查用户是否已存在
        User existingUser = userMapper.findByUserAccount(user.getUserAccount());
        if (existingUser != null) {
            log.info("用户已存在: {}", user.getUserAccount());
            return false;
        }

        // 设置初始值
        user.setCreateAt(new Date());
        user.setUpdateAt(new Date());
        user.setDeleteFlag("0");
        if (user.getEmailVerified() == null) {
            user.setEmailVerified(0);
        }
        if (user.getExpireTime() == null) {
            user.setExpireTime(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000));
        }

        // 密码加密
        String encryptedPassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8));
        user.setPassword(encryptedPassword);

        return userMapper.insert(user) > 0;
    }

    @Override
    public boolean addUser(User user) {
        if (user == null) {
            return false;
        }

        // 设置初始值
        user.setCreateAt(new Date());
        user.setUpdateAt(new Date());
        user.setDeleteFlag("0");
        if (user.getEmailVerified() == null) {
            user.setEmailVerified(0);
        }
        if (user.getExpireTime() == null) {
            user.setExpireTime(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000));
        }

        // 密码加密
        String encryptedPassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8));
        user.setPassword(encryptedPassword);

        return userMapper.insert(user) > 0;
    }

    @Override
    public User findById(Integer id) {
        User user = userMapper.findById(id);
        if (user != null) {
            // 隐藏敏感信息
            user.setPassword(null);
        }
        return user;
    }

    @Override
    public Boolean findByUserAccount(User user) {
        // 检查用户是否已存在
        User existingUser = userMapper.findByUserAccount(user.getUserAccount());
        if (existingUser != null) {
            log.info("用户已存在: {}", user.getUserAccount());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User find(User user) {
        User res;
        // 隐藏敏感信息
        if (user.getRole().equals("S")) {
            res = userMapper.findById(user.getId());
        } else {
            res = userMapper.findParentById(user.getId());
        }
        if (res != null) {
            // 隐藏敏感信息
            res.setPassword(null);
        }
        return res;
    }

    @Override
    public User edit(User user) {
        if (user.getRole().equals("S")) {
            userMapper.update(user);
        } else {
            userMapper.updateParent(user);
        }
        return null;
    }

    @Override
    public User changeCredentials(User user) {
        if (user.getId() == null || !StringUtils.hasText(user.getRole())) {
            throw new CustomException("用户信息不完整");
        }
        if (!StringUtils.hasText(user.getUserAccount())) {
            throw new CustomException("账号不能为空");
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new CustomException("密码不能为空");
        }

        User existing = find(user);
        if (existing == null) {
            throw new CustomException("用户不存在");
        }

        if (!existing.getUserAccount().equals(user.getUserAccount())) {
            User conflict = userMapper.findByUserAccount(user.getUserAccount());
            if (conflict != null
                    && (!conflict.getId().equals(user.getId()) || !user.getRole().equals(conflict.getRole()))) {
                throw new CustomException("账号已存在");
            }
        }

        String decryptedPassword = AESUtil.decryptBase64(user.getPassword());
        String encryptedPassword = DigestUtils.md5DigestAsHex(decryptedPassword.getBytes(StandardCharsets.UTF_8));

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setRole(user.getRole());
        updateUser.setUserAccount(user.getUserAccount());
        updateUser.setPassword(encryptedPassword);
        updateUser.setUpdateAt(new Date());
        edit(updateUser);

        User updated = find(user);
        if (updated != null) {
            updated.setPassword(null);
        }
        return updated;
    }

    @Override
    public void sendResetPasswordCode(User user) {
        User existing = resolveUserForPasswordReset(user);
        if (!StringUtils.hasText(existing.getEmail())) {
            throw new CustomException("未绑定邮箱，无法重置密码");
        }

        String code = generateVerificationCode();
        redisUtil.set(getResetPasswordKey(existing.getEmail()), code, 5 * 60);
        GoDaddyEmailSender.send(existing.getEmail(), code);
    }

    @Override
    public void resetPassword(User user) {
        if (!StringUtils.hasText(user.getVerificationCode())) {
            throw new CustomException("验证码不能为空");
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new CustomException("密码不能为空");
        }

        User existing = resolveUserForPasswordReset(user);
        if (!StringUtils.hasText(existing.getEmail())) {
            throw new CustomException("未绑定邮箱，无法重置密码");
        }

        String redisKey = getResetPasswordKey(existing.getEmail());
        String storedCode = redisUtil.get(redisKey);
        if (!StringUtils.hasText(storedCode) || !storedCode.equals(user.getVerificationCode())) {
            throw new CustomException("验证码错误或已过期");
        }

        String decryptedPassword = AESUtil.decryptBase64(user.getPassword());
        String encryptedPassword = DigestUtils.md5DigestAsHex(decryptedPassword.getBytes(StandardCharsets.UTF_8));

        User updateUser = new User();
        updateUser.setId(existing.getId());
        updateUser.setRole(existing.getRole());
        updateUser.setPassword(encryptedPassword);
        updateUser.setUpdateAt(new Date());
        edit(updateUser);

        redisUtil.delete(redisKey);
    }

    private User resolveUserForPasswordReset(User user) {
        if (StringUtils.hasText(user.getUserAccount())) {
            User existing = userMapper.findByUserAccount(user.getUserAccount());
            if (existing == null) {
                throw new CustomException("用户不存在");
            }
            return existing;
        }
        if (user.getId() != null && StringUtils.hasText(user.getRole())) {
            User existing = find(user);
            if (existing == null) {
                throw new CustomException("用户不存在");
            }
            return existing;
        }
        throw new CustomException("用户信息不完整");
    }

    @Override
    public boolean addUsers(List<User> users) {
        if (users == null || users.isEmpty()) {
            return false;
        }
        for (User user : users) {
            user.setCreateAt(new Date());
            user.setUpdateAt(new Date());
            user.setDeleteFlag("0");
            if (user.getEmailVerified() == null) {
                user.setEmailVerified(0);
            }
            if (user.getExpireTime() == null) {
                user.setExpireTime(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000));
            }
            String encryptedPassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8));
            user.setPassword(encryptedPassword);
            userMapper.insert(user);
        }
        return true;
    }
    
    @Override
    public List<User> findStudentsByName(String name) {
        // 隐藏敏感信息
        List<User> students = userMapper.findStudentsByName(name);
        if (students != null) {
            students.forEach(student -> student.setPassword(null));
        }
        return students;
    }
}
