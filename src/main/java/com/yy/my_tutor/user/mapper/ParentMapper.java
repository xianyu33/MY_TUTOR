package com.yy.my_tutor.user.mapper;

import com.yy.my_tutor.user.domain.Parent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ParentMapper {
    Parent findById(Integer id);
    List<Parent> findAll();
    int insert(Parent parent);
    int update(Parent parent);
    int deleteById(Integer id);
    List<Parent> findByUserAccount(String userAccount);
    
    /**
     * 查询未审批的老师（支持名称、电话、邮箱查询）
     * @param name 名称（模糊查询）
     * @param tel 电话（模糊查询）
     * @param email 邮箱（模糊查询）
     * @return 未审批的老师列表
     */
    List<Parent> findUnapprovedTeachers(@Param("name") String name, @Param("tel") String tel, @Param("email") String email);
    
    /**
     * 审批通过老师
     * @param id 老师ID
     * @return 影响行数
     */
    int approveTeacher(Integer id);
    
    /**
     * 查询已审批通过的老师（支持名称、电话、邮箱查询）
     * @param name 名称（模糊查询）
     * @param tel 电话（模糊查询）
     * @param email 邮箱（模糊查询）
     * @return 已审批通过的老师列表
     */
    List<Parent> findApprovedTeachers(@Param("name") String name, @Param("tel") String tel, @Param("email") String email);
} 