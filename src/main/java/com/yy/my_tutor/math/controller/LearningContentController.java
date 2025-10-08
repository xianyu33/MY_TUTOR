package com.yy.my_tutor.math.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.math.domain.LearningContent;
import com.yy.my_tutor.math.service.LearningContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学习内容记录控制器
 */
@RestController
@RequestMapping("/api/math/content")
@CrossOrigin(origins = "*")
public class LearningContentController {
    
    @Autowired
    private LearningContentService learningContentService;
    
    /**
     * 查询用户的学习内容记录
     */
    @GetMapping("/user/{userId}")
    public RespResult<List<LearningContent>> findLearningContentByUserId(@PathVariable Integer userId) {
        List<LearningContent> contentList = learningContentService.findLearningContentByUserId(userId);
        return RespResult.success(contentList);
    }
    
    /**
     * 根据用户ID和知识点ID查询学习内容
     */
    @GetMapping("/user/{userId}/knowledge/{knowledgePointId}")
    public RespResult<List<LearningContent>> findLearningContentByUserAndKnowledge(
            @PathVariable Integer userId, @PathVariable Integer knowledgePointId) {
        List<LearningContent> contentList = learningContentService.findLearningContentByUserAndKnowledge(userId, knowledgePointId);
        return RespResult.success(contentList);
    }
    
    /**
     * 根据内容类型查询学习内容
     */
    @GetMapping("/user/{userId}/type/{contentType}")
    public RespResult<List<LearningContent>> findLearningContentByType(
            @PathVariable Integer userId, @PathVariable Integer contentType) {
        List<LearningContent> contentList = learningContentService.findLearningContentByType(userId, contentType);
        return RespResult.success(contentList);
    }
    
    /**
     * 根据完成状态查询学习内容
     */
    @GetMapping("/user/{userId}/status/{completionStatus}")
    public RespResult<List<LearningContent>> findLearningContentByCompletionStatus(
            @PathVariable Integer userId, @PathVariable Integer completionStatus) {
        List<LearningContent> contentList = learningContentService.findLearningContentByCompletionStatus(userId, completionStatus);
        return RespResult.success(contentList);
    }
    
    /**
     * 新增学习内容记录
     */
    @PostMapping("/add")
    public RespResult<LearningContent> addLearningContent(@RequestBody LearningContent learningContent) {
        LearningContent result = learningContentService.addLearningContent(learningContent);
        if (result != null) {
            return RespResult.success("学习内容添加成功", result);
        } else {
            return RespResult.error("学习内容添加失败");
        }
    }
    
    /**
     * 更新学习内容记录
     */
    @PutMapping("/update")
    public RespResult<LearningContent> updateLearningContent(@RequestBody LearningContent learningContent) {
        LearningContent result = learningContentService.updateLearningContent(learningContent);
        if (result != null) {
            return RespResult.success("学习内容更新成功", result);
        } else {
            return RespResult.error("学习内容更新失败");
        }
    }
    
    /**
     * 完成学习内容
     */
    @PostMapping("/complete")
    public RespResult<LearningContent> completeLearningContent(@RequestParam Integer contentId, 
                                                              @RequestParam Integer score, 
                                                              @RequestParam(required = false) String feedback) {
        LearningContent result = learningContentService.completeLearningContent(contentId, score, feedback);
        if (result != null) {
            return RespResult.success("学习内容完成", result);
        } else {
            return RespResult.error("学习内容完成失败");
        }
    }
}
