package com.yy.my_tutor.math.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.math.domain.LearningProgress;
import com.yy.my_tutor.math.service.LearningProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学习进度控制器
 */
@RestController
@RequestMapping("/api/math/progress")
@CrossOrigin(origins = "*")
public class LearningProgressController {
    
    @Autowired
    private LearningProgressService learningProgressService;
    
    /**
     * 查询用户的学习进度
     */
    @GetMapping("/user/{userId}")
    public RespResult<List<LearningProgress>> findLearningProgressByUserId(@PathVariable Integer userId) {
        List<LearningProgress> progressList = learningProgressService.findLearningProgressByUserId(userId);
        return RespResult.success(progressList);
    }
    
    /**
     * 根据用户ID和知识点ID查询学习进度
     */
    @GetMapping("/user/{userId}/knowledge/{knowledgePointId}")
    public RespResult<LearningProgress> findLearningProgressByUserAndKnowledge(
            @PathVariable Integer userId, @PathVariable Integer knowledgePointId) {
        LearningProgress progress = learningProgressService.findLearningProgressByUserAndKnowledge(userId, knowledgePointId);
        if (progress != null) {
            return RespResult.success(progress);
        } else {
            return RespResult.error("学习进度不存在");
        }
    }
    
    /**
     * 根据知识点ID查询学习进度
     */
    @GetMapping("/knowledge/{knowledgePointId}")
    public RespResult<List<LearningProgress>> findLearningProgressByKnowledgePointId(@PathVariable Integer knowledgePointId) {
        List<LearningProgress> progressList = learningProgressService.findLearningProgressByKnowledgePointId(knowledgePointId);
        return RespResult.success(progressList);
    }
    
    /**
     * 根据学习状态查询进度
     */
    @GetMapping("/user/{userId}/status/{progressStatus}")
    public RespResult<List<LearningProgress>> findLearningProgressByStatus(
            @PathVariable Integer userId, @PathVariable Integer progressStatus) {
        List<LearningProgress> progressList = learningProgressService.findLearningProgressByStatus(userId, progressStatus);
        return RespResult.success(progressList);
    }
    
    /**
     * 开始学习知识点
     */
    @PostMapping("/start")
    public RespResult<LearningProgress> startLearning(@RequestParam Integer userId, @RequestParam Integer knowledgePointId) {
        LearningProgress progress = learningProgressService.startLearning(userId, knowledgePointId);
        if (progress != null) {
            return RespResult.success("开始学习成功", progress);
        } else {
            return RespResult.error("开始学习失败");
        }
    }
    
    /**
     * 更新学习进度
     */
    @PutMapping("/update")
    public RespResult<LearningProgress> updateLearningProgress(@RequestBody LearningProgress learningProgress) {
        LearningProgress result = learningProgressService.updateLearningProgress(learningProgress);
        if (result != null) {
            return RespResult.success("学习进度更新成功", result);
        } else {
            return RespResult.error("学习进度更新失败");
        }
    }
    
    /**
     * 完成学习知识点
     */
    @PostMapping("/complete")
    public RespResult<LearningProgress> completeLearning(@RequestParam Integer userId, @RequestParam Integer knowledgePointId) {
        LearningProgress progress = learningProgressService.completeLearning(userId, knowledgePointId);
        if (progress != null) {
            return RespResult.success("学习完成", progress);
        } else {
            return RespResult.error("学习完成失败");
        }
    }
    
    /**
     * 更新学习进度状态
     */
    @PutMapping("/status")
    public RespResult<String> updateProgressStatus(@RequestParam Integer userId, 
                                                 @RequestParam Integer knowledgePointId, 
                                                 @RequestParam Integer progressStatus) {
        boolean result = learningProgressService.updateProgressStatus(userId, knowledgePointId, progressStatus);
        if (result) {
            return RespResult.success("进度状态更新成功");
        } else {
            return RespResult.error("进度状态更新失败");
        }
    }
}
