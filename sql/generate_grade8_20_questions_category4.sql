-- Generate 20 Grade 8 Math Questions for Category 4 (Comprehensive Application)
-- English content with French translations
-- Difficulty distribution: 7 easy (1) + 7 medium (2) + 6 hard (3)
-- Question types: All Multiple choice (1)

USE tutor;

-- =============================================
-- Comprehensive Application Questions (Category 4) - 20 questions
-- =============================================

INSERT INTO `question` (`knowledge_point_id`, `question_type`, `question_title`, `question_title_fr`, `question_content`, `question_content_fr`, `options`, `options_fr`, `correct_answer`, `correct_answer_fr`, `answer_explanation`, `answer_explanation_fr`, `difficulty_level`, `points`, `sort_order`) VALUES

-- Easy Questions (Difficulty 1) - 7 questions
((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'FUNCTION' AND delete_flag = 'N' LIMIT 1), 1, 'Function Definition', 'Définition de fonction', 'Which of the following best describes a function?', 'Lequel des suivants décrit le mieux une fonction ?',
'["A. A relation where each input has multiple outputs", "B. A relation where each input has exactly one output", "C. A relation where inputs and outputs are equal", "D. Any mathematical relationship"]',
'["A. Une relation où chaque entrée a plusieurs sorties", "B. Une relation où chaque entrée a exactement une sortie", "C. Une relation où les entrées et sorties sont égales", "D. Toute relation mathématique"]',
'B', 'B', 'A function is a relation where each input (x-value) maps to exactly one output (y-value). This is the fundamental definition of a function.', 'Une fonction est une relation où chaque entrée (valeur x) correspond à exactement une sortie (valeur y). C\'est la définition fondamentale d\'une fonction.', 1, 2, 1),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'FUNCTION' AND delete_flag = 'N' LIMIT 1), 1, 'Domain of a Function', 'Domaine d\'une fonction', 'What is the term for the set of all possible input values of a function?', 'Quel est le terme pour l\'ensemble de toutes les valeurs d\'entrée possibles d\'une fonction ?',
'["A. Range", "B. Domain", "C. Codomain", "D. Output set"]',
'["A. Image", "B. Domaine", "C. Codomaine", "D. Ensemble de sortie"]',
'B', 'B', 'The domain is the set of all possible input values (x-values) for which the function is defined and produces a valid output.', 'Le domaine est l\'ensemble de toutes les valeurs d\'entrée possibles (valeurs x) pour lesquelles la fonction est définie et produit une sortie valide.', 1, 2, 2),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'FUNCTION' AND delete_flag = 'N' LIMIT 1), 1, 'Range of a Function', 'Image d\'une fonction', 'What is the term for the set of all possible output values of a function?', 'Quel est le terme pour l\'ensemble de toutes les valeurs de sortie possibles d\'une fonction ?',
'["A. Domain", "B. Range", "C. Codomain", "D. Input set"]',
'["A. Domaine", "B. Image", "C. Codomaine", "D. Ensemble d\'entrée"]',
'B', 'B', 'The range is the set of all possible output values (y-values) that result from the function when all domain values are considered.', 'L\'image est l\'ensemble de toutes les valeurs de sortie possibles (valeurs y) qui résultent de la fonction lorsque toutes les valeurs du domaine sont considérées.', 1, 2, 3),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'LINEAR_FUNCTION' AND delete_flag = 'N' LIMIT 1), 1, 'Linear Function Standard Form', 'Forme standard de fonction linéaire', 'What is the general form of a linear function?', 'Quelle est la forme générale d\'une fonction linéaire ?',
'["A. y = ax² + bx + c", "B. y = mx + b", "C. y = a/x", "D. y = √x"]',
'["A. y = ax² + bx + c", "B. y = mx + b", "C. y = a/x", "D. y = √x"]',
'B', 'B', 'The general form of a linear function is y = mx + b, where m represents the slope and b represents the y-intercept.', 'La forme générale d\'une fonction linéaire est y = mx + b, où m représente la pente et b représente l\'ordonnée à l\'origine.', 1, 2, 4),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'LINEAR_FUNCTION' AND delete_flag = 'N' LIMIT 1), 1, 'Slope in Linear Function', 'Pente dans une fonction linéaire', 'In the equation y = 5x + 3, what does the number 5 represent?', 'Dans l\'équation y = 5x + 3, que représente le nombre 5 ?',
'["A. The y-intercept", "B. The slope", "C. The x-intercept", "D. The domain"]',
'["A. L\'ordonnée à l\'origine", "B. La pente", "C. L\'abscisse à l\'origine", "D. Le domaine"]',
'B', 'B', 'In the form y = mx + b, the coefficient m is the slope. In this equation, 5 is the slope, which indicates how steep the line is.', 'Dans la forme y = mx + b, le coefficient m est la pente. Dans cette équation, 5 est la pente, qui indique à quel point la ligne est raide.', 1, 2, 5),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'LINEAR_FUNCTION' AND delete_flag = 'N' LIMIT 1), 1, 'Y-Intercept in Linear Function', 'Ordonnée à l\'origine dans une fonction linéaire', 'In the equation y = 2x - 4, what is the y-intercept?', 'Dans l\'équation y = 2x - 4, quelle est l\'ordonnée à l\'origine ?',
'["A. 2", "B. -2", "C. 4", "D. -4"]',
'["A. 2", "B. -2", "C. 4", "D. -4"]',
'D', 'D', 'In the form y = mx + b, the constant term b is the y-intercept. In this equation, -4 is the y-intercept, meaning the line crosses the y-axis at (0, -4).', 'Dans la forme y = mx + b, le terme constant b est l\'ordonnée à l\'origine. Dans cette équation, -4 est l\'ordonnée à l\'origine, ce qui signifie que la ligne croise l\'axe y à (0, -4).', 1, 2, 6),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'LINEAR_FUNCTION_GRAPH' AND delete_flag = 'N' LIMIT 1), 1, 'Linear Function Graph Type', 'Type de graphique de fonction linéaire', 'What type of graph does a linear function produce?', 'Quel type de graphique une fonction linéaire produit-elle ?',
'["A. A curved line", "B. A straight line", "C. A parabola", "D. A circle"]',
'["A. Une ligne courbe", "B. Une ligne droite", "C. Une parabole", "D. Un cercle"]',
'B', 'B', 'Linear functions produce straight line graphs because the rate of change (slope) is constant throughout the function.', 'Les fonctions linéaires produisent des graphiques en ligne droite car le taux de changement (pente) est constant tout au long de la fonction.', 1, 2, 7),

