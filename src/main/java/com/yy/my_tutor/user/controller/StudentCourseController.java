package com.yy.my_tutor.user.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.math.domain.KnowledgeCategory;
import com.yy.my_tutor.user.domain.LearningProgressStats;
import com.yy.my_tutor.user.domain.StudentCategoryBinding;
import com.yy.my_tutor.user.service.StudentRegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学生课程管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/student/course")
@CrossOrigin(origins = "*")
public class StudentCourseController {

    @Autowired
    private StudentRegistrationService studentRegistrationService;

    /**
     * 为现有学生分配课程
     * @param userId 学生ID
     * @param gradeLevel 年级等级
     * @param forceUpdate 是否强制更新
     * @return 分配结果
     */
    @PostMapping("/assign")
    public RespResult<Boolean> assignCourses(
            @RequestParam Integer userId,
            @RequestParam Integer gradeLevel,
            @RequestParam(defaultValue = "false") Boolean forceUpdate) {
        
        log.info("为学生 {} 分配课程，年级等级: {}, 强制更新: {}", userId, gradeLevel, forceUpdate);
        
        try {
            boolean result = studentRegistrationService.reassignCoursesByGrade(userId, gradeLevel, forceUpdate);
            if (result) {
                return RespResult.success("课程分配成功", true);
            } else {
                return RespResult.error("课程分配失败");
            }
        } catch (Exception e) {
            log.error("课程分配过程中发生异常: {}", e.getMessage(), e);
            return RespResult.error("课程分配失败: " + e.getMessage());
        }
    }

    /**
     * 获取学生的学习进度统计
     * @param userId 学生ID
     * @return 学习进度统计
     */
    @GetMapping("/progress-stats/{userId}")
    public RespResult<LearningProgressStats> getLearningProgressStats(@PathVariable Integer userId) {
        log.info("获取学生 {} 的学习进度统计", userId);
        
        try {
            LearningProgressStats stats = studentRegistrationService.getLearningProgressStats(userId);
            return RespResult.success("获取学习进度统计成功", stats);
        } catch (Exception e) {
            log.error("获取学习进度统计过程中发生异常: {}", e.getMessage(), e);
            return RespResult.error("获取学习进度统计失败: " + e.getMessage());
        }
    }

    /**
     * 重新分配学生课程（根据学生当前年级）
     * @param userId 学生ID
     * @param forceUpdate 是否强制更新
     * @return 分配结果
     */
    @PostMapping("/reassign/{userId}")
    public RespResult<Boolean> reassignCoursesByStudentGrade(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "false") Boolean forceUpdate) {
        
        log.info("根据学生年级重新分配课程，学生ID: {}, 强制更新: {}", userId, forceUpdate);
        
        try {
            // 这里需要先获取学生的年级信息，然后调用分配方法
            // 由于需要获取学生信息，这里暂时返回错误，建议使用上面的assign接口
            return RespResult.error("请使用 /assign 接口，需要提供年级等级参数");
        } catch (Exception e) {
            log.error("重新分配课程过程中发生异常: {}", e.getMessage(), e);
            return RespResult.error("重新分配课程失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取学生绑定的知识点分类列表
     * @param userId 学生ID
     * @return 学生绑定的知识点分类列表
     */
    @GetMapping("/bound-categories/{userId}")
    public RespResult<List<KnowledgeCategory>> getStudentBoundCategories(@PathVariable Integer userId) {
        log.info("获取学生 {} 绑定的知识点分类列表", userId);
        
        try {
            List<KnowledgeCategory> categories = studentRegistrationService.getStudentBoundCategories(userId);
            return RespResult.success("获取学生绑定分类列表成功", categories);
        } catch (Exception e) {
            log.error("获取学生绑定分类过程中发生异常: {}", e.getMessage(), e);
            return RespResult.error("获取学生绑定分类失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据年级获取知识点分类列表
     * @param gradeId 年级ID
     * @return 知识点分类列表
     */
    @GetMapping("/categories/grade/{gradeId}")
    public RespResult<List<KnowledgeCategory>> getCategoriesByGrade(@PathVariable Integer gradeId) {
        log.info("获取年级 {} 的知识点分类列表", gradeId);
        
        try {
            List<KnowledgeCategory> categories = studentRegistrationService.getCategoriesByGrade(gradeId);
            return RespResult.success("获取年级分类列表成功", categories);
        } catch (Exception e) {
            log.error("获取年级分类过程中发生异常: {}", e.getMessage(), e);
            return RespResult.error("获取年级分类失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取学生分类学习进度详情（包含知识点学习进度）
     * @param userId 学生ID
     * @return 学生分类学习进度详情列表
     */
    @GetMapping("/category-progress/{userId}")
    public RespResult<List<StudentCategoryBinding>> getStudentCategoryProgressDetails(@PathVariable Integer userId) {
        log.info("获取学生 {} 的分类学习进度详情", userId);
        
        try {
            List<StudentCategoryBinding> details = studentRegistrationService.getStudentCategoryProgressDetails(userId);
            return RespResult.success("获取学生分类学习进度详情成功", details);
        } catch (Exception e) {
            log.error("获取学生分类学习进度详情过程中发生异常: {}", e.getMessage(), e);
            return RespResult.error("获取学生分类学习进度详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取学生指定分类的学习进度详情
     * @param userId 学生ID
     * @param categoryId 分类ID
     * @return 分类学习进度详情
     */
    @GetMapping("/category-progress/{userId}/{categoryId}")
    public RespResult<StudentCategoryBinding> getStudentCategoryProgressDetail(
            @PathVariable Integer userId, 
            @PathVariable Integer categoryId) {
        log.info("获取学生 {} 分类 {} 的学习进度详情", userId, categoryId);
        
        try {
            StudentCategoryBinding detail = studentRegistrationService.getStudentCategoryProgressDetail(userId, categoryId);
            if (detail != null) {
                return RespResult.success("获取学生分类学习进度详情成功", detail);
            } else {
                return RespResult.error("学生与分类没有绑定关系");
            }
        } catch (Exception e) {
            log.error("获取学生分类学习进度详情过程中发生异常: {}", e.getMessage(), e);
            return RespResult.error("获取学生分类学习进度详情失败: " + e.getMessage());
        }
    }
}
