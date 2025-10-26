package com.yy.my_tutor.user.service;

import com.yy.my_tutor.user.domain.User;
import java.util.List;

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
     * 新增多个用户
     * @param users 用户列表
     * @return 是否成功
     */
    boolean addUsers(List<User> users);

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    User findById(Integer id);

    Boolean findByUserAccount(User user);

    User find(User user);

    User edit(User user);
    
    /**
     * 根据名称动态查询学生列表
     * @param name 学生姓名（模糊查询，可选）
     * @return 学生列表
     */
    List<User> findStudentsByName(String name);
}
