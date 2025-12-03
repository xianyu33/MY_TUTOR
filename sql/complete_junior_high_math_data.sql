-- Complete Junior High School Mathematics Data Setup Script
-- This script sets up all required data for junior high school mathematics knowledge points
-- Execute this script in the following order:

-- Step 1: Insert Knowledge Categories
-- ====================================
-- Insert knowledge categories with English names and descriptions
INSERT INTO knowledge_category (category_name, category_code, description, sort_order) VALUES
('Number and Algebra', 'NUMBER_ALGEBRA', 'Number recognition, operations, algebraic expressions, equations, and inequalities', 1),
('Geometry', 'GEOMETRY', 'Shape recognition, measurement, transformations, and spatial relationships', 2),
('Statistics and Probability', 'STATISTICS_PROBABILITY', 'Data collection, organization, analysis, and probability calculations', 3),
('Comprehensive Application', 'COMPREHENSIVE', 'Comprehensive application of mathematical knowledge to solve real-world problems', 4)
ON DUPLICATE KEY UPDATE 
description=VALUES(description), 
sort_order=VALUES(sort_order);

-- Step 2: Verify Categories Inserted
-- ===================================
-- Check that all required categories exist
SELECT 
    id,
    category_name,
    category_code,
    description,
    sort_order
FROM knowledge_category 
WHERE delete_flag = 'N'
ORDER BY sort_order ASC;

-- Step 3: Insert Knowledge Points
-- ===============================
-- Grade 7 Mathematics Knowledge Points
INSERT INTO knowledge_point (grade_id, category_id, point_name, point_code, description, content, difficulty_level, sort_order, learning_objectives) VALUES

-- Number and Algebra Knowledge Points
(7, 1, 'Positive and Negative Numbers', 'POSITIVE_NEGATIVE_NUMBERS', 'Understanding positive and negative numbers', 'Understand the concept of positive and negative numbers, master their representation and comparison', 1, 1, 'Be able to identify positive and negative numbers, understand their position relationship on the number line'),
(7, 1, 'Rational Numbers', 'RATIONAL_NUMBERS', 'Concept and classification of rational numbers', 'Understand the definition of rational numbers, master their classification and representation', 2, 2, 'Be able to distinguish rational and irrational numbers, master rational number classification'),
(7, 1, 'Number Line', 'NUMBER_LINE', 'Understanding and application of number line', 'Understand the concept of number line, be able to represent rational numbers on the number line', 1, 3, 'Be able to accurately represent rational numbers on the number line, understand distance on the number line'),
(7, 1, 'Opposite Numbers', 'OPPOSITE_NUMBERS', 'Concept and properties of opposite numbers', 'Understand the definition of opposite numbers, master their properties', 1, 4, 'Be able to find the opposite of any number, understand the properties of opposite numbers'),
(7, 1, 'Absolute Value', 'ABSOLUTE_VALUE', 'Concept and calculation of absolute value', 'Understand the definition of absolute value, master the calculation methods', 2, 5, 'Be able to calculate the absolute value of any number, understand the geometric meaning of absolute value'),
(7, 1, 'Addition of Rational Numbers', 'RATIONAL_ADDITION', 'Addition operations with rational numbers', 'Master the rules and laws of addition with rational numbers', 2, 6, 'Be able to perform addition operations with rational numbers fluently, understand addition laws'),
(7, 1, 'Subtraction of Rational Numbers', 'RATIONAL_SUBTRACTION', 'Subtraction operations with rational numbers', 'Master the rules of subtraction with rational numbers', 2, 7, 'Be able to perform subtraction operations with rational numbers fluently, understand the relationship between subtraction and addition'),
(7, 1, 'Multiplication of Rational Numbers', 'RATIONAL_MULTIPLICATION', 'Multiplication operations with rational numbers', 'Master the rules and laws of multiplication with rational numbers', 2, 8, 'Be able to perform multiplication operations with rational numbers fluently, understand multiplication laws'),
(7, 1, 'Division of Rational Numbers', 'RATIONAL_DIVISION', 'Division operations with rational numbers', 'Master the rules of division with rational numbers', 2, 9, 'Be able to perform division operations with rational numbers fluently, understand the relationship between division and multiplication'),
(7, 1, 'Exponentiation of Rational Numbers', 'RATIONAL_POWER', 'Exponentiation operations with rational numbers', 'Understand the concept of exponentiation, master the rules of exponentiation', 3, 10, 'Be able to perform exponentiation operations with rational numbers, understand the meaning of exponentiation'),
(7, 1, 'Scientific Notation', 'SCIENTIFIC_NOTATION', 'Representation using scientific notation', 'Master the representation methods of scientific notation', 2, 11, 'Be able to represent large and small numbers using scientific notation'),
(7, 1, 'Approximate Numbers', 'APPROXIMATE_NUMBERS', 'Concept and representation of approximate numbers', 'Understand the concept of approximate numbers, master their representation methods', 2, 12, 'Be able to identify approximate numbers, master their representation methods'),

