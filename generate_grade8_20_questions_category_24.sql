-- Generate 20 Grade 8 Math Questions (English with French translations)
-- Categories: Category 2 (Geometry) and Category 4 (Comprehensive Application)
-- Difficulty distribution: 7 easy (1) + 7 medium (2) + 6 hard (3)
-- Question types: Multiple choice (1), Fill-in-blank (2), Calculation (3), Application (4)

USE tutor;

-- =============================================
-- Geometry Questions (Category 2) - 12 questions
-- =============================================

INSERT INTO `question` (`knowledge_point_id`, `question_type`, `question_title`, `question_title_fr`, `question_content`, `question_content_fr`, `options`, `options_fr`, `correct_answer`, `correct_answer_fr`, `answer_explanation`, `answer_explanation_fr`, `difficulty_level`, `points`, `sort_order`) VALUES

-- Easy Questions (Difficulty 1) - 4 questions
((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 2 AND point_code = 'TRIANGLE' AND delete_flag = 'N' LIMIT 1), 1, 'Triangle Classification', 'Classification des triangles', 'What type of triangle has all three sides equal in length?', 'Quel type de triangle a les trois côtés de longueur égale ?',
'["A. Isosceles triangle", "B. Scalene triangle", "C. Equilateral triangle", "D. Right triangle"]',
'["A. Triangle isocèle", "B. Triangle scalène", "C. Triangle équilatéral", "D. Triangle rectangle"]',
'C', 'C', 'An equilateral triangle has all three sides equal in length. This is a fundamental property of equilateral triangles.', 'Un triangle équilatéral a les trois côtés de longueur égale. C\'est une propriété fondamentale des triangles équilatéraux.', 1, 2, 1),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 2 AND point_code = 'QUADRILATERAL' AND delete_flag = 'N' LIMIT 1), 1, 'Quadrilateral Angles', 'Angles du quadrilatère', 'What is the sum of the interior angles of any quadrilateral?', 'Quelle est la somme des angles intérieurs de n\'importe quel quadrilatère ?',
'["A. 180°", "B. 270°", "C. 360°", "D. 450°"]',
'["A. 180°", "B. 270°", "C. 360°", "D. 450°"]',
'C', 'C', 'The sum of interior angles of any quadrilateral is always 360 degrees. This can be proven by dividing the quadrilateral into two triangles, each with 180°.', 'La somme des angles intérieurs de n\'importe quel quadrilatère est toujours de 360 degrés. On peut le prouver en divisant le quadrilatère en deux triangles, chacun ayant 180°.', 1, 2, 2),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 2 AND point_code = 'RECTANGLE' AND delete_flag = 'N' LIMIT 1), 1, 'Rectangle Properties', 'Propriétés du rectangle', 'How many right angles does a rectangle have?', 'Combien d\'angles droits un rectangle a-t-il ?',
'["A. 1", "B. 2", "C. 3", "D. 4"]',
'["A. 1", "B. 2", "C. 3", "D. 4"]',
'D', 'D', 'A rectangle has exactly four right angles, each measuring 90 degrees. This is a defining property of rectangles.', 'Un rectangle a exactement quatre angles droits, chacun mesurant 90 degrés. C\'est une propriété définissante des rectangles.', 1, 2, 3),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 2 AND point_code = 'SQUARE' AND delete_flag = 'N' LIMIT 1), 1, 'Square Classification', 'Classification du carré', 'A square is a special type of which shape?', 'Un carré est un type spécial de quelle forme ?',
'["A. Triangle", "B. Parallelogram", "C. Circle", "D. Pentagon"]',
'["A. Triangle", "B. Parallélogramme", "C. Cercle", "D. Pentagone"]',
'B', 'B', 'A square is a special type of parallelogram with all sides equal and all angles equal to 90 degrees.', 'Un carré est un type spécial de parallélogramme avec tous les côtés égaux et tous les angles égaux à 90 degrés.', 1, 2, 4),

