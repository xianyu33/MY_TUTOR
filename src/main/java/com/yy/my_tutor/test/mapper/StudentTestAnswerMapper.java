package com.yy.my_tutor.test.mapper;

import com.yy.my_tutor.test.domain.StudentTestAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生测试答题详情Mapper接口
 */
@Mapper
public interface StudentTestAnswerMapper {
    
    /**
     * 查询测试记录的答题详情
     */
    List<StudentTestAnswer> findAnswersByTestRecordId(@Param("testRecordId") Integer testRecordId);
    
    /**
     * 根据学生ID和题目ID查询答题记录
     */
    StudentTestAnswer findAnswerByStudentAndQuestion(@Param("studentId") Integer studentId, @Param("questionId") Integer questionId);
    
    /**
     * 插入答题详情
     */
    int insertTestAnswer(StudentTestAnswer testAnswer);
    
    /**
     * 更新答题详情
     */
    int updateTestAnswer(StudentTestAnswer testAnswer);
    
    /**
     * 批量插入答题详情
     */
    int batchInsertTestAnswers(@Param("answers") List<StudentTestAnswer> answers);
    
    /**
     * 删除答题详情（逻辑删除）
     */
    int deleteTestAnswer(@Param("id") Integer id);
    
    /**
     * 根据测试记录ID和题目ID查询答题记录
     */
    StudentTestAnswer findAnswerByRecordAndQuestion(@Param("testRecordId") Integer testRecordId, @Param("questionId") Integer questionId);
    
    /**
     * 插入答题详情（别名方法）
     */
    int insertAnswer(StudentTestAnswer testAnswer);
    
    /**
     * 更新答题详情（别名方法）
     */
    int updateAnswer(StudentTestAnswer testAnswer);
}


