package com.yy.my_tutor.test.service.impl;

import com.yy.my_tutor.course.service.CourseGenerateService;
import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.math.domain.Question;
import com.yy.my_tutor.math.service.KnowledgePointService;
import com.yy.my_tutor.test.domain.*;
import com.yy.my_tutor.test.mapper.StudentTestRecordMapper;
import com.yy.my_tutor.test.mapper.TestMapper;
import com.yy.my_tutor.test.mapper.TestQuestionMapper;
import com.yy.my_tutor.test.service.AITestGenerateService;
import com.yy.my_tutor.test.service.QuestionSourceService;
import com.yy.my_tutor.test.service.StudentTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AI 测试生成服务实现类
 */
@Slf4j
@Service
public class AITestGenerateServiceImpl implements AITestGenerateService {

    @Resource
    private KnowledgePointService knowledgePointService;

    @Resource
    private QuestionSourceService questionSourceService;

    @Resource
    private TestMapper testMapper;

    @Resource
    private TestQuestionMapper testQuestionMapper;

    @Resource
    private StudentTestRecordMapper studentTestRecordMapper;

    @Resource
    private StudentTestService studentTestService;

    @Resource
    private CourseGenerateService courseGenerateService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TestWithQuestionsDTO generateTestBySingleKnowledgePoint(GenerateAITestRequest request) {
        // 1. 参数校验
        validateRequest(request);
        if (request.getKnowledgePointId() == null) {
            throw new IllegalArgumentException("知识点ID不能为空");
        }

        // 2. 查询知识点信息
        KnowledgePoint knowledgePoint = knowledgePointService.findKnowledgePointById(request.getKnowledgePointId());
        if (knowledgePoint == null) {
            throw new IllegalArgumentException("知识点不存在: " + request.getKnowledgePointId());
        }

        // 3. 确定难度级别
        Integer difficultyLevel = request.getDifficultyLevel();
        if (difficultyLevel == null) {
            difficultyLevel = courseGenerateService.determineDifficultyLevel(
                    request.getStudentId(), request.getKnowledgePointId());
        }

        // 4. 获取题目（优先从题库，不足则AI生成，固定单选类型）
        List<Question> questions = questionSourceService.getQuestions(
                request.getStudentId(),
                Collections.singletonList(request.getKnowledgePointId()),
                request.getQuestionCount(),
                difficultyLevel,
                request.getSaveToQuestionBank()
        );

        if (questions.isEmpty()) {
            throw new RuntimeException("无法生成足够的题目");
        }

        // 5. 创建测试
        String testName = request.getTestName() != null ? request.getTestName() :
                "AI Test - " + knowledgePoint.getPointName();
        String testNameFr = request.getTestNameFr() != null ? request.getTestNameFr() :
                "Test IA - " + (knowledgePoint.getPointNameFr() != null ? knowledgePoint.getPointNameFr() : knowledgePoint.getPointName());

        StudentTestRecord record = createTestAndRecord(
                request.getStudentId(),
                knowledgePoint.getGradeId(),
                questions,
                testName,
                testNameFr,
                difficultyLevel,
                Collections.singletonList(request.getKnowledgePointId())
        );

        // 6. 返回测试详情
        return studentTestService.getTestWithQuestions(record.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TestWithQuestionsDTO generateTestByCategory(GenerateAITestRequest request) {
        // 1. 参数校验
        validateRequest(request);
        if (request.getCategoryId() == null) {
            throw new IllegalArgumentException("知识类型ID不能为空");
        }
        if (request.getDifficultyLevel() == null) {
            throw new IllegalArgumentException("难度级别不能为空");
        }

        // 2. 根据知识类型ID查询关联的所有知识点
        List<KnowledgePoint> knowledgePoints = knowledgePointService.findKnowledgePointsByCategoryId(request.getCategoryId());
        if (knowledgePoints == null || knowledgePoints.isEmpty()) {
            throw new IllegalArgumentException("该知识类型下没有关联的知识点");
        }

        log.info("Found {} knowledge points for category {}", knowledgePoints.size(), request.getCategoryId());

        // 3. 提取知识点ID列表
        List<Integer> knowledgePointIds = knowledgePoints.stream()
                .map(KnowledgePoint::getId)
                .collect(Collectors.toList());

        // 4. 使用传入的难度级别
        Integer difficultyLevel = request.getDifficultyLevel();

        // 5. 计算每个知识点需要的题目数量
        int totalCount = request.getQuestionCount();
        int perPointCount = Math.max(1, totalCount / knowledgePoints.size());
        int remainder = totalCount % knowledgePoints.size();

        // 6. 获取或生成题目
        List<Question> allQuestions = new ArrayList<>();
        for (int i = 0; i < knowledgePoints.size(); i++) {
            KnowledgePoint kp = knowledgePoints.get(i);
            int count = perPointCount + (i < remainder ? 1 : 0);

            List<Question> questions = questionSourceService.getQuestions(
                    request.getStudentId(),
                    Collections.singletonList(kp.getId()),
                    count,
                    difficultyLevel,
                    request.getSaveToQuestionBank()
            );
            allQuestions.addAll(questions);
        }

        if (allQuestions.isEmpty()) {
            throw new RuntimeException("无法生成足够的题目");
        }

        // 7. 打乱题目顺序
        Collections.shuffle(allQuestions);

        // 8. 创建测试
        String testName = request.getTestName() != null ? request.getTestName() : "AI Category Test";
        String testNameFr = request.getTestNameFr() != null ? request.getTestNameFr() : "Test IA par Catégorie";

        Integer gradeId = request.getGradeId() != null ? request.getGradeId() : knowledgePoints.get(0).getGradeId();

        StudentTestRecord record = createTestAndRecord(
                request.getStudentId(),
                gradeId,
                allQuestions,
                testName,
                testNameFr,
                difficultyLevel,
                knowledgePointIds
        );

        // 9. 返回测试详情
        return studentTestService.getTestWithQuestions(record.getId());
    }

    /**
     * 参数校验
     */
    private void validateRequest(GenerateAITestRequest request) {
        if (request.getStudentId() == null) {
            throw new IllegalArgumentException("学生ID不能为空");
        }
        if (request.getQuestionCount() == null || request.getQuestionCount() <= 0) {
            throw new IllegalArgumentException("题目数量必须大于0");
        }
    }

    /**
     * 创建测试和测试记录
     */
    private StudentTestRecord createTestAndRecord(Integer studentId, Integer gradeId,
                                                   List<Question> questions, String testName,
                                                   String testNameFr, Integer difficultyLevel,
                                                   List<Integer> knowledgePointIds) {
        // 1. 创建测试
        Test test = new Test();
        test.setTestName(testName);
        test.setTestNameFr(testNameFr);
        test.setGradeId(gradeId);
        test.setKnowledgePointIds(knowledgePointIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",")));
        test.setTotalQuestions(questions.size());
        test.setTotalPoints(questions.size()); // 每题1分
        test.setTimeLimit(60); // 默认60分钟
        test.setDifficultyLevel(difficultyLevel);
        test.setTestType(2); // AI生成测试
        test.setStatus(1); // 启用
        test.setCreateAt(new Date());
        test.setUpdateAt(new Date());
        test.setDeleteFlag("N");

        testMapper.insertTest(test);
        log.info("Created test with id: {}", test.getId());

        // 2. 创建测试-题目关联
        for (int i = 0; i < questions.size(); i++) {
            TestQuestion testQuestion = new TestQuestion();
            testQuestion.setTestId(test.getId());
            testQuestion.setQuestionId(questions.get(i).getId());
            testQuestion.setSortOrder(i + 1);
            testQuestion.setPoints(1);
            testQuestion.setCreateAt(new Date());

            testQuestionMapper.insertTestQuestion(testQuestion);
        }

        // 3. 创建学生测试记录
        StudentTestRecord record = new StudentTestRecord();
        record.setStudentId(studentId);
        record.setTestId(test.getId());
        record.setTestName(testName);
        record.setTestNameFr(testNameFr);
        record.setStartTime(new Date());
        record.setTotalQuestions(questions.size());
        record.setTotalPoints(questions.size());
        record.setTestStatus(1); // 进行中
        record.setCreateAt(new Date());
        record.setUpdateAt(new Date());
        record.setDeleteFlag("N");

        studentTestRecordMapper.insertTestRecord(record);
        log.info("Created test record with id: {}", record.getId());

        return record;
    }
}
