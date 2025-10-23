package com.yy.my_tutor.util;

import com.yy.my_tutor.math.domain.KnowledgeCategory;
import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.math.domain.Question;
import com.yy.my_tutor.test.domain.StudentTestAnswer;
import com.yy.my_tutor.test.domain.StudentTestRecord;
import com.yy.my_tutor.test.domain.Test;

/**
 * 多语言工具类
 * 用于根据用户语言偏好返回相应的文本内容
 */
public class MultilingualUtil {
    
    /**
     * 支持的语言常量
     */
    public static final String LANGUAGE_ZH = "zh";
    public static final String LANGUAGE_FR = "fr";
    
    /**
     * 默认语言
     */
    public static final String DEFAULT_LANGUAGE = LANGUAGE_ZH;
    
    /**
     * 获取知识点分类名称
     * @param category 知识点分类对象
     * @param language 语言代码
     * @return 对应语言的分类名称
     */
    public static String getCategoryName(KnowledgeCategory category, String language) {
        if (category == null) {
            return null;
        }
        
        if (LANGUAGE_FR.equals(language) && category.getCategoryNameFr() != null && !category.getCategoryNameFr().trim().isEmpty()) {
            return category.getCategoryNameFr();
        }
        return category.getCategoryName();
    }
    
    /**
     * 获取知识点分类描述
     * @param category 知识点分类对象
     * @param language 语言代码
     * @return 对应语言的分类描述
     */
    public static String getCategoryDescription(KnowledgeCategory category, String language) {
        if (category == null) {
            return null;
        }
        
        if (LANGUAGE_FR.equals(language) && category.getDescriptionFr() != null && !category.getDescriptionFr().trim().isEmpty()) {
            return category.getDescriptionFr();
        }
        return category.getDescription();
    }
    
    /**
     * 获取知识点名称
     * @param knowledgePoint 知识点对象
     * @param language 语言代码
     * @return 对应语言的知识点名称
     */
    public static String getKnowledgePointName(KnowledgePoint knowledgePoint, String language) {
        if (knowledgePoint == null) {
            return null;
        }
        
        if (LANGUAGE_FR.equals(language) && knowledgePoint.getPointNameFr() != null && !knowledgePoint.getPointNameFr().trim().isEmpty()) {
            return knowledgePoint.getPointNameFr();
        }
        return knowledgePoint.getPointName();
    }
    
    /**
     * 获取知识点描述
     * @param knowledgePoint 知识点对象
     * @param language 语言代码
     * @return 对应语言的知识点描述
     */
    public static String getKnowledgePointDescription(KnowledgePoint knowledgePoint, String language) {
        if (knowledgePoint == null) {
            return null;
        }
        
        if (LANGUAGE_FR.equals(language) && knowledgePoint.getDescriptionFr() != null && !knowledgePoint.getDescriptionFr().trim().isEmpty()) {
            return knowledgePoint.getDescriptionFr();
        }
        return knowledgePoint.getDescription();
    }
    
    /**
     * 获取知识点内容
     * @param knowledgePoint 知识点对象
     * @param language 语言代码
     * @return 对应语言的知识点内容
     */
    public static String getKnowledgePointContent(KnowledgePoint knowledgePoint, String language) {
        if (knowledgePoint == null) {
            return null;
        }
        
        if (LANGUAGE_FR.equals(language) && knowledgePoint.getContentFr() != null && !knowledgePoint.getContentFr().trim().isEmpty()) {
            return knowledgePoint.getContentFr();
        }
        return knowledgePoint.getContent();
    }
    
    /**
     * 获取知识点学习目标
     * @param knowledgePoint 知识点对象
     * @param language 语言代码
     * @return 对应语言的学习目标
     */
    public static String getKnowledgePointLearningObjectives(KnowledgePoint knowledgePoint, String language) {
        if (knowledgePoint == null) {
            return null;
        }
        
        if (LANGUAGE_FR.equals(language) && knowledgePoint.getLearningObjectivesFr() != null && !knowledgePoint.getLearningObjectivesFr().trim().isEmpty()) {
            return knowledgePoint.getLearningObjectivesFr();
        }
        return knowledgePoint.getLearningObjectives();
    }
    
    /**
     * 获取题目标题
     * @param question 题目对象
     * @param language 语言代码
     * @return 对应语言的题目标题
     */
    public static String getQuestionTitle(Question question, String language) {
        if (question == null) {
            return null;
        }
        
        if (LANGUAGE_FR.equals(language) && question.getQuestionTitleFr() != null && !question.getQuestionTitleFr().trim().isEmpty()) {
            return question.getQuestionTitleFr();
        }
        return question.getQuestionTitle();
    }
    
