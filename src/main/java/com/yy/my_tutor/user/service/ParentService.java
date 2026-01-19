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

    List<User> findChild(Parent parent);
    
    /**
     * 查询未审批的老师（支持名称、电话、邮箱查询）
     * @param name 名称（模糊查询，可选）
     * @param tel 电话（模糊查询，可选）
     * @param email 邮箱（模糊查询，可选）
     * @return 未审批的老师列表
     */
    List<Parent> findUnapprovedTeachers(String name, String tel, String email);
    
    /**
     * 审批通过老师
     * @param id 老师ID
     * @return 是否成功
     */
    boolean approveTeacher(Integer id);
    
    /**
     * 查询已审批通过的老师（支持名称、电话、邮箱查询）
     * @param name 名称（模糊查询，可选）
     * @param tel 电话（模糊查询，可选）
     * @param email 邮箱（模糊查询，可选）
     * @return 已审批通过的老师列表
     */
    List<Parent> findApprovedTeachers(String name, String tel, String email);
}