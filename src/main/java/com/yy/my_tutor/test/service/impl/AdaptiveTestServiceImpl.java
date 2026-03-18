package com.yy.my_tutor.test.service.impl;

import com.yy.my_tutor.math.domain.KnowledgeCategory;
import com.yy.my_tutor.math.domain.KnowledgePoint;
import com.yy.my_tutor.math.domain.Question;
import com.yy.my_tutor.math.service.KnowledgeCategoryService;
import com.yy.my_tutor.math.service.KnowledgePointService;
import com.yy.my_tutor.test.domain.*;
import com.yy.my_tutor.test.mapper.KnowledgeMasteryMapper;
import com.yy.my_tutor.test.mapper.StudentTestRecordMapper;
import com.yy.my_tutor.test.mapper.TestMapper;
import com.yy.my_tutor.test.mapper.TestQuestionMapper;
import com.yy.my_tutor.test.service.AdaptiveTestService;
import com.yy.my_tutor.test.service.QuestionSourceService;
import com.yy.my_tutor.test.service.StudentTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 自适应测验生成服务实现
 */
@Slf4j
@Service
public class AdaptiveTestServiceImpl implements AdaptiveTestService {

    @Resource
    private KnowledgePointService knowledgePointService;

    @Resource
    private KnowledgeCategoryService knowledgeCategoryService;

    @Resource
    private KnowledgeMasteryMapper knowledgeMasteryMapper;

    @Resource
    private StudentTestRecordMapper studentTestRecordMapper;

    @Resource
    private QuestionSourceService questionSourceService;

    @Resource
    private TestMapper testMapper;

    @Resource
    private TestQuestionMapper testQuestionMapper;

