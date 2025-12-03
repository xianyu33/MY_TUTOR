# 学生知识点分类绑定关系功能API文档

## 功能概述

学生知识点分类绑定关系功能为MY_TUTOR系统提供了更精细的学习管理能力，包括：
- 学生与知识点分类的绑定关系管理
- 知识点分类级别的学习进度统计
- 分类下知识点的详细学习情况查询
- 自动化的分类绑定关系创建和维护

## 数据库设计

### student_category_binding 表

| 字段名 | 类型 | 说明 | 示例 |
|--------|------|------|------|
| `id` | INT | 绑定关系ID | 1 |
| `student_id` | INT | 学生ID | 1 |
| `category_id` | INT | 知识点分类ID | 1 |
| `grade_id` | INT | 年级ID | 1 |
| `binding_status` | TINYINT | 绑定状态：1-已绑定，2-学习中，3-已完成 | 1 |
| `overall_progress` | DECIMAL(5,2) | 整体学习进度百分比 | 25.50 |
| `total_knowledge_points` | INT | 该分类下总知识点数量 | 10 |
| `completed_knowledge_points` | INT | 已完成的知识点数量 | 3 |
| `in_progress_knowledge_points` | INT | 学习中的知识点数量 | 2 |
| `not_started_knowledge_points` | INT | 未开始的知识点数量 | 5 |
| `total_study_duration` | INT | 总学习时长（分钟） | 120 |
| `last_study_time` | DATETIME | 最后学习时间 | 2023-12-21 10:30:00 |
| `start_time` | DATETIME | 开始学习时间 | 2023-12-20 09:00:00 |
| `complete_time` | DATETIME | 完成时间 | NULL |
| `notes` | TEXT | 学习笔记 | 需要重点复习 |

## API接口

### 1. 学生分类学习进度查询

#### 1.1 获取学生所有分类的学习进度详情
- **URL**: `GET /api/student/course/category-progress/{userId}`
- **功能**: 获取学生所有分类的学习进度详情，包含每个分类下的知识点学习情况

**请求示例**:
```bash
GET /api/student/course/category-progress/1
```

**返回示例**:
```json
{
  "code": 200,
  "message": "获取学生分类学习进度详情成功",
  "data": [
    {
      "id": 1,
      "studentId": 1,
      "categoryId": 1,
      "gradeId": 1,
      "bindingStatus": 2,
      "overallProgress": 25.50,
      "totalKnowledgePoints": 10,
      "completedKnowledgePoints": 3,
      "inProgressKnowledgePoints": 2,
      "notStartedKnowledgePoints": 5,
      "totalStudyDuration": 120,
      "lastStudyTime": "2023-12-21 10:30:00",
      "startTime": "2023-12-20 09:00:00",
      "completeTime": null,
      "notes": null,
      "knowledgeCategory": {
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
      "learningProgressList": [
        {
          "id": 1,
          "userId": 1,
          "knowledgePointId": 1,
          "knowledgeCategoryId": 1,
          "progressStatus": 3,
          "completionPercentage": 100.00,
          "startTime": "2023-12-20 09:00:00",
          "completeTime": "2023-12-20 10:00:00",
          "studyDuration": 60,
          "lastStudyTime": "2023-12-20 10:00:00",
          "notes": null
        },
        {
          "id": 2,
          "userId": 1,
          "knowledgePointId": 2,
          "knowledgeCategoryId": 1,
          "progressStatus": 2,
          "completionPercentage": 50.00,
          "startTime": "2023-12-21 09:00:00",
          "completeTime": null,
          "studyDuration": 30,
          "lastStudyTime": "2023-12-21 10:30:00",
          "notes": null
        }
      ]
    }
  ]
}
```

#### 1.2 获取学生指定分类的学习进度详情
- **URL**: `GET /api/student/course/category-progress/{userId}/{categoryId}`
- **功能**: 获取学生指定分类的学习进度详情

**请求示例**:
```bash
GET /api/student/course/category-progress/1/1
```

**返回示例**:
```json
{
  "code": 200,
  "message": "获取学生分类学习进度详情成功",
  "data": {
    "id": 1,
    "studentId": 1,
    "categoryId": 1,
    "gradeId": 1,
    "bindingStatus": 2,
    "overallProgress": 25.50,
    "totalKnowledgePoints": 10,
    "completedKnowledgePoints": 3,
    "inProgressKnowledgePoints": 2,
    "notStartedKnowledgePoints": 5,
    "totalStudyDuration": 120,
    "lastStudyTime": "2023-12-21 10:30:00",
    "startTime": "2023-12-20 09:00:00",
    "completeTime": null,
    "notes": null,
    "knowledgeCategory": {
      "id": 1,
      "categoryName": "数与代数",
      "categoryCode": "NUM_ALG",
      "gradeId": 1,
      "iconUrl": "/icons/category/numbers-algebra.png",
      "iconClass": "icon-category-numbers"
    },
    "learningProgressList": [
      {
        "id": 1,
        "userId": 1,
        "knowledgePointId": 1,
        "knowledgeCategoryId": 1,
        "progressStatus": 3,
        "completionPercentage": 100.00,
        "studyDuration": 60,
        "lastStudyTime": "2023-12-20 10:00:00"
      }
    ]
  }
}
```

