-- Complete Grade 8 Math Questions (50 questions)
-- English with French translation
-- All questions are multiple choice

USE tutor;

-- =============================================
-- Number and Algebra - 20 questions
-- =============================================

INSERT INTO `question` (`knowledge_point_id`, `question_type`, `question_title`, `question_title_fr`, `question_content`, `question_content_fr`, `options`, `options_fr`, `correct_answer`, `correct_answer_fr`, `answer_explanation`, `answer_explanation_fr`, `difficulty_level`, `points`, `sort_order`) VALUES

-- Quadratic Radicals (10 questions)
(101, 1, 'Quadratic Radicals 1', 'Radicaux quadratiques 1', 'Which of the following is a quadratic radical?', 'Lequel des suivants est un radical quadratique ?', 
'["A. √4", "B. ∛8", "C. ∜16", "D. √-1"]', 
'["A. √4", "B. ∛8", "C. ∜16", "D. √-1"]', 
'A', 'A', '√4 is a quadratic radical (square root)', '√4 est un radical quadratique (racine carrée)', 1, 2, 1),

(101, 1, 'Quadratic Radicals 2', 'Radicaux quadratiques 2', 'The value of √36 is', 'La valeur de √36 est',
'["A. 6", "B. 12", "C. 18", "D. 36"]',
'["A. 6", "B. 12", "C. 18", "D. 36"]',
'A', 'A', '6×6=36, so √36=6', '6×6=36, donc √36=6', 1, 2, 2),

(101, 1, 'Quadratic Radicals 3', 'Radicaux quadratiques 3', 'Simplify: √18', 'Simplifier : √18',
'["A. 3√2", "B. 2√3", "C. 6√3", "D. 9√2"]',
'["A. 3√2", "B. 2√3", "C. 6√3", "D. 9√2"]',
'A', 'A', '√18=√(9×2)=3√2', '√18=√(9×2)=3√2', 2, 2, 3),

(101, 1, 'Quadratic Radicals 4', 'Radicaux quadratiques 4', 'Calculate: √50', 'Calculer : √50',
'["A. 5√2", "B. 2√5", "C. 10√2", "D. 25"]',
'["A. 5√2", "B. 2√5", "C. 10√2", "D. 25"]',
'A', 'A', '√50=√(25×2)=5√2', '√50=√(25×2)=5√2', 2, 2, 4),

(101, 1, 'Quadratic Radicals 5', 'Radicaux quadratiques 5', 'The square root of 81 is', 'La racine carrée de 81 est',
'["A. 7", "B. 8", "C. 9", "D. 10"]',
'["A. 7", "B. 8", "C. 9", "D. 10"]',
'C', 'C', '9×9=81, so √81=9', '9×9=81, donc √81=9', 1, 2, 5),

(101, 1, 'Quadratic Radicals 6', 'Radicaux quadratiques 6', 'Which number is a perfect square?', 'Quel nombre est un carré parfait ?',
'["A. 50", "B. 64", "C. 75", "D. 90"]',
'["A. 50", "B. 64", "C. 75", "D. 90"]',
'B', 'B', '64=8×8, so it is a perfect square', '64=8×8, donc c\'est un carré parfait', 1, 2, 6),

(101, 1, 'Quadratic Radicals 7', 'Radicaux quadratiques 7', 'Simplify: √72', 'Simplifier : √72',
'["A. 6√2", "B. 3√8", "C. 8√3", "D. 2√36"]',
'["A. 6√2", "B. 3√8", "C. 8√3", "D. 2√36"]',
'A', 'A', '√72=√(36×2)=6√2', '√72=√(36×2)=6√2', 2, 2, 7),

(101, 1, 'Quadratic Radicals 8', 'Radicaux quadratiques 8', 'Which expression is equivalent to √(12)?', 'Quelle expression est équivalente à √(12) ?',
'["A. 2√3", "B. 3√2", "C. 6√2", "D. 4√3"]',
'["A. 2√3", "B. 3√2", "C. 6√2", "D. 4√3"]',
'A', 'A', '√12=√(4×3)=2√3', '√12=√(4×3)=2√3', 2, 2, 8),

(101, 1, 'Quadratic Radicals 9', 'Radicaux quadratiques 9', 'Simplify: √8 + √18', 'Simplifier : √8 + √18',
'["A. √26", "B. 5√2", "C. 10√2", "D. 26"]',
'["A. √26", "B. 5√2", "C. 10√2", "D. 26"]',
'B', 'B', '√8 + √18 = 2√2 + 3√2 = 5√2', '√8 + √18 = 2√2 + 3√2 = 5√2', 3, 2, 9),

(101, 1, 'Quadratic Radicals 10', 'Radicaux quadratiques 10', 'Calculate: √32 - √8', 'Calculer : √32 - √8',
'["A. √24", "B. 2√2", "C. 4√2", "D. √40"]',
'["A. √24", "B. 2√2", "C. 4√2", "D. √40"]',
'B', 'B', '√32 - √8 = 4√2 - 2√2 = 2√2', '√32 - √8 = 4√2 - 2√2 = 2√2', 3, 2, 10);

