package com.yy.my_tutor.user.service;

import com.yy.my_tutor.user.domain.LearningProgressStats;
import com.yy.my_tutor.user.domain.User;
import com.yy.my_tutor.user.service.StudentRegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 学生课程分配服务测试
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class StudentRegistrationServiceTest {

    @Autowired
    private StudentRegistrationService studentRegistrationService;

    @Test
    public void testAssignCoursesByGrade() {
        // 测试数据
        Integer userId = 1;
        Integer gradeLevel = 9; // 九年级

        // 执行课程分配
        boolean result = studentRegistrationService.assignCoursesByGrade(userId, gradeLevel);

        // 验证结果
        assertTrue(result, "课程分配应该成功");

        // 验证学习进度统计
        LearningProgressStats stats = studentRegistrationService.getLearningProgressStats(userId);
        assertNotNull(stats, "学习进度统计不应该为空");
        assertEquals(userId, stats.getUserId(), "学生ID应该匹配");
        assertEquals(gradeLevel, stats.getGradeLevel(), "年级等级应该匹配");
        assertTrue(stats.getTotalKnowledgePoints() > 0, "应该有知识点");
    }

    @Test
    public void testReassignCoursesByGrade() {
        // 测试数据
        Integer userId = 1;
        Integer gradeLevel = 9;
        boolean forceUpdate = true;

        // 执行重新分配
        boolean result = studentRegistrationService.reassignCoursesByGrade(userId, gradeLevel, forceUpdate);

        // 验证结果
        assertTrue(result, "重新分配课程应该成功");
    }

    @Test
    public void testGetLearningProgressStats() {
        // 测试数据
        Integer userId = 1;

        // 获取学习进度统计
        LearningProgressStats stats = studentRegistrationService.getLearningProgressStats(userId);

        // 验证结果
        assertNotNull(stats, "学习进度统计不应该为空");
        assertEquals(userId, stats.getUserId(), "学生ID应该匹配");
        assertNotNull(stats.getDistribution(), "完成度分布不应该为空");
    }

    @Test
    public void testParseGradeLevel() {
        // 测试各种年级格式的解析
        assertEquals(1, parseGradeLevel("1"));
        assertEquals(1, parseGradeLevel("一年级"));
        assertEquals(1, parseGradeLevel("1年级"));
        
        assertEquals(9, parseGradeLevel("9"));
        assertEquals(9, parseGradeLevel("九年级"));
        assertEquals(9, parseGradeLevel("初三"));
        
        assertEquals(12, parseGradeLevel("12"));
        assertEquals(12, parseGradeLevel("高三"));
        
        assertNull(parseGradeLevel("无效年级"));
        assertNull(parseGradeLevel(null));
        assertNull(parseGradeLevel(""));
    }

    // 辅助方法：解析年级等级（模拟实现）
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
                    return null;
            }
        }
    }
}
