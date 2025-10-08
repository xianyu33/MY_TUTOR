package com.yy.my_tutor.math.mapper;

import com.yy.my_tutor.math.domain.StudentAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生答题记录Mapper接口
 */
@Mapper
public interface StudentAnswerMapper {
    
    /**
     * 查询用户的答题记录
     */
    List<StudentAnswer> findStudentAnswersByUserId(@Param("userId") Integer userId);
    
    /**
     * 根据用户ID和问题ID查询答题记录
     */
    List<StudentAnswer> findStudentAnswersByUserAndQuestion(@Param("userId") Integer userId, @Param("questionId") Integer questionId);
    
    /**
     * 根据知识点ID查询答题记录
     */
    List<StudentAnswer> findStudentAnswersByKnowledgePointId(@Param("userId") Integer userId, @Param("knowledgePointId") Integer knowledgePointId);
    
    /**
     * 查询正确答题记录
     */
    List<StudentAnswer> findCorrectAnswersByUserId(@Param("userId") Integer userId);
    
    /**
     * 查询错误答题记录
     */
    List<StudentAnswer> findIncorrectAnswersByUserId(@Param("userId") Integer userId);
    
    /**
     * 插入答题记录
     */
    int insertStudentAnswer(StudentAnswer studentAnswer);
    
    /**
     * 更新答题记录
     */
    int updateStudentAnswer(StudentAnswer studentAnswer);
    
    /**
     * 删除答题记录（逻辑删除）
     */
    int deleteStudentAnswer(@Param("id") Integer id);
    
    /**
     * 统计用户答题情况
     */
    int countAnswersByUserAndKnowledge(@Param("userId") Integer userId, @Param("knowledgePointId") Integer knowledgePointId);
    
    /**
     * 统计用户正确答题数
     */
    int countCorrectAnswersByUserAndKnowledge(@Param("userId") Integer userId, @Param("knowledgePointId") Integer knowledgePointId);
}
