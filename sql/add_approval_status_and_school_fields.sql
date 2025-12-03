-- =============================================
-- 数据库迁移脚本：添加审批状态字段和学校字段
-- =============================================

USE tutor;

-- 1. 为 parent 表添加审批状态字段（approval_status）
-- 审批状态：0-未审批，1-已审批
-- 注意：如果字段已存在，请先删除再执行，或使用 MODIFY 语句修改
ALTER TABLE `parent` 
ADD COLUMN `approval_status` TINYINT DEFAULT 0 COMMENT '审批状态：0-未审批，1-已审批' AFTER `type`;

-- 如果字段已存在，取消上面的 ADD COLUMN，使用下面的 MODIFY 语句
-- ALTER TABLE `parent` 
-- MODIFY COLUMN `approval_status` TINYINT DEFAULT 0 COMMENT '审批状态：0-未审批，1-已审批';

-- 为已存在的老师用户（type=1）设置默认审批状态为0-未审批
UPDATE `parent` 
SET `approval_status` = 0 
WHERE `type` = 1 AND (`approval_status` IS NULL);

-- 2. 为 user 表添加学校字段（school）
-- 注意：如果字段已存在，请先删除再执行，或使用 MODIFY 语句修改
ALTER TABLE `user` 
ADD COLUMN `school` VARCHAR(100) COMMENT '学校' AFTER `grade`;

-- 如果字段已存在，取消上面的 ADD COLUMN，使用下面的 MODIFY 语句
-- ALTER TABLE `user` 
-- MODIFY COLUMN `school` VARCHAR(100) COMMENT '学校';

-- 3. 为 parent 表添加学校字段（school）
-- 注意：如果字段已存在，请先删除再执行，或使用 MODIFY 语句修改
ALTER TABLE `parent` 
ADD COLUMN `school` VARCHAR(100) COMMENT '学校' AFTER `grade`;

-- 如果字段已存在，取消上面的 ADD COLUMN，使用下面的 MODIFY 语句
-- ALTER TABLE `parent` 
-- MODIFY COLUMN `school` VARCHAR(100) COMMENT '学校';

-- 添加索引（可选，如果需要根据学校查询）
-- CREATE INDEX `idx_school` ON `user` (`school`);

-- 添加审批状态索引（可选，如果需要根据审批状态查询老师）
-- CREATE INDEX `idx_approval_status` ON `parent` (`approval_status`);

