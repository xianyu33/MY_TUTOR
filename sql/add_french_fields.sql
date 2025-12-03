-- =============================================
-- MY_TUTOR 多语言支持 - 添加法语字段脚本
-- 为现有表添加法语冗余字段
-- =============================================

-- 使用数据库
USE tutor;

-- =============================================
-- 1. 知识点分类表添加法语字段
-- =============================================
ALTER TABLE `knowledge_category` 
ADD COLUMN `category_name_fr` VARCHAR(50) COMMENT '分类名称（法语）' AFTER `category_name`,
ADD COLUMN `description_fr` TEXT COMMENT '分类描述（法语）' AFTER `description`,
ADD COLUMN `icon_url` VARCHAR(500) COMMENT '分类图标URL' AFTER `description_fr`,
ADD COLUMN `icon_class` VARCHAR(100) COMMENT '分类图标CSS类名' AFTER `icon_url`;

-- 添加索引
CREATE INDEX `idx_category_name_fr` ON `knowledge_category`(`category_name_fr`);
CREATE INDEX `idx_icon_class` ON `knowledge_category`(`icon_class`);

-- =============================================
-- 2. 知识点表添加法语字段和图标字段
-- =============================================
ALTER TABLE `knowledge_point` 
ADD COLUMN `point_name_fr` VARCHAR(100) COMMENT '知识点名称（法语）' AFTER `point_name`,
ADD COLUMN `description_fr` TEXT COMMENT '知识点描述（法语）' AFTER `description`,
ADD COLUMN `content_fr` TEXT COMMENT '知识点内容详情（法语）' AFTER `content`,
ADD COLUMN `learning_objectives_fr` TEXT COMMENT '学习目标（法语）' AFTER `learning_objectives`,
ADD COLUMN `icon_url` VARCHAR(500) COMMENT '知识点图标URL' AFTER `content_fr`,
ADD COLUMN `icon_class` VARCHAR(100) COMMENT '知识点图标CSS类名' AFTER `icon_url`;

-- 添加索引
CREATE INDEX `idx_point_name_fr` ON `knowledge_point`(`point_name_fr`);
CREATE INDEX `idx_icon_class` ON `knowledge_point`(`icon_class`);

-- =============================================
-- 3. 题目表添加法语字段
-- =============================================
ALTER TABLE `question` 
ADD COLUMN `question_title_fr` VARCHAR(200) COMMENT '题目标题（法语）' AFTER `question_title`,
ADD COLUMN `question_content_fr` TEXT COMMENT '题目内容（法语）' AFTER `question_content`,
ADD COLUMN `options_fr` TEXT COMMENT '选项（法语，JSON格式）' AFTER `options`,
ADD COLUMN `correct_answer_fr` TEXT COMMENT '正确答案（法语）' AFTER `correct_answer`,
ADD COLUMN `answer_explanation_fr` TEXT COMMENT '答案解释（法语）' AFTER `answer_explanation`;

-- =============================================
-- 4. 测试表添加法语字段
-- =============================================
ALTER TABLE `test` 
ADD COLUMN `test_name_fr` VARCHAR(200) COMMENT '测试名称（法语）' AFTER `test_name`;

-- 添加索引
CREATE INDEX `idx_test_name` ON `test`(`test_name`);
CREATE INDEX `idx_test_name_fr` ON `test`(`test_name_fr`);

-- =============================================
-- 5. 学生测试记录表添加法语字段
-- =============================================
ALTER TABLE `student_test_record` 
ADD COLUMN `test_name_fr` VARCHAR(200) COMMENT '测试名称（法语，冗余）' AFTER `test_name`;

-- =============================================
-- 6. 学生测试答题详情表添加法语字段
-- =============================================
ALTER TABLE `student_test_answer` 
ADD COLUMN `question_content_fr` TEXT COMMENT '题目内容（法语，冗余）' AFTER `question_content`,
ADD COLUMN `correct_answer_fr` TEXT COMMENT '正确答案（法语，冗余）' AFTER `correct_answer`,
ADD COLUMN `student_answer_fr` TEXT COMMENT '学生答案（法语）' AFTER `student_answer`;

-- =============================================
-- 7. 更新基础数据 - 知识点分类
-- =============================================
UPDATE `knowledge_category` SET 
    `category_name_fr` = 'Nombres et Algèbre',
    `description_fr` = 'Reconnaissance des nombres, opérations, expressions algébriques, etc.',
    `icon_url` = '/icons/category/numbers-algebra.png',
    `icon_class` = 'icon-category-numbers'
