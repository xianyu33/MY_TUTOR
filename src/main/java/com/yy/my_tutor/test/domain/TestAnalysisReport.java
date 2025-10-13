package com.yy.my_tutor.test.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 测试分析报告实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestAnalysisReport implements Serializable {
    private Integer id;
    private Integer testRecordId;
    private Integer studentId;
    private Integer testId;
    private Integer reportType;
    private String reportTitle;
    private String reportContent;
    private String filePath;
    private Long fileSize;
    private Integer downloadCount;
    private String analysisData;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
    private String updateBy;
    private String deleteFlag;
    
    // 关联对象
    private StudentTestRecord testRecord;
}


