package com.yy.my_tutor.test.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yy.my_tutor.ark.service.ArkAIService;
import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.math.domain.Question;
import com.yy.my_tutor.math.mapper.QuestionMapper;
import com.yy.my_tutor.test.service.QuestionGenerateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 题目生成服务实现类
 */
@Slf4j
@Service
public class QuestionGenerateServiceImpl implements QuestionGenerateService {

    @Resource
    private ArkAIService arkAIService;

    @Resource
    private QuestionMapper questionMapper;

    @Override
    public List<Question> generateQuestions(KnowledgePoint knowledgePoint, int count,
                                            Integer difficultyLevel, Integer questionType) {
        // 1. 构建 Prompt
        String prompt = buildQuestionPrompt(knowledgePoint, count, difficultyLevel, questionType);
        String systemPrompt = buildSystemPrompt();

        log.info("Generating {} questions for knowledgePoint: {}, difficulty: {}, type: {}",
                count, knowledgePoint.getPointName(), difficultyLevel, questionType);

        // 2. 调用 AI 生成
        String aiResponse = arkAIService.chat(systemPrompt, prompt);

        // 3. 解析响应
        List<Question> questions = parseAIResponse(aiResponse, knowledgePoint, difficultyLevel, prompt);

        log.info("Generated {} questions successfully", questions.size());
        return questions;
    }

    @Override
    public List<Question> generateAndSaveQuestions(KnowledgePoint knowledgePoint, int count,
                                                   Integer difficultyLevel, Integer questionType) {
        // 1. 生成题目
        List<Question> questions = generateQuestions(knowledgePoint, count, difficultyLevel, questionType);

        // 2. 保存到数据库
        for (Question question : questions) {
            questionMapper.insertQuestion(question);
            log.info("Saved question to database, id: {}", question.getId());
        }

        return questions;
    }

    /**
     * 构建系统提示词
     */
    private String buildSystemPrompt() {
        return "You are an expert K-12 mathematics educator and question designer. " +
                "Your task is to generate high-quality math questions based on the given knowledge point. " +
                "You must respond in valid JSON format only, with no additional text before or after the JSON. " +
                "The JSON must be properly formatted and parseable. " +
                "Generate questions that are pedagogically sound, age-appropriate, and test genuine understanding.";
    }

