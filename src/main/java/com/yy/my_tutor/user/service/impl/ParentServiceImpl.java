package com.yy.my_tutor.user.service.impl;

import com.yy.my_tutor.user.domain.Parent;
import com.yy.my_tutor.user.mapper.ParentMapper;
import com.yy.my_tutor.user.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yy.my_tutor.user.domain.User;
import com.yy.my_tutor.user.mapper.UserMapper;

import java.util.Date;
import java.util.List;

@Service
public class ParentServiceImpl implements ParentService {

    @Autowired
    private ParentMapper parentMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Parent findById(Integer id) {
        return parentMapper.findById(id);
    }

    @Override
    public List<Parent> findAll() {
        return parentMapper.findAll();
    }

    @Override
    public boolean addParent(Parent parent) {
        if (parent.getCreateAt() == null) {
            parent.setCreateAt(new Date());
        }
        parent.setDeleteFlag("0");
        return parentMapper.insert(parent) > 0;
    }

    @Override
    public boolean updateParent(Parent parent) {
        parent.setUpdateAt(new Date());
        return parentMapper.update(parent) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return parentMapper.deleteById(id) > 0;
    }

    @Override
    public List<Parent> findByUserAccount(String userAccount) {
        return parentMapper.findByUserAccount(userAccount);
    }

    @Override
    @Transactional
    public boolean addParentWithUsers(Parent parent, List<User> users) {
        if (parent == null || users == null || users.isEmpty()) {
            return false;
        }
        parent.setCreateAt(new Date());
        parent.setDeleteFlag("0");
        parentMapper.insert(parent);
        Integer parentId = parent.getId();
        for (User user : users) {
            user.setParentId(parentId);
            user.setCreateAt(new Date());
            user.setUpdateAt(new Date());
            user.setDeleteFlag("0");
            String encryptedPassword = org.springframework.util.DigestUtils.md5DigestAsHex(user.getPassword().getBytes(java.nio.charset.StandardCharsets.UTF_8));
            user.setPassword(encryptedPassword);
            userMapper.insert(user);
        }
        return true;
    }
} 