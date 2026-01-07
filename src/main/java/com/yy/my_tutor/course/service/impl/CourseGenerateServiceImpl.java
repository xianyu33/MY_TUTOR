package com.yy.my_tutor.course.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yy.my_tutor.ark.service.ArkAIService;
import com.yy.my_tutor.course.domain.Course;
import com.yy.my_tutor.course.domain.GenerateCourseRequest;
import com.yy.my_tutor.course.service.CourseGenerateService;
import com.yy.my_tutor.course.service.CourseService;
import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.math.service.KnowledgePointService;
import com.yy.my_tutor.test.domain.TestAnalysisReport;
import com.yy.my_tutor.test.mapper.TestAnalysisReportMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 课程生成服务实现类
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

    /**
     * 难度级别常量
     */
    private static final int DIFFICULTY_EASY = 1;
    private static final int DIFFICULTY_MEDIUM = 2;
    private static final int DIFFICULTY_HARD = 3;

    @Override
    public Course generateCourse(GenerateCourseRequest request) {
        // 1. 参数校验
        if (request.getKnowledgePointId() == null) {
            throw new IllegalArgumentException("知识点ID不能为空");
        }
        if (request.getStudentId() == null) {
            throw new IllegalArgumentException("学生ID不能为空");
        }

        // 2. 查询知识点信息
        KnowledgePoint knowledgePoint = knowledgePointService.findKnowledgePointById(request.getKnowledgePointId());
        if (knowledgePoint == null) {
            throw new IllegalArgumentException("知识点不存在: " + request.getKnowledgePointId());
        }

        // 3. 确定难度级别
        Integer difficultyLevel = request.getDifficultyLevel();
        if (difficultyLevel == null) {
            difficultyLevel = determineDifficultyLevel(request.getStudentId(), request.getKnowledgePointId());
        }

        // 4. 构建 Prompt 并调用 AI 生成课程（每次都重新生成，保留历史记录）
        String prompt = buildPrompt(knowledgePoint, difficultyLevel, request.getLanguage());
        String systemPrompt = buildSystemPrompt();

        log.info("Generating course for knowledgePoint: {}, difficulty: {}", knowledgePoint.getPointName(), difficultyLevel);
        String aiResponse = arkAIService.chat(systemPrompt, prompt);

        // 5. 解析 AI 响应并构建 Course 对象
        Course course = parseAIResponse(aiResponse, request, knowledgePoint, difficultyLevel);
        course.setPromptUsed(prompt);
        course.setGenerationSource("AI");
        course.setModelId(arkAIService.getDefaultModel());

        // 6. 保存到数据库
        courseService.save(course);
        log.info("Course generated and saved successfully, id: {}", course.getId());

        return course;
    }

    @Override
    public Integer determineDifficultyLevel(Integer studentId, Integer knowledgePointId) {
        // 查询学生的测验分析报告
        List<TestAnalysisReport> reports = testAnalysisReportMapper.findReportsByStudentId(studentId);

        if (reports == null || reports.isEmpty()) {
            log.info("No test reports found for student: {}, using default difficulty: EASY", studentId);
            return DIFFICULTY_EASY;
        }

        String knowledgePointIdStr = String.valueOf(knowledgePointId);

        // 遍历报告，查找该知识点的掌握情况
        for (TestAnalysisReport report : reports) {
            // 检查是否在薄弱知识点中
            if (containsKnowledgePoint(report.getWeakKnowledgePoints(), knowledgePointIdStr)) {
                log.info("KnowledgePoint {} is in weak points for student {}, difficulty: EASY", knowledgePointId, studentId);
                return DIFFICULTY_EASY;
            }

            // 检查是否在需要提高的知识点中
            if (containsKnowledgePoint(report.getNeedsImprovementPoints(), knowledgePointIdStr)) {
                log.info("KnowledgePoint {} needs improvement for student {}, difficulty: MEDIUM", knowledgePointId, studentId);
                return DIFFICULTY_MEDIUM;
            }

            // 检查是否在掌握好的知识点中
            if (containsKnowledgePoint(report.getStrongKnowledgePoints(), knowledgePointIdStr)) {
                log.info("KnowledgePoint {} is strong for student {}, difficulty: HARD", knowledgePointId, studentId);
                return DIFFICULTY_HARD;
            }
        }

        // 如果没有找到相关记录，默认返回简单难度
        log.info("KnowledgePoint {} not found in any report for student {}, using default difficulty: EASY", knowledgePointId, studentId);
        return DIFFICULTY_EASY;
    }

    /**
     * 检查 JSON 数组字符串中是否包含指定的知识点ID
     */
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

    /**
     * 构建系统提示词
     */
    private String buildSystemPrompt() {
        return "You are an expert educational content creator specializing in K-12 mathematics education. " +
                "Your task is to generate comprehensive course content based on the given knowledge point. " +
                "You must respond in valid JSON format only, with no additional text before or after the JSON. " +
                "The JSON must be properly formatted and parseable.";
    }

    /**
     * 构建用户提示词
     */
    private String buildPrompt(KnowledgePoint knowledgePoint, Integer difficultyLevel, String language) {
        String difficultyDesc = getDifficultyDescription(difficultyLevel);

        StringBuilder prompt = new StringBuilder();
        prompt.append("Please generate a comprehensive course for the following knowledge point:\n\n");
        prompt.append("Knowledge Point Name: ").append(knowledgePoint.getPointName()).append("\n");
        if (knowledgePoint.getPointNameFr() != null) {
            prompt.append("Knowledge Point Name (French): ").append(knowledgePoint.getPointNameFr()).append("\n");
        }
        prompt.append("Description: ").append(knowledgePoint.getDescription() != null ? knowledgePoint.getDescription() : "N/A").append("\n");
        prompt.append("Learning Objectives: ").append(knowledgePoint.getLearningObjectives() != null ? knowledgePoint.getLearningObjectives() : "N/A").append("\n");
        prompt.append("Difficulty Level: ").append(difficultyDesc).append("\n\n");

        prompt.append("Please generate the course content in the following JSON format:\n");
        prompt.append("{\n");
        prompt.append("  \"courseTitle\": \"Course title in English\",\n");
        prompt.append("  \"courseTitleFr\": \"Course title in French\",\n");
        prompt.append("  \"explanation\": \"Detailed explanation of the knowledge point in English, suitable for ").append(difficultyDesc).append(" level\",\n");
        prompt.append("  \"explanationFr\": \"Detailed explanation in French\",\n");
        prompt.append("  \"examples\": \"2-3 worked examples with step-by-step solutions in English\",\n");
        prompt.append("  \"examplesFr\": \"Same examples in French\",\n");
        prompt.append("  \"keySummary\": \"Key points and formulas to remember in English\",\n");
        prompt.append("  \"keySummaryFr\": \"Key summary in French\",\n");
        prompt.append("  \"additionalInfo\": \"Tips, common mistakes to avoid, and connections to other topics in English\",\n");
        prompt.append("  \"additionalInfoFr\": \"Additional info in French\"\n");
        prompt.append("}\n\n");

        prompt.append("Important requirements:\n");
        prompt.append("1. Content must be appropriate for the ").append(difficultyDesc).append(" difficulty level\n");
        prompt.append("2. Use clear, student-friendly language\n");
        prompt.append("3. Include practical examples relevant to real-world applications\n");
        prompt.append("4. Ensure French translations are accurate and natural\n");
        prompt.append("5. Return ONLY valid JSON, no additional text\n");

        return prompt.toString();
    }

    /**
     * 获取难度级别描述
     */
    private String getDifficultyDescription(Integer difficultyLevel) {
        switch (difficultyLevel) {
            case DIFFICULTY_EASY:
                return "Easy (beginner)";
            case DIFFICULTY_MEDIUM:
                return "Medium (intermediate)";
            case DIFFICULTY_HARD:
                return "Hard (advanced)";
            default:
                return "Easy (beginner)";
        }
    }

    /**
     * 解析 AI 响应并构建 Course 对象
     */
    private Course parseAIResponse(String aiResponse, GenerateCourseRequest request,
                                   KnowledgePoint knowledgePoint, Integer difficultyLevel) {
        Course course = new Course();
        course.setStudentId(request.getStudentId());
        course.setKnowledgePointId(request.getKnowledgePointId());
        course.setDifficultyLevel(difficultyLevel);
        course.setKnowledgePoint(knowledgePoint);

        try {
            // 尝试提取 JSON 内容（处理可能的前后文字）
            String jsonContent = extractJsonContent(aiResponse);
            log.info("Extracted JSON content length: {}", jsonContent.length());
            log.debug("Extracted JSON content: {}", jsonContent);

            JSONObject json = JSON.parseObject(jsonContent);
            log.info("Parsed JSON keys: {}", json.keySet());

            course.setCourseTitle(getJsonStringMultiKey(json, "courseTitle", "course_title", "title"));
            course.setCourseTitleFr(getJsonStringMultiKey(json, "courseTitleFr", "course_title_fr", "titleFr", "title_fr"));
            course.setExplanation(getJsonStringMultiKey(json, "explanation", "explanation_en", "content"));
            course.setExplanationFr(getJsonStringMultiKey(json, "explanationFr", "explanation_fr"));
            course.setExamples(getJsonStringMultiKey(json, "examples", "examples_en", "example", "worked_examples"));
            course.setExamplesFr(getJsonStringMultiKey(json, "examplesFr", "examples_fr", "exampleFr", "example_fr"));
            course.setKeySummary(getJsonStringMultiKey(json, "keySummary", "key_summary", "summary", "keyPoints", "key_points"));
            course.setKeySummaryFr(getJsonStringMultiKey(json, "keySummaryFr", "key_summary_fr", "summaryFr", "summary_fr"));
            course.setAdditionalInfo(getJsonStringMultiKey(json, "additionalInfo", "additional_info", "tips", "additional"));
            course.setAdditionalInfoFr(getJsonStringMultiKey(json, "additionalInfoFr", "additional_info_fr", "tipsFr", "tips_fr"));

            // 日志输出解析结果
            log.info("Parsed course - title: {}, explanation length: {}, examples length: {}, keySummary length: {}, additionalInfo length: {}",
                    course.getCourseTitle(),
                    course.getExplanation() != null ? course.getExplanation().length() : 0,
                    course.getExamples() != null ? course.getExamples().length() : 0,
                    course.getKeySummary() != null ? course.getKeySummary().length() : 0,
                    course.getAdditionalInfo() != null ? course.getAdditionalInfo().length() : 0);

        } catch (Exception e) {
            log.error("Failed to parse AI response as JSON. Error: {}", e.getMessage());
            log.error("Raw AI response (first 2000 chars): {}",
                    aiResponse != null && aiResponse.length() > 2000 ? aiResponse.substring(0, 2000) : aiResponse);
            // 如果解析失败，将原始响应存入 explanation 字段
            course.setCourseTitle("Course for " + knowledgePoint.getPointName());
            course.setCourseTitleFr("Cours pour " + (knowledgePoint.getPointNameFr() != null ? knowledgePoint.getPointNameFr() : knowledgePoint.getPointName()));
            course.setExplanation(aiResponse);
            course.setExplanationFr(aiResponse);
        }

        return course;
    }

    /**
     * 尝试多个可能的 key 获取 JSON 字符串值
     */
    private String getJsonStringMultiKey(JSONObject json, String... keys) {
        for (String key : keys) {
            Object value = json.get(key);
            if (value != null) {
                String strValue = value.toString();
                if (!strValue.isEmpty()) {
                    return strValue;
                }
            }
        }
        return null;
    }

    /**
     * 安全获取 JSON 字符串值
     */
    private String getJsonString(JSONObject json, String key) {
        Object value = json.get(key);
        return value != null ? value.toString() : null;
    }

    /**
     * 从 AI 响应中提取 JSON 内容
     */
    private String extractJsonContent(String response) {
        if (response == null || response.isEmpty()) {
            throw new IllegalArgumentException("AI response is empty");
        }

        String content = response.trim();

        // 处理 Markdown 代码块格式 ```json ... ``` 或 ``` ... ```
        if (content.contains("```")) {
            // 移除 ```json 或 ``` 开头
            content = content.replaceAll("```json\\s*", "");
            content = content.replaceAll("```\\s*", "");
            content = content.trim();
        }

        // 查找第一个 { 和最后一个 }
        int start = content.indexOf('{');
        int end = content.lastIndexOf('}');

        if (start != -1 && end != -1 && end > start) {
            String jsonStr = content.substring(start, end + 1);
            // 清理可能导致解析问题的字符
            jsonStr = cleanJsonString(jsonStr);
            return jsonStr;
        }

        // 如果没有找到 JSON 格式，返回原始内容
        return content;
    }

    /**
     * 清理 JSON 字符串中可能导致解析问题的字符
     * 特别处理 LaTeX 公式中的反斜杠
     */
    private String cleanJsonString(String jsonStr) {
        if (jsonStr == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        boolean inString = false;

        for (int i = 0; i < jsonStr.length(); i++) {
            char c = jsonStr.charAt(i);

            if (c == '"' && (i == 0 || jsonStr.charAt(i - 1) != '\\')) {
                inString = !inString;
                result.append(c);
                continue;
            }

            if (inString) {
                if (c == '\\') {
                    // 检查下一个字符，判断是否是有效的 JSON 转义序列
                    if (i + 1 < jsonStr.length()) {
                        char next = jsonStr.charAt(i + 1);
                        // 有效的 JSON 转义序列: \", \\, \/, \b, \f, \n, \r, \t, \\uXXXX
                        if (next == '"' || next == '\\' || next == '/' ||
                            next == 'b' || next == 'f' || next == 'n' ||
                            next == 'r' || next == 't' || next == 'u') {
                            // 有效转义序列，保持不变
                            result.append(c);
                        } else {
                            // 无效转义序列（如 LaTeX 的 \( \) \log 等），需要双重转义
                            result.append("\\\\");
                        }
                    } else {
                        result.append("\\\\");
                    }
                } else if (c == '\n') {
                    result.append("\\n");
                } else if (c == '\r') {
                    result.append("\\r");
                } else if (c == '\t') {
                    result.append("\\t");
                } else {
                    result.append(c);
                }
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }
}