    /**
     * 构建用户提示词
     */
    private String buildQuestionPrompt(KnowledgePoint knowledgePoint, int count,
                                       Integer difficultyLevel, Integer questionType) {
        String difficultyDesc = getDifficultyDescription(difficultyLevel);
        String typeDesc = getQuestionTypeDescription(questionType);

        StringBuilder prompt = new StringBuilder();
        prompt.append("Please generate ").append(count).append(" math questions for the following knowledge point:\n\n");
        prompt.append("Knowledge Point Name: ").append(knowledgePoint.getPointName()).append("\n");
        if (knowledgePoint.getPointNameFr() != null) {
            prompt.append("Knowledge Point Name (French): ").append(knowledgePoint.getPointNameFr()).append("\n");
        }
        prompt.append("Description: ").append(knowledgePoint.getDescription() != null ? knowledgePoint.getDescription() : "N/A").append("\n");
        prompt.append("Learning Objectives: ").append(knowledgePoint.getLearningObjectives() != null ? knowledgePoint.getLearningObjectives() : "N/A").append("\n");
        prompt.append("Difficulty Level: ").append(difficultyDesc).append("\n");
        if (typeDesc != null) {
            prompt.append("Question Type: ").append(typeDesc).append("\n");
        }
        prompt.append("\n");

        prompt.append("Please generate the questions in the following JSON format:\n");
        prompt.append("{\n");
        prompt.append("  \"questions\": [\n");
        prompt.append("    {\n");
        prompt.append("      \"questionType\": 1,  // 1-multiple choice, 2-multiple select, 3-fill blank, 4-calculation\n");
        prompt.append("      \"questionTitle\": \"Brief title in English\",\n");
        prompt.append("      \"questionTitleFr\": \"Brief title in French\",\n");
        prompt.append("      \"questionContent\": \"Full question text in English\",\n");
        prompt.append("      \"questionContentFr\": \"Full question text in French\",\n");
        prompt.append("      \"options\": \"[\\\"A. option1\\\", \\\"B. option2\\\", \\\"C. option3\\\", \\\"D. option4\\\"]\",  // JSON array string for multiple choice, null for others\n");
        prompt.append("      \"optionsFr\": \"[\\\"A. option1\\\", \\\"B. option2\\\", \\\"C. option3\\\", \\\"D. option4\\\"]\",  // French version\n");
        prompt.append("      \"correctAnswer\": \"B\",  // The correct answer (letter for choice, value for fill/calc)\n");
        prompt.append("      \"correctAnswerFr\": \"B\",  // French version if different\n");
        prompt.append("      \"answerExplanation\": \"Detailed step-by-step explanation in English\",\n");
        prompt.append("      \"answerExplanationFr\": \"Detailed explanation in French\",\n");
        prompt.append("      \"difficultyLevel\": ").append(difficultyLevel != null ? difficultyLevel : 1).append(",\n");
        prompt.append("      \"points\": 1\n");
        prompt.append("    }\n");
        prompt.append("  ]\n");
        prompt.append("}\n\n");

        prompt.append("Important requirements:\n");
        prompt.append("1. Generate exactly ").append(count).append(" questions\n");
        prompt.append("2. All questions must be appropriate for ").append(difficultyDesc).append(" difficulty level\n");
        prompt.append("3. Each question must have both English and French versions\n");
        prompt.append("4. For multiple choice questions, always provide 4 options (A, B, C, D)\n");
        prompt.append("5. Include detailed, step-by-step explanations\n");
        prompt.append("6. Questions should be varied and test different aspects of the knowledge point\n");
        prompt.append("7. Ensure mathematical accuracy in all calculations\n");
        prompt.append("8. Return ONLY valid JSON, no additional text\n");

        return prompt.toString();
    }

    /**
     * 获取难度级别描述
     */
    private String getDifficultyDescription(Integer difficultyLevel) {
        if (difficultyLevel == null) {
            return "Easy (beginner)";
        }
        switch (difficultyLevel) {
            case 1:
                return "Easy (beginner)";
            case 2:
                return "Medium (intermediate)";
            case 3:
                return "Hard (advanced)";
            default:
                return "Easy (beginner)";
        }
    }

    /**
     * 获取题目类型描述
     */
    private String getQuestionTypeDescription(Integer questionType) {
        if (questionType == null) {
            return null;
        }
        switch (questionType) {
            case 1:
                return "Multiple Choice (single answer)";
            case 2:
                return "Multiple Select (multiple answers)";
            case 3:
                return "Fill in the Blank";
            case 4:
                return "Calculation/Problem Solving";
            default:
                return null;
        }
    }

