package com.yy.my_tutor.user.service.impl;

import com.yy.my_tutor.common.AESUtil;
import com.yy.my_tutor.user.domain.Parent;
import com.yy.my_tutor.user.mapper.ParentMapper;
import com.yy.my_tutor.user.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yy.my_tutor.user.domain.User;
import com.yy.my_tutor.user.mapper.UserMapper;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
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
        if (parent == null) {
            return false;
        }
        parent.setCreateAt(new Date());
        parent.setDeleteFlag("0");
        String decryptedPassword = AESUtil.decryptBase64(parent.getPassword());
        parent.setPassword(DigestUtils.md5DigestAsHex(decryptedPassword.getBytes(StandardCharsets.UTF_8)));
        parentMapper.insert(parent);
        Integer parentId = parent.getId();
        if (!users.isEmpty()) {
            for (User user : users) {
                user.setParentId(parentId);
                user.setCreateAt(new Date());
                user.setUpdateAt(new Date());
                user.setDeleteFlag("0");
                String decryptedUserPassword = AESUtil.decryptBase64(user.getPassword());
                String encryptedPassword = DigestUtils.md5DigestAsHex(decryptedUserPassword.getBytes(StandardCharsets.UTF_8));
                user.setPassword(encryptedPassword);
                userMapper.insert(user);
            }
        }
        return true;
    }

    @Override
    public List<User> findChild(Parent parent) {
        return userMapper.findChild(parent.getId());
    }
}
