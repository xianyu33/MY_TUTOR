package com.yy.my_tutor.test.service.impl;

import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.math.domain.Question;
import com.yy.my_tutor.math.service.GradeService;
import com.yy.my_tutor.math.service.KnowledgePointService;
import com.yy.my_tutor.math.mapper.QuestionMapper;
import com.yy.my_tutor.test.domain.BatchAnswerRequest;
import com.yy.my_tutor.test.domain.StudentTestAnswer;
import com.yy.my_tutor.test.domain.StudentTestRecord;
import com.yy.my_tutor.test.domain.Test;
import com.yy.my_tutor.test.domain.TestAnalysisReport;
import com.yy.my_tutor.test.domain.TestAnalysisResult;
import com.yy.my_tutor.test.domain.TestQuestion;
import com.yy.my_tutor.test.domain.TestQuestionDetail;
import com.yy.my_tutor.test.domain.TestWithQuestionsDTO;
import com.yy.my_tutor.test.mapper.StudentTestAnswerMapper;
import com.yy.my_tutor.test.mapper.StudentTestRecordMapper;
import com.yy.my_tutor.test.mapper.TestAnalysisReportMapper;
import com.yy.my_tutor.test.mapper.TestMapper;
import com.yy.my_tutor.test.mapper.TestQuestionMapper;
import com.yy.my_tutor.test.service.StudentTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 学生测试服务实现类
 */
@Slf4j
@Service
public class StudentTestServiceImpl implements StudentTestService {

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private TestQuestionMapper testQuestionMapper;

    @Autowired
    private StudentTestRecordMapper studentTestRecordMapper;

    @Autowired
    private StudentTestAnswerMapper studentTestAnswerMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private KnowledgePointService knowledgePointService;

