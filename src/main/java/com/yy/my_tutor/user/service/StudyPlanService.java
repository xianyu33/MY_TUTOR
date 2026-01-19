package com.yy.my_tutor.user.service;

import com.yy.my_tutor.user.domain.StudyPlan;

import java.util.List;

/**
 * 学习计划服务接口
 */
public interface StudyPlanService {

    /**
     * 获取学生所有学习计划
     * @param studentId 学生ID
     * @return 学习计划列表
     */
    List<StudyPlan> getStudentStudyPlans(Integer studentId);

    /**
     * 获取单个计划详情
     * @param studentId 学生ID
     * @param categoryId 分类ID
     * @return 学习计划详情
     */
    StudyPlan getStudyPlanDetail(Integer studentId, Integer categoryId);

    /**
     * 完成知识点学习（同时更新计划进度）
     * @param studentId 学生ID
     * @param knowledgePointId 知识点ID
     * @return 更新后的学习计划
     */
    StudyPlan completeKnowledgePoint(Integer studentId, Integer knowledgePointId);

    /**
     * 取消完成知识点学习（同时更新计划进度）
     * @param studentId 学生ID
     * @param knowledgePointId 知识点ID
     * @return 更新后的学习计划
     */
    StudyPlan uncompleteKnowledgePoint(Integer studentId, Integer knowledgePointId);

    /**
     * 刷新计划进度（重新计算）
     * @param studentId 学生ID
     * @param categoryId 分类ID
     * @return 更新是否成功
     */
    boolean refreshPlanProgress(Integer studentId, Integer categoryId);

    /**
     * 根据知识点ID获取所属的学习计划
     * @param studentId 学生ID
     * @param knowledgePointId 知识点ID
     * @return 学习计划
     */
    StudyPlan getStudyPlanByKnowledgePoint(Integer studentId, Integer knowledgePointId);
}
