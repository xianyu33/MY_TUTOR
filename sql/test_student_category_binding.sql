-- =============================================
-- 测试学生知识点分类绑定关系功能
-- =============================================

-- 1. 检查表是否存在
SHOW TABLES LIKE 'student_category_binding';

-- 2. 检查表结构
DESCRIBE student_category_binding;

-- 3. 检查现有数据
SELECT COUNT(*) as total_bindings FROM student_category_binding WHERE delete_flag = 'N';

-- 4. 检查分类数据
SELECT 
    kc.id,
    kc.category_name,
    kc.grade_id,
    COUNT(kp.id) as knowledge_point_count
FROM knowledge_category kc
LEFT JOIN knowledge_point kp ON kc.id = kp.category_id AND kp.delete_flag = 'N'
WHERE kc.delete_flag = 'N'
GROUP BY kc.id, kc.category_name, kc.grade_id
ORDER BY kc.grade_id, kc.sort_order;

-- 5. 检查学生数据
SELECT 
    u.id,
    u.username,
    u.grade,
    COUNT(scb.id) as binding_count
FROM user u
LEFT JOIN student_category_binding scb ON u.id = scb.student_id AND scb.delete_flag = 'N'
WHERE u.role = 'student' AND u.delete_flag = '0'
GROUP BY u.id, u.username, u.grade
ORDER BY u.id;

-- 6. 检查学习进度数据
SELECT 
    lp.user_id,
    lp.knowledge_category_id,
    COUNT(*) as progress_count,
    AVG(lp.completion_percentage) as avg_completion
FROM learning_progress lp
WHERE lp.delete_flag = 'N'
GROUP BY lp.user_id, lp.knowledge_category_id
ORDER BY lp.user_id, lp.knowledge_category_id;