-- Geometry Knowledge Points
(7, 2, 'Solid Figures', 'SOLID_FIGURES', 'Understanding solid figures', 'Recognize common solid figures, understand their characteristics', 1, 1, 'Be able to identify common solid figures, understand their characteristics'),
(7, 2, 'Plane Figures', 'PLANE_FIGURES', 'Understanding plane figures', 'Recognize common plane figures, understand their characteristics', 1, 2, 'Be able to identify common plane figures, understand their characteristics'),
(7, 2, 'Point, Line, Plane', 'POINT_LINE_PLANE', 'Basic geometric elements', 'Understand the concepts and relationships of point, line, and plane', 1, 3, 'Understand the concepts and relationships of basic geometric elements'),
(7, 2, 'Angles', 'ANGLE', 'Concept and classification of angles', 'Understand the concept of angles, master their classification and representation', 2, 4, 'Be able to identify and represent various angles, understand angle classification'),
(7, 2, 'Angle Measurement', 'ANGLE_MEASUREMENT', 'Units and methods of angle measurement', 'Master the units and methods of angle measurement', 2, 5, 'Be able to accurately measure angle sizes, understand the degree system'),
(7, 2, 'Angle Operations', 'ANGLE_OPERATIONS', 'Addition and subtraction of angles', 'Master the methods of angle addition and subtraction', 2, 6, 'Be able to perform addition and subtraction operations with angles'),
(7, 2, 'Intersecting Lines', 'INTERSECTING_LINES', 'Concept and properties of intersecting lines', 'Understand the concept of intersecting lines, master their properties', 2, 7, 'Understand the concept and properties of intersecting lines'),
(7, 2, 'Parallel Lines', 'PARALLEL_LINES', 'Concept and determination of parallel lines', 'Understand the concept of parallel lines, master methods for determining parallel lines', 3, 8, 'Be able to determine if two lines are parallel, understand the properties of parallel lines'),
(7, 2, 'Properties of Parallel Lines', 'PARALLEL_LINE_PROPERTIES', 'Theorems about parallel line properties', 'Master the theorems about parallel line properties', 3, 9, 'Be able to apply parallel line properties to solve problems'),

-- Statistics and Probability Knowledge Points
(7, 3, 'Data Collection', 'DATA_COLLECTION', 'Methods of data collection', 'Understand methods and considerations for data collection', 1, 1, 'Understand basic methods of data collection'),
(7, 3, 'Data Organization', 'DATA_ORGANIZATION', 'Methods of data organization', 'Master basic methods of data organization', 1, 2, 'Be able to organize collected data'),
(7, 3, 'Data Description', 'DATA_DESCRIPTION', 'Descriptive statistics of data', 'Master descriptive statistical methods for data', 2, 3, 'Be able to describe data using statistical charts'),

-- Comprehensive Application Knowledge Points
(7, 4, 'Linear Equations in One Variable', 'LINEAR_EQUATION', 'Concept and solution of linear equations in one variable', 'Understand the concept of linear equations in one variable, master their solution methods', 3, 1, 'Be able to solve linear equations in one variable, understand the concept of equation solutions'),
(7, 4, 'Applications of Linear Equations', 'LINEAR_EQUATION_APPLICATION', 'Applications of linear equations in one variable', 'Master the applications of linear equations in real-world problems', 3, 2, 'Be able to apply linear equations to solve real-world problems'),
(7, 4, 'Linear Inequalities in One Variable', 'LINEAR_INEQUALITY', 'Concept and solution of linear inequalities in one variable', 'Understand the concept of linear inequalities in one variable, master their solution methods', 3, 3, 'Be able to solve linear inequalities in one variable, understand the concept of inequality solution sets'),
(7, 4, 'Systems of Linear Inequalities', 'LINEAR_INEQUALITY_SYSTEM', 'Solution of systems of linear inequalities', 'Master the solution methods for systems of linear inequalities', 3, 4, 'Be able to solve systems of linear inequalities, understand the concept of inequality system solution sets')

