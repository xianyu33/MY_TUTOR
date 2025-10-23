# 学生课程分配功能API文档

## 功能概述

学生课程分配功能提供了完整的学生学习进度管理，包括：
- 学生注册时自动分配课程
- 手动为现有学生分配课程
- 重新分配学生课程
- 学习进度统计查询
- 支持多语言知识点和图标显示

## 核心功能

### 1. 自动课程分配
在学生注册时，系统会根据学生填写的年级自动：
- 查找对应年级的所有知识点
- 为每个知识点创建学习进度记录
- 初始化学习状态为"未开始"
- 生成对应的测试题

### 2. 手动课程分配
支持为现有学生手动分配课程：
- 根据年级等级分配知识点
- 支持强制更新（删除现有进度重新分配）
- 防重复分配机制

### 3. 学习进度统计
提供详细的学习进度统计信息：
- 总体完成百分比
- 各状态知识点数量统计
- 学习时长统计
- 完成度分布统计

## API接口

### 1. 学生注册（自动分配课程）

**接口地址：** `POST /user/register`

**功能说明：** 学生注册时自动根据年级分配课程和生成测试题

**请求参数：**
```json
{
  "userAccount": "student001",
  "username": "张三",
  "sex": "1",
  "age": 15,
  "password": "YI37nVty2I0MSPRCk1Nh4A==",
  "tel": "13800138000",
  "country": "CHN",
  "email": "student001@example.com",
  "grade": "9"
}
```

**返回示例：**
```json
{
  "code": 200,
  "message": "注册成功，已自动分配课程和生成测试题",
  "data": true
}
```

### 2. 手动分配课程

**接口地址：** `POST /api/student/course/assign`

**功能说明：** 为现有学生手动分配课程

**请求参数：**
- `userId` (必填): 学生ID
- `gradeLevel` (必填): 年级等级（1-12）
- `forceUpdate` (可选): 是否强制更新，默认false

**请求示例：**
```
POST /api/student/course/assign?userId=1&gradeLevel=9&forceUpdate=false
```

**返回示例：**
```json
{
  "code": 200,
  "message": "课程分配成功",
  "data": true
}
```

### 3. 获取学习进度统计

**接口地址：** `GET /api/student/course/progress-stats/{userId}`

**功能说明：** 获取学生的学习进度统计信息

**请求示例：**
```
GET /api/student/course/progress-stats/1
```

**返回示例：**
```json
{
  "code": 200,
  "message": "获取学习进度统计成功",
  "data": {
    "userId": 1,
    "gradeLevel": 9,
    "gradeName": "九年级",
    "totalKnowledgePoints": 25,
    "notStartedCount": 15,
    "inProgressCount": 8,
    "completedCount": 2,
    "overallCompletionPercentage": 12.50,
    "totalStudyDuration": 180,
    "lastStudyTime": "2023-12-21 10:30:00",
    "distribution": {
      "lowProgressCount": 15,
      "mediumProgressCount": 6,
      "highProgressCount": 2,
      "nearCompleteCount": 0,
      "completeCount": 2
    }
  }
}
```

## 数据表结构

### learning_progress 表
存储学生的学习进度信息：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | INT | 主键ID |
| user_id | INT | 学生ID |
| knowledge_point_id | INT | 知识点ID |
| progress_status | TINYINT | 学习状态：1-未开始，2-学习中，3-已完成 |
| completion_percentage | DECIMAL | 完成百分比 |
| start_time | DATETIME | 开始学习时间 |
| complete_time | DATETIME | 完成学习时间 |
| study_duration | INT | 学习时长（分钟） |
| last_study_time | DATETIME | 最后学习时间 |
| notes | TEXT | 学习笔记 |
| create_at | DATETIME | 创建时间 |
| update_at | DATETIME | 更新时间 |
| delete_flag | CHAR | 删除标志 |

