-- =============================================
-- MY_TUTOR 完整数据库建表语句
-- 包含：用户管理、年级管理、知识点管理、测试系统、学习进度等
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS tutor CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tutor;

-- =============================================
-- 1. 用户相关表
-- =============================================

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
  `user_account` VARCHAR(50) NOT NULL COMMENT '用户账号',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `sex` CHAR(1) COMMENT '性别：1-男，2-女',
  `age` INT COMMENT '年龄',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（MD5加密）',
  `tel` VARCHAR(20) COMMENT '电话',
  `country` VARCHAR(10) DEFAULT 'CHN' COMMENT '国家',
  `email` VARCHAR(100) COMMENT '邮箱',
  `grade` VARCHAR(10) COMMENT '年级',
  `role` CHAR(1) DEFAULT 'S' COMMENT '角色：P-家长，S-学生，T-老师',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `delete_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志：0-正常，1-删除',
  UNIQUE KEY `uk_user_account` (`user_account`),
  INDEX `idx_email` (`email`),
  INDEX `idx_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 家长/老师表
CREATE TABLE IF NOT EXISTS `parent` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '家长/老师ID',
  `user_account` VARCHAR(50) NOT NULL COMMENT '用户账号',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `sex` CHAR(1) COMMENT '性别：1-男，2-女',
  `age` INT COMMENT '年龄',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（MD5加密）',
  `tel` VARCHAR(20) COMMENT '电话',
  `country` VARCHAR(10) DEFAULT 'CHN' COMMENT '国家',
  `email` VARCHAR(100) COMMENT '邮箱',
  `grade` VARCHAR(10) COMMENT '年级',
  `type` TINYINT DEFAULT 0 COMMENT '类型：0-家长，1-老师',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `delete_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志：0-正常，1-删除',
  UNIQUE KEY `uk_parent_account` (`user_account`),
  INDEX `idx_email` (`email`),
  INDEX `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家长/老师表';