ON DUPLICATE KEY UPDATE 
description=VALUES(description), 
content=VALUES(content), 
difficulty_level=VALUES(difficulty_level), 
sort_order=VALUES(sort_order),
learning_objectives=VALUES(learning_objectives);

-- Grade 8 Mathematics Knowledge Points
INSERT INTO knowledge_point (grade_id, category_id, point_name, point_code, description, content, difficulty_level, sort_order, learning_objectives) VALUES

-- Number and Algebra Knowledge Points
(8, 1, 'Quadratic Radicals', 'QUADRATIC_RADICAL', 'Concept and properties of quadratic radicals', 'Understand the concept of quadratic radicals, master their properties', 2, 1, 'Be able to identify quadratic radicals, master their properties'),
(8, 1, 'Operations with Quadratic Radicals', 'QUADRATIC_RADICAL_OPERATIONS', 'Four operations with quadratic radicals', 'Master the four operations with quadratic radicals', 3, 2, 'Be able to perform four operations with quadratic radicals fluently'),
(8, 1, 'Quadratic Equations', 'QUADRATIC_EQUATION', 'Concept and solution of quadratic equations', 'Understand the concept of quadratic equations, master their solution methods', 3, 3, 'Be able to solve quadratic equations, understand the concept of equation solutions'),
(8, 1, 'Methods for Solving Quadratic Equations', 'QUADRATIC_EQUATION_SOLVING', 'Multiple methods for solving quadratic equations', 'Master completing the square, quadratic formula, and factoring methods', 3, 4, 'Be able to use multiple methods to solve quadratic equations'),
(8, 1, 'Discriminant of Quadratic Equations', 'QUADRATIC_DISCRIMINANT', 'Concept and application of discriminant', 'Understand the concept of discriminant, master its applications', 3, 5, 'Be able to use discriminant to determine the nature of quadratic equation solutions'),
(8, 1, 'Applications of Quadratic Equations', 'QUADRATIC_EQUATION_APPLICATION', 'Applications of quadratic equations', 'Master the applications of quadratic equations in real-world problems', 3, 6, 'Be able to apply quadratic equations to solve real-world problems'),
(8, 1, 'Rational Expressions', 'FRACTION', 'Concept and properties of rational expressions', 'Understand the concept of rational expressions, master their properties', 2, 7, 'Be able to identify rational expressions, master their properties'),
(8, 1, 'Operations with Rational Expressions', 'FRACTION_OPERATIONS', 'Four operations with rational expressions', 'Master the four operations with rational expressions', 3, 8, 'Be able to perform four operations with rational expressions fluently'),
(8, 1, 'Rational Equations', 'FRACTIONAL_EQUATION', 'Concept and solution of rational equations', 'Understand the concept of rational equations, master their solution methods', 3, 9, 'Be able to solve rational equations, understand the concept of equation solutions'),
(8, 1, 'Applications of Rational Equations', 'FRACTIONAL_EQUATION_APPLICATION', 'Applications of rational equations', 'Master the applications of rational equations in real-world problems', 3, 10, 'Be able to apply rational equations to solve real-world problems'),

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
(8, 2, 'Trapezoids', 'TRAPEZOID', 'Concept and properties of trapezoids', 'Understand the concept of trapezoids, master their properties', 2, 14, 'Be able to identify trapezoids, master their properties'),

-- Statistics and Probability Knowledge Points
(8, 3, 'Data Analysis', 'DATA_ANALYSIS', 'Methods of data analysis', 'Master the methods of data analysis', 2, 1, 'Be able to analyze data'),
(8, 3, 'Mean', 'MEAN', 'Concept and calculation of mean', 'Understand the concept of mean, master its calculation methods', 2, 2, 'Be able to calculate various types of means'),
(8, 3, 'Median', 'MEDIAN', 'Concept and calculation of median', 'Understand the concept of median, master its calculation methods', 2, 3, 'Be able to calculate median'),
(8, 3, 'Mode', 'MODE', 'Concept and calculation of mode', 'Understand the concept of mode, master its calculation methods', 2, 4, 'Be able to calculate mode'),
(8, 3, 'Variance', 'VARIANCE', 'Concept and calculation of variance', 'Understand the concept of variance, master its calculation methods', 3, 5, 'Be able to calculate variance'),
(8, 3, 'Standard Deviation', 'STANDARD_DEVIATION', 'Concept and calculation of standard deviation', 'Understand the concept of standard deviation, master its calculation methods', 3, 6, 'Be able to calculate standard deviation'),

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

