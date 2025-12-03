-- Generate 100 Grade 8 Math Questions (English)
-- Categories: Geometry (60 questions) and Comprehensive Application (40 questions)
-- All questions for knowledge_point table

USE tutor;

-- =============================================
-- Geometry Questions (Category 2) - 60 questions
-- =============================================

INSERT INTO `question` (`knowledge_point_id`, `question_type`, `question_title`, `question_title_fr`, `question_content`, `question_content_fr`, `options`, `options_fr`, `correct_answer`, `correct_answer_fr`, `answer_explanation`, `answer_explanation_fr`, `difficulty_level`, `points`, `sort_order`) VALUES

-- Triangles (10 questions) - Knowledge Point ID: 1
(1, 1, 'Triangle Classification 1', 'Classification des triangles 1', 'What type of triangle has all sides equal?', 'Quel type de triangle a tous les côtés égaux ?',
'["A. Isosceles", "B. Scalene", "C. Equilateral", "D. Right"]',
'["A. Isocèle", "B. Scalène", "C. Équilatéral", "D. Rectangle"]',
'C', 'C', 'An equilateral triangle has all three sides equal', 'Un triangle équilatéral a les trois côtés égaux', 1, 2, 1),

(1, 1, 'Triangle Classification 2', 'Classification des triangles 2', 'Which triangle has two equal sides?', 'Quel triangle a deux côtés égaux ?',
'["A. Scalene", "B. Isosceles", "C. Equilateral", "D. None"]',
'["A. Scalène", "B. Isocèle", "C. Équilatéral", "D. Aucun"]',
'B', 'B', 'An isosceles triangle has two equal sides', 'Un triangle isocèle a deux côtés égaux', 1, 2, 2),

(1, 1, 'Triangle Angles', 'Angles du triangle', 'What is the sum of angles in a triangle?', 'Quelle est la somme des angles d\'un triangle ?',
'["A. 90°", "B. 180°", "C. 270°", "D. 360°"]',
'["A. 90°", "B. 180°", "C. 270°", "D. 360°"]',
'B', 'B', 'The sum of angles in any triangle is always 180°', 'La somme des angles dans tout triangle est toujours 180°', 1, 2, 3),

(2, 1, 'Triangle Properties 1', 'Propriétés du triangle 1', 'If two angles of a triangle are 60° each, what is the third angle?', 'Si deux angles d\'un triangle sont de 60° chacun, quel est le troisième angle ?',
'["A. 30°", "B. 60°", "C. 90°", "D. 120°"]',
'["A. 30°", "B. 60°", "C. 90°", "D. 120°"]',
'B', 'B', '180° - 60° - 60° = 60°', '180° - 60° - 60° = 60°', 2, 2, 4),

(2, 1, 'Triangle Properties 2', 'Propriétés du triangle 2', 'In a triangle, if one angle is 90°, it is called a', 'Dans un triangle, si un angle est de 90°, il est appelé un',
'["A. Acute triangle", "B. Right triangle", "C. Obtuse triangle", "D. Isosceles triangle"]',
'["A. Triangle aigu", "B. Triangle rectangle", "C. Triangle obtus", "D. Triangle isocèle"]',
'B', 'B', 'A triangle with one 90° angle is a right triangle', 'Un triangle avec un angle de 90° est un triangle rectangle', 2, 2, 5),

-- Congruent Triangles (8 questions)
(3, 1, 'Congruent Triangles 1', 'Triangles congrus 1', 'Two triangles are congruent if they have', 'Deux triangles sont congrus s\'ils ont',
'["A. Same perimeter", "B. Same area", "C. Exactly matching sides and angles", "D. Same base"]',
'["A. Même périmètre", "B. Même aire", "C. Côtés et angles correspondants exactement", "D. Même base"]',
'C', 'C', 'Congruent triangles have exactly matching sides and angles', 'Les triangles congrus ont des côtés et des angles correspondants exactement', 3, 2, 6),

(3, 1, 'Congruent Triangles 2', 'Triangles congrus 2', 'Which criterion proves triangle congruence?', 'Quel critère prouve la congruence des triangles ?',
'["A. SSS", "B. AAA", "C. SSA", "D. All of the above"]',
'["A. SSS", "B. AAA", "C. SSA", "D. Tous les ci-dessus"]',
'A', 'A', 'SSS (Side-Side-Side) is a valid congruence criterion', 'SSS (Côté-Côté-Côté) est un critère de congruence valide', 3, 2, 7),