-- Medium Questions (Difficulty 2) - 4 questions
((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 2 AND point_code = 'TRIANGLE_PROPERTIES' AND delete_flag = 'N' LIMIT 1), 2, 'Triangle Angle Calculation', 'Calcul d\'angle de triangle', 'In a triangle, if two angles measure 45° and 60°, what is the measure of the third angle?', 'Dans un triangle, si deux angles mesurent 45° et 60°, quelle est la mesure du troisième angle ?',
NULL, NULL,
'75°', '75°', 'The sum of angles in a triangle is 180°. Therefore, the third angle = 180° - 45° - 60° = 75°.', 'La somme des angles dans un triangle est de 180°. Par conséquent, le troisième angle = 180° - 45° - 60° = 75°.', 2, 3, 5),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 2 AND point_code = 'ISOSCELES_TRIANGLE' AND delete_flag = 'N' LIMIT 1), 2, 'Isosceles Triangle Property', 'Propriété du triangle isocèle', 'In an isosceles triangle, if the base angles each measure 40°, what is the measure of the vertex angle?', 'Dans un triangle isocèle, si les angles de base mesurent chacun 40°, quelle est la mesure de l\'angle au sommet ?',
NULL, NULL,
'100°', '100°', 'In an isosceles triangle, the base angles are equal. Sum of all angles is 180°, so vertex angle = 180° - 40° - 40° = 100°.', 'Dans un triangle isocèle, les angles de base sont égaux. La somme de tous les angles est de 180°, donc l\'angle au sommet = 180° - 40° - 40° = 100°.', 2, 3, 6),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 2 AND point_code = 'PARALLELOGRAM' AND delete_flag = 'N' LIMIT 1), 1, 'Parallelogram Angles', 'Angles du parallélogramme', 'In a parallelogram, if one angle measures 120°, what is the measure of its adjacent angle?', 'Dans un parallélogramme, si un angle mesure 120°, quelle est la mesure de son angle adjacent ?',
'["A. 30°", "B. 60°", "C. 90°", "D. 120°"]',
'["A. 30°", "B. 60°", "C. 90°", "D. 120°"]',
'B', 'B', 'In a parallelogram, adjacent angles are supplementary (sum to 180°). Therefore, the adjacent angle = 180° - 120° = 60°.', 'Dans un parallélogramme, les angles adjacents sont supplémentaires (somme à 180°). Par conséquent, l\'angle adjacent = 180° - 120° = 60°.', 2, 2, 7),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 2 AND point_code = 'RHOMBUS' AND delete_flag = 'N' LIMIT 1), 3, 'Rhombus Area Calculation', 'Calcul de l\'aire du losange', 'A rhombus has diagonals of lengths 8 cm and 6 cm. Calculate its area.', 'Un losange a des diagonales de longueurs 8 cm et 6 cm. Calculez son aire.',
NULL, NULL,
'24 cm²', '24 cm²', 'The area of a rhombus is half the product of its diagonals. Area = (1/2) × 8 × 6 = 24 cm².', 'L\'aire d\'un losange est la moitié du produit de ses diagonales. Aire = (1/2) × 8 × 6 = 24 cm².', 2, 4, 8),

