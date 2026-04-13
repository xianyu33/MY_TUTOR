package com.yy.my_tutor.test.service.impl;

import com.yy.my_tutor.test.domain.StudentTestRecord;
import com.yy.my_tutor.test.domain.TestRecordKnowledgeScore;
import com.yy.my_tutor.test.mapper.StudentTestRecordMapper;
import com.yy.my_tutor.test.service.StudentTestRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 学生测试记录服务实现类
 */
@Service
public class StudentTestRecordServiceImpl implements StudentTestRecordService {
    
    @Autowired
    private StudentTestRecordMapper testRecordMapper;
    
    @Override
    public List<StudentTestRecord> findTestRecordsByStudentId(Integer studentId) {
        return testRecordMapper.findTestRecordsByStudentId(studentId);
    }
    
    @Override
    public List<StudentTestRecord> findTestRecordsByTestId(Integer testId) {
        return testRecordMapper.findTestRecordsByTestId(testId);
    }
    
    @Override
    public StudentTestRecord findTestRecordByStudentAndTest(Integer studentId, Integer testId) {
        return testRecordMapper.findTestRecordByStudentAndTest(studentId, testId);
    }
    
    @Override
    public List<StudentTestRecord> findOngoingTestRecords(Integer studentId) {
        return testRecordMapper.findOngoingTestRecords(studentId);
    }
    
    @Override
    public StudentTestRecord startTest(Integer studentId, Integer testId) {
        StudentTestRecord record = new StudentTestRecord();
        record.setStudentId(studentId);
        record.setTestId(testId);
        record.setTestName("测试_" + new Date().getTime());
        record.setStartTime(new Date());
        record.setTestStatus(1); // 进行中
        record.setCreateAt(new Date());
        record.setUpdateAt(new Date());
        record.setDeleteFlag("N");
        
        int result = testRecordMapper.insertTestRecord(record);
        return result > 0 ? record : null;
    }
    
    @Override
    public StudentTestRecord submitTest(Integer testRecordId) {
        StudentTestRecord record = new StudentTestRecord();
        record.setId(testRecordId);
        record.setEndTime(new Date());
        record.setSubmitTime(new Date());
        record.setTestStatus(2); // 已完成
        record.setUpdateAt(new Date());
        
        // 这里应该计算得分等统计信息
        // 暂时设置默认值，实际应该从答题详情中计算
        
        int result = testRecordMapper.updateTestRecord(record);
        return result > 0 ? record : null;
    }
    
    @Override
    public StudentTestRecord updateTestRecord(StudentTestRecord testRecord) {
        testRecord.setUpdateAt(new Date());
        
        int result = testRecordMapper.updateTestRecord(testRecord);
        return result > 0 ? testRecord : null;
    }
    
    @Override
    public boolean deleteTestRecord(Integer id) {
        int result = testRecordMapper.deleteTestRecord(id);
        return result > 0;
    }
    
    @Override
    public List<StudentTestRecord> findTestRecordsByStudentIdAndKnowledgePointIds(Integer studentId, List<Integer> knowledgePointIds) {
        List<StudentTestRecord> records = testRecordMapper.findTestRecordsByStudentIdAndKnowledgePointIds(studentId, knowledgePointIds);
        if (records == null || records.isEmpty() || knowledgePointIds == null || knowledgePointIds.isEmpty()) {
            return records;
        }
        List<Integer> recordIds = records.stream()
                .map(StudentTestRecord::getId)
                .filter(id -> id != null)
                .collect(Collectors.toList());
        if (recordIds.isEmpty()) {
            return records;
        }
        List<TestRecordKnowledgeScore> kpScores = testRecordMapper.findKnowledgePointScoresForTestRecords(
                studentId, knowledgePointIds, recordIds);
        if (kpScores == null || kpScores.isEmpty()) {
            return records;
        }
        Map<Integer, TestRecordKnowledgeScore> scoreMap = kpScores.stream()
                .filter(s -> s.getTestRecordId() != null)
                .collect(Collectors.toMap(TestRecordKnowledgeScore::getTestRecordId, Function.identity(), (a, b) -> a));
        for (StudentTestRecord r : records) {
            TestRecordKnowledgeScore s = scoreMap.get(r.getId());
            if (s == null) {
                continue;
            }
            r.setKnowledgePointEarnedPoints(s.getEarnedPoints());
            r.setKnowledgePointTotalPoints(s.getTotalPoints());
            Integer total = s.getTotalPoints();
            Integer earned = s.getEarnedPoints();
            if (total != null && total > 0 && earned != null) {
                r.setKnowledgePointScorePercentage(new BigDecimal(earned)
                        .divide(new BigDecimal(total), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(100)));
            }
        }
        return records;
    }
    
    @Override
    public List<StudentTestRecord> findTestRecordsByCategoryAndDifficulty(Integer studentId, Integer categoryId, Integer difficultyLevel) {
        return testRecordMapper.findTestRecordsByCategoryAndDifficulty(studentId, categoryId, difficultyLevel);
    }
}


