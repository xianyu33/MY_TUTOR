package com.yy.my_tutor.test.service;

import com.yy.my_tutor.test.domain.GenerateAdaptiveTestRequest;
import com.yy.my_tutor.test.domain.TestWithQuestionsDTO;

/**
 * 自适应测验生成服务
 */
public interface AdaptiveTestService {

    /**
     * 根据学生掌握情况生成自适应混合难度测验
     *
     * @param request 自适应测验请求
     * @return 测验详情（包含题目列表）
     */
    TestWithQuestionsDTO generateAdaptiveTest(GenerateAdaptiveTestRequest request);

    /**
     * 根据单个知识点的掌握情况生成自适应测验
     *
     * @param request 自适应测验请求（需提供 knowledgePointId）
     * @return 测验详情（包含题目列表）
     */
    TestWithQuestionsDTO generateAdaptiveTestByKnowledgePoint(GenerateAdaptiveTestRequest request);
}
