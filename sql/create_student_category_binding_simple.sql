-- =============================================
-- 学生知识点分类绑定关系表创建脚本 - 简化版
-- =============================================

USE tutor;

-- 创建学生知识点分类绑定关系表
CREATE TABLE IF NOT EXISTS `student_category_binding` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '绑定关系ID',
  `student_id` INT NOT NULL COMMENT '学生ID',
  `category_id` INT NOT NULL COMMENT '知识点分类ID',
  `grade_id` INT NOT NULL COMMENT '年级ID',
  `binding_status` TINYINT DEFAULT 1 COMMENT '绑定状态：1-已绑定，2-学习中，3-已完成',
  `overall_progress` DECIMAL(5,2) DEFAULT 0.00 COMMENT '整体学习进度百分比',
  `total_knowledge_points` INT DEFAULT 0 COMMENT '该分类下总知识点数量',
  `completed_knowledge_points` INT DEFAULT 0 COMMENT '已完成的知识点数量',
  `in_progress_knowledge_points` INT DEFAULT 0 COMMENT '学习中的知识点数量',
  `not_started_knowledge_points` INT DEFAULT 0 COMMENT '未开始的知识点数量',
  `total_study_duration` INT DEFAULT 0 COMMENT '总学习时长（分钟）',
  `last_study_time` DATETIME COMMENT '最后学习时间',
  `start_time` DATETIME COMMENT '开始学习时间',
  `complete_time` DATETIME COMMENT '完成时间',
  `notes` TEXT COMMENT '学习笔记',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `delete_flag` CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  CONSTRAINT `fk_binding_student` FOREIGN KEY (`student_id`) REFERENCES `user`(`id`),
  CONSTRAINT `fk_binding_category` FOREIGN KEY (`category_id`) REFERENCES `knowledge_category`(`id`),
  CONSTRAINT `fk_binding_grade` FOREIGN KEY (`grade_id`) REFERENCES `grade`(`id`),
  UNIQUE KEY `uk_student_category` (`student_id`, `category_id`),
  INDEX `idx_student_grade` (`student_id`, `grade_id`),
  INDEX `idx_binding_status` (`binding_status`),
  INDEX `idx_overall_progress` (`overall_progress`),
  INDEX `idx_last_study_time` (`last_study_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生知识点分类绑定关系表';
