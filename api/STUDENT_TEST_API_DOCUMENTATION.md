# 学生测试功能API文档

## 功能概述

学生测试功能提供了完整的测试流程，包括：
- 随机题目生成
- 学生测试记录绑定
- 答题记录管理
- 历史记录查询
- 统计报表生成
- **多语言支持（中法双语）**

## 多语言支持

### 支持的语言
- `zh` - 中文（默认）
- `fr` - 法语

### 多语言字段
测试相关表支持多语言字段：
- **test表**: `test_name_fr` - 测试名称（法语）
- **student_test_record表**: `test_name_fr` - 测试名称（法语，冗余）
- **student_test_answer表**: 
  - `question_content_fr` - 题目内容（法语，冗余）
  - `correct_answer_fr` - 正确答案（法语，冗余）
  - `student_answer_fr` - 学生答案（法语）

### 语言参数使用
所有API接口都支持通过 `language` 参数指定语言：
- 默认语言：中文 (`zh`)
- 无效语言代码会自动回退到默认语言
- 法语内容为空时自动回退到中文内容

## API接口

### 1. 生成随机测试

**接口地址：** `POST /api/student-test/generate-random`

**请求参数：**
- `studentId` (必填): 学生ID
- `gradeId` (必填): 年级ID
- `difficultyLevel` (可选): 难度等级，默认2（中等）
- `questionCount` (可选): 题目数量，默认10
- `language` (可选): 语言代码，默认 `zh`

**请求示例：**
```
POST /api/student-test/generate-random
Content-Type: application/x-www-form-urlencoded

studentId=1&gradeId=9&difficultyLevel=2&questionCount=15&language=fr
```

**返回示例：**
```json
{
  "code": 200,
  "message": "生成随机测试成功",
  "data": {
    "id": 1,
    "studentId": 1,
    "testId": 1,
    "testName": "Test aléatoire_1703123456789",
    "testNameFr": "Test aléatoire_1703123456789",
    "startTime": "2023-12-21 10:30:00",
    "totalQuestions": 15,
    "totalPoints": 15,
    "testStatus": 1
  }
}
```

### 2. 开始测试

**接口地址：** `POST /api/student-test/start`

**请求参数：**
- `studentId` (必填): 学生ID
- `testId` (必填): 测试ID
- `language` (可选): 语言代码，默认 `zh`

**请求示例：**
```
POST /api/student-test/start
Content-Type: application/x-www-form-urlencoded

studentId=1&testId=1&language=fr
```

**返回示例：**
```json
{
  "code": 200,
  "message": "开始测试成功",
  "data": {
    "id": 1,
    "studentId": 1,
    "testId": 1,
    "testName": "Test de mathématiques",
    "testNameFr": "Test de mathématiques",
    "startTime": "2023-12-21 10:30:00",
    "testStatus": 1
  }
}
```

### 3. 提交答案

**接口地址：** `POST /api/student-test/submit-answer`

**请求参数：**
- `testRecordId` (必填): 测试记录ID
- `questionId` (必填): 题目ID
- `studentAnswer` (必填): 学生答案
- `language` (可选): 语言代码，默认 `zh`

**请求示例：**
```
POST /api/student-test/submit-answer
Content-Type: application/x-www-form-urlencoded

testRecordId=1&questionId=1&studentAnswer=A&language=fr
```

**返回示例：**
```json
{
  "code": 200,
  "message": "提交答案成功",
  "data": {
    "id": 1,
    "testRecordId": 1,
    "questionId": 1,
    "questionContent": "Quel est le plus grand nombre ?",
    "questionContentFr": "Quel est le plus grand nombre ?",
    "correctAnswer": "D",
    "correctAnswerFr": "D",
    "studentAnswer": "A",
    "studentAnswerFr": "A",
    "isCorrect": 0,
    "points": 1,
    "earnedPoints": 0,
    "answerTime": "2023-12-21 10:35:00"
  }
}
```

### 4. 完成测试

**接口地址：** `POST /api/student-test/complete`

**请求参数：**
- `testRecordId` (必填): 测试记录ID

**请求示例：**
```
POST /api/student-test/complete
Content-Type: application/x-www-form-urlencoded

testRecordId=1
```

**返回示例：**
```json
{
  "code": 200,
  "message": "完成测试成功",
  "data": {
    "id": 1,
    "studentId": 1,
    "testId": 1,
    "endTime": "2023-12-21 11:00:00",
    "submitTime": "2023-12-21 11:00:00",
    "timeSpent": 30,
    "answeredQuestions": 15,
    "correctAnswers": 12,
    "earnedPoints": 12,
    "scorePercentage": 80.00,
    "testStatus": 2
  }
}
```

### 5. 查询测试历史记录

**接口地址：** `GET /api/student-test/history/{studentId}`

**请求参数：**
- `studentId` (路径参数): 学生ID
- `page` (查询参数): 页码，默认1
- `size` (查询参数): 每页大小，默认10

**请求示例：**
```
GET /api/student-test/history/1?page=1&size=10
```

