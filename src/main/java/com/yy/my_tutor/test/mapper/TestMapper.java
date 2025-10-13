package com.yy.my_tutor.test.mapper;

import com.yy.my_tutor.test.domain.Test;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 测试Mapper接口
 */
@Mapper
public interface TestMapper {
    
    /**
     * 查询所有测试
     */
    List<Test> findAllTests();
    
    /**
     * 根据ID查询测试
     */
    Test findTestById(@Param("id") Integer id);
    
    /**
     * 根据年级ID查询测试
     */
    List<Test> findTestsByGradeId(@Param("gradeId") Integer gradeId);
    
    /**
     * 根据年级和难度查询测试
     */
    List<Test> findTestsByGradeAndDifficulty(@Param("gradeId") Integer gradeId, @Param("difficultyLevel") Integer difficultyLevel);
    
    /**
     * 根据测试类型查询测试
     */
    List<Test> findTestsByType(@Param("testType") Integer testType);
    
    /**
     * 插入测试
     */
    int insertTest(Test test);
    
    /**
     * 更新测试
     */
    int updateTest(Test test);
    
    /**
     * 删除测试（逻辑删除）
     */
    int deleteTest(@Param("id") Integer id);
}


