package com.yy.my_tutor.test.domain;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 难度分配工具类 — 将百分比转换为各难度的实际题数
 */
public class DifficultyDistribution {

    private final Map<Integer, Integer> percentages; // key=难度(1/2/3), value=百分比

    public DifficultyDistribution(Map<Integer, Integer> percentages) {
        this.percentages = percentages;
    }

    /**
     * 将百分比分配转换为实际题数，确保总和等于 totalQuestions
     *
     * @param totalQuestions 总题数
     * @return key=难度级别, value=实际题数
     */
    public Map<Integer, Integer> computeCounts(int totalQuestions) {
        Map<Integer, Integer> counts = new LinkedHashMap<>();
        int assigned = 0;
        Integer lastKey = null;

        for (Map.Entry<Integer, Integer> entry : percentages.entrySet()) {
            int count = (int) Math.round(totalQuestions * entry.getValue() / 100.0);
            counts.put(entry.getKey(), count);
            assigned += count;
            lastKey = entry.getKey();
        }

        // 由于四舍五入可能导致总数偏差，将差额加到最后一个难度上
        if (lastKey != null && assigned != totalQuestions) {
            counts.put(lastKey, counts.get(lastKey) + (totalQuestions - assigned));
        }

        // 去掉题数为0的项
        counts.entrySet().removeIf(e -> e.getValue() <= 0);

        return counts;
    }

    /**
     * 将百分比Map转换为JSON字符串用于持久化
     */
    public String toJson() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<Integer, Integer> entry : percentages.entrySet()) {
            if (!first) sb.append(",");
            sb.append("\"").append(entry.getKey()).append("\":").append(entry.getValue());
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
}
