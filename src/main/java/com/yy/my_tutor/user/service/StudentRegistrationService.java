package com.yy.my_tutor.user.service;

import com.yy.my_tutor.user.domain.User;

/**
 * 学生注册服务接口
 */
public interface StudentRegistrationService {
    
    /**
     * 学生注册时自动分配课程和生成测试题
     * @param user 学生用户信息
     * @return 注册是否成功
     */
    boolean registerStudentWithCoursesAndTest(User user);
    
    /**
     * 根据年级分配课程（学习进度）
     * @param userId 学生ID
     * @param gradeLevel 年级等级
     * @return 分配是否成功
     */
    boolean assignCoursesByGrade(Integer userId, Integer gradeLevel);
    
    /**
     * 为指定年级生成测试题
     * @param gradeId 年级ID
     * @return 生成的测试题ID
     */
    Integer generateTestForGrade(Integer gradeId);
}
