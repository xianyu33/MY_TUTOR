package com.yy.my_tutor.test.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 自适应策略工具类 — 提取分桶和难度计算逻辑，供 AdaptiveTestServiceImpl 和 QuestionPoolFillJob 共用
 */
public class AdaptiveStrategyHelper {

    private AdaptiveStrategyHelper() {}

    /**
     * 知识点分桶结果
     */
    public static class BucketResult {
        private final List<Integer> weakKpIds;
        private final List<Integer> improvingKpIds;
        private final List<Integer> masteredKpIds;

        public BucketResult(List<Integer> weakKpIds, List<Integer> improvingKpIds, List<Integer> masteredKpIds) {
            this.weakKpIds = weakKpIds;
            this.improvingKpIds = improvingKpIds;
            this.masteredKpIds = masteredKpIds;
        }

        public List<Integer> getWeakKpIds() { return weakKpIds; }
        public List<Integer> getImprovingKpIds() { return improvingKpIds; }
        public List<Integer> getMasteredKpIds() { return masteredKpIds; }
    }

    /**
     * 将知识点按掌握程度分桶（薄弱优先策略）
     *
     * @param allKpIds   所有知识点ID
     * @param masteryMap 知识点掌握数据 (kpId -> KnowledgeMastery)
     * @return 分桶结果
     */
    public static BucketResult bucketKnowledgePoints(List<Integer> allKpIds,
                                                      Map<Integer, KnowledgeMastery> masteryMap) {
        List<Integer> weakKpIds = new ArrayList<>();
        List<Integer> improvingKpIds = new ArrayList<>();
        List<Integer> masteredKpIds = new ArrayList<>();

        for (Integer kpId : allKpIds) {
            KnowledgeMastery m = masteryMap.get(kpId);
            if (m == null || m.getAccuracyRate() == null) {
                weakKpIds.add(kpId);
            } else if (m.getAccuracyRate().compareTo(new BigDecimal(50)) < 0) {
                weakKpIds.add(kpId);
            } else if (m.getAccuracyRate().compareTo(new BigDecimal(80)) < 0) {
                improvingKpIds.add(kpId);
            } else {
                masteredKpIds.add(kpId);
            }
        }

        return new BucketResult(weakKpIds, improvingKpIds, masteredKpIds);
    }

    /**
     * 根据掌握数据自动计算难度分配百分比
     *
     * @param masteryMap 知识点掌握数据
     * @param kpIds      知识点ID列表
     * @return key=难度级别(1/2/3), value=百分比
     */
    public static Map<Integer, Integer> computeAdaptiveDifficultyDistribution(
            Map<Integer, KnowledgeMastery> masteryMap, List<Integer> kpIds) {

        BigDecimal totalWeightedAccuracy = BigDecimal.ZERO;
        int totalWeight = 0;

        for (Integer kpId : kpIds) {
            KnowledgeMastery m = masteryMap.get(kpId);
            if (m != null && m.getAccuracyRate() != null && m.getTotalQuestions() != null && m.getTotalQuestions() > 0) {
                totalWeightedAccuracy = totalWeightedAccuracy.add(
                        m.getAccuracyRate().multiply(new BigDecimal(m.getTotalQuestions())));
                totalWeight += m.getTotalQuestions();
            }
        }

        BigDecimal overallAccuracy = BigDecimal.ZERO;
        if (totalWeight > 0) {
            overallAccuracy = totalWeightedAccuracy.divide(new BigDecimal(totalWeight), 2, RoundingMode.HALF_UP);
        }

        Map<Integer, Integer> result = new LinkedHashMap<>();
        if (overallAccuracy.compareTo(new BigDecimal(40)) < 0) {
            result.put(1, 50); result.put(2, 35); result.put(3, 15);
        } else if (overallAccuracy.compareTo(new BigDecimal(60)) < 0) {
            result.put(1, 35); result.put(2, 40); result.put(3, 25);
        } else if (overallAccuracy.compareTo(new BigDecimal(80)) < 0) {
            result.put(1, 20); result.put(2, 45); result.put(3, 35);
        } else {
            result.put(1, 10); result.put(2, 35); result.put(3, 55);
        }

        return result;
    }
}
