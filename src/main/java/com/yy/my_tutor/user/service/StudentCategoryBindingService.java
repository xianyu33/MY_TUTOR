package com.yy.my_tutor.user.service;

import com.yy.my_tutor.user.domain.StudentCategoryBinding;

import java.util.List;

/**
 * 学生知识点分类绑定关系服务接口
 */
public interface StudentCategoryBindingService {
    
    /**
     * 创建学生分类绑定关系
     * @param studentId 学生ID
     * @param categoryId 分类ID
     * @param gradeId 年级ID
     * @return 绑定关系对象
     */
    StudentCategoryBinding createStudentCategoryBinding(Integer studentId, Integer categoryId, Integer gradeId);
    
    /**
     * 更新学生分类绑定关系
     * @param binding 绑定关系对象
     * @return 更新后的绑定关系对象
     */
    StudentCategoryBinding updateStudentCategoryBinding(StudentCategoryBinding binding);
    
    /**
     * 根据ID查询绑定关系
     * @param id 绑定关系ID
     * @return 绑定关系对象
     */
    StudentCategoryBinding findStudentCategoryBindingById(Integer id);
    
    /**
     * 根据学生ID和分类ID查询绑定关系
     * @param studentId 学生ID
     * @param categoryId 分类ID
     * @return 绑定关系对象
     */
    StudentCategoryBinding findStudentCategoryBindingByStudentAndCategory(Integer studentId, Integer categoryId);
    
    /**
     * 根据学生ID查询所有绑定关系
     * @param studentId 学生ID
     * @return 绑定关系列表
     */
    List<StudentCategoryBinding> findStudentCategoryBindingsByStudentId(Integer studentId);
    
    /**
     * 根据学生ID和年级ID查询绑定关系
     * @param studentId 学生ID
     * @param gradeId 年级ID
     * @return 绑定关系列表
     */
    List<StudentCategoryBinding> findStudentCategoryBindingsByStudentAndGrade(Integer studentId, Integer gradeId);
    
    /**
     * 根据分类ID查询所有绑定关系
     * @param categoryId 分类ID
     * @return 绑定关系列表
     */
    List<StudentCategoryBinding> findStudentCategoryBindingsByCategoryId(Integer categoryId);
    
    /**
     * 根据绑定状态查询绑定关系
     * @param studentId 学生ID
     * @param bindingStatus 绑定状态
     * @return 绑定关系列表
     */
    List<StudentCategoryBinding> findStudentCategoryBindingsByStatus(Integer studentId, Integer bindingStatus);
    
    /**
     * 更新绑定状态
     * @param studentId 学生ID
     * @param categoryId 分类ID
     * @param bindingStatus 绑定状态
     * @return 更新是否成功
     */
    boolean updateBindingStatus(Integer studentId, Integer categoryId, Integer bindingStatus);
    
    /**
     * 更新整体进度
     * @param studentId 学生ID
     * @param categoryId 分类ID
     * @param overallProgress 整体进度
     * @return 更新是否成功
     */
    boolean updateOverallProgress(Integer studentId, Integer categoryId, java.math.BigDecimal overallProgress);
    
    /**
     * 更新学习统计信息
     * @param studentId 学生ID
     * @param categoryId 分类ID
     * @return 更新是否成功
     */
    boolean updateStudyStatistics(Integer studentId, Integer categoryId);
    
    /**
     * 删除绑定关系（逻辑删除）
     * @param id 绑定关系ID
     * @return 删除是否成功
     */
    boolean deleteStudentCategoryBinding(Integer id);
    
    /**
     * 获取学生分类学习进度详情（包含知识点学习进度）
     * @param studentId 学生ID
     * @return 学生分类学习进度详情列表
     */
    List<StudentCategoryBinding> getStudentCategoryProgressDetails(Integer studentId);
    
    /**
     * 获取学生指定分类的学习进度详情
     * @param studentId 学生ID
     * @param categoryId 分类ID
     * @return 分类学习进度详情
     */
    StudentCategoryBinding getStudentCategoryProgressDetail(Integer studentId, Integer categoryId);
    
    /**
     * 批量创建学生分类绑定关系
     * @param studentId 学生ID
     * @param gradeId 年级ID
     * @return 创建的绑定关系数量
     */
    int batchCreateStudentCategoryBindings(Integer studentId, Integer gradeId);
}
