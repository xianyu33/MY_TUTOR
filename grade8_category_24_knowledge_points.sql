-- Grade 8 Knowledge Points for Category 2 (Geometry) and Category 4 (Comprehensive Application)
-- All in English, for database insertion

USE tutor;

-- =============================================
-- Category 2: Geometry (14 knowledge points)
-- =============================================

INSERT INTO `knowledge_point` (`grade_id`, `category_id`, `point_name`, `point_code`, `description`, `content`, `difficulty_level`, `sort_order`, `learning_objectives`) VALUES

-- Geometry Knowledge Points
(8, 2, 'Triangles', 'TRIANGLE', 'Concept and classification of triangles', 'Understand the concept of triangles, master their classification', 1, 1, 'Be able to identify and classify various triangles'),

(8, 2, 'Properties of Triangles', 'TRIANGLE_PROPERTIES', 'Basic properties of triangles', 'Master the basic properties of triangles', 2, 2, 'Be able to apply basic properties of triangles to solve problems'),

(8, 2, 'Congruent Triangles', 'CONGRUENT_TRIANGLES', 'Concept and criteria for congruent triangles', 'Understand the concept of congruent triangles, master the criteria for congruence', 3, 3, 'Be able to determine if two triangles are congruent'),

(8, 2, 'Properties of Congruent Triangles', 'CONGRUENT_TRIANGLE_PROPERTIES', 'Properties of congruent triangles', 'Master the properties of congruent triangles', 3, 4, 'Be able to apply properties of congruent triangles to solve problems'),

(8, 2, 'Isosceles Triangles', 'ISOSCELES_TRIANGLE', 'Concept and properties of isosceles triangles', 'Understand the concept of isosceles triangles, master their properties', 2, 5, 'Be able to identify isosceles triangles, master their properties'),

(8, 2, 'Equilateral Triangles', 'EQUILATERAL_TRIANGLE', 'Concept and properties of equilateral triangles', 'Understand the concept of equilateral triangles, master their properties', 2, 6, 'Be able to identify equilateral triangles, master their properties'),

(8, 2, 'Right Triangles', 'RIGHT_TRIANGLE', 'Concept and properties of right triangles', 'Understand the concept of right triangles, master their properties', 2, 7, 'Be able to identify right triangles, master their properties'),

(8, 2, 'Pythagorean Theorem', 'PYTHAGOREAN_THEOREM', 'Pythagorean theorem and its converse', 'Master the Pythagorean theorem and its converse', 3, 8, 'Be able to apply the Pythagorean theorem to solve problems'),

(8, 2, 'Quadrilaterals', 'QUADRILATERAL', 'Concept and classification of quadrilaterals', 'Understand the concept of quadrilaterals, master their classification', 1, 9, 'Be able to identify and classify various quadrilaterals'),

(8, 2, 'Parallelograms', 'PARALLELOGRAM', 'Concept and properties of parallelograms', 'Understand the concept of parallelograms, master their properties', 2, 10, 'Be able to identify parallelograms, master their properties'),

(8, 2, 'Rectangles', 'RECTANGLE', 'Concept and properties of rectangles', 'Understand the concept of rectangles, master their properties', 2, 11, 'Be able to identify rectangles, master their properties'),

(8, 2, 'Rhombuses', 'RHOMBUS', 'Concept and properties of rhombuses', 'Understand the concept of rhombuses, master their properties', 2, 12, 'Be able to identify rhombuses, master their properties'),

(8, 2, 'Squares', 'SQUARE', 'Concept and properties of squares', 'Understand the concept of squares, master their properties', 2, 13, 'Be able to identify squares, master their properties'),

(8, 2, 'Trapezoids', 'TRAPEZOID', 'Concept and properties of trapezoids', 'Understand the concept of trapezoids, master their properties', 2, 14, 'Be able to identify trapezoids, master their properties')

