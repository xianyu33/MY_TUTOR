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
} 