-- 家长/老师 与 学生 关联表
CREATE TABLE IF NOT EXISTS `guardian_student_rel` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `guardian_id` INT NOT NULL COMMENT '家长/老师ID（关联 parent.id）',
  `guardian_type` TINYINT(1) NOT NULL COMMENT '0-家长 1-老师（冗余）',
  `student_id` INT NOT NULL COMMENT '学生ID（关联 user.id）',
  `relation` VARCHAR(50) DEFAULT NULL COMMENT '关系：父亲/母亲/监护人/班主任/任课老师等',
  `start_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '关系开始时间',
  `end_at` DATETIME DEFAULT NULL COMMENT '关系结束时间（空表示有效）',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `create_by` VARCHAR(255) DEFAULT NULL,
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` VARCHAR(255) DEFAULT NULL,
  `delete_flag` CHAR(1) DEFAULT '0' COMMENT '0-正常 1-删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_guardian_student_active` (`guardian_id`,`student_id`,`delete_flag`),
  KEY `idx_guardian_type` (`guardian_type`),
  KEY `idx_student_id` (`student_id`),
  CONSTRAINT `fk_gsr_guardian` FOREIGN KEY (`guardian_id`) REFERENCES `parent` (`id`),
  CONSTRAINT `fk_gsr_student` FOREIGN KEY (`student_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家长/老师 与 学生 关联表';

-- =============================================
-- 2. 年级和知识点相关表
-- =============================================

-- 年级表
CREATE TABLE IF NOT EXISTS `grade` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '年级ID',
  `grade_name` VARCHAR(20) NOT NULL COMMENT '年级名称，如：一年级、二年级等',
  `grade_level` INT NOT NULL COMMENT '年级等级，1-12',
  `description` TEXT COMMENT '年级描述',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `delete_flag` CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  UNIQUE KEY `uk_grade_level` (`grade_level`),
  INDEX `idx_grade_name` (`grade_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='年级表';

-- 知识点分类表
CREATE TABLE IF NOT EXISTS `knowledge_category` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
  `category_name` VARCHAR(50) NOT NULL COMMENT '分类名称，如：数与代数、几何、统计与概率等',
  `category_name_fr` VARCHAR(50) COMMENT '分类名称（法语）',
  `category_code` VARCHAR(20) NOT NULL COMMENT '分类编码',
  `description` TEXT COMMENT '分类描述',
  `description_fr` TEXT COMMENT '分类描述（法语）',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `delete_flag` CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  UNIQUE KEY `uk_category_code` (`category_code`),
  INDEX `idx_category_name` (`category_name`),
  INDEX `idx_category_name_fr` (`category_name_fr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识点分类表';

-- 知识点表
CREATE TABLE IF NOT EXISTS `knowledge_point` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '知识点ID',
  `grade_id` INT NOT NULL COMMENT '年级ID',
  `category_id` INT NOT NULL COMMENT '分类ID',
  `point_name` VARCHAR(100) NOT NULL COMMENT '知识点名称',
  `point_code` VARCHAR(50) NOT NULL COMMENT '知识点编码',
  `description` TEXT COMMENT '知识点描述',
  `content` TEXT NOT NULL COMMENT '知识点内容详情',
  `difficulty_level` TINYINT DEFAULT 1 COMMENT '难度等级：1-简单，2-中等，3-困难',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `prerequisite_points` TEXT COMMENT '前置知识点ID列表，JSON格式',
  `learning_objectives` TEXT COMMENT '学习目标',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `delete_flag` CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  CONSTRAINT `fk_kp_grade` FOREIGN KEY (`grade_id`) REFERENCES `grade`(`id`),
  CONSTRAINT `fk_kp_category` FOREIGN KEY (`category_id`) REFERENCES `knowledge_category`(`id`),
  UNIQUE KEY `uk_point_code` (`point_code`),
  INDEX `idx_grade_category` (`grade_id`, `category_id`),
  INDEX `idx_point_name` (`point_name`),
  INDEX `idx_kp_difficulty` (`difficulty_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识点表';

-- 题目表
CREATE TABLE IF NOT EXISTS `question` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '题目ID',
  `knowledge_point_id` INT NOT NULL COMMENT '知识点ID',
  `question_type` TINYINT DEFAULT 1 COMMENT '题目类型：1-选择题，2-填空题，3-解答题',
  `question_title` VARCHAR(200) COMMENT '题目标题',
  `question_content` TEXT NOT NULL COMMENT '题目内容',
  `options` TEXT COMMENT '选项（JSON格式）',
  `correct_answer` TEXT NOT NULL COMMENT '正确答案',
  `answer_explanation` TEXT COMMENT '答案解释',
  `difficulty_level` TINYINT DEFAULT 1 COMMENT '难度等级：1-简单，2-中等，3-困难',
  `points` INT DEFAULT 1 COMMENT '分值',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `delete_flag` CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  CONSTRAINT `fk_question_kp` FOREIGN KEY (`knowledge_point_id`) REFERENCES `knowledge_point`(`id`),
  INDEX `idx_kp_type` (`knowledge_point_id`, `question_type`),
  INDEX `idx_difficulty` (`difficulty_level`),
  INDEX `idx_points` (`points`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题目表';

-- =============================================
-- 3. 学习进度相关表
-- =============================================

-- 学习进度表
CREATE TABLE IF NOT EXISTS `learning_progress` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '进度ID',
  `user_id` INT NOT NULL COMMENT '学生用户ID',
  `knowledge_point_id` INT NOT NULL COMMENT '知识点ID',
  `progress_status` TINYINT DEFAULT 1 COMMENT '学习状态：1-未开始，2-学习中，3-已完成',
  `completion_percentage` DECIMAL(5,2) DEFAULT 0.00 COMMENT '完成百分比',
  `start_time` DATETIME COMMENT '开始学习时间',
  `complete_time` DATETIME COMMENT '完成时间',
  `study_duration` INT DEFAULT 0 COMMENT '学习时长（分钟）',
  `last_study_time` DATETIME COMMENT '最后学习时间',
  `notes` TEXT COMMENT '学习笔记',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `delete_flag` CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  CONSTRAINT `fk_lp_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  CONSTRAINT `fk_lp_kp` FOREIGN KEY (`knowledge_point_id`) REFERENCES `knowledge_point`(`id`),
  UNIQUE KEY `uk_user_knowledge` (`user_id`, `knowledge_point_id`),
  INDEX `idx_user_progress` (`user_id`, `progress_status`),
  INDEX `idx_knowledge_progress` (`knowledge_point_id`, `progress_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习进度表';

-- 学习内容记录表
CREATE TABLE IF NOT EXISTS `learning_content` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
  `user_id` INT NOT NULL COMMENT '学生用户ID',
  `knowledge_point_id` INT NOT NULL COMMENT '知识点ID',
  `content_type` TINYINT NOT NULL COMMENT '内容类型：1-视频，2-文档，3-练习，4-测试',
  `content_title` VARCHAR(200) NOT NULL COMMENT '内容标题',
  `content_url` VARCHAR(500) COMMENT '内容链接或路径',
  `content_data` TEXT COMMENT '内容数据（JSON格式）',
  `study_time` INT DEFAULT 0 COMMENT '学习时长（分钟）',
  `completion_status` TINYINT DEFAULT 1 COMMENT '完成状态：1-未完成，2-已完成',
  `score` DECIMAL(5,2) COMMENT '得分',
  `feedback` TEXT COMMENT '学习反馈',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `delete_flag` CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  CONSTRAINT `fk_lc_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  CONSTRAINT `fk_lc_kp` FOREIGN KEY (`knowledge_point_id`) REFERENCES `knowledge_point`(`id`),
  INDEX `idx_user_content` (`user_id`, `content_type`),
  INDEX `idx_completion_status` (`completion_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习内容记录表';

-- 学习统计表
CREATE TABLE IF NOT EXISTS `learning_statistics` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '统计ID',
  `user_id` INT NOT NULL COMMENT '学生用户ID',
  `statistics_date` DATE NOT NULL COMMENT '统计日期',
  `total_study_time` INT DEFAULT 0 COMMENT '总学习时长（分钟）',
  `completed_knowledge_points` INT DEFAULT 0 COMMENT '已完成知识点数',
  `total_tests` INT DEFAULT 0 COMMENT '总测试次数',
  `average_score` DECIMAL(5,2) DEFAULT 0.00 COMMENT '平均得分',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `delete_flag` CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  CONSTRAINT `fk_ls_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  UNIQUE KEY `uk_user_date` (`user_id`, `statistics_date`),
  INDEX `idx_statistics_date` (`statistics_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习统计表';

-- =============================================
-- 4. 测试系统相关表
-- =============================================

-- 测试表
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
  CONSTRAINT `fk_test_grade` FOREIGN KEY (`grade_id`) REFERENCES `grade`(`id`),
  INDEX `idx_grade_test` (`grade_id`),
  INDEX `idx_test_type` (`test_type`),
  INDEX `idx_difficulty` (`difficulty_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试表';

-- 测试题目关联表
CREATE TABLE IF NOT EXISTS `test_question` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
  `test_id` INT NOT NULL COMMENT '测试ID',
  `question_id` INT NOT NULL COMMENT '题目ID',
  `sort_order` INT DEFAULT 0 COMMENT '题目顺序',
  `points` INT DEFAULT 1 COMMENT '分值',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  CONSTRAINT `fk_tq_test` FOREIGN KEY (`test_id`) REFERENCES `test`(`id`),
  CONSTRAINT `fk_tq_question` FOREIGN KEY (`question_id`) REFERENCES `question`(`id`),
  UNIQUE KEY `uk_test_question` (`test_id`, `question_id`),
  INDEX `idx_test_order` (`test_id`, `sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试题目关联表';

-- 学生测试记录表
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
  CONSTRAINT `fk_str_student` FOREIGN KEY (`student_id`) REFERENCES `user`(`id`),
  CONSTRAINT `fk_str_test` FOREIGN KEY (`test_id`) REFERENCES `test`(`id`),
  INDEX `idx_student_test` (`student_id`, `test_id`),
  INDEX `idx_test_status` (`test_status`),
  INDEX `idx_submit_time` (`submit_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生测试记录表';

-- 学生测试答题详情表
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
  CONSTRAINT `fk_sta_record` FOREIGN KEY (`test_record_id`) REFERENCES `student_test_record`(`id`),
  CONSTRAINT `fk_sta_student` FOREIGN KEY (`student_id`) REFERENCES `user`(`id`),
  CONSTRAINT `fk_sta_question` FOREIGN KEY (`question_id`) REFERENCES `question`(`id`),
  INDEX `idx_record_question` (`test_record_id`, `question_id`),
  INDEX `idx_student_answer` (`student_id`, `question_id`),
  INDEX `idx_is_correct` (`is_correct`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生测试答题详情表';

-- 测试分析报告表
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
  CONSTRAINT `fk_tar_record` FOREIGN KEY (`test_record_id`) REFERENCES `student_test_record`(`id`),
  CONSTRAINT `fk_tar_student` FOREIGN KEY (`student_id`) REFERENCES `user`(`id`),
  CONSTRAINT `fk_tar_test` FOREIGN KEY (`test_id`) REFERENCES `test`(`id`),
  UNIQUE KEY `uk_record_type` (`test_record_id`, `report_type`),
  INDEX `idx_student_report` (`student_id`),
  INDEX `idx_report_type` (`report_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试分析报告表';

-- 知识点掌握情况表（基于测试结果更新）
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
  CONSTRAINT `fk_km_student` FOREIGN KEY (`student_id`) REFERENCES `user`(`id`),
  CONSTRAINT `fk_km_knowledge` FOREIGN KEY (`knowledge_point_id`) REFERENCES `knowledge_point`(`id`),
  UNIQUE KEY `uk_student_knowledge` (`student_id`, `knowledge_point_id`),
  INDEX `idx_mastery_level` (`mastery_level`),
  INDEX `idx_accuracy_rate` (`accuracy_rate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识点掌握情况表';

-- =============================================
-- 5. 聊天系统相关表（如果需要）
-- =============================================

-- 聊天消息表
CREATE TABLE IF NOT EXISTS `chat_message` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '消息ID',
  `sender_id` INT NOT NULL COMMENT '发送者ID',
  `receiver_id` INT NOT NULL COMMENT '接收者ID',
  `message_type` TINYINT DEFAULT 1 COMMENT '消息类型：1-文本，2-图片，3-文件',
  `message_content` TEXT NOT NULL COMMENT '消息内容',
  `message_status` TINYINT DEFAULT 1 COMMENT '消息状态：1-已发送，2-已读，3-已删除',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `delete_flag` CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  CONSTRAINT `fk_cm_sender` FOREIGN KEY (`sender_id`) REFERENCES `user`(`id`),
  CONSTRAINT `fk_cm_receiver` FOREIGN KEY (`receiver_id`) REFERENCES `user`(`id`),
  INDEX `idx_sender_receiver` (`sender_id`, `receiver_id`),
  INDEX `idx_message_status` (`message_status`),
  INDEX `idx_create_at` (`create_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天消息表';

-- 聊天消息详情表
CREATE TABLE IF NOT EXISTS `chat_message_detail` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '详情ID',
  `message_id` INT NOT NULL COMMENT '消息ID',
  `detail_type` TINYINT DEFAULT 1 COMMENT '详情类型：1-文本，2-图片，3-文件',
  `detail_content` TEXT COMMENT '详情内容',
  `file_path` VARCHAR(500) COMMENT '文件路径',
  `file_size` BIGINT DEFAULT 0 COMMENT '文件大小（字节）',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  CONSTRAINT `fk_cmd_message` FOREIGN KEY (`message_id`) REFERENCES `chat_message`(`id`),
  INDEX `idx_message_detail` (`message_id`, `detail_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天消息详情表';

-- =============================================
-- 6. 基础数据插入
-- =============================================

-- 插入年级基础数据
INSERT INTO `grade` (`grade_name`, `grade_level`, `description`) VALUES
('一年级', 1, '小学一年级'),
('二年级', 2, '小学二年级'),
('三年级', 3, '小学三年级'),
('四年级', 4, '小学四年级'),
('五年级', 5, '小学五年级'),
('六年级', 6, '小学六年级'),
('七年级', 7, '初中一年级'),
('八年级', 8, '初中二年级'),
('九年级', 9, '初中三年级'),
('高一', 10, '高中一年级'),
('高二', 11, '高中二年级'),
('高三', 12, '高中三年级')
ON DUPLICATE KEY UPDATE `description`=VALUES(`description`);

-- 插入知识点分类基础数据
INSERT INTO `knowledge_category` (`category_name`, `category_code`, `description`, `sort_order`) VALUES
('数与代数', 'NUM_ALG', '数的认识、运算、代数式等', 1),
('几何', 'GEOMETRY', '图形认识、测量、变换等', 2),
('统计与概率', 'STAT_PROB', '数据收集、整理、分析等', 3),
('综合与实践', 'COMPREHENSIVE', '综合运用数学知识解决实际问题', 4)
ON DUPLICATE KEY UPDATE `description`=VALUES(`description`);

-- 插入示例测试数据
INSERT INTO `test` (`test_name`, `grade_id`, `knowledge_point_ids`, `total_questions`, `total_points`, `time_limit`, `difficulty_level`, `test_type`, `status`) VALUES
('一年级数学基础测试', 1, '[1,2]', 10, 10, 30, 1, 1, 1),
('二年级数学综合测试', 2, '[4,5]', 15, 15, 45, 2, 1, 1),
('三年级数学进阶测试', 3, '[6,7,8]', 20, 20, 60, 3, 2, 1),
('七年级数学入门测试', 7, '[9,10]', 25, 25, 90, 2, 1, 1),
('九年级数学综合测试', 9, '[11,12,13]', 30, 30, 120, 3, 2, 1)
ON DUPLICATE KEY UPDATE `test_name`=VALUES(`test_name`);

-- =============================================
-- 7. 创建索引优化
-- =============================================

-- 为常用查询字段创建复合索引
CREATE INDEX `idx_user_role_grade` ON `user`(`role`, `grade`);
CREATE INDEX `idx_kp_grade_difficulty` ON `knowledge_point`(`grade_id`, `difficulty_level`);
CREATE INDEX `idx_question_kp_difficulty` ON `question`(`knowledge_point_id`, `difficulty_level`);
CREATE INDEX `idx_test_record_student_status` ON `student_test_record`(`student_id`, `test_status`);
CREATE INDEX `idx_test_answer_record_correct` ON `student_test_answer`(`test_record_id`, `is_correct`);

-- =============================================
-- 8. 创建视图（可选）
-- =============================================

-- 学生测试统计视图
CREATE OR REPLACE VIEW `v_student_test_statistics` AS
SELECT 
    str.student_id,
    u.username,
    u.grade,
    COUNT(str.id) as total_tests,
    COUNT(CASE WHEN str.test_status = 2 THEN 1 END) as completed_tests,
    SUM(str.total_questions) as total_questions,
    SUM(str.answered_questions) as answered_questions,
    SUM(str.correct_answers) as correct_answers,
    SUM(str.total_points) as total_points,
    SUM(str.earned_points) as earned_points,
    AVG(str.score_percentage) as average_score,
    ROUND(SUM(str.correct_answers) * 100.0 / NULLIF(SUM(str.answered_questions), 0), 2) as accuracy_rate
FROM `student_test_record` str
JOIN `user` u ON str.student_id = u.id
WHERE str.delete_flag = 'N' AND u.delete_flag = '0'
GROUP BY str.student_id, u.username, u.grade;

-- 知识点掌握情况视图
CREATE OR REPLACE VIEW `v_knowledge_mastery_summary` AS
SELECT 
    km.student_id,
    u.username,
    kp.point_name,
    g.grade_name,
    kc.category_name,
    km.total_tests,
    km.total_questions,
    km.correct_answers,
    km.accuracy_rate,
    km.mastery_level,
    CASE 
        WHEN km.mastery_level = 1 THEN '未掌握'
        WHEN km.mastery_level = 2 THEN '基本掌握'
        WHEN km.mastery_level = 3 THEN '熟练掌握'
        ELSE '未知'
    END as mastery_level_name,
    km.last_test_time
FROM `knowledge_mastery` km
JOIN `user` u ON km.student_id = u.id
JOIN `knowledge_point` kp ON km.knowledge_point_id = kp.id
JOIN `grade` g ON kp.grade_id = g.id
JOIN `knowledge_category` kc ON kp.category_id = kc.id
WHERE km.delete_flag = 'N' AND u.delete_flag = '0';

-- =============================================
-- 建表完成
-- =============================================

-- 显示创建的表
SHOW TABLES;

-- 显示表结构统计
SELECT 
    TABLE_NAME as '表名',
    TABLE_ROWS as '预估行数',
    DATA_LENGTH as '数据大小(字节)',
    INDEX_LENGTH as '索引大小(字节)',
    TABLE_COMMENT as '表注释'
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'tutor' 
ORDER BY TABLE_NAME;