ON DUPLICATE KEY UPDATE 
    description=VALUES(description), 
    content=VALUES(content), 
    difficulty_level=VALUES(difficulty_level), 
    sort_order=VALUES(sort_order),
    learning_objectives=VALUES(learning_objectives);

-- =============================================
-- Category 4: Comprehensive Application (7 knowledge points)
-- =============================================

INSERT INTO `knowledge_point` (`grade_id`, `category_id`, `point_name`, `point_code`, `description`, `content`, `difficulty_level`, `sort_order`, `learning_objectives`) VALUES

-- Comprehensive Application Knowledge Points
(8, 4, 'Functions', 'FUNCTION', 'Concept and representation of functions', 'Understand the concept of functions, master their representation methods', 3, 1, 'Be able to understand the concept of functions, master their representation methods'),

(8, 4, 'Linear Functions', 'LINEAR_FUNCTION', 'Concept and properties of linear functions', 'Understand the concept of linear functions, master their properties', 3, 2, 'Be able to identify linear functions, master their properties'),

(8, 4, 'Graphs of Linear Functions', 'LINEAR_FUNCTION_GRAPH', 'Graphs and properties of linear functions', 'Master the graphs and properties of linear functions', 3, 3, 'Be able to draw graphs of linear functions, understand their properties'),

(8, 4, 'Applications of Linear Functions', 'LINEAR_FUNCTION_APPLICATION', 'Applications of linear functions', 'Master the applications of linear functions in real-world problems', 3, 4, 'Be able to apply linear functions to solve real-world problems'),

(8, 4, 'Inverse Proportion Functions', 'INVERSE_PROPORTION_FUNCTION', 'Concept and properties of inverse proportion functions', 'Understand the concept of inverse proportion functions, master their properties', 3, 5, 'Be able to identify inverse proportion functions, master their properties'),

(8, 4, 'Graphs of Inverse Proportion Functions', 'INVERSE_PROPORTION_FUNCTION_GRAPH', 'Graphs and properties of inverse proportion functions', 'Master the graphs and properties of inverse proportion functions', 3, 6, 'Be able to draw graphs of inverse proportion functions, understand their properties'),

(8, 4, 'Applications of Inverse Proportion Functions', 'INVERSE_PROPORTION_FUNCTION_APPLICATION', 'Applications of inverse proportion functions', 'Master the applications of inverse proportion functions in real-world problems', 3, 7, 'Be able to apply inverse proportion functions to solve real-world problems')

ON DUPLICATE KEY UPDATE 
    description=VALUES(description), 
    content=VALUES(content), 
    difficulty_level=VALUES(difficulty_level), 
    sort_order=VALUES(sort_order),
    learning_objectives=VALUES(learning_objectives);

-- =============================================
-- Verification Query
-- =============================================

-- Query to verify inserted knowledge points
SELECT 
    kp.id,
    kp.grade_id,
    kp.category_id,
    kc.category_name,
    kp.point_name,
    kp.point_code,
    kp.difficulty_level,
    kp.sort_order,
    kp.learning_objectives
FROM knowledge_point kp
JOIN knowledge_category kc ON kp.category_id = kc.id
WHERE kp.grade_id = 8 
    AND kp.category_id IN (2, 4)
    AND kp.delete_flag = 'N'
ORDER BY kp.category_id, kp.sort_order;

-- Summary statistics
SELECT 
    category_id,
    kc.category_name,
    COUNT(*) as knowledge_point_count,
    SUM(CASE WHEN difficulty_level = 1 THEN 1 ELSE 0 END) as easy_count,
    SUM(CASE WHEN difficulty_level = 2 THEN 1 ELSE 0 END) as medium_count,
    SUM(CASE WHEN difficulty_level = 3 THEN 1 ELSE 0 END) as hard_count
FROM knowledge_point kp
JOIN knowledge_category kc ON kp.category_id = kc.id
WHERE kp.grade_id = 8 
    AND kp.category_id IN (2, 4)
    AND kp.delete_flag = 'N'
GROUP BY category_id, kc.category_name
ORDER BY category_id;