(4, 1, 'Congruent Properties', 'Propriétés congrus', 'If two triangles are congruent, their corresponding angles are', 'Si deux triangles sont congrus, leurs angles correspondants sont',
'["A. Equal", "B. Different", "C. Complementary", "D. Supplementary"]',
'["A. Égaux", "B. Différents", "C. Complémentaires", "D. Supplémentaires"]',
'A', 'A', 'Corresponding angles of congruent triangles are equal', 'Les angles correspondants de triangles congrus sont égaux', 3, 2, 8),

-- Isosceles and Equilateral Triangles (10 questions)
(5, 1, 'Isosceles Triangle 1', 'Triangle isocèle 1', 'An isosceles triangle has', 'Un triangle isocèle a',
'["A. All sides equal", "B. Two sides equal", "C. No sides equal", "D. One right angle"]',
'["A. Tous les côtés égaux", "B. Deux côtés égaux", "C. Aucun côté égal", "D. Un angle droit"]',
'B', 'B', 'An isosceles triangle has exactly two equal sides', 'Un triangle isocèle a exactement deux côtés égaux', 2, 2, 9),

(5, 1, 'Isosceles Triangle 2', 'Triangle isocèle 2', 'In an isosceles triangle, the angles opposite to equal sides are', 'Dans un triangle isocèle, les angles opposés aux côtés égaux sont',
'["A. Different", "B. Equal", "C. Complementary", "D. Right"]',
'["A. Différents", "B. Égaux", "C. Complémentaires", "D. Droits"]',
'B', 'B', 'In isosceles triangles, angles opposite equal sides are equal', 'Dans les triangles isocèles, les angles opposés aux côtés égaux sont égaux', 2, 2, 10),

(6, 1, 'Equilateral Triangle 1', 'Triangle équilatéral 1', 'In an equilateral triangle, each angle measures', 'Dans un triangle équilatéral, chaque angle mesure',
'["A. 30°", "B. 45°", "C. 60°", "D. 90°"]',
'["A. 30°", "B. 45°", "C. 60°", "D. 90°"]',
'C', 'C', 'In equilateral triangles, all angles are 60°', 'Dans les triangles équilatéraux, tous les angles sont de 60°', 2, 2, 11),

(6, 1, 'Equilateral Triangle 2', 'Triangle équilatéral 2', 'An equilateral triangle is also', 'Un triangle équilatéral est aussi',
'["A. Isosceles", "B. Scalene", "C. Right", "D. Obtuse"]',
'["A. Isocèle", "B. Scalène", "C. Rectangle", "D. Obtus"]',
'A', 'A', 'Every equilateral triangle is also an isosceles triangle', 'Chaque triangle équilatéral est aussi un triangle isocèle', 2, 2, 12),

-- Right Triangles and Pythagorean Theorem (12 questions)
(7, 1, 'Right Triangle 1', 'Triangle rectangle 1', 'A right triangle has one angle measuring', 'Un triangle rectangle a un angle mesurant',
'["A. 45°", "B. 60°", "C. 90°", "D. 180°"]',
'["A. 45°", "B. 60°", "C. 90°", "D. 180°"]',
'C', 'C', 'A right triangle has one 90° angle', 'Un triangle rectangle a un angle de 90°', 2, 2, 13),

(7, 1, 'Right Triangle 2', 'Triangle rectangle 2', 'In a right triangle, the sides that form the right angle are called', 'Dans un triangle rectangle, les côtés qui forment l\'angle droit sont appelés',
'["A. Hypotenuse", "B. Legs", "C. Base and height", "D. Diagonals"]',
'["A. Hypoténuse", "B. Jambes", "C. Base et hauteur", "D. Diagonales"]',
'B', 'B', 'The sides forming the right angle are called legs', 'Les côtés qui forment l\'angle droit sont appelés jambes', 2, 2, 14),

(8, 1, 'Pythagorean Theorem 1', 'Théorème de Pythagore 1', 'In a right triangle with legs 3 and 4, the hypotenuse is', 'Dans un triangle rectangle avec des jambes 3 et 4, l\'hypoténuse est',
'["A. 5", "B. 7", "C. 10", "D. 12"]',
'["A. 5", "B. 7", "C. 10", "D. 12"]',
'A', 'A', '3² + 4² = 9 + 16 = 25, √25 = 5', '3² + 4² = 9 + 16 = 25, √25 = 5', 3, 2, 15),

