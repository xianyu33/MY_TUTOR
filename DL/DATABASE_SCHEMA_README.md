# MY_TUTOR 数据库建表语句说明

## 文件说明

本项目提供了三个建表语句文件：

### 1. `complete_database_schema.sql` - 完整数据库建表语句
包含所有功能模块的完整建表语句：
- 用户管理（用户表、家长/老师表、关联表）
- 年级和知识点管理
- 学习进度管理
- 测试系统（核心功能）
- 聊天系统
- 统计报表
- 基础数据插入
- 索引优化
- 视图创建

### 2. `test_system_schema.sql` - 测试系统专用建表语句
专门针对测试功能的简化版建表语句：
- 基础表（用户、年级、知识点、题目）
- 测试系统核心表
- 基础数据插入
- 索引优化
- 统计视图

### 3. `test_business_schema.sql` - 原有测试业务表
原有的测试业务相关表结构（已存在）

## 使用建议

### 新项目部署
如果是全新部署，建议使用 `complete_database_schema.sql`：
```sql
-- 执行完整建表语句
source complete_database_schema.sql;
```

### 仅测试功能
如果只需要测试功能，使用 `test_system_schema.sql`：
```sql
-- 执行测试系统建表语句
source test_system_schema.sql;
```

### 现有项目升级
如果已有基础表结构，可以单独执行测试相关表的创建：
```sql
-- 只执行测试相关表的创建部分
source test_business_schema.sql;
```

## 核心表结构

### 基础表
- `user` - 用户表
- `grade` - 年级表
- `knowledge_category` - 知识点分类表
- `knowledge_point` - 知识点表
- `question` - 题目表

### 测试系统核心表
- `test` - 测试表
- `test_question` - 测试题目关联表
- `student_test_record` - 学生测试记录表
- `student_test_answer` - 学生测试答题详情表

## 功能特性

### 1. 随机题目生成
- 支持根据年级和难度随机选择题目
- 每次生成的测试题目都不同
- 通过 `test_question` 表管理测试与题目的关系

### 2. 学生测试记录绑定
- 每个测试记录都绑定到具体学生
- 支持多个学生同时进行不同测试
- 完整的测试状态管理（进行中/已完成/已超时）

### 3. 历史记录查询
- 支持分页查询学生测试历史
- 详细的答题情况记录
- 测试结果统计分析

### 4. 统计报表功能
- 自动计算测试统计信息
- 支持按年级筛选统计
- 提供平均得分和正确率分析

## 数据示例

建表语句中包含了基础数据：
- 12个年级（一年级到高三）
- 4个知识点分类（数与代数、几何、统计与概率、综合与实践）
- 10个示例知识点
- 13道示例题目
- 5个示例测试

## 索引优化

建表语句包含了以下索引优化：
- 用户表：按角色和年级的复合索引
- 知识点表：按年级和难度的复合索引
- 题目表：按知识点和难度的复合索引
- 测试记录表：按学生和状态的复合索引
- 答题详情表：按记录和正确性的复合索引

## 视图

提供了统计视图 `v_student_test_statistics`：
- 学生测试统计汇总
- 包含总测试数、完成测试数、正确率等关键指标
- 便于快速查询学生测试情况

## 注意事项

1. **执行前备份**：执行建表语句前请备份现有数据
2. **字符集**：使用 utf8mb4 字符集，支持完整的 Unicode 字符
3. **外键约束**：所有表都设置了适当的外键约束
4. **软删除**：使用 delete_flag 字段实现软删除
5. **时间戳**：所有表都包含创建时间和更新时间字段

## 测试数据

建表完成后，可以通过以下方式验证：
```sql
-- 查看所有表
SHOW TABLES;

-- 查看表结构
DESCRIBE user;
DESCRIBE test;
DESCRIBE student_test_record;

-- 查看基础数据
SELECT * FROM grade;
SELECT * FROM knowledge_category;
SELECT * FROM knowledge_point LIMIT 5;
SELECT * FROM question LIMIT 5;
SELECT * FROM test;

-- 查看统计视图
SELECT * FROM v_student_test_statistics;
```

## API 接口

建表完成后，可以使用以下 API 接口：
- `POST /api/student-test/generate-random` - 生成随机测试
- `POST /api/student-test/start` - 开始测试
- `POST /api/student-test/submit-answer` - 提交答案
- `POST /api/student-test/complete` - 完成测试
- `GET /api/student-test/history/{studentId}` - 查询历史记录
- `GET /api/student-test/statistics/{studentId}` - 获取统计报表

详细的 API 文档请参考 `STUDENT_TEST_API_DOCUMENTATION.md` 文件。
