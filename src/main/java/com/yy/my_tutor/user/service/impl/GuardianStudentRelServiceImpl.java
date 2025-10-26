package com.yy.my_tutor.user.service.impl;

import com.yy.my_tutor.math.domain.KnowledgeCategory;
import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.math.domain.LearningProgress;
import com.yy.my_tutor.math.service.KnowledgeCategoryService;
import com.yy.my_tutor.math.service.KnowledgePointService;
import com.yy.my_tutor.math.service.LearningProgressService;
import com.yy.my_tutor.user.domain.GuardianStudentRel;
import com.yy.my_tutor.user.domain.StudentDetailDTO;
import com.yy.my_tutor.user.mapper.GuardianStudentRelMapper;
import com.yy.my_tutor.user.service.GuardianStudentRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuardianStudentRelServiceImpl implements GuardianStudentRelService {

    @Autowired
    private GuardianStudentRelMapper relMapper;
    
    @Autowired
    private LearningProgressService learningProgressService;
    
    @Autowired
    private KnowledgeCategoryService knowledgeCategoryService;
    
    @Autowired
    private KnowledgePointService knowledgePointService;

    @Override
    public List<GuardianStudentRel> listByGuardian(Integer guardianId, Integer guardianType) {
        return relMapper.findByGuardian(guardianId, guardianType);
    }
    
    @Override
    public List<GuardianStudentRel> listByGuardianWithDetails(Integer guardianId, Integer guardianType) {
        return relMapper.findByGuardianWithDetails(guardianId, guardianType);
    }

    @Override
    public List<GuardianStudentRel> listByStudent(Integer studentId) {
        return relMapper.findByStudent(studentId);
    }

    @Override
    public GuardianStudentRel bind(Integer guardianId, Integer guardianType, Integer studentId, String relation, String operator) {
        GuardianStudentRel exists = relMapper.findUnique(guardianId, studentId);
        if (exists != null) {
            return exists;
        }
        GuardianStudentRel rel = new GuardianStudentRel();
        rel.setGuardianId(guardianId);
        rel.setGuardianType(guardianType);
        rel.setStudentId(studentId);
        rel.setRelation(relation);
        rel.setStartAt(new Date());
        rel.setCreateAt(new Date());
        rel.setUpdateAt(new Date());
        rel.setCreateBy(operator);
        rel.setUpdateBy(operator);
        rel.setDeleteFlag("0");
        relMapper.insert(rel);
        return rel;
    }

    @Override
    public boolean unbind(Long id) {
        return relMapper.logicalDelete(id) > 0;
    }

    @Override
    public GuardianStudentRel updateRelation(Long id, String relation, String operator) {
        GuardianStudentRel rel = new GuardianStudentRel();
        rel.setId(id);
        rel.setRelation(relation);
        rel.setUpdateBy(operator);
        rel.setUpdateAt(new Date());
        int cnt = relMapper.update(rel);
        return cnt > 0 ? rel : null;
    }
    
    @Override
    public List<StudentDetailDTO> getStudentsWithDetailsByGuardian(Integer guardianId, Integer guardianType) {
        // 查询带详细信息的关联记录
        List<GuardianStudentRel> relations = relMapper.findByGuardianWithDetails(guardianId, guardianType);
        
        List<StudentDetailDTO> studentDetails = new ArrayList<>();
        
        for (GuardianStudentRel rel : relations) {
            if (rel.getStudent() == null) {
                continue;
            }
            
            StudentDetailDTO detail = new StudentDetailDTO();
            
            // 设置关系信息
            detail.setRelationId(rel.getId());
            detail.setRelation(rel.getRelation());
            detail.setRelationStartAt(rel.getStartAt());
            detail.setRelationEndAt(rel.getEndAt());
            
            // 设置学生信息
            detail.setStudentId(rel.getStudent().getId());
            detail.setStudentAccount(rel.getStudent().getUserAccount());
            detail.setStudentName(rel.getStudent().getUsername());
            detail.setStudentSex(rel.getStudent().getSex());
            detail.setStudentAge(rel.getStudent().getAge());
            detail.setStudentTel(rel.getStudent().getTel());
            detail.setStudentCountry(rel.getStudent().getCountry());
            detail.setStudentEmail(rel.getStudent().getEmail());
            detail.setStudentGrade(rel.getStudent().getGrade());
            detail.setStudentRole(rel.getStudent().getRole());
            detail.setStudentCreateAt(rel.getStudent().getCreateAt());
            detail.setStudentUpdateAt(rel.getStudent().getUpdateAt());
            
            // 获取学生各知识点类型的学习情况
            List<StudentDetailDTO.CategoryLearningProgress> categoryProgressList = getCategoryLearningProgress(rel.getStudent().getId());
            detail.setCategoryLearningProgress(categoryProgressList);
            
            studentDetails.add(detail);
        }
        
        return studentDetails;
    }
    
    /**
     * 获取学生各知识点类型的学习情况
     */
    private List<StudentDetailDTO.CategoryLearningProgress> getCategoryLearningProgress(Integer studentId) {
        List<StudentDetailDTO.CategoryLearningProgress> categoryProgressList = new ArrayList<>();
        
        try {
            // 1. 获取学生的所有学习进度记录
            List<LearningProgress> allProgress = learningProgressService.findLearningProgressByUserId(studentId);
            if (allProgress == null || allProgress.isEmpty()) {
                return categoryProgressList;
            }
            
            // 2. 获取所有知识点分类
            List<KnowledgeCategory> categories = knowledgeCategoryService.findAllCategories();
            
            // 3. 为每个分类统计学习情况
            for (KnowledgeCategory category : categories) {
                // 获取该分类下的所有知识点
                List<KnowledgePoint> knowledgePoints = knowledgePointService.findKnowledgePointsByCategoryId(category.getId());
                if (knowledgePoints == null || knowledgePoints.isEmpty()) {
                    continue;
                }
                
                int totalKnowledgePoints = knowledgePoints.size();
                int completedCount = 0;
                int inProgressCount = 0;
                int notStartedCount = 0;
                int easyCount = 0;
                int mediumCount = 0;
                int hardCount = 0;
                BigDecimal totalProgress = BigDecimal.ZERO;
                
                // 统计该分类下的学习情况
                for (KnowledgePoint kp : knowledgePoints) {
                    // 查找该知识点的学习进度
                    LearningProgress progress = allProgress.stream()
                        .filter(p -> p.getKnowledgePointId().equals(kp.getId()))
                        .findFirst()
                        .orElse(null);
                    
                    // 统计状态
                    if (progress == null) {
                        notStartedCount++;
                    } else {
                        Integer status = progress.getProgressStatus();
                        if (status == null) {
                            notStartedCount++;
                        } else if (status == 3) {
                            completedCount++;
                        } else if (status == 2) {
                            inProgressCount++;
                        } else {
                            notStartedCount++;
                        }
                        
                        // 累加进度
                        if (progress.getCompletionPercentage() != null) {
                            totalProgress = totalProgress.add(progress.getCompletionPercentage());
                        }
                    }
                    
                    // 统计难度分布
                    Integer difficulty = kp.getDifficultyLevel();
                    if (difficulty != null) {
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
                    }
                }
                
                // 计算整体进度百分比
                BigDecimal overallProgress = BigDecimal.ZERO;
                if (totalKnowledgePoints > 0) {
                    overallProgress = totalProgress.divide(
                        BigDecimal.valueOf(totalKnowledgePoints), 2, BigDecimal.ROUND_HALF_UP
                    );
                }
                
                // 创建分类学习进度对象
                StudentDetailDTO.CategoryLearningProgress categoryProgress = new StudentDetailDTO.CategoryLearningProgress();
                categoryProgress.setCategoryId(category.getId());
                categoryProgress.setCategoryName(category.getCategoryName());
                categoryProgress.setCategoryNameFr(category.getCategoryNameFr());
                categoryProgress.setTotalKnowledgePoints(totalKnowledgePoints);
                categoryProgress.setCompletedKnowledgePoints(completedCount);
                categoryProgress.setInProgressKnowledgePoints(inProgressCount);
                categoryProgress.setNotStartedKnowledgePoints(notStartedCount);
                categoryProgress.setOverallProgress(overallProgress);
                categoryProgress.setEasyCount(easyCount);
                categoryProgress.setMediumCount(mediumCount);
                categoryProgress.setHardCount(hardCount);
                
                categoryProgressList.add(categoryProgress);
            }
            
            // 按分类排序
            categoryProgressList.sort((a, b) -> {
                if (a.getCategoryId() != null && b.getCategoryId() != null) {
                    return a.getCategoryId().compareTo(b.getCategoryId());
                }
                return 0;
            });
            
        } catch (Exception e) {
            // 如果查询失败，返回空列表
            System.err.println("获取知识点类型学习情况失败: " + e.getMessage());
        }
        
        return categoryProgressList;
    }
}


