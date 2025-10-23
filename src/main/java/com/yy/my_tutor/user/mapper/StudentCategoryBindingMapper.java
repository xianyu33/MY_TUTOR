package com.yy.my_tutor.user.mapper;

import com.yy.my_tutor.user.domain.StudentCategoryBinding;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生知识点分类绑定关系Mapper接口
 */
@Mapper
public interface StudentCategoryBindingMapper {
    
    /**
     * 插入学生分类绑定关系
     * @param binding 绑定关系对象
     * @return 插入结果
     */
    int insertStudentCategoryBinding(StudentCategoryBinding binding);
    
    /**
     * 更新学生分类绑定关系
     * @param binding 绑定关系对象
     * @return 更新结果
     */
    int updateStudentCategoryBinding(StudentCategoryBinding binding);
    
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
    StudentCategoryBinding findStudentCategoryBindingByStudentAndCategory(@Param("studentId") Integer studentId, @Param("categoryId") Integer categoryId);
    
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
    List<StudentCategoryBinding> findStudentCategoryBindingsByStudentAndGrade(@Param("studentId") Integer studentId, @Param("gradeId") Integer gradeId);
    
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
    List<StudentCategoryBinding> findStudentCategoryBindingsByStatus(@Param("studentId") Integer studentId, @Param("bindingStatus") Integer bindingStatus);
    
    /**
     * 更新绑定状态
     * @param studentId 学生ID
     * @param categoryId 分类ID
     * @param bindingStatus 绑定状态
     * @return 更新结果
     */
    int updateBindingStatus(@Param("studentId") Integer studentId, @Param("categoryId") Integer categoryId, @Param("bindingStatus") Integer bindingStatus);
    
    /**
     * 更新整体进度
     * @param studentId 学生ID
     * @param categoryId 分类ID
     * @param overallProgress 整体进度
     * @return 更新结果
     */
    int updateOverallProgress(@Param("studentId") Integer studentId, @Param("categoryId") Integer categoryId, @Param("overallProgress") java.math.BigDecimal overallProgress);
    
    /**
     * 更新学习统计信息
     * @param binding 绑定关系对象
     * @return 更新结果
     */
    int updateStudyStatistics(StudentCategoryBinding binding);
    
    /**
     * 删除绑定关系（逻辑删除）
     * @param id 绑定关系ID
     * @return 删除结果
     */
    int deleteStudentCategoryBinding(Integer id);
}
