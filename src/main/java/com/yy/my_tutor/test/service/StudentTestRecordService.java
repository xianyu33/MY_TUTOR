package com.yy.my_tutor.test.service;

import com.yy.my_tutor.test.domain.StudentTestRecord;

import java.util.List;

/**
 * 学生测试记录服务接口
 */
public interface StudentTestRecordService {
    
    /**
     * 查询学生的测试记录
     */
    List<StudentTestRecord> findTestRecordsByStudentId(Integer studentId);
    
    /**
     * 根据测试ID查询记录
     */
    List<StudentTestRecord> findTestRecordsByTestId(Integer testId);
    
    /**
     * 根据学生ID和测试ID查询记录
     */
    StudentTestRecord findTestRecordByStudentAndTest(Integer studentId, Integer testId);
    
    /**
     * 查询进行中的测试记录
     */
    List<StudentTestRecord> findOngoingTestRecords(Integer studentId);
    
    /**
     * 开始测试
     */
    StudentTestRecord startTest(Integer studentId, Integer testId);
    
    /**
     * 提交测试
     */
    StudentTestRecord submitTest(Integer testRecordId);
    
    /**
     * 更新测试记录
     */
    StudentTestRecord updateTestRecord(StudentTestRecord testRecord);
    
    /**
     * 删除测试记录
     */
    boolean deleteTestRecord(Integer id);
    
    /**
     * 根据学生ID和知识点ID列表查询测试记录
     * 查询包含指定知识点的测试记录
     * @param studentId 学生ID
     * @param knowledgePointIds 知识点ID列表
     * @return 测试记录列表
     */
    List<StudentTestRecord> findTestRecordsByStudentIdAndKnowledgePointIds(Integer studentId, List<Integer> knowledgePointIds);
    
    /**
     * 根据学生ID、知识类型和难度等级查询测试记录
     * @param studentId 学生ID
     * @param categoryId 知识类型ID（分类ID）
     * @param difficultyLevel 知识点难度等级（1-简单，2-中等，3-困难）
     * @return 测试记录列表
     */
    List<StudentTestRecord> findTestRecordsByCategoryAndDifficulty(Integer studentId, Integer categoryId, Integer difficultyLevel);
}