-- Grade 9 Mathematics Knowledge Points
INSERT INTO knowledge_point (grade_id, category_id, point_name, point_code, description, content, difficulty_level, sort_order, learning_objectives) VALUES

-- Number and Algebra Knowledge Points
(9, 1, 'Quadratic Equations Review', 'QUADRATIC_EQUATION_REVIEW', 'Review and deepening of quadratic equations', 'Review the concept and solution methods of quadratic equations, deepen understanding', 3, 1, 'Be able to solve quadratic equations fluently, understand the concept of equation solutions'),
(9, 1, 'Relationship between Roots and Coefficients', 'QUADRATIC_EQUATION_ROOTS', 'Vieta\'s theorem', 'Master the relationship between roots and coefficients of quadratic equations (Vieta\'s theorem)', 3, 2, 'Be able to apply Vieta\'s theorem to solve problems'),
(9, 1, 'Applications of Quadratic Equations Review', 'QUADRATIC_EQUATION_APPLICATION_REVIEW', 'Deepening applications of quadratic equations', 'Deepen the applications of quadratic equations in real-world problems', 3, 3, 'Be able to apply quadratic equations to solve complex real-world problems'),
(9, 1, 'Quadratic Functions', 'QUADRATIC_FUNCTION', 'Concept and properties of quadratic functions', 'Understand the concept of quadratic functions, master their properties', 3, 4, 'Be able to identify quadratic functions, master their properties'),
(9, 1, 'Graphs of Quadratic Functions', 'QUADRATIC_FUNCTION_GRAPH', 'Graphs and properties of quadratic functions', 'Master the graphs and properties of quadratic functions', 3, 5, 'Be able to draw graphs of quadratic functions, understand their properties'),
(9, 1, 'Applications of Quadratic Functions', 'QUADRATIC_FUNCTION_APPLICATION', 'Applications of quadratic functions', 'Master the applications of quadratic functions in real-world problems', 3, 6, 'Be able to apply quadratic functions to solve real-world problems'),
(9, 1, 'Quadratic Inequalities', 'QUADRATIC_INEQUALITY', 'Concept and solution of quadratic inequalities', 'Understand the concept of quadratic inequalities, master their solution methods', 3, 7, 'Be able to solve quadratic inequalities, understand the concept of inequality solution sets'),
(9, 1, 'Systems of Quadratic Inequalities', 'QUADRATIC_INEQUALITY_SYSTEM', 'Solution of systems of quadratic inequalities', 'Master the solution methods for systems of quadratic inequalities', 3, 8, 'Be able to solve systems of quadratic inequalities, understand the concept of inequality system solution sets'),
(9, 1, 'Applications of Quadratic Inequalities', 'QUADRATIC_INEQUALITY_APPLICATION', 'Applications of quadratic inequalities', 'Master the applications of quadratic inequalities in real-world problems', 3, 9, 'Be able to apply quadratic inequalities to solve real-world problems'),