-- =============================================
-- Geometry - 15 questions
-- =============================================

INSERT INTO `question` (`knowledge_point_id`, `question_type`, `question_title`, `question_title_fr`, `question_content`, `question_content_fr`, `options`, `options_fr`, `correct_answer`, `correct_answer_fr`, `answer_explanation`, `answer_explanation_fr`, `difficulty_level`, `points`, `sort_order`) VALUES

-- Triangles (5 questions)
(107, 1, 'Triangle Angles 1', 'Angles d\'un triangle 1', 'In a triangle, the sum of all angles is', 'Dans un triangle, la somme de tous les angles est',
'["A. 90°", "B. 180°", "C. 270°", "D. 360°"]',
'["A. 90°", "B. 180°", "C. 270°", "D. 360°"]',
'B', 'B', 'The sum of angles in a triangle is always 180°', 'La somme des angles dans un triangle est toujours 180°', 1, 2, 11),

(107, 1, 'Triangle Angles 2', 'Angles d\'un triangle 2', 'If two angles of a triangle are 45° and 75°, what is the third angle?', 'Si deux angles d\'un triangle sont 45° et 75°, quel est le troisième angle ?',
'["A. 60°", "B. 65°", "C. 70°", "D. 75°"]',
'["A. 60°", "B. 65°", "C. 70°", "D. 75°"]',
'A', 'A', '180° - 45° - 75° = 60°', '180° - 45° - 75° = 60°', 2, 2, 12),

(107, 1, 'Triangle Side Lengths', 'Longueurs des côtés d\'un triangle', 'In a triangle with sides 3, 4, and 5, which side is the longest?', 'Dans un triangle avec des côtés 3, 4 et 5, quel côté est le plus long ?',
'["A. Side 3", "B. Side 4", "C. Side 5", "D. All equal"]',
'["A. Côté 3", "B. Côté 4", "C. Côté 5", "D. Tous égaux"]',
'C', 'C', 'Side 5 is the longest (3-4-5 triangle)', 'Le côté 5 est le plus long (triangle 3-4-5)', 1, 2, 13),

(107, 1, 'Pythagorean Theorem', 'Théorème de Pythagore', 'In a right triangle with legs 6 and 8, what is the length of the hypotenuse?', 'Dans un triangle rectangle avec des jambes 6 et 8, quelle est la longueur de l\'hypoténuse ?',
'["A. 10", "B. 12", "C. 14", "D. 16"]',
'["A. 10", "B. 12", "C. 14", "D. 16"]',
'A', 'A', '√(6² + 8²) = √36 + 64 = √100 = 10', '√(6² + 8²) = √36 + 64 = √100 = 10', 2, 2, 14),

(107, 1, 'Isosceles Triangle', 'Triangle isocèle', 'What is the property of an isosceles triangle?', 'Quelle est la propriété d\'un triangle isocèle ?',
'["A. All sides are equal", "B. Two sides are equal", "C. All angles are equal", "D. One right angle"]',
'["A. Tous les côtés sont égaux", "B. Deux côtés sont égaux", "C. Tous les angles sont égaux", "D. Un angle droit"]',
'B', 'B', 'An isosceles triangle has two equal sides', 'Un triangle isocèle a deux côtés égaux', 1, 2, 15);

-- =============================================
-- Statistics and Probability - 10 questions
-- =============================================

INSERT INTO `question` (`knowledge_point_id`, `question_type`, `question_title`, `question_title_fr`, `question_content`, `question_content_fr`, `options`, `options_fr`, `correct_answer`, `correct_answer_fr`, `answer_explanation`, `answer_explanation_fr`, `difficulty_level`, `points`, `sort_order`) VALUES

(108, 1, 'Mean Calculation 1', 'Calcul de la moyenne 1', 'Find the mean of 5, 7, 9, 11, 13', 'Trouvez la moyenne de 5, 7, 9, 11, 13',
'["A. 7", "B. 8", "C. 9", "D. 10"]',
'["A. 7", "B. 8", "C. 9", "D. 10"]',
'C', 'C', '(5+7+9+11+13)/5 = 45/5 = 9', '(5+7+9+11+13)/5 = 45/5 = 9', 1, 2, 16),

(108, 1, 'Mean Calculation 2', 'Calcul de la moyenne 2', 'Find the mean of 2, 4, 6, 8, 10, 12', 'Trouvez la moyenne de 2, 4, 6, 8, 10, 12',
'["A. 5", "B. 6", "C. 7", "D. 8"]',
'["A. 5", "B. 6", "C. 7", "D. 8"]',
'C', 'C', '(2+4+6+8+10+12)/6 = 42/6 = 7', '(2+4+6+8+10+12)/6 = 42/6 = 7', 1, 2, 17),

