package com.yy.my_tutor.course.service.impl;

import com.yy.my_tutor.ark.service.ArkAIService;
import com.yy.my_tutor.course.domain.Course;
import com.yy.my_tutor.course.domain.CourseContent;
import com.yy.my_tutor.course.mapper.CourseContentMapper;
import com.yy.my_tutor.math.domain.KnowledgePoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 课程翻译异步服务 - 按阶段翻译，写入 course_content 表
 */
@Slf4j
@Service
public class CourseTranslationService {

    @Resource
    private ArkAIService arkAIService;

    @Resource
    private CourseContentMapper courseContentMapper;

    /**
     * 异步翻译指定阶段的内容到目标语言
     *
     * @param course          课程主记录
     * @param stage           当前阶段 (1-4)
     * @param sourceContent   源内容文本
     * @param targetLanguage  目标语言 (en/fr)
     * @param knowledgePoint  知识点信息
     */
    @Async("courseTranslationExecutor")
    public void translateStageAsync(Course course, int stage, String sourceContent, String targetLanguage, KnowledgePoint knowledgePoint) {
        log.info("Starting async translation, courseId: {}, stage: {}, targetLanguage: {}", course.getId(), stage, targetLanguage);
        try {
            if (sourceContent == null || sourceContent.isEmpty()) {
                log.warn("No content to translate for stage {}, courseId: {}", stage, course.getId());
                return;
            }

            String targetLanguageName = "fr".equals(targetLanguage) ? "French" : "English";

            String systemPrompt = "You are an expert K-12 mathematics education content translator. " +
                    "Translate the given course content accurately and naturally into the target language. " +
                    "Keep the exact same formatting, line breaks, and structure. " +
                    "Respond with the translated content directly as plain text. Do not wrap it in JSON or code blocks.";

            String userPrompt = "Translate the following course content into " + targetLanguageName + ". " +
                    "The translation must be accurate and natural, keeping the content and formatting exactly the same, only changing the language.\n\n" +
                    "Original content:\n" + sourceContent;

            String translatedContent = arkAIService.chat(systemPrompt, userPrompt).trim();

            // 翻译 courseTitle（Stage 1 时）
            String translatedTitle = null;
            if (stage == 1 && knowledgePoint.getPointName() != null) {
                translatedTitle = arkAIService.chat(
                        "You are a translator. Translate the given text into " + targetLanguageName + ". Respond with the translation only, nothing else.",
                        knowledgePoint.getPointName()
                ).trim();
            }

            // 查找目标语言的内容记录
            CourseContent targetContent = courseContentMapper.findByCourseIdAndLanguage(course.getId(), targetLanguage);

            if (targetContent == null) {
                // 目标语言记录不存在，创建新记录
                targetContent = new CourseContent();
                targetContent.setCourseId(course.getId());
                targetContent.setLanguage(targetLanguage);
                if (translatedTitle != null) {
                    targetContent.setCourseTitle(translatedTitle);
                }
                setStageContent(targetContent, stage, translatedContent);
                courseContentMapper.insert(targetContent);
                log.info("Created new translated content, courseId: {}, language: {}, stage: {}", course.getId(), targetLanguage, stage);
            } else {
                // 目标语言记录已存在，更新对应阶段字段
                CourseContent updateContent = new CourseContent();
                updateContent.setId(targetContent.getId());
                if (translatedTitle != null) {
                    updateContent.setCourseTitle(translatedTitle);
                }
                setStageContent(updateContent, stage, translatedContent);
                courseContentMapper.update(updateContent);
                log.info("Updated translated content, courseId: {}, language: {}, stage: {}", course.getId(), targetLanguage, stage);
            }
        } catch (Exception e) {
            log.error("Async translation failed, courseId: {}, stage: {}, targetLanguage: {}",
                    course.getId(), stage, targetLanguage, e);
        }
    }

    private void setStageContent(CourseContent content, int stage, String translatedContent) {
        switch (stage) {
            case 1: content.setExplanation(translatedContent); break;
            case 2: content.setExamples(translatedContent); break;
            case 3: content.setKeySummary(translatedContent); break;
            case 4: content.setAdditionalInfo(translatedContent); break;
        }
    }
}
