package com.yy.my_tutor.user.service.impl;

import com.yy.my_tutor.math.domain.Grade;
import com.yy.my_tutor.math.domain.KnowledgeCategory;
import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.math.domain.LearningProgress;
import com.yy.my_tutor.math.service.GradeService;
import com.yy.my_tutor.math.service.KnowledgeCategoryService;
import com.yy.my_tutor.math.service.KnowledgePointService;
import com.yy.my_tutor.math.service.LearningProgressService;
import com.yy.my_tutor.test.domain.Test;
import com.yy.my_tutor.test.service.TestService;
import com.yy.my_tutor.user.domain.CategoryWithProgress;
import com.yy.my_tutor.user.domain.KnowledgePointWithProgress;
import com.yy.my_tutor.user.domain.LearningProgressStats;
import com.yy.my_tutor.user.domain.StudentCategoryBinding;
import com.yy.my_tutor.user.domain.User;
import com.yy.my_tutor.user.mapper.UserMapper;
import com.yy.my_tutor.user.service.StudentRegistrationService;
import com.yy.my_tutor.user.service.StudentCategoryBindingService;
import com.yy.my_tutor.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 学生注册服务实现类
 */
@Slf4j
@Service
public class StudentRegistrationServiceImpl implements StudentRegistrationService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private KnowledgePointService knowledgePointService;
    
    @Autowired
    private KnowledgeCategoryService knowledgeCategoryService;
    
    @Autowired
    private LearningProgressService learningProgressService;

    @Autowired
    private TestService testService;

    @Autowired
    private StudentCategoryBindingService studentCategoryBindingService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean registerStudentWithCoursesAndTest(User user) {
        try {
            // 1. 先注册学生
            boolean registerResult = userService.register(user);
            if (!registerResult) {
                log.error("学生注册失败: {}", user.getUserAccount());
                return false;
            }

            // 2. 获取注册后的学生ID
            User registeredUser = userMapper.findByUserAccount(user.getUserAccount());
            if (registeredUser == null) {
                log.error("注册后无法找到学生: {}", user.getUserAccount());
                return false;
            }

            // 3. 根据年级分配课程
            Integer gradeLevel = parseGradeLevel(user.getGrade());
            if (gradeLevel != null) {
                boolean assignResult = assignCoursesByGrade(registeredUser.getId(), gradeLevel);
                if (!assignResult) {
                    log.warn("课程分配失败，但学生注册成功: {}", user.getUserAccount());
                }

                // 4. 生成测试题
                Grade grade = gradeService.findGradeByLevel(gradeLevel);
                if (grade != null) {
                    Integer testId = generateTestForGrade(grade.getId());
                    if (testId != null) {
                        log.info("为学生 {} 生成测试题成功，测试ID: {}", user.getUserAccount(), testId);
                    } else {
                        log.warn("测试题生成失败，但学生注册成功: {}", user.getUserAccount());
                    }
                }
            } else {
                log.warn("无法解析年级信息: {}", user.getGrade());
            }

            return true;
        } catch (Exception e) {
            log.error("学生注册过程中发生异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignCoursesByGrade(Integer userId, Integer gradeLevel) {
        try {
            // 1. 根据年级等级获取年级信息
            Grade grade = gradeService.findGradeByLevel(gradeLevel);
            if (grade == null) {
                log.error("找不到年级等级为 {} 的年级信息", gradeLevel);
                return false;
            }

            // 2. 根据年级ID获取所有知识点
            List<KnowledgePoint> knowledgePoints = knowledgePointService.findKnowledgePointsByGradeId(grade.getId());
            if (knowledgePoints == null || knowledgePoints.isEmpty()) {
                log.warn("年级 {} 没有找到知识点", grade.getGradeName());
                return true; // 没有知识点也算成功
            }

            // 3. 检查是否已经为该学生分配过课程
            List<LearningProgress> existingProgress = learningProgressService.findLearningProgressByUserId(userId);
            if (existingProgress != null && !existingProgress.isEmpty()) {
                log.info("学生 {} 已经分配过课程，跳过重复分配", userId);
                return true;
            }

            // 4. 为每个知识点创建学习进度记录
            int successCount = 0;
            int totalCount = knowledgePoints.size();

            // 5. 创建学生分类绑定关系
            int bindingCount = studentCategoryBindingService.batchCreateStudentCategoryBindings(userId, grade.getId());
            log.info("为学生 {} 创建分类绑定关系 {} 个", userId, bindingCount);

            for (KnowledgePoint knowledgePoint : knowledgePoints) {
                try {
                    // 检查是否已存在该知识点的学习进度
                    LearningProgress existing = learningProgressService.findLearningProgressByUserAndKnowledge(userId, knowledgePoint.getId());
                    if (existing != null) {
                        log.debug("知识点 {} 的学习进度已存在，跳过", knowledgePoint.getPointName());
                        successCount++;
                        continue;
                    }

                    LearningProgress progress = new LearningProgress();
                    progress.setUserId(userId);
                    progress.setKnowledgePointId(knowledgePoint.getId());
                    progress.setKnowledgeCategoryId(knowledgePoint.getCategoryId());
                    progress.setProgressStatus(1); // 未开始
                    progress.setCompletionPercentage(BigDecimal.ZERO);
                    progress.setStudyDuration(0);
                    progress.setCreateAt(new Date());
                    progress.setUpdateAt(new Date());
                    progress.setDeleteFlag("N");

                    LearningProgress result = learningProgressService.insertLearningProgress(progress);
                    if (result != null) {
                        successCount++;
                        log.debug("为知识点 {} 创建学习进度成功", knowledgePoint.getPointName());
                    } else {
                        log.error("为知识点 {} 创建学习进度失败", knowledgePoint.getPointName());
                    }
                } catch (Exception e) {
                    log.error("为知识点 {} 创建学习进度失败: {}", knowledgePoint.getPointName(), e.getMessage());
                }
            }

            log.info("为学生 {} 分配课程完成，成功创建 {}/{} 个学习进度记录", userId, successCount, totalCount);

            // 5. 记录分配结果统计
            if (successCount == totalCount) {
                log.info("学生 {} 的课程分配完全成功", userId);
            } else if (successCount > 0) {
                log.warn("学生 {} 的课程分配部分成功，成功 {} 个，失败 {} 个", userId, successCount, totalCount - successCount);
            } else {
                log.error("学生 {} 的课程分配完全失败", userId);
            }

            return successCount > 0;

        } catch (Exception e) {
            log.error("分配课程过程中发生异常: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Integer generateTestForGrade(Integer gradeId) {
        try {
            // 生成测试题，默认难度为2（中等），题目数量为10
            Test test = testService.generateTest(gradeId, 2, 10);
            if (test != null) {
                log.info("为年级 {} 生成测试题成功，测试ID: {}", gradeId, test.getId());
                return test.getId();
            } else {
                log.error("为年级 {} 生成测试题失败", gradeId);
                return null;
            }
        } catch (Exception e) {
            log.error("生成测试题过程中发生异常: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 解析年级字符串为年级等级
     * @param gradeStr 年级字符串，如 "9", "九年级", "初三" 等
     * @return 年级等级，如 9
     */
    private Integer parseGradeLevel(String gradeStr) {
        if (gradeStr == null || gradeStr.trim().isEmpty()) {
            return null;
        }

        try {
            // 尝试直接解析为数字
            return Integer.parseInt(gradeStr.trim());
        } catch (NumberFormatException e) {
            // 如果不是纯数字，尝试根据年级名称解析
            String grade = gradeStr.trim();
            switch (grade) {
                case "一年级":
                case "1年级":
                    return 1;
                case "二年级":
                case "2年级":
                    return 2;
                case "三年级":
                case "3年级":
                    return 3;
                case "四年级":
                case "4年级":
                    return 4;
                case "五年级":
                case "5年级":
                    return 5;
                case "六年级":
                case "6年级":
                    return 6;
                case "七年级":
                case "7年级":
                case "初一":
                    return 7;
                case "八年级":
                case "8年级":
                case "初二":
                    return 8;
                case "九年级":
                case "9年级":
                case "初三":
                    return 9;
                case "高一":
                case "10年级":
                    return 10;
                case "高二":
                case "11年级":
                    return 11;
                case "高三":
                case "12年级":
                    return 12;
                default:
                    log.warn("无法解析年级字符串: {}", gradeStr);
                    return null;
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reassignCoursesByGrade(Integer userId, Integer gradeLevel, boolean forceUpdate) {
        try {
            // 1. 验证学生是否存在
            User user = userMapper.findById(userId);
            if (user == null) {
                log.error("学生 {} 不存在", userId);
                return false;
            }

            // 2. 如果强制更新，先删除现有的学习进度
            if (forceUpdate) {
                List<LearningProgress> existingProgress = learningProgressService.findLearningProgressByUserId(userId);
                if (existingProgress != null && !existingProgress.isEmpty()) {
                    log.info("强制更新模式：删除学生 {} 的现有学习进度记录 {} 条", userId, existingProgress.size());
                    // 这里可以添加删除逻辑，或者标记为删除
                    for (LearningProgress progress : existingProgress) {
                        progress.setDeleteFlag("Y");
                        progress.setUpdateAt(new Date());
                        learningProgressService.updateLearningProgress(progress);
                    }
                }
            }

            // 3. 重新分配课程
            boolean result = assignCoursesByGrade(userId, gradeLevel);

            if (result) {
                log.info("为学生 {} 重新分配课程成功", userId);
            } else {
                log.error("为学生 {} 重新分配课程失败", userId);
            }

            return result;

        } catch (Exception e) {
            log.error("重新分配课程过程中发生异常: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public LearningProgressStats getLearningProgressStats(Integer userId) {
        try {
            // 1. 获取学生的学习进度
            List<LearningProgress> progressList = learningProgressService.findLearningProgressByUserId(userId);
            if (progressList == null || progressList.isEmpty()) {
                log.warn("学生 {} 没有学习进度记录", userId);
                return createEmptyStats(userId);
            }

            // 2. 获取学生信息
            User user = userMapper.findById(userId);
            if (user == null) {
                log.error("学生 {} 不存在", userId);
                return createEmptyStats(userId);
            }

            // 3. 解析年级信息
            Integer gradeLevel = parseGradeLevel(user.getGrade());
            Grade grade = null;
            if (gradeLevel != null) {
                grade = gradeService.findGradeByLevel(gradeLevel);
            }

            // 4. 统计学习进度
            LearningProgressStats stats = new LearningProgressStats();
            stats.setUserId(userId);
            stats.setGradeLevel(gradeLevel);
            stats.setGradeName(grade != null ? grade.getGradeName() : "未知年级");
            stats.setTotalKnowledgePoints(progressList.size());

            int notStartedCount = 0;
            int inProgressCount = 0;
            int completedCount = 0;
            int totalStudyDuration = 0;
            BigDecimal totalCompletionPercentage = BigDecimal.ZERO;
            Date lastStudyTime = null;

            LearningProgressStats.ProgressDistribution distribution = new LearningProgressStats.ProgressDistribution();
            int lowProgressCount = 0;
            int mediumProgressCount = 0;
            int highProgressCount = 0;
            int nearCompleteCount = 0;
            int completeCount = 0;

            for (LearningProgress progress : progressList) {
                // 统计状态
                if (progress.getProgressStatus() == null) {
                    notStartedCount++;
                } else {
                    switch (progress.getProgressStatus()) {
                        case 1: // 未开始
                            notStartedCount++;
                            break;
                        case 2: // 学习中
                            inProgressCount++;
                            break;
                        case 3: // 已完成
                            completedCount++;
                            break;
                    }
                }

                // 统计完成百分比
                BigDecimal completionPercentage = progress.getCompletionPercentage();
                if (completionPercentage == null) {
                    completionPercentage = BigDecimal.ZERO;
                }
                totalCompletionPercentage = totalCompletionPercentage.add(completionPercentage);

                // 统计学习时长
                if (progress.getStudyDuration() != null) {
                    totalStudyDuration += progress.getStudyDuration();
                }

                // 更新最后学习时间
                if (progress.getLastStudyTime() != null) {
                    if (lastStudyTime == null || progress.getLastStudyTime().after(lastStudyTime)) {
                        lastStudyTime = progress.getLastStudyTime();
                    }
                }

                // 统计完成度分布
                double percentage = completionPercentage.doubleValue();
                if (percentage == 0) {
                    lowProgressCount++;
                } else if (percentage > 0 && percentage <= 25) {
                    lowProgressCount++;
                } else if (percentage > 25 && percentage <= 50) {
                    mediumProgressCount++;
                } else if (percentage > 50 && percentage <= 75) {
                    highProgressCount++;
                } else if (percentage > 75 && percentage < 100) {
                    nearCompleteCount++;
                } else if (percentage == 100) {
                    completeCount++;
                }
            }

            // 5. 设置统计结果
            stats.setNotStartedCount(notStartedCount);
            stats.setInProgressCount(inProgressCount);
            stats.setCompletedCount(completedCount);
            stats.setTotalStudyDuration(totalStudyDuration);

            // 计算总体完成百分比
            if (progressList.size() > 0) {
                BigDecimal overallPercentage = totalCompletionPercentage.divide(new BigDecimal(progressList.size()), 2, BigDecimal.ROUND_HALF_UP);
                stats.setOverallCompletionPercentage(overallPercentage);
            } else {
                stats.setOverallCompletionPercentage(BigDecimal.ZERO);
            }

            // 设置最后学习时间
            if (lastStudyTime != null) {
                stats.setLastStudyTime(lastStudyTime.toString());
            }

            // 设置分布统计
            distribution.setLowProgressCount(lowProgressCount);
            distribution.setMediumProgressCount(mediumProgressCount);
            distribution.setHighProgressCount(highProgressCount);
            distribution.setNearCompleteCount(nearCompleteCount);
            distribution.setCompleteCount(completeCount);
            stats.setDistribution(distribution);

            log.info("获取学生 {} 的学习进度统计成功", userId);
            return stats;

        } catch (Exception e) {
            log.error("获取学习进度统计过程中发生异常: {}", e.getMessage(), e);
            return createEmptyStats(userId);
        }
    }

    /**
     * 创建空的学习进度统计
     */
    private LearningProgressStats createEmptyStats(Integer userId) {
        LearningProgressStats stats = new LearningProgressStats();
        stats.setUserId(userId);
        stats.setTotalKnowledgePoints(0);
        stats.setNotStartedCount(0);
        stats.setInProgressCount(0);
        stats.setCompletedCount(0);
        stats.setOverallCompletionPercentage(BigDecimal.ZERO);
        stats.setTotalStudyDuration(0);

        LearningProgressStats.ProgressDistribution distribution = new LearningProgressStats.ProgressDistribution();
        distribution.setLowProgressCount(0);
        distribution.setMediumProgressCount(0);
        distribution.setHighProgressCount(0);
        distribution.setNearCompleteCount(0);
        distribution.setCompleteCount(0);
        stats.setDistribution(distribution);

        return stats;
    }

    @Override
    public List<KnowledgeCategory> getStudentBoundCategories(Integer userId) {
        try {
            // 1. 获取学生的学习进度记录
            List<LearningProgress> progressList = learningProgressService.findLearningProgressByUserId(userId);
            if (progressList == null || progressList.isEmpty()) {
                log.warn("学生 {} 没有学习进度记录", userId);
                return new ArrayList<>();
            }

            // 2. 提取唯一的分类ID
            Set<Integer> categoryIds = progressList.stream()
                .map(LearningProgress::getKnowledgeCategoryId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

            if (categoryIds.isEmpty()) {
                log.warn("学生 {} 的学习进度中没有分类信息", userId);
                return new ArrayList<>();
            }

            // 3. 根据分类ID获取分类信息
            List<KnowledgeCategory> categories = new ArrayList<>();
            for (Integer categoryId : categoryIds) {
                KnowledgeCategory category = knowledgeCategoryService.findCategoryById(categoryId);
                if (category != null) {
                    categories.add(category);
                }
            }

            // 4. 按排序字段排序
            categories.sort(Comparator.comparing(KnowledgeCategory::getSortOrder));

            log.info("获取学生 {} 绑定的分类列表成功，共 {} 个分类", userId, categories.size());
            return categories;

        } catch (Exception e) {
            log.error("获取学生绑定分类过程中发生异常: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<KnowledgeCategory> getCategoriesByGrade(Integer gradeId) {
        try {
            List<KnowledgeCategory> categories = knowledgeCategoryService.findKnowledgeCategoriesByGradeId(gradeId);
            if (categories == null) {
                categories = new ArrayList<>();
            }

            log.info("获取年级 {} 的分类列表成功，共 {} 个分类", gradeId, categories.size());
            return categories;

        } catch (Exception e) {
            log.error("获取年级分类过程中发生异常: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<StudentCategoryBinding> getStudentCategoryProgressDetails(Integer userId) {
        try {
            List<StudentCategoryBinding> details = studentCategoryBindingService.getStudentCategoryProgressDetails(userId);
            log.info("获取学生 {} 分类学习进度详情成功，共 {} 个分类", userId, details.size());
            return details;
        } catch (Exception e) {
            log.error("获取学生分类学习进度详情过程中发生异常: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public StudentCategoryBinding getStudentCategoryProgressDetail(Integer userId, Integer categoryId) {
        try {
            StudentCategoryBinding detail = studentCategoryBindingService.getStudentCategoryProgressDetail(userId, categoryId);
            if (detail != null) {
                log.info("获取学生 {} 分类 {} 学习进度详情成功", userId, categoryId);
            } else {
                log.warn("学生 {} 与分类 {} 没有绑定关系", userId, categoryId);
            }
            return detail;
        } catch (Exception e) {
            log.error("获取学生分类学习进度详情过程中发生异常: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<CategoryWithProgress> getStudentBoundCategoriesWithProgress(Integer userId) {
        try {
            // 1. 获取学生的分类绑定关系
            List<StudentCategoryBinding> bindings = studentCategoryBindingService.getStudentCategoryProgressDetails(userId);
            if (bindings == null || bindings.isEmpty()) {
                log.warn("学生 {} 没有分类绑定关系", userId);
                return new ArrayList<>();
            }

            // 2. 为每个分类创建增强信息对象
            List<CategoryWithProgress> result = new ArrayList<>();
            for (StudentCategoryBinding binding : bindings) {
                CategoryWithProgress categoryWithProgress = new CategoryWithProgress();
                
                // 获取分类基本信息
                KnowledgeCategory category = knowledgeCategoryService.findCategoryById(binding.getCategoryId());
                if (category == null) {
                    log.warn("分类 {} 不存在", binding.getCategoryId());
                    continue;
                }
                
                // 设置分类基本信息
                categoryWithProgress.setId(category.getId());
                categoryWithProgress.setCategoryName(category.getCategoryName());
                categoryWithProgress.setCategoryNameFr(category.getCategoryNameFr());
                categoryWithProgress.setCategoryCode(category.getCategoryCode());
                categoryWithProgress.setDescription(category.getDescription());
                categoryWithProgress.setDescriptionFr(category.getDescriptionFr());
                categoryWithProgress.setGradeId(category.getGradeId());
                categoryWithProgress.setIconUrl(category.getIconUrl());
                categoryWithProgress.setIconClass(category.getIconClass());
                categoryWithProgress.setSortOrder(category.getSortOrder());
                
                // 设置学习进度统计
                categoryWithProgress.setTotalKnowledgePoints(binding.getTotalKnowledgePoints());
                categoryWithProgress.setCompletedKnowledgePoints(binding.getCompletedKnowledgePoints());
                categoryWithProgress.setInProgressKnowledgePoints(binding.getInProgressKnowledgePoints());
                categoryWithProgress.setNotStartedKnowledgePoints(binding.getNotStartedKnowledgePoints());
                categoryWithProgress.setOverallProgress(binding.getOverallProgress());
                categoryWithProgress.setTotalStudyDuration(binding.getTotalStudyDuration());
                categoryWithProgress.setLastStudyTime(binding.getLastStudyTime());
                categoryWithProgress.setStartTime(binding.getStartTime());
                categoryWithProgress.setCompleteTime(binding.getCompleteTime());
                categoryWithProgress.setBindingStatus(binding.getBindingStatus());
                
                // 计算难度分布
                calculateDifficultyDistribution(categoryWithProgress, userId, category.getId());
                
                // 设置创建和更新时间
                categoryWithProgress.setCreateAt(binding.getCreateAt());
                categoryWithProgress.setUpdateAt(binding.getUpdateAt());
                
                result.add(categoryWithProgress);
            }
            
            // 3. 按sortOrder排序
            result.sort(Comparator.comparing(CategoryWithProgress::getSortOrder));
            
            log.info("获取学生 {} 的分类列表成功，共 {} 个分类", userId, result.size());
            return result;
            
        } catch (Exception e) {
            log.error("获取学生绑定分类过程发生异常: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 计算分类下的难度分布
     */
    private void calculateDifficultyDistribution(CategoryWithProgress categoryWithProgress, Integer userId, Integer categoryId) {
        try {
            // 获取分类下的所有知识点
            List<KnowledgePoint> knowledgePoints = knowledgePointService.findKnowledgePointsByCategoryId(categoryId);
            
            if (knowledgePoints == null || knowledgePoints.isEmpty()) {
                categoryWithProgress.setEasyCount(0);
                categoryWithProgress.setMediumCount(0);
                categoryWithProgress.setHardCount(0);
                categoryWithProgress.setAverageDifficulty(BigDecimal.ZERO);
                return;
            }
            
            // 创建知识点ID到难度的映射
            Map<Integer, Integer> pointDifficultyMap = knowledgePoints.stream()
                .collect(Collectors.toMap(
                    KnowledgePoint::getId, 
                    kp -> kp.getDifficultyLevel() != null ? kp.getDifficultyLevel() : 1
                ));
            
            // 统计难度分布
            int easyCount = 0;
            int mediumCount = 0;
            int hardCount = 0;
            int totalDifficulty = 0;
            int countedPoints = 0;
            
            for (KnowledgePoint kp : knowledgePoints) {
                Integer difficulty = pointDifficultyMap.get(kp.getId());
                if (difficulty == null) {
                    difficulty = 1;
                }
                
                switch (difficulty) {
                    case 1:
                        easyCount++;
                        break;
                    case 2:
                        mediumCount++;
                        break;
                    case 3:
                        hardCount++;
                        break;
                }
                
                totalDifficulty += difficulty;
                countedPoints++;
            }
            
            categoryWithProgress.setEasyCount(easyCount);
            categoryWithProgress.setMediumCount(mediumCount);
            categoryWithProgress.setHardCount(hardCount);
            
            // 计算平均难度
            if (countedPoints > 0) {
                BigDecimal avgDifficulty = new BigDecimal(totalDifficulty)
                    .divide(new BigDecimal(countedPoints), 2, BigDecimal.ROUND_HALF_UP);
                categoryWithProgress.setAverageDifficulty(avgDifficulty);
            } else {
                categoryWithProgress.setAverageDifficulty(BigDecimal.ZERO);
            }
            
        } catch (Exception e) {
            log.error("计算难度分布时发生异常: {}", e.getMessage());
            categoryWithProgress.setEasyCount(0);
            categoryWithProgress.setMediumCount(0);
            categoryWithProgress.setHardCount(0);
            categoryWithProgress.setAverageDifficulty(BigDecimal.ZERO);
        }
    }
    
    @Override
    public List<KnowledgePointWithProgress> getKnowledgePointsWithProgress(Integer userId, Integer gradeId, Integer categoryId) {
        try {
            // 1. 获取知识点列表
            List<KnowledgePoint> knowledgePoints = knowledgePointService.findKnowledgePointsByGradeAndCategory(gradeId, categoryId);
            if (knowledgePoints == null || knowledgePoints.isEmpty()) {
                log.warn("年级 {} 分类 {} 没有找到知识点", gradeId, categoryId);
                return new ArrayList<>();
            }
            
            // 2. 获取分类信息
            KnowledgeCategory category = knowledgeCategoryService.findCategoryById(categoryId);
            if (category == null) {
                log.error("分类 {} 不存在", categoryId);
                return new ArrayList<>();
            }
            
            // 3. 获取年级信息
            Grade grade = gradeService.findGradeById(gradeId);
            if (grade == null) {
                log.error("年级 {} 不存在", gradeId);
                return new ArrayList<>();
            }
            
            // 4. 获取学生的学习进度
            Map<Integer, LearningProgress> progressMap = new HashMap<>();
            if (userId != null) {
                List<LearningProgress> progressList = learningProgressService.findLearningProgressByUserId(userId);
                if (progressList != null) {
                    progressMap = progressList.stream()
                        .filter(p -> p.getKnowledgePointId() != null)
                        .collect(Collectors.toMap(LearningProgress::getKnowledgePointId, p -> p));
                }
            }
            
            // 5. 构建包含学习进度的知识点列表
            List<KnowledgePointWithProgress> result = new ArrayList<>();
            for (KnowledgePoint kp : knowledgePoints) {
                KnowledgePointWithProgress kpWithProgress = new KnowledgePointWithProgress();
                
                // 设置基本信息
                kpWithProgress.setId(kp.getId());
                kpWithProgress.setGradeId(kp.getGradeId());
                kpWithProgress.setCategoryId(kp.getCategoryId());
                kpWithProgress.setPointName(kp.getPointName());
                kpWithProgress.setPointNameFr(kp.getPointNameFr());
                kpWithProgress.setPointCode(kp.getPointCode());
                kpWithProgress.setDescription(kp.getDescription());
                kpWithProgress.setDescriptionFr(kp.getDescriptionFr());
                kpWithProgress.setContent(kp.getContent());
                kpWithProgress.setContentFr(kp.getContentFr());
                kpWithProgress.setIconUrl(kp.getIconUrl());
                kpWithProgress.setIconClass(kp.getIconClass());
                kpWithProgress.setDifficultyLevel(kp.getDifficultyLevel());
                kpWithProgress.setSortOrder(kp.getSortOrder());
                kpWithProgress.setLearningObjectives(kp.getLearningObjectives());
                kpWithProgress.setLearningObjectivesFr(kp.getLearningObjectivesFr());
                kpWithProgress.setCreateAt(kp.getCreateAt());
                kpWithProgress.setUpdateAt(kp.getUpdateAt());
                
                // 设置分类信息
                kpWithProgress.setCategoryName(category.getCategoryName());
                kpWithProgress.setCategoryNameFr(category.getCategoryNameFr());
                kpWithProgress.setCategoryCode(category.getCategoryCode());
                
                // 设置年级信息
                kpWithProgress.setGradeName(grade.getGradeName());
                kpWithProgress.setGradeLevel(grade.getGradeLevel());
                
                // 设置学习进度
                LearningProgress progress = progressMap.get(kp.getId());
                if (progress != null) {
                    kpWithProgress.setProgressStatus(progress.getProgressStatus());
                    kpWithProgress.setCompletionPercentage(progress.getCompletionPercentage());
                    kpWithProgress.setStartTime(progress.getStartTime());
                    kpWithProgress.setCompleteTime(progress.getCompleteTime());
                    kpWithProgress.setStudyDuration(progress.getStudyDuration());
                    kpWithProgress.setLastStudyTime(progress.getLastStudyTime());
                    kpWithProgress.setNotes(progress.getNotes());
                } else {
                    // 如果没有学习进度，设置为未开始
                    kpWithProgress.setProgressStatus(1);
                    kpWithProgress.setCompletionPercentage(BigDecimal.ZERO);
                    kpWithProgress.setStudyDuration(0);
                }
                
                // 设置难度信息
                setDifficultyInfo(kpWithProgress, kp.getDifficultyLevel());
                
                result.add(kpWithProgress);
            }
            
            // 6. 按排序字段排序
            result.sort(Comparator.comparing(KnowledgePointWithProgress::getSortOrder));
            
            log.info("获取学生 {} 年级 {} 分类 {} 的知识点列表成功，共 {} 条", userId, gradeId, categoryId, result.size());
            return result;
            
        } catch (Exception e) {
            log.error("获取知识点详情过程中发生异常: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 设置难度信息
     */
    private void setDifficultyInfo(KnowledgePointWithProgress kpWithProgress, Integer difficultyLevel) {
        if (difficultyLevel == null) {
            kpWithProgress.setDifficultyName("未知");
            kpWithProgress.setDifficultyDescription("难度等级未设置");
            return;
        }
        
        switch (difficultyLevel) {
            case 1:
                kpWithProgress.setDifficultyName("简单");
                kpWithProgress.setDifficultyDescription("基础概念，容易理解和掌握");
                break;
            case 2:
                kpWithProgress.setDifficultyName("中等");
                kpWithProgress.setDifficultyDescription("需要一定练习才能掌握的难度");
                break;
            case 3:
                kpWithProgress.setDifficultyName("困难");
                kpWithProgress.setDifficultyDescription("高级概念，需要付出较多努力");
                break;
            default:
                kpWithProgress.setDifficultyName("未知");
                kpWithProgress.setDifficultyDescription("难度等级超出范围");
        }
    }
}

