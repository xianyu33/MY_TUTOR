-- =============================================
-- 知识点分类表添加图标字段 - 简化版
-- =============================================

USE tutor;

-- 添加图标字段
ALTER TABLE `knowledge_category` 
ADD COLUMN `icon_url` VARCHAR(500) COMMENT '分类图标URL' AFTER `description_fr`,
ADD COLUMN `icon_class` VARCHAR(100) COMMENT '分类图标CSS类名' AFTER `icon_url`;

-- 添加索引
CREATE INDEX `idx_icon_class` ON `knowledge_category`(`icon_class`);

-- 更新现有数据
UPDATE `knowledge_category` SET 
    `icon_url` = '/icons/category/numbers-algebra.png',
    `icon_class` = 'icon-category-numbers'
WHERE `category_code` = 'NUM_ALG';

UPDATE `knowledge_category` SET 
    `icon_url` = '/icons/category/geometry.png',
    `icon_class` = 'icon-category-geometry'
WHERE `category_code` = 'GEOMETRY';

UPDATE `knowledge_category` SET 
    `icon_url` = '/icons/category/statistics.png',
    `icon_class` = 'icon-category-statistics'
WHERE `category_code` = 'STAT_PROB';

UPDATE `knowledge_category` SET 
    `icon_url` = '/icons/category/comprehensive.png',
    `icon_class` = 'icon-category-comprehensive'
WHERE `category_code` = 'COMPREHENSIVE';
