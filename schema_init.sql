-- 初始化数据库与表结构（数学知识点模块 + 家长/老师-学生关系模块）
-- 如数据库已存在，可注释掉以下两行
CREATE DATABASE IF NOT EXISTS tutor CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tutor;

-- 年级表
CREATE TABLE IF NOT EXISTS grade (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '年级ID',
  grade_name VARCHAR(20) NOT NULL COMMENT '年级名称，如：一年级、二年级等',
  grade_level INT NOT NULL COMMENT '年级等级，1-12',
  description TEXT COMMENT '年级描述',
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  create_by VARCHAR(50) COMMENT '创建人',
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  update_by VARCHAR(50) COMMENT '更新人',
  delete_flag CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  UNIQUE KEY uk_grade_level (grade_level),
  INDEX idx_grade_name (grade_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='年级表';

-- 知识点分类表
CREATE TABLE IF NOT EXISTS knowledge_category (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
  category_name VARCHAR(50) NOT NULL COMMENT '分类名称，如：数与代数、几何、统计与概率等',
  category_code VARCHAR(20) NOT NULL COMMENT '分类编码',
  description TEXT COMMENT '分类描述',
  sort_order INT DEFAULT 0 COMMENT '排序',
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  create_by VARCHAR(50) COMMENT '创建人',
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  update_by VARCHAR(50) COMMENT '更新人',
  delete_flag CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  UNIQUE KEY uk_category_code (category_code),
  INDEX idx_category_name (category_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识点分类表';

-- 知识点表
CREATE TABLE IF NOT EXISTS knowledge_point (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '知识点ID',
  grade_id INT NOT NULL COMMENT '年级ID',
  category_id INT NOT NULL COMMENT '分类ID',
  point_name VARCHAR(100) NOT NULL COMMENT '知识点名称',
  point_code VARCHAR(50) NOT NULL COMMENT '知识点编码',
  description TEXT COMMENT '知识点描述',
  content TEXT NOT NULL COMMENT '知识点内容详情',
  difficulty_level TINYINT DEFAULT 1 COMMENT '难度等级：1-简单，2-中等，3-困难',
  sort_order INT DEFAULT 0 COMMENT '排序',
  prerequisite_points TEXT COMMENT '前置知识点ID列表，JSON格式',
  learning_objectives TEXT COMMENT '学习目标',
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  create_by VARCHAR(50) COMMENT '创建人',
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  update_by VARCHAR(50) COMMENT '更新人',
  delete_flag CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  CONSTRAINT fk_kp_grade FOREIGN KEY (grade_id) REFERENCES grade(id),
  CONSTRAINT fk_kp_category FOREIGN KEY (category_id) REFERENCES knowledge_category(id),
  UNIQUE KEY uk_point_code (point_code),
  INDEX idx_grade_category (grade_id, category_id),
  INDEX idx_point_name (point_name),
  INDEX idx_kp_difficulty (difficulty_level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识点表';

-- 学习进度表（注意 user 为关键字，建议使用 `user`）
CREATE TABLE IF NOT EXISTS learning_progress (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '进度ID',
  user_id INT NOT NULL COMMENT '学生用户ID',
  knowledge_point_id INT NOT NULL COMMENT '知识点ID',
  progress_status TINYINT DEFAULT 1 COMMENT '学习状态：1-未开始，2-学习中，3-已完成',
  completion_percentage DECIMAL(5,2) DEFAULT 0.00 COMMENT '完成百分比',
  start_time DATETIME COMMENT '开始学习时间',
  complete_time DATETIME COMMENT '完成时间',
  study_duration INT DEFAULT 0 COMMENT '学习时长（分钟）',
  last_study_time DATETIME COMMENT '最后学习时间',
  notes TEXT COMMENT '学习笔记',
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  create_by VARCHAR(50) COMMENT '创建人',
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  update_by VARCHAR(50) COMMENT '更新人',
  delete_flag CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  CONSTRAINT fk_lp_user FOREIGN KEY (user_id) REFERENCES `user`(id),
  CONSTRAINT fk_lp_kp FOREIGN KEY (knowledge_point_id) REFERENCES knowledge_point(id),
  UNIQUE KEY uk_user_knowledge (user_id, knowledge_point_id),
  INDEX idx_user_progress (user_id, progress_status),
  INDEX idx_knowledge_progress (knowledge_point_id, progress_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习进度表';

-- 学习内容记录表
CREATE TABLE IF NOT EXISTS learning_content (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
  user_id INT NOT NULL COMMENT '学生用户ID',
  knowledge_point_id INT NOT NULL COMMENT '知识点ID',
  content_type TINYINT NOT NULL COMMENT '内容类型：1-视频，2-文档，3-练习，4-测试',
  content_title VARCHAR(200) NOT NULL COMMENT '内容标题',
  content_url VARCHAR(500) COMMENT '内容链接或路径',
  content_data TEXT COMMENT '内容数据（JSON格式）',
  study_time INT DEFAULT 0 COMMENT '学习时长（分钟）',
  completion_status TINYINT DEFAULT 1 COMMENT '完成状态：1-未完成，2-已完成',
  score DECIMAL(5,2) COMMENT '得分',
  feedback TEXT COMMENT '学习反馈',
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  create_by VARCHAR(50) COMMENT '创建人',
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  update_by VARCHAR(50) COMMENT '更新人',
  delete_flag CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  CONSTRAINT fk_lc_user FOREIGN KEY (user_id) REFERENCES `user`(id),
  CONSTRAINT fk_lc_kp FOREIGN KEY (knowledge_point_id) REFERENCES knowledge_point(id),
  INDEX idx_user_knowledge_content (user_id, knowledge_point_id),
  INDEX idx_content_type (content_type),
  INDEX idx_completion_status (completion_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习内容记录表';

-- 问题表
CREATE TABLE IF NOT EXISTS question (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '问题ID',
  knowledge_point_id INT NOT NULL COMMENT '知识点ID',
  question_type TINYINT NOT NULL COMMENT '题目类型：1-选择题，2-填空题，3-计算题，4-应用题',
  question_title VARCHAR(500) NOT NULL COMMENT '题目标题/主干',
  question_content TEXT NOT NULL COMMENT '题目详细内容',
  options TEXT COMMENT '选项内容（JSON，仅选择题使用）',
  correct_answer TEXT NOT NULL COMMENT '正确答案',
  answer_explanation TEXT COMMENT '答案解析',
  difficulty_level TINYINT DEFAULT 1 COMMENT '难度等级：1-简单，2-中等，3-困难',
  points INT DEFAULT 1 COMMENT '分值',
  sort_order INT DEFAULT 0 COMMENT '排序',
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  create_by VARCHAR(50) COMMENT '创建人',
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  update_by VARCHAR(50) COMMENT '更新人',
  delete_flag CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  CONSTRAINT fk_q_kp FOREIGN KEY (knowledge_point_id) REFERENCES knowledge_point(id),
  INDEX idx_knowledge_question (knowledge_point_id),
  INDEX idx_question_type (question_type),
  INDEX idx_q_difficulty (difficulty_level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问题表';

-- 学生答题记录表
CREATE TABLE IF NOT EXISTS student_answer (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '答题记录ID',
  user_id INT NOT NULL COMMENT '学生用户ID',
  question_id INT NOT NULL COMMENT '问题ID',
  knowledge_point_id INT NOT NULL COMMENT '知识点ID（冗余便于统计）',
  user_answer TEXT COMMENT '学生答案',
  is_correct TINYINT DEFAULT 0 COMMENT '是否正确：0-错误，1-正确',
  score DECIMAL(5,2) DEFAULT 0.00 COMMENT '得分',
  answer_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '答题时间',
  time_spent INT DEFAULT 0 COMMENT '答题用时（秒）',
  attempt_count INT DEFAULT 1 COMMENT '尝试次数',
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  create_by VARCHAR(50) COMMENT '创建人',
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  update_by VARCHAR(50) COMMENT '更新人',
  delete_flag CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  CONSTRAINT fk_sa_user FOREIGN KEY (user_id) REFERENCES `user`(id),
  CONSTRAINT fk_sa_question FOREIGN KEY (question_id) REFERENCES question(id),
  CONSTRAINT fk_sa_kp FOREIGN KEY (knowledge_point_id) REFERENCES knowledge_point(id),
  INDEX idx_user_answer (user_id, question_id),
  INDEX idx_knowledge_answer (knowledge_point_id),
  INDEX idx_answer_time (answer_time),
  INDEX idx_is_correct (is_correct)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生答题记录表';

-- 学习统计表
CREATE TABLE IF NOT EXISTS learning_statistics (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '统计ID',
  user_id INT NOT NULL COMMENT '学生用户ID',
  knowledge_point_id INT NOT NULL COMMENT '知识点ID',
  total_study_time INT DEFAULT 0 COMMENT '总学习时长（分钟）',
  total_questions INT DEFAULT 0 COMMENT '总答题数',
  correct_answers INT DEFAULT 0 COMMENT '正确答题数',
  accuracy_rate DECIMAL(5,2) DEFAULT 0.00 COMMENT '正确率（百分比）',
  mastery_level TINYINT DEFAULT 1 COMMENT '掌握程度：1-未掌握，2-基本掌握，3-熟练掌握',
  last_study_date DATE COMMENT '最后学习日期',
  study_days INT DEFAULT 0 COMMENT '学习天数',
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  create_by VARCHAR(50) COMMENT '创建人',
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  update_by VARCHAR(50) COMMENT '更新人',
  delete_flag CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  CONSTRAINT fk_ls_user FOREIGN KEY (user_id) REFERENCES `user`(id),
  CONSTRAINT fk_ls_kp FOREIGN KEY (knowledge_point_id) REFERENCES knowledge_point(id),
  UNIQUE KEY uk_user_knowledge_stats (user_id, knowledge_point_id),
  INDEX idx_user_stats (user_id),
  INDEX idx_mastery_level (mastery_level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习统计表';

-- 家长表类型变更（如字段已存在可忽略）
ALTER TABLE `parent`
  MODIFY COLUMN `type` tinyint(1) unsigned zerofill DEFAULT '0' COMMENT '0-家长  1-老师';

-- 家长/老师 与 学生 关联表
CREATE TABLE IF NOT EXISTS `guardian_student_rel` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `guardian_id` int NOT NULL COMMENT '家长/老师ID（关联 parent.id）',
  `guardian_type` tinyint(1) NOT NULL COMMENT '0-家长 1-老师（冗余）',
  `student_id` int NOT NULL COMMENT '学生ID（关联 user.id）',
  `relation` varchar(50) DEFAULT NULL COMMENT '关系：父亲/母亲/监护人/班主任/任课老师等',
  `start_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '关系开始时间',
  `end_at` datetime DEFAULT NULL COMMENT '关系结束时间（空表示有效）',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(255) DEFAULT NULL,
  `update_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(255) DEFAULT NULL,
  `delete_flag` char(1) DEFAULT '0' COMMENT '0-正常 1-删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_guardian_student_active` (`guardian_id`,`student_id`,`delete_flag`),
  KEY `idx_guardian_type` (`guardian_type`),
  KEY `idx_student_id` (`student_id`),
  CONSTRAINT `fk_gsr_guardian` FOREIGN KEY (`guardian_id`) REFERENCES `parent` (`id`),
  CONSTRAINT `fk_gsr_student` FOREIGN KEY (`student_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家长/老师 与 学生 关联表';

-- 基础数据
INSERT INTO grade (grade_name, grade_level, description) VALUES
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
ON DUPLICATE KEY UPDATE description=VALUES(description);

INSERT INTO knowledge_category (category_name, category_code, description, sort_order) VALUES
('数与代数', 'NUMBER_ALGEBRA', '数的认识、运算、代数式等', 1),
('几何', 'GEOMETRY', '图形认识、测量、变换等', 2),
('统计与概率', 'STATISTICS_PROBABILITY', '数据收集、整理、分析等', 3),
('综合应用', 'COMPREHENSIVE', '综合运用数学知识解决问题', 4)
ON DUPLICATE KEY UPDATE description=VALUES(description), sort_order=VALUES(sort_order);

INSERT INTO knowledge_point (grade_id, category_id, point_name, point_code, description, content, difficulty_level, sort_order) VALUES
(1, 1, '认识数字1-10', 'NUM_1_10', '认识数字1到10', '通过实物、图片等方式认识数字1-10，理解数字的含义', 1, 1),
(1, 1, '10以内加法', 'ADD_10', '10以内数的加法运算', '学习10以内数的加法运算，掌握加法口诀', 2, 2),
(1, 2, '认识基本图形', 'BASIC_SHAPES', '认识圆形、正方形、三角形等基本图形', '通过观察和操作认识基本图形，了解图形的特征', 1, 1),
(2, 1, '100以内数的认识', 'NUM_100', '认识100以内的数', '学习100以内数的读写、比较大小等', 2, 1),
(2, 1, '100以内加减法', 'ADD_SUB_100', '100以内数的加减法运算', '掌握100以内数的加减法运算方法', 3, 2)
ON DUPLICATE KEY UPDATE description=VALUES(description), content=VALUES(content), difficulty_level=VALUES(difficulty_level), sort_order=VALUES(sort_order);


