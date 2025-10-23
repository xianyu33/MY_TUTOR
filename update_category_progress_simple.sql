-- =============================================
-- 学习进度和知识点分类关系更新脚本 - 简化版
-- =============================================

USE tutor;

-- 1. 为knowledge_category表添加grade_id字段
ALTER TABLE `knowledge_category`
ADD COLUMN `grade_id` INT COMMENT '年级ID' AFTER `description_fr`,
ADD INDEX `idx_grade_id` (`grade_id`);

-- 2. 创建learning_progress表（如果不存在）
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

-- 3. 为learning_progress表添加knowledge_category_id字段（如果表已存在）
ALTER TABLE `learning_progress`
ADD COLUMN `knowledge_category_id` INT COMMENT '知识点分类ID' AFTER `knowledge_point_id`,
ADD INDEX `idx_user_category` (`user_id`, `knowledge_category_id`);

-- 4. 更新现有数据
UPDATE `knowledge_category` SET `grade_id` = 1 WHERE `category_code` = 'NUM_ALG';
UPDATE `knowledge_category` SET `grade_id` = 1 WHERE `category_code` = 'GEOMETRY';
UPDATE `knowledge_category` SET `grade_id` = 1 WHERE `category_code` = 'STAT_PROB';
UPDATE `knowledge_category` SET `grade_id` = 1 WHERE `category_code` = 'COMPREHENSIVE';

-- 更新learning_progress表的knowledge_category_id字段
UPDATE `learning_progress` lp
JOIN `knowledge_point` kp ON lp.knowledge_point_id = kp.id
SET lp.knowledge_category_id = kp.category_id
WHERE lp.knowledge_category_id IS NULL;