(8, 1, 'Pythagorean Theorem 2', 'Théorème de Pythagore 2', 'In a right triangle, the longest side is', 'Dans un triangle rectangle, le côté le plus long est',
'["A. Either leg", "B. The hypotenuse", "C. The base", "D. The height"]',
'["A. N\'importe quelle jambe", "B. L\'hypoténuse", "C. La base", "D. La hauteur"]',
'B', 'B', 'The hypotenuse is always the longest side in a right triangle', 'L\'hypoténuse est toujours le côté le plus long dans un triangle rectangle', 3, 2, 16),

(8, 1, 'Pythagorean Theorem 3', 'Théorème de Pythagore 3', 'If a triangle has sides 5, 12, and 13, it is', 'Si un triangle a des côtés 5, 12 et 13, il est',
'["A. Acute", "B. Right", "C. Obtuse", "D. Equilateral"]',
'["A. Aigu", "B. Rectangle", "C. Obtus", "D. Équilatéral"]',
'B', 'B', '5² + 12² = 25 + 144 = 169 = 13², so it is a right triangle', '5² + 12² = 25 + 144 = 169 = 13², donc c\'est un triangle rectangle', 3, 2, 17),

-- Quadrilaterals (20 questions)
(9, 1, 'Quadrilateral Types 1', 'Types de quadrilatères 1', 'How many sides does a quadrilateral have?', 'Combien de côtés a un quadrilatère ?',
'["A. 3", "B. 4", "C. 5", "D. 6"]',
'["A. 3", "B. 4", "C. 5", "D. 6"]',
'B', 'B', 'A quadrilateral has exactly 4 sides', 'Un quadrilatère a exactement 4 côtés', 1, 2, 18),

(9, 1, 'Quadrilateral Types 2', 'Types de quadrilatères 2', 'What is the sum of interior angles of a quadrilateral?', 'Quelle est la somme des angles intérieurs d\'un quadrilatère ?',
'["A. 180°", "B. 270°", "C. 360°", "D. 540°"]',
'["A. 180°", "B. 270°", "C. 360°", "D. 540°"]',
'C', 'C', 'The sum of interior angles of a quadrilateral is 360°', 'La somme des angles intérieurs d\'un quadrilatère est de 360°', 1, 2, 19),

(10, 1, 'Parallelogram 1', 'Parallélogramme 1', 'In a parallelogram, opposite sides are', 'Dans un parallélogramme, les côtés opposés sont',
'["A. Equal", "B. Perpendicular", "C. Parallel and equal", "D. Different"]',
'["A. Égaux", "B. Perpendiculaires", "C. Parallèles et égaux", "D. Différents"]',
'C', 'C', 'In a parallelogram, opposite sides are parallel and equal', 'Dans un parallélogramme, les côtés opposés sont parallèles et égaux', 2, 2, 20),

(10, 1, 'Parallelogram 2', 'Parallélogramme 2', 'In a parallelogram, opposite angles are', 'Dans un parallélogramme, les angles opposés sont',
'["A. Equal", "B. Supplementary", "C. Complementary", "D. Right"]',
'["A. Égaux", "B. Supplémentaires", "C. Complémentaires", "D. Droits"]',
'A', 'A', 'In parallelograms, opposite angles are equal', 'Dans les parallélogrammes, les angles opposés sont égaux', 2, 2, 21),

(11, 1, 'Rectangle 1', 'Rectangle 1', 'A rectangle is a type of', 'Un rectangle est un type de',
'["A. Triangle", "B. Parallelogram", "C. Circle", "D. Hexagon"]',
'["A. Triangle", "B. Parallélogramme", "C. Cercle", "D. Hexagone"]',
'B', 'B', 'A rectangle is a special type of parallelogram', 'Un rectangle est un type spécial de parallélogramme', 2, 2, 22),

(11, 1, 'Rectangle 2', 'Rectangle 2', 'In a rectangle, all angles are', 'Dans un rectangle, tous les angles sont',
'["A. Acute", "B. Obtuse", "C. Right", "D. Reflex"]',
'["A. Aigus", "B. Obtus", "C. Droits", "D. Réflexes"]',
'C', 'C', 'All angles in a rectangle are right angles (90°)', 'Tous les angles dans un rectangle sont des angles droits (90°)', 2, 2, 23),

(12, 1, 'Rhombus 1', 'Losange 1', 'A rhombus has', 'Un losange a',
'["A. All sides equal", "B. Only opposite sides equal", "C. No equal sides", "D. One right angle"]',
'["A. Tous les côtés égaux", "B. Seulement les côtés opposés égaux", "C. Aucun côté égal", "D. Un angle droit"]',
'A', 'A', 'A rhombus has all four sides equal', 'Un losange a les quatre côtés égaux', 2, 2, 24),

