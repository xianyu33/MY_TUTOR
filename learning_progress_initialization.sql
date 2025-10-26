-- Learning Progress Initialization SQL Script
-- This script demonstrates how learning progress data is initialized when a new student is registered

-- =============================================
-- Example: Initialize Learning Progress for a New Grade 7 Student
-- =============================================

-- Assume we have a new student with user_id = 1001 and grade_id = 7

-- Step 1: Create student-category bindings for Grade 7
-- ===================================================
INSERT INTO `student_category_binding` 
(`student_id`, `category_id`, `grade_id`, `binding_status`, 
 `overall_progress`, `total_knowledge_points`, 
 `completed_knowledge_points`, `in_progress_knowledge_points`, 
 `not_started_knowledge_points`, `total_study_duration`, 
 `create_at`, `delete_flag`) 
VALUES

-- Category 1: Number and Algebra (13 knowledge points)
(1001, 1, 7, 1, 0.00, 13, 0, 0, 13, 0, NOW(), 'N'),

-- Category 2: Geometry (10 knowledge points)
(1001, 2, 7, 1, 0.00, 10, 0, 0, 10, 0, NOW(), 'N'),

-- Category 3: Statistics and Probability (3 knowledge points)
(1001, 3, 7, 1, 0.00, 3, 0, 0, 3, 0, NOW(), 'N'),

-- Category 4: Comprehensive Application (4 knowledge points)
(1001, 4, 7, 1, 0.00, 4, 0, 0, 4, 0, NOW(), 'N')

ON DUPLICATE KEY UPDATE 
binding_status=VALUES(binding_status), 
overall_progress=VALUES(overall_progress);

-- Step 2: Create learning progress records for each knowledge point
-- ==============================================================
-- Note: Replace the knowledge_point_id values with actual IDs from your database

INSERT INTO `learning_progress` 
(`user_id`, `knowledge_point_id`, `knowledge_category_id`, 
 `progress_status`, `completion_percentage`, `study_duration`, 
 `create_at`, `update_at`, `delete_flag`) 
SELECT 
    1001 AS user_id,
    kp.id AS knowledge_point_id,
    kp.category_id AS knowledge_category_id,
    1 AS progress_status,  -- Not Started
    0.00 AS completion_percentage,
    0 AS study_duration,
    NOW() AS create_at,
    NOW() AS update_at,
    'N' AS delete_flag
FROM `knowledge_point` kp
WHERE kp.grade_id = 7  -- Grade 7
AND kp.delete_flag = 'N'
ON DUPLICATE KEY UPDATE 
progress_status=VALUES(progress_status),
completion_percentage=VALUES(completion_percentage),
update_at=VALUES(update_at);

-- =============================================
-- Verification Queries
-- =============================================

-- Query 1: Check created bindings
SELECT 
    scb.id,
    scb.student_id,
    kc.category_name,
    scb.total_knowledge_points,
    scb.completed_knowledge_points,
    scb.in_progress_knowledge_points,
    scb.not_started_knowledge_points,
    scb.overall_progress
FROM `student_category_binding` scb
JOIN `knowledge_category` kc ON scb.category_id = kc.id
WHERE scb.student_id = 1001
AND scb.delete_flag = 'N'
ORDER BY kc.sort_order;

-- Query 2: Check created learning progress
SELECT 
    lp.id,
    lp.user_id,
    kp.point_name,
    kc.category_name,
    lp.progress_status,
    lp.completion_percentage,
    lp.study_duration,
    lp.last_study_time
FROM `learning_progress` lp
JOIN `knowledge_point` kp ON lp.knowledge_point_id = kp.id
JOIN `knowledge_category` kc ON lp.knowledge_category_id = kc.id
WHERE lp.user_id = 1001
AND lp.delete_flag = 'N'
ORDER BY kc.sort_order, kp.sort_order;

-- Query 3: Get learning progress summary by category
SELECT 
    kc.category_name,
    COUNT(lp.id) as total_points,
    COUNT(CASE WHEN lp.progress_status = 1 THEN 1 END) as not_started,
    COUNT(CASE WHEN lp.progress_status = 2 THEN 1 END) as in_progress,
    COUNT(CASE WHEN lp.progress_status = 3 THEN 1 END) as completed,
    ROUND(AVG(lp.completion_percentage), 2) as avg_completion,
    SUM(lp.study_duration) as total_study_minutes
FROM `learning_progress` lp
JOIN `knowledge_category` kc ON lp.knowledge_category_id = kc.id
WHERE lp.user_id = 1001
AND lp.delete_flag = 'N'
GROUP BY kc.category_name
ORDER BY kc.sort_order;

