package com.yy.my_tutor.test.service;

import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.math.domain.Question;

import java.util.List;

/**
 * 题目生成服务接口
 * 负责通过 AI 生成题目
 */
public interface QuestionGenerateService {

    /**
     * 根据知识点 AI 生成题目
     *
     * @param knowledgePoint  知识点
     * @param count           生成数量
     * @param difficultyLevel 难度等级（1-简单，2-中等，3-困难）
     * @param questionType    题目类型（可选，null表示混合类型）
     * @return 生成的题目列表
     */
    List<Question> generateQuestions(KnowledgePoint knowledgePoint, int count,
                                     Integer difficultyLevel, Integer questionType);

    /**
     * 根据知识点 AI 生成题目并保存到题库
     *
     * @param knowledgePoint  知识点
     * @param count           生成数量
     * @param difficultyLevel 难度等级
     * @param questionType    题目类型
     * @return 生成并保存后的题目列表（带ID）
     */
    List<Question> generateAndSaveQuestions(KnowledgePoint knowledgePoint, int count,
                                            Integer difficultyLevel, Integer questionType);
}