    @Resource
    private StudentTestService studentTestService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TestWithQuestionsDTO generateAdaptiveTest(GenerateAdaptiveTestRequest request) {
        // 1. 参数校验
        validateRequest(request);

        // 2. 查询该分类下的所有知识点
        List<KnowledgePoint> knowledgePoints = knowledgePointService.findKnowledgePointsByCategoryId(request.getCategoryId());
        if (knowledgePoints == null || knowledgePoints.isEmpty()) {
            throw new IllegalArgumentException("该知识类型下没有关联的知识点");
        }

        List<Integer> allKpIds = knowledgePoints.stream()
                .map(KnowledgePoint::getId)
                .collect(Collectors.toList());

        // 3. 获取学生掌握数据
        List<KnowledgeMastery> masteryList = knowledgeMasteryMapper
                .findMasteryByStudentAndKnowledgePointIds(request.getStudentId(), allKpIds);

        Map<Integer, KnowledgeMastery> masteryMap = new HashMap<>();
        if (masteryList != null) {
            for (KnowledgeMastery m : masteryList) {
                masteryMap.put(m.getKnowledgePointId(), m);
            }
        }

        // 4. 知识点分桶（薄弱优先策略）— 委托给 AdaptiveStrategyHelper
        AdaptiveStrategyHelper.BucketResult buckets = AdaptiveStrategyHelper.bucketKnowledgePoints(allKpIds, masteryMap);
        List<Integer> weakKpIds = buckets.getWeakKpIds();
        List<Integer> improvingKpIds = buckets.getImprovingKpIds();
        List<Integer> masteredKpIds = buckets.getMasteredKpIds();

        log.info("Knowledge point buckets - weak: {}, improving: {}, mastered: {}",
                weakKpIds.size(), improvingKpIds.size(), masteredKpIds.size());

        // 5. 计算难度分配 — 始终自动计算
        Map<Integer, Integer> diffPercentages = AdaptiveStrategyHelper.computeAdaptiveDifficultyDistribution(masteryMap, allKpIds);

        DifficultyDistribution distribution = new DifficultyDistribution(diffPercentages);
        Map<Integer, Integer> difficultyCounts = distribution.computeCounts(request.getQuestionCount());

        log.info("Difficulty distribution: {}", difficultyCounts);

        // 6. 按知识点分桶分配题量
        int totalCount = request.getQuestionCount();
        int weakCount = (int) Math.round(totalCount * 0.50);
        int improvingCount = (int) Math.round(totalCount * 0.30);
        int masteredCount = totalCount - weakCount - improvingCount;

        // 如果某个桶没有知识点，重新分配到其他桶
        if (weakKpIds.isEmpty()) {
            improvingCount += weakCount / 2;
            masteredCount += weakCount - weakCount / 2;
            weakCount = 0;
        }
        if (improvingKpIds.isEmpty()) {
            weakCount += improvingCount / 2;
            masteredCount += improvingCount - improvingCount / 2;
            improvingCount = 0;
        }
        if (masteredKpIds.isEmpty()) {
            weakCount += masteredCount / 2;
            improvingCount += masteredCount - masteredCount / 2;
            masteredCount = 0;
        }

        // 7. 为每个桶按难度取题
        List<Question> allQuestions = new ArrayList<>();

        allQuestions.addAll(getQuestionsForBucket(request.getStudentId(), weakKpIds, weakCount, difficultyCounts, totalCount));
        allQuestions.addAll(getQuestionsForBucket(request.getStudentId(), improvingKpIds, improvingCount, difficultyCounts, totalCount));
        allQuestions.addAll(getQuestionsForBucket(request.getStudentId(), masteredKpIds, masteredCount, difficultyCounts, totalCount));

        if (allQuestions.isEmpty()) {
            throw new RuntimeException("无法生成足够的题目");
        }

        // 8. 打乱题目顺序
        Collections.shuffle(allQuestions);

        // 9. 获取上次测验记录ID — 始终查最近完成记录
        Integer previousTestRecordId = null;
        StudentTestRecord latestRecord = studentTestRecordMapper
                .findLatestCompletedRecordByStudentId(request.getStudentId());
        if (latestRecord != null) {
            previousTestRecordId = latestRecord.getId();
        }

        // 10. 自动生成测试名称
        String[] testNames = generateTestName(request.getStudentId(), request.getCategoryId());
        String testName = testNames[0];
        String testNameFr = testNames[1];
        Integer gradeId = request.getGradeId() != null ? request.getGradeId() : knowledgePoints.get(0).getGradeId();

        StudentTestRecord record = createAdaptiveTestAndRecord(
                request.getStudentId(),
                gradeId,
                allQuestions,
                testName,
                testNameFr,
                allKpIds,
                distribution.toJson(),
                previousTestRecordId
        );

        return studentTestService.getTestWithQuestions(record.getId());
    }

    /**
     * 为一个桶的知识点按难度批量取题
     */
    private List<Question> getQuestionsForBucket(Integer studentId, List<Integer> kpIds,
                                                  int bucketCount, Map<Integer, Integer> difficultyCounts,
                                                  int totalQuestions) {
        List<Question> result = new ArrayList<>();
        if (kpIds.isEmpty() || bucketCount <= 0) {
            return result;
        }

        // 计算此桶内每个难度需要的题数
        Map<Integer, Integer> bucketDiffCounts = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> entry : difficultyCounts.entrySet()) {
            int count = (int) Math.round((double) bucketCount * entry.getValue() / totalQuestions);
            if (count > 0) {
                bucketDiffCounts.put(entry.getKey(), count);
            }
        }

        // 一次批量调用，减少重复 DB 查询
        Map<Integer, List<Question>> questionsByDiff = questionSourceService
                .getQuestionsByDifficulties(studentId, kpIds, bucketDiffCounts, true);

        for (List<Question> questions : questionsByDiff.values()) {
            result.addAll(questions);
        }

