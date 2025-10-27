package com.yy.my_tutor.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 批量答题请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchAnswerRequest implements Serializable {
    
    /**
     * 测试记录ID
     */
    private Integer testRecordId;
    
    /**
     * 学生ID
     */
    private Integer studentId;
    
    /**
     * 答题列表
     */
    private List<AnswerItem> answers;
    
    /**
     * 答题项目
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerItem implements Serializable {
        /**
         * 题目ID
         */
        private Integer questionId;
        
        /**
         * 学生答案
         */
        private String studentAnswer;
        
        /**
         * 答题用时（秒）
         */
        private Integer timeSpent;
    }
}