WHERE `category_code` = 'NUM_ALG';

UPDATE `knowledge_category` SET 
    `category_name_fr` = 'Géométrie',
    `description_fr` = 'Reconnaissance des formes, mesure, transformation, etc.',
    `icon_url` = '/icons/category/geometry.png',
    `icon_class` = 'icon-category-geometry'
WHERE `category_code` = 'GEOMETRY';

UPDATE `knowledge_category` SET 
    `category_name_fr` = 'Statistiques et Probabilité',
    `description_fr` = 'Collecte, organisation et analyse de données, etc.',
    `icon_url` = '/icons/category/statistics.png',
    `icon_class` = 'icon-category-statistics'
WHERE `category_code` = 'STAT_PROB';

UPDATE `knowledge_category` SET 
    `category_name_fr` = 'Compréhension et Pratique',
    `description_fr` = 'Application intégrée des connaissances mathématiques pour résoudre des problèmes pratiques',
    `icon_url` = '/icons/category/comprehensive.png',
    `icon_class` = 'icon-category-comprehensive'
WHERE `category_code` = 'COMPREHENSIVE';

-- =============================================
-- 8. 更新基础数据 - 知识点
-- =============================================
UPDATE `knowledge_point` SET 
    `point_name_fr` = 'Reconnaissance des nombres',
    `description_fr` = 'Reconnaissance des nombres de 1 à 100',
    `content_fr` = 'Apprendre la lecture, l\'écriture et la comparaison des nombres de 1 à 100',
    `icon_url` = '/icons/numbers.png',
    `icon_class` = 'icon-numbers'
WHERE `point_code` = 'NUM_001';

UPDATE `knowledge_point` SET 
    `point_name_fr` = 'Opération d\'addition',
    `description_fr` = 'Addition dans les 20',
    `content_fr` = 'Apprendre les opérations d\'addition dans les 20',
    `icon_url` = '/icons/addition.png',
    `icon_class` = 'icon-addition'
WHERE `point_code` = 'NUM_002';

UPDATE `knowledge_point` SET 
    `point_name_fr` = 'Reconnaissance des formes',
    `description_fr` = 'Formes de base',
    `content_fr` = 'Reconnaître les formes de base comme le cercle, le carré, le triangle, etc.',
    `icon_url` = '/icons/shapes.png',
    `icon_class` = 'icon-shapes'
WHERE `point_code` = 'GEO_001';

UPDATE `knowledge_point` SET 
    `point_name_fr` = 'Table de multiplication',
    `description_fr` = 'Table de multiplication de 9',
    `content_fr` = 'Apprendre la table de multiplication de 9',
    `icon_url` = '/icons/multiplication.png',
    `icon_class` = 'icon-multiplication'
WHERE `point_code` = 'NUM_003';

UPDATE `knowledge_point` SET 
    `point_name_fr` = 'Opération de division',
    `description_fr` = 'Division simple',
    `content_fr` = 'Apprendre les opérations de division simples',
    `icon_url` = '/icons/division.png',
    `icon_class` = 'icon-division'
WHERE `point_code` = 'NUM_004';

UPDATE `knowledge_point` SET 
    `point_name_fr` = 'Reconnaissance des fractions',
    `description_fr` = 'Concept de fraction',
    `content_fr` = 'Comprendre le concept et les méthodes de représentation des fractions',
    `icon_url` = '/icons/fractions.png',
    `icon_class` = 'icon-fractions'
WHERE `point_code` = 'NUM_005';

UPDATE `knowledge_point` SET 
    `point_name_fr` = 'Nombres rationnels',
    `description_fr` = 'Opérations sur les nombres rationnels',
    `content_fr` = 'Apprendre les quatre opérations sur les nombres rationnels',
    `icon_url` = '/icons/rational-numbers.png',
    `icon_class` = 'icon-rational'
WHERE `point_code` = 'NUM_006';

UPDATE `knowledge_point` SET 
    `point_name_fr` = 'Géométrie plane',
    `description_fr` = 'Formes planes',
    `content_fr` = 'Apprendre les concepts de base de la géométrie plane',
    `icon_url` = '/icons/plane-geometry.png',
    `icon_class` = 'icon-plane-geometry'