-- Medium Questions (Difficulty 2) - 7 questions
((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'LINEAR_FUNCTION' AND delete_flag = 'N' LIMIT 1), 1, 'Finding Slope from Equation', 'Trouver la pente à partir de l\'équation', 'What is the slope of the linear function y = -4x + 9?', 'Quelle est la pente de la fonction linéaire y = -4x + 9 ?',
'["A. -9", "B. -4", "C. 4", "D. 9"]',
'["A. -9", "B. -4", "C. 4", "D. 9"]',
'B', 'B', 'In the linear function y = mx + b, the coefficient of x is the slope. Here, the slope m = -4.', 'Dans la fonction linéaire y = mx + b, le coefficient de x est la pente. Ici, la pente m = -4.', 2, 3, 8),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'LINEAR_FUNCTION' AND delete_flag = 'N' LIMIT 1), 1, 'Finding Y-Intercept from Equation', 'Trouver l\'ordonnée à l\'origine à partir de l\'équation', 'What is the y-intercept of the linear function y = 3x - 7?', 'Quelle est l\'ordonnée à l\'origine de la fonction linéaire y = 3x - 7 ?',
'["A. -7", "B. -3", "C. 3", "D. 7"]',
'["A. -7", "B. -3", "C. 3", "D. 7"]',
'A', 'A', 'In the form y = mx + b, the constant term b is the y-intercept. In this equation, b = -7, so the y-intercept is -7.', 'Dans la forme y = mx + b, le terme constant b est l\'ordonnée à l\'origine. Dans cette équation, b = -7, donc l\'ordonnée à l\'origine est -7.', 2, 3, 9),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'LINEAR_FUNCTION_GRAPH' AND delete_flag = 'N' LIMIT 1), 1, 'Positive Slope Graph', 'Graphique de pente positive', 'If a line on a graph goes upward from left to right, what can be said about its slope?', 'Si une ligne sur un graphique monte de gauche à droite, que peut-on dire de sa pente ?',
'["A. The slope is negative", "B. The slope is positive", "C. The slope is zero", "D. The slope is undefined"]',
'["A. La pente est négative", "B. La pente est positive", "C. La pente est zéro", "D. La pente est indéfinie"]',
'B', 'B', 'When a line goes upward from left to right, it has a positive slope. This means as x increases, y also increases.', 'Quand une ligne monte de gauche à droite, elle a une pente positive. Cela signifie que lorsque x augmente, y augmente également.', 2, 2, 10),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'LINEAR_FUNCTION_GRAPH' AND delete_flag = 'N' LIMIT 1), 1, 'Negative Slope Graph', 'Graphique de pente négative', 'If a line on a graph goes downward from left to right, what can be said about its slope?', 'Si une ligne sur un graphique descend de gauche à droite, que peut-on dire de sa pente ?',
'["A. The slope is negative", "B. The slope is positive", "C. The slope is zero", "D. The slope is undefined"]',
'["A. La pente est négative", "B. La pente est positive", "C. La pente est zéro", "D. La pente est indéfinie"]',
'A', 'A', 'When a line goes downward from left to right, it has a negative slope. This means as x increases, y decreases.', 'Quand une ligne descend de gauche à droite, elle a une pente négative. Cela signifie que lorsque x augmente, y diminue.', 2, 2, 11),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'LINEAR_FUNCTION_APPLICATION' AND delete_flag = 'N' LIMIT 1), 1, 'Distance-Time Function', 'Fonction distance-temps', 'A bicycle travels at a constant speed of 15 km/h. If d(t) represents the distance traveled after t hours, what is the distance after 4 hours?', 'Un vélo se déplace à une vitesse constante de 15 km/h. Si d(t) représente la distance parcourue après t heures, quelle est la distance après 4 heures ?',
'["A. 45 km", "B. 50 km", "C. 55 km", "D. 60 km"]',
'["A. 45 km", "B. 50 km", "C. 55 km", "D. 60 km"]',
'D', 'D', 'Distance equals speed times time, so d(t) = 15t. After 4 hours: d(4) = 15 × 4 = 60 km.', 'La distance est égale à la vitesse multipliée par le temps, donc d(t) = 15t. Après 4 heures : d(4) = 15 × 4 = 60 km.', 2, 4, 12),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'LINEAR_FUNCTION_APPLICATION' AND delete_flag = 'N' LIMIT 1), 1, 'Cost Function Calculation', 'Calcul de fonction de coût', 'A gym charges a membership fee of $50 per month plus $5 per class attended. If C(x) represents the total monthly cost when attending x classes, what is the cost for attending 8 classes?', 'Une salle de sport facture un abonnement de 50$ par mois plus 5$ par cours suivi. Si C(x) représente le coût mensuel total lorsqu\'on suit x cours, quel est le coût pour suivre 8 cours ?',
'["A. $80", "B. $85", "C. $90", "D. $95"]',
'["A. 80$", "B. 85$", "C. 90$", "D. 95$"]',
'C', 'C', 'The function is C(x) = 50 + 5x, where 50 is the fixed monthly fee and 5x is the cost for x classes. For 8 classes: C(8) = 50 + 5(8) = 50 + 40 = $90.', 'La fonction est C(x) = 50 + 5x, où 50 est l\'abonnement mensuel fixe et 5x est le coût pour x cours. Pour 8 cours : C(8) = 50 + 5(8) = 50 + 40 = 90$.', 2, 4, 13),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'INVERSE_PROPORTION_FUNCTION' AND delete_flag = 'N' LIMIT 1), 1, 'Inverse Proportion Function Form', 'Forme de fonction de proportion inverse', 'What is the general form of an inverse proportion function?', 'Quelle est la forme générale d\'une fonction de proportion inverse ?',
'["A. y = kx", "B. y = k/x", "C. y = kx²", "D. y = k/x²"]',
'["A. y = kx", "B. y = k/x", "C. y = kx²", "D. y = k/x²"]',
'B', 'B', 'An inverse proportion function has the form y = k/x, where k is a constant. As x increases, y decreases proportionally, and vice versa.', 'Une fonction de proportion inverse a la forme y = k/x, où k est une constante. Lorsque x augmente, y diminue proportionnellement, et vice versa.', 2, 2, 14),

