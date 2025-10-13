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
}


