package com.yy.my_tutor.math.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.math.domain.Grade;
import com.yy.my_tutor.math.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 年级控制器
 */
@RestController
@RequestMapping("/api/math/grade")
@CrossOrigin(origins = "*")
public class GradeController {
    
    @Autowired
    private GradeService gradeService;
    
    /**
     * 查询所有年级
     */
    @GetMapping("/list")
    public RespResult<List<Grade>> findAllGrades() {
        List<Grade> grades = gradeService.findAllGrades();
        return RespResult.success(grades);
    }
    
    /**
     * 根据ID查询年级
     */
    @GetMapping("/{id}")
    public RespResult<Grade> findGradeById(@PathVariable Integer id) {
        Grade grade = gradeService.findGradeById(id);
        if (grade != null) {
            return RespResult.success(grade);
        } else {
            return RespResult.error("年级不存在");
        }
    }
    
    /**
     * 根据年级等级查询年级
     */
    @GetMapping("/level/{gradeLevel}")
    public RespResult<Grade> findGradeByLevel(@PathVariable Integer gradeLevel) {
        Grade grade = gradeService.findGradeByLevel(gradeLevel);
        if (grade != null) {
            return RespResult.success(grade);
        } else {
            return RespResult.error("年级不存在");
        }
    }
    
    /**
     * 新增年级
     */
    @PostMapping("/add")
    public RespResult<Grade> addGrade(@RequestBody Grade grade) {
        Grade result = gradeService.addGrade(grade);
        if (result != null) {
            return RespResult.success("年级添加成功", result);
        } else {
            return RespResult.error("年级添加失败");
        }
    }
    
    /**
     * 更新年级
     */
    @PutMapping("/update")
    public RespResult<Grade> updateGrade(@RequestBody Grade grade) {
        Grade result = gradeService.updateGrade(grade);
        if (result != null) {
            return RespResult.success("年级更新成功", result);
        } else {
            return RespResult.error("年级更新失败");
        }
    }
    
    /**
     * 删除年级
     */
    @DeleteMapping("/{id}")
    public RespResult<String> deleteGrade(@PathVariable Integer id) {
        boolean result = gradeService.deleteGrade(id);
        if (result) {
            return RespResult.success("年级删除成功");
        } else {
            return RespResult.error("年级删除失败");
        }
    }
}
