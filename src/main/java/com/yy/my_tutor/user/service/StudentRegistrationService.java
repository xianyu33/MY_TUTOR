package com.yy.my_tutor.user.service;

import com.yy.my_tutor.math.domain.KnowledgeCategory;
import com.yy.my_tutor.user.domain.CategoryWithProgress;
import com.yy.my_tutor.user.domain.KnowledgePointWithProgress;
import com.yy.my_tutor.user.domain.LearningProgressStats;
import com.yy.my_tutor.user.domain.StudentCategoryBinding;
import com.yy.my_tutor.user.domain.User;

import java.util.List;

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

    /**
     * 为现有学生重新分配课程（根据年级）
     * @param userId 学生ID
     * @param gradeLevel 年级等级
     * @param forceUpdate 是否强制更新（删除现有进度重新分配）
     * @return 分配是否成功
     */
    boolean reassignCoursesByGrade(Integer userId, Integer gradeLevel, boolean forceUpdate);

    /**
     * 获取学生的学习进度统计
     * @param userId 学生ID
     * @return 学习进度统计信息
     */
    LearningProgressStats getLearningProgressStats(Integer userId);

    /**
     * 获取学生绑定的知识点分类列表
     * @param userId 学生ID
     * @return 学生绑定的知识点分类列表
     */
    List<KnowledgeCategory> getStudentBoundCategories(Integer userId);
    
    /**
     * 获取学生绑定的知识点分类列表（包含学习进度统计）
     * @param userId 学生ID
     * @return 学生绑定的知识点分类列表（包含学习进度）
     */
    List<CategoryWithProgress> getStudentBoundCategoriesWithProgress(Integer userId);

    /**
     * 根据年级获取知识点分类列表
     * @param gradeId 年级ID
     * @return 知识点分类列表
     */
    List<KnowledgeCategory> getCategoriesByGrade(Integer gradeId);

    /**
     * 获取学生分类学习进度详情（包含知识点学习进度）
     * @param userId 学生ID
     * @return 学生分类学习进度详情列表
     */
    List<StudentCategoryBinding> getStudentCategoryProgressDetails(Integer userId);

    /**
     * 获取学生指定分类的学习进度详情
     * @param userId 学生ID
     * @param categoryId 分类ID
     * @return 分类学习进度详情
     */
    StudentCategoryBinding getStudentCategoryProgressDetail(Integer userId, Integer categoryId);
    
    /**
     * 根据年级和分类查询知识点详情（包含学生学习进度）
     * @param userId 学生ID
     * @param gradeId 年级ID
     * @param categoryId 分类ID
     * @return 知识点详情列表（包含学习进度）
     */
    List<KnowledgePointWithProgress> getKnowledgePointsWithProgress(Integer userId, Integer gradeId, Integer categoryId);
}
