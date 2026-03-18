package com.yy.my_tutor.test.service;

import com.yy.my_tutor.math.domain.Question;

import java.util.List;
import java.util.Map;

/**
 * 题目来源服务 — 统一的取题逻辑（题库优先 → 复用最久未做 → AI生成兜底）
 */
public interface QuestionSourceService {

    /**
     * 获取题目：优先从题库取学生未做过的，不足时复用间隔最久的已做题，仍不足则AI生成
     *
     * @param studentId         学生ID
     * @param knowledgePointIds 知识点ID列表
     * @param count             需要的题目数量
     * @param difficultyLevel   难度级别（1=简单 2=中等 3=困难）
     * @param saveToBank        AI生成的题目是否保存到题库
     * @return 题目列表
     */
    List<Question> getQuestions(Integer studentId, List<Integer> knowledgePointIds,
                               int count, Integer difficultyLevel, Boolean saveToBank);

    /**
     * 批量取题：一次查询多个难度，减少 DB 查询和 AI 兜底触发
     *
     * @param studentId          学生ID
     * @param knowledgePointIds  知识点ID列表
     * @param difficultyCountMap key=难度级别, value=需要的题目数量
     * @param saveToBank         AI生成的题目是否保存到题库
     * @return key=难度级别, value=题目列表
     */
    Map<Integer, List<Question>> getQuestionsByDifficulties(
            Integer studentId, List<Integer> knowledgePointIds,
            Map<Integer, Integer> difficultyCountMap, Boolean saveToBank);
}
