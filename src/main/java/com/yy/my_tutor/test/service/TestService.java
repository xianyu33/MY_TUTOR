package com.yy.my_tutor.test.service;

import com.yy.my_tutor.test.domain.Test;

import java.util.List;

/**
 * 测试服务接口
 */
public interface TestService {
    
    /**
     * 查询所有测试
     */
    List<Test> findAllTests();
    
    /**
     * 根据ID查询测试
     */
    Test findTestById(Integer id);
    
    /**
     * 根据年级ID查询测试
     */
    List<Test> findTestsByGradeId(Integer gradeId);
    
    /**
     * 根据年级和难度查询测试
     */
    List<Test> findTestsByGradeAndDifficulty(Integer gradeId, Integer difficultyLevel);
    
    /**
     * 根据测试类型查询测试
     */
    List<Test> findTestsByType(Integer testType);
    
    /**
     * 生成测试题目
     */
    Test generateTest(Integer gradeId, Integer difficultyLevel, Integer questionCount);
    
    /**
     * 新增测试
     */
    Test addTest(Test test);
    
    /**
     * 更新测试
     */
    Test updateTest(Test test);
    
    /**
     * 删除测试
     */
    boolean deleteTest(Integer id);
}


