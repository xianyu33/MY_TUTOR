# 根据知识类型和难度等级的测试接口文档

## 接口说明

本文档包含两个接口，支持根据知识类型（分类ID）和知识点难度等级进行测试生成和查询操作。

---

## 接口1：生成随机测试

### 基本信息

**接口地址**: `POST /api/student-test/generate-random`

**功能说明**: 根据知识类型和知识点难度等级为学生生成随机测试题

**请求方式**: `POST`

**Content-Type**: `application/json`

### 请求参数

#### 方式1：根据知识类型和难度等级生成（推荐）

```json
{
  "studentId": 123,
  "gradeId": 8,
  "categoryId": 1,
  "difficultyLevel": 2,
  "questionCount": 30
}
```

#### 方式2：使用知识点ID或分类ID列表（向后兼容）

```json
{
  "studentId": 123,
  "gradeId": 8,
  "knowledgePointIds": [1, 2, 3],
  "questionCount": 30,
  "equalDifficultyDistribution": true
}
```

### 参数说明

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| studentId | Integer | 是 | 学生ID |
| gradeId | Integer | 是 | 年级ID |
| categoryId | Integer | 否 | 知识类型ID（分类ID），与difficultyLevel一起使用（推荐方式） |
| difficultyLevel | Integer | 否 | 知识点难度等级（1-简单，2-中等，3-困难），与categoryId一起使用（推荐方式） |
| questionCount | Integer | 是 | 题目数量 |
| knowledgePointIds | List<Integer> | 否 | 知识点ID列表（旧方式，向后兼容） |
| categoryIds | List<Integer> | 否 | 知识点分类ID列表（旧方式，向后兼容） |
| equalDifficultyDistribution | Boolean | 否 | 是否均匀分配难度（默认false，仅旧方式有效） |

**注意**: 
- **推荐使用方式1**：同时提供 `categoryId` 和 `difficultyLevel`，系统会根据指定的知识类型和难度等级生成测试题
- 如果提供了 `categoryId` 和 `difficultyLevel`，系统优先使用新方式
- 如果未提供新方式参数，系统会使用旧方式（向后兼容）

### 难度等级说明

- `1` - 简单（Easy）
- `2` - 中等（Medium）
- `3` - 困难（Hard）

### 响应示例

#### 成功响应

```json
{
  "code": 200,
  "message": "生成随机测试成功",
  "data": {
    "id": 1001,
    "studentId": 123,
    "testId": 5001,
    "testName": "Assessment",
    "testNameFr": "Assessment",
    "startTime": "2023-12-21 10:30:00",
    "endTime": null,
    "timeLimit": 60,
    "totalQuestions": 30,
    "totalPoints": 30,
    "answeredQuestions": 0,
    "testStatus": 1,
    "createAt": "2023-12-21 10:30:00",
    "easyCount": 0,
    "mediumCount": 30,
    "hardCount": 0,
    "questions": [
      {
        "questionId": 501,
        "sortOrder": 1,
        "points": 1,
        "questionTitle": "Linear Function",
        "questionTitleFr": "Fonction linéaire",
        "questionContent": "In the linear function y = 2x + 3, the slope is",
        "questionContentFr": "Dans la fonction linéaire y = 2x + 3, la pente est",
        "options": "[\"A. 2\", \"B. 3\", \"C. -2\", \"D. 5\"]",
        "optionsFr": "[\"A. 2\", \"B. 3\", \"C. -2\", \"D. 5\"]",
        "correctAnswer": "A",
        "correctAnswerFr": "A",
        "answerExplanation": "The coefficient of x is the slope",
        "answerExplanationFr": "Le coefficient de x est la pente",
        "difficultyLevel": 2,
        "knowledgePointId": 15,
        "knowledgePointName": "Linear Functions",
        "knowledgePointNameFr": "Fonctions linéaires"
      }
    ]
  }
}
```

#### 失败响应

```json
{
  "code": 500,
  "message": "生成随机测试失败",
  "data": null
}
```

### 响应字段说明

#### 测试基本信息
- `id`: 测试记录ID
- `studentId`: 学生ID
- `testId`: 测试ID
- `testName`: 测试名称
- `testNameFr`: 测试名称（法语）
- `startTime`: 开始时间
- `endTime`: 结束时间（未完成时为null）
- `timeLimit`: 时间限制（分钟）
- `totalQuestions`: 总题目数
- `totalPoints`: 总分
- `answeredQuestions`: 已答题数
- `testStatus`: 测试状态（1-进行中，2-已完成）
- `createAt`: 创建时间

#### 难度统计
- `easyCount`: 简单题数量
- `mediumCount`: 中等题数量
- `hardCount`: 困难题数量

