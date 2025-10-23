package com.yy.my_tutor.test.mapper;

import com.yy.my_tutor.test.domain.TestQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 测试题目关联Mapper接口
 */
@Mapper
public interface TestQuestionMapper {
    
    /**
     * 根据测试ID查询题目关联
     */
    List<TestQuestion> findTestQuestionsByTestId(@Param("testId") Integer testId);
    
    /**
     * 插入测试题目关联
     */
    int insertTestQuestion(TestQuestion testQuestion);
    
    /**
     * 删除测试的所有题目关联
     */
    int deleteTestQuestionsByTestId(@Param("testId") Integer testId);
    
    /**
     * 根据测试ID和题目ID查询关联
     */
    TestQuestion findTestQuestionByTestAndQuestion(@Param("testId") Integer testId, @Param("questionId") Integer questionId);
}