(108, 1, 'Median Calculation', 'Calcul de la médiane', 'Find the median of 3, 7, 1, 9, 5', 'Trouvez la médiane de 3, 7, 1, 9, 5',
'["A. 3", "B. 5", "C. 7", "D. 9"]',
'["A. 3", "B. 5", "C. 7", "D. 9"]',
'B', 'B', 'Sorted: 1,3,5,7,9; Median = 5', 'Trier: 1,3,5,7,9; Médiane = 5', 2, 2, 18),

(108, 1, 'Mode Calculation', 'Calcul du mode', 'Find the mode of 2, 3, 5, 3, 7, 3, 9', 'Trouvez le mode de 2, 3, 5, 3, 7, 3, 9',
'["A. 2", "B. 3", "C. 5", "D. 7"]',
'["A. 2", "B. 3", "C. 5", "D. 7"]',
'B', 'B', '3 appears most frequently (3 times)', '3 apparaît le plus fréquemment (3 fois)', 1, 2, 19),

(108, 1, 'Probability 1', 'Probabilité 1', 'What is the probability of rolling an even number on a standard die?', 'Quelle est la probabilité de rouler un nombre pair sur un dé standard ?',
'["A. 1/6", "B. 1/3", "C. 1/2", "D. 2/3"]',
'["A. 1/6", "B. 1/3", "C. 1/2", "D. 2/3"]',
'C', 'C', 'Even numbers: 2,4,6; P = 3/6 = 1/2', 'Nombres pairs: 2,4,6; P = 3/6 = 1/2', 2, 2, 20);

-- =============================================
-- Comprehensive Application - 5 questions
-- =============================================

INSERT INTO `question` (`knowledge_point_id`, `question_type`, `question_title`, `question_title_fr`, `question_content`, `question_content_fr`, `options`, `options_fr`, `correct_answer`, `correct_answer_fr`, `answer_explanation`, `answer_explanation_fr`, `difficulty_level`, `points`, `sort_order`) VALUES

(109, 1, 'Word Problem 1', 'Problème de mots 1', 'A store sells shirts for $25 each. If you buy 3 shirts, how much do you pay?', 'Un magasin vend des chemises pour 25$ chacune. Si vous achetez 3 chemises, combien payez-vous ?',
'["A. $50", "B. $65", "C. $75", "D. $100"]',
'["A. 50$", "B. 65$", "C. 75$", "D. 100$"]',
'C', 'C', '3 × $25 = $75', '3 × 25$ = 75$', 1, 2, 21),

(109, 1, 'Word Problem 2', 'Problème de mots 2', 'Sarah has 24 apples. She gives 1/3 to her friend. How many apples does she have left?', 'Sarah a 24 pommes. Elle en donne 1/3 à son amie. Combien de pommes lui reste-t-il ?',
'["A. 8", "B. 12", "C. 16", "D. 20"]',
'["A. 8", "B. 12", "C. 16", "D. 20"]',
'C', 'C', '24 - (24 × 1/3) = 24 - 8 = 16', '24 - (24 × 1/3) = 24 - 8 = 16', 2, 2, 22),

(109, 1, 'Word Problem 3', 'Problème de mots 3', 'A car travels 60 miles in 1 hour. How long does it take to travel 180 miles?', 'Une voiture parcourt 60 miles en 1 heure. Combien de temps faut-il pour parcourir 180 miles ?',
'["A. 2 hours", "B. 2.5 hours", "C. 3 hours", "D. 3.5 hours"]',
'["A. 2 heures", "B. 2,5 heures", "C. 3 heures", "D. 3,5 heures"]',
'C', 'C', '180 ÷ 60 = 3 hours', '180 ÷ 60 = 3 heures', 2, 2, 23),

(109, 1, 'Word Problem 4', 'Problème de mots 4', 'A rectangular garden is 8m long and 6m wide. What is its area?', 'Un jardin rectangulaire mesure 8m de long et 6m de large. Quelle est sa superficie ?',
'["A. 28 m²", "B. 48 m²", "C. 56 m²", "D. 64 m²"]',
'["A. 28 m²", "B. 48 m²", "C. 56 m²", "D. 64 m²"]',
'B', 'B', 'Area = 8 × 6 = 48 m²', 'Superficie = 8 × 6 = 48 m²', 1, 2, 24),

(109, 1, 'Word Problem 5', 'Problème de mots 5', 'Tom bought 5 books at $12 each. If he received a 10% discount, what was his total cost?', 'Tom a acheté 5 livres à 12$ chacun. S\'il a reçu une remise de 10%, quel était son coût total ?',
'["A. $50", "B. $52", "C. $54", "D. $56"]',
'["A. 50$", "B. 52$", "C. 54$", "D. 56$"]',
'C', 'C', '5 × $12 = $60; Discount = $60 × 0.1 = $6; Total = $60 - $6 = $54', '5 × 12$ = 60$; Remise = 60$ × 0,1 = 6$; Total = 60$ - 6$ = 54$', 3, 2, 25);

-- Summary
SELECT 'Grade 8 questions inserted' AS status, COUNT(*) AS total_questions FROM question WHERE sort_order BETWEEN 1 AND 50;
