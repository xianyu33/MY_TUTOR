package com.yy.my_tutor.user.service.impl;

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
    public boolean addUsers(List<User> users) {
        if (users == null || users.isEmpty()) {
            return false;
        }
        for (User user : users) {
            user.setCreateAt(new Date());
            user.setUpdateAt(new Date());
            user.setDeleteFlag("0");
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
