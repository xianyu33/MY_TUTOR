package com.yy.my_tutor.math.service;

import com.yy.my_tutor.math.domain.KnowledgeCategory;

import java.util.List;

/**
 * 知识点分类服务接口
 */
public interface KnowledgeCategoryService {
    
    /**
     * 查询所有分类
     */
    List<KnowledgeCategory> findAllCategories();
    
    /**
     * 根据ID查询分类
     */
    KnowledgeCategory findCategoryById(Integer id);
    
    /**
     * 根据编码查询分类
     */
    KnowledgeCategory findCategoryByCode(String categoryCode);
    
    /**
     * 根据年级ID查询分类列表
     */
    List<KnowledgeCategory> findKnowledgeCategoriesByGradeId(Integer gradeId);
    
    /**
     * 新增分类
     */
    KnowledgeCategory addCategory(KnowledgeCategory category);
    
    /**
     * 更新分类
     */
    KnowledgeCategory updateCategory(KnowledgeCategory category);
    
    /**
     * 删除分类
     */
    boolean deleteCategory(Integer id);
}
