package com.yy.my_tutor.math.mapper;

import com.yy.my_tutor.math.domain.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 问题Mapper接口
 */
@Mapper
public interface QuestionMapper {
    
    /**
     * 查询所有问题
     */
    List<Question> findAllQuestions();
    
    /**
     * 根据ID查询问题
     */
    Question findQuestionById(@Param("id") Integer id);
    
    /**
     * 根据知识点ID查询问题
     */
    List<Question> findQuestionsByKnowledgePointId(@Param("knowledgePointId") Integer knowledgePointId);
    
    /**
     * 根据题目类型查询问题
     */
    List<Question> findQuestionsByType(@Param("questionType") Integer questionType);
    
    /**
     * 根据难度等级查询问题
     */
    List<Question> findQuestionsByDifficulty(@Param("difficultyLevel") Integer difficultyLevel);
    
    /**
     * 根据知识点和难度查询问题
     */
    List<Question> findQuestionsByKnowledgeAndDifficulty(@Param("knowledgePointId") Integer knowledgePointId, @Param("difficultyLevel") Integer difficultyLevel);
    
    /**
     * 随机获取指定数量的题目
     */
    List<Question> findRandomQuestions(@Param("knowledgePointId") Integer knowledgePointId, @Param("limit") Integer limit);
    
    /**
     * 根据年级和难度随机获取指定数量的题目
     */
    List<Question> findRandomQuestionsByGradeAndDifficulty(@Param("gradeId") Integer gradeId, 
                                                          @Param("difficultyLevel") Integer difficultyLevel, 
                                                          @Param("limit") Integer limit);
    
    /**
     * 插入问题
     */
    int insertQuestion(Question question);
    
    /**
     * 更新问题
     */
    int updateQuestion(Question question);
    
    /**
     * 删除问题（逻辑删除）
     */
    int deleteQuestion(@Param("id") Integer id);
}
