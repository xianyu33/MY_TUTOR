-- 学生测试业务相关表结构
-- 注意：执行前请备份数据库

-- 1. 测试表
CREATE TABLE IF NOT EXISTS `test` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '测试ID',
  `test_name` VARCHAR(200) NOT NULL COMMENT '测试名称',
  `grade_id` INT NOT NULL COMMENT '年级ID',
  `knowledge_point_ids` TEXT COMMENT '知识点ID列表，JSON格式',
  `total_questions` INT DEFAULT 0 COMMENT '总题目数',
  `total_points` INT DEFAULT 0 COMMENT '总分',
  `time_limit` INT DEFAULT 60 COMMENT '时间限制（分钟）',
  `difficulty_level` TINYINT DEFAULT 2 COMMENT '难度等级：1-简单，2-中等，3-困难',
  `test_type` TINYINT DEFAULT 1 COMMENT '测试类型：1-练习测试，2-正式考试，3-模拟考试',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1-启用，2-禁用',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `delete_flag` CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  CONSTRAINT `fk_test_grade` FOREIGN KEY (`grade_id`) REFERENCES `grade` (`id`),
  INDEX `idx_grade_test` (`grade_id`),
  INDEX `idx_test_type` (`test_type`),
  INDEX `idx_difficulty` (`difficulty_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试表';

-- 2. 测试题目关联表
CREATE TABLE IF NOT EXISTS `test_question` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
  `test_id` INT NOT NULL COMMENT '测试ID',
  `question_id` INT NOT NULL COMMENT '题目ID',
  `sort_order` INT DEFAULT 0 COMMENT '题目顺序',
  `points` INT DEFAULT 1 COMMENT '分值',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  CONSTRAINT `fk_tq_test` FOREIGN KEY (`test_id`) REFERENCES `test` (`id`),
  CONSTRAINT `fk_tq_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`),
  UNIQUE KEY `uk_test_question` (`test_id`, `question_id`),
  INDEX `idx_test_order` (`test_id`, `sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试题目关联表';

-- 3. 学生测试记录表
CREATE TABLE IF NOT EXISTS `student_test_record` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
  `student_id` INT NOT NULL COMMENT '学生ID',
  `test_id` INT NOT NULL COMMENT '测试ID',
  `test_name` VARCHAR(200) NOT NULL COMMENT '测试名称（冗余）',
  `start_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` DATETIME COMMENT '结束时间',
  `submit_time` DATETIME COMMENT '提交时间',
  `time_spent` INT DEFAULT 0 COMMENT '用时（分钟）',
  `total_questions` INT DEFAULT 0 COMMENT '总题目数',
  `answered_questions` INT DEFAULT 0 COMMENT '已答题目数',
  `correct_answers` INT DEFAULT 0 COMMENT '正确答题数',
  `total_points` INT DEFAULT 0 COMMENT '总分',
  `earned_points` INT DEFAULT 0 COMMENT '得分',
  `score_percentage` DECIMAL(5,2) DEFAULT 0.00 COMMENT '得分率',
  `test_status` TINYINT DEFAULT 1 COMMENT '测试状态：1-进行中，2-已完成，3-已超时',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `delete_flag` CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  CONSTRAINT `fk_str_student` FOREIGN KEY (`student_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_str_test` FOREIGN KEY (`test_id`) REFERENCES `test` (`id`),
  INDEX `idx_student_test` (`student_id`, `test_id`),
  INDEX `idx_test_status` (`test_status`),
  INDEX `idx_submit_time` (`submit_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生测试记录表';

-- 4. 学生测试答题详情表
CREATE TABLE IF NOT EXISTS `student_test_answer` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '答题详情ID',
  `test_record_id` INT NOT NULL COMMENT '测试记录ID',
  `student_id` INT NOT NULL COMMENT '学生ID',
  `question_id` INT NOT NULL COMMENT '题目ID',
  `question_content` TEXT NOT NULL COMMENT '题目内容（冗余）',
  `correct_answer` TEXT NOT NULL COMMENT '正确答案（冗余）',
  `student_answer` TEXT COMMENT '学生答案',
  `is_correct` TINYINT DEFAULT 0 COMMENT '是否正确：0-错误，1-正确',
  `points` INT DEFAULT 0 COMMENT '题目分值',
  `earned_points` INT DEFAULT 0 COMMENT '获得分值',
  `answer_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '答题时间',
  `time_spent` INT DEFAULT 0 COMMENT '答题用时（秒）',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  CONSTRAINT `fk_sta_record` FOREIGN KEY (`test_record_id`) REFERENCES `student_test_record` (`id`),
  CONSTRAINT `fk_sta_student` FOREIGN KEY (`student_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_sta_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`),
  INDEX `idx_record_question` (`test_record_id`, `question_id`),
  INDEX `idx_student_answer` (`student_id`, `question_id`),
  INDEX `idx_is_correct` (`is_correct`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生测试答题详情表';

-- 5. 测试分析报告表
CREATE TABLE IF NOT EXISTS `test_analysis_report` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '报告ID',
  `test_record_id` INT NOT NULL COMMENT '测试记录ID',
  `student_id` INT NOT NULL COMMENT '学生ID',
  `test_id` INT NOT NULL COMMENT '测试ID',
  `report_type` TINYINT DEFAULT 1 COMMENT '报告类型：1-PDF，2-WORD，3-EXCEL',
  `report_title` VARCHAR(200) NOT NULL COMMENT '报告标题',
  `report_content` LONGTEXT COMMENT '报告内容（HTML/JSON格式）',
  `file_path` VARCHAR(500) COMMENT '文件路径',
  `file_size` BIGINT DEFAULT 0 COMMENT '文件大小（字节）',
  `download_count` INT DEFAULT 0 COMMENT '下载次数',
  `analysis_data` TEXT COMMENT '分析数据（JSON格式）',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `delete_flag` CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  CONSTRAINT `fk_tar_record` FOREIGN KEY (`test_record_id`) REFERENCES `student_test_record` (`id`),
  CONSTRAINT `fk_tar_student` FOREIGN KEY (`student_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_tar_test` FOREIGN KEY (`test_id`) REFERENCES `test` (`id`),
  UNIQUE KEY `uk_record_type` (`test_record_id`, `report_type`),
  INDEX `idx_student_report` (`student_id`),
  INDEX `idx_report_type` (`report_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试分析报告表';

-- 6. 知识点掌握情况表（基于测试结果更新）
CREATE TABLE IF NOT EXISTS `knowledge_mastery` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '掌握情况ID',
  `student_id` INT NOT NULL COMMENT '学生ID',
  `knowledge_point_id` INT NOT NULL COMMENT '知识点ID',
  `total_tests` INT DEFAULT 0 COMMENT '总测试次数',
  `total_questions` INT DEFAULT 0 COMMENT '总答题数',
  `correct_answers` INT DEFAULT 0 COMMENT '正确答题数',
  `accuracy_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '正确率',
  `mastery_level` TINYINT DEFAULT 1 COMMENT '掌握程度：1-未掌握，2-基本掌握，3-熟练掌握',
  `last_test_time` DATETIME COMMENT '最后测试时间',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `delete_flag` CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  CONSTRAINT `fk_km_student` FOREIGN KEY (`student_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_km_knowledge` FOREIGN KEY (`knowledge_point_id`) REFERENCES `knowledge_point` (`id`),
  UNIQUE KEY `uk_student_knowledge` (`student_id`, `knowledge_point_id`),
  INDEX `idx_mastery_level` (`mastery_level`),
  INDEX `idx_accuracy_rate` (`accuracy_rate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识点掌握情况表';

-- 插入示例测试数据
INSERT INTO test (test_name, grade_id, knowledge_point_ids, total_questions, total_points, time_limit, difficulty_level, test_type, status) VALUES
('一年级数学基础测试', 1, '[1,2]', 10, 10, 30, 1, 1, 1),
('二年级数学综合测试', 2, '[4,5]', 15, 15, 45, 2, 1, 1),
('三年级数学进阶测试', 3, '[6,7,8]', 20, 20, 60, 3, 2, 1)
ON DUPLICATE KEY UPDATE test_name=VALUES(test_name);