-- Hard Questions (Difficulty 3) - 4 questions
((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 2 AND point_code = 'CONGRUENT_TRIANGLES' AND delete_flag = 'N' LIMIT 1), 1, 'Triangle Congruence Criteria', 'Critères de congruence des triangles', 'Which of the following is NOT a valid criterion for triangle congruence?', 'Lequel des suivants n\'est PAS un critère valide pour la congruence des triangles ?',
'["A. SSS (Side-Side-Side)", "B. SAS (Side-Angle-Side)", "C. AAA (Angle-Angle-Angle)", "D. ASA (Angle-Side-Angle)"]',
'["A. SSS (Côté-Côté-Côté)", "B. SAS (Côté-Angle-Côté)", "C. AAA (Angle-Angle-Angle)", "D. ASA (Angle-Côté-Angle)"]',
'C', 'C', 'AAA (Angle-Angle-Angle) is not a valid congruence criterion because triangles with the same angles can have different side lengths (similar but not necessarily congruent).', 'AAA (Angle-Angle-Angle) n\'est pas un critère de congruence valide car les triangles avec les mêmes angles peuvent avoir des longueurs de côtés différentes (similaires mais pas nécessairement congrus).', 3, 3, 9),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 2 AND point_code = 'PYTHAGOREAN_THEOREM' AND delete_flag = 'N' LIMIT 1), 3, 'Pythagorean Theorem Application', 'Application du théorème de Pythagore', 'A right triangle has one leg of 12 cm and a hypotenuse of 15 cm. Find the length of the other leg.', 'Un triangle rectangle a une jambe de 12 cm et une hypoténuse de 15 cm. Trouvez la longueur de l\'autre jambe.',
NULL, NULL,
'9 cm', '9 cm', 'Using the Pythagorean theorem: a² + b² = c², where c is the hypotenuse. So, 12² + b² = 15², which gives 144 + b² = 225. Therefore, b² = 81, so b = 9 cm.', 'En utilisant le théorème de Pythagore : a² + b² = c², où c est l\'hypoténuse. Donc, 12² + b² = 15², ce qui donne 144 + b² = 225. Par conséquent, b² = 81, donc b = 9 cm.', 3, 4, 10),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 2 AND point_code = 'CONGRUENT_TRIANGLE_PROPERTIES' AND delete_flag = 'N' LIMIT 1), 4, 'Congruent Triangles Problem', 'Problème de triangles congrus', 'In the diagram, triangle ABC is congruent to triangle DEF. If angle A measures 50° and side AB = 8 cm, what is the measure of angle D and the length of side DE?', 'Dans le diagramme, le triangle ABC est congruent au triangle DEF. Si l\'angle A mesure 50° et le côté AB = 8 cm, quelle est la mesure de l\'angle D et la longueur du côté DE ?',
NULL, NULL,
'Angle D = 50°, DE = 8 cm', 'Angle D = 50°, DE = 8 cm', 'When triangles are congruent, corresponding angles are equal and corresponding sides are equal. Since angle A corresponds to angle D, angle D = 50°. Since AB corresponds to DE, DE = 8 cm.', 'Lorsque les triangles sont congrus, les angles correspondants sont égaux et les côtés correspondants sont égaux. Puisque l\'angle A correspond à l\'angle D, angle D = 50°. Puisque AB correspond à DE, DE = 8 cm.', 3, 5, 11),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 2 AND point_code = 'PYTHAGOREAN_THEOREM' AND delete_flag = 'N' LIMIT 1), 4, 'Pythagorean Theorem Word Problem', 'Problème de mot avec théorème de Pythagore', 'A ladder 10 meters long leans against a wall. The foot of the ladder is 6 meters away from the base of the wall. How high up the wall does the ladder reach?', 'Une échelle de 10 mètres de long s\'appuie contre un mur. Le pied de l\'échelle est à 6 mètres de la base du mur. À quelle hauteur sur le mur l\'échelle atteint-elle ?',
NULL, NULL,
'8 meters', '8 mètres', 'This forms a right triangle with the ladder as the hypotenuse (10 m), distance from wall as one leg (6 m), and height as the other leg (h). Using Pythagorean theorem: 6² + h² = 10², so 36 + h² = 100, h² = 64, h = 8 meters.', 'Cela forme un triangle rectangle avec l\'échelle comme hypoténuse (10 m), la distance du mur comme une jambe (6 m), et la hauteur comme l\'autre jambe (h). En utilisant le théorème de Pythagore : 6² + h² = 10², donc 36 + h² = 100, h² = 64, h = 8 mètres.', 3, 5, 12),

-- =============================================
-- Comprehensive Application Questions (Category 4) - 8 questions
-- =============================================

