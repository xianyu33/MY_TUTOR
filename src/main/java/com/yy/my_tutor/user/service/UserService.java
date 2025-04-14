package com.yy.my_tutor.user.service;

import com.yy.my_tutor.user.domain.User;

public interface UserService {
    
    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param password 密码
     * @return 用户信息
     */
    User login(String userAccount, String password);
    
    /**
     * 用户注册
     * @param user 用户信息
     * @return 是否成功
     */
    boolean register(User user);
    
    /**
     * 新增用户
     * @param user 用户信息
     * @return 是否成功
     */
    boolean addUser(User user);
    
    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    User findById(Integer id);
} 