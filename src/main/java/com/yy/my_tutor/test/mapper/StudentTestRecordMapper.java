package com.yy.my_tutor.test.mapper;

import com.yy.my_tutor.test.domain.StudentTestRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生测试记录Mapper接口
 */
@Mapper
public interface StudentTestRecordMapper {
    
    /**
     * 查询学生的测试记录
     */
    List<StudentTestRecord> findTestRecordsByStudentId(@Param("studentId") Integer studentId);
    
    /**
     * 根据测试ID查询记录
     */
    List<StudentTestRecord> findTestRecordsByTestId(@Param("testId") Integer testId);
    
    /**
     * 根据学生ID和测试ID查询记录
     */
    StudentTestRecord findTestRecordByStudentAndTest(@Param("studentId") Integer studentId, @Param("testId") Integer testId);
    
    /**
     * 查询进行中的测试记录
     */
    List<StudentTestRecord> findOngoingTestRecords(@Param("studentId") Integer studentId);
    
    /**
     * 插入测试记录
     */
    int insertTestRecord(StudentTestRecord testRecord);
    
    /**
     * 更新测试记录
     */
    int updateTestRecord(StudentTestRecord testRecord);
    
    /**
     * 删除测试记录（逻辑删除）
     */
    int deleteTestRecord(@Param("id") Integer id);
    
    /**
     * 根据ID查询测试记录
     */
    StudentTestRecord findTestRecordById(@Param("id") Integer id);
    
    /**
     * 分页查询学生的测试记录
     */
    List<StudentTestRecord> findTestRecordsByStudentIdWithPagination(@Param("studentId") Integer studentId, 
                                                                     @Param("offset") Integer offset, 
                                                                     @Param("limit") Integer limit);
}


