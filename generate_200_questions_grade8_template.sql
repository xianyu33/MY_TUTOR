-- Generate 200 Grade 8 Math Questions (English with French Translation)
-- Categories: Number and Algebra (80), Geometry (60), Statistics (30), Comprehensive (30)

USE tutor;

-- Grade 8 Knowledge Points assumed: 
-- NUMBER_ALGEBRA: 201-210 (Quadratic Radicals, Quadratic Equations, Functions, etc.)
-- GEOMETRY: 211-220 (Triangles, Quadrilaterals, Circles, etc.)
-- STATISTICS: 221-225 (Data Analysis, Probability, etc.)
-- COMPREHENSIVE: 226-230 (Similar Triangles, Trigonometry, etc.)

-- =============================================
-- SECTION 1: Number and Algebra (80 questions)
-- =============================================

-- Quadratic Radicals (10 questions)
INSERT INTO `question` (`knowledge_point_id`, `question_type`, `question_title`, `question_title_fr`, `question_content`, `question_content_fr`, `options`, `options_fr`, `correct_answer`, `correct_answer_fr`, `answer_explanation`, `answer_explanation_fr`, `difficulty_level`, `points`, `sort_order`) VALUES

(201, 1, 'Quadratic Radicals Concept', 'Concept des radicaux quadratiques', 'Which of the following is a quadratic radical?', 'Lequel des suivants est un radical quadratique ?',
'["A. √4", "B. ∛8", "C. ∜16", "D. √-1"]',
'["A. √4", "B. ∛8", "C. ∜16", "D. √-1"]',
'A', 'A', '√4 is a quadratic radical (square root)', '√4 est un radical quadratique (racine carrée)', 1, 2, 1),

(201, 1, 'Simplifying Radicals', 'Simplification des radicaux', 'The value of √36 is', 'La valeur de √36 est',
'["A. 6", "B. 12", "C. 18", "D. 36"]',
'["A. 6", "B. 12", "C. 18", "D. 36"]',
'A', 'A', '6×6=36, so √36=6', '6×6=36, donc √36=6', 1, 2, 2),

(201, 1, 'Radical Simplification', 'Simplification radicale', 'Simplify: √18', 'Simplifier : √18',
'["A. 3√2", "B. 2√3", "C. 6√3", "D. 9√2"]',
'["A. 3√2", "B. 2√3", "C. 6√3", "D. 9√2"]',
'A', 'A', '√18=√(9×2)=3√2', '√18=√(9×2)=3√2', 2, 2, 3);

-- Continue with rest of questions...
-- Note: Due to length, creating a Python script to generate all 200 questions