-- Query 4: Get overall learning progress statistics
SELECT 
    u.user_account,
    u.username,
    g.grade_name,
    COUNT(lp.id) as total_knowledge_points,
    COUNT(CASE WHEN lp.progress_status = 1 THEN 1 END) as not_started_count,
    COUNT(CASE WHEN lp.progress_status = 2 THEN 1 END) as in_progress_count,
    COUNT(CASE WHEN lp.progress_status = 3 THEN 1 END) as completed_count,
    ROUND(AVG(lp.completion_percentage), 2) as overall_completion_percentage,
    SUM(lp.study_duration) as total_study_duration_minutes
FROM `user` u
JOIN `learning_progress` lp ON u.id = lp.user_id
JOIN `grade` g ON u.grade = CAST(g.grade_level AS CHAR)
WHERE u.id = 1001
AND u.delete_flag = '0'
AND lp.delete_flag = 'N'
GROUP BY u.id, u.user_account, u.username, g.grade_name;

-- =============================================
-- Update Learning Progress (Example)
-- =============================================

-- Example: Mark a knowledge point as "In Progress"
UPDATE `learning_progress` 
SET 
    progress_status = 2,  -- In Progress
    completion_percentage = 25.00,
    start_time = NOW(),
    last_study_time = NOW(),
    study_duration = 30,
    update_at = NOW()
WHERE user_id = 1001
AND knowledge_point_id = (SELECT id FROM knowledge_point WHERE point_code = 'POSITIVE_NEGATIVE_NUMBERS')
AND delete_flag = 'N';

-- Example: Mark a knowledge point as "Completed"
UPDATE `learning_progress` 
SET 
    progress_status = 3,  -- Completed
    completion_percentage = 100.00,
    complete_time = NOW(),
    last_study_time = NOW(),
    study_duration = 120,
    update_at = NOW()
WHERE user_id = 1001
AND knowledge_point_id = (SELECT id FROM knowledge_point WHERE point_code = 'POSITIVE_NEGATIVE_NUMBERS')
AND delete_flag = 'N';

-- =============================================
-- Update Category Binding Statistics
-- =============================================

-- Update category binding based on learning progress
UPDATE `student_category_binding` scb
SET 
    completed_knowledge_points = (
        SELECT COUNT(*) 
        FROM `learning_progress` lp 
        WHERE lp.user_id = scb.student_id 
        AND lp.knowledge_category_id = scb.category_id
        AND lp.progress_status = 3
        AND lp.delete_flag = 'N'
    ),
    in_progress_knowledge_points = (
        SELECT COUNT(*) 
        FROM `learning_progress` lp 
        WHERE lp.user_id = scb.student_id 
        AND lp.knowledge_category_id = scb.category_id
        AND lp.progress_status = 2
        AND lp.delete_flag = 'N'
    ),
    not_started_knowledge_points = (
        SELECT COUNT(*) 
        FROM `learning_progress` lp 
        WHERE lp.user_id = scb.student_id 
        AND lp.knowledge_category_id = scb.category_id
        AND lp.progress_status = 1
        AND lp.delete_flag = 'N'
    ),
    overall_progress = (
        SELECT COALESCE(ROUND(AVG(lp.completion_percentage), 2), 0.00)
        FROM `learning_progress` lp 
        WHERE lp.user_id = scb.student_id 
        AND lp.knowledge_category_id = scb.category_id
        AND lp.delete_flag = 'N'
    ),
    last_study_time = (
        SELECT MAX(lp.last_study_time)
        FROM `learning_progress` lp 
        WHERE lp.user_id = scb.student_id 
        AND lp.knowledge_category_id = scb.category_id
        AND lp.delete_flag = 'N'
    ),
    total_study_duration = (
        SELECT COALESCE(SUM(lp.study_duration), 0)
        FROM `learning_progress` lp 
        WHERE lp.user_id = scb.student_id 
        AND lp.knowledge_category_id = scb.category_id
        AND lp.delete_flag = 'N'
    ),
    binding_status = CASE
        WHEN (
            SELECT COUNT(*) 
            FROM `learning_progress` lp 
            WHERE lp.user_id = scb.student_id 
            AND lp.knowledge_category_id = scb.category_id
            AND lp.progress_status = 3
            AND lp.delete_flag = 'N'
        ) = scb.total_knowledge_points THEN 3  -- Completed
        WHEN (
            SELECT COUNT(*) 
            FROM `learning_progress` lp 
            WHERE lp.user_id = scb.student_id 
            AND lp.knowledge_category_id = scb.category_id
            AND lp.progress_status != 1
            AND lp.delete_flag = 'N'
        ) > 0 THEN 2  -- Learning
        ELSE 1  -- Bound
    END,
    update_at = NOW()
WHERE scb.student_id = 1001
AND scb.delete_flag = 'N';