-- Hard Questions (Difficulty 3) - 6 questions
((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'LINEAR_FUNCTION_APPLICATION' AND delete_flag = 'N' LIMIT 1), 1, 'Linear Function Real-World Problem', 'Problème réel avec fonction linéaire', 'A taxi company charges a base fee of $3.50 plus $2.25 per mile traveled. If C(m) represents the total cost for traveling m miles, how much would a 12-mile trip cost?', 'Une compagnie de taxi facture un tarif de base de 3,50$ plus 2,25$ par mile parcouru. Si C(m) représente le coût total pour parcourir m miles, combien coûterait un trajet de 12 miles ?',
'["A. $27.50", "B. $30.00", "C. $30.50", "D. $33.00"]',
'["A. 27,50$", "B. 30,00$", "C. 30,50$", "D. 33,00$"]',
'C', 'C', 'The function is C(m) = 3.50 + 2.25m, where 3.50 is the base fee and 2.25m is the variable cost per mile. For 12 miles: C(12) = 3.50 + 2.25(12) = 3.50 + 27 = $30.50.', 'La fonction est C(m) = 3,50 + 2,25m, où 3,50 est le tarif de base et 2,25m est le coût variable par mile. Pour 12 miles : C(12) = 3,50 + 2,25(12) = 3,50 + 27 = 30,50$.', 3, 5, 15),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'LINEAR_FUNCTION_APPLICATION' AND delete_flag = 'N' LIMIT 1), 1, 'Comparing Linear Functions', 'Comparaison de fonctions linéaires', 'Store A sells apples for $2.50 per pound with no additional fee. Store B sells apples for $2.00 per pound but charges a $3.00 service fee. For how many pounds of apples is Store A cheaper than Store B?', 'Le magasin A vend des pommes à 2,50$ la livre sans frais supplémentaires. Le magasin B vend des pommes à 2,00$ la livre mais facture 3,00$ de frais de service. Pour combien de livres de pommes le magasin A est-il moins cher que le magasin B ?',
'["A. Less than 5 pounds", "B. Less than 6 pounds", "C. Less than 7 pounds", "D. Less than 8 pounds"]',
'["A. Moins de 5 livres", "B. Moins de 6 livres", "C. Moins de 7 livres", "D. Moins de 8 livres"]',
'B', 'B', 'Store A: A(x) = 2.50x. Store B: B(x) = 3.00 + 2.00x. Set A(x) < B(x): 2.50x < 3.00 + 2.00x, which simplifies to 0.50x < 3.00, so x < 6. Store A is cheaper for less than 6 pounds.', 'Magasin A : A(x) = 2,50x. Magasin B : B(x) = 3,00 + 2,00x. Posons A(x) < B(x) : 2,50x < 3,00 + 2,00x, ce qui se simplifie à 0,50x < 3,00, donc x < 6. Le magasin A est moins cher pour moins de 6 livres.', 3, 5, 16),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'LINEAR_FUNCTION_GRAPH' AND delete_flag = 'N' LIMIT 1), 1, 'Graph Analysis', 'Analyse de graphique', 'A linear function passes through points (2, 5) and (6, 13). What is the equation of the function in the form y = mx + b?', 'Une fonction linéaire passe par les points (2, 5) et (6, 13). Quelle est l\'équation de la fonction sous la forme y = mx + b ?',
'["A. y = 2x + 1", "B. y = 2x + 3", "C. y = 3x - 1", "D. y = 4x - 3"]',
'["A. y = 2x + 1", "B. y = 2x + 3", "C. y = 3x - 1", "D. y = 4x - 3"]',
'A', 'A', 'Slope m = (13 - 5)/(6 - 2) = 8/4 = 2. Using point (2, 5): 5 = 2(2) + b, so 5 = 4 + b, thus b = 1. The equation is y = 2x + 1.', 'Pente m = (13 - 5)/(6 - 2) = 8/4 = 2. En utilisant le point (2, 5) : 5 = 2(2) + b, donc 5 = 4 + b, ainsi b = 1. L\'équation est y = 2x + 1.', 3, 5, 17),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'INVERSE_PROPORTION_FUNCTION' AND delete_flag = 'N' LIMIT 1), 1, 'Inverse Proportion Calculation', 'Calcul de proportion inverse', 'If y varies inversely with x, and y = 12 when x = 3, what is the value of y when x = 9?', 'Si y varie inversement avec x, et y = 12 quand x = 3, quelle est la valeur de y quand x = 9 ?',
'["A. 3", "B. 4", "C. 6", "D. 9"]',
'["A. 3", "B. 4", "C. 6", "D. 9"]',
'B', 'B', 'For inverse proportion, y = k/x. When y = 12 and x = 3: 12 = k/3, so k = 36. When x = 9: y = 36/9 = 4.', 'Pour une proportion inverse, y = k/x. Quand y = 12 et x = 3 : 12 = k/3, donc k = 36. Quand x = 9 : y = 36/9 = 4.', 3, 4, 18),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'INVERSE_PROPORTION_FUNCTION_APPLICATION' AND delete_flag = 'N' LIMIT 1), 1, 'Inverse Proportion Word Problem', 'Problème de mot avec proportion inverse', 'The time it takes to paint a room is inversely proportional to the number of painters. If 3 painters can complete the job in 8 hours, how long would it take 6 painters to complete the same job?', 'Le temps qu\'il faut pour peindre une pièce est inversement proportionnel au nombre de peintres. Si 3 peintres peuvent terminer le travail en 8 heures, combien de temps faudrait-il à 6 peintres pour terminer le même travail ?',
'["A. 2 hours", "B. 3 hours", "C. 4 hours", "D. 5 hours"]',
'["A. 2 heures", "B. 3 heures", "C. 4 heures", "D. 5 heures"]',
'C', 'C', 'This is an inverse proportion problem. Work = painters × time = constant. 3 × 8 = 24 painter-hours. For 6 painters: 24 ÷ 6 = 4 hours.', 'C\'est un problème de proportion inverse. Travail = peintres × temps = constante. 3 × 8 = 24 heures-peintres. Pour 6 peintres : 24 ÷ 6 = 4 heures.', 3, 5, 19),

