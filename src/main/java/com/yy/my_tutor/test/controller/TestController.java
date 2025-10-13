package com.yy.my_tutor.test.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.test.domain.Test;
import com.yy.my_tutor.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 测试控制器
 */
@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {
    
    @Autowired
    private TestService testService;
    
    /**
     * 查询所有测试
     */
    @GetMapping("/list")
    public RespResult<List<Test>> findAllTests() {
        List<Test> tests = testService.findAllTests();
        return RespResult.success(tests);
    }
    
    /**
     * 根据ID查询测试
     */
    @GetMapping("/{id}")
    public RespResult<Test> findTestById(@PathVariable Integer id) {
        Test test = testService.findTestById(id);
        if (test != null) {
            return RespResult.success(test);
        } else {
            return RespResult.error("测试不存在");
        }
    }
    
    /**
     * 根据年级ID查询测试
     */
    @GetMapping("/grade/{gradeId}")
    public RespResult<List<Test>> findTestsByGradeId(@PathVariable Integer gradeId) {
        List<Test> tests = testService.findTestsByGradeId(gradeId);
        return RespResult.success(tests);
    }
    
    /**
     * 根据年级和难度查询测试
     */
    @GetMapping("/grade/{gradeId}/difficulty/{difficultyLevel}")
    public RespResult<List<Test>> findTestsByGradeAndDifficulty(
            @PathVariable Integer gradeId, @PathVariable Integer difficultyLevel) {
        List<Test> tests = testService.findTestsByGradeAndDifficulty(gradeId, difficultyLevel);
        return RespResult.success(tests);
    }
    
    /**
     * 根据测试类型查询测试
     */
    @GetMapping("/type/{testType}")
    public RespResult<List<Test>> findTestsByType(@PathVariable Integer testType) {
        List<Test> tests = testService.findTestsByType(testType);
        return RespResult.success(tests);
    }
    
    /**
     * 生成测试题目
     */
    @PostMapping("/generate")
    public RespResult<Test> generateTest(@RequestParam Integer gradeId,
                                        @RequestParam Integer difficultyLevel,
                                        @RequestParam(defaultValue = "10") Integer questionCount) {
        Test test = testService.generateTest(gradeId, difficultyLevel, questionCount);
        if (test != null) {
            return RespResult.success("测试生成成功", test);
        } else {
            return RespResult.error("测试生成失败");
        }
    }
    
    /**
     * 新增测试
     */
    @PostMapping("/add")
    public RespResult<Test> addTest(@RequestBody Test test) {
        Test result = testService.addTest(test);
        if (result != null) {
            return RespResult.success("测试添加成功", result);
        } else {
            return RespResult.error("测试添加失败");
        }
    }
    
    /**
     * 更新测试
     */
    @PutMapping("/update")
    public RespResult<Test> updateTest(@RequestBody Test test) {
        Test result = testService.updateTest(test);
        if (result != null) {
            return RespResult.success("测试更新成功", result);
        } else {
            return RespResult.error("测试更新失败");
        }
    }
    
    /**
     * 删除测试
     */
    @DeleteMapping("/{id}")
    public RespResult<String> deleteTest(@PathVariable Integer id) {
        boolean result = testService.deleteTest(id);
        if (result) {
            return RespResult.success("测试删除成功");
        } else {
            return RespResult.error("测试删除失败");
        }
    }
}


