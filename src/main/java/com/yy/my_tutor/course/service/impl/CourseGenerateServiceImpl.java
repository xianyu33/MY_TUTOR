package com.yy.my_tutor.course.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yy.my_tutor.ark.service.ArkAIService;
import com.yy.my_tutor.course.domain.Course;
import com.yy.my_tutor.course.domain.CourseContent;
import com.yy.my_tutor.course.domain.GenerateCourseRequest;
import com.yy.my_tutor.course.mapper.CourseContentMapper;
import com.yy.my_tutor.course.service.CourseGenerateService;
import com.yy.my_tutor.course.service.CourseService;
import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.math.service.KnowledgePointService;
import com.yy.my_tutor.test.domain.TestAnalysisReport;
import com.yy.my_tutor.test.mapper.TestAnalysisReportMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import javax.annotation.Resource;
import java.util.List;

/**
 * 课程生成服务实现类 - 四阶段学习模型
 *
 * Stage 1 Understand: explanation（概念定义、关键要素拆解、简单示例、适用边界）
 * Stage 2 Apply:      examples（典型使用场景、复杂案例、实现思路/使用步骤、常见错误）
 * Stage 3 Master:     key_summary（解题步骤拆解、对比分析、原理层理解、扩展应用）
 * Stage 4 Evaluate:   additional_info（场景判断题、方案选择与理由、错误案例诊断、学习总结与反思）
 */
@Slf4j
@Service
public class CourseGenerateServiceImpl implements CourseGenerateService {

    @Resource
    private KnowledgePointService knowledgePointService;

    @Resource
    private TestAnalysisReportMapper testAnalysisReportMapper;

    @Resource
    private ArkAIService arkAIService;

    @Resource
    private CourseService courseService;

    @Resource
    private CourseContentMapper courseContentMapper;

    @Resource
    private CourseTranslationService courseTranslationService;

    private static final int DIFFICULTY_EASY = 1;
    private static final int DIFFICULTY_MEDIUM = 2;
    private static final int DIFFICULTY_HARD = 3;

    private static final int MAX_STAGE = 4;

    @Override
    public Course generateCourse(GenerateCourseRequest request) {
        // 1. 参数校验
        if (request.getKnowledgePointId() == null) {
            throw new IllegalArgumentException("Knowledge point ID is required");
        }
        if (request.getStudentId() == null) {
            throw new IllegalArgumentException("Student ID is required");
        }

        String language = request.getLanguage();
        if (language == null || language.isEmpty()) {
            language = "en";
        }

        // 2. 查询知识点信息
        KnowledgePoint knowledgePoint = knowledgePointService.findKnowledgePointById(request.getKnowledgePointId());
        if (knowledgePoint == null) {
            throw new IllegalArgumentException("Knowledge point not found: " + request.getKnowledgePointId());
        }

        // 3. 确定难度级别
        Integer difficultyLevel = request.getDifficultyLevel();
        if (difficultyLevel == null) {
            difficultyLevel = determineDifficultyLevel(request.getStudentId(), request.getKnowledgePointId());
        }

        // 4. 查询该学生+知识点+难度是否已有课程记录
        Course existingCourse = courseService.findByStudentKnowledgePointAndDifficulty(
                request.getStudentId(), request.getKnowledgePointId(), difficultyLevel);

        if (existingCourse != null) {
            int currentStage = existingCourse.getCurrentStage() != null ? existingCourse.getCurrentStage() : 0;

            if (currentStage >= MAX_STAGE) {
                log.info("Course already fully generated, courseId: {}", existingCourse.getId());
                existingCourse.setKnowledgePoint(knowledgePoint);
                CourseContent content = courseContentMapper.findByCourseIdAndLanguage(existingCourse.getId(), language);
                existingCourse.setContent(content);
                return existingCourse;
            }

            // 校验用户是否已完成当前阶段
            int completedStage = existingCourse.getCompletedStage() != null ? existingCourse.getCompletedStage() : 0;
            if (completedStage < currentStage) {
                throw new IllegalStateException("请先完成当前阶段的学习（阶段 " + currentStage + "），再生成下一阶段");
            }

            // 生成下一阶段
            int nextStage = currentStage + 1;
            return generateNextStage(existingCourse, nextStage, knowledgePoint, difficultyLevel, language);
        } else {
            // 新课程，从 Stage 1 开始
            return generateNewCourse(request, knowledgePoint, difficultyLevel, language);
        }
    }

