package com.yy.my_tutor.math.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.math.domain.KnowledgeCategory;
import com.yy.my_tutor.math.service.KnowledgeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识点分类控制器
 */
@RestController
@RequestMapping("/api/math/category")
@CrossOrigin(origins = "*")
public class KnowledgeCategoryController {
    
    @Autowired
    private KnowledgeCategoryService knowledgeCategoryService;
    
    /**
     * 查询所有分类
     */
    @GetMapping("/list")
    public RespResult<List<KnowledgeCategory>> findAllCategories() {
        List<KnowledgeCategory> categories = knowledgeCategoryService.findAllCategories();
        return RespResult.success(categories);
    }
    
    /**
     * 根据ID查询分类
     */
    @GetMapping("/{id}")
    public RespResult<KnowledgeCategory> findCategoryById(@PathVariable Integer id) {
        KnowledgeCategory category = knowledgeCategoryService.findCategoryById(id);
        if (category != null) {
            return RespResult.success(category);
        } else {
            return RespResult.error("分类不存在");
        }
    }
    
    /**
     * 根据编码查询分类
     */
    @GetMapping("/code/{categoryCode}")
    public RespResult<KnowledgeCategory> findCategoryByCode(@PathVariable String categoryCode) {
        KnowledgeCategory category = knowledgeCategoryService.findCategoryByCode(categoryCode);
        if (category != null) {
            return RespResult.success(category);
        } else {
            return RespResult.error("分类不存在");
        }
    }
    
    /**
     * 新增分类
     */
    @PostMapping("/add")
    public RespResult<KnowledgeCategory> addCategory(@RequestBody KnowledgeCategory category) {
        KnowledgeCategory result = knowledgeCategoryService.addCategory(category);
        if (result != null) {
            return RespResult.success("分类添加成功", result);
        } else {
            return RespResult.error("分类添加失败");
        }
    }
    
    /**
     * 更新分类
     */
    @PutMapping("/update")
    public RespResult<KnowledgeCategory> updateCategory(@RequestBody KnowledgeCategory category) {
        KnowledgeCategory result = knowledgeCategoryService.updateCategory(category);
        if (result != null) {
            return RespResult.success("分类更新成功", result);
        } else {
            return RespResult.error("分类更新失败");
        }
    }
    
    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public RespResult<String> deleteCategory(@PathVariable Integer id) {
        boolean result = knowledgeCategoryService.deleteCategory(id);
        if (result) {
            return RespResult.success("分类删除成功");
        } else {
            return RespResult.error("分类删除失败");
        }
    }
}
