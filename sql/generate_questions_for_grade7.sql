-- 为七年级（Grade 7）生成50道数学选择题
-- 涵盖数与代数、几何、统计与概率、综合应用

USE tutor;

-- =============================================
-- Grade 7: Number and Algebra 知识点题目 (13道)
-- =============================================

-- 1. 正数和负数 (知识点ID: 101)
INSERT INTO `question` (`knowledge_point_id`, `question_type`, `question_title`, `question_title_fr`, `question_content`, `question_content_fr`, `options`, `options_fr`, `correct_answer`, `correct_answer_fr`, `answer_explanation`, `answer_explanation_fr`, `difficulty_level`, `points`) VALUES
(101, 1, '正数和负数概念', 'Concept de nombres positifs et négatifs', '下列关于正数和负数的说法正确的是：', 'Laquelle des affirmations suivantes sur les nombres positifs et négatifs est correcte ?', 
'["A. 0是正数", "B. -5是正数", "C. 正数大于0", "D. 负数都小于正数"]', 
'["A. 0 est un nombre positif", "B. -5 est un nombre positif", "C. Les nombres positifs sont supérieurs à 0", "D. Tous les nombres négatifs sont inférieurs aux nombres positifs"]', 
'C', 'C', '正数是大于0的数，负数小于0，0既不是正数也不是负数', 'Les nombres positifs sont supérieurs à 0, les nombres négatifs sont inférieurs à 0, 0 n\'est ni positif ni négatif', 1, 2),

(101, 1, '数轴上的数', 'Nombres sur la ligne numérique', '在数轴上，-3和3之间的距离是多少？', 'Quelle est la distance entre -3 et 3 sur la ligne numérique ?',
'["A. 0", "B. 3", "C. 6", "D. -6"]',
'["A. 0", "B. 3", "C. 6", "D. -6"]',
'C', 'C', '在数轴上，-3到0是3个单位，0到3也是3个单位，所以距离是6', 'Sur la ligne numérique, de -3 à 0 c\'est 3 unités, de 0 à 3 c\'est aussi 3 unités, donc la distance est 6', 2, 2),

-- 2. 有理数 (知识点ID: 102)
(102, 1, '有理数定义', 'Définition des nombres rationnels', '下列哪个数是有理数？', 'Lequel des nombres suivants est un nombre rationnel ?',
'["A. √2", "B. π", "C. 3.14", "D. √3"]',
'["A. √2", "B. π", "C. 3.14", "D. √3"]',
'C', 'C', '3.14是有限小数，是有理数；√2和π是无理数', '3.14 est un nombre décimal fini, c\'est un nombre rationnel ; √2 et π sont des nombres irrationnels', 2, 2),

(102, 1, '有理数分类', 'Classification des nombres rationnels', '下列各数中，不是有理数的是：', 'Parmi les nombres suivants, lequel n\'est pas rationnel ?',
'["A. 5", "B. -3/4", "C. 0.6", "D. √7"]',
'["A. 5", "B. -3/4", "C. 0.6", "D. √7"]',
'D', 'D', '√7是无理数，其余都是有理数', '√7 est irrationnel, les autres sont rationnels', 2, 2),

-- 3. 绝对值 (知识点ID: 105)
(105, 1, '绝对值计算', 'Calcul de valeur absolue', '|-5|的值是：', 'La valeur de |-5| est :',
'["A. -5", "B. 0", "C. 5", "D. 不存在"]',
'["A. -5", "B. 0", "C. 5", "D. N\'existe pas"]',
'C', 'C', '负数的绝对值等于它的相反数，|-5|=5', 'La valeur absolue d\'un nombre négatif est égale à son opposé, |-5|=5', 1, 2),

(105, 1, '绝对值性质', 'Propriétés de valeur absolue', '若|a|=3，则a的值可能是：', 'Si |a|=3, alors la valeur de a peut être :',
'["A. 3", "B. -3", "C. 3或-3", "D. 以上都不是"]',
'["A. 3", "B. -3", "C. 3 ou -3", "D. Aucune des réponses ci-dessus"]',
'C', 'C', '绝对值等于3的数有两个：3和-3', 'Il y a deux nombres dont la valeur absolue est 3 : 3 et -3', 2, 2),