    /**
     * 获取题目内容
     * @param question 题目对象
     * @param language 语言代码
     * @return 对应语言的题目内容
     */
    public static String getQuestionContent(Question question, String language) {
        if (question == null) {
            return null;
        }
        
        if (LANGUAGE_FR.equals(language) && question.getQuestionContentFr() != null && !question.getQuestionContentFr().trim().isEmpty()) {
            return question.getQuestionContentFr();
        }
        return question.getQuestionContent();
    }
    
    /**
     * 获取题目选项
     * @param question 题目对象
     * @param language 语言代码
     * @return 对应语言的题目选项
     */
    public static String getQuestionOptions(Question question, String language) {
        if (question == null) {
            return null;
        }
        
        if (LANGUAGE_FR.equals(language) && question.getOptionsFr() != null && !question.getOptionsFr().trim().isEmpty()) {
            return question.getOptionsFr();
        }
        return question.getOptions();
    }
    
    /**
     * 获取正确答案
     * @param question 题目对象
     * @param language 语言代码
     * @return 对应语言的正确答案
     */
    public static String getCorrectAnswer(Question question, String language) {
        if (question == null) {
            return null;
        }
        
        if (LANGUAGE_FR.equals(language) && question.getCorrectAnswerFr() != null && !question.getCorrectAnswerFr().trim().isEmpty()) {
            return question.getCorrectAnswerFr();
        }
        return question.getCorrectAnswer();
    }
    
    /**
     * 获取答案解释
     * @param question 题目对象
     * @param language 语言代码
     * @return 对应语言的答案解释
     */
    public static String getAnswerExplanation(Question question, String language) {
        if (question == null) {
            return null;
        }
        
        if (LANGUAGE_FR.equals(language) && question.getAnswerExplanationFr() != null && !question.getAnswerExplanationFr().trim().isEmpty()) {
            return question.getAnswerExplanationFr();
        }
        return question.getAnswerExplanation();
    }
    
    /**
     * 获取测试名称
     * @param test 测试对象
     * @param language 语言代码
     * @return 对应语言的测试名称
     */
    public static String getTestName(Test test, String language) {
        if (test == null) {
            return null;
        }
        
        if (LANGUAGE_FR.equals(language) && test.getTestNameFr() != null && !test.getTestNameFr().trim().isEmpty()) {
            return test.getTestNameFr();
        }
        return test.getTestName();
    }
    
    /**
     * 获取学生测试记录中的测试名称
     * @param record 学生测试记录对象
     * @param language 语言代码
     * @return 对应语言的测试名称
     */
    public static String getTestRecordName(StudentTestRecord record, String language) {
        if (record == null) {
            return null;
        }
        
        if (LANGUAGE_FR.equals(language) && record.getTestNameFr() != null && !record.getTestNameFr().trim().isEmpty()) {
            return record.getTestNameFr();
        }
        return record.getTestName();
    }
    
    /**
     * 获取学生答题中的题目内容
     * @param answer 学生答题对象
     * @param language 语言代码
     * @return 对应语言的题目内容
     */
    public static String getStudentAnswerQuestionContent(StudentTestAnswer answer, String language) {
        if (answer == null) {
            return null;
        }
        
        if (LANGUAGE_FR.equals(language) && answer.getQuestionContentFr() != null && !answer.getQuestionContentFr().trim().isEmpty()) {
            return answer.getQuestionContentFr();
        }
        return answer.getQuestionContent();
    }
    
    /**
     * 获取学生答题中的正确答案
     * @param answer 学生答题对象
     * @param language 语言代码
     * @return 对应语言的正确答案
     */
    public static String getStudentAnswerCorrectAnswer(StudentTestAnswer answer, String language) {
        if (answer == null) {
            return null;
        }
        
        if (LANGUAGE_FR.equals(language) && answer.getCorrectAnswerFr() != null && !answer.getCorrectAnswerFr().trim().isEmpty()) {
            return answer.getCorrectAnswerFr();
        }
        return answer.getCorrectAnswer();
    }
    
    /**
     * 获取学生答题中的学生答案
     * @param answer 学生答题对象
     * @param language 语言代码
     * @return 对应语言的学生答案
     */
    public static String getStudentAnswerStudentAnswer(StudentTestAnswer answer, String language) {
        if (answer == null) {
            return null;
        }
        
        if (LANGUAGE_FR.equals(language) && answer.getStudentAnswerFr() != null && !answer.getStudentAnswerFr().trim().isEmpty()) {
            return answer.getStudentAnswerFr();
        }
        return answer.getStudentAnswer();
    }
    
    /**
     * 验证语言代码是否有效
     * @param language 语言代码
     * @return 是否有效
     */
    public static boolean isValidLanguage(String language) {
        return LANGUAGE_ZH.equals(language) || LANGUAGE_FR.equals(language);
    }
    
    /**
     * 获取默认语言
     * @param language 输入的语言代码
     * @return 有效的语言代码，如果无效则返回默认语言
     */
    public static String getValidLanguage(String language) {
        return isValidLanguage(language) ? language : DEFAULT_LANGUAGE;
    }
}
