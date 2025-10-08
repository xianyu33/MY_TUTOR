package com.yy.my_tutor.math.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.math.domain.LearningStatistics;
import com.yy.my_tutor.math.service.LearningStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学习统计控制器
 */
@RestController
@RequestMapping("/api/math/statistics")
@CrossOrigin(origins = "*")
public class LearningStatisticsController {
    
    @Autowired
    private LearningStatisticsService learningStatisticsService;
    
    /**
     * 查询用户的学习统计
     */
    @GetMapping("/user/{userId}")
    public RespResult<List<LearningStatistics>> findLearningStatisticsByUserId(@PathVariable Integer userId) {
        List<LearningStatistics> statistics = learningStatisticsService.findLearningStatisticsByUserId(userId);
        return RespResult.success(statistics);
    }
    
    /**
     * 根据用户ID和知识点ID查询学习统计
     */
    @GetMapping("/user/{userId}/knowledge/{knowledgePointId}")
    public RespResult<LearningStatistics> findLearningStatisticsByUserAndKnowledge(
            @PathVariable Integer userId, @PathVariable Integer knowledgePointId) {
        LearningStatistics statistics = learningStatisticsService.findLearningStatisticsByUserAndKnowledge(userId, knowledgePointId);
        if (statistics != null) {
            return RespResult.success(statistics);
        } else {
            return RespResult.error("学习统计不存在");
        }
    }
    
    /**
     * 根据掌握程度查询学习统计
     */
    @GetMapping("/user/{userId}/mastery/{masteryLevel}")
    public RespResult<List<LearningStatistics>> findLearningStatisticsByMasteryLevel(
            @PathVariable Integer userId, @PathVariable Integer masteryLevel) {
        List<LearningStatistics> statistics = learningStatisticsService.findLearningStatisticsByMasteryLevel(userId, masteryLevel);
        return RespResult.success(statistics);
    }
    
    /**
     * 更新学习统计信息
     */
    @PostMapping("/update")
    public RespResult<LearningStatistics> updateLearningStatistics(@RequestParam Integer userId, 
                                                                  @RequestParam Integer knowledgePointId) {
        LearningStatistics statistics = learningStatisticsService.updateLearningStatistics(userId, knowledgePointId);
        if (statistics != null) {
            return RespResult.success("学习统计更新成功", statistics);
        } else {
            return RespResult.error("学习统计更新失败");
        }
    }
    
    /**
     * 计算并更新掌握程度
     */
    @PostMapping("/mastery/calculate")
    public RespResult<LearningStatistics> calculateMasteryLevel(@RequestParam Integer userId, 
                                                               @RequestParam Integer knowledgePointId) {
        LearningStatistics statistics = learningStatisticsService.calculateMasteryLevel(userId, knowledgePointId);
        if (statistics != null) {
            return RespResult.success("掌握程度计算成功", statistics);
        } else {
            return RespResult.error("掌握程度计算失败");
        }
    }
}
