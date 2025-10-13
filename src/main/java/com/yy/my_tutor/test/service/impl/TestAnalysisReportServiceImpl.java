package com.yy.my_tutor.test.service.impl;

import com.yy.my_tutor.test.domain.TestAnalysisReport;
import com.yy.my_tutor.test.mapper.TestAnalysisReportMapper;
import com.yy.my_tutor.test.service.TestAnalysisReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 测试分析报告服务实现类
 */
@Service
public class TestAnalysisReportServiceImpl implements TestAnalysisReportService {
    
    @Autowired
    private TestAnalysisReportMapper reportMapper;
    
    @Override
    public List<TestAnalysisReport> findReportsByStudentId(Integer studentId) {
        return reportMapper.findReportsByStudentId(studentId);
    }
    
    @Override
    public List<TestAnalysisReport> findReportsByTestRecordId(Integer testRecordId) {
        return reportMapper.findReportsByTestRecordId(testRecordId);
    }
    
    @Override
    public TestAnalysisReport findReportByRecordAndType(Integer testRecordId, Integer reportType) {
        return reportMapper.findReportByRecordAndType(testRecordId, reportType);
    }
    
    @Override
    public TestAnalysisReport generateAnalysisReport(Integer testRecordId, Integer reportType) {
        TestAnalysisReport report = new TestAnalysisReport();
        report.setTestRecordId(testRecordId);
        report.setReportType(reportType);
        report.setReportTitle("测试分析报告_" + new Date().getTime());
        report.setCreateAt(new Date());
        report.setUpdateAt(new Date());
        report.setDeleteFlag("N");
        
        // 这里应该实现具体的报告生成逻辑
        // 暂时设置默认值
        
        int result = reportMapper.insertAnalysisReport(report);
        return result > 0 ? report : null;
    }
    
    @Override
    public TestAnalysisReport addAnalysisReport(TestAnalysisReport report) {
        Date now = new Date();
        report.setCreateAt(now);
        report.setUpdateAt(now);
        report.setDeleteFlag("N");
        
        int result = reportMapper.insertAnalysisReport(report);
        return result > 0 ? report : null;
    }
    
    @Override
    public TestAnalysisReport updateAnalysisReport(TestAnalysisReport report) {
        report.setUpdateAt(new Date());
        
        int result = reportMapper.updateAnalysisReport(report);
        return result > 0 ? report : null;
    }
    
    @Override
    public boolean updateDownloadCount(Integer id) {
        int result = reportMapper.updateDownloadCount(id);
        return result > 0;
    }
    
    @Override
    public boolean deleteAnalysisReport(Integer id) {
        int result = reportMapper.deleteAnalysisReport(id);
        return result > 0;
    }
}
