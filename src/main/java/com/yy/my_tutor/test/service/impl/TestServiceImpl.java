package com.yy.my_tutor.test.service.impl;

import com.yy.my_tutor.math.domain.Question;
import com.yy.my_tutor.math.mapper.QuestionMapper;
import com.yy.my_tutor.test.domain.Test;
import com.yy.my_tutor.test.mapper.TestMapper;
import com.yy.my_tutor.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 测试服务实现类
 */
@Service
public class TestServiceImpl implements TestService {
    
    @Autowired
    private TestMapper testMapper;
    
    @Autowired
    private QuestionMapper questionMapper;
    
    @Override
    public List<Test> findAllTests() {
        return testMapper.findAllTests();
    }
    
    @Override
    public Test findTestById(Integer id) {
        return testMapper.findTestById(id);
    }
    
    @Override
    public List<Test> findTestsByGradeId(Integer gradeId) {
        return testMapper.findTestsByGradeId(gradeId);
    }
    
    @Override
    public List<Test> findTestsByGradeAndDifficulty(Integer gradeId, Integer difficultyLevel) {
        return testMapper.findTestsByGradeAndDifficulty(gradeId, difficultyLevel);
    }
    
    @Override
    public List<Test> findTestsByType(Integer testType) {
        return testMapper.findTestsByType(testType);
    }
    
    @Override
    public Test generateTest(Integer gradeId, Integer difficultyLevel, Integer questionCount) {
        // 根据年级和难度随机获取题目
        List<Question> questions = questionMapper.findRandomQuestions(null, questionCount);
        
        Test test = new Test();
        test.setTestName("自动生成测试_" + new Date().getTime());
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
        
        int result = testMapper.insertTest(test);
        return result > 0 ? test : null;
    }
    
    @Override
    public Test addTest(Test test) {
        Date now = new Date();
        test.setCreateAt(now);
        test.setUpdateAt(now);
        test.setDeleteFlag("N");
        
        int result = testMapper.insertTest(test);
        return result > 0 ? test : null;
    }
    
    @Override
    public Test updateTest(Test test) {
        test.setUpdateAt(new Date());
        
        int result = testMapper.updateTest(test);
        return result > 0 ? test : null;
    }
    
    @Override
    public boolean deleteTest(Integer id) {
        int result = testMapper.deleteTest(id);
        return result > 0;
    }
}


