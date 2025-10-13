package com.yy.my_tutor.test.mapper;

import com.yy.my_tutor.test.domain.TestAnalysisReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 测试分析报告Mapper接口
 */
@Mapper
public interface TestAnalysisReportMapper {
    
    /**
     * 查询学生的分析报告
     */
    List<TestAnalysisReport> findReportsByStudentId(@Param("studentId") Integer studentId);
    
    /**
     * 根据测试记录ID查询报告
     */
    List<TestAnalysisReport> findReportsByTestRecordId(@Param("testRecordId") Integer testRecordId);
    
    /**
     * 根据测试记录ID和报告类型查询报告
     */
    TestAnalysisReport findReportByRecordAndType(@Param("testRecordId") Integer testRecordId, @Param("reportType") Integer reportType);
    
    /**
     * 插入分析报告
     */
    int insertAnalysisReport(TestAnalysisReport report);
    
    /**
     * 更新分析报告
     */
    int updateAnalysisReport(TestAnalysisReport report);
    
    /**
     * 更新下载次数
     */
    int updateDownloadCount(@Param("id") Integer id);
    
    /**
     * 删除分析报告（逻辑删除）
     */
    int deleteAnalysisReport(@Param("id") Integer id);
}