        return result;
    }

    /**
     * 创建自适应测试和测试记录（testType=3）
     */
    private StudentTestRecord createAdaptiveTestAndRecord(Integer studentId, Integer gradeId,
                                                           List<Question> questions, String testName,
                                                           String testNameFr, List<Integer> knowledgePointIds,
                                                           String difficultyDistributionJson,
                                                           Integer previousTestRecordId) {
        Test test = new Test();
        test.setTestName(testName);
        test.setTestNameFr(testNameFr);
        test.setGradeId(gradeId);
        test.setKnowledgePointIds(knowledgePointIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",")));
        test.setTotalQuestions(questions.size());
        test.setTotalPoints(questions.size());
        test.setTimeLimit(60);
        test.setDifficultyLevel(null); // 混合难度，不设单一值
        test.setDifficultyDistribution(difficultyDistributionJson);
        test.setPreviousTestRecordId(previousTestRecordId);
        test.setTestType(3); // 自适应测验
        test.setStatus(1);
        test.setCreateAt(new Date());
        test.setUpdateAt(new Date());
        test.setDeleteFlag("N");

        testMapper.insertTest(test);
        log.info("Created adaptive test with id: {}", test.getId());

        for (int i = 0; i < questions.size(); i++) {
            TestQuestion testQuestion = new TestQuestion();
            testQuestion.setTestId(test.getId());
            testQuestion.setQuestionId(questions.get(i).getId());
            testQuestion.setSortOrder(i + 1);
            testQuestion.setPoints(1);
            testQuestion.setCreateAt(new Date());
            testQuestionMapper.insertTestQuestion(testQuestion);
        }

        StudentTestRecord record = new StudentTestRecord();
        record.setStudentId(studentId);
        record.setTestId(test.getId());
        record.setTestName(testName);
        record.setTestNameFr(testNameFr);
        record.setStartTime(new Date());
        record.setTotalQuestions(questions.size());
        record.setTotalPoints(questions.size());
        record.setTestStatus(1);
        record.setCreateAt(new Date());
        record.setUpdateAt(new Date());
        record.setDeleteFlag("N");

        studentTestRecordMapper.insertTestRecord(record);
        log.info("Created adaptive test record with id: {}", record.getId());

        return record;
    }

    /**
     * 自动生成测试名称（中文 + 法语）
     * 格式：自适应测验 - 分类名 (yyyy-MM-dd) [#序号]
     * @return [testName, testNameFr]
     */
    private String[] generateTestName(Integer studentId, Integer categoryId) {
        KnowledgeCategory category = knowledgeCategoryService.findCategoryById(categoryId);
        String categoryName = category != null ? category.getCategoryName() : "未知分类";
        String categoryNameFr = category != null ? category.getCategoryNameFr() : "Catégorie inconnue";

        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        String prefix = "自适应测验 - " + categoryName + " (" + dateStr + ")";
        String prefixFr = "Test Adaptatif - " + categoryNameFr + " (" + dateStr + ")";

        int count = studentTestRecordMapper.countByStudentAndTestNamePrefix(studentId, prefix);

        String testName;
        String testNameFr;
        if (count == 0) {
            testName = prefix;
            testNameFr = prefixFr;
        } else {
            testName = prefix + " #" + (count + 1);
            testNameFr = prefixFr + " #" + (count + 1);
        }

        return new String[]{testName, testNameFr};
    }

    private void validateRequest(GenerateAdaptiveTestRequest request) {
        if (request.getStudentId() == null) {
            throw new IllegalArgumentException("学生ID不能为空");
        }
        if (request.getCategoryId() == null) {
            throw new IllegalArgumentException("知识类型ID不能为空");
        }
        if (request.getQuestionCount() == null || request.getQuestionCount() <= 0) {
            throw new IllegalArgumentException("题目数量必须大于0");
        }
    }

    private void validateKnowledgePointRequest(GenerateAdaptiveTestRequest request) {
        if (request.getStudentId() == null) {
            throw new IllegalArgumentException("学生ID不能为空");
        }
        if (request.getKnowledgePointId() == null) {
            throw new IllegalArgumentException("知识点ID不能为空");
        }
        if (request.getQuestionCount() == null || request.getQuestionCount() <= 0) {
            throw new IllegalArgumentException("题目数量必须大于0");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TestWithQuestionsDTO generateAdaptiveTestByKnowledgePoint(GenerateAdaptiveTestRequest request) {
        // 1. 参数校验
        validateKnowledgePointRequest(request);

        // 2. 获取知识点信息
        KnowledgePoint knowledgePoint = knowledgePointService.findKnowledgePointById(request.getKnowledgePointId());
        if (knowledgePoint == null) {
            throw new IllegalArgumentException("知识点不存在");
        }

        List<Integer> kpIds = Collections.singletonList(request.getKnowledgePointId());

        // 3. 获取学生掌握数据
        List<KnowledgeMastery> masteryList = knowledgeMasteryMapper
                .findMasteryByStudentAndKnowledgePointIds(request.getStudentId(), kpIds);

        Map<Integer, KnowledgeMastery> masteryMap = new HashMap<>();
        if (masteryList != null) {
            for (KnowledgeMastery m : masteryList) {
                masteryMap.put(m.getKnowledgePointId(), m);
            }
        }

        // 4. 计算难度分配
        Map<Integer, Integer> diffPercentages = AdaptiveStrategyHelper.computeAdaptiveDifficultyDistribution(masteryMap, kpIds);
        DifficultyDistribution distribution = new DifficultyDistribution(diffPercentages);
        Map<Integer, Integer> difficultyCounts = distribution.computeCounts(request.getQuestionCount());

        log.info("KP adaptive difficulty distribution: {}", difficultyCounts);

        // 5. 直接按难度取题（无需分桶）
        Map<Integer, List<Question>> questionsByDiff = questionSourceService
                .getQuestionsByDifficulties(request.getStudentId(), kpIds, difficultyCounts, true);

        List<Question> allQuestions = new ArrayList<>();
        for (List<Question> questions : questionsByDiff.values()) {
            allQuestions.addAll(questions);
        }

        if (allQuestions.isEmpty()) {
            throw new RuntimeException("无法生成足够的题目");
        }

        // 6. 打乱题目顺序
        Collections.shuffle(allQuestions);

        // 7. 获取上次测验记录ID
        Integer previousTestRecordId = null;
        StudentTestRecord latestRecord = studentTestRecordMapper
                .findLatestCompletedRecordByStudentId(request.getStudentId());
        if (latestRecord != null) {
            previousTestRecordId = latestRecord.getId();
        }

        // 8. 自动生成测试名称
        String pointName = knowledgePoint.getPointName();
        String pointNameFr = knowledgePoint.getPointNameFr();
        String[] testNames = generateTestNameByKnowledgePoint(request.getStudentId(), pointName, pointNameFr);
        String testName = testNames[0];
        String testNameFr = testNames[1];
        Integer gradeId = request.getGradeId() != null ? request.getGradeId() : knowledgePoint.getGradeId();

        StudentTestRecord record = createAdaptiveTestAndRecord(
                request.getStudentId(),
                gradeId,
                allQuestions,
                testName,
                testNameFr,
                kpIds,
                distribution.toJson(),
                previousTestRecordId
        );

        return studentTestService.getTestWithQuestions(record.getId());
    }

    /**
     * 按知识点生成测试名称
     * 格式：自适应测验 - 知识点名 (yyyy-MM-dd) [#序号]
     */
    private String[] generateTestNameByKnowledgePoint(Integer studentId, String pointName, String pointNameFr) {
        if (pointName == null) pointName = "未知知识点";
        if (pointNameFr == null) pointNameFr = "Point inconnu";

        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        String prefix = "自适应测验 - " + pointName + " (" + dateStr + ")";
        String prefixFr = "Test Adaptatif - " + pointNameFr + " (" + dateStr + ")";

        int count = studentTestRecordMapper.countByStudentAndTestNamePrefix(studentId, prefix);

        String testName;
        String testNameFr;
        if (count == 0) {
            testName = prefix;
            testNameFr = prefixFr;
        } else {
            testName = prefix + " #" + (count + 1);
            testNameFr = prefixFr + " #" + (count + 1);
        }

        return new String[]{testName, testNameFr};
    }
}
