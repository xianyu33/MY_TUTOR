# 增强版生成随机测试接口文档

## 接口说明

**接口地址**: `POST /api/student-test/generate-random`

**功能说明**: 为学生生成随机测试，返回包含题目详情的完整测试数据

## 请求参数

```json
{
  "studentId": 123,
  "gradeId": 8,
  "categoryIds": [2, 4],
  "questionCount": 30,
  "equalDifficultyDistribution": true
}
```

## 响应示例

```json
{
  "code": 200,
  "message": "生成随机测试成功",
  "data": {
    "id": 1001,
    "studentId": 123,
    "testId": 5001,
    "testName": "随机测试_1703123456789",
    "testNameFr": "Test aléatoire_1703123456789",
    "startTime": "2023-12-21 10:30:00",
    "endTime": null,
    "timeLimit": 60,
    "totalQuestions": 30,
    "totalPoints": 30,
    "answeredQuestions": 0,
    "testStatus": 1,
    "createAt": "2023-12-21 10:30:00",
    
    "easyCount": 10,
    "mediumCount": 10,
    "hardCount": 10,
    
    "questions": [
      {
        "questionId": 501,
        "sortOrder": 1,
        "points": 2,
        
        "questionTitle": "Triangle Classification",
        "questionTitleFr": "Classification des triangles",
        "questionContent": "What type of triangle has all sides equal?",
        "questionContentFr": "Quel type de triangle a tous les côtés égaux ?",
        
        "options": "[\"A. Isosceles\", \"B. Scalene\", \"C. Equilateral\", \"D. Right\"]",
        "optionsFr": "[\"A. Isocèle\", \"B. Scalène\", \"C. Équilatéral\", \"D. Rectangle\"]",
        
        "correctAnswer": "C",
        "correctAnswerFr": "C",
        "answerExplanation": "An equilateral triangle has all three sides equal",
        "answerExplanationFr": "Un triangle équilatéral a les trois côtés égaux",
        
        "difficultyLevel": 1,
        
        "knowledgePointId": 1,
        "knowledgePointName": "Triangles",
        "knowledgePointNameFr": "Triangles",
        
        "studentAnswer": null,
        "isCorrect": null,
        "earnedPoints": null
      },
      {
        "questionId": 502,
        "sortOrder": 2,
        "points": 2,
        
        "questionTitle": "Linear Function",
        "questionTitleFr": "Fonction linéaire",
        "questionContent": "In the linear function y = 2x + 3, the slope is",
        "questionContentFr": "Dans la fonction linéaire y = 2x + 3, la pente est",
        
        "options": "[\"A. 2\", \"B. 3\", \"C. -2\", \"D. 5\"]",
        "optionsFr": "[\"A. 2\", \"B. 3\", \"C. -2\", \"D. 5\"]",
        
        "correctAnswer": "A",
        "correctAnswerFr": "A",
        "answerExplanation": "The coefficient of x is the slope, so slope = 2",
        "answerExplanationFr": "Le coefficient de x est la pente, donc pente = 2",
        
        "difficultyLevel": 3,
        
        "knowledgePointId": 15,
        "knowledgePointName": "Linear Functions",
        "knowledgePointNameFr": "Fonctions linéaires",
        
        "studentAnswer": null,
        "isCorrect": null,
        "earnedPoints": null
      }
    ]
  }
}
```

## 响应字段说明

### 测试基本信息
- `id`: 测试记录ID
- `studentId`: 学生ID
- `testId`: 测试ID
- `testName`: 测试名称（英文）
- `testNameFr`: 测试名称（法语）
- `startTime`: 开始时间
- `endTime`: 结束时间
- `timeLimit`: 时间限制（分钟）
- `totalQuestions`: 总题目数
- `totalPoints`: 总分
- `answeredQuestions`: 已回答题目数
- `testStatus`: 测试状态（1-进行中，2-已完成）
- `createAt`: 创建时间

### 难度统计
- `easyCount`: 简单题数量
- `mediumCount`: 中等题数量
- `hardCount`: 困难题数量

### 题目详情列表 (questions)
每个题目包含：

**基本信息**
- `questionId`: 题目ID
- `sortOrder`: 排序序号
- `points`: 分值

