-- 自适应测验：新增难度分配和关联测试记录字段
ALTER TABLE `test` ADD COLUMN `difficulty_distribution` VARCHAR(100) DEFAULT NULL
  COMMENT '难度分配JSON，如 {"1":30,"2":40,"3":30} 表示百分比' AFTER `difficulty_level`;
ALTER TABLE `test` ADD COLUMN `previous_test_record_id` INT DEFAULT NULL
  COMMENT '自适应测验：上一次测验记录ID' AFTER `difficulty_distribution`;
