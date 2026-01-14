package com.yy.my_tutor.test.service;

import com.yy.my_tutor.test.domain.GenerateAITestRequest;
import com.yy.my_tutor.test.domain.TestWithQuestionsDTO;

/**
 * AI 测试生成服务接口
 * 负责整合题目生成、题库管理和测试创建的完整流程
 */
public interface AITestGenerateService {

    /**
     * 根据单个知识点生成 AI 测试
     * 流程：检查题库 → 不足则AI生成补充 → 创建测试 → 返回测试详情
     *
     * @param request 生成请求
     * @return 测试详情（包含题目列表）
     */
    TestWithQuestionsDTO generateTestBySingleKnowledgePoint(GenerateAITestRequest request);

    /**
     * 根据知识类型生成 AI 测试
     * 流程：查询分类下所有知识点 → 检查题库 → 不足则AI生成补充 → 创建测试 → 返回测试详情
     *
     * @param request 生成请求（需包含 categoryId 和 difficultyLevel）
     * @return 测试详情（包含题目列表）
     */
    TestWithQuestionsDTO generateTestByCategory(GenerateAITestRequest request);
}