    /**
     * 解析 AI 响应并构建 Question 列表
     */
    private List<Question> parseAIResponse(String aiResponse, KnowledgePoint knowledgePoint,
                                           Integer difficultyLevel, String promptUsed) {
        List<Question> questions = new ArrayList<>();

        try {
            // 提取 JSON 内容
            String jsonContent = extractJsonContent(aiResponse);
            log.debug("Extracted JSON content: {}", jsonContent);

            JSONObject json = JSON.parseObject(jsonContent);
            JSONArray questionsArray = json.getJSONArray("questions");

            if (questionsArray == null || questionsArray.isEmpty()) {
                log.warn("No questions found in AI response");
                return questions;
            }

            for (int i = 0; i < questionsArray.size(); i++) {
                JSONObject qObj = questionsArray.getJSONObject(i);
                Question question = new Question();

                // 设置关联信息
                question.setKnowledgePointId(knowledgePoint.getId());
                question.setGenerationSource("AI");
                question.setModelId(arkAIService.getDefaultModel());
                question.setPromptUsed(promptUsed);

                // 解析题目内容
                question.setQuestionType(qObj.getInteger("questionType"));
                question.setQuestionTitle(getJsonString(qObj, "questionTitle"));
                question.setQuestionTitleFr(getJsonString(qObj, "questionTitleFr"));
                question.setQuestionContent(getJsonString(qObj, "questionContent"));
                question.setQuestionContentFr(getJsonString(qObj, "questionContentFr"));
                question.setOptions(getJsonString(qObj, "options"));
                question.setOptionsFr(getJsonString(qObj, "optionsFr"));
                question.setCorrectAnswer(getJsonString(qObj, "correctAnswer"));
                question.setCorrectAnswerFr(getJsonString(qObj, "correctAnswerFr"));
                question.setAnswerExplanation(getJsonString(qObj, "answerExplanation"));
                question.setAnswerExplanationFr(getJsonString(qObj, "answerExplanationFr"));

                // 设置难度和分值
                Integer qDifficulty = qObj.getInteger("difficultyLevel");
                question.setDifficultyLevel(qDifficulty != null ? qDifficulty : difficultyLevel);
                Integer points = qObj.getInteger("points");
                question.setPoints(points != null ? points : 1);
                question.setSortOrder(i + 1);

                questions.add(question);
            }

            log.info("Parsed {} questions from AI response", questions.size());

        } catch (Exception e) {
            log.error("Failed to parse AI response: {}", e.getMessage());
            log.error("Raw AI response (first 2000 chars): {}",
                    aiResponse != null && aiResponse.length() > 2000 ? aiResponse.substring(0, 2000) : aiResponse);
        }

        return questions;
    }

    /**
     * 从 AI 响应中提取 JSON 内容
     */
    private String extractJsonContent(String response) {
        if (response == null || response.isEmpty()) {
            throw new IllegalArgumentException("AI response is empty");
        }

        String content = response.trim();

        // 处理 Markdown 代码块格式
        if (content.contains("```")) {
            content = content.replaceAll("```json\\s*", "");
            content = content.replaceAll("```\\s*", "");
            content = content.trim();
        }

        // 查找第一个 { 和最后一个 }
        int start = content.indexOf('{');
        int end = content.lastIndexOf('}');

        if (start != -1 && end != -1 && end > start) {
            String jsonStr = content.substring(start, end + 1);
            return cleanJsonString(jsonStr);
        }

        return content;
    }

    /**
     * 清理 JSON 字符串
     */
    private String cleanJsonString(String jsonStr) {
        if (jsonStr == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        boolean inString = false;

        for (int i = 0; i < jsonStr.length(); i++) {
            char c = jsonStr.charAt(i);

            if (!inString) {
                result.append(c);
                if (c == '"') {
                    inString = true;
                }
                continue;
            }

            // In string
            if (c == '\\') {
                // Check next char
                if (i + 1 < jsonStr.length()) {
                    char next = jsonStr.charAt(i + 1);
                    if (isValidEscape(next)) {
                        // Valid escape (e.g. \" or \\).
                        // Output `\`
                        result.append('\\');
                        // Output `next`
                        result.append(next);
                        // Skip next char in loop
                        i++;
                    } else {
                        // Invalid escape. Escape the backslash.
                        result.append("\\\\");
                        // Do NOT skip next char. It will be processed normally.
                    }
                } else {
                    // Backslash at end of string
                    result.append("\\\\");
                }
            } else if (c == '"') {
                // Closing quote
                inString = false;
                result.append(c);
            } else if (c == '\n') {
                result.append("\\n");
            } else if (c == '\r') {
                result.append("\\r");
            } else if (c == '\t') {
                result.append("\\t");
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    private boolean isValidEscape(char c) {
        return c == '"' || c == '\\' || c == '/' ||
               c == 'b' || c == 'f' || c == 'n' ||
               c == 'r' || c == 't' || c == 'u';
    }

    /**
     * 安全获取 JSON 字符串值
     */
    private String getJsonString(JSONObject json, String key) {
        Object value = json.get(key);
        return value != null ? value.toString() : null;
    }
}