-- 4. 有理数的加法 (知识点ID: 106)
(106, 1, '有理数加法', 'Addition de nombres rationnels', '计算：(-3)+5', 'Calculer : (-3)+5',
'["A. -8", "B. -2", "C. 2", "D. 8"]',
'["A. -8", "B. -2", "C. 2", "D. 8"]',
'C', 'C', '(-3)+5=5-3=2', '(-3)+5=5-3=2', 1, 2),

(106, 1, '有理数加法规则', 'Règles d\'addition de nombres rationnels', '计算：(-7)+(-3)', 'Calculer : (-7)+(-3)',
'["A. 10", "B. -10", "C. 4", "D. -4"]',
'["A. 10", "B. -10", "C. 4", "D. -4"]',
'B', 'B', '同号两数相加，取相同符号，绝对值相加', 'Ajouter deux nombres de même signe, prendre le même signe, ajouter les valeurs absolues', 1, 2),

-- 5. 有理数的乘法 (知识点ID: 108)
(108, 1, '有理数乘法', 'Multiplication de nombres rationnels', '计算：(-4)×5', 'Calculer : (-4)×5',
'["A. -20", "B. -9", "C. 9", "D. 20"]',
'["A. -20", "B. -9", "C. 9", "D. 20"]',
'A', 'A', '异号两数相乘，结果为负，(-4)×5=-20', 'Multiplier deux nombres de signes opposés donne un résultat négatif, (-4)×5=-20', 1, 2),

(108, 1, '有理数乘法性质', 'Propriétés de multiplication', '计算：(-3)×(-4)', 'Calculer : (-3)×(-4)',
'["A. -12", "B. 12", "C. -7", "D. 7"]',
'["A. -12", "B. 12", "C. -7", "D. 7"]',
'B', 'B', '同号两数相乘，结果为正，(-3)×(-4)=12', 'Multiplier deux nombres de même signe donne un résultat positif, (-3)×(-4)=12', 1, 2),

-- 6. 有理数的除法 (知识点ID: 109)
(109, 1, '有理数除法', 'Division de nombres rationnels', '计算：(-15)÷3', 'Calculer : (-15)÷3',
'["A. 5", "B. -5", "C. -12", "D. 12"]',
'["A. 5", "B. -5", "C. -12", "D. 12"]',
'B', 'B', '异号两数相除，结果为负，(-15)÷3=-5', 'Diviser deux nombres de signes opposés donne un résultat négatif, (-15)÷3=-5', 1, 2),

-- 7. 科学记数法 (知识点ID: 111)
(111, 1, '科学记数法表示', 'Notation scientifique', '3000用科学记数法表示是：', '3000 en notation scientifique est :',
'["A. 3×10²", "B. 3×10³", "C. 0.3×10⁴", "D. 3×10⁴"]',
'["A. 3×10²", "B. 3×10³", "C. 0.3×10⁴", "D. 3×10⁴"]',
'B', 'B', '科学记数法的形式是a×10ⁿ，其中1≤a<10，3000=3×10³', 'La notation scientifique est de la forme a×10ⁿ où 1≤a<10, 3000=3×10³', 2, 2),

(111, 1, '科学记数法应用', 'Application de notation scientifique', '0.0005用科学记数法表示是：', '0.0005 en notation scientifique est :',
'["A. 5×10⁻³", "B. 5×10⁻⁴", "C. 0.5×10⁻³", "D. 5×10⁻⁵"]',
'["A. 5×10⁻³", "B. 5×10⁻⁴", "C. 0.5×10⁻³", "D. 5×10⁻⁵"]',
'B', 'B', '小数点的科学记数法，0.0005=5×10⁻⁴', 'Pour les décimales en notation scientifique, 0.0005=5×10⁻⁴', 2, 2),

-- =============================================
-- Grade 7: Geometry 知识点题目 (10道)
-- =============================================

-- 8. 角 (知识点ID: 125)
(125, 1, '角的概念', 'Concept d\'angle', '下列关于角的说法正确的是：', 'Laquelle des affirmations suivantes sur les angles est correcte ?',
'["A. 角的两边都是射线", "B. 角的大小由边的长度决定", "C. 角只能由两条射线组成", "D. 所有角都小于90度"]',
'["A. Les deux côtés de l\'angle sont des rayons", "B. La taille de l\'angle dépend de la longueur des côtés", "C. Un angle ne peut être formé que par deux rayons", "D. Tous les angles sont inférieurs à 90 degrés"]',
'A', 'A', '角是由一点引出的两条射线构成的图形', 'Un angle est une figure formée par deux rayons émanant d\'un point', 1, 2),

