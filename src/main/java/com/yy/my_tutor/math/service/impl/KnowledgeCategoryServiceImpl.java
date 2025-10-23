package com.yy.my_tutor.math.service.impl;

import com.yy.my_tutor.math.domain.KnowledgeCategory;
import com.yy.my_tutor.math.mapper.KnowledgeCategoryMapper;
import com.yy.my_tutor.math.service.KnowledgeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 知识点分类服务实现类
 */
@Service
public class KnowledgeCategoryServiceImpl implements KnowledgeCategoryService {
    
    @Autowired
    private KnowledgeCategoryMapper knowledgeCategoryMapper;
    
    @Override
    public List<KnowledgeCategory> findAllCategories() {
        return knowledgeCategoryMapper.findAllCategories();
    }
    
    @Override
    public KnowledgeCategory findCategoryById(Integer id) {
        return knowledgeCategoryMapper.findCategoryById(id);
    }
    
    @Override
    public KnowledgeCategory findCategoryByCode(String categoryCode) {
        return knowledgeCategoryMapper.findCategoryByCode(categoryCode);
    }
    
    @Override
    public List<KnowledgeCategory> findKnowledgeCategoriesByGradeId(Integer gradeId) {
        return knowledgeCategoryMapper.findKnowledgeCategoriesByGradeId(gradeId);
    }
    
    @Override
    public KnowledgeCategory addCategory(KnowledgeCategory category) {
        Date now = new Date();
        category.setCreateAt(now);
        category.setUpdateAt(now);
        category.setDeleteFlag("N");
        
        int result = knowledgeCategoryMapper.insertCategory(category);
        return result > 0 ? category : null;
    }
    
    @Override
    public KnowledgeCategory updateCategory(KnowledgeCategory category) {
        category.setUpdateAt(new Date());
        
        int result = knowledgeCategoryMapper.updateCategory(category);
        return result > 0 ? category : null;
    }
    
    @Override
    public boolean deleteCategory(Integer id) {
        int result = knowledgeCategoryMapper.deleteCategory(id);
        return result > 0;
    }
}
