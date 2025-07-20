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
}