    @Override
    public Flux<String> generateCourseStream(GenerateCourseRequest request) {
        // 1. 参数校验
        if (request.getKnowledgePointId() == null) {
            return Flux.error(new IllegalArgumentException("Knowledge point ID is required"));
        }
        if (request.getStudentId() == null) {
            return Flux.error(new IllegalArgumentException("Student ID is required"));
        }

        String language = request.getLanguage();
        if (language == null || language.isEmpty()) {
            language = "en";
        }

        // 2. 查询知识点信息
        KnowledgePoint knowledgePoint = knowledgePointService.findKnowledgePointById(request.getKnowledgePointId());
        if (knowledgePoint == null) {
            return Flux.error(new IllegalArgumentException("Knowledge point not found: " + request.getKnowledgePointId()));
        }

        // 3. 确定难度级别
        Integer difficultyLevel = request.getDifficultyLevel();
        if (difficultyLevel == null) {
            difficultyLevel = determineDifficultyLevel(request.getStudentId(), request.getKnowledgePointId());
        }

        // 4. 查询该学生+知识点+难度是否已有课程记录
        Course existingCourse = courseService.findByStudentKnowledgePointAndDifficulty(
                request.getStudentId(), request.getKnowledgePointId(), difficultyLevel);

        int stage;
        boolean isNewCourse;

        if (existingCourse != null) {
            int currentStage = existingCourse.getCurrentStage() != null ? existingCourse.getCurrentStage() : 0;
            if (currentStage >= MAX_STAGE) {
                log.info("Course already fully generated, courseId: {}", existingCourse.getId());
                return Flux.just("[NO_DATA]");
            }

            // 校验用户是否已完成当前阶段
            int completedStage = existingCourse.getCompletedStage() != null ? existingCourse.getCompletedStage() : 0;
            if (completedStage < currentStage) {
                return Flux.just("[ERROR] 请先完成当前阶段的学习（阶段 " + currentStage + "），再生成下一阶段");
            }

            stage = currentStage + 1;
            isNewCourse = false;
        } else {
            stage = 1;
            isNewCourse = true;
        }

        // 5. 构建提示词
        String systemPrompt = buildSystemPrompt();
        String userPrompt = buildStagePrompt(stage, knowledgePoint, difficultyLevel, language);

        log.info("Streaming course generation, stage: {}, knowledgePoint: {}, language: {}", stage, knowledgePoint.getPointName(), language);

        // 6. 调用流式 AI
        StringBuilder fullResponse = new StringBuilder();

        // capture effectively final variables for lambda
        final int currentStage = stage;
        final boolean newCourse = isNewCourse;
        final String lang = language;
        final Integer difficulty = difficultyLevel;
        final KnowledgePoint kp = knowledgePoint;
        final Course existing = existingCourse;

        return Flux.create(sink -> {
            arkAIService.streamChat(systemPrompt, userPrompt)
                    .subscribe(
                            token -> {
                                fullResponse.append(token);
                                sink.next(token);
                            },
                            error -> {
                                log.error("Stream course generation failed", error);
                                sink.error(error);
                            },
                            () -> {
                                try {
                                    // 流结束，发送阶段完成标记
                                    sink.next("[STAGE_DONE]");

                                    // 累积完整文本直接存库
                                    String content = fullResponse.toString().trim();

                                    Course course;
                                    if (newCourse) {
                                        // 创建主课程记录
                                        course = new Course();
                                        course.setStudentId(request.getStudentId());
                                        course.setKnowledgePointId(request.getKnowledgePointId());
                                        course.setDifficultyLevel(difficulty);
                                        course.setCurrentStage(1);
                                        course.setCompletedStage(0);
                                        courseService.save(course);

                                        // 创建内容记录
                                        CourseContent courseContent = new CourseContent();
                                        courseContent.setCourseId(course.getId());
                                        courseContent.setLanguage(lang);
                                        courseContent.setCourseTitle(kp.getPointName());
                                        courseContent.setExplanation(content);
                                        courseContentMapper.insert(courseContent);
                                        course.setContent(courseContent);

                                        log.info("Stream: new course saved, id: {}, language: {}", course.getId(), lang);
                                    } else {
                                        // 更新主课程阶段
                                        Course updateCourse = new Course();
                                        updateCourse.setId(existing.getId());
                                        updateCourse.setCurrentStage(currentStage);
                                        courseService.update(updateCourse);

                                        // 更新或创建内容记录
                                        saveOrUpdateStageContent(existing.getId(), currentStage, content, lang);

                                        log.info("Stream: course updated, id: {}, stage: {}", existing.getId(), currentStage);
                                        course = courseService.findById(existing.getId());
                                        CourseContent courseContent = courseContentMapper.findByCourseIdAndLanguage(existing.getId(), lang);
                                        course.setContent(courseContent);
                                    }

                                    // 触发异步翻译
                                    String targetLanguage = "en".equals(lang) ? "fr" : "en";
                                    courseTranslationService.translateStageAsync(course, currentStage, content, targetLanguage, kp);

                                    // 发送最终 Course JSON
                                    course.setKnowledgePoint(kp);
                                    sink.next(JSON.toJSONString(course));
                                } catch (Exception e) {
                                    log.error("Stream: failed to process completed response", e);
                                    sink.next("[ERROR] " + e.getMessage());
                                } finally {
                                    sink.complete();
                                }
                            }
                    );
        }, FluxSink.OverflowStrategy.BUFFER);
    }

    /**
     * 创建新课程并生成 Stage 1（explanation）
     */
    private Course generateNewCourse(GenerateCourseRequest request, KnowledgePoint knowledgePoint,
                                     Integer difficultyLevel, String language) {
        String systemPrompt = buildSystemPrompt();
        String userPrompt = buildStagePrompt(1, knowledgePoint, difficultyLevel, language);

        log.info("Generating new course Stage 1, knowledgePoint: {}, difficulty: {}, language: {}",
                knowledgePoint.getPointName(), difficultyLevel, language);
        String aiResponse = arkAIService.chat(systemPrompt, userPrompt);

        // 创建主课程记录
        Course course = new Course();
        course.setStudentId(request.getStudentId());
        course.setKnowledgePointId(request.getKnowledgePointId());
        course.setDifficultyLevel(difficultyLevel);
        course.setCurrentStage(1);
        course.setCompletedStage(0);
        courseService.save(course);

        // 创建内容记录
        CourseContent courseContent = new CourseContent();
        courseContent.setCourseId(course.getId());
        courseContent.setLanguage(language);
        courseContent.setCourseTitle(knowledgePoint.getPointName());
        courseContent.setExplanation(aiResponse.trim());
        courseContentMapper.insert(courseContent);
        course.setContent(courseContent);

        log.info("New course saved, id: {}, language: {}, stage: 1", course.getId(), language);

        // 异步翻译 Stage 1 到另一种语言
        String targetLanguage = "en".equals(language) ? "fr" : "en";
        courseTranslationService.translateStageAsync(course, 1, aiResponse.trim(), targetLanguage, knowledgePoint);

        course.setKnowledgePoint(knowledgePoint);
        return course;
    }

    /**
     * 生成下一阶段内容并更新现有课程记录
     */
    private Course generateNextStage(Course existingCourse, int stage, KnowledgePoint knowledgePoint,
                                     Integer difficultyLevel, String language) {
        String systemPrompt = buildSystemPrompt();
        String userPrompt = buildStagePrompt(stage, knowledgePoint, difficultyLevel, language);

        log.info("Generating Stage {} for courseId: {}, language: {}", stage, existingCourse.getId(), language);
        String aiResponse = arkAIService.chat(systemPrompt, userPrompt);
        String content = aiResponse.trim();

        // 更新主课程阶段
        Course updateCourse = new Course();
        updateCourse.setId(existingCourse.getId());
        updateCourse.setCurrentStage(stage);
        courseService.update(updateCourse);

        // 更新或创建内容记录
        saveOrUpdateStageContent(existingCourse.getId(), stage, content, language);

        log.info("Course updated, id: {}, stage: {}", existingCourse.getId(), stage);

        // 异步翻译当前阶段到另一种语言
        String targetLanguage = "en".equals(language) ? "fr" : "en";
        Course updatedCourse = courseService.findById(existingCourse.getId());
        CourseContent courseContent = courseContentMapper.findByCourseIdAndLanguage(existingCourse.getId(), language);
        updatedCourse.setContent(courseContent);
        courseTranslationService.translateStageAsync(updatedCourse, stage, content, targetLanguage, knowledgePoint);

        updatedCourse.setKnowledgePoint(knowledgePoint);
        return updatedCourse;
    }

    /**
     * 保存或更新指定语言的阶段内容
     */
    private void saveOrUpdateStageContent(Integer courseId, int stage, String content, String language) {
        CourseContent existing = courseContentMapper.findByCourseIdAndLanguage(courseId, language);
        if (existing == null) {
            CourseContent courseContent = new CourseContent();
            courseContent.setCourseId(courseId);
            courseContent.setLanguage(language);
            setStageContent(courseContent, stage, content);
            courseContentMapper.insert(courseContent);
        } else {
            CourseContent updateContent = new CourseContent();
            updateContent.setId(existing.getId());
            setStageContent(updateContent, stage, content);
            courseContentMapper.update(updateContent);
        }
    }

    private void setStageContent(CourseContent courseContent, int stage, String content) {
        switch (stage) {
            case 1:
                courseContent.setExplanation(content);
                break;
            case 2:
                courseContent.setExamples(content);
                break;
            case 3:
                courseContent.setKeySummary(content);
                break;
            case 4:
                courseContent.setAdditionalInfo(content);
                break;
            default:
                throw new IllegalStateException("Invalid stage: " + stage);
        }
    }

    @Override
    public Integer determineDifficultyLevel(Integer studentId, Integer knowledgePointId) {
        List<TestAnalysisReport> reports = testAnalysisReportMapper.findReportsByStudentId(studentId);

        if (reports == null || reports.isEmpty()) {
            log.info("No test reports found for student: {}, using default difficulty: EASY", studentId);
            return DIFFICULTY_EASY;
        }

        String knowledgePointIdStr = String.valueOf(knowledgePointId);

        for (TestAnalysisReport report : reports) {
            if (containsKnowledgePoint(report.getWeakKnowledgePoints(), knowledgePointIdStr)) {
                log.info("KnowledgePoint {} is in weak points for student {}, difficulty: EASY", knowledgePointId, studentId);
                return DIFFICULTY_EASY;
            }
            if (containsKnowledgePoint(report.getNeedsImprovementPoints(), knowledgePointIdStr)) {
                log.info("KnowledgePoint {} needs improvement for student {}, difficulty: MEDIUM", knowledgePointId, studentId);
                return DIFFICULTY_MEDIUM;
            }
            if (containsKnowledgePoint(report.getStrongKnowledgePoints(), knowledgePointIdStr)) {
                log.info("KnowledgePoint {} is strong for student {}, difficulty: HARD", knowledgePointId, studentId);
                return DIFFICULTY_HARD;
            }
        }

        log.info("KnowledgePoint {} not found in any report for student {}, using default difficulty: EASY", knowledgePointId, studentId);
        return DIFFICULTY_EASY;
    }

