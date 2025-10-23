-- =============================================
-- MY_TUTOR 测试系统专用建表语句
-- 包含：用户表、年级表、知识点表、题目表、测试相关表
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS tutor CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tutor;

-- =============================================
-- 1. 基础表
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

-- 年级表
CREATE TABLE IF NOT EXISTS `grade` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '年级ID',
  `grade_name` VARCHAR(20) NOT NULL COMMENT '年级名称',
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
  `category_name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `category_name_fr` VARCHAR(50) COMMENT '分类名称（法语）',
  `category_code` VARCHAR(20) NOT NULL COMMENT '分类编码',
  `description` TEXT COMMENT '分类描述',
  `description_fr` TEXT COMMENT '分类描述（法语）',
  `grade_id` INT COMMENT '年级ID',
  `icon_url` VARCHAR(500) COMMENT '分类图标URL',
  `icon_class` VARCHAR(100) COMMENT '分类图标CSS类名',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `delete_flag` CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  UNIQUE KEY `uk_category_code` (`category_code`),
  INDEX `idx_category_name` (`category_name`),
  INDEX `idx_category_name_fr` (`category_name_fr`),
  INDEX `idx_grade_id` (`grade_id`),
  INDEX `idx_icon_class` (`icon_class`),
  FOREIGN KEY (`grade_id`) REFERENCES `grade`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识点分类表';

-- 知识点表
CREATE TABLE IF NOT EXISTS `knowledge_point` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '知识点ID',
  `grade_id` INT NOT NULL COMMENT '年级ID',
  `category_id` INT NOT NULL COMMENT '分类ID',
  `point_name` VARCHAR(100) NOT NULL COMMENT '知识点名称',
  `point_name_fr` VARCHAR(100) COMMENT '知识点名称（法语）',
  `point_code` VARCHAR(50) NOT NULL COMMENT '知识点编码',
  `description` TEXT COMMENT '知识点描述',
  `description_fr` TEXT COMMENT '知识点描述（法语）',
  `content` TEXT NOT NULL COMMENT '知识点内容详情',
  `content_fr` TEXT COMMENT '知识点内容详情（法语）',
  `icon_url` VARCHAR(500) COMMENT '知识点图标URL',
  `icon_class` VARCHAR(100) COMMENT '知识点图标CSS类名',
  `difficulty_level` TINYINT DEFAULT 1 COMMENT '难度等级：1-简单，2-中等，3-困难',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `prerequisite_points` TEXT COMMENT '前置知识点ID列表，JSON格式',
  `learning_objectives` TEXT COMMENT '学习目标',
  `learning_objectives_fr` TEXT COMMENT '学习目标（法语）',
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
  INDEX `idx_point_name_fr` (`point_name_fr`),
  INDEX `idx_kp_difficulty` (`difficulty_level`),
  INDEX `idx_icon_class` (`icon_class`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识点表';

-- 题目表
CREATE TABLE IF NOT EXISTS `question` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '题目ID',
  `knowledge_point_id` INT NOT NULL COMMENT '知识点ID',
  `question_type` TINYINT DEFAULT 1 COMMENT '题目类型：1-选择题，2-填空题，3-解答题',
  `question_title` VARCHAR(200) COMMENT '题目标题',
  `question_title_fr` VARCHAR(200) COMMENT '题目标题（法语）',
  `question_content` TEXT NOT NULL COMMENT '题目内容',
  `question_content_fr` TEXT COMMENT '题目内容（法语）',
  `options` TEXT COMMENT '选项（JSON格式）',
  `options_fr` TEXT COMMENT '选项（法语，JSON格式）',
  `correct_answer` TEXT NOT NULL COMMENT '正确答案',
  `correct_answer_fr` TEXT COMMENT '正确答案（法语）',
  `answer_explanation` TEXT COMMENT '答案解释',
  `answer_explanation_fr` TEXT COMMENT '答案解释（法语）',
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

-- 学生知识点分类绑定关系表
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

-- =============================================
-- 3. 学习进度管理表
-- =============================================

-- 学习进度表
CREATE TABLE IF NOT EXISTS `learning_progress` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '进度ID',
  `user_id` INT NOT NULL COMMENT '学生ID',
  `knowledge_point_id` INT NOT NULL COMMENT '知识点ID',
  `knowledge_category_id` INT COMMENT '知识点分类ID',
  `progress_status` TINYINT DEFAULT 1 COMMENT '学习状态：1-未开始，2-学习中，3-已完成',
  `completion_percentage` DECIMAL(5,2) DEFAULT 0.00 COMMENT '完成百分比',
  `start_time` DATETIME COMMENT '开始学习时间',
  `complete_time` DATETIME COMMENT '完成学习时间',
  `study_duration` INT DEFAULT 0 COMMENT '学习时长（分钟）',
  `last_study_time` DATETIME COMMENT '最后学习时间',
  `notes` TEXT COMMENT '学习笔记',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `delete_flag` CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  CONSTRAINT `fk_progress_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  CONSTRAINT `fk_progress_kp` FOREIGN KEY (`knowledge_point_id`) REFERENCES `knowledge_point`(`id`),
  CONSTRAINT `fk_progress_category` FOREIGN KEY (`knowledge_category_id`) REFERENCES `knowledge_category`(`id`) ON DELETE SET NULL,
  UNIQUE KEY `uk_user_kp` (`user_id`, `knowledge_point_id`),
  INDEX `idx_user_category` (`user_id`, `knowledge_category_id`),
  INDEX `idx_progress_status` (`progress_status`),
  INDEX `idx_completion` (`completion_percentage`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习进度表';

-- =============================================
-- 3. 测试系统核心表
-- =============================================

-- 测试表
CREATE TABLE IF NOT EXISTS `test` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '测试ID',
  `test_name` VARCHAR(200) NOT NULL COMMENT '测试名称',
  `test_name_fr` VARCHAR(200) COMMENT '测试名称（法语）',
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
  INDEX `idx_difficulty` (`difficulty_level`),
  INDEX `idx_test_name` (`test_name`),
  INDEX `idx_test_name_fr` (`test_name_fr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试表';

-- 测试题目关联表
CREATE TABLE IF NOT EXISTS `test_question` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
  `test_id` INT NOT NULL COMMENT '测试ID',
  `question_id` INT NOT NULL COMMENT '题目ID',
  `sort_order` INT DEFAULT 0 COMMENT '题目顺序',
  `points` INT DEFAULT 1 COMMENT '分值',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY `uk_test_question` (`test_id`, `question_id`),
  INDEX `idx_test_order` (`test_id`, `sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试题目关联表';

-- 学生测试记录表
CREATE TABLE IF NOT EXISTS `student_test_record` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
  `student_id` INT NOT NULL COMMENT '学生ID',
  `test_id` INT NOT NULL COMMENT '测试ID',
  `test_name` VARCHAR(200) NOT NULL COMMENT '测试名称（冗余）',
  `test_name_fr` VARCHAR(200) COMMENT '测试名称（法语，冗余）',
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
  `question_content_fr` TEXT COMMENT '题目内容（法语，冗余）',
  `correct_answer` TEXT NOT NULL COMMENT '正确答案（冗余）',
  `correct_answer_fr` TEXT COMMENT '正确答案（法语，冗余）',
  `student_answer` TEXT COMMENT '学生答案',
  `student_answer_fr` TEXT COMMENT '学生答案（法语）',
  `is_correct` TINYINT DEFAULT 0 COMMENT '是否正确：0-错误，1-正确',
  `points` INT DEFAULT 0 COMMENT '题目分值',
  `earned_points` INT DEFAULT 0 COMMENT '获得分值',
  `answer_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '答题时间',
  `time_spent` INT DEFAULT 0 COMMENT '答题用时（秒）',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  INDEX `idx_record_question` (`test_record_id`, `question_id`),
  INDEX `idx_student_answer` (`student_id`, `question_id`),
  INDEX `idx_is_correct` (`is_correct`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生测试答题详情表';

-- =============================================
-- 3. 基础数据插入
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
INSERT INTO `knowledge_category` (`category_name`, `category_name_fr`, `category_code`, `description`, `description_fr`, `grade_id`, `icon_url`, `icon_class`, `sort_order`) VALUES
('数与代数', 'Nombres et Algèbre', 'NUM_ALG', '数的认识、运算、代数式等', 'Reconnaissance des nombres, opérations, expressions algébriques, etc.', 1, '/icons/category/numbers-algebra.png', 'icon-category-numbers', 1),
('几何', 'Géométrie', 'GEOMETRY', '图形认识、测量、变换等', 'Reconnaissance des formes, mesure, transformation, etc.', 1, '/icons/category/geometry.png', 'icon-category-geometry', 2),
('统计与概率', 'Statistiques et Probabilité', 'STAT_PROB', '数据收集、整理、分析等', 'Collecte, organisation et analyse de données, etc.', 1, '/icons/category/statistics.png', 'icon-category-statistics', 3),
('综合与实践', 'Compréhension et Pratique', 'COMPREHENSIVE', '综合运用数学知识解决实际问题', 'Application intégrée des connaissances mathématiques pour résoudre des problèmes pratiques', 1, '/icons/category/comprehensive.png', 'icon-category-comprehensive', 4)
ON DUPLICATE KEY UPDATE `description`=VALUES(`description`), `description_fr`=VALUES(`description_fr`), `grade_id`=VALUES(`grade_id`), `icon_url`=VALUES(`icon_url`), `icon_class`=VALUES(`icon_class`);

-- 插入示例知识点数据
INSERT INTO `knowledge_point` (`grade_id`, `category_id`, `point_name`, `point_name_fr`, `point_code`, `description`, `description_fr`, `content`, `content_fr`, `icon_url`, `icon_class`, `difficulty_level`) VALUES
(1, 1, '数的认识', 'Reconnaissance des nombres', 'NUM_001', '认识1-100的数字', 'Reconnaissance des nombres de 1 à 100', '学习1-100数字的读写和大小比较', 'Apprendre la lecture, l\'écriture et la comparaison des nombres de 1 à 100', '/icons/numbers.png', 'icon-numbers', 1),
(1, 1, '加法运算', 'Opération d\'addition', 'NUM_002', '20以内加法', 'Addition dans les 20', '学习20以内的加法运算', 'Apprendre les opérations d\'addition dans les 20', '/icons/addition.png', 'icon-addition', 1),
(1, 2, '图形认识', 'Reconnaissance des formes', 'GEO_001', '基本图形', 'Formes de base', '认识圆形、正方形、三角形等基本图形', 'Reconnaître les formes de base comme le cercle, le carré, le triangle, etc.', '/icons/shapes.png', 'icon-shapes', 1),
(2, 1, '乘法口诀', 'Table de multiplication', 'NUM_003', '九九乘法表', 'Table de multiplication de 9', '学习九九乘法口诀', 'Apprendre la table de multiplication de 9', '/icons/multiplication.png', 'icon-multiplication', 2),
(2, 1, '除法运算', 'Opération de division', 'NUM_004', '简单除法', 'Division simple', '学习简单的除法运算', 'Apprendre les opérations de division simples', '/icons/division.png', 'icon-division', 2),
(3, 1, '分数认识', 'Reconnaissance des fractions', 'NUM_005', '分数概念', 'Concept de fraction', '认识分数的概念和表示方法', 'Comprendre le concept et les méthodes de représentation des fractions', '/icons/fractions.png', 'icon-fractions', 2),
(7, 1, '有理数', 'Nombres rationnels', 'NUM_006', '有理数运算', 'Opérations sur les nombres rationnels', '学习有理数的四则运算', 'Apprendre les quatre opérations sur les nombres rationnels', '/icons/rational-numbers.png', 'icon-rational', 2),
(7, 2, '平面几何', 'Géométrie plane', 'GEO_002', '平面图形', 'Formes planes', '学习平面几何的基本概念', 'Apprendre les concepts de base de la géométrie plane', '/icons/plane-geometry.png', 'icon-plane-geometry', 2),
(9, 1, '二次函数', 'Fonction quadratique', 'NUM_007', '二次函数', 'Fonction quadratique', '学习二次函数的图像和性质', 'Apprendre les graphiques et propriétés des fonctions quadratiques', '/icons/quadratic-function.png', 'icon-quadratic', 3),
(9, 2, '立体几何', 'Géométrie solide', 'GEO_003', '立体图形', 'Formes solides', '学习立体几何的基本概念', 'Apprendre les concepts de base de la géométrie solide', '/icons/solid-geometry.png', 'icon-solid-geometry', 3)
ON DUPLICATE KEY UPDATE `description`=VALUES(`description`), `description_fr`=VALUES(`description_fr`), `content`=VALUES(`content`), `content_fr`=VALUES(`content_fr`), `icon_url`=VALUES(`icon_url`), `icon_class`=VALUES(`icon_class`);

-- 插入示例题目数据
INSERT INTO `question` (`knowledge_point_id`, `question_type`, `question_title`, `question_title_fr`, `question_content`, `question_content_fr`, `options`, `options_fr`, `correct_answer`, `correct_answer_fr`, `answer_explanation`, `answer_explanation_fr`, `difficulty_level`, `points`) VALUES
(1, 1, '数字认识', 'Reconnaissance des nombres', '下列哪个数字最大？', 'Quel est le plus grand nombre parmi les suivants ?', '["A. 15", "B. 25", "C. 35", "D. 45"]', '["A. 15", "B. 25", "C. 35", "D. 45"]', 'D', 'D', '45是最大的数字', '45 est le plus grand nombre', 1, 1),
(1, 1, '数字比较', 'Comparaison de nombres', '比较大小：23和32', 'Comparez les tailles : 23 et 32', '["A. 23 > 32", "B. 23 < 32", "C. 23 = 32", "D. 无法比较"]', '["A. 23 > 32", "B. 23 < 32", "C. 23 = 32", "D. Impossible à comparer"]', 'B', 'B', '32比23大', '32 est plus grand que 23', 1, 1),
(2, 1, '加法运算', 'Opération d\'addition', '5 + 7 = ?', '5 + 7 = ?', '["A. 11", "B. 12", "C. 13", "D. 14"]', '["A. 11", "B. 12", "C. 13", "D. 14"]', 'B', 'B', '5 + 7 = 12', '5 + 7 = 12', 1, 1),
(2, 1, '加法运算', 'Opération d\'addition', '8 + 9 = ?', '8 + 9 = ?', '["A. 16", "B. 17", "C. 18", "D. 19"]', '["A. 16", "B. 17", "C. 18", "D. 19"]', 'B', 'B', '8 + 9 = 17', '8 + 9 = 17', 1, 1),
(3, 1, '图形认识', 'Reconnaissance des formes', '下列哪个是圆形？', 'Lequel des suivants est un cercle ?', '["A. 正方形", "B. 三角形", "C. 圆形", "D. 长方形"]', '["A. Carré", "B. Triangle", "C. Cercle", "D. Rectangle"]', 'C', 'C', '圆形是选项C', 'Le cercle est l\'option C', 1, 1),
(4, 1, '乘法运算', 'Opération de multiplication', '3 × 4 = ?', '3 × 4 = ?', '["A. 10", "B. 11", "C. 12", "D. 13"]', '["A. 10", "B. 11", "C. 12", "D. 13"]', 'C', 'C', '3 × 4 = 12', '3 × 4 = 12', 2, 1),
(4, 1, '乘法运算', 'Opération de multiplication', '6 × 7 = ?', '6 × 7 = ?', '["A. 40", "B. 41", "C. 42", "D. 43"]', '["A. 40", "B. 41", "C. 42", "D. 43"]', 'C', 'C', '6 × 7 = 42', '6 × 7 = 42', 2, 1),
(5, 1, '除法运算', 'Opération de division', '12 ÷ 3 = ?', '12 ÷ 3 = ?', '["A. 3", "B. 4", "C. 5", "D. 6"]', '["A. 3", "B. 4", "C. 5", "D. 6"]', 'B', 'B', '12 ÷ 3 = 4', '12 ÷ 3 = 4', 2, 1),
(6, 1, '分数认识', 'Reconnaissance des fractions', '1/2 表示什么？', 'Que représente 1/2 ?', '["A. 一半", "B. 两倍", "C. 三分之一", "D. 四分之一"]', '["A. La moitié", "B. Le double", "C. Un tiers", "D. Un quart"]', 'A', 'A', '1/2表示一半', '1/2 représente la moitié', 2, 1),
(7, 1, '有理数运算', 'Opérations sur les nombres rationnels', '(-3) + 5 = ?', '(-3) + 5 = ?', '["A. -8", "B. -2", "C. 2", "D. 8"]', '["A. -8", "B. -2", "C. 2", "D. 8"]', 'C', 'C', '(-3) + 5 = 2', '(-3) + 5 = 2', 2, 1),
(8, 1, '平面几何', 'Géométrie plane', '三角形的内角和是多少度？', 'Quelle est la somme des angles intérieurs d\'un triangle ?', '["A. 90°", "B. 180°", "C. 270°", "D. 360°"]', '["A. 90°", "B. 180°", "C. 270°", "D. 360°"]', 'B', 'B', '三角形内角和是180°', 'La somme des angles intérieurs d\'un triangle est 180°', 2, 1),
(9, 1, '二次函数', 'Fonction quadratique', 'y = x² 的图像是什么形状？', 'Quelle est la forme du graphique de y = x² ?', '["A. 直线", "B. 抛物线", "C. 双曲线", "D. 椭圆"]', '["A. Ligne droite", "B. Parabole", "C. Hyperbole", "D. Ellipse"]', 'B', 'B', 'y = x² 的图像是抛物线', 'Le graphique de y = x² est une parabole', 3, 1),
(10, 1, '立体几何', 'Géométrie solide', '正方体有几个面？', 'Combien de faces a un cube ?', '["A. 4", "B. 6", "C. 8", "D. 12"]', '["A. 4", "B. 6", "C. 8", "D. 12"]', 'B', 'B', '正方体有6个面', 'Un cube a 6 faces', 3, 1)
ON DUPLICATE KEY UPDATE `question_content`=VALUES(`question_content`), `question_content_fr`=VALUES(`question_content_fr`), `options`=VALUES(`options`), `options_fr`=VALUES(`options_fr`), `correct_answer`=VALUES(`correct_answer`), `correct_answer_fr`=VALUES(`correct_answer_fr`), `answer_explanation`=VALUES(`answer_explanation`), `answer_explanation_fr`=VALUES(`answer_explanation_fr`);

-- 插入示例测试数据
INSERT INTO `test` (`test_name`, `test_name_fr`, `grade_id`, `knowledge_point_ids`, `total_questions`, `total_points`, `time_limit`, `difficulty_level`, `test_type`, `status`) VALUES
('一年级数学基础测试', 'Test de base en mathématiques de première année', 1, '[1,2,3]', 5, 5, 30, 1, 1, 1),
('二年级数学综合测试', 'Test complet de mathématiques de deuxième année', 2, '[4,5]', 3, 3, 45, 2, 1, 1),
('三年级数学进阶测试', 'Test avancé de mathématiques de troisième année', 3, '[6]', 1, 1, 60, 2, 2, 1),
('七年级数学入门测试', 'Test d\'introduction aux mathématiques de septième année', 7, '[7,8]', 2, 2, 90, 2, 1, 1),
('九年级数学综合测试', 'Test complet de mathématiques de neuvième année', 9, '[9,10]', 2, 2, 120, 3, 2, 1)
ON DUPLICATE KEY UPDATE `test_name`=VALUES(`test_name`), `test_name_fr`=VALUES(`test_name_fr`);

-- =============================================
-- 4. 创建索引优化
-- =============================================

-- 为常用查询字段创建复合索引
CREATE INDEX `idx_user_role_grade` ON `user`(`role`, `grade`);
CREATE INDEX `idx_kp_grade_difficulty` ON `knowledge_point`(`grade_id`, `difficulty_level`);
CREATE INDEX `idx_question_kp_difficulty` ON `question`(`knowledge_point_id`, `difficulty_level`);
CREATE INDEX `idx_test_record_student_status` ON `student_test_record`(`student_id`, `test_status`);
CREATE INDEX `idx_test_answer_record_correct` ON `student_test_answer`(`test_record_id`, `is_correct`);

-- =============================================
-- 5. 创建视图
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