#### 题目详情
- `questionId`: 题目ID
- `sortOrder`: 排序序号
- `points`: 分值
- `questionTitle`: 题目标题
- `questionTitleFr`: 题目标题（法语）
- `questionContent`: 题目内容
- `questionContentFr`: 题目内容（法语）
- `options`: 选项（JSON字符串）
- `optionsFr`: 选项（法语，JSON字符串）
- `correctAnswer`: 正确答案
- `correctAnswerFr`: 正确答案（法语）
- `answerExplanation`: 答案解析
- `answerExplanationFr`: 答案解析（法语）
- `difficultyLevel`: 难度等级
- `knowledgePointId`: 知识点ID
- `knowledgePointName`: 知识点名称
- `knowledgePointNameFr`: 知识点名称（法语）

### 使用示例

#### cURL - 新方式（推荐）

```bash
curl -X POST http://localhost:8080/api/student-test/generate-random \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 123,
    "gradeId": 8,
    "categoryId": 1,
    "difficultyLevel": 2,
    "questionCount": 30
  }'
```

#### JavaScript/Fetch - 新方式

```javascript
fetch('http://localhost:8080/api/student-test/generate-random', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    studentId: 123,
    gradeId: 8,
    categoryId: 1,
    difficultyLevel: 2,
    questionCount: 30
  })
})
.then(response => response.json())
.then(data => {
  console.log('生成成功:', data);
})
.catch(error => {
  console.error('生成失败:', error);
});
```

### 错误处理

#### 参数验证失败

```json
{
  "code": 500,
  "message": "学生ID和年级ID不能为空",
  "data": null
}
```

```json
{
  "code": 500,
  "message": "题目数量必须大于0",
  "data": null
}
```

#### 没有符合条件的题目

```json
{
  "code": 500,
  "message": "生成随机测试失败",
  "data": null
}
```

**注意**: 当指定知识类型和难度等级下没有足够题目时，会返回失败。请确保数据库中存在符合条件的题目。

---

## 接口2：查询测试记录

### 基本信息

**接口地址**: `POST /api/student-test-record/student/{studentId}/knowledge-points`

**功能说明**: 根据知识类型和知识点难度等级查询学生的测试记录

**请求方式**: `POST`

**Content-Type**: `application/json`

### 请求参数

#### Path Parameters

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| studentId | Integer | 是 | 学生ID（路径参数） |

#### Request Body

```json
{
  "categoryId": 1,
  "difficultyLevel": 2
}
```

### 参数说明

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| categoryId | Integer | 是 | 知识类型ID（分类ID） |
| difficultyLevel | Integer | 是 | 知识点难度等级（1-简单，2-中等，3-困难） |

### 难度等级说明

- `1` - 简单（Easy）
- `2` - 中等（Medium）
- `3` - 困难（Hard）

### 响应示例

#### 成功响应

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1001,
      "studentId": 123,
      "testId": 5001,
      "testName": "Assessment",
      "testNameFr": "Assessment",
      "startTime": "2023-12-21 10:30:00",
      "endTime": "2023-12-21 11:30:00",
      "submitTime": "2023-12-21 11:30:00",
      "timeSpent": 60,
      "totalQuestions": 30,
      "answeredQuestions": 30,
      "correctAnswers": 25,
      "totalPoints": 30,
      "earnedPoints": 25,
      "scorePercentage": 83.33,
      "testStatus": 2,
      "createAt": "2023-12-21 10:30:00",
      "updateAt": "2023-12-21 11:30:00"
    },
    {
      "id": 1002,
      "studentId": 123,
      "testId": 5002,
      "testName": "Assessment",
      "testNameFr": "Assessment",
      "startTime": "2023-12-20 14:00:00",
      "endTime": "2023-12-20 15:00:00",
      "submitTime": "2023-12-20 15:00:00",
      "timeSpent": 60,
      "totalQuestions": 30,
      "answeredQuestions": 30,
      "correctAnswers": 28,
      "totalPoints": 30,
      "earnedPoints": 28,
      "scorePercentage": 93.33,
      "testStatus": 2,
      "createAt": "2023-12-20 14:00:00",
      "updateAt": "2023-12-20 15:00:00"
    }
  ]
}
```

#### 空结果响应

```json
{
  "code": 200,
  "message": "操作成功",
  "data": []
}
```

### 响应字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Integer | 测试记录ID |
| studentId | Integer | 学生ID |
| testId | Integer | 测试ID |
| testName | String | 测试名称 |
| testNameFr | String | 测试名称（法语） |
| startTime | String | 开始时间 |
| endTime | String | 结束时间（未完成时为null） |
| submitTime | String | 提交时间（未提交时为null） |
| timeSpent | Integer | 用时（分钟） |
| totalQuestions | Integer | 总题目数 |
| answeredQuestions | Integer | 已答题数 |
| correctAnswers | Integer | 正确答案数 |
| totalPoints | Integer | 总分 |
| earnedPoints | Integer | 获得分数 |
| scorePercentage | BigDecimal | 得分率（百分比） |
| testStatus | Integer | 测试状态（1-进行中，2-已完成） |
| createAt | String | 创建时间 |
| updateAt | String | 更新时间 |

### 使用示例

#### cURL

```bash
curl -X POST http://localhost:8080/api/student-test-record/student/123/knowledge-points \
  -H "Content-Type: application/json" \
  -d '{
    "categoryId": 1,
    "difficultyLevel": 2
  }'
