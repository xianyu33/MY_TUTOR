package com.yy.my_tutor.user.service.impl;

import com.yy.my_tutor.math.domain.KnowledgeCategory;
import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.math.domain.LearningProgress;
import com.yy.my_tutor.math.service.KnowledgeCategoryService;
import com.yy.my_tutor.math.service.KnowledgePointService;
import com.yy.my_tutor.math.service.LearningProgressService;
import com.yy.my_tutor.user.domain.StudentCategoryBinding;
import com.yy.my_tutor.user.mapper.StudentCategoryBindingMapper;
import com.yy.my_tutor.user.service.StudentCategoryBindingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 学生知识点分类绑定关系服务实现类
 */
@Slf4j
@Service
public class StudentCategoryBindingServiceImpl implements StudentCategoryBindingService {
    
    @Autowired
    private StudentCategoryBindingMapper studentCategoryBindingMapper;
    
    @Autowired
    private KnowledgeCategoryService knowledgeCategoryService;
    
    @Autowired
    private KnowledgePointService knowledgePointService;
    
    @Autowired
    private LearningProgressService learningProgressService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentCategoryBinding createStudentCategoryBinding(Integer studentId, Integer categoryId, Integer gradeId) {
        try {
            // 检查是否已存在绑定关系
            StudentCategoryBinding existing = studentCategoryBindingMapper.findStudentCategoryBindingByStudentAndCategory(studentId, categoryId);
            if (existing != null) {
                log.warn("学生 {} 与分类 {} 的绑定关系已存在", studentId, categoryId);
                return existing;
            }
            
            // 创建新的绑定关系
            StudentCategoryBinding binding = new StudentCategoryBinding();
            binding.setStudentId(studentId);
            binding.setCategoryId(categoryId);
            binding.setGradeId(gradeId);
            binding.setBindingStatus(1); // 已绑定
            binding.setOverallProgress(BigDecimal.ZERO);
            binding.setTotalKnowledgePoints(0);
            binding.setCompletedKnowledgePoints(0);
            binding.setInProgressKnowledgePoints(0);
            binding.setNotStartedKnowledgePoints(0);
            binding.setTotalStudyDuration(0);
            binding.setCreateAt(new Date());
            binding.setUpdateAt(new Date());
            binding.setDeleteFlag("N");
            
            int result = studentCategoryBindingMapper.insertStudentCategoryBinding(binding);
            if (result > 0) {
                log.info("为学生 {} 创建分类 {} 绑定关系成功", studentId, categoryId);
                return binding;
            } else {
                log.error("为学生 {} 创建分类 {} 绑定关系失败", studentId, categoryId);
                return null;
            }
            
        } catch (Exception e) {
            log.error("创建学生分类绑定关系过程中发生异常: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentCategoryBinding updateStudentCategoryBinding(StudentCategoryBinding binding) {
        try {
            binding.setUpdateAt(new Date());
            int result = studentCategoryBindingMapper.updateStudentCategoryBinding(binding);
            if (result > 0) {
                log.info("更新学生分类绑定关系成功，ID: {}", binding.getId());
                return binding;
            } else {
                log.error("更新学生分类绑定关系失败，ID: {}", binding.getId());
                return null;
            }
        } catch (Exception e) {
            log.error("更新学生分类绑定关系过程中发生异常: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public StudentCategoryBinding findStudentCategoryBindingById(Integer id) {
        return studentCategoryBindingMapper.findStudentCategoryBindingById(id);
    }
    
    @Override
    public StudentCategoryBinding findStudentCategoryBindingByStudentAndCategory(Integer studentId, Integer categoryId) {
        return studentCategoryBindingMapper.findStudentCategoryBindingByStudentAndCategory(studentId, categoryId);
    }
    
    @Override
    public List<StudentCategoryBinding> findStudentCategoryBindingsByStudentId(Integer studentId) {
        return studentCategoryBindingMapper.findStudentCategoryBindingsByStudentId(studentId);
    }
    
    @Override
    public List<StudentCategoryBinding> findStudentCategoryBindingsByStudentAndGrade(Integer studentId, Integer gradeId) {
        return studentCategoryBindingMapper.findStudentCategoryBindingsByStudentAndGrade(studentId, gradeId);
    }
    
    @Override
    public List<StudentCategoryBinding> findStudentCategoryBindingsByCategoryId(Integer categoryId) {
        return studentCategoryBindingMapper.findStudentCategoryBindingsByCategoryId(categoryId);
    }
    
    @Override
    public List<StudentCategoryBinding> findStudentCategoryBindingsByStatus(Integer studentId, Integer bindingStatus) {
        return studentCategoryBindingMapper.findStudentCategoryBindingsByStatus(studentId, bindingStatus);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBindingStatus(Integer studentId, Integer categoryId, Integer bindingStatus) {
        try {
            int result = studentCategoryBindingMapper.updateBindingStatus(studentId, categoryId, bindingStatus);
            if (result > 0) {
                log.info("更新学生 {} 分类 {} 绑定状态为 {} 成功", studentId, categoryId, bindingStatus);
                return true;
            } else {
                log.error("更新学生 {} 分类 {} 绑定状态失败", studentId, categoryId);
                return false;
            }
        } catch (Exception e) {
            log.error("更新绑定状态过程中发生异常: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOverallProgress(Integer studentId, Integer categoryId, BigDecimal overallProgress) {
        try {
            int result = studentCategoryBindingMapper.updateOverallProgress(studentId, categoryId, overallProgress);
            if (result > 0) {
                log.info("更新学生 {} 分类 {} 整体进度为 {}% 成功", studentId, categoryId, overallProgress);
                return true;
            } else {
                log.error("更新学生 {} 分类 {} 整体进度失败", studentId, categoryId);
                return false;
            }
        } catch (Exception e) {
            log.error("更新整体进度过程中发生异常: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStudyStatistics(Integer studentId, Integer categoryId) {
        try {
            // 获取该分类下的所有知识点
            List<KnowledgePoint> knowledgePoints = knowledgePointService.findKnowledgePointsByCategoryId(categoryId);
            if (knowledgePoints == null || knowledgePoints.isEmpty()) {
                log.warn("分类 {} 下没有知识点", categoryId);
                return true;
            }
            
            // 获取学生的学习进度
            List<LearningProgress> learningProgressList = learningProgressService.findLearningProgressByUserId(studentId);
            if (learningProgressList == null) {
                learningProgressList = new ArrayList<>();
            }
            
            // 统计该分类下的学习进度
            int totalKnowledgePoints = knowledgePoints.size();
            int completedKnowledgePoints = 0;
            int inProgressKnowledgePoints = 0;
            int notStartedKnowledgePoints = 0;
            int totalStudyDuration = 0;
            Date lastStudyTime = null;
            BigDecimal totalProgress = BigDecimal.ZERO;
            
            for (KnowledgePoint knowledgePoint : knowledgePoints) {
                LearningProgress progress = learningProgressList.stream()
                    .filter(p -> p.getKnowledgePointId().equals(knowledgePoint.getId()))
                    .findFirst()
                    .orElse(null);
                
                if (progress != null) {
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
                    
                    // 统计进度
                    BigDecimal completionPercentage = progress.getCompletionPercentage();
                    if (completionPercentage == null) {
                        completionPercentage = BigDecimal.ZERO;
                    }
                    totalProgress = totalProgress.add(completionPercentage);
                    
                    // 统计状态
                    Integer progressStatus = progress.getProgressStatus();
                    if (progressStatus == null) {
                        notStartedKnowledgePoints++;
                    } else {
                        switch (progressStatus) {
                            case 1: // 未开始
                                notStartedKnowledgePoints++;
                                break;
                            case 2: // 学习中
                                inProgressKnowledgePoints++;
                                break;
                            case 3: // 已完成
                                completedKnowledgePoints++;
                                break;
                        }
                    }
                } else {
                    notStartedKnowledgePoints++;
                }
            }
            
            // 计算整体进度
            BigDecimal overallProgress = totalKnowledgePoints > 0 ? 
                totalProgress.divide(new BigDecimal(totalKnowledgePoints), 2, BigDecimal.ROUND_HALF_UP) : 
                BigDecimal.ZERO;
            
            // 更新绑定关系
            StudentCategoryBinding binding = new StudentCategoryBinding();
            binding.setStudentId(studentId);
            binding.setCategoryId(categoryId);
            binding.setTotalKnowledgePoints(totalKnowledgePoints);
            binding.setCompletedKnowledgePoints(completedKnowledgePoints);
            binding.setInProgressKnowledgePoints(inProgressKnowledgePoints);
            binding.setNotStartedKnowledgePoints(notStartedKnowledgePoints);
            binding.setTotalStudyDuration(totalStudyDuration);
            binding.setLastStudyTime(lastStudyTime);
            binding.setOverallProgress(overallProgress);
            
            int result = studentCategoryBindingMapper.updateStudyStatistics(binding);
            if (result > 0) {
                log.info("更新学生 {} 分类 {} 学习统计信息成功", studentId, categoryId);
                return true;
            } else {
                log.error("更新学生 {} 分类 {} 学习统计信息失败", studentId, categoryId);
                return false;
            }
            
        } catch (Exception e) {
            log.error("更新学习统计信息过程中发生异常: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteStudentCategoryBinding(Integer id) {
        try {
            int result = studentCategoryBindingMapper.deleteStudentCategoryBinding(id);
            if (result > 0) {
                log.info("删除学生分类绑定关系成功，ID: {}", id);
                return true;
            } else {
                log.error("删除学生分类绑定关系失败，ID: {}", id);
                return false;
            }
        } catch (Exception e) {
            log.error("删除学生分类绑定关系过程中发生异常: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public List<StudentCategoryBinding> getStudentCategoryProgressDetails(Integer studentId) {
        try {
            // 获取学生的所有绑定关系
            List<StudentCategoryBinding> bindings = studentCategoryBindingMapper.findStudentCategoryBindingsByStudentId(studentId);
            if (bindings == null || bindings.isEmpty()) {
                log.warn("学生 {} 没有分类绑定关系", studentId);
                return new ArrayList<>();
            }
            
            // 为每个绑定关系补充详细信息
            for (StudentCategoryBinding binding : bindings) {
                // 补充分类信息
                KnowledgeCategory category = knowledgeCategoryService.findCategoryById(binding.getCategoryId());
                binding.setKnowledgeCategory(category);
                
                // 补充该分类下的知识点学习进度
                List<KnowledgePoint> knowledgePoints = knowledgePointService.findKnowledgePointsByCategoryId(binding.getCategoryId());
                List<LearningProgress> learningProgressList = learningProgressService.findLearningProgressByUserId(studentId);
                
                if (learningProgressList != null && knowledgePoints != null) {
                    List<LearningProgress> categoryProgressList = knowledgePoints.stream()
                        .map(kp -> learningProgressList.stream()
                            .filter(p -> p.getKnowledgePointId().equals(kp.getId()))
                            .findFirst()
                            .orElse(null))
                        .filter(p -> p != null)
                        .collect(Collectors.toList());
                    
                    binding.setLearningProgressList(categoryProgressList);
                }
                
                // 更新统计信息
                updateStudyStatistics(studentId, binding.getCategoryId());
            }
            
            log.info("获取学生 {} 分类学习进度详情成功，共 {} 个分类", studentId, bindings.size());
            return bindings;
            
        } catch (Exception e) {
            log.error("获取学生分类学习进度详情过程中发生异常: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public StudentCategoryBinding getStudentCategoryProgressDetail(Integer studentId, Integer categoryId) {
        try {
            // 获取绑定关系
            StudentCategoryBinding binding = studentCategoryBindingMapper.findStudentCategoryBindingByStudentAndCategory(studentId, categoryId);
            if (binding == null) {
                log.warn("学生 {} 与分类 {} 没有绑定关系", studentId, categoryId);
                return null;
            }
            
            // 补充分类信息
            KnowledgeCategory category = knowledgeCategoryService.findCategoryById(categoryId);
            binding.setKnowledgeCategory(category);
            
            // 补充该分类下的知识点学习进度
            List<KnowledgePoint> knowledgePoints = knowledgePointService.findKnowledgePointsByCategoryId(categoryId);
            List<LearningProgress> learningProgressList = learningProgressService.findLearningProgressByUserId(studentId);
            
            if (learningProgressList != null && knowledgePoints != null) {
                List<LearningProgress> categoryProgressList = knowledgePoints.stream()
                    .map(kp -> learningProgressList.stream()
                        .filter(p -> p.getKnowledgePointId().equals(kp.getId()))
                        .findFirst()
                        .orElse(null))
                    .filter(p -> p != null)
                    .collect(Collectors.toList());
                
                binding.setLearningProgressList(categoryProgressList);
            }
            
            // 更新统计信息
            updateStudyStatistics(studentId, categoryId);
            
            log.info("获取学生 {} 分类 {} 学习进度详情成功", studentId, categoryId);
            return binding;
            
        } catch (Exception e) {
            log.error("获取学生分类学习进度详情过程中发生异常: {}", e.getMessage(), e);
            return null;
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchCreateStudentCategoryBindings(Integer studentId, Integer gradeId) {
        try {
            // 获取该年级的所有分类
            List<KnowledgeCategory> categories = knowledgeCategoryService.findKnowledgeCategoriesByGradeId(gradeId);
            if (categories == null || categories.isEmpty()) {
                log.warn("年级 {} 没有知识点分类", gradeId);
                return 0;
            }
            
            int successCount = 0;
            for (KnowledgeCategory category : categories) {
                StudentCategoryBinding binding = createStudentCategoryBinding(studentId, category.getId(), gradeId);
                if (binding != null) {
                    successCount++;
                }
            }
            
            log.info("为学生 {} 批量创建分类绑定关系完成，成功 {} 个", studentId, successCount);
            return successCount;
            
        } catch (Exception e) {
            log.error("批量创建学生分类绑定关系过程中发生异常: {}", e.getMessage(), e);
            throw e;
        }
    }
}