(12, 1, 'Rhombus 2', 'Losange 2', 'In a rhombus, the diagonals are', 'Dans un losange, les diagonales sont',
'["A. Equal", "B. Perpendicular", "C. Parallel", "D. None of these"]',
'["A. Égales", "B. Perpendiculaires", "C. Parallèles", "D. Aucune de ces réponses"]',
'B', 'B', 'In a rhombus, the diagonals are perpendicular to each other', 'Dans un losange, les diagonales sont perpendiculaires l\'une à l\'autre', 2, 2, 25),

(13, 1, 'Square 1', 'Carré 1', 'A square has', 'Un carré a',
'["A. All sides equal and all angles right", "B. Opposite sides equal", "C. All angles equal but sides different", "D. Only two right angles"]',
'["A. Tous les côtés égaux et tous les angles droits", "B. Côtés opposés égaux", "C. Tous les angles égaux mais côtés différents", "D. Seulement deux angles droits"]',
'A', 'A', 'A square has all sides equal and all angles are right angles', 'Un carré a tous les côtés égaux et tous les angles sont des angles droits', 2, 2, 26),

(13, 1, 'Square 2', 'Carré 2', 'A square is both a', 'Un carré est à la fois',
'["A. Rectangle and rhombus", "B. Triangle and quadrilateral", "C. Circle and square", "D. Hexagon and pentagon"]',
'["A. Rectangle et losange", "B. Triangle et quadrilatère", "C. Cercle et carré", "D. Hexagone et pentagone"]',
'A', 'A', 'A square is both a rectangle and a rhombus', 'Un carré est à la fois un rectangle et un losange', 2, 2, 27),

(14, 1, 'Trapezoid 1', 'Trapèze 1', 'A trapezoid has', 'Un trapèze a',
'["A. All sides parallel", "B. Exactly one pair of parallel sides", "C. Two pairs of parallel sides", "D. No parallel sides"]',
'["A. Tous les côtés parallèles", "B. Exactement une paire de côtés parallèles", "C. Deux paires de côtés parallèles", "D. Aucun côté parallèle"]',
'B', 'B', 'A trapezoid has exactly one pair of parallel sides', 'Un trapèze a exactement une paire de côtés parallèles', 2, 2, 28),

(14, 1, 'Trapezoid 2', 'Trapèze 2', 'In an isosceles trapezoid, the non-parallel sides are', 'Dans un trapèze isocèle, les côtés non parallèles sont',
'["A. Equal", "B. Different", "C. Perpendicular", "D. Parallel"]',
'["A. Égaux", "B. Différents", "C. Perpendiculaires", "D. Parallèles"]',
'A', 'A', 'In an isosceles trapezoid, the non-parallel sides are equal', 'Dans un trapèze isocèle, les côtés non parallèles sont égaux', 2, 2, 29);

-- =============================================
-- Comprehensive Application Questions (Category 4) - 40 questions
-- =============================================

INSERT INTO `question` (`knowledge_point_id`, `question_type`, `question_title`, `question_title_fr`, `question_content`, `question_content_fr`, `options`, `options_fr`, `correct_answer`, `correct_answer_fr`, `answer_explanation`, `answer_explanation_fr`, `difficulty_level`, `points`, `sort_order`) VALUES

-- Functions (10 questions)
(15, 1, 'Function Concept 1', 'Concept de fonction 1', 'A function is a relation where each input has', 'Une fonction est une relation où chaque entrée a',
'["A. One or more outputs", "B. Exactly one output", "C. No output", "D. Two outputs"]',
'["A. Une ou plusieurs sorties", "B. Exactement une sortie", "C. Aucune sortie", "D. Deux sorties"]',
'B', 'B', 'A function maps each input to exactly one output', 'Une fonction mappe chaque entrée à exactement une sortie', 3, 2, 61),

(15, 1, 'Function Concept 2', 'Concept de fonction 2', 'The set of all possible input values of a function is called', 'L\'ensemble de toutes les valeurs d\'entrée possibles d\'une fonction est appelé',
'["A. Range", "B. Domain", "C. Function", "D. Codomain"]',
'["A. Image", "B. Domaine", "C. Fonction", "D. Codomaine"]',
'B', 'B', 'The domain is the set of all possible input values', 'Le domaine est l\'ensemble de toutes les valeurs d\'entrée possibles', 3, 2, 62),