**返回示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "studentId": 1,
      "testId": 1,
      "testName": "数学测试",
      "startTime": "2023-12-21 10:30:00",
      "endTime": "2023-12-21 11:00:00",
      "timeSpent": 30,
      "scorePercentage": 80.00,
      "testStatus": 2
    }
  ]
}
```

### 6. 查询答题详情

**接口地址：** `GET /api/student-test/answer-details/{testRecordId}`

**请求参数：**
- `testRecordId` (路径参数): 测试记录ID

**请求示例：**
```
GET /api/student-test/answer-details/1
```

**返回示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "testRecordId": 1,
      "questionId": 1,
      "questionContent": "1+1=?",
      "correctAnswer": "2",
      "studentAnswer": "2",
      "isCorrect": 1,
      "earnedPoints": 1,
      "answerTime": "2023-12-21 10:35:00"
    }
  ]
}
```

### 7. 获取测试统计报表

**接口地址：** `GET /api/student-test/statistics/{studentId}`

**请求参数：**
- `studentId` (路径参数): 学生ID
- `gradeId` (查询参数): 年级ID（可选）

**请求示例：**
```
GET /api/student-test/statistics/1?gradeId=9
```

**返回示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalTests": 10,
    "completedTests": 8,
    "totalQuestions": 150,
    "answeredQuestions": 120,
    "correctAnswers": 96,
    "totalPoints": 150,
    "earnedPoints": 96,
    "averageScore": 80.00,
    "accuracyRate": 80.00
  }
}
```

### 8. 获取正在进行的测试

**接口地址：** `GET /api/student-test/ongoing/{studentId}`

**请求参数：**
- `studentId` (路径参数): 学生ID

**请求示例：**
```
GET /api/student-test/ongoing/1
```

**返回示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 2,
      "studentId": 1,
      "testId": 2,
      "testName": "数学测试2",
      "startTime": "2023-12-21 14:00:00",
      "testStatus": 1
    }
  ]
}
```

## 测试流程说明

### 1. 随机测试生成流程
1. 调用 `/generate-random` 接口生成随机测试
2. 系统根据年级和难度随机选择题目
3. 创建测试记录，状态为"进行中"
4. 返回测试记录ID供后续使用

### 2. 答题流程
1. 调用 `/start` 接口开始测试（如果使用已有测试）
2. 调用 `/submit-answer` 接口逐题提交答案
3. 调用 `/complete` 接口完成测试
4. 系统自动计算得分和统计信息

### 3. 历史记录查询
1. 调用 `/history` 接口查看测试历史
2. 调用 `/answer-details` 接口查看具体答题情况
3. 调用 `/statistics` 接口查看统计报表

## 数据表说明

### 主要表结构
- `test`: 测试表，存储测试基本信息
- `test_question`: 测试题目关联表，存储测试与题目的关系
- `student_test_record`: 学生测试记录表，存储学生的测试记录
- `student_test_answer`: 学生答题详情表，存储具体的答题情况

### 测试状态说明
- `1`: 进行中
- `2`: 已完成
- `3`: 已超时

### 难度等级说明
- `1`: 简单
- `2`: 中等
- `3`: 困难

## 多语言使用说明

### 1. 语言参数
所有API接口都支持 `language` 参数：
- `zh` - 中文（默认）
- `fr` - 法语

### 2. 多语言字段说明
- **测试名称**: `testName` (中文) / `testNameFr` (法语)
- **题目内容**: `questionContent` (中文) / `questionContentFr` (法语)
- **正确答案**: `correctAnswer` (中文) / `correctAnswerFr` (法语)
- **学生答案**: `studentAnswer` (中文) / `studentAnswerFr` (法语)

### 3. 回退机制
- 如果法语内容为空，自动回退到中文内容
- 无效语言代码自动回退到默认语言（中文）
- 确保系统在任何情况下都能正常显示内容

### 4. 使用示例
```bash
# 中文测试
POST /api/student-test/generate-random?studentId=1&gradeId=9&language=zh

# 法语测试
POST /api/student-test/generate-random?studentId=1&gradeId=9&language=fr
```

## 注意事项

1. **多语言支持**: 所有接口都支持中法双语，通过 `language` 参数控制
2. **数据一致性**: 确保中法双语内容在语义上保持一致
3. **性能考虑**: 多语言字段会增加存储空间，建议合理使用索引
4. **测试记录**: 测试记录会保存当前语言的内容，便于历史查询
5. **答案记录**: 学生答案会同时保存中法两种语言版本
6. **统计报表**: 统计功能支持多语言显示
7. **权限验证**: 建议在生产环境中添加适当的权限验证
8. **参数校验**: 建议添加完整的参数校验和错误处理
9. **缓存优化**: 可以考虑为多语言内容添加缓存机制
10. **扩展性**: 可以轻松添加其他语言支持（如英语、西班牙语等）

## 相关文档

- **数学知识点API**: 详见 `MATH_API_DOCUMENTATION.md`
- **多语言支持**: 详见 `MULTILINGUAL_SUPPORT_README.md`
- **知识点图标**: 详见 `KNOWLEDGE_POINT_ICONS_README.md`
- **数据库架构**: 详见 `DATABASE_SCHEMA_README.md`
