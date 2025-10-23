-- =============================================
-- 知识点分类表图标字段管理脚本
-- =============================================
-- 功能: 添加/删除知识点分类表的图标字段
-- 版本: 1.0
-- 日期: 2023-12-21
-- =============================================

USE tutor;

-- =============================================
-- 选项1: 添加图标字段（默认执行）
-- =============================================

-- 检查字段是否已存在
SET @column_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'tutor' 
    AND TABLE_NAME = 'knowledge_category' 
    AND COLUMN_NAME = 'icon_url'
);

-- 如果字段不存在，则添加
SET @sql = IF(@column_exists = 0, 
    'ALTER TABLE `knowledge_category` 
     ADD COLUMN `icon_url` VARCHAR(500) COMMENT ''分类图标URL'' AFTER `description_fr`,
     ADD COLUMN `icon_class` VARCHAR(100) COMMENT ''分类图标CSS类名'' AFTER `icon_url`',
    'SELECT ''图标字段已存在，跳过添加'' AS message'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查索引是否存在
SET @index_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.STATISTICS 
    WHERE TABLE_SCHEMA = 'tutor' 
    AND TABLE_NAME = 'knowledge_category' 
    AND INDEX_NAME = 'idx_icon_class'
);

-- 如果索引不存在，则添加
SET @sql = IF(@index_exists = 0, 
    'CREATE INDEX `idx_icon_class` ON `knowledge_category`(`icon_class`)',
    'SELECT ''图标索引已存在，跳过添加'' AS message'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 更新现有数据（使用INSERT ... ON DUPLICATE KEY UPDATE）
INSERT INTO `knowledge_category` (`category_code`, `icon_url`, `icon_class`) VALUES
('NUM_ALG', '/icons/category/numbers-algebra.png', 'icon-category-numbers'),
('GEOMETRY', '/icons/category/geometry.png', 'icon-category-geometry'),
('STAT_PROB', '/icons/category/statistics.png', 'icon-category-statistics'),
('COMPREHENSIVE', '/icons/category/comprehensive.png', 'icon-category-comprehensive')
ON DUPLICATE KEY UPDATE 
    `icon_url` = VALUES(`icon_url`),
    `icon_class` = VALUES(`icon_class`);

-- =============================================
-- 选项2: 删除图标字段（注释掉，需要时取消注释）
-- =============================================

/*
-- 删除索引
DROP INDEX IF EXISTS `idx_icon_class` ON `knowledge_category`;

-- 删除字段
ALTER TABLE `knowledge_category` 
DROP COLUMN IF EXISTS `icon_url`,
DROP COLUMN IF EXISTS `icon_class`;

SELECT '图标字段已删除' AS message;
*/

-- =============================================
-- 验证结果
-- =============================================

-- 显示表结构
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    CHARACTER_MAXIMUM_LENGTH,
    COLUMN_COMMENT,
    ORDINAL_POSITION
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'tutor' 
AND TABLE_NAME = 'knowledge_category'
AND COLUMN_NAME IN ('icon_url', 'icon_class')
ORDER BY ORDINAL_POSITION;

-- 显示更新后的数据
SELECT 
    id,
    category_name,
    category_name_fr,
    category_code,
    icon_url,
    icon_class,
    sort_order
FROM `knowledge_category` 
WHERE delete_flag = 'N' 
ORDER BY sort_order;

-- 显示执行结果
SELECT '知识点分类图标字段更新完成！' AS message;
