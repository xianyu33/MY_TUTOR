package com.yy.my_tutor.user.mapper;

import com.yy.my_tutor.user.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 根据用户账号查询用户
     * @param userAccount 用户账号
     * @return 用户信息
     */
    User findByUserAccount(String userAccount);

    /**
     * 插入新用户
     * @param user 用户信息
     * @return 影响行数
     */
    int insert(User user);

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    User findById(Integer id);

    List<User> findChild(Integer id);

    User findParentById(Integer id);

    void update(User user);

    void updateParent(User user);
    
    /**
     * 根据名称动态查询学生列表
     * @param name 学生姓名（模糊查询）
     * @return 学生列表
     */
    List<User> findStudentsByName(@Param("name") String name);
}
