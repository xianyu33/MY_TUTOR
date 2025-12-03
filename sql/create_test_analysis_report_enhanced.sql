-- Enhanced Test Analysis Report Table
-- Add knowledge point analysis fields (strong, needs improvement, weak)

USE tutor;

-- Check if test_analysis_report table exists
-- If not exists, create it with enhanced fields for knowledge point analysis

CREATE TABLE IF NOT EXISTS `test_analysis_report` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '报告ID',
  `test_record_id` INT NOT NULL COMMENT '测试记录ID',
  `student_id` INT NOT NULL COMMENT '学生ID',
  `test_id` INT NOT NULL COMMENT '测试ID',
  `report_type` TINYINT DEFAULT 1 COMMENT '报告类型：1-分析报告，2-详细报告，3-评估报告',
  `report_title` VARCHAR(200) NOT NULL COMMENT '报告标题',
  `report_title_fr` VARCHAR(200) COMMENT '报告标题（法语）',
  `report_content` LONGTEXT COMMENT '报告内容（HTML/JSON格式）',
  `file_path` VARCHAR(500) COMMENT '文件路径',
  `file_size` BIGINT DEFAULT 0 COMMENT '文件大小（字节）',
  `download_count` INT DEFAULT 0 COMMENT '下载次数',
  `analysis_data` TEXT COMMENT '分析数据（JSON格式）',
  
  -- Enhanced fields for knowledge point analysis
  `strong_knowledge_points` TEXT COMMENT '掌握较好的知识点ID列表（JSON）',
  `needs_improvement_points` TEXT COMMENT '需要改进的知识点ID列表（JSON）',
  `weak_knowledge_points` TEXT COMMENT '薄弱知识点ID列表（JSON）',
  
  `overall_score` DECIMAL(5,2) DEFAULT 0.00 COMMENT '总体得分',
  `total_points` INT DEFAULT 0 COMMENT '总分',
  `earned_points` INT DEFAULT 0 COMMENT '获得分数',
  `accuracy_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '正确率（百分比）',
  
  `strong_points_summary` TEXT COMMENT '掌握较好的知识点摘要',
  `needs_improvement_summary` TEXT COMMENT '需要改进的知识点摘要',
  `weak_points_summary` TEXT COMMENT '薄弱知识点摘要',
  
  `recommendations` TEXT COMMENT '学习建议',
  `recommendations_fr` TEXT COMMENT '学习建议（法语）',
  
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `delete_flag` CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  
  CONSTRAINT `fk_tar_record` FOREIGN KEY (`test_record_id`) REFERENCES `student_test_record`(`id`),
  CONSTRAINT `fk_tar_student` FOREIGN KEY (`student_id`) REFERENCES `user`(`id`),
  CONSTRAINT `fk_tar_test` FOREIGN KEY (`test_id`) REFERENCES `test`(`id`),
  
  UNIQUE KEY `uk_record_type` (`test_record_id`, `report_type`),
  INDEX `idx_student_report` (`student_id`),
  INDEX `idx_report_type` (`report_type`),
  INDEX `idx_overall_score` (`overall_score`),
  INDEX `idx_accuracy_rate` (`accuracy_rate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试分析报告表（增强版）';

-- Add enhanced fields to existing table if it already exists
ALTER TABLE `test_analysis_report` 
  ADD COLUMN IF NOT EXISTS `report_title_fr` VARCHAR(200) COMMENT '报告标题（法语）';

ALTER TABLE `test_analysis_report` 
  ADD COLUMN IF NOT EXISTS `strong_knowledge_points` TEXT COMMENT '掌握较好的知识点ID列表（JSON）';

ALTER TABLE `test_analysis_report` 
  ADD COLUMN IF NOT EXISTS `needs_improvement_points` TEXT COMMENT '需要改进的知识点ID列表（JSON）';

ALTER TABLE `test_analysis_report` 
  ADD COLUMN IF NOT EXISTS `weak_knowledge_points` TEXT COMMENT '薄弱知识点ID列表（JSON）';

ALTER TABLE `test_analysis_report` 
  ADD COLUMN IF NOT EXISTS `overall_score` DECIMAL(5,2) DEFAULT 0.00 COMMENT '总体得分';

ALTER TABLE `test_analysis_report` 
  ADD COLUMN IF NOT EXISTS `total_points` INT DEFAULT 0 COMMENT '总分';

ALTER TABLE `test_analysis_report` 
  ADD COLUMN IF NOT EXISTS `earned_points` INT DEFAULT 0 COMMENT '获得分数';

ALTER TABLE `test_analysis_report` 
  ADD COLUMN IF NOT EXISTS `accuracy_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '正确率（百分比）';

ALTER TABLE `test_analysis_report` 
  ADD COLUMN IF NOT EXISTS `strong_points_summary` TEXT COMMENT '掌握较好的知识点摘要';

ALTER TABLE `test_analysis_report` 
  ADD COLUMN IF NOT EXISTS `needs_improvement_summary` TEXT COMMENT '需要改进的知识点摘要';

ALTER TABLE `test_analysis_report` 
  ADD COLUMN IF NOT EXISTS `weak_points_summary` TEXT COMMENT '薄弱知识点摘要';

ALTER TABLE `test_analysis_report` 
  ADD COLUMN IF NOT EXISTS `recommendations` TEXT COMMENT '学习建议';

ALTER TABLE `test_analysis_report` 
  ADD COLUMN IF NOT EXISTS `recommendations_fr` TEXT COMMENT '学习建议（法语）';

-- Create index for performance
CREATE INDEX IF NOT EXISTS `idx_overall_score` ON `test_analysis_report`(`overall_score`);
CREATE INDEX IF NOT EXISTS `idx_accuracy_rate` ON `test_analysis_report`(`accuracy_rate`);

-- Verification query
SELECT 
    COLUMN_NAME,
    COLUMN_TYPE,
    COLUMN_COMMENT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'tutor' 
    AND TABLE_NAME = 'test_analysis_report'
ORDER BY ORDINAL_POSITION;