```

#### JavaScript/Fetch

```javascript
fetch('http://localhost:8080/api/student-test-record/student/123/knowledge-points', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    categoryId: 1,
    difficultyLevel: 2
  })
})
.then(response => response.json())
.then(data => {
  console.log('查询成功:', data);
})
.catch(error => {
  console.error('查询失败:', error);
});
```

#### Vue.js

```javascript
async function queryTestRecords(studentId, categoryId, difficultyLevel) {
  try {
    const response = await fetch(
      `/api/student-test-record/student/${studentId}/knowledge-points`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          categoryId: categoryId,
          difficultyLevel: difficultyLevel
        })
      }
    );
    
    const result = await response.json();
    if (result.code === 200) {
      console.log('测试记录:', result.data);
      return result.data;
    } else {
      console.error('查询失败:', result.message);
      return [];
    }
  } catch (error) {
    console.error('请求异常:', error);
    return [];
  }
}
```

### 错误处理

#### 参数验证失败

```json
{
  "code": 500,
  "message": "请求参数不能为空",
  "data": null
}
```

```json
{
  "code": 500,
  "message": "知识类型ID和难度等级不能为空",
  "data": null
}
```

---

## 业务说明

### 知识类型（分类）说明

知识类型对应 `knowledge_category` 表中的分类，常见分类包括：
- `1` - 数与代数（Number and Algebra）
- `2` - 几何（Geometry）
- `3` - 统计与概率（Statistics and Probability）
- `4` - 综合应用（Comprehensive Application）

### 查询逻辑

1. **生成测试接口**：
   - 系统根据 `gradeId`、`categoryId` 和 `difficultyLevel` 查询符合条件的题目
   - 从题目池中随机选择指定数量的题目
   - 如果可用题目数量少于请求数量，会使用所有可用题目并记录警告日志

2. **查询测试记录接口**：
   - 系统查询该学生在指定知识类型和难度等级下的所有测试记录
   - 返回的测试记录中包含该知识类型和难度等级的题目
   - 结果按创建时间倒序排列（最新的在前）

### 注意事项

1. **题目数量限制**：
   - 生成测试时，如果指定知识类型和难度等级下的题目数量不足，系统会使用所有可用题目
   - 建议在生成测试前先确认该分类和难度等级下有足够的题目

2. **测试记录关联**：
   - 查询测试记录时，系统会通过题目关联到知识点，再通过知识点关联到分类
   - 只要测试中包含指定分类和难度等级的题目，该测试记录就会被返回

3. **多语言支持**：
   - 所有接口返回的数据都支持中法双语
   - 法语字段以 `Fr` 后缀标识（如 `testNameFr`、`questionTitleFr` 等）

---

## 完整示例

### 场景：学生完成一次测试并查询历史记录

#### 步骤1：生成测试

```bash
POST /api/student-test/generate-random
{
  "studentId": 123,
  "gradeId": 8,
  "categoryId": 1,
  "difficultyLevel": 2,
  "questionCount": 30
}
```

**响应**：
```json
{
  "code": 200,
  "message": "生成随机测试成功",
  "data": {
    "id": 1001,
    "testId": 5001,
    "totalQuestions": 30,
    "testStatus": 1
  }
}
```

#### 步骤2：提交答案（使用批量提交接口）

```bash
POST /api/student-test/batch-submit
{
  "testRecordId": 1001,
  "studentId": 123,
  "answers": [...]
}
```

#### 步骤3：查询该分类和难度等级的测试记录

```bash
POST /api/student-test-record/student/123/knowledge-points
{
  "categoryId": 1,
  "difficultyLevel": 2
}
```

**响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1001,
      "testId": 5001,
      "scorePercentage": 83.33,
      "testStatus": 2
    }
  ]
}
```

---

## 版本历史

- **v1.0** (2023-12-21): 初始版本，支持根据知识类型和难度等级生成测试和查询记录