((SELECT id FROM knowledge_point WHERE grade_id = 8 AND category_id = 4 AND point_code = 'INVERSE_PROPORTION_FUNCTION_APPLICATION' AND delete_flag = 'N' LIMIT 1), 1, 'Complex Inverse Proportion', 'Proportion inverse complexe', 'The speed of a vehicle is inversely proportional to the time taken for a fixed distance. If a car travels 300 km at 60 km/h, how fast must it travel to cover the same distance in 4 hours instead of 5 hours?', 'La vitesse d\'un véhicule est inversement proportionnelle au temps pris pour une distance fixe. Si une voiture parcourt 300 km à 60 km/h, à quelle vitesse doit-elle rouler pour parcourir la même distance en 4 heures au lieu de 5 heures ?',
'["A. 70 km/h", "B. 72 km/h", "C. 75 km/h", "D. 80 km/h"]',
'["A. 70 km/h", "B. 72 km/h", "C. 75 km/h", "D. 80 km/h"]',
'C', 'C', 'For a fixed distance, speed × time = constant. At 60 km/h for 5 hours, the distance is 300 km. For the same distance in 4 hours: speed = 300/4 = 75 km/h. Alternatively, since speed is inversely proportional to time, and time ratio is 4/5, speed ratio is 5/4, so speed = 60 × (5/4) = 75 km/h.', 'Pour une distance fixe, vitesse × temps = constante. À 60 km/h pendant 5 heures, la distance est de 300 km. Pour la même distance en 4 heures : vitesse = 300/4 = 75 km/h. Alternativement, puisque la vitesse est inversement proportionnelle au temps, et le rapport de temps est 4/5, le rapport de vitesse est 5/4, donc vitesse = 60 × (5/4) = 75 km/h.', 3, 5, 20);

