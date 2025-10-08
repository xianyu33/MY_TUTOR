package com.yy.my_tutor.math.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.math.service.KnowledgePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识点控制器
 */
@RestController
@RequestMapping("/api/math/knowledge")
@CrossOrigin(origins = "*")
public class KnowledgePointController {
    
    @Autowired
    private KnowledgePointService knowledgePointService;
    
    /**
     * 查询所有知识点
     */
    @GetMapping("/list")
    public RespResult<List<KnowledgePoint>> findAllKnowledgePoints() {
        List<KnowledgePoint> knowledgePoints = knowledgePointService.findAllKnowledgePoints();
        return RespResult.success(knowledgePoints);
    }
    
    /**
     * 根据ID查询知识点
     */
    @GetMapping("/{id}")
    public RespResult<KnowledgePoint> findKnowledgePointById(@PathVariable Integer id) {
        KnowledgePoint knowledgePoint = knowledgePointService.findKnowledgePointById(id);
        if (knowledgePoint != null) {
            return RespResult.success(knowledgePoint);
        } else {
            return RespResult.error("知识点不存在");
        }
    }
    
    /**
     * 根据年级ID查询知识点
     */
    @GetMapping("/grade/{gradeId}")
    public RespResult<List<KnowledgePoint>> findKnowledgePointsByGradeId(@PathVariable Integer gradeId) {
        List<KnowledgePoint> knowledgePoints = knowledgePointService.findKnowledgePointsByGradeId(gradeId);
        return RespResult.success(knowledgePoints);
    }
    
    /**
     * 根据分类ID查询知识点
     */
    @GetMapping("/category/{categoryId}")
    public RespResult<List<KnowledgePoint>> findKnowledgePointsByCategoryId(@PathVariable Integer categoryId) {
        List<KnowledgePoint> knowledgePoints = knowledgePointService.findKnowledgePointsByCategoryId(categoryId);
        return RespResult.success(knowledgePoints);
    }
    
    /**
     * 根据年级和分类查询知识点
     */
    @GetMapping("/grade/{gradeId}/category/{categoryId}")
    public RespResult<List<KnowledgePoint>> findKnowledgePointsByGradeAndCategory(
            @PathVariable Integer gradeId, @PathVariable Integer categoryId) {
        List<KnowledgePoint> knowledgePoints = knowledgePointService.findKnowledgePointsByGradeAndCategory(gradeId, categoryId);
        return RespResult.success(knowledgePoints);
    }
    
    /**
     * 根据编码查询知识点
     */
    @GetMapping("/code/{pointCode}")
    public RespResult<KnowledgePoint> findKnowledgePointByCode(@PathVariable String pointCode) {
        KnowledgePoint knowledgePoint = knowledgePointService.findKnowledgePointByCode(pointCode);
        if (knowledgePoint != null) {
            return RespResult.success(knowledgePoint);
        } else {
            return RespResult.error("知识点不存在");
        }
    }
    
    /**
     * 根据难度等级查询知识点
     */
    @GetMapping("/difficulty/{difficultyLevel}")
    public RespResult<List<KnowledgePoint>> findKnowledgePointsByDifficulty(@PathVariable Integer difficultyLevel) {
        List<KnowledgePoint> knowledgePoints = knowledgePointService.findKnowledgePointsByDifficulty(difficultyLevel);
        return RespResult.success(knowledgePoints);
    }
    
    /**
     * 新增知识点
     */
    @PostMapping("/add")
    public RespResult<KnowledgePoint> addKnowledgePoint(@RequestBody KnowledgePoint knowledgePoint) {
        KnowledgePoint result = knowledgePointService.addKnowledgePoint(knowledgePoint);
        if (result != null) {
            return RespResult.success("知识点添加成功", result);
        } else {
            return RespResult.error("知识点添加失败");
        }
    }
    
    /**
     * 更新知识点
     */
    @PutMapping("/update")
    public RespResult<KnowledgePoint> updateKnowledgePoint(@RequestBody KnowledgePoint knowledgePoint) {
        KnowledgePoint result = knowledgePointService.updateKnowledgePoint(knowledgePoint);
        if (result != null) {
            return RespResult.success("知识点更新成功", result);
        } else {
            return RespResult.error("知识点更新失败");
        }
    }
    
    /**
     * 删除知识点
     */
    @DeleteMapping("/{id}")
    public RespResult<String> deleteKnowledgePoint(@PathVariable Integer id) {
        boolean result = knowledgePointService.deleteKnowledgePoint(id);
        if (result) {
            return RespResult.success("知识点删除成功");
        } else {
            return RespResult.error("知识点删除失败");
        }
    }
}