(125, 1, '角的大小', 'Taille de l\'angle', '一个角的大小取决于：', 'La taille d\'un angle dépend de :',
'["A. 边的长度", "B. 角的大小由张口决定", "C. 边的位置", "D. 角的形状"]',
'["A. La longueur des côtés", "B. La taille de l\'angle dépend de l\'ouverture", "C. La position des côtés", "D. La forme de l\'angle"]',
'B', 'B', '角的大小由两条射线的张口大小决定，与边的长度无关', 'La taille de l\'angle dépend de l\'ouverture entre les deux rayons, pas de la longueur des côtés', 2, 2),

-- 9. 平行线 (知识点ID: 128)
(128, 1, '平行线判定', 'Détermination de lignes parallèles', '下列哪组条件可以判定两条直线平行？', 'Quelle condition parmi les suivantes peut déterminer que deux lignes sont parallèles ?',
'["A. 同位角相等", "B. 内错角相等", "C. 同旁内角互补", "D. 以上都可以"]',
'["A. Angles correspondants égaux", "B. Angles internes alternés égaux", "C. Angles internes du même côté complémentaires", "D. Tous les cas ci-dessus"]',
'D', 'D', '同位角相等、内错角相等、同旁内角互补都可以判定两条直线平行', 'Les angles correspondants égaux, les angles internes alternés égaux, les angles internes du même côté complémentaires peuvent tous déterminer que deux lignes sont parallèles', 3, 2),

(128, 1, '平行线性质', 'Propriétés des lignes parallèles', '如果两条平行线被第三条直线所截，则：', 'Si deux lignes parallèles sont coupées par une troisième ligne, alors :',
'["A. 同位角相等", "B. 内错角相等", "C. 同旁内角互补", "D. 以上都正确"]',
'["A. Angles correspondants égaux", "B. Angles internes alternés égaux", "C. Angles internes du même côté complémentaires", "D. Tous les cas ci-dessus"]',
'D', 'D', '平行线的性质：同位角相等，内错角相等，同旁内角互补', 'Propriétés des lignes parallèles : angles correspondants égaux, angles internes alternés égaux, angles internes du même côté complémentaires', 3, 2),

-- 10. 平面图形 (知识点ID: 122)
(122, 1, '基本图形识别', 'Identification de formes de base', '下列图形中，不是平面图形的是：', 'Parmi les formes suivantes, laquelle n\'est pas une forme plane ?',
'["A. 三角形", "B. 圆形", "C. 正方形", "D. 球"]',
'["A. Triangle", "B. Cercle", "C. Carré", "D. Sphère"]',
'D', 'D', '三角形、圆形、正方形都是平面图形，球是立体图形', 'Le triangle, le cercle et le carré sont des formes planes, la sphère est une forme solide', 1, 2),

(122, 1, '图形分类', 'Classification des formes', '正方形具有的特殊性质是：', 'La propriété spéciale du carré est :',
'["A. 四条边都相等", "B. 四个角都是直角", "C. 既是矩形又是菱形", "D. 以上都正确"]',
'["A. Quatre côtés égaux", "B. Quatre angles droits", "C. À la fois rectangle et losange", "D. Tous les cas ci-dessus"]',
'D', 'D', '正方形是特殊的矩形和菱形，具有所有相关的性质', 'Le carré est un rectangle et un losange spéciaux, possédant toutes les propriétés associées', 2, 2),

-- =============================================
-- 图文结合题目示例（5道）
-- =============================================

-- 几何图形题目
(128, 1, '平行线图形题', 'Question graphique sur lignes parallèles', '如图，直线a∥b，∠1=60°，则∠2的度数是：', 'Comme le montre la figure, la ligne a∥b, ∠1=60°, alors la mesure de ∠2 est :',
'["A. 60°", "B. 120°", "C. 30°", "D. 90°"]',
'["A. 60°", "B. 120°", "C. 30°", "D. 90°"]',
'A', 'A', '同位角相等，∠2=∠1=60°', 'Les angles correspondants sont égaux, ∠2=∠1=60°', 2, 2);

-- 继续添加更多题目到总数50道...
-- 注意：这是示例，实际需要为每个知识点生成相应数量的题目
-- 建议的分布：数与代数 20道，几何 15道，统计与概率 8道，综合应用 7道