-- =============================================
-- Verification Query
-- =============================================

-- Verify inserted questions
SELECT 
    q.id,
    kp.category_id,
    kc.category_name,
    kp.point_name,
    q.question_type,
    q.difficulty_level,
    q.question_title,
    q.points,
    q.sort_order
FROM question q
JOIN knowledge_point kp ON q.knowledge_point_id = kp.id
JOIN knowledge_category kc ON kp.category_id = kc.id
WHERE kp.grade_id = 8 
    AND kp.category_id = 4
    AND q.delete_flag = 'N'
ORDER BY q.sort_order;

-- Summary statistics by difficulty
SELECT 
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
WHERE kp.grade_id = 8 
    AND kp.category_id = 4
    AND q.delete_flag = 'N'
GROUP BY q.difficulty_level
ORDER BY q.difficulty_level;

-- Question type distribution (all should be type 1 - Multiple Choice)
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
    AND kp.category_id = 4
    AND q.delete_flag = 'N'
GROUP BY q.question_type
ORDER BY q.question_type;

-- Knowledge point coverage
SELECT 
    kp.point_name,
    kp.point_code,
    COUNT(q.id) as question_count
FROM knowledge_point kp
LEFT JOIN question q ON kp.id = q.knowledge_point_id AND q.delete_flag = 'N'
WHERE kp.grade_id = 8 
    AND kp.category_id = 4
    AND kp.delete_flag = 'N'
GROUP BY kp.id, kp.point_name, kp.point_code
ORDER BY kp.sort_order;

