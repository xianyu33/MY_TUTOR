package com.yy.my_tutor.test.service;

import com.yy.my_tutor.test.domain.StudentTestRecord;
import com.yy.my_tutor.test.domain.Test;
import com.yy.my_tutor.test.domain.StudentTestAnswer;

import java.util.List;
import java.util.Map;

/**
 * 学生测试服务接口
 */
public interface StudentTestService {
    
    /**
     * 为学生生成随机测试
     * @param studentId 学生ID
     * @param gradeId 年级ID
     * @param difficultyLevel 难度等级
     * @param questionCount 题目数量
     * @return 测试记录
     */
    StudentTestRecord generateRandomTestForStudent(Integer studentId, Integer gradeId, 
                                                   Integer difficultyLevel, Integer questionCount);
    
    /**
     * 开始测试
     * @param studentId 学生ID
     * @param testId 测试ID
     * @return 测试记录
     */
    StudentTestRecord startTest(Integer studentId, Integer testId);
    
    /**
     * 提交答案
     * @param testRecordId 测试记录ID
     * @param questionId 题目ID
     * @param studentAnswer 学生答案
     * @return 答题记录
     */
    StudentTestAnswer submitAnswer(Integer testRecordId, Integer questionId, String studentAnswer);
    
    /**
     * 完成测试
     * @param testRecordId 测试记录ID
     * @return 完成的测试记录
     */
    StudentTestRecord completeTest(Integer testRecordId);
    
    /**
     * 查询学生的测试历史记录
     * @param studentId 学生ID
     * @param page 页码
     * @param size 每页大小
     * @return 测试记录列表
     */
    List<StudentTestRecord> getStudentTestHistory(Integer studentId, Integer page, Integer size);
    
    /**
     * 查询测试的详细答题情况
     * @param testRecordId 测试记录ID
     * @return 答题详情列表
     */
    List<StudentTestAnswer> getTestAnswerDetails(Integer testRecordId);
    
    /**
     * 获取学生测试统计报表
     * @param studentId 学生ID
     * @param gradeId 年级ID（可选）
     * @return 统计报表数据
     */
    Map<String, Object> getStudentTestStatistics(Integer studentId, Integer gradeId);
    
    /**
     * 获取知识点掌握情况
     * @param studentId 学生ID
     * @return 知识点掌握情况列表
     */
    List<Map<String, Object>> getKnowledgeMastery(Integer studentId);
    
    /**
     * 获取正在进行的测试
     * @param studentId 学生ID
     * @return 正在进行的测试记录
     */
    List<StudentTestRecord> getOngoingTests(Integer studentId);
    
    /**
     * 为学生生成随机测试（支持难度均匀分配和知识点/分类筛选）
     * @param studentId 学生ID
     * @param gradeId 年级ID
     * @param knowledgePointIds 知识点ID列表（可选，优先使用）
     * @param categoryIds 知识点分类ID列表（可选，当knowledgePointIds为空时使用）
     * @param questionCount 题目数量
     * @param equalDistribution 是否均匀分配难度（简单、中等、困难）
     * @return 测试记录
     */
    StudentTestRecord generateRandomTestWithDistribution(Integer studentId, Integer gradeId, 
                                                       List<Integer> knowledgePointIds,
                                                       List<Integer> categoryIds, 
                                                       Integer questionCount, 
                                                       boolean equalDistribution);
    
    /**
     * 获取测试详情（包含题目列表）
     * @param testRecordId 测试记录ID
     * @return 测试详情（包含题目列表）
     */
    com.yy.my_tutor.test.domain.TestWithQuestionsDTO getTestWithQuestions(Integer testRecordId);
    
    /**
     * 批量提交答案并生成分析报告
     * @param request 批量答题请求
     * @return 测试分析结果（包含总得分、各知识点得分和分析）
     */
    com.yy.my_tutor.test.domain.TestAnalysisResult batchSubmitAnswersAndAnalyze(com.yy.my_tutor.test.domain.BatchAnswerRequest request);
}
