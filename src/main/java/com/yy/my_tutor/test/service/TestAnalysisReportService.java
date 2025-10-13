package com.yy.my_tutor.test.service;

import com.yy.my_tutor.test.domain.TestAnalysisReport;

import java.util.List;

/**
 * 测试分析报告服务接口
 */
public interface TestAnalysisReportService {
    
    /**
     * 查询学生的分析报告
     */
    List<TestAnalysisReport> findReportsByStudentId(Integer studentId);
    
    /**
     * 根据测试记录ID查询报告
     */
    List<TestAnalysisReport> findReportsByTestRecordId(Integer testRecordId);
    
    /**
     * 根据测试记录ID和报告类型查询报告
     */
    TestAnalysisReport findReportByRecordAndType(Integer testRecordId, Integer reportType);
    
    /**
     * 生成分析报告
     */
    TestAnalysisReport generateAnalysisReport(Integer testRecordId, Integer reportType);
    
    /**
     * 新增分析报告
     */
    TestAnalysisReport addAnalysisReport(TestAnalysisReport report);
    
    /**
     * 更新分析报告
     */
    TestAnalysisReport updateAnalysisReport(TestAnalysisReport report);
    
    /**
     * 更新下载次数
     */
    boolean updateDownloadCount(Integer id);
    
    /**
     * 删除分析报告
     */
    boolean deleteAnalysisReport(Integer id);
}


