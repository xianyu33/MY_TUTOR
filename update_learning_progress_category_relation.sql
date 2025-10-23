-- =============================================
-- 学习进度和知识点分类关系更新脚本
-- =============================================
-- 脚本名称: update_learning_progress_category_relation.sql
-- 创建时间: 2023-12-21
-- 说明: 为learning_progress表添加knowledge_category_id字段，为knowledge_category表添加grade_id字段
-- =============================================

USE tutor;

-- =============================================
-- 1. 为knowledge_category表添加grade_id字段
-- =============================================

-- 检查grade_id字段是否已存在
SET @column_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'tutor' 
    AND TABLE_NAME = 'knowledge_category' 
    AND COLUMN_NAME = 'grade_id'
);

-- 如果字段不存在，则添加
SET @sql = IF(@column_exists = 0, 
    'ALTER TABLE `knowledge_category` 
     ADD COLUMN `grade_id` INT COMMENT ''年级ID'' AFTER `description_fr`,
     ADD INDEX `idx_grade_id` (`grade_id`),
     ADD CONSTRAINT `fk_category_grade` FOREIGN KEY (`grade_id`) REFERENCES `grade`(`id`) ON DELETE SET NULL',
    'SELECT ''grade_id字段已存在，跳过添加'' AS message'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- =============================================
-- 2. 创建learning_progress表（如果不存在）
-- =============================================

-- 检查learning_progress表是否存在
SET @table_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.TABLES 
    WHERE TABLE_SCHEMA = 'tutor' 
    AND TABLE_NAME = 'learning_progress'
);

-- 如果表不存在，则创建
SET @sql = IF(@table_exists = 0, 
    'CREATE TABLE `learning_progress` (
      `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT ''进度ID'',
      `user_id` INT NOT NULL COMMENT ''学生ID'',
      `knowledge_point_id` INT NOT NULL COMMENT ''知识点ID'',
      `knowledge_category_id` INT COMMENT ''知识点分类ID'',
      `progress_status` TINYINT DEFAULT 1 COMMENT ''学习状态：1-未开始，2-学习中，3-已完成'',
      `completion_percentage` DECIMAL(5,2) DEFAULT 0.00 COMMENT ''完成百分比'',
      `start_time` DATETIME COMMENT ''开始学习时间'',
      `complete_time` DATETIME COMMENT ''完成学习时间'',
      `study_duration` INT DEFAULT 0 COMMENT ''学习时长（分钟）'',
      `last_study_time` DATETIME COMMENT ''最后学习时间'',
      `notes` TEXT COMMENT ''学习笔记'',
      `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
      `create_by` VARCHAR(50) COMMENT ''创建人'',
      `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ''更新时间'',
      `update_by` VARCHAR(50) COMMENT ''更新人'',
      `delete_flag` CHAR(1) DEFAULT ''N'' COMMENT ''删除标志：Y-已删除，N-未删除'',
      CONSTRAINT `fk_progress_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
      CONSTRAINT `fk_progress_kp` FOREIGN KEY (`knowledge_point_id`) REFERENCES `knowledge_point`(`id`),
      CONSTRAINT `fk_progress_category` FOREIGN KEY (`knowledge_category_id`) REFERENCES `knowledge_category`(`id`) ON DELETE SET NULL,
      UNIQUE KEY `uk_user_kp` (`user_id`, `knowledge_point_id`),
      INDEX `idx_user_category` (`user_id`, `knowledge_category_id`),
      INDEX `idx_progress_status` (`progress_status`),
      INDEX `idx_completion` (`completion_percentage`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT=''学习进度表''',
    'SELECT ''learning_progress表已存在，跳过创建'' AS message'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- =============================================
-- 3. 为learning_progress表添加knowledge_category_id字段（如果表已存在）
-- =============================================

-- 检查knowledge_category_id字段是否已存在
SET @column_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'tutor' 
    AND TABLE_NAME = 'learning_progress' 
    AND COLUMN_NAME = 'knowledge_category_id'
);

-- 如果字段不存在，则添加
SET @sql = IF(@column_exists = 0, 
    'ALTER TABLE `learning_progress` 
     ADD COLUMN `knowledge_category_id` INT COMMENT ''知识点分类ID'' AFTER `knowledge_point_id`,
     ADD INDEX `idx_user_category` (`user_id`, `knowledge_category_id`),
     ADD CONSTRAINT `fk_progress_category` FOREIGN KEY (`knowledge_category_id`) REFERENCES `knowledge_category`(`id`) ON DELETE SET NULL',
    'SELECT ''knowledge_category_id字段已存在，跳过添加'' AS message'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- =============================================
-- 4. 更新现有数据
-- =============================================

-- 更新knowledge_category表的grade_id字段
UPDATE `knowledge_category` SET `grade_id` = 1 WHERE `category_code` = 'NUM_ALG';
UPDATE `knowledge_category` SET `grade_id` = 1 WHERE `category_code` = 'GEOMETRY';
UPDATE `knowledge_category` SET `grade_id` = 1 WHERE `category_code` = 'STAT_PROB';
UPDATE `knowledge_category` SET `grade_id` = 1 WHERE `category_code` = 'COMPREHENSIVE';

-- 更新learning_progress表的knowledge_category_id字段（基于knowledge_point的category_id）
UPDATE `learning_progress` lp
JOIN `knowledge_point` kp ON lp.knowledge_point_id = kp.id
SET lp.knowledge_category_id = kp.category_id
WHERE lp.knowledge_category_id IS NULL;

-- =============================================
-- 5. 验证更新结果
-- =============================================

-- 显示knowledge_category表结构
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    CHARACTER_MAXIMUM_LENGTH,
    COLUMN_COMMENT,
    ORDINAL_POSITION
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'tutor' 
AND TABLE_NAME = 'knowledge_category'
AND COLUMN_NAME IN ('grade_id')
ORDER BY ORDINAL_POSITION;

-- 显示learning_progress表结构
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    CHARACTER_MAXIMUM_LENGTH,
    COLUMN_COMMENT,
    ORDINAL_POSITION
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'tutor' 
AND TABLE_NAME = 'learning_progress'
AND COLUMN_NAME IN ('knowledge_category_id')
ORDER BY ORDINAL_POSITION;

-- 显示更新后的数据
SELECT 
    id,
    category_name,
    category_code,
    grade_id,
    icon_url,
    icon_class
FROM `knowledge_category` 
WHERE delete_flag = 'N' 
ORDER BY sort_order;

-- 显示学习进度数据统计
SELECT 
    COUNT(*) as total_progress,
    COUNT(knowledge_category_id) as with_category,
    COUNT(*) - COUNT(knowledge_category_id) as without_category
FROM `learning_progress` 
WHERE delete_flag = 'N';

-- =============================================
-- 6. 回滚脚本（如需要）
-- =============================================
-- 如果需要回滚，可以执行以下语句：
-- 
-- -- 删除learning_progress表的外键约束和字段
-- ALTER TABLE `learning_progress` 
-- DROP FOREIGN KEY `fk_progress_category`,
-- DROP INDEX `idx_user_category`,
-- DROP COLUMN `knowledge_category_id`;
-- 
-- -- 删除knowledge_category表的外键约束和字段
-- ALTER TABLE `knowledge_category` 
-- DROP FOREIGN KEY `fk_category_grade`,
-- DROP INDEX `idx_grade_id`,
-- DROP COLUMN `grade_id`;

-- =============================================
-- 脚本执行完成
-- =============================================
SELECT '学习进度和知识点分类关系更新完成！' AS message;