    private boolean containsKnowledgePoint(String jsonArrayStr, String knowledgePointId) {
        if (jsonArrayStr == null || jsonArrayStr.isEmpty()) {
            return false;
        }
        try {
            JSONArray array = JSON.parseArray(jsonArrayStr);
            for (int i = 0; i < array.size(); i++) {
                if (knowledgePointId.equals(String.valueOf(array.get(i)))) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.warn("Failed to parse knowledge points JSON: {}", jsonArrayStr, e);
        }
        return false;
    }

    private String buildSystemPrompt() {
        return "You are an expert K-12 mathematics education content creator. Generate course content for the given knowledge point. " +
                "Respond with the content directly in Markdown format: use ## headings for sections, **bold** for key terms, " +
                "numbered lists for steps, and $ for inline math formulas. Do not wrap it in JSON or code blocks.";
    }

    private String buildStagePrompt(int stage, KnowledgePoint knowledgePoint, Integer difficultyLevel, String language) {
        String difficultyDesc = getDifficultyDescription(difficultyLevel);
        String languageName = "fr".equals(language) ? "French" : "English";

        switch (stage) {
            case 1:
                return buildStage1Prompt(knowledgePoint, difficultyDesc, languageName);
            case 2:
                return buildStage2Prompt(knowledgePoint, difficultyDesc, languageName);
            case 3:
                return buildStage3Prompt(knowledgePoint, difficultyDesc, languageName);
            case 4:
                return buildStage4Prompt(knowledgePoint, difficultyDesc, languageName);
            default:
                throw new IllegalArgumentException("Invalid stage: " + stage);
        }
    }

    private String buildStage1Prompt(KnowledgePoint kp, String difficulty, String languageName) {
        return "Stage 1 - Understand: Generate content to help the student deeply understand the following knowledge point, written in " + languageName + ".\n\n" +
                "Knowledge Point: " + kp.getPointName() + "\n" +
                "Description: " + (kp.getDescription() != null ? kp.getDescription() : "N/A") + "\n" +
                "Learning Objectives: " + (kp.getLearningObjectives() != null ? kp.getLearningObjectives() : "N/A") + "\n" +
                "Difficulty Level: " + difficulty + "\n\n" +
                "Your content MUST cover all four aspects:\n" +
                "1. Concept Definition: Clearly define the concept in precise, student-friendly language.\n" +
                "2. Key Elements: Break down the concept into its essential components or properties.\n" +
                "3. Simple Examples: Provide 1-2 simple, intuitive examples to illustrate the concept.\n" +
                "4. Applicable Boundaries: Explain when this concept applies and when it does NOT apply (edge cases, limitations).\n\n" +
                "Respond with the content directly in Markdown format.";
    }

    private String buildStage2Prompt(KnowledgePoint kp, String difficulty, String languageName) {
        return "Stage 2 - Apply: Generate application-focused content for the following knowledge point, written in " + languageName + ".\n\n" +
                "Knowledge Point: " + kp.getPointName() + "\n" +
                "Description: " + (kp.getDescription() != null ? kp.getDescription() : "N/A") + "\n" +
                "Difficulty Level: " + difficulty + "\n\n" +
                "Your content MUST cover all four aspects:\n" +
                "1. Typical Use Scenarios: Describe 2-3 common real-world or exam scenarios where this knowledge point is applied.\n" +
                "2. Complex Cases: Provide 1-2 challenging worked examples with detailed step-by-step solutions.\n" +
                "3. Implementation Steps: Outline a clear, reusable procedure or strategy for solving problems of this type.\n" +
                "4. Common Mistakes: List 2-3 frequent errors students make when applying this concept, with explanations of why they are wrong.\n\n" +
                "Respond with the content directly in Markdown format.";
    }

    private String buildStage3Prompt(KnowledgePoint kp, String difficulty, String languageName) {
        return "Stage 3 - Master: Generate mastery-level content for the following knowledge point, written in " + languageName + ".\n\n" +
                "Knowledge Point: " + kp.getPointName() + "\n" +
                "Description: " + (kp.getDescription() != null ? kp.getDescription() : "N/A") + "\n" +
                "Difficulty Level: " + difficulty + "\n\n" +
                "Your content MUST cover all four aspects:\n" +
                "1. Problem-Solving Steps: Break down the general approach to solving problems involving this concept into clear, numbered steps.\n" +
                "2. Comparative Analysis: Compare this concept with related or easily confused concepts, highlighting similarities and differences.\n" +
                "3. Deeper Principles: Explain the underlying mathematical principles or reasoning behind the concept (the \"why\" behind the rules).\n" +
                "4. Extended Applications: Show how this concept connects to more advanced topics or cross-disciplinary applications.\n\n" +
                "Respond with the content directly in Markdown format.";
    }

    private String buildStage4Prompt(KnowledgePoint kp, String difficulty, String languageName) {
        return "Stage 4 - Evaluate & Reflect: Generate evaluation and reflection content for the following knowledge point, written in " + languageName + ".\n\n" +
                "Knowledge Point: " + kp.getPointName() + "\n" +
                "Description: " + (kp.getDescription() != null ? kp.getDescription() : "N/A") + "\n" +
                "Difficulty Level: " + difficulty + "\n\n" +
                "Your content MUST cover all four aspects:\n" +
                "1. Scenario Judgment Questions: Provide 2-3 scenario-based questions where the student must judge whether this concept applies and explain why.\n" +
                "2. Solution Selection & Reasoning: Present a problem with multiple possible approaches and ask the student to choose the best one with justification.\n" +
                "3. Error Case Diagnosis: Show 1-2 incorrect solutions and ask the student to identify and explain the errors.\n" +
                "4. Learning Summary & Reflection: Provide guiding prompts that help the student summarize what they have learned and reflect on their understanding.\n\n" +
                "Respond with the content directly in Markdown format.";
    }

    private String getDifficultyDescription(Integer difficultyLevel) {
        switch (difficultyLevel) {
            case DIFFICULTY_EASY:
                return "Easy (Beginner)";
            case DIFFICULTY_MEDIUM:
                return "Medium (Intermediate)";
            case DIFFICULTY_HARD:
                return "Hard (Advanced)";
            default:
                return "Easy (Beginner)";
        }
    }
}
