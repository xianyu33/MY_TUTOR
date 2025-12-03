-- Knowledge Category Data Insertion Script (English Version)
-- Insert knowledge categories for junior high school mathematics

-- Insert knowledge categories with English names and descriptions
INSERT INTO knowledge_category (category_name, category_code, description, sort_order) VALUES
('Number and Algebra', 'NUMBER_ALGEBRA', 'Number recognition, operations, algebraic expressions, equations, and inequalities', 1),
('Geometry', 'GEOMETRY', 'Shape recognition, measurement, transformations, and spatial relationships', 2),
('Statistics and Probability', 'STATISTICS_PROBABILITY', 'Data collection, organization, analysis, and probability calculations', 3),
('Comprehensive Application', 'COMPREHENSIVE', 'Comprehensive application of mathematical knowledge to solve real-world problems', 4)
ON DUPLICATE KEY UPDATE 
description=VALUES(description), 
sort_order=VALUES(sort_order);

-- Query to verify the inserted categories
SELECT 
    id,
    category_name,
    category_code,
    description,
    sort_order,
    create_at
FROM knowledge_category 
WHERE delete_flag = 'N'
ORDER BY sort_order ASC;

-- Query to show the relationship between categories and knowledge points
SELECT 
    kc.id as category_id,
    kc.category_name,
    kc.category_code,
    COUNT(kp.id) as knowledge_point_count,
    GROUP_CONCAT(DISTINCT g.grade_name ORDER BY g.grade_level) as grades_covered
FROM knowledge_category kc
LEFT JOIN knowledge_point kp ON kc.id = kp.category_id AND kp.delete_flag = 'N'
LEFT JOIN grade g ON kp.grade_id = g.id AND g.delete_flag = 'N'
WHERE kc.delete_flag = 'N'
GROUP BY kc.id, kc.category_name, kc.category_code
ORDER BY kc.sort_order ASC;

