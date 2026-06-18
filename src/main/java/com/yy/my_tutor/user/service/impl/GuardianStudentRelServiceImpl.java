package com.yy.my_tutor.user.service.impl;

import com.yy.my_tutor.math.domain.Grade;
import com.yy.my_tutor.math.domain.KnowledgeCategory;
import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.math.domain.LearningProgress;
import com.yy.my_tutor.math.service.GradeService;
import com.yy.my_tutor.math.service.KnowledgeCategoryService;
import com.yy.my_tutor.math.service.KnowledgePointService;
import com.yy.my_tutor.math.service.LearningProgressService;
import com.yy.my_tutor.payment.service.AnnualLicenseService;
import com.yy.my_tutor.payment.util.PaymentException;
import com.yy.my_tutor.user.domain.GuardianStudentRel;
import com.yy.my_tutor.user.domain.StudentDetailDTO;
import com.yy.my_tutor.user.domain.User;
import com.yy.my_tutor.user.mapper.GuardianStudentRelMapper;
import com.yy.my_tutor.user.mapper.UserMapper;
import com.yy.my_tutor.user.service.GuardianStudentRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GuardianStudentRelServiceImpl implements GuardianStudentRelService {

    @Autowired
    private GuardianStudentRelMapper relMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GradeService gradeService;
    
    @Autowired
    private LearningProgressService learningProgressService;
    
    @Autowired
    private KnowledgeCategoryService knowledgeCategoryService;
    
    @Autowired
    private KnowledgePointService knowledgePointService;

    @Autowired
    private AnnualLicenseService annualLicenseService;

    @Override
    public List<GuardianStudentRel> listByGuardian(Integer guardianId, Integer guardianType) {
        List<GuardianStudentRel> list = relMapper.findByGuardian(guardianId, guardianType);
        if (list == null || list.isEmpty()) {
            return list;
        }
        for (GuardianStudentRel rel : list) {
            if (rel.getStudentId() != null) {
                rel.setCategoryLearningProgress(getCategoryLearningProgress(rel.getStudentId()));
            }
        }
        return list;
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
    @Transactional(rollbackFor = Exception.class)
    public GuardianStudentRel bind(Integer guardianId, Integer guardianType, Integer studentId,
                                   String relation, String operator, Boolean activate) {
        GuardianStudentRel exists = relMapper.findUnique(guardianId, studentId);
        if (exists != null) {
            activateIfRequested(guardianId, guardianType, studentId, operator, activate);
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
        activateIfRequested(guardianId, guardianType, studentId, operator, activate);
        return rel;
    }

    private void activateIfRequested(Integer guardianId, Integer guardianType, Integer studentId,
                                     String operator, Boolean activate) {
        if (!Boolean.TRUE.equals(activate)) {
            return;
        }
        if (guardianType == null || guardianType != 1) {
            throw PaymentException.of("PAYMENT_ACTIVATION_TEACHER_REQUIRED", "只有老师绑定学生时可以激活年度授权");
        }
        annualLicenseService.activateForTeacherBind(guardianId, studentId, operator);
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
            detail.setAvatarUrl(rel.getStudent().getAvatarUrl());
            
            // 获取学生各知识点类型的学习情况
            List<StudentDetailDTO.CategoryLearningProgress> categoryProgressList = getCategoryLearningProgress(rel.getStudent().getId());
            detail.setCategoryLearningProgress(categoryProgressList);
            
            studentDetails.add(detail);
        }
        
        return studentDetails;
    }
    
    /**
     * 获取学生各知识大类的学习情况：大类进度 = 该年级下该大类所有知识点（小类）完成度之和 / 小类数量。
     */
    private List<StudentDetailDTO.CategoryLearningProgress> getCategoryLearningProgress(Integer studentId) {
        List<StudentDetailDTO.CategoryLearningProgress> categoryProgressList = new ArrayList<>();
        
        try {
            Map<Integer, LearningProgress> progressByKpId = new HashMap<>();
            List<LearningProgress> allProgress = learningProgressService.findLearningProgressByUserId(studentId);
            if (allProgress != null) {
                for (LearningProgress lp : allProgress) {
                    if (lp.getKnowledgePointId() != null) {
                        progressByKpId.put(lp.getKnowledgePointId(), lp);
                    }
                }
            }

            User student = userMapper.findById(studentId);
            Integer gradeId = null;
            if (student != null && student.getGrade() != null) {
                Integer level = parseGradeLevel(student.getGrade());
                if (level != null) {
                    Grade g = gradeService.findGradeByLevel(level);
                    if (g != null) {
                        gradeId = g.getId();
                    }
                }
            }

            List<KnowledgeCategory> categories;
            if (gradeId != null) {
                categories = knowledgeCategoryService.findKnowledgeCategoriesByGradeId(gradeId);
            } else {
                log.warn("无法解析学生 {} 年级，回退为查询全部分类", studentId);
                categories = knowledgeCategoryService.findAllCategories();
            }

            if (categories == null || categories.isEmpty()) {
                return categoryProgressList;
            }

            for (KnowledgeCategory category : categories) {
                List<KnowledgePoint> knowledgePoints;
                if (gradeId != null) {
                    knowledgePoints = knowledgePointService.findKnowledgePointsByGradeAndCategory(gradeId, category.getId());
                } else {
                    knowledgePoints = knowledgePointService.findKnowledgePointsByCategoryId(category.getId());
                }
                if (knowledgePoints == null || knowledgePoints.isEmpty()) {
                    continue;
                }

                int totalKnowledgePoints = knowledgePoints.size();
                if (totalKnowledgePoints <= 0) {
                    continue;
                }
                int completedCount = 0;
                int inProgressCount = 0;
                int notStartedCount = 0;
                int easyCount = 0;
                int mediumCount = 0;
                int hardCount = 0;
                // 各知识点（小类）完成度之和
                BigDecimal knowledgePointProgressSum = BigDecimal.ZERO;

                for (KnowledgePoint kp : knowledgePoints) {
                    LearningProgress progress = progressByKpId.get(kp.getId());

                    BigDecimal pointPct = BigDecimal.ZERO;
                    if (progress != null && progress.getCompletionPercentage() != null) {
                        pointPct = progress.getCompletionPercentage();
                    }
                    knowledgePointProgressSum = knowledgePointProgressSum.add(pointPct);

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
                    }

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
                            default:
                                break;
                        }
                    }
                }

                BigDecimal overallProgress = BigDecimal.ZERO;
                if (totalKnowledgePoints > 0) {
                    overallProgress = knowledgePointProgressSum.divide(
                            BigDecimal.valueOf(totalKnowledgePoints), 2, RoundingMode.HALF_UP);
                }

                StudentDetailDTO.CategoryLearningProgress categoryProgress = new StudentDetailDTO.CategoryLearningProgress();
                categoryProgress.setCategoryId(category.getId());
                categoryProgress.setCategoryName(category.getCategoryName());
                categoryProgress.setCategoryNameFr(category.getCategoryNameFr());
                categoryProgress.setTotalKnowledgePoints(totalKnowledgePoints);
                categoryProgress.setCompletedKnowledgePoints(completedCount);
                categoryProgress.setInProgressKnowledgePoints(inProgressCount);
                categoryProgress.setNotStartedKnowledgePoints(notStartedCount);
                categoryProgress.setKnowledgePointProgressSum(knowledgePointProgressSum.setScale(2, RoundingMode.HALF_UP));
                categoryProgress.setOverallProgress(overallProgress);
                categoryProgress.setEasyCount(easyCount);
                categoryProgress.setMediumCount(mediumCount);
                categoryProgress.setHardCount(hardCount);

                categoryProgressList.add(categoryProgress);
            }

            // 按 overallProgress 降序（进度高的在前）；null 视为 0 排在后面
            categoryProgressList.sort((a, b) -> {
                BigDecimal pa = a.getOverallProgress() != null ? a.getOverallProgress() : BigDecimal.ZERO;
                BigDecimal pb = b.getOverallProgress() != null ? b.getOverallProgress() : BigDecimal.ZERO;
                int cmp = pb.compareTo(pa);
                if (cmp != 0) {
                    return cmp;
                }
                Integer ida = a.getCategoryId();
                Integer idb = b.getCategoryId();
                if (ida != null && idb != null) {
                    return ida.compareTo(idb);
                }
                return 0;
            });

        } catch (Exception e) {
            log.error("获取知识点类型学习情况失败: {}", e.getMessage(), e);
        }

        return categoryProgressList;
    }

    /**
     * 解析年级字符串为年级等级（与课程分配逻辑一致）
     */
    private Integer parseGradeLevel(String gradeStr) {
        if (gradeStr == null || gradeStr.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(gradeStr.trim());
        } catch (NumberFormatException e) {
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
}
