-- 课程表拆分重构：course（主表+进度） + course_content（内容按语言存储）

-- 1. 删除旧 course 表，重建主表
DROP TABLE IF EXISTS `course`;

CREATE TABLE `course` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '课程ID',
  `student_id` INT NOT NULL COMMENT '学生ID',
  `knowledge_point_id` INT NOT NULL COMMENT '知识点ID',
  `difficulty_level` TINYINT DEFAULT 1 COMMENT '难度等级：1-简单，2-中等，3-困难',
  `current_stage` TINYINT DEFAULT 0 COMMENT '当前已生成阶段：0-未开始，1-4对应已生成的阶段',
  `completed_stage` INT DEFAULT 0 COMMENT '用户已完成的阶段 0-4',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `create_by` VARCHAR(50),
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` VARCHAR(50),
  `delete_flag` CHAR(1) DEFAULT '0',
  INDEX `idx_course_student` (`student_id`),
  INDEX `idx_course_kp` (`knowledge_point_id`),
  INDEX `idx_course_student_kp_diff` (`student_id`, `knowledge_point_id`, `difficulty_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程主表';

-- 2. 创建 course_content 内容表
CREATE TABLE IF NOT EXISTS `course_content` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '内容ID',
  `course_id` INT NOT NULL COMMENT '课程ID（关联 course.id）',
  `language` VARCHAR(10) NOT NULL DEFAULT 'en' COMMENT '语言：en-英语，fr-法语',
  `course_title` VARCHAR(200) COMMENT '课程标题（可按语言翻译）',
  `explanation` LONGTEXT COMMENT '阶段1 Understand：概念定义、关键要素、简单示例、适用边界',
  `examples` LONGTEXT COMMENT '阶段2 Apply：典型场景、复杂案例、实现步骤、常见错误',
  `key_summary` LONGTEXT COMMENT '阶段3 Master：步骤拆解、对比分析、原理理解、扩展应用',
  `additional_info` LONGTEXT COMMENT '阶段4 Evaluate：场景判断、方案选择、错误诊断、学习总结',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE INDEX `uk_course_language` (`course_id`, `language`),
  INDEX `idx_content_course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程内容表（按语言存储）';