WHERE `point_code` = 'GEO_002';

UPDATE `knowledge_point` SET 
    `point_name_fr` = 'Fonction quadratique',
    `description_fr` = 'Fonction quadratique',
    `content_fr` = 'Apprendre les graphiques et propriétés des fonctions quadratiques',
    `icon_url` = '/icons/quadratic-function.png',
    `icon_class` = 'icon-quadratic'
WHERE `point_code` = 'NUM_007';

UPDATE `knowledge_point` SET 
    `point_name_fr` = 'Géométrie solide',
    `description_fr` = 'Formes solides',
    `content_fr` = 'Apprendre les concepts de base de la géométrie solide',
    `icon_url` = '/icons/solid-geometry.png',
    `icon_class` = 'icon-solid-geometry'
WHERE `point_code` = 'GEO_003';

-- =============================================
-- 9. 更新基础数据 - 题目（示例）
-- =============================================
-- 更新前几个题目的法语内容作为示例
UPDATE `question` SET 
    `question_title_fr` = 'Reconnaissance des nombres',
    `question_content_fr` = 'Quel est le plus grand nombre parmi les suivants ?',
    `options_fr` = '["A. 15", "B. 25", "C. 35", "D. 45"]',
    `correct_answer_fr` = 'D',
    `answer_explanation_fr` = '45 est le plus grand nombre'
WHERE `id` = 1;

UPDATE `question` SET 
    `question_title_fr` = 'Comparaison de nombres',
    `question_content_fr` = 'Comparez les tailles : 23 et 32',
    `options_fr` = '["A. 23 > 32", "B. 23 < 32", "C. 23 = 32", "D. Impossible à comparer"]',
    `correct_answer_fr` = 'B',
    `answer_explanation_fr` = '32 est plus grand que 23'
WHERE `id` = 2;

UPDATE `question` SET 
    `question_title_fr` = 'Opération d\'addition',
    `question_content_fr` = '5 + 7 = ?',
    `options_fr` = '["A. 11", "B. 12", "C. 13", "D. 14"]',
    `correct_answer_fr` = 'B',
    `answer_explanation_fr` = '5 + 7 = 12'
WHERE `id` = 3;

-- =============================================
-- 10. 更新基础数据 - 测试
-- =============================================
UPDATE `test` SET 
    `test_name_fr` = 'Test de base en mathématiques de première année'
WHERE `test_name` = '一年级数学基础测试';

UPDATE `test` SET 
    `test_name_fr` = 'Test complet de mathématiques de deuxième année'
WHERE `test_name` = '二年级数学综合测试';

UPDATE `test` SET 
    `test_name_fr` = 'Test avancé de mathématiques de troisième année'
WHERE `test_name` = '三年级数学进阶测试';

UPDATE `test` SET 
    `test_name_fr` = 'Test d\'introduction aux mathématiques de septième année'
WHERE `test_name` = '七年级数学入门测试';

UPDATE `test` SET 
    `test_name_fr` = 'Test complet de mathématiques de neuvième année'
WHERE `test_name` = '九年级数学综合测试';

-- =============================================
-- 11. 验证更新结果
-- =============================================

-- 查看知识点分类的法语内容
SELECT 
    category_name,
    category_name_fr,
    description,
    description_fr
FROM knowledge_category 
WHERE delete_flag = 'N';

-- 查看知识点的法语内容和图标
SELECT 
    point_name,
    point_name_fr,
    description,
    description_fr,
    icon_url,
    icon_class
FROM knowledge_point 
WHERE delete_flag = 'N' 
LIMIT 5;

-- 查看题目的法语内容
SELECT 
    question_title,
    question_title_fr,
    question_content,
    question_content_fr
FROM question 
WHERE delete_flag = 'N' 
LIMIT 3;

-- 查看测试的法语内容
SELECT 
    test_name,
    test_name_fr
FROM test 
WHERE delete_flag = 'N';

-- =============================================
-- 脚本执行完成
-- =============================================

-- 显示表结构变更
SHOW COLUMNS FROM knowledge_category;
SHOW COLUMNS FROM knowledge_point;
SHOW COLUMNS FROM question;
SHOW COLUMNS FROM test;
SHOW COLUMNS FROM student_test_record;
SHOW COLUMNS FROM student_test_answer;