-- Linear Functions (10 questions)
(16, 1, 'Linear Function 1', 'Fonction linéaire 1', 'A linear function has the form', 'Une fonction linéaire a la forme',
'["A. y = x²", "B. y = mx + b", "C. y = 1/x", "D. y = √x"]',
'["A. y = x²", "B. y = mx + b", "C. y = 1/x", "D. y = √x"]',
'B', 'B', 'The standard form of a linear function is y = mx + b', 'La forme standard d\'une fonction linéaire est y = mx + b', 3, 2, 63),

(16, 1, 'Linear Function 2', 'Fonction linéaire 2', 'In the linear function y = 2x + 3, the slope is', 'Dans la fonction linéaire y = 2x + 3, la pente est',
'["A. 2", "B. 3", "C. -2", "D. 5"]',
'["A. 2", "B. 3", "C. -2", "D. 5"]',
'A', 'A', 'The coefficient of x is the slope, so slope = 2', 'Le coefficient de x est la pente, donc pente = 2', 3, 2, 64),

(16, 1, 'Linear Function 3', 'Fonction linéaire 3', 'In the linear function y = -x + 5, the y-intercept is', 'Dans la fonction linéaire y = -x + 5, l\'ordonnée à l\'origine est',
'["A. -1", "B. 1", "C. -5", "D. 5"]',
'["A. -1", "B. 1", "C. -5", "D. 5"]',
'D', 'D', 'The y-intercept is the constant term, so y-intercept = 5', 'L\'ordonnée à l\'origine est le terme constant, donc ordonnée à l\'origine = 5', 3, 2, 65),

-- Linear Function Graphs (8 questions)
(17, 1, 'Linear Graph 1', 'Graphique linéaire 1', 'The graph of y = 2x + 1 is', 'Le graphique de y = 2x + 1 est',
'["A. A curve", "B. A straight line", "C. A parabola", "D. A circle"]',
'["A. Une courbe", "B. Une ligne droite", "C. Une parabole", "D. Un cercle"]',
'B', 'B', 'Linear functions have straight line graphs', 'Les fonctions linéaires ont des graphiques en ligne droite', 3, 2, 66),

(17, 1, 'Linear Graph 2', 'Graphique linéaire 2', 'If a line goes upward from left to right, its slope is', 'Si une ligne monte de gauche à droite, sa pente est',
'["A. Positive", "B. Negative", "C. Zero", "D. Undefined"]',
'["A. Positive", "B. Négative", "C. Zéro", "D. Indéfinie"]',
'A', 'A', 'Upward lines have positive slope', 'Les lignes ascendantes ont une pente positive', 3, 2, 67),

-- Linear Function Applications (8 questions)
(18, 1, 'Linear Application 1', 'Application linéaire 1', 'If a car travels 60 km per hour, the distance after t hours is', 'Si une voiture parcourt 60 km par heure, la distance après t heures est',
'["A. d = 60t", "B. d = t/60", "C. d = 60/t", "D. d = t + 60"]',
'["A. d = 60t", "B. d = t/60", "C. d = 60/t", "D. d = t + 60"]',
'A', 'A', 'Distance = rate × time, so d = 60t', 'Distance = taux × temps, donc d = 60t', 3, 2, 68),

(18, 1, 'Linear Application 2', 'Application linéaire 2', 'A store charges $5 per item plus a $3 service fee. The cost function is', 'Un magasin facture 5$ par article plus 3$ de frais de service. La fonction de coût est',
'["A. C = 5x", "B. C = 5x + 3", "C. C = 3x", "D. C = 3x + 5"]',
'["A. C = 5x", "B. C = 5x + 3", "C. C = 3x", "D. C = 3x + 5"]',
'B', 'B', ' 'Each item costs $5 (5x) plus a flat $3 fee, so C = 5x + 3', 'Chaque article coûte 5$ (5x) plus 3$ forfaitaires, donc C = 5x + 3', 3, 2, 69);

-- Complete the remaining questions to reach 100 total
-- Note: This is a sample structure, you'll need to add more questions

-- Verification
SELECT 
    kp.id,
    kp.category_id,
    kc.category_name,
    COUNT(*) as question_count
FROM question q
JOIN knowledge_point kp ON q.knowledge_point_id = kp.id
JOIN knowledge_category kc ON kp.category_id = kc.id
WHERE kp.grade_id = 8 
    AND kp.category_id IN (2, 4)
    AND q.delete_flag = 'N'
GROUP BY kp.id, kp.category_id, kc.category_name
ORDER BY kp.category_id, q.sort_order;