-- Easy Questions (Difficulty 1) - 3 questions
((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'FUNCTION' AND delete_flag = 'N' LIMIT 1), 1, 'Function Definition', 'Définition de fonction', 'A function is a relation where each input has exactly how many outputs?', 'Une fonction est une relation où chaque entrée a exactement combien de sorties ?',
'["A. Zero outputs", "B. One output", "C. Two outputs", "D. One or more outputs"]',
'["A. Zéro sortie", "B. Une sortie", "C. Deux sorties", "D. Une ou plusieurs sorties"]',
'B', 'B', 'By definition, a function maps each input to exactly one output. This is what distinguishes a function from a general relation.', 'Par définition, une fonction associe chaque entrée à exactement une sortie. C\'est ce qui distingue une fonction d\'une relation générale.', 1, 2, 13),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'FUNCTION' AND delete_flag = 'N' LIMIT 1), 1, 'Function Domain and Range', 'Domaine et image d\'une fonction', 'What is the set of all possible input values of a function called?', 'Comment s\'appelle l\'ensemble de toutes les valeurs d\'entrée possibles d\'une fonction ?',
'["A. Range", "B. Domain", "C. Codomain", "D. Function"]',
'["A. Image", "B. Domaine", "C. Codomaine", "D. Fonction"]',
'B', 'B', 'The domain of a function is the set of all possible input values (x-values) for which the function is defined.', 'Le domaine d\'une fonction est l\'ensemble de toutes les valeurs d\'entrée possibles (valeurs x) pour lesquelles la fonction est définie.', 1, 2, 14),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'LINEAR_FUNCTION' AND delete_flag = 'N' LIMIT 1), 1, 'Linear Function Form', 'Forme de fonction linéaire', 'What is the standard form of a linear function?', 'Quelle est la forme standard d\'une fonction linéaire ?',
'["A. y = x²", "B. y = mx + b", "C. y = 1/x", "D. y = √x"]',
'["A. y = x²", "B. y = mx + b", "C. y = 1/x", "D. y = √x"]',
'B', 'B', 'The standard form of a linear function is y = mx + b, where m is the slope and b is the y-intercept.', 'La forme standard d\'une fonction linéaire est y = mx + b, où m est la pente et b est l\'ordonnée à l\'origine.', 1, 2, 15),

-- Medium Questions (Difficulty 2) - 3 questions
((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'LINEAR_FUNCTION' AND delete_flag = 'N' LIMIT 1), 2, 'Linear Function Slope', 'Pente de fonction linéaire', 'In the linear function y = -3x + 7, what is the slope?', 'Dans la fonction linéaire y = -3x + 7, quelle est la pente ?',
NULL, NULL,
'-3', '-3', 'In the form y = mx + b, the coefficient of x is the slope. Here, m = -3, so the slope is -3.', 'Dans la forme y = mx + b, le coefficient de x est la pente. Ici, m = -3, donc la pente est -3.', 2, 3, 16),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'LINEAR_FUNCTION_GRAPH' AND delete_flag = 'N' LIMIT 1), 1, 'Linear Graph Characteristics', 'Caractéristiques du graphique linéaire', 'What type of graph does a linear function produce?', 'Quel type de graphique une fonction linéaire produit-elle ?',
'["A. A curve", "B. A straight line", "C. A parabola", "D. A circle"]',
'["A. Une courbe", "B. Une ligne droite", "C. Une parabole", "D. Un cercle"]',
'B', 'B', 'Linear functions produce straight line graphs. This is because the rate of change (slope) is constant throughout the function.', 'Les fonctions linéaires produisent des graphiques en ligne droite. C\'est parce que le taux de changement (pente) est constant tout au long de la fonction.', 2, 2, 17),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'LINEAR_FUNCTION_APPLICATION' AND delete_flag = 'N' LIMIT 1), 3, 'Linear Function Calculation', 'Calcul de fonction linéaire', 'If a car travels at a constant speed of 65 km/h, write a function that represents the distance traveled after t hours, and find the distance after 3 hours.', 'Si une voiture roule à une vitesse constante de 65 km/h, écrivez une fonction qui représente la distance parcourue après t heures, et trouvez la distance après 3 heures.',
NULL, NULL,
'd(t) = 65t, 195 km', 'd(t) = 65t, 195 km', 'Distance = speed × time, so d(t) = 65t. After 3 hours: d(3) = 65 × 3 = 195 km.', 'Distance = vitesse × temps, donc d(t) = 65t. Après 3 heures : d(3) = 65 × 3 = 195 km.', 2, 4, 18),

