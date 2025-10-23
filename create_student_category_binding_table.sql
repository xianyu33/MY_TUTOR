-- =============================================
-- 学生知识点分类绑定关系表创建脚本
-- =============================================
-- 脚本名称: create_student_category_binding_table.sql
-- 创建时间: 2023-12-21
-- 说明: 创建学生知识点分类绑定关系表，用于保存知识点类型的学习进度
-- =============================================

USE tutor;

-- =============================================
-- 1. 创建学生知识点分类绑定关系表
-- =============================================

CREATE TABLE IF NOT EXISTS `student_category_binding` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '绑定关系ID',
  `student_id` INT NOT NULL COMMENT '学生ID',
  `category_id` INT NOT NULL COMMENT '知识点分类ID',
  `grade_id` INT NOT NULL COMMENT '年级ID',
  `binding_status` TINYINT DEFAULT 1 COMMENT '绑定状态：1-已绑定，2-学习中，3-已完成',
  `overall_progress` DECIMAL(5,2) DEFAULT 0.00 COMMENT '整体学习进度百分比',
  `total_knowledge_points` INT DEFAULT 0 COMMENT '该分类下总知识点数量',
  `completed_knowledge_points` INT DEFAULT 0 COMMENT '已完成的知识点数量',
  `in_progress_knowledge_points` INT DEFAULT 0 COMMENT '学习中的知识点数量',
  `not_started_knowledge_points` INT DEFAULT 0 COMMENT '未开始的知识点数量',
  `total_study_duration` INT DEFAULT 0 COMMENT '总学习时长（分钟）',
  `last_study_time` DATETIME COMMENT '最后学习时间',
  `start_time` DATETIME COMMENT '开始学习时间',
  `complete_time` DATETIME COMMENT '完成时间',
  `notes` TEXT COMMENT '学习笔记',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `delete_flag` CHAR(1) DEFAULT 'N' COMMENT '删除标志：Y-已删除，N-未删除',
  UNIQUE KEY `uk_student_category` (`student_id`, `category_id`),
  INDEX `idx_student_grade` (`student_id`, `grade_id`),
  INDEX `idx_binding_status` (`binding_status`),
  INDEX `idx_overall_progress` (`overall_progress`),
  INDEX `idx_last_study_time` (`last_study_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生知识点分类绑定关系表';

-- =============================================
-- 2. 为现有学生创建分类绑定关系
-- =============================================

-- 为所有学生创建分类绑定关系（基于他们的年级）
INSERT INTO `student_category_binding` (
    `student_id`, `category_id`, `grade_id`, `binding_status`, `overall_progress`,
    `total_knowledge_points`, `completed_knowledge_points`, `in_progress_knowledge_points`,
    `not_started_knowledge_points`, `total_study_duration`, `create_at`, `update_at`, `delete_flag`
)
SELECT
    u.id as student_id,
    kc.id as category_id,
    u.grade_id as grade_id,
    1 as binding_status,
    0.00 as overall_progress,
    0 as total_knowledge_points,
    0 as completed_knowledge_points,
    0 as in_progress_knowledge_points,
    0 as not_started_knowledge_points,
    0 as total_study_duration,
    NOW() as create_at,
    NOW() as update_at,
    'N' as delete_flag
FROM `user` u
JOIN `knowledge_category` kc ON kc.grade_id = u.grade_id
WHERE u.role = 'student' AND u.delete_flag = '0'
ON DUPLICATE KEY UPDATE
    `update_at` = NOW();

-- =============================================
-- 3. 更新绑定关系的统计信息
-- =============================================

-- 更新每个绑定关系的统计信息
UPDATE `student_category_binding` scb
SET
    `total_knowledge_points` = (
        SELECT COUNT(*)
        FROM `knowledge_point` kp
        WHERE kp.category_id = scb.category_id AND kp.delete_flag = 'N'
    ),
    `completed_knowledge_points` = (
        SELECT COUNT(*)
        FROM `learning_progress` lp
        JOIN `knowledge_point` kp ON lp.knowledge_point_id = kp.id
        WHERE lp.user_id = scb.student_id
        AND kp.category_id = scb.category_id
        AND lp.progress_status = 3
        AND lp.delete_flag = 'N'
    ),
    `in_progress_knowledge_points` = (
        SELECT COUNT(*)
        FROM `learning_progress` lp
        JOIN `knowledge_point` kp ON lp.knowledge_point_id = kp.id
        WHERE lp.user_id = scb.student_id
        AND kp.category_id = scb.category_id
        AND lp.progress_status = 2
        AND lp.delete_flag = 'N'
    ),
    `not_started_knowledge_points` = (
        SELECT COUNT(*)
        FROM `learning_progress` lp
        JOIN `knowledge_point` kp ON lp.knowledge_point_id = kp.id
        WHERE lp.user_id = scb.student_id
        AND kp.category_id = scb.category_id
        AND lp.progress_status = 1
        AND lp.delete_flag = 'N'
    ),
    `total_study_duration` = (
        SELECT COALESCE(SUM(lp.study_duration), 0)
        FROM `learning_progress` lp
        JOIN `knowledge_point` kp ON lp.knowledge_point_id = kp.id
        WHERE lp.user_id = scb.student_id
        AND kp.category_id = scb.category_id
        AND lp.delete_flag = 'N'
    ),
    `last_study_time` = (
        SELECT MAX(lp.last_study_time)
        FROM `learning_progress` lp
        JOIN `knowledge_point` kp ON lp.knowledge_point_id = kp.id
        WHERE lp.user_id = scb.student_id
        AND kp.category_id = scb.category_id
        AND lp.delete_flag = 'N'
    ),
    `overall_progress` = (
        SELECT CASE
            WHEN COUNT(kp.id) > 0 THEN
                ROUND(SUM(COALESCE(lp.completion_percentage, 0)) / COUNT(kp.id), 2)
            ELSE 0.00
        END
        FROM `knowledge_point` kp
        LEFT JOIN `learning_progress` lp ON lp.knowledge_point_id = kp.id
            AND lp.user_id = scb.student_id
            AND lp.delete_flag = 'N'
        WHERE kp.category_id = scb.category_id
        AND kp.delete_flag = 'N'
    ),
    `update_at` = NOW()
WHERE scb.delete_flag = 'N';

-- =============================================
-- 4. 验证创建结果
-- =============================================

-- 显示表结构
DESCRIBE `student_category_binding`;

-- 显示创建的数据统计
SELECT
    COUNT(*) as total_bindings,
    COUNT(DISTINCT student_id) as total_students,
    COUNT(DISTINCT category_id) as total_categories,
    COUNT(DISTINCT grade_id) as total_grades
FROM `student_category_binding`
WHERE delete_flag = 'N';

-- 显示绑定关系示例
SELECT
    scb.id,
    u.username as student_name,
    u.grade as student_grade,
    kc.category_name,
    scb.binding_status,
    scb.overall_progress,
    scb.total_knowledge_points,
    scb.completed_knowledge_points,
    scb.in_progress_knowledge_points,
    scb.not_started_knowledge_points,
    scb.total_study_duration
FROM `student_category_binding` scb
JOIN `user` u ON scb.student_id = u.id
JOIN `knowledge_category` kc ON scb.category_id = kc.id
WHERE scb.delete_flag = 'N'
ORDER BY scb.student_id, scb.category_id
LIMIT 10;

-- =============================================
-- 5. 回滚脚本（如需要）
-- =============================================
-- 如果需要回滚，可以执行以下语句：
--
-- -- 删除表
-- DROP TABLE IF EXISTS `student_category_binding`;

-- =============================================
-- 脚本执行完成
-- =============================================
SELECT '学生知识点分类绑定关系表创建完成！' AS message;
