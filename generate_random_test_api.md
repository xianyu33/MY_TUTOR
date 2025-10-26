# 生成随机测试接口文档

## 接口说明

**接口地址**: `POST /api/student-test/generate-random`

**功能说明**: 为学生生成随机测试，支持难度均匀分配和知识点分类筛选

## 请求参数

使用 `@RequestBody` 接收 JSON 格式的请求参数：

```json
{
  "studentId": 123,
  "gradeId": 8,
  "categoryIds": [1, 2, 3],
  "questionCount": 30,
  "equalDifficultyDistribution": true
}
```

### 参数说明

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| studentId | Integer | 是 | 学生ID |
| gradeId | Integer | 是 | 年级ID |
| categoryIds | List<Integer> | 否 | 知识点分类ID列表（可选） |
| questionCount | Integer | 是 | 题目数量 |
| equalDifficultyDistribution | Boolean | 否 | 是否均匀分配难度（默认false） |

## 响应示例

### 成功响应

```json
{
  "code": 200,
  "message": "生成随机测试成功",
  "data": {
    "id": 1001,
    "studentId": 123,
    "testId": 5001,
    "testName": "随机测试_1703123456789",
    "startTime": "2023-12-21 10:30:00",
    "totalQuestions": 30,
    "totalPoints": 30,
    "testStatus": 1
  }
}
```

### 失败响应

```json
{
  "code": 500,
  "message": "生成随机测试失败",
  "data": null
}
```

## 难度分配说明

### equalDifficultyDistribution = true
题目按难度均匀分配：
- 简单（Level 1）: ⌈n/3⌉ 道题
- 中等（Level 2）: n/3 道题
- 困难（Level 3）: n - ⌈n/3⌉ - n/3 道题

示例：30道题
- 简单: 10道
- 中等: 10道
- 困难: 10道

### equalDifficultyDistribution = false
默认难度为中等（Level 2），随机选择30道题

## 使用示例

### cURL

```bash
curl -X POST http://localhost:8080/api/student-test/generate-random \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 123,
    "gradeId": 8,
    "categoryIds": [1, 2],
    "questionCount": 30,
    "equalDifficultyDistribution": true
  }'
```

### JavaScript/Fetch

```javascript
fetch('http://localhost:8080/api/student-test/generate-random', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    studentId: 123,
    gradeId: 8,
    categoryIds: [1, 2],
    questionCount: 30,
    equalDifficultyDistribution: true
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

### Vue.js

```javascript
async generateTest() {
  try {
    const response = await this.$http.post('/api/student-test/generate-random', {
      studentId: this.studentId,
      gradeId: this.gradeId,
      categoryIds: [1, 2, 3],
      questionCount: 30,
      equalDifficultyDistribution: true
    });
    
    if (response.data.code === 200) {
      this.$message.success('生成测试成功');
      this.testRecord = response.data.data;
    } else {
      this.$message.error(response.data.message);
    }
  } catch (error) {
    console.error('生成测试失败:', error);
    this.$message.error('生成测试失败');
  }
}
```

## 说明

1. **参数验证**: 学生ID、年级ID、题目数量为必填项
2. **难度分配**: `equalDifficultyDistribution=true` 时按1:1:1分配难度
3. **分类筛选**: `categoryIds` 可选，用于限制题目分类
4. **随机打乱**: 题目顺序会自动打乱

