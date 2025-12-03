-- Update Knowledge Category Icons to Font Awesome
-- Replace Element UI icons with Font Awesome icons

USE tutor;

-- Update icon classes to Font Awesome
UPDATE `knowledge_category` SET 
    `icon_class` = 'fas fa-calculator',
    `update_at` = NOW()
WHERE `category_code` = 'NUMBER_ALGEBRA' 
   OR `category_name` = 'Number and Algebra'
   OR `id` = 1;

UPDATE `knowledge_category` SET 
    `icon_class` = 'fas fa-shapes',
    `update_at` = NOW()
WHERE `category_code` = 'GEOMETRY'
   OR `category_name` = 'Geometry'
   OR `id` = 2;

UPDATE `knowledge_category` SET 
    `icon_class` = 'fas fa-chart-bar',
    `update_at` = NOW()
WHERE `category_code` = 'STATISTICS_PROBABILITY'
   OR `category_name` = 'Statistics and Probability'
   OR `id` = 3;

UPDATE `knowledge_category` SET 
    `icon_class` = 'fas fa-puzzle-piece',
    `update_at` = NOW()
WHERE `category_code` = 'COMPREHENSIVE'
   OR `category_name` = 'Comprehensive Application'
   OR `id` = 4;

-- Query to verify updates
SELECT 
    id,
    category_name,
    category_code,
    icon_class,
    update_at
FROM `knowledge_category`
WHERE `delete_flag` = 'N'
ORDER BY id;
