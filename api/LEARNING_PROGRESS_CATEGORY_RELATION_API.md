# 学习进度与知识点分类关系功能API文档

## 功能概述

学习进度与知识点分类关系功能为MY_TUTOR系统提供了更精细的学习管理能力，包括：
- 学习进度记录知识点分类信息
- 知识点分类关联年级信息
- 学生与知识点分类的绑定关系
- 基于分类的学习进度查询和统计

## 数据库变更

### 1. knowledge_category表新增字段

| 字段名 | 类型 | 说明 | 示例 |
|--------|------|------|------|
| `grade_id` | INT | 年级ID | 1 |
| `icon_url` | VARCHAR(500) | 分类图标URL | `/icons/category/numbers-algebra.png` |
| `icon_class` | VARCHAR(100) | 分类图标CSS类名 | `icon-category-numbers` |

### 2. learning_progress表新增字段

| 字段名 | 类型 | 说明 | 示例 |
|--------|------|------|------|
| `knowledge_category_id` | INT | 知识点分类ID | 1 |

### 3. 新增learning_progress表（如果不存在）

完整的learning_progress表结构，包含所有必要的字段和约束。

## API接口

### 1. 学生课程管理接口

#### 1.1 手动分配课程
- **URL**: `POST /api/student/course/assign`
- **参数**: 
  - `userId` (必填): 学生ID
  - `gradeLevel` (必填): 年级等级（1-12）
  - `forceUpdate` (可选): 是否强制更新，默认false
- **功能**: 为现有学生手动分配课程，自动绑定知识点分类

**请求示例**:
```bash
POST /api/student/course/assign?userId=1&gradeLevel=9&forceUpdate=false
```

**返回示例**:
```json
{
  "code": 200,
  "message": "课程分配成功",
  "data": true
}
```

#### 1.2 获取学习进度统计
- **URL**: `GET /api/student/course/progress-stats/{userId}`
- **功能**: 获取学生的学习进度统计信息

**请求示例**:
```bash
GET /api/student/course/progress-stats/1
```

**返回示例**:
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

### 2. 学生分类绑定接口

#### 2.1 获取学生绑定的知识点分类列表
- **URL**: `GET /api/student/course/bound-categories/{userId}`
- **功能**: 获取学生绑定的知识点分类列表

**请求示例**:
```bash
GET /api/student/course/bound-categories/1
```

**返回示例**:
```json
{
  "code": 200,
  "message": "获取学生绑定分类列表成功",
  "data": [
    {
      "id": 1,
      "categoryName": "数与代数",
      "categoryNameFr": "Nombres et Algèbre",
      "categoryCode": "NUM_ALG",
      "description": "数的认识、运算、代数式等",
      "descriptionFr": "Reconnaissance des nombres, opérations, expressions algébriques, etc.",
      "gradeId": 1,
      "iconUrl": "/icons/category/numbers-algebra.png",
      "iconClass": "icon-category-numbers",
      "sortOrder": 1
    },
    {
      "id": 2,
      "categoryName": "几何",
      "categoryNameFr": "Géométrie",
      "categoryCode": "GEOMETRY",
      "description": "图形认识、测量、变换等",
      "descriptionFr": "Reconnaissance des formes, mesure, transformation, etc.",
      "gradeId": 1,
      "iconUrl": "/icons/category/geometry.png",
      "iconClass": "icon-category-geometry",
      "sortOrder": 2
    }
  ]
}
```

#### 2.2 根据年级获取知识点分类列表
- **URL**: `GET /api/student/course/categories/grade/{gradeId}`
- **功能**: 根据年级ID获取知识点分类列表

**请求示例**:
```bash
GET /api/student/course/categories/grade/1
```

**返回示例**:
```json
{
  "code": 200,
  "message": "获取年级分类列表成功",
  "data": [
    {
      "id": 1,
      "categoryName": "数与代数",
      "categoryCode": "NUM_ALG",
      "gradeId": 1,
      "iconUrl": "/icons/category/numbers-algebra.png",
      "iconClass": "icon-category-numbers",
      "sortOrder": 1
    }
  ]
}
```

### 3. 学生注册接口（增强）

#### 3.1 学生注册（自动分配课程和分类）
- **URL**: `POST /user/register`
- **功能**: 学生注册时自动根据年级分配课程和知识点分类

**请求参数**:
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

**返回示例**:
```json
{
  "code": 200,
  "message": "注册成功，已自动分配课程和生成测试题",
  "data": true
}
```

## 业务流程

### 1. 学生注册流程（增强）
```
1. 学生填写注册信息（包含年级）
2. 系统验证注册信息
3. 创建学生账户
4. 根据年级查找对应知识点
5. 为每个知识点创建学习进度记录（包含分类ID）
6. 生成初始测试题
7. 返回注册成功结果
```

### 2. 课程分配流程（增强）
```
1. 验证学生ID和年级等级
2. 查找年级对应的知识点
3. 检查是否已分配过课程
4. 为每个知识点创建学习进度记录（包含分类ID）
5. 记录分配结果统计
6. 返回分配结果
```

### 3. 分类绑定查询流程
```
1. 获取学生的学习进度记录
2. 提取唯一的分类ID
3. 根据分类ID获取分类信息
4. 按排序字段排序
5. 返回分类列表
```

## 数据表关系

### 1. 表关系图
```
user (学生)
  ↓ (1:N)
learning_progress (学习进度)
  ↓ (N:1)
knowledge_point (知识点)
  ↓ (N:1)
knowledge_category (知识点分类)
  ↓ (N:1)
grade (年级)
```

### 2. 关键约束
- `learning_progress.user_id` → `user.id`
- `learning_progress.knowledge_point_id` → `knowledge_point.id`
- `learning_progress.knowledge_category_id` → `knowledge_category.id`
- `knowledge_category.grade_id` → `grade.id`
- `knowledge_point.category_id` → `knowledge_category.id`

## 使用示例

### 1. 前端查询学生分类绑定
```javascript
// 获取学生绑定的分类列表
async function getStudentCategories(userId) {
  const response = await fetch(`/api/student/course/bound-categories/${userId}`);
  const result = await response.json();
  
  if (result.code === 200) {
    return result.data;
  }
  throw new Error(result.message);
}

// 使用示例
const categories = await getStudentCategories(1);
categories.forEach(category => {
  console.log(`分类: ${category.categoryName}, 图标: ${category.iconUrl}`);
});
```

### 2. 后端服务调用
```java
// 获取学生绑定的分类
List<KnowledgeCategory> categories = studentRegistrationService.getStudentBoundCategories(userId);

// 根据年级获取分类
List<KnowledgeCategory> gradeCategories = studentRegistrationService.getCategoriesByGrade(gradeId);

// 获取学习进度统计
LearningProgressStats stats = studentRegistrationService.getLearningProgressStats(userId);
```

## 注意事项

1. **数据一致性**: 确保知识点分类与年级的对应关系正确
2. **外键约束**: 删除分类时会自动处理相关的学习进度记录
3. **索引优化**: 添加了必要的索引以提高查询性能
4. **多语言支持**: 分类支持中法双语显示
5. **图标支持**: 分类支持图片和CSS图标两种方式
6. **事务处理**: 课程分配过程使用事务确保数据一致性

## 相关文档

- **学生课程分配功能**: `STUDENT_COURSE_ASSIGNMENT_API.md`
- **知识点分类图标**: `KNOWLEDGE_CATEGORY_ICONS_README.md`
- **多语言支持**: `MULTILINGUAL_SUPPORT_README.md`
- **数据库架构**: `DATABASE_SCHEMA_README.md`
