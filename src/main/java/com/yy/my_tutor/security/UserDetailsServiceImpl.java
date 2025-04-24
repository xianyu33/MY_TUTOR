package com.yy.my_tutor.security;

import com.yy.my_tutor.user.domain.User;
import com.yy.my_tutor.user.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author yy
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    UserMapper userInfoMapper;

    @Override
    public LoginUser loadUserByUsername(String s) throws UsernameNotFoundException {

        User userInfoRequestBody = new User();
        userInfoRequestBody.setUserAccount(s);
        User userInfos = userInfoMapper.findByUserAccount(s);
        if (null == userInfos) {
            throw new RuntimeException("用户名或者密码错误");
        }
        return new LoginUser(userInfos);

    }
}
