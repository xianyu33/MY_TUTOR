package com.yy.my_tutor.user.service.impl;

import com.yy.my_tutor.math.domain.Grade;
import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.math.domain.LearningProgress;
import com.yy.my_tutor.math.service.GradeService;
import com.yy.my_tutor.math.service.KnowledgePointService;
import com.yy.my_tutor.math.service.LearningProgressService;
import com.yy.my_tutor.test.domain.Test;
import com.yy.my_tutor.test.service.TestService;
import com.yy.my_tutor.user.domain.User;
import com.yy.my_tutor.user.mapper.UserMapper;
import com.yy.my_tutor.user.service.StudentRegistrationService;
import com.yy.my_tutor.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * 学生注册服务实现类
 */
@Slf4j
@Service
public class StudentRegistrationServiceImpl implements StudentRegistrationService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private GradeService gradeService;
    
    @Autowired
    private KnowledgePointService knowledgePointService;
    
    @Autowired
    private LearningProgressService learningProgressService;
    
    @Autowired
    private TestService testService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean registerStudentWithCoursesAndTest(User user) {
        try {
            // 1. 先注册学生
            boolean registerResult = userService.register(user);
            if (!registerResult) {
                log.error("学生注册失败: {}", user.getUserAccount());
                return false;
            }
            
            // 2. 获取注册后的学生ID
            User registeredUser = userMapper.findByUserAccount(user.getUserAccount());
            if (registeredUser == null) {
                log.error("注册后无法找到学生: {}", user.getUserAccount());
                return false;
            }
            
            // 3. 根据年级分配课程
            Integer gradeLevel = parseGradeLevel(user.getGrade());
            if (gradeLevel != null) {
                boolean assignResult = assignCoursesByGrade(registeredUser.getId(), gradeLevel);
                if (!assignResult) {
                    log.warn("课程分配失败，但学生注册成功: {}", user.getUserAccount());
                }
                
                // 4. 生成测试题
                Grade grade = gradeService.findGradeByLevel(gradeLevel);
                if (grade != null) {
                    Integer testId = generateTestForGrade(grade.getId());
                    if (testId != null) {
                        log.info("为学生 {} 生成测试题成功，测试ID: {}", user.getUserAccount(), testId);
                    } else {
                        log.warn("测试题生成失败，但学生注册成功: {}", user.getUserAccount());
                    }
                }
            } else {
                log.warn("无法解析年级信息: {}", user.getGrade());
            }
            
            return true;
        } catch (Exception e) {
            log.error("学生注册过程中发生异常: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignCoursesByGrade(Integer userId, Integer gradeLevel) {
        try {
            // 1. 根据年级等级获取年级信息
            Grade grade = gradeService.findGradeByLevel(gradeLevel);
            if (grade == null) {
                log.error("找不到年级等级为 {} 的年级信息", gradeLevel);
                return false;
            }
            
            // 2. 根据年级ID获取所有知识点
            List<KnowledgePoint> knowledgePoints = knowledgePointService.findKnowledgePointsByGradeId(grade.getId());
            if (knowledgePoints == null || knowledgePoints.isEmpty()) {
                log.warn("年级 {} 没有找到知识点", grade.getGradeName());
                return true; // 没有知识点也算成功
            }
            
            // 3. 为每个知识点创建学习进度记录
            int successCount = 0;
            for (KnowledgePoint knowledgePoint : knowledgePoints) {
                try {
                    LearningProgress progress = new LearningProgress();
                    progress.setUserId(userId);
                    progress.setKnowledgePointId(knowledgePoint.getId());
                    progress.setProgressStatus(1); // 未开始
                    progress.setCompletionPercentage(BigDecimal.ZERO);
                    progress.setStudyDuration(0);
                    progress.setCreateAt(new Date());
                    progress.setUpdateAt(new Date());
                    progress.setDeleteFlag("N");
                    
                    LearningProgress result = learningProgressService.insertLearningProgress(progress);
                    if (result != null) {
                        successCount++;
                    }
                } catch (Exception e) {
                    log.error("为知识点 {} 创建学习进度失败: {}", knowledgePoint.getId(), e.getMessage());
                }
            }
            
            log.info("为学生 {} 分配课程完成，成功创建 {} 个学习进度记录", userId, successCount);
            return successCount > 0;
            
        } catch (Exception e) {
            log.error("分配课程过程中发生异常: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public Integer generateTestForGrade(Integer gradeId) {
        try {
            // 生成测试题，默认难度为2（中等），题目数量为10
            Test test = testService.generateTest(gradeId, 2, 10);
            if (test != null) {
                log.info("为年级 {} 生成测试题成功，测试ID: {}", gradeId, test.getId());
                return test.getId();
            } else {
                log.error("为年级 {} 生成测试题失败", gradeId);
                return null;
            }
        } catch (Exception e) {
            log.error("生成测试题过程中发生异常: {}", e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 解析年级字符串为年级等级
     * @param gradeStr 年级字符串，如 "9", "九年级", "初三" 等
     * @return 年级等级，如 9
     */
    private Integer parseGradeLevel(String gradeStr) {
        if (gradeStr == null || gradeStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            // 尝试直接解析为数字
            return Integer.parseInt(gradeStr.trim());
        } catch (NumberFormatException e) {
            // 如果不是纯数字，尝试根据年级名称解析
            String grade = gradeStr.trim();
            switch (grade) {
                case "一年级":
                case "1年级":
                    return 1;
                case "二年级":
                case "2年级":
                    return 2;
                case "三年级":
                case "3年级":
                    return 3;
                case "四年级":
                case "4年级":
                    return 4;
                case "五年级":
                case "5年级":
                    return 5;
                case "六年级":
                case "6年级":
                    return 6;
                case "七年级":
                case "7年级":
                case "初一":
                    return 7;
                case "八年级":
                case "8年级":
                case "初二":
                    return 8;
                case "九年级":
                case "9年级":
                case "初三":
                    return 9;
                case "高一":
                case "10年级":
                    return 10;
                case "高二":
                case "11年级":
                    return 11;
                case "高三":
                case "12年级":
                    return 12;
                default:
                    log.warn("无法解析年级字符串: {}", gradeStr);
                    return null;
            }
        }
    }
}
