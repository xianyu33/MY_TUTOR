package com.yy.my_tutor.user.service.impl;

import com.yy.my_tutor.math.domain.KnowledgeCategory;
import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.math.domain.LearningProgress;
import com.yy.my_tutor.math.service.KnowledgeCategoryService;
import com.yy.my_tutor.math.service.KnowledgePointService;
import com.yy.my_tutor.math.service.LearningProgressService;
import com.yy.my_tutor.user.domain.StudentCategoryBinding;
import com.yy.my_tutor.user.domain.StudyPlan;
import com.yy.my_tutor.user.service.StudentCategoryBindingService;
import com.yy.my_tutor.user.service.StudyPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 学习计划服务实现类
 */
@Slf4j
@Service
public class StudyPlanServiceImpl implements StudyPlanService {

    @Autowired
    private StudentCategoryBindingService studentCategoryBindingService;

    @Autowired
    private KnowledgePointService knowledgePointService;

    @Autowired
    private KnowledgeCategoryService knowledgeCategoryService;

    @Autowired
    private LearningProgressService learningProgressService;

    @Override
    public List<StudyPlan> getStudentStudyPlans(Integer studentId) {
        try {
            List<StudentCategoryBinding> bindings = studentCategoryBindingService.findStudentCategoryBindingsByStudentId(studentId);
            if (bindings == null || bindings.isEmpty()) {
                log.warn("学生 {} 没有学习计划", studentId);
                return new ArrayList<>();
            }

            List<StudyPlan> plans = new ArrayList<>();
            for (StudentCategoryBinding binding : bindings) {
                StudyPlan plan = convertToStudyPlan(binding);
                if (plan != null) {
                    plans.add(plan);
                }
            }

            log.info("获取学生 {} 的学习计划成功，共 {} 个计划", studentId, plans.size());
            return plans;

        } catch (Exception e) {
            log.error("获取学生学习计划过程中发生异常: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public StudyPlan getStudyPlanDetail(Integer studentId, Integer categoryId) {
        try {
            StudentCategoryBinding binding = studentCategoryBindingService.findStudentCategoryBindingByStudentAndCategory(studentId, categoryId);
            if (binding == null) {
                log.warn("学生 {} 与分类 {} 没有绑定关系", studentId, categoryId);
                return null;
            }

            // 先刷新进度统计
            studentCategoryBindingService.updateStudyStatistics(studentId, categoryId);

            // 重新获取最新数据
            binding = studentCategoryBindingService.findStudentCategoryBindingByStudentAndCategory(studentId, categoryId);

            return convertToStudyPlan(binding);

        } catch (Exception e) {
            log.error("获取学习计划详情过程中发生异常: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudyPlan completeKnowledgePoint(Integer studentId, Integer knowledgePointId) {
        try {
            // 1. 获取知识点信息
            KnowledgePoint knowledgePoint = knowledgePointService.findKnowledgePointById(knowledgePointId);
            if (knowledgePoint == null) {
                log.error("知识点 {} 不存在", knowledgePointId);
                return null;
            }

            Integer categoryId = knowledgePoint.getCategoryId();

            // 2. 完成知识点学习
            LearningProgress progress = learningProgressService.completeLearning(studentId, knowledgePointId);
            if (progress == null) {
                // 如果没有现有进度记录，先创建再完成
                LearningProgress newProgress = new LearningProgress();
                newProgress.setUserId(studentId);
                newProgress.setKnowledgePointId(knowledgePointId);
                newProgress.setKnowledgeCategoryId(categoryId);
                newProgress.setProgressStatus(3); // 已完成
                newProgress.setCompletionPercentage(new BigDecimal("100.00"));
                newProgress.setStartTime(new Date());
                newProgress.setCompleteTime(new Date());
                newProgress.setLastStudyTime(new Date());
                newProgress.setStudyDuration(0);
                newProgress.setCreateAt(new Date());
                newProgress.setUpdateAt(new Date());
                newProgress.setDeleteFlag("N");

                progress = learningProgressService.insertLearningProgress(newProgress);
                if (progress == null) {
                    log.error("创建学习进度记录失败");
                    return null;
                }
            }

            // 3. 更新计划进度
            studentCategoryBindingService.updateStudyStatistics(studentId, categoryId);

            // 4. 返回更新后的计划
            log.info("学生 {} 完成知识点 {} 学习，计划进度已更新", studentId, knowledgePointId);
            return getStudyPlanDetail(studentId, categoryId);

        } catch (Exception e) {
            log.error("完成知识点学习过程中发生异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudyPlan uncompleteKnowledgePoint(Integer studentId, Integer knowledgePointId) {
        try {
            // 1. 获取知识点信息
            KnowledgePoint knowledgePoint = knowledgePointService.findKnowledgePointById(knowledgePointId);
            if (knowledgePoint == null) {
                log.error("知识点 {} 不存在", knowledgePointId);
                return null;
            }

            Integer categoryId = knowledgePoint.getCategoryId();

            // 2. 更新学习进度为未完成
            LearningProgress progress = learningProgressService.findLearningProgressByUserAndKnowledge(studentId, knowledgePointId);
            if (progress != null) {
                progress.setProgressStatus(1); // 未开始
                progress.setCompletionPercentage(BigDecimal.ZERO);
                progress.setCompleteTime(null);
                progress.setUpdateAt(new Date());

                learningProgressService.updateLearningProgress(progress);
            }

            // 3. 更新计划进度
            studentCategoryBindingService.updateStudyStatistics(studentId, categoryId);

            // 4. 返回更新后的计划
            log.info("学生 {} 取消完成知识点 {}，计划进度已更新", studentId, knowledgePointId);
            return getStudyPlanDetail(studentId, categoryId);

        } catch (Exception e) {
            log.error("取消完成知识点过程中发生异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean refreshPlanProgress(Integer studentId, Integer categoryId) {
        try {
            return studentCategoryBindingService.updateStudyStatistics(studentId, categoryId);
        } catch (Exception e) {
            log.error("刷新计划进度过程中发生异常: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public StudyPlan getStudyPlanByKnowledgePoint(Integer studentId, Integer knowledgePointId) {
        try {
            KnowledgePoint knowledgePoint = knowledgePointService.findKnowledgePointById(knowledgePointId);
            if (knowledgePoint == null) {
                log.error("知识点 {} 不存在", knowledgePointId);
                return null;
            }

            return getStudyPlanDetail(studentId, knowledgePoint.getCategoryId());

        } catch (Exception e) {
            log.error("根据知识点获取学习计划过程中发生异常: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将 StudentCategoryBinding 转换为 StudyPlan
     */
    private StudyPlan convertToStudyPlan(StudentCategoryBinding binding) {
        if (binding == null) {
            return null;
        }

        StudyPlan plan = new StudyPlan();
        plan.setId(binding.getId());
        plan.setStudentId(binding.getStudentId());
        plan.setCategoryId(binding.getCategoryId());
        plan.setGradeId(binding.getGradeId());

        // 获取分类信息
        KnowledgeCategory category = knowledgeCategoryService.findCategoryById(binding.getCategoryId());
        if (category != null) {
            plan.setPlanName(category.getCategoryName());
            plan.setPlanNameFr(category.getCategoryNameFr());
            plan.setCategoryCode(category.getCategoryCode());
            plan.setDescription(category.getDescription());
            plan.setDescriptionFr(category.getDescriptionFr());
            plan.setIconUrl(category.getIconUrl());
            plan.setIconClass(category.getIconClass());
        }

        // 进度信息
        plan.setProgressPercentage(binding.getOverallProgress() != null ? binding.getOverallProgress() : BigDecimal.ZERO);
        plan.setTotalKnowledgePoints(binding.getTotalKnowledgePoints() != null ? binding.getTotalKnowledgePoints() : 0);
        plan.setCompletedKnowledgePoints(binding.getCompletedKnowledgePoints() != null ? binding.getCompletedKnowledgePoints() : 0);
        plan.setInProgressKnowledgePoints(binding.getInProgressKnowledgePoints() != null ? binding.getInProgressKnowledgePoints() : 0);
        plan.setNotStartedKnowledgePoints(binding.getNotStartedKnowledgePoints() != null ? binding.getNotStartedKnowledgePoints() : 0);

        // 计算计划状态
        plan.setPlanStatus(calculatePlanStatus(plan.getCompletedKnowledgePoints(), plan.getTotalKnowledgePoints()));

        // 时间信息
        plan.setTotalStudyDuration(binding.getTotalStudyDuration());
        plan.setLastStudyTime(binding.getLastStudyTime());
        plan.setStartTime(binding.getStartTime());
        plan.setCompleteTime(binding.getCompleteTime());
        plan.setCreateAt(binding.getCreateAt());
        plan.setUpdateAt(binding.getUpdateAt());

        return plan;
    }

    /**
     * 计算计划状态
     * @param completed 已完成数
     * @param total 总数
     * @return 状态：1-未开始，2-进行中，3-已完成
     */
    private Integer calculatePlanStatus(Integer completed, Integer total) {
        if (completed == null || completed == 0) {
            return 1; // 未开始
        } else if (total != null && total > 0 && completed >= total) {
            return 3; // 已完成
        } else {
            return 2; // 进行中
        }
    }
}
