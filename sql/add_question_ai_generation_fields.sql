-- =====================================================
-- AI 题目生成功能 - 数据库变更脚本
-- 创建时间: 2025-01-08
-- 功能说明:
--   1. Question 表新增 AI 生成相关字段
--   2. 支持记录题目来源（人工录入/AI生成）
--   3. student_test_record 表新增 test_name_fr 字段（如不存在）
-- =====================================================

-- 1. Question 表新增字段
ALTER TABLE question
    ADD COLUMN generation_source VARCHAR(20) DEFAULT 'MANUAL' COMMENT '来源：MANUAL-人工录入, AI-AI生成',
    ADD COLUMN model_id VARCHAR(100) DEFAULT NULL COMMENT 'AI模型ID（AI生成时记录）',
    ADD COLUMN prompt_used TEXT DEFAULT NULL COMMENT '生成时使用的提示词';

-- 2. 为 generation_source 添加索引，便于按来源筛选
CREATE INDEX idx_question_generation_source ON question(generation_source);

-- 3. 为查询学生未做过的题目添加复合索引（提升查询性能）
CREATE INDEX idx_question_kp_difficulty ON question(knowledge_point_id, difficulty_level, delete_flag);

-- 4. student_test_record 表新增 test_name_fr 字段（如果不存在）
-- 注意：如果字段已存在，此语句会报错，可忽略
ALTER TABLE student_test_record
    ADD COLUMN test_name_fr VARCHAR(200) DEFAULT NULL COMMENT '测试名称（法语）' AFTER test_name;
