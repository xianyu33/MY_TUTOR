-- =============================================
-- 知识点分类表添加图标字段更新脚本
-- =============================================
-- 脚本名称: update_knowledge_category_icons.sql
-- 创建时间: 2023-12-21
-- 说明: 为knowledge_category表添加图标相关字段
-- =============================================

USE tutor;

-- =============================================
-- 1. 添加图标字段
-- =============================================
ALTER TABLE `knowledge_category` 
ADD COLUMN `icon_url` VARCHAR(500) COMMENT '分类图标URL' AFTER `description_fr`,
ADD COLUMN `icon_class` VARCHAR(100) COMMENT '分类图标CSS类名' AFTER `icon_url`;

-- =============================================
-- 2. 添加索引
-- =============================================
CREATE INDEX `idx_icon_class` ON `knowledge_category`(`icon_class`);

-- =============================================
-- 3. 更新现有数据 - 添加图标信息
-- =============================================

-- 更新"数与代数"分类
UPDATE `knowledge_category` SET 
    `icon_url` = '/icons/category/numbers-algebra.png',
    `icon_class` = 'icon-category-numbers'
WHERE `category_code` = 'NUM_ALG';

-- 更新"几何"分类
UPDATE `knowledge_category` SET 
    `icon_url` = '/icons/category/geometry.png',
    `icon_class` = 'icon-category-geometry'
WHERE `category_code` = 'GEOMETRY';

-- 更新"统计与概率"分类
UPDATE `knowledge_category` SET 
    `icon_url` = '/icons/category/statistics.png',
    `icon_class` = 'icon-category-statistics'
WHERE `category_code` = 'STAT_PROB';

-- 更新"综合与实践"分类
UPDATE `knowledge_category` SET 
    `icon_url` = '/icons/category/comprehensive.png',
    `icon_class` = 'icon-category-comprehensive'
WHERE `category_code` = 'COMPREHENSIVE';

-- =============================================
-- 4. 验证更新结果
-- =============================================
-- 查看表结构
DESCRIBE `knowledge_category`;

-- 查看更新后的数据
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

-- =============================================
-- 5. 回滚脚本（如需要）
-- =============================================
-- 如果需要回滚，可以执行以下语句：
-- 
-- -- 删除索引
-- DROP INDEX `idx_icon_class` ON `knowledge_category`;
-- 
-- -- 删除字段
-- ALTER TABLE `knowledge_category` 
-- DROP COLUMN `icon_url`,
-- DROP COLUMN `icon_class`;

-- =============================================
-- 脚本执行完成
-- =============================================
SELECT '知识点分类图标字段更新完成！' AS message;