-- Geometry Knowledge Points
(9, 2, 'Circles', 'CIRCLE', 'Concept and properties of circles', 'Understand the concept of circles, master their basic properties', 2, 1, 'Be able to identify circles, master their basic properties'),
(9, 2, 'Symmetry of Circles', 'CIRCLE_SYMMETRY', 'Symmetry properties of circles', 'Master the symmetry properties of circles', 2, 2, 'Be able to understand the symmetry properties of circles'),
(9, 2, 'Perpendicular Chord Theorem', 'PERPENDICULAR_CHORD_THEOREM', 'Perpendicular chord theorem and its corollaries', 'Master the perpendicular chord theorem and its corollaries', 3, 3, 'Be able to apply the perpendicular chord theorem to solve problems'),
(9, 2, 'Central Angles', 'CENTRAL_ANGLE', 'Concept and properties of central angles', 'Understand the concept of central angles, master their properties', 2, 4, 'Be able to identify central angles, master their properties'),
(9, 2, 'Inscribed Angles', 'INSCRIBED_ANGLE', 'Concept and properties of inscribed angles', 'Understand the concept of inscribed angles, master their properties', 3, 5, 'Be able to identify inscribed angles, master their properties'),
(9, 2, 'Inscribed Angle Theorem', 'INSCRIBED_ANGLE_THEOREM', 'Inscribed angle theorem and its corollaries', 'Master the inscribed angle theorem and its corollaries', 3, 6, 'Be able to apply the inscribed angle theorem to solve problems'),
(9, 2, 'Inscribed Quadrilaterals', 'INSCRIBED_QUADRILATERAL', 'Properties of inscribed quadrilaterals', 'Master the properties of inscribed quadrilaterals', 3, 7, 'Be able to identify inscribed quadrilaterals, master their properties'),
(9, 2, 'Tangents', 'TANGENT', 'Concept and properties of tangents', 'Understand the concept of tangents, master their properties', 3, 8, 'Be able to identify tangents, master their properties'),
(9, 2, 'Tangent Length Theorem', 'TANGENT_LENGTH_THEOREM', 'Tangent length theorem', 'Master the tangent length theorem', 3, 9, 'Be able to apply the tangent length theorem to solve problems'),
(9, 2, 'Chord-Tangent Angles', 'CHORD_TANGENT_ANGLE', 'Concept and properties of chord-tangent angles', 'Understand the concept of chord-tangent angles, master their properties', 3, 10, 'Be able to identify chord-tangent angles, master their properties'),
(9, 2, 'Intersecting Chord Theorem', 'INTERSECTING_CHORD_THEOREM', 'Intersecting chord theorem', 'Master the intersecting chord theorem', 3, 11, 'Be able to apply the intersecting chord theorem to solve problems'),
(9, 2, 'Secant-Tangent Theorem', 'SECANT_TANGENT_THEOREM', 'Secant-tangent theorem', 'Master the secant-tangent theorem', 3, 12, 'Be able to apply the secant-tangent theorem to solve problems'),
(9, 2, 'Position Relationships of Circles', 'CIRCLE_POSITION_RELATIONSHIP', 'Position relationships between circles', 'Master the position relationships between circles', 3, 13, 'Be able to determine the position relationships between circles'),
(9, 2, 'Regular Polygons', 'REGULAR_POLYGON', 'Concept and properties of regular polygons', 'Understand the concept of regular polygons, master their properties', 2, 14, 'Be able to identify regular polygons, master their properties'),
(9, 2, 'Calculations with Regular Polygons', 'REGULAR_POLYGON_CALCULATION', 'Calculations with regular polygons', 'Master the calculation methods for regular polygons', 3, 15, 'Be able to calculate related quantities of regular polygons'),
(9, 2, 'Arc Length', 'ARC_LENGTH', 'Concept and calculation of arc length', 'Understand the concept of arc length, master its calculation methods', 2, 16, 'Be able to calculate arc length'),
(9, 2, 'Sector Area', 'SECTOR_AREA', 'Concept and calculation of sector area', 'Understand the concept of sector area, master its calculation methods', 2, 17, 'Be able to calculate sector area'),
(9, 2, 'Cones', 'CONE', 'Concept and properties of cones', 'Understand the concept of cones, master their properties', 2, 18, 'Be able to identify cones, master their properties'),
(9, 2, 'Calculations with Cones', 'CONE_CALCULATION', 'Calculations with cones', 'Master the calculation methods for cones', 3, 19, 'Be able to calculate related quantities of cones'),
(9, 2, 'Cylinders', 'CYLINDER', 'Concept and properties of cylinders', 'Understand the concept of cylinders, master their properties', 2, 20, 'Be able to identify cylinders, master their properties'),
(9, 2, 'Calculations with Cylinders', 'CYLINDER_CALCULATION', 'Calculations with cylinders', 'Master the calculation methods for cylinders', 3, 21, 'Be able to calculate related quantities of cylinders'),
(9, 2, 'Spheres', 'SPHERE', 'Concept and properties of spheres', 'Understand the concept of spheres, master their properties', 2, 22, 'Be able to identify spheres, master their properties'),
(9, 2, 'Calculations with Spheres', 'SPHERE_CALCULATION', 'Calculations with spheres', 'Master the calculation methods for spheres', 3, 23, 'Be able to calculate related quantities of spheres'),

