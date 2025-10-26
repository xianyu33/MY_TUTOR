-- 知识点分类图标和图片资源配置
-- Element UI 风格的图标和图片

-- 更新知识点分类的图标信息
UPDATE `knowledge_category` SET 
    `icon_url` = '/images/category/numbers-algebra.png',
    `icon_class` = 'icon-category-number-algebra',
    `update_at` = NOW()
WHERE `category_code` = 'NUMBER_ALGEBRA';

UPDATE `knowledge_category` SET 
    `icon_url` = '/images/category/geometry.png',
    `icon_class` = 'icon-category-geometry',
    `update_at` = NOW()
WHERE `category_code` = 'GEOMETRY';

UPDATE `knowledge_category` SET 
    `icon_url` = '/images/category/statistics-probability.png',
    `icon_class` = 'icon-category-statistics',
    `update_at` = NOW()
WHERE `category_code` = 'STATISTICS_PROBABILITY';

UPDATE `knowledge_category` SET 
    `icon_url` = '/images/category/comprehensive.png',
    `icon_class` = 'icon-category-comprehensive',
    `update_at` = NOW()
WHERE `category_code` = 'COMPREHENSIVE';

-- 查询更新结果
SELECT 
    id,
    category_name,
    category_code,
    icon_url,
    icon_class,
    sort_order
FROM `knowledge_category`
WHERE `delete_flag` = 'N'
ORDER BY sort_order;
