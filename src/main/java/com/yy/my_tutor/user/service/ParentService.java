package com.yy.my_tutor.user.service;

import com.yy.my_tutor.user.domain.Parent;
import com.yy.my_tutor.user.domain.User;
import java.util.List;

public interface ParentService {
    Parent findById(Integer id);
    List<Parent> findAll();
    boolean addParent(Parent parent);
    boolean updateParent(Parent parent);
    boolean deleteById(Integer id);
    List<Parent> findByUserAccount(String userAccount);
    boolean addParentWithUsers(Parent parent, List<User> users);
} 