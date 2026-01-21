package com.yy.my_tutor.user.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.user.domain.StudyPlan;
import com.yy.my_tutor.user.service.StudyPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学习计划控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/study-plan")
@CrossOrigin(origins = "*")
public class StudyPlanController {

    @Autowired
    private StudyPlanService studyPlanService;

    /**
     * 获取学生所有学习计划
     * @param studentId 学生ID
     * @return 学习计划列表
     */
    @GetMapping("/list/{studentId}")
    public RespResult<List<StudyPlan>> getStudentStudyPlans(@PathVariable Integer studentId) {
        log.info("获取学生 {} 的所有学习计划", studentId);

        try {
            List<StudyPlan> plans = studyPlanService.getStudentStudyPlans(studentId);
            return RespResult.success("获取学习计划列表成功", plans);
        } catch (Exception e) {
            log.error("获取学习计划列表过程中发生异常: {}", e.getMessage(), e);
            return RespResult.error("获取学习计划列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取单个计划详情
     * @param studentId 学生ID
     * @param categoryId 分类ID
     * @return 学习计划详情
     */
    @GetMapping("/{studentId}/{categoryId}")
    public RespResult<StudyPlan> getStudyPlanDetail(
            @PathVariable Integer studentId,
            @PathVariable Integer categoryId) {
        log.info("获取学生 {} 分类 {} 的学习计划详情", studentId, categoryId);

        try {
            StudyPlan plan = studyPlanService.getStudyPlanDetail(studentId, categoryId);
            if (plan != null) {
                return RespResult.success("获取学习计划详情成功", plan);
            } else {
                return RespResult.error("学习计划不存在");
            }
        } catch (Exception e) {
            log.error("获取学习计划详情过程中发生异常: {}", e.getMessage(), e);
            return RespResult.error("获取学习计划详情失败: " + e.getMessage());
        }
    }

    /**
     * 完成知识点学习（自动更新计划进度）
     * @param studentId 学生ID
     * @param knowledgePointId 知识点ID
     * @return 更新后的学习计划
     */
    @PostMapping("/complete-knowledge")
    public RespResult<StudyPlan> completeKnowledgePoint(
            @RequestParam Integer studentId,
            @RequestParam Integer knowledgePointId) {
        log.info("学生 {} 完成知识点 {} 学习", studentId, knowledgePointId);

        try {
            StudyPlan plan = studyPlanService.completeKnowledgePoint(studentId, knowledgePointId);
            if (plan != null) {
                return RespResult.success("知识点学习完成，计划进度已更新", plan);
            } else {
                return RespResult.error("完成知识点学习失败");
            }
        } catch (Exception e) {
            log.error("完成知识点学习过程中发生异常: {}", e.getMessage(), e);
            return RespResult.error("完成知识点学习失败: " + e.getMessage());
        }
    }

    /**
     * 取消完成知识点（自动更新计划进度）
     * @param studentId 学生ID
     * @param knowledgePointId 知识点ID
     * @return 更新后的学习计划
     */
    @PostMapping("/uncomplete-knowledge")
    public RespResult<StudyPlan> uncompleteKnowledgePoint(
            @RequestParam Integer studentId,
            @RequestParam Integer knowledgePointId) {
        log.info("学生 {} 取消完成知识点 {}", studentId, knowledgePointId);

        try {
            StudyPlan plan = studyPlanService.uncompleteKnowledgePoint(studentId, knowledgePointId);
            if (plan != null) {
                return RespResult.success("已取消完成，计划进度已更新", plan);
            } else {
                return RespResult.error("取消完成失败");
            }
        } catch (Exception e) {
            log.error("取消完成知识点过程中发生异常: {}", e.getMessage(), e);
            return RespResult.error("取消完成失败: " + e.getMessage());
        }
    }

    /**
     * 刷新计划进度
     * @param studentId 学生ID
     * @param categoryId 分类ID
     * @return 刷新结果
     */
    @PostMapping("/refresh/{studentId}/{categoryId}")
    public RespResult<StudyPlan> refreshPlanProgress(
            @PathVariable Integer studentId,
            @PathVariable Integer categoryId) {
        log.info("刷新学生 {} 分类 {} 的计划进度", studentId, categoryId);

        try {
            boolean success = studyPlanService.refreshPlanProgress(studentId, categoryId);
            if (success) {
                StudyPlan plan = studyPlanService.getStudyPlanDetail(studentId, categoryId);
                return RespResult.success("计划进度刷新成功", plan);
            } else {
                return RespResult.error("计划进度刷新失败");
            }
        } catch (Exception e) {
            log.error("刷新计划进度过程中发生异常: {}", e.getMessage(), e);
            return RespResult.error("刷新计划进度失败: " + e.getMessage());
        }
    }

    /**
     * 根据知识点获取所属学习计划
     * @param studentId 学生ID
     * @param knowledgePointId 知识点ID
     * @return 学习计划
     */
    @GetMapping("/by-knowledge/{studentId}/{knowledgePointId}")
    public RespResult<StudyPlan> getStudyPlanByKnowledgePoint(
            @PathVariable Integer studentId,
            @PathVariable Integer knowledgePointId) {
        log.info("获取学生 {} 知识点 {} 所属的学习计划", studentId, knowledgePointId);

        try {
            StudyPlan plan = studyPlanService.getStudyPlanByKnowledgePoint(studentId, knowledgePointId);
            if (plan != null) {
                return RespResult.success("获取学习计划成功", plan);
            } else {
                return RespResult.error("学习计划不存在");
            }
        } catch (Exception e) {
            log.error("获取学习计划过程中发生异常: {}", e.getMessage(), e);
            return RespResult.error("获取学习计划失败: " + e.getMessage());
        }
    }
}
