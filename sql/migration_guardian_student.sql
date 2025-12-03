-- 增量迁移脚本：家长/老师-学生关系模块
-- 适用：已存在数据库与 parent 表的环境
-- 风险提示：执行前请备份

SET NAMES utf8mb4;

-- 1) parent 表：确保存在 type 字段（0-家长 1-老师）
ALTER TABLE `parent`
  MODIFY COLUMN `type` tinyint(1) unsigned zerofill DEFAULT '0' COMMENT '0-家长  1-老师';

-- 2) 使用现有 user 表充当学生表，无需创建 student 表

-- 3) 创建家长/老师 与 学生 关联表（如果不存在）
CREATE TABLE IF NOT EXISTS `guardian_student_rel` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `guardian_id` int NOT NULL COMMENT '家长/老师ID（关联 parent.id）',
  `guardian_type` tinyint(1) NOT NULL COMMENT '0-家长 1-老师（冗余）',
  `student_id` int NOT NULL COMMENT '学生ID（关联 user.id）',
  `relation` varchar(50) DEFAULT NULL COMMENT '关系：父亲/母亲/监护人/班主任/任课老师等',
  `start_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '关系开始时间',
  `end_at` datetime DEFAULT NULL COMMENT '关系结束时间（空表示有效）',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(255) DEFAULT NULL,
  `update_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(255) DEFAULT NULL,
  `delete_flag` char(1) DEFAULT '0' COMMENT '0-正常 1-删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_guardian_student_active` (`guardian_id`,`student_id`,`delete_flag`),
  KEY `idx_guardian_type` (`guardian_type`),
  KEY `idx_student_id` (`student_id`),
  CONSTRAINT `fk_gsr_guardian` FOREIGN KEY (`guardian_id`) REFERENCES `parent` (`id`),
  CONSTRAINT `fk_gsr_student` FOREIGN KEY (`student_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家长/老师 与 学生 关联表';

-- 使用方式（登录目标库后执行）：
-- SOURCE migration_guardian_student.sql;


