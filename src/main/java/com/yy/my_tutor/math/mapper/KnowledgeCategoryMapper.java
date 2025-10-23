package com.yy.my_tutor.math.mapper;

import com.yy.my_tutor.math.domain.KnowledgeCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 知识点分类Mapper接口
 */
@Mapper
public interface KnowledgeCategoryMapper {
    
    /**
     * 查询所有分类
     */
    List<KnowledgeCategory> findAllCategories();
    
    /**
     * 根据ID查询分类
     */
    KnowledgeCategory findCategoryById(@Param("id") Integer id);
    
    /**
     * 根据编码查询分类
     */
    KnowledgeCategory findCategoryByCode(@Param("categoryCode") String categoryCode);
    
    /**
     * 根据年级ID查询分类列表
     */
    List<KnowledgeCategory> findKnowledgeCategoriesByGradeId(@Param("gradeId") Integer gradeId);
    
    /**
     * 插入分类
     */
    int insertCategory(KnowledgeCategory category);
    
    /**
     * 更新分类
     */
    int updateCategory(KnowledgeCategory category);
    
    /**
     * 删除分类（逻辑删除）
     */
    int deleteCategory(@Param("id") Integer id);
}