    @Autowired
    private TestAnalysisReportMapper testAnalysisReportMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentTestRecord generateRandomTestForStudent(Integer studentId, Integer gradeId,
                                                          Integer difficultyLevel, Integer questionCount) {
        try {
            // 1. 根据年级和难度随机获取题目
            List<Question> questions = questionMapper.findRandomQuestionsByGradeAndDifficulty(gradeId, difficultyLevel, questionCount);

            if (questions == null || questions.isEmpty()) {
                log.warn("没有找到符合条件的题目，年级ID: {}, 难度: {}", gradeId, difficultyLevel);
                return null;
            }

            // 2. 创建测试
            Test test = new Test();
            test.setTestName("随机测试_" + new Date().getTime());
            test.setGradeId(gradeId);
            test.setDifficultyLevel(difficultyLevel);
            test.setTotalQuestions(questions.size());
            test.setTotalPoints(questions.size()); // 默认每题1分
            test.setTimeLimit(60); // 默认60分钟
            test.setTestType(1); // 练习测试
            test.setStatus(1); // 启用
            test.setCreateAt(new Date());
            test.setUpdateAt(new Date());
            test.setDeleteFlag("N");

            int testResult = testMapper.insertTest(test);
            if (testResult <= 0) {
                log.error("创建测试失败");
                return null;
            }

            // 3. 创建测试题目关联
            for (int i = 0; i < questions.size(); i++) {
                TestQuestion testQuestion = new TestQuestion();
                testQuestion.setTestId(test.getId());
                testQuestion.setQuestionId(questions.get(i).getId());
                testQuestion.setSortOrder(i + 1);
                testQuestion.setPoints(1);
                testQuestion.setCreateAt(new Date());

                testQuestionMapper.insertTestQuestion(testQuestion);
            }

            // 4. 创建学生测试记录
            StudentTestRecord record = new StudentTestRecord();
            record.setStudentId(studentId);
            record.setTestId(test.getId());
            record.setTestName(test.getTestName());
            record.setStartTime(new Date());
            record.setTotalQuestions(questions.size());
            record.setTotalPoints(questions.size());
            record.setTestStatus(1); // 进行中
            record.setCreateAt(new Date());
            record.setUpdateAt(new Date());
            record.setDeleteFlag("N");

            int recordResult = studentTestRecordMapper.insertTestRecord(record);
            if (recordResult <= 0) {
                log.error("创建测试记录失败");
                return null;
            }

            log.info("为学生 {} 生成随机测试成功，测试ID: {}, 记录ID: {}", studentId, test.getId(), record.getId());
            return record;

        } catch (Exception e) {
            log.error("生成随机测试时发生异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public StudentTestRecord startTest(Integer studentId, Integer testId) {
        // 检查是否已有进行中的测试记录
        StudentTestRecord existingRecord = studentTestRecordMapper.findTestRecordByStudentAndTest(studentId, testId);
        if (existingRecord != null && existingRecord.getTestStatus() == 1) {
            log.info("学生 {} 已有进行中的测试记录: {}", studentId, existingRecord.getId());
            return existingRecord;
        }

        // 创建新的测试记录
        Test test = testMapper.findTestById(testId);
        if (test == null) {
            log.error("测试不存在: {}", testId);
            return null;
        }

        StudentTestRecord record = new StudentTestRecord();
        record.setStudentId(studentId);
        record.setTestId(testId);
        record.setTestName(test.getTestName());
        record.setStartTime(new Date());
        record.setTotalQuestions(test.getTotalQuestions());
        record.setTotalPoints(test.getTotalPoints());
        record.setTestStatus(1); // 进行中
        record.setCreateAt(new Date());
        record.setUpdateAt(new Date());
        record.setDeleteFlag("N");

        int result = studentTestRecordMapper.insertTestRecord(record);
        return result > 0 ? record : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentTestAnswer submitAnswer(Integer testRecordId, Integer questionId, String studentAnswer) {
        try {
            // 获取测试记录
            StudentTestRecord record = studentTestRecordMapper.findTestRecordById(testRecordId);
            if (record == null) {
                log.error("测试记录不存在: {}", testRecordId);
                return null;
            }

            // 获取题目信息
            Question question = questionMapper.findQuestionById(questionId);
            if (question == null) {
                log.error("题目不存在: {}", questionId);
                return null;
            }

            // 检查是否已有答题记录
            StudentTestAnswer existingAnswer = studentTestAnswerMapper.findAnswerByRecordAndQuestion(testRecordId, questionId);

            // 判断答案是否正确
            boolean isCorrect = question.getCorrectAnswer().equals(studentAnswer);
            int earnedPoints = isCorrect ? question.getPoints() : 0;

            if (existingAnswer != null) {
                // 更新现有答题记录
                existingAnswer.setStudentAnswer(studentAnswer);
                existingAnswer.setIsCorrect(isCorrect ? 1 : 0);
                existingAnswer.setEarnedPoints(earnedPoints);
                existingAnswer.setAnswerTime(new Date());

                int result = studentTestAnswerMapper.updateAnswer(existingAnswer);
                return result > 0 ? existingAnswer : null;
            } else {
                // 创建新的答题记录
                StudentTestAnswer answer = new StudentTestAnswer();
                answer.setTestRecordId(testRecordId);
                answer.setStudentId(record.getStudentId());
                answer.setQuestionId(questionId);
                answer.setQuestionContent(question.getQuestionContent());
                answer.setCorrectAnswer(question.getCorrectAnswer());
                answer.setStudentAnswer(studentAnswer);
                answer.setIsCorrect(isCorrect ? 1 : 0);
                answer.setPoints(question.getPoints());
                answer.setEarnedPoints(earnedPoints);
                answer.setAnswerTime(new Date());
                answer.setCreateAt(new Date());

                int result = studentTestAnswerMapper.insertAnswer(answer);
                return result > 0 ? answer : null;
            }

        } catch (Exception e) {
            log.error("提交答案时发生异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentTestRecord completeTest(Integer testRecordId) {
        try {
            // 获取测试记录
            StudentTestRecord record = studentTestRecordMapper.findTestRecordById(testRecordId);
            if (record == null) {
                log.error("测试记录不存在: {}", testRecordId);
                return null;
            }

            // 获取所有答题记录
            List<StudentTestAnswer> answers = studentTestAnswerMapper.findAnswersByTestRecordId(testRecordId);

            // 计算统计信息
            int answeredQuestions = answers.size();
            int correctAnswers = (int) answers.stream().filter(answer -> answer.getIsCorrect() == 1).count();
            int earnedPoints = answers.stream().mapToInt(StudentTestAnswer::getEarnedPoints).sum();

            // 计算得分率
            BigDecimal scorePercentage = BigDecimal.ZERO;
            if (record.getTotalPoints() > 0) {
                scorePercentage = new BigDecimal(earnedPoints)
                    .divide(new BigDecimal(record.getTotalPoints()), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100));
            }

            // 计算用时
            Date endTime = new Date();
            int timeSpent = (int) ((endTime.getTime() - record.getStartTime().getTime()) / (1000 * 60)); // 分钟

            // 更新测试记录
            record.setEndTime(endTime);
            record.setSubmitTime(endTime);
            record.setTimeSpent(timeSpent);
            record.setAnsweredQuestions(answeredQuestions);
            record.setCorrectAnswers(correctAnswers);
            record.setEarnedPoints(earnedPoints);
            record.setScorePercentage(scorePercentage);
            record.setTestStatus(2); // 已完成
            record.setUpdateAt(new Date());

            int result = studentTestRecordMapper.updateTestRecord(record);
            if (result > 0) {
                log.info("测试完成，记录ID: {}, 得分: {}/{}", testRecordId, earnedPoints, record.getTotalPoints());
                return record;
            } else {
                log.error("更新测试记录失败");
                return null;
            }

        } catch (Exception e) {
            log.error("完成测试时发生异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<StudentTestRecord> getStudentTestHistory(Integer studentId, Integer page, Integer size) {
        int offset = (page - 1) * size;
        return studentTestRecordMapper.findTestRecordsByStudentIdWithPagination(studentId, offset, size);
    }

    @Override
    public List<StudentTestAnswer> getTestAnswerDetails(Integer testRecordId) {
        return studentTestAnswerMapper.findAnswersByTestRecordId(testRecordId);
    }

    @Override
    public Map<String, Object> getStudentTestStatistics(Integer studentId, Integer gradeId) {
        Map<String, Object> statistics = new HashMap<>();

        // 获取测试记录统计
        List<StudentTestRecord> records = studentTestRecordMapper.findTestRecordsByStudentId(studentId);
        if (gradeId != null) {
            records = records.stream()
                .filter(record -> record.getTest().getGradeId().equals(gradeId))
                .collect(java.util.stream.Collectors.toList());
        }

        int totalTests = records.size();
        int completedTests = (int) records.stream().filter(record -> record.getTestStatus() == 2).count();
        int totalQuestions = records.stream().mapToInt(StudentTestRecord::getTotalQuestions).sum();
        int answeredQuestions = records.stream().mapToInt(StudentTestRecord::getAnsweredQuestions).sum();
        int correctAnswers = records.stream().mapToInt(StudentTestRecord::getCorrectAnswers).sum();
        int totalPoints = records.stream().mapToInt(StudentTestRecord::getTotalPoints).sum();
        int earnedPoints = records.stream().mapToInt(StudentTestRecord::getEarnedPoints).sum();

        // 计算平均分
        BigDecimal averageScore = BigDecimal.ZERO;
        if (completedTests > 0) {
            BigDecimal totalScorePercentage = records.stream()
                .filter(record -> record.getTestStatus() == 2)
                .map(StudentTestRecord::getScorePercentage)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            averageScore = totalScorePercentage.divide(new BigDecimal(completedTests), 2, BigDecimal.ROUND_HALF_UP);
        }

        // 计算正确率
        BigDecimal accuracyRate = BigDecimal.ZERO;
        if (answeredQuestions > 0) {
            accuracyRate = new BigDecimal(correctAnswers)
                .divide(new BigDecimal(answeredQuestions), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100));
        }

        statistics.put("totalTests", totalTests);
        statistics.put("completedTests", completedTests);
        statistics.put("totalQuestions", totalQuestions);
        statistics.put("answeredQuestions", answeredQuestions);
        statistics.put("correctAnswers", correctAnswers);
        statistics.put("totalPoints", totalPoints);
        statistics.put("earnedPoints", earnedPoints);
        statistics.put("averageScore", averageScore);
        statistics.put("accuracyRate", accuracyRate);

        return statistics;
    }

    @Override
    public List<Map<String, Object>> getKnowledgeMastery(Integer studentId) {
        // 这里需要实现知识点掌握情况的查询逻辑
        // 暂时返回空列表，后续可以基于测试结果计算
        return new ArrayList<>();
    }

    @Override
    public List<StudentTestRecord> getOngoingTests(Integer studentId) {
        return studentTestRecordMapper.findOngoingTestRecords(studentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentTestRecord generateRandomTestWithDistribution(Integer studentId, Integer gradeId,
                                                              List<Integer> categoryIds,
                                                              Integer questionCount,
                                                              boolean equalDistribution) {
        try {
            List<Question> allQuestions = new ArrayList<>();

            // 先根据年级和分类筛选出符合条件的题目池
            if (equalDistribution) {
                // 均匀分配难度：1/3简单，1/3中等，1/3困难
                int easyCount = (int) Math.ceil(questionCount / 3.0);
                int mediumCount = questionCount / 3;
                int hardCount = questionCount - easyCount - mediumCount;

                log.info("难度分配：简单={}, 中等={}, 困难={}，年级ID: {}, 分类: {}",
                        easyCount, mediumCount, hardCount, gradeId, categoryIds);

                // 从符合条件的题目池中获取简单题
                if (easyCount > 0) {
                    List<Question> easyQuestionPool = questionMapper.findQuestionsByGradeCategoryAndDifficulty(
                        gradeId, categoryIds, 1);
                    if (easyQuestionPool != null && !easyQuestionPool.isEmpty()) {
                        // 打乱顺序后取指定数量
                        Collections.shuffle(easyQuestionPool);
                        int actualEasyCount = Math.min(easyCount, easyQuestionPool.size());
                        allQuestions.addAll(easyQuestionPool.subList(0, actualEasyCount));
                        log.info("从题目池中获取简单题: 需要{}, 实际获取{}", easyCount, actualEasyCount);
                    } else {
                        log.warn("没有找到符合条件的简单题，年级ID: {}, 分类: {}", gradeId, categoryIds);
                    }
                }

                // 从符合条件的题目池中获取中等题
                if (mediumCount > 0) {
                    List<Question> mediumQuestionPool = questionMapper.findQuestionsByGradeCategoryAndDifficulty(
                        gradeId, categoryIds, 2);
                    if (mediumQuestionPool != null && !mediumQuestionPool.isEmpty()) {
                        // 打乱顺序后取指定数量
                        Collections.shuffle(mediumQuestionPool);
                        int actualMediumCount = Math.min(mediumCount, mediumQuestionPool.size());
                        allQuestions.addAll(mediumQuestionPool.subList(0, actualMediumCount));
                        log.info("从题目池中获取中等题: 需要{}, 实际获取{}", mediumCount, actualMediumCount);
                    } else {
                        log.warn("没有找到符合条件的中等题，年级ID: {}, 分类: {}", gradeId, categoryIds);
                    }
                }

                // 从符合条件的题目池中获取困难题
                if (hardCount > 0) {
                    List<Question> hardQuestionPool = questionMapper.findQuestionsByGradeCategoryAndDifficulty(
                        gradeId, categoryIds, 3);
                    if (hardQuestionPool != null && !hardQuestionPool.isEmpty()) {
                        // 打乱顺序后取指定数量
                        Collections.shuffle(hardQuestionPool);
                        int actualHardCount = Math.min(hardCount, hardQuestionPool.size());
                        allQuestions.addAll(hardQuestionPool.subList(0, actualHardCount));
                        log.info("从题目池中获取困难题: 需要{}, 实际获取{}", hardCount, actualHardCount);
                    } else {
                        log.warn("没有找到符合条件的困难题，年级ID: {}, 分类: {}", gradeId, categoryIds);
                    }
                }
            } else {
                // 不均匀分配，从符合条件的题目池中随机选择（不限制难度）
                List<Question> questionPool = questionMapper.findQuestionsByGradeCategoryAndDifficulty(
                    gradeId, categoryIds, null);
                if (questionPool != null && !questionPool.isEmpty()) {
                    // 打乱顺序后取指定数量
                    Collections.shuffle(questionPool);
                    int actualCount = Math.min(questionCount, questionPool.size());
                    allQuestions.addAll(questionPool.subList(0, actualCount));
                    log.info("从题目池中随机获取题目: 需要{}, 实际获取{}", questionCount, actualCount);
                } else {
                    log.warn("没有找到符合条件的题目，年级ID: {}, 分类: {}", gradeId, categoryIds);
                }
            }

            if (allQuestions.isEmpty()) {
                log.warn("没有找到符合条件的题目，年级ID: {}, 分类: {}", gradeId, categoryIds);
                return null;
            }

            // 最终打乱所有题目顺序
            Collections.shuffle(allQuestions);

            // 创建测试
            Test test = new Test();
            test.setTestName("随机测试_" + new Date().getTime());
            test.setGradeId(gradeId);
            test.setTotalQuestions(allQuestions.size());
            test.setTotalPoints(allQuestions.size()); // 默认每题1分
            test.setTimeLimit(60); // 默认60分钟
            test.setTestType(1); // 练习测试
            test.setStatus(1); // 启用
            test.setCreateAt(new Date());
            test.setUpdateAt(new Date());
            test.setDeleteFlag("N");

            int testResult = testMapper.insertTest(test);
            if (testResult <= 0) {
                log.error("创建测试失败");
                return null;
            }

            // 创建测试题目关联
            for (int i = 0; i < allQuestions.size(); i++) {
                TestQuestion testQuestion = new TestQuestion();
                testQuestion.setTestId(test.getId());
                testQuestion.setQuestionId(allQuestions.get(i).getId());
                testQuestion.setSortOrder(i + 1);
                testQuestion.setPoints(1);
                testQuestion.setCreateAt(new Date());

                testQuestionMapper.insertTestQuestion(testQuestion);
            }

            // 创建学生测试记录
            StudentTestRecord record = new StudentTestRecord();
            record.setStudentId(studentId);
            record.setTestId(test.getId());
            record.setTestName(test.getTestName());
            record.setStartTime(new Date());
            record.setTotalQuestions(allQuestions.size());
            record.setTotalPoints(allQuestions.size());
            record.setTestStatus(1); // 进行中
            record.setCreateAt(new Date());
            record.setUpdateAt(new Date());
            record.setDeleteFlag("N");

            int recordResult = studentTestRecordMapper.insertTestRecord(record);
            if (recordResult <= 0) {
                log.error("创建测试记录失败");
                return null;
            }

            log.info("为学生 {} 生成随机测试成功，测试ID: {}, 记录ID: {}, 难度分配: {}",
                    studentId, test.getId(), record.getId(), equalDistribution);
            return record;

        } catch (Exception e) {
            log.error("生成随机测试时发生异常: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public TestWithQuestionsDTO getTestWithQuestions(Integer testRecordId) {
        try {
            // 1. 获取测试记录
            StudentTestRecord record = studentTestRecordMapper.findTestRecordById(testRecordId);
            if (record == null) {
                log.error("测试记录不存在: {}", testRecordId);
                return null;
            }

            // 2. 获取测试信息
            Test test = testMapper.findTestById(record.getTestId());
            if (test == null) {
                log.error("测试不存在: {}", record.getTestId());
                return null;
            }

            // 3. 获取测试题目关联
            List<TestQuestion> testQuestions = testQuestionMapper.findTestQuestionsByTestId(test.getId());
            if (testQuestions == null || testQuestions.isEmpty()) {
                log.warn("测试 {} 没有题目", test.getId());
                return null;
            }

            // 4. 构建题目详情列表
            List<TestQuestionDetail> questionDetails = new ArrayList<>();
            int easyCount = 0;
            int mediumCount = 0;
            int hardCount = 0;

            for (TestQuestion tq : testQuestions) {
                Question question = questionMapper.findQuestionById(tq.getQuestionId());
                if (question != null) {
                    TestQuestionDetail detail = new TestQuestionDetail();

                    // 设置题目基本信息
                    detail.setQuestionId(question.getId());
                    detail.setSortOrder(tq.getSortOrder());
                    detail.setPoints(tq.getPoints());
                    detail.setQuestionTitle(question.getQuestionTitle());
                    detail.setQuestionTitleFr(question.getQuestionTitleFr());
                    detail.setQuestionContent(question.getQuestionContent());
                    detail.setQuestionContentFr(question.getQuestionContentFr());
                    detail.setOptions(question.getOptions());
                    detail.setOptionsFr(question.getOptionsFr());
                    detail.setCorrectAnswer(question.getCorrectAnswer());
                    detail.setCorrectAnswerFr(question.getCorrectAnswerFr());
                    detail.setAnswerExplanation(question.getAnswerExplanation());
                    detail.setAnswerExplanationFr(question.getAnswerExplanationFr());
                    detail.setDifficultyLevel(question.getDifficultyLevel());

                    // 设置知识点信息
                    detail.setKnowledgePointId(question.getKnowledgePointId());
                    KnowledgePoint kp = knowledgePointService.findKnowledgePointById(question.getKnowledgePointId());
                    if (kp != null) {
                        detail.setKnowledgePointName(kp.getPointName());
                        detail.setKnowledgePointNameFr(kp.getPointNameFr());
                    }

                    // 统计难度分布
                    Integer difficulty = question.getDifficultyLevel();
                    if (difficulty != null) {
                        switch (difficulty) {
                            case 1:
                                easyCount++;
                                break;
                            case 2:
                                mediumCount++;
                                break;
                            case 3:
                                hardCount++;
                                break;
                        }
                    }

                    questionDetails.add(detail);
                }
            }

            // 按排序字段排序
            questionDetails.sort(Comparator.comparing(TestQuestionDetail::getSortOrder));

            // 5. 构建返回DTO
            TestWithQuestionsDTO dto = new TestWithQuestionsDTO();
            dto.setId(record.getId());
            dto.setStudentId(record.getStudentId());
            dto.setTestId(record.getTestId());
            dto.setTestName(record.getTestName());
            dto.setTestNameFr(record.getTestNameFr());
            dto.setStartTime(record.getStartTime());
            dto.setEndTime(record.getEndTime());
            dto.setTimeLimit(test.getTimeLimit());
            dto.setTotalQuestions(record.getTotalQuestions());
            dto.setTotalPoints(record.getTotalPoints());
            dto.setAnsweredQuestions(record.getAnsweredQuestions());
            dto.setTestStatus(record.getTestStatus());
            dto.setCreateAt(record.getCreateAt());
            dto.setQuestions(questionDetails);
            dto.setEasyCount(easyCount);
            dto.setMediumCount(mediumCount);
            dto.setHardCount(hardCount);

            log.info("获取测试详情成功，测试记录ID: {}, 题目数量: {}", testRecordId, questionDetails.size());
            return dto;

        } catch (Exception e) {
            log.error("获取测试详情时发生异常: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TestAnalysisResult batchSubmitAnswersAndAnalyze(BatchAnswerRequest request) {
        try {
            Integer testRecordId = request.getTestRecordId();
            Integer studentId = request.getStudentId();

            // 1. 获取测试记录
            StudentTestRecord record = studentTestRecordMapper.findTestRecordById(testRecordId);
            if (record == null) {
                log.error("测试记录不存在: {}", testRecordId);
                return null;
            }

            // 2. 获取测试信息
            Test test = testMapper.findTestById(record.getTestId());
            if (test == null) {
                log.error("测试不存在: {}", record.getTestId());
                return null;
            }

            // 3. 获取测试题目
            List<TestQuestion> testQuestions = testQuestionMapper.findTestQuestionsByTestId(test.getId());
            if (testQuestions == null || testQuestions.isEmpty()) {
                log.error("测试没有题目: {}", test.getId());
                return null;
            }

            // 4. 创建答案映射
            Map<Integer, BatchAnswerRequest.AnswerItem> answerMap = request.getAnswers().stream()
                .collect(Collectors.toMap(BatchAnswerRequest.AnswerItem::getQuestionId, item -> item));

            // 5. 批量保存答案并计算得分
            int earnedPoints = 0;
            int correctAnswers = 0;
            int answeredQuestions = 0;
            Map<Integer, Integer> knowledgePointStats = new HashMap<>(); // 知识点正确数
            Map<Integer, Integer> knowledgePointTotals = new HashMap<>(); // 知识点总题数

            for (TestQuestion tq : testQuestions) {
                Question question = questionMapper.findQuestionById(tq.getQuestionId());
                if (question == null) {
                    continue;
                }

                BatchAnswerRequest.AnswerItem answerItem = answerMap.get(tq.getQuestionId());

                // 创建答题记录
                StudentTestAnswer answer = new StudentTestAnswer();
                answer.setTestRecordId(testRecordId);
                answer.setStudentId(studentId);
                answer.setQuestionId(question.getId());
                answer.setQuestionContent(question.getQuestionContent());
                answer.setQuestionContentFr(question.getQuestionContentFr());
                answer.setCorrectAnswer(question.getCorrectAnswer());
                answer.setCorrectAnswerFr(question.getCorrectAnswerFr());
                answer.setPoints(tq.getPoints());
                answer.setAnswerTime(new Date());

                if (answerItem != null && answerItem.getStudentAnswer() != null) {
                    answer.setStudentAnswer(answerItem.getStudentAnswer());
                    answer.setTimeSpent(answerItem.getTimeSpent());
                    answeredQuestions++;

                    // 判断是否正确
                    boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(answerItem.getStudentAnswer());
                    answer.setIsCorrect(isCorrect ? 1 : 0);

                    if (isCorrect) {
                        correctAnswers++;
                        earnedPoints += tq.getPoints();
                        // 统计知识点正确数
                        knowledgePointStats.merge(question.getKnowledgePointId(), 1, Integer::sum);
                    } else {
                        answer.setEarnedPoints(0);
                    }

                    if (isCorrect) {
                        answer.setEarnedPoints(tq.getPoints());
                    } else {
                        answer.setEarnedPoints(0);
                    }
                } else {
                    answer.setIsCorrect(0);
                    answer.setEarnedPoints(0);
                }

                // 统计知识点总题数
                knowledgePointTotals.merge(question.getKnowledgePointId(), 1, Integer::sum);

                // 保存答题记录
                studentTestAnswerMapper.insertTestAnswer(answer);
            }

            // 6. 更新测试记录
            Date endTime = new Date();
            int timeSpent = (int) ((endTime.getTime() - record.getStartTime().getTime()) / (1000 * 60)); // 分钟

            BigDecimal scorePercentage = BigDecimal.ZERO;
            if (record.getTotalPoints() > 0) {
                scorePercentage = new BigDecimal(earnedPoints)
                    .divide(new BigDecimal(record.getTotalPoints()), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100));
            }

            record.setEndTime(endTime);
            record.setSubmitTime(endTime);
            record.setTimeSpent(timeSpent);
            record.setAnsweredQuestions(answeredQuestions);
            record.setCorrectAnswers(correctAnswers);
            record.setEarnedPoints(earnedPoints);
            record.setScorePercentage(scorePercentage);
            record.setTestStatus(2); // 已完成
            record.setUpdateAt(new Date());

            studentTestRecordMapper.updateTestRecord(record);

            // 7. 计算各知识点得分和分析
            List<TestAnalysisResult.KnowledgePointScore> kpScores = new ArrayList<>();
            List<Integer> strongPoints = new ArrayList<>();
            List<Integer> needsImprovementPoints = new ArrayList<>();
            List<Integer> weakPoints = new ArrayList<>();

            for (Integer kpId : knowledgePointTotals.keySet()) {
                KnowledgePoint kp = knowledgePointService.findKnowledgePointById(kpId);
                if (kp == null) {
                    continue;
                }

                int total = knowledgePointTotals.get(kpId);
                int correct = knowledgePointStats.getOrDefault(kpId, 0);

                // 计算知识点得分
                int kpTotalPoints = 0;
                int kpEarnedPoints = 0;
                for (TestQuestion tq : testQuestions) {
                    Question q = questionMapper.findQuestionById(tq.getQuestionId());
                    if (q != null && q.getKnowledgePointId().equals(kpId)) {
                        kpTotalPoints += tq.getPoints();
                        BatchAnswerRequest.AnswerItem item = answerMap.get(q.getId());
                        if (item != null) {
                            boolean isCorrect = q.getCorrectAnswer().equalsIgnoreCase(item.getStudentAnswer());
                            if (isCorrect) {
                                kpEarnedPoints += tq.getPoints();
                            }
                        }
                    }
                }

                BigDecimal kpScoreRate = BigDecimal.ZERO;
                if (kpTotalPoints > 0) {
                    kpScoreRate = new BigDecimal(kpEarnedPoints)
                        .divide(new BigDecimal(kpTotalPoints), 4, BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal(100));
                }

                TestAnalysisResult.KnowledgePointScore score = new TestAnalysisResult.KnowledgePointScore();
                score.setKnowledgePointId(kpId);
                score.setKnowledgePointName(kp.getPointName());
                score.setKnowledgePointNameFr(kp.getPointNameFr());
                score.setTotalPoints(kpTotalPoints);
                score.setEarnedPoints(kpEarnedPoints);
                score.setScoreRate(kpScoreRate);
                score.setQuestionCount(total);
                score.setCorrectCount(correct);

                kpScores.add(score);

                // 分类知识点
                if (kpScoreRate.compareTo(new BigDecimal(80)) >= 0) {
                    strongPoints.add(kpId);
                } else if (kpScoreRate.compareTo(new BigDecimal(50)) >= 0) {
                    needsImprovementPoints.add(kpId);
                } else {
                    weakPoints.add(kpId);
                }
            }

            // 8. 构建知识点分析
            TestAnalysisResult.KnowledgePointAnalysis analysis = new TestAnalysisResult.KnowledgePointAnalysis();
            analysis.setStrongPoints(strongPoints);
            analysis.setNeedsImprovementPoints(needsImprovementPoints);
            analysis.setWeakPoints(weakPoints);

            // 生成分析摘要（中文）
            String strongSummary = strongPoints.isEmpty() ? "None" :
                strongPoints.stream()
                    .map(kpId -> {
                        KnowledgePoint kp = knowledgePointService.findKnowledgePointById(kpId);
                        return kp != null ? kp.getPointName() : "";
                    })
                    .filter(name -> !name.isEmpty())
                    .collect(Collectors.joining(", "));

            String needsSummary = needsImprovementPoints.isEmpty() ? "None" :
                needsImprovementPoints.stream()
                    .map(kpId -> {
                        KnowledgePoint kp = knowledgePointService.findKnowledgePointById(kpId);
                        return kp != null ? kp.getPointName() : "";
                    })
                    .filter(name -> !name.isEmpty())
                    .collect(Collectors.joining(", "));

            String weakSummary = weakPoints.isEmpty() ? "None" :
                weakPoints.stream()
                    .map(kpId -> {
                        KnowledgePoint kp = knowledgePointService.findKnowledgePointById(kpId);
                        return kp != null ? kp.getPointName() : "";
                    })
                    .filter(name -> !name.isEmpty())
                    .collect(Collectors.joining(", "));

            // 生成分析摘要（法语）
            String strongSummaryFr = strongPoints.isEmpty() ? "Aucun" :
                strongPoints.stream()
                    .map(kpId -> {
                        KnowledgePoint kp = knowledgePointService.findKnowledgePointById(kpId);
                        return kp != null && kp.getPointNameFr() != null ? kp.getPointNameFr() :
                               (kp != null ? kp.getPointName() : "");
                    })
                    .filter(name -> !name.isEmpty())
                    .collect(Collectors.joining(", "));

            String needsSummaryFr = needsImprovementPoints.isEmpty() ? "Aucun" :
                needsImprovementPoints.stream()
                    .map(kpId -> {
                        KnowledgePoint kp = knowledgePointService.findKnowledgePointById(kpId);
                        return kp != null && kp.getPointNameFr() != null ? kp.getPointNameFr() :
                               (kp != null ? kp.getPointName() : "");
                    })
                    .filter(name -> !name.isEmpty())
                    .collect(Collectors.joining(", "));

            String weakSummaryFr = weakPoints.isEmpty() ? "Aucun" :
                weakPoints.stream()
                    .map(kpId -> {
                        KnowledgePoint kp = knowledgePointService.findKnowledgePointById(kpId);
                        return kp != null && kp.getPointNameFr() != null ? kp.getPointNameFr() :
                               (kp != null ? kp.getPointName() : "");
                    })
                    .filter(name -> !name.isEmpty())
                    .collect(Collectors.joining(", "));

            analysis.setStrongSummary(strongSummary);
            analysis.setStrongSummaryFr(strongSummaryFr);
            analysis.setNeedsImprovementSummary(needsSummary);
            analysis.setNeedsImprovementSummaryFr(needsSummaryFr);
            analysis.setWeakSummary(weakSummary);
            analysis.setWeakSummaryFr(weakSummaryFr);

            // 9. 构建返回结果
            TestAnalysisResult result = new TestAnalysisResult();
            result.setTestRecordId(testRecordId);
            result.setStudentId(studentId);
            result.setTestId(test.getId());
            result.setTotalPoints(record.getTotalPoints());
            result.setEarnedPoints(earnedPoints);
            result.setScoreRate(scorePercentage);
            result.setAccuracyRate(new BigDecimal(correctAnswers)
                .divide(new BigDecimal(answeredQuestions), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100)));
            result.setTotalQuestions(record.getTotalQuestions());
            result.setCorrectAnswers(correctAnswers);
            result.setKnowledgePointScores(kpScores);
            result.setKnowledgePointAnalysis(analysis);
            result.setCreateAt(new Date());

            // 生成总体评价
            if (scorePercentage.compareTo(new BigDecimal(80)) >= 0) {
                result.setOverallComment("表现优秀！");
                result.setOverallCommentFr("Excellent travail !");
            } else if (scorePercentage.compareTo(new BigDecimal(60)) >= 0) {
                result.setOverallComment("表现良好");
                result.setOverallCommentFr("Bon travail");
            } else if (scorePercentage.compareTo(new BigDecimal(40)) >= 0) {
                result.setOverallComment("需要继续努力");
                result.setOverallCommentFr("Continuez vos efforts");
            } else {
                result.setOverallComment("需要加强学习");
                result.setOverallCommentFr("Renforcez votre apprentissage");
            }

            // 生成学习建议
            StringBuilder recommendations = new StringBuilder();
            recommendations.append("总体得分: ").append(scorePercentage).append("%\n\n");

            if (!strongPoints.isEmpty()) {
                recommendations.append("掌握较好: ").append(strongSummary).append("\n");
            }
            if (!needsImprovementPoints.isEmpty()) {
                recommendations.append("需要改进: ").append(needsSummary).append("\n");
            }
            if (!weakPoints.isEmpty()) {
                recommendations.append("薄弱环节: ").append(weakSummary).append("\n");
            }

            result.setRecommendations(recommendations.toString());
            result.setRecommendationsFr(recommendations.toString()); // 简化处理，实际应该翻译

            // 10. 保存分析报告到数据库
            TestAnalysisReport analysisReport = new TestAnalysisReport();
            analysisReport.setTestRecordId(testRecordId);
            analysisReport.setStudentId(studentId);
            analysisReport.setTestId(test.getId());
            analysisReport.setReportType(1); // 1-分析报告
            analysisReport.setReportTitle("测试分析报告_" + testRecordId);
            analysisReport.setReportTitleFr("Rapport d'analyse de test_" + testRecordId);

            // 将分析结果转换为JSON存储
            String reportContent = String.format(
                "{\"overallComment\":\"%s\",\"recommendations\":\"%s\"}",
                result.getOverallComment(), result.getRecommendations()
            );
            analysisReport.setReportContent(reportContent);

            // 保存知识点分类（JSON数组）
            analysisReport.setStrongKnowledgePoints("[" + strongPoints.stream()
                .map(String::valueOf).collect(Collectors.joining(",")) + "]");
            analysisReport.setNeedsImprovementPoints("[" + needsImprovementPoints.stream()
                .map(String::valueOf).collect(Collectors.joining(",")) + "]");
            analysisReport.setWeakKnowledgePoints("[" + weakPoints.stream()
                .map(String::valueOf).collect(Collectors.joining(",")) + "]");

            // 保存统计信息
            analysisReport.setOverallScore(scorePercentage);
            analysisReport.setTotalPoints(record.getTotalPoints());
            analysisReport.setEarnedPoints(earnedPoints);
            analysisReport.setAccuracyRate(result.getAccuracyRate());

            // 保存摘要
            analysisReport.setStrongPointsSummary(strongSummary);
            analysisReport.setNeedsImprovementSummary(needsSummary);
            analysisReport.setWeakPointsSummary(weakSummary);

            // 保存建议
            analysisReport.setRecommendations(recommendations.toString());
            analysisReport.setRecommendationsFr(recommendations.toString());

            // 保存analysisData（完整JSON数据）
            String analysisData = String.format(
                "{\"strongPoints\":[%s],\"needsImprovementPoints\":[%s],\"weakPoints\":[%s],\"overallScore\":%s}",
                strongPoints.stream().map(String::valueOf).collect(Collectors.joining(",")),
                needsImprovementPoints.stream().map(String::valueOf).collect(Collectors.joining(",")),
                weakPoints.stream().map(String::valueOf).collect(Collectors.joining(",")),
                scorePercentage
            );
            analysisReport.setAnalysisData(analysisData);

            analysisReport.setCreateAt(new Date());
            analysisReport.setDeleteFlag("N");

            // 检查是否已存在报告
            TestAnalysisReport existingReport = testAnalysisReportMapper.findByTestRecordId(testRecordId);
            if (existingReport != null) {
                analysisReport.setId(existingReport.getId());
                testAnalysisReportMapper.updateAnalysisReport(analysisReport);
                log.info("更新分析报告成功，测试记录: {}", testRecordId);
            } else {
                testAnalysisReportMapper.insertAnalysisReport(analysisReport);
                log.info("保存分析报告成功，测试记录: {}", testRecordId);
            }

            log.info("批量提交答案成功，测试记录: {}, 得分: {}/{}", testRecordId, earnedPoints, record.getTotalPoints());
            return result;

        } catch (Exception e) {
            log.error("批量提交答案时发生异常: {}", e.getMessage(), e);
            return null;
        }
    }
}