### 2. 学生课程管理接口（增强）

#### 2.1 手动分配课程（增强）
- **URL**: `POST /api/student/course/assign`
- **功能**: 为现有学生手动分配课程，自动创建分类绑定关系

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

#### 2.2 获取学习进度统计（增强）
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

### 3. 学生注册接口（增强）

#### 3.1 学生注册（自动创建分类绑定）
- **URL**: `POST /user/register`
- **功能**: 学生注册时自动根据年级分配课程和创建分类绑定关系

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
6. 批量创建学生分类绑定关系
7. 生成初始测试题
8. 返回注册成功结果
```

### 2. 分类绑定关系创建流程
```
1. 根据学生年级获取所有分类
2. 为每个分类创建绑定关系
3. 初始化统计信息（总知识点数量等）
4. 设置绑定状态为"已绑定"
5. 记录创建时间
```

### 3. 学习进度统计更新流程
```
1. 获取该分类下的所有知识点
2. 查询学生的学习进度记录
3. 统计各状态的知识点数量
4. 计算整体学习进度百分比
5. 统计总学习时长和最后学习时间
6. 更新绑定关系的统计信息
```

## 数据表关系

### 表关系图
```
user (学生)
  ↓ (1:N)
student_category_binding (学生分类绑定关系)
  ↓ (N:1)
knowledge_category (知识点分类)
  ↓ (N:1)
grade (年级)

learning_progress (学习进度)
  ↓ (N:1)
knowledge_point (知识点)
  ↓ (N:1)
knowledge_category (知识点分类)
```

### 关键约束
- `student_category_binding.student_id` → `user.id`
- `student_category_binding.category_id` → `knowledge_category.id`
- `student_category_binding.grade_id` → `grade.id`
- `learning_progress.knowledge_point_id` → `knowledge_point.id`
- `learning_progress.knowledge_category_id` → `knowledge_category.id`

## 使用示例

### 1. 前端查询学生分类学习进度
```javascript
// 获取学生所有分类的学习进度
async function getStudentCategoryProgress(userId) {
  const response = await fetch(`/api/student/course/category-progress/${userId}`);
  const result = await response.json();
  
  if (result.code === 200) {
    return result.data;
  }
  throw new Error(result.message);
}

// 获取学生指定分类的学习进度
async function getStudentCategoryProgressDetail(userId, categoryId) {
  const response = await fetch(`/api/student/course/category-progress/${userId}/${categoryId}`);
  const result = await response.json();
  
  if (result.code === 200) {
    return result.data;
  }
  throw new Error(result.message);
}

// 使用示例
const categoryProgress = await getStudentCategoryProgress(1);
categoryProgress.forEach(category => {
  console.log(`分类: ${category.knowledgeCategory.categoryName}`);
  console.log(`整体进度: ${category.overallProgress}%`);
  console.log(`已完成: ${category.completedKnowledgePoints}/${category.totalKnowledgePoints}`);
  
  // 显示该分类下的知识点学习情况
  category.learningProgressList.forEach(progress => {
    console.log(`知识点进度: ${progress.completionPercentage}%`);
  });
});
```

### 2. 后端服务调用
```java
// 获取学生分类学习进度详情
List<StudentCategoryBinding> details = studentRegistrationService.getStudentCategoryProgressDetails(userId);

// 获取学生指定分类的学习进度详情
StudentCategoryBinding detail = studentRegistrationService.getStudentCategoryProgressDetail(userId, categoryId);

// 创建学生分类绑定关系
StudentCategoryBinding binding = studentCategoryBindingService.createStudentCategoryBinding(studentId, categoryId, gradeId);

// 更新学习统计信息
boolean success = studentCategoryBindingService.updateStudyStatistics(studentId, categoryId);
```

## 注意事项

1. **数据一致性**: 使用事务处理确保数据一致性
2. **外键约束**: 删除学生或分类时会自动处理相关记录
3. **索引优化**: 添加必要索引提高查询性能
4. **统计更新**: 学习进度变化时自动更新分类统计信息
5. **多语言支持**: 分类支持中法双语显示
6. **图标支持**: 分类支持图片和CSS图标两种方式
7. **性能考虑**: 大量数据时建议分批处理统计更新

## 相关文档

- **学习进度与分类关系**: `LEARNING_PROGRESS_CATEGORY_RELATION_API.md`
- **学生课程分配功能**: `STUDENT_COURSE_ASSIGNMENT_API.md`
- **知识点分类图标**: `KNOWLEDGE_CATEGORY_ICONS_README.md`
- **多语言支持**: `MULTILINGUAL_SUPPORT_README.md`
- **数据库架构**: `DATABASE_SCHEMA_README.md`
