package com.yy.my_tutor.test.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.test.domain.TestAnalysisReport;
import com.yy.my_tutor.test.service.TestAnalysisReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 测试分析报告控制器
 */
@RestController
@RequestMapping("/api/test-report")
@CrossOrigin(origins = "*")
public class TestAnalysisReportController {
    
    @Autowired
    private TestAnalysisReportService reportService;
    
    /**
     * 查询学生的分析报告
     */
    @GetMapping("/student/{studentId}")
    public RespResult<List<TestAnalysisReport>> findReportsByStudentId(@PathVariable Integer studentId) {
        List<TestAnalysisReport> reports = reportService.findReportsByStudentId(studentId);
        return RespResult.success(reports);
    }
    
    /**
     * 根据测试记录ID查询报告
     */
    @GetMapping("/record/{testRecordId}")
    public RespResult<List<TestAnalysisReport>> findReportsByTestRecordId(@PathVariable Integer testRecordId) {
        List<TestAnalysisReport> reports = reportService.findReportsByTestRecordId(testRecordId);
        return RespResult.success(reports);
    }
    
    /**
     * 根据测试记录ID和报告类型查询报告
     */
    @GetMapping("/record/{testRecordId}/type/{reportType}")
    public RespResult<TestAnalysisReport> findReportByRecordAndType(
            @PathVariable Integer testRecordId, @PathVariable Integer reportType) {
        TestAnalysisReport report = reportService.findReportByRecordAndType(testRecordId, reportType);
        if (report != null) {
            return RespResult.success(report);
        } else {
            return RespResult.error("分析报告不存在");
        }
    }
    
    /**
     * 生成分析报告
     */
    @PostMapping("/generate")
    public RespResult<TestAnalysisReport> generateAnalysisReport(@RequestParam Integer testRecordId,
                                                               @RequestParam Integer reportType) {
        TestAnalysisReport report = reportService.generateAnalysisReport(testRecordId, reportType);
        if (report != null) {
            return RespResult.success("分析报告生成成功", report);
        } else {
            return RespResult.error("分析报告生成失败");
        }
    }
    
    /**
     * 新增分析报告
     */
    @PostMapping("/add")
    public RespResult<TestAnalysisReport> addAnalysisReport(@RequestBody TestAnalysisReport report) {
        TestAnalysisReport result = reportService.addAnalysisReport(report);
        if (result != null) {
            return RespResult.success("分析报告添加成功", result);
        } else {
            return RespResult.error("分析报告添加失败");
        }
    }
    
    /**
     * 更新分析报告
     */
    @PutMapping("/update")
    public RespResult<TestAnalysisReport> updateAnalysisReport(@RequestBody TestAnalysisReport report) {
        TestAnalysisReport result = reportService.updateAnalysisReport(report);
        if (result != null) {
            return RespResult.success("分析报告更新成功", result);
        } else {
            return RespResult.error("分析报告更新失败");
        }
    }
    
    /**
     * 更新下载次数
     */
    @PutMapping("/download/{id}")
    public RespResult<String> updateDownloadCount(@PathVariable Integer id) {
        boolean result = reportService.updateDownloadCount(id);
        if (result) {
            return RespResult.success("下载次数更新成功");
        } else {
            return RespResult.error("下载次数更新失败");
        }
    }
    
    /**
     * 删除分析报告
     */
    @DeleteMapping("/{id}")
    public RespResult<String> deleteAnalysisReport(@PathVariable Integer id) {
        boolean result = reportService.deleteAnalysisReport(id);
        if (result) {
            return RespResult.success("分析报告删除成功");
        } else {
            return RespResult.error("分析报告删除失败");
        }
    }
}