-- Statistics and Probability Knowledge Points
(9, 3, 'Probability', 'PROBABILITY', 'Concept and calculation of probability', 'Understand the concept of probability, master its calculation methods', 2, 1, 'Be able to calculate probabilities of simple events'),
(9, 3, 'Applications of Probability', 'PROBABILITY_APPLICATION', 'Applications of probability', 'Master the applications of probability in real-world problems', 3, 2, 'Be able to apply probability to solve real-world problems'),
(9, 3, 'Statistics', 'STATISTICS', 'Concept and methods of statistics', 'Understand the concept of statistics, master statistical methods', 2, 3, 'Be able to perform simple statistical analysis'),
(9, 3, 'Applications of Statistics', 'STATISTICS_APPLICATION', 'Applications of statistics', 'Master the applications of statistics in real-world problems', 3, 4, 'Be able to apply statistical methods to solve real-world problems'),

-- Comprehensive Application Knowledge Points
(9, 4, 'Similar Triangles', 'SIMILAR_TRIANGLES', 'Concept and criteria for similar triangles', 'Understand the concept of similar triangles, master the criteria for similarity', 3, 1, 'Be able to determine if two triangles are similar'),
(9, 4, 'Properties of Similar Triangles', 'SIMILAR_TRIANGLE_PROPERTIES', 'Properties of similar triangles', 'Master the properties of similar triangles', 3, 2, 'Be able to apply properties of similar triangles to solve problems'),
(9, 4, 'Applications of Similar Triangles', 'SIMILAR_TRIANGLE_APPLICATION', 'Applications of similar triangles', 'Master the applications of similar triangles in real-world problems', 3, 3, 'Be able to apply similar triangles to solve real-world problems'),
(9, 4, 'Trigonometric Functions of Acute Angles', 'TRIGONOMETRIC_FUNCTIONS', 'Concept and calculation of trigonometric functions of acute angles', 'Understand the concept of trigonometric functions of acute angles, master their calculation methods', 3, 4, 'Be able to calculate trigonometric function values of acute angles'),
(9, 4, 'Applications of Trigonometric Functions', 'TRIGONOMETRIC_FUNCTIONS_APPLICATION', 'Applications of trigonometric functions of acute angles', 'Master the applications of trigonometric functions of acute angles in real-world problems', 3, 5, 'Be able to apply trigonometric functions of acute angles to solve real-world problems'),
(9, 4, 'Solving Right Triangles', 'SOLVING_RIGHT_TRIANGLE', 'Methods for solving right triangles', 'Master the methods for solving right triangles', 3, 6, 'Be able to solve right triangles'),
(9, 4, 'Applications of Solving Right Triangles', 'SOLVING_RIGHT_TRIANGLE_APPLICATION', 'Applications of solving right triangles', 'Master the applications of solving right triangles in real-world problems', 3, 7, 'Be able to apply methods for solving right triangles to solve real-world problems'),
(9, 4, 'Projections', 'PROJECTION', 'Concept and properties of projections', 'Understand the concept of projections, master their properties', 2, 8, 'Be able to understand the concept and properties of projections'),
(9, 4, 'Applications of Projections', 'PROJECTION_APPLICATION', 'Applications of projections', 'Master the applications of projections in real-world problems', 3, 9, 'Be able to apply projections to solve real-world problems')

ON DUPLICATE KEY UPDATE 
description=VALUES(description), 
content=VALUES(content), 
difficulty_level=VALUES(difficulty_level), 
sort_order=VALUES(sort_order),
learning_objectives=VALUES(learning_objectives);

-- Step 4: Final Verification
-- ===========================
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

-- Query insertion results
SELECT 
    g.grade_name,
    kc.category_name,
    kp.point_name,
    kp.point_code,
    kp.difficulty_level,
    kp.sort_order
FROM knowledge_point kp
JOIN grade g ON kp.grade_id = g.id
JOIN knowledge_category kc ON kp.category_id = kc.id
WHERE g.grade_level IN (7, 8, 9)
ORDER BY g.grade_level, kc.sort_order, kp.sort_order;