**题目内容**（支持英文和法语）
- `questionTitle`: 题目标题（英文）
- `questionTitleFr`: 题目标题（法语）
- `questionContent`: 题目内容（英文）
- `questionContentFr`: 题目内容（法语）
- `options`: 选项（JSON，英文）
- `optionsFr`: 选项（JSON，法语）

**答案信息**
- `correctAnswer`: 正确答案（英文）
- `correctAnswerFr`: 正确答案（法语）
- `answerExplanation`: 答案解释（英文）
- `answerExplanationFr`: 答案解释（法语）

**难度和知识点**
- `difficultyLevel`: 难度等级（1-简单，2-中等，3-困难）
- `knowledgePointId`: 知识点ID
- `knowledgePointName`: 知识点名称（英文）
- `knowledgePointNameFr`: 知识点名称（法语）

**学生答题信息**（初始为null）
- `studentAnswer`: 学生答案
- `isCorrect`: 是否正确
- `earnedPoints`: 获得的分数

## 使用示例

### JavaScript/Fetch
```javascript
const response = await fetch('http://localhost:8080/api/student-test/generate-random', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    studentId: 123,
    gradeId: 8,
    categoryIds: [2, 4],
    questionCount: 30,
    equalDifficultyDistribution: true
  })
});

const data = await response.json();

console.log('测试ID:', data.data.id);
console.log('题目数量:', data.data.totalQuestions);

// 遍历题目列表
data.data.questions.forEach((question, index) => {
  console.log(`第 ${index + 1} 题:`, question.questionContent);
  console.log('难度:', question.difficultyLevel === 1 ? '简单' : question.difficultyLevel === 2 ? '中等' : '困难');
});
```

### Vue.js
```vue
<script>
export default {
  data() {
    return {
      testDetails: null,
      loading: false
    }
  },
  methods: {
    async generateTest() {
      this.loading = true;
      try {
        const response = await this.$http.post('/api/student-test/generate-random', {
          studentId: this.studentId,
          gradeId: 8,
          categoryIds: [2, 4],
          questionCount: 30,
          equalDifficultyDistribution: true
        });
        
        if (response.data.code === 200) {
          this.testDetails = response.data.data;
          this.$message.success('生成测试成功');
        }
      } catch (error) {
        this.$message.error('生成测试失败');
      } finally {
        this.loading = false;
      }
    }
  }
}
</script>

<template>
  <div v-if="testDetails">
    <h3>{{ testDetails.testName }}</h3>
    <p>总题目: {{ testDetails.totalQuestions }}</p>
    <p>简单: {{ testDetails.easyCount }}, 中等: {{ testDetails.mediumCount }}, 困难: {{ testDetails.hardCount }}</p>
    
    <div v-for="(q, index) in testDetails.questions" :key="q.questionId">
      <h4>第 {{ index + 1 }} 题 ({{ q.points }}分)</h4>
      <p>{{ q.questionContent }}</p>
      <el-radio-group v-model="answers[q.questionId]">
        <el-radio 
          v-for="option in JSON.parse(q.options)" 
          :key="option"
          :label="option"
        >
          {{ option }}
        </el-radio>
      </el-radio-group>
    </div>
  </div>
</template>
```

## 优势

1. **完整的题目详情**: 返回所有题目的完整信息，包括内容、选项、答案、知识点
2. **多语言支持**: 所有内容都包含英文和法语版本
3. **难度统计**: 自动统计简单、中等、困难题目的数量
4. **答题状态**: 可以追踪学生答题状态
5. **知识点关联**: 每个题目关联对应的知识点信息

## 注意事项

1. 题目选项是JSON格式，需要解析后才能显示
2. 初始状态所有题目的 `studentAnswer`、`isCorrect`、`earnedPoints` 都为 `null`
3. `testStatus` 初始为 `1`（进行中）
4. 难度分布只在 `equalDifficultyDistribution=true` 时才会均匀分配

## 错误处理

### 参数错误
```json
{
  "code": 500,
  "message": "学生ID和年级ID不能为空",
  "data": null
}
```

### 生成失败
```json
{
  "code": 500,
  "message": "生成随机测试失败",
  "data": null
}
```