### knowledge_point 表（多语言支持）
存储知识点信息，支持中法双语和图标：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | INT | 主键ID |
| grade_id | INT | 年级ID |
| category_id | INT | 分类ID |
| point_name | VARCHAR | 知识点名称（中文） |
| point_name_fr | VARCHAR | 知识点名称（法语） |
| description | TEXT | 知识点描述（中文） |
| description_fr | TEXT | 知识点描述（法语） |
| content | TEXT | 知识点内容（中文） |
| content_fr | TEXT | 知识点内容（法语） |
| icon_url | VARCHAR | 图标文件URL |
| icon_class | VARCHAR | 图标CSS类名 |
| difficulty_level | TINYINT | 难度等级 |
| learning_objectives | TEXT | 学习目标（中文） |
| learning_objectives_fr | TEXT | 学习目标（法语） |

## 业务流程

### 1. 学生注册流程
```
1. 学生填写注册信息（包含年级）
2. 系统验证注册信息
3. 创建学生账户
4. 根据年级查找对应知识点
5. 为每个知识点创建学习进度记录
6. 生成初始测试题
7. 返回注册成功结果
```

### 2. 课程分配流程
```
1. 验证学生ID和年级等级
2. 查找年级对应的知识点
3. 检查是否已分配过课程
4. 为每个知识点创建学习进度记录
5. 记录分配结果统计
6. 返回分配结果
```

### 3. 学习进度统计流程
```
1. 获取学生的学习进度记录
2. 统计各状态的知识点数量
3. 计算总体完成百分比
4. 统计学习时长和最后学习时间
5. 分析完成度分布
6. 返回统计结果
```

## 年级映射

系统支持以下年级格式的自动解析：

| 年级字符串 | 年级等级 | 说明 |
|------------|----------|------|
| "1", "一年级", "1年级" | 1 | 小学一年级 |
| "2", "二年级", "2年级" | 2 | 小学二年级 |
| "3", "三年级", "3年级" | 3 | 小学三年级 |
| "4", "四年级", "4年级" | 4 | 小学四年级 |
| "5", "五年级", "5年级" | 5 | 小学五年级 |
| "6", "六年级", "6年级" | 6 | 小学六年级 |
| "7", "七年级", "7年级", "初一" | 7 | 初中一年级 |
| "8", "八年级", "8年级", "初二" | 8 | 初中二年级 |
| "9", "九年级", "9年级", "初三" | 9 | 初中三年级 |
| "10", "高一", "10年级" | 10 | 高中一年级 |
| "11", "高二", "11年级" | 11 | 高中二年级 |
| "12", "高三", "12年级" | 12 | 高中三年级 |

## 多语言支持

### 知识点多语言字段
- `point_name` / `point_name_fr` - 知识点名称
- `description` / `description_fr` - 知识点描述
- `content` / `content_fr` - 知识点内容
- `learning_objectives` / `learning_objectives_fr` - 学习目标

### 图标支持
- `icon_url` - 图标文件URL路径
- `icon_class` - 图标CSS类名
- 优先使用图片图标，回退到CSS图标

## 错误处理

### 常见错误码
- `400` - 参数错误
- `404` - 学生不存在
- `500` - 服务器内部错误

### 错误示例
```json
{
  "code": 404,
  "message": "学生不存在",
  "data": null
}
```

## 注意事项

1. **数据一致性**: 确保中法双语内容在语义上保持一致
2. **防重复分配**: 系统会自动检查是否已分配过课程，避免重复创建
3. **事务处理**: 课程分配过程使用事务处理，确保数据一致性
4. **日志记录**: 详细记录分配过程和结果，便于问题排查
5. **性能优化**: 大量知识点分配时建议分批处理
6. **权限控制**: 建议在生产环境中添加适当的权限验证

## 相关文档

- **数学知识点API**: `MATH_API_DOCUMENTATION.md`
- **学生测试功能API**: `STUDENT_TEST_API_DOCUMENTATION.md`
- **多语言支持**: `MULTILINGUAL_SUPPORT_README.md`
- **知识点图标**: `KNOWLEDGE_POINT_ICONS_README.md`
- **数据库架构**: `DATABASE_SCHEMA_README.md`
