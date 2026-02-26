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
     * 根据年级、分类ID和难度筛选符合条件的题目（不限制数量，用于获取题目池）
     */
    List<Question> findQuestionsByGradeCategoryAndDifficulty(@Param("gradeId") Integer gradeId,
                                                             @Param("categoryIds") List<Integer> categoryIds,
                                                             @Param("difficultyLevel") Integer difficultyLevel);
    
    /**
     * 根据知识点ID列表和难度筛选符合条件的题目（不限制数量，用于获取题目池）
     */
    List<Question> findQuestionsByKnowledgePointIdsAndDifficulty(@Param("knowledgePointIds") List<Integer> knowledgePointIds,
                                                                  @Param("difficultyLevel") Integer difficultyLevel);
    
    /**
     * 根据年级、知识类型（分类ID）和难度等级查询题目
     */
    List<Question> findQuestionsByGradeCategoryAndDifficultyLevel(@Param("gradeId") Integer gradeId,
                                                                   @Param("categoryId") Integer categoryId,
                                                                   @Param("difficultyLevel") Integer difficultyLevel);
    
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

    /**
     * 查询学生未做过的题目（根据知识点和难度）
     *
     * @param knowledgePointId 知识点ID
     * @param difficultyLevel  难度等级（可选）
     * @param doneQuestionIds  学生已做过的题目ID列表
     * @return 未做过的题目列表
     */
    List<Question> findUndoneQuestionsByKnowledgePoint(@Param("knowledgePointId") Integer knowledgePointId,
                                                       @Param("difficultyLevel") Integer difficultyLevel,
                                                       @Param("doneQuestionIds") List<Integer> doneQuestionIds);

    /**
     * 查询学生未做过的题目（根据多个知识点和难度）
     *
     * @param knowledgePointIds 知识点ID列表
     * @param difficultyLevel   难度等级（可选）
     * @param doneQuestionIds   学生已做过的题目ID列表
     * @return 未做过的题目列表
     */
    List<Question> findUndoneQuestionsByKnowledgePoints(@Param("knowledgePointIds") List<Integer> knowledgePointIds,
                                                        @Param("difficultyLevel") Integer difficultyLevel,
                                                        @Param("doneQuestionIds") List<Integer> doneQuestionIds);

    /**
     * 统计某知识点+难度下的题目总数
     *
     * @param knowledgePointId 知识点ID
     * @param difficultyLevel  难度等级
     * @return 题目总数
     */
    int countByKnowledgePointAndDifficulty(@Param("knowledgePointId") Integer knowledgePointId,
                                           @Param("difficultyLevel") Integer difficultyLevel);

    /**
     * 查询已做过但间隔最久的题目（按最近做题时间升序排列）
     * 用于题库耗尽时的兜底复用
     *
     * @param knowledgePointIds 知识点ID列表
     * @param difficultyLevel   难度等级（可选）
     * @param doneQuestionIds   已做过的题目ID列表
     * @param limit             返回数量限制
     * @return 间隔最久的已做题目列表
     */
    List<Question> findLeastRecentlyDoneQuestions(@Param("knowledgePointIds") List<Integer> knowledgePointIds,
                                                  @Param("difficultyLevel") Integer difficultyLevel,
                                                  @Param("doneQuestionIds") List<Integer> doneQuestionIds,
                                                  @Param("studentId") Integer studentId,
                                                  @Param("limit") int limit);
}