-- Hard Questions (Difficulty 3) - 2 questions
((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'LINEAR_FUNCTION_APPLICATION' AND delete_flag = 'N' LIMIT 1), 4, 'Linear Function Word Problem', 'Problème de mot avec fonction linéaire', 'A phone company charges a monthly fee of $20 plus $0.10 per text message. Write a function C(x) that represents the total monthly cost for x text messages, and determine how much it costs to send 150 messages.', 'Une compagnie de téléphonie facture un abonnement mensuel de 20$ plus 0,10$ par message texte. Écrivez une fonction C(x) qui représente le coût mensuel total pour x messages texte, et déterminez combien il en coûte pour envoyer 150 messages.',
NULL, NULL,
'C(x) = 20 + 0.10x, $35', 'C(x) = 20 + 0,10x, 35$', 'The function is C(x) = 20 + 0.10x, where 20 is the fixed monthly fee and 0.10x is the variable cost for x messages. For 150 messages: C(150) = 20 + 0.10(150) = 20 + 15 = $35.', 'La fonction est C(x) = 20 + 0,10x, où 20 est l\'abonnement mensuel fixe et 0,10x est le coût variable pour x messages. Pour 150 messages : C(150) = 20 + 0,10(150) = 20 + 15 = 35$.', 3, 5, 19),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'INVERSE_PROPORTION_FUNCTION_APPLICATION' AND delete_flag = 'N' LIMIT 1), 4, 'Inverse Proportion Application', 'Application de proportion inverse', 'If 8 workers can complete a job in 12 days, how many days would it take 6 workers to complete the same job? Assume the work rate is constant.', 'Si 8 travailleurs peuvent terminer un travail en 12 jours, combien de jours faudrait-il à 6 travailleurs pour terminer le même travail ? Supposons que le taux de travail soit constant.',
NULL, NULL,
'16 days', '16 jours', 'This is an inverse proportion problem. More workers means fewer days. Work = workers × days = constant. 8 × 12 = 96 worker-days. For 6 workers: 96 ÷ 6 = 16 days.', 'C\'est un problème de proportion inverse. Plus de travailleurs signifie moins de jours. Travail = travailleurs × jours = constante. 8 × 12 = 96 jours-travailleurs. Pour 6 travailleurs : 96 ÷ 6 = 16 jours.', 3, 5, 20);

-- =============================================
-- Verification Query
-- =============================================

-- Verify inserted questions
SELECT 
    q.id,
    kp.category_id,
    kc.category_name,
    q.question_type,
    q.difficulty_level,
    q.question_title,
    q.points
FROM question q
JOIN knowledge_point kp ON q.knowledge_point_id = kp.id
JOIN knowledge_category kc ON kp.category_id = kc.id
WHERE kp.grade_id = 8 
    AND kp.category_id IN (2, 4)
    AND q.delete_flag = 'N'
ORDER BY kp.category_id, q.sort_order;

-- Summary statistics by difficulty
SELECT 
    kp.category_id,
    kc.category_name,
    q.difficulty_level,
    CASE q.difficulty_level
        WHEN 1 THEN 'Easy'
        WHEN 2 THEN 'Medium'
        WHEN 3 THEN 'Hard'
    END AS difficulty_name,
    COUNT(*) as question_count,
    SUM(q.points) as total_points
FROM question q
JOIN knowledge_point kp ON q.knowledge_point_id = kp.id
JOIN knowledge_category kc ON kp.category_id = kc.id
WHERE kp.grade_id = 8 
    AND kp.category_id IN (2, 4)
    AND q.delete_flag = 'N'
GROUP BY kp.category_id, kc.category_name, q.difficulty_level
ORDER BY kp.category_id, q.difficulty_level;

-- Question type distribution
SELECT 
    q.question_type,
    CASE q.question_type
        WHEN 1 THEN 'Multiple Choice'
        WHEN 2 THEN 'Fill-in-blank'
        WHEN 3 THEN 'Calculation'
        WHEN 4 THEN 'Application'
    END AS question_type_name,
    COUNT(*) as question_count
FROM question q
JOIN knowledge_point kp ON q.knowledge_point_id = kp.id
WHERE kp.grade_id = 8 
    AND kp.category_id IN (2, 4)
    AND q.delete_flag = 'N'
GROUP BY q.question_type
ORDER BY q.question_type;




