# 根据知识点生成随机测试接口文档

## 接口说明

**接口地址**: `POST /api/student-test/generate-random`

**功能说明**: 为学生生成随机测试，支持根据知识点ID或知识点分类筛选题目，支持难度均匀分配，返回包含完整题目详情的测试数据

**版本**: v2.0（新增知识点ID筛选功能）

## 更新说明

### v2.0 新增功能
- ✅ 支持根据知识点ID列表（`knowledgePointIds`）精确筛选题目
- ✅ 保持向后兼容，原有的分类ID筛选（`categoryIds`）仍然支持
- ✅ 如果同时提供知识点ID和分类ID，优先使用知识点ID

### 筛选优先级
1. **知识点ID** (`knowledgePointIds`) - 优先使用，精确筛选
2. **分类ID** (`categoryIds`) - 当知识点ID为空时使用
3. **全年级** - 如果都不提供，从整个年级随机选择

## 请求参数

### 请求示例1：使用知识点ID（推荐）

```json
{
  "studentId": 123,
  "gradeId": 8,
  "knowledgePointIds": [1, 2, 3, 5],
  "questionCount": 30,
  "equalDifficultyDistribution": true
}
```

### 请求示例2：使用分类ID（向后兼容）

```json
{
  "studentId": 123,
  "gradeId": 8,
  "categoryIds": [2, 4],
  "questionCount": 30,
  "equalDifficultyDistribution": true
}
```

### 请求示例3：混合使用（知识点ID优先）

```json
{
  "studentId": 123,
  "gradeId": 8,
  "knowledgePointIds": [1, 2, 3],
  "categoryIds": [2, 4],
  "questionCount": 30,
  "equalDifficultyDistribution": true
}
```
**注意**: 此示例中会优先使用 `knowledgePointIds`，`categoryIds` 将被忽略

### 参数详细说明

| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| studentId | Integer | ✅ 是 | - | 学生ID |
| gradeId | Integer | ✅ 是 | - | 年级ID |
| knowledgePointIds | List<Integer> | ❌ 否 | null | 知识点ID列表（优先使用，推荐） |
| categoryIds | List<Integer> | ❌ 否 | null | 知识点分类ID列表（当knowledgePointIds为空时使用） |
| questionCount | Integer | ✅ 是 | - | 题目数量（必须大于0） |
| equalDifficultyDistribution | Boolean | ❌ 否 | false | 是否均匀分配难度（true=1:1:1，false=随机） |

### 参数验证规则

1. `studentId` 和 `gradeId` 不能为空
2. `questionCount` 必须大于0
3. `knowledgePointIds` 和 `categoryIds` 至少提供一个（如果都不提供，将从整个年级随机选择）
4. 如果同时提供，优先使用 `knowledgePointIds`

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
    "testName": "Assessment",
    "testNameFr": "Évaluation",
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
        "points": 1,
        
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
        "points": 1,
        
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
        
        "difficultyLevel": 2,
        
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

### 失败响应

#### 参数错误
```json
{
  "code": 500,
  "message": "学生ID和年级ID不能为空",
  "data": null
}
```

#### 题目数量错误
```json
{
  "code": 500,
  "message": "题目数量必须大于0",
  "data": null
}
```

#### 生成失败
```json
{
  "code": 500,
  "message": "生成随机测试失败",
  "data": null
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
- `endTime`: 结束时间（初始为null）
- `timeLimit`: 时间限制（分钟，默认60）
- `totalQuestions`: 总题目数
- `totalPoints`: 总分
- `answeredQuestions`: 已回答题目数（初始为0）
- `testStatus`: 测试状态（1=进行中，2=已完成）
- `createAt`: 创建时间

### 难度统计
- `easyCount`: 简单题数量（Level 1）
- `mediumCount`: 中等题数量（Level 2）
- `hardCount`: 困难题数量（Level 3）

### 题目详情列表 (questions)
每个题目包含以下信息：

#### 基本信息
- `questionId`: 题目ID
- `sortOrder`: 排序序号
- `points`: 分值（默认1分）

#### 题目内容（支持双语）
- `questionTitle`: 题目标题（英文）
- `questionTitleFr`: 题目标题（法语）
- `questionContent`: 题目内容（英文）
- `questionContentFr`: 题目内容（法语）
- `options`: 选项（JSON格式字符串，英文）
- `optionsFr`: 选项（JSON格式字符串，法语）

#### 答案信息（支持双语）
- `correctAnswer`: 正确答案（英文）
- `correctAnswerFr`: 正确答案（法语）
- `answerExplanation`: 答案解释（英文）
- `answerExplanationFr`: 答案解释（法语）

#### 难度和知识点
- `difficultyLevel`: 难度等级（1=简单，2=中等，3=困难）
- `knowledgePointId`: 知识点ID
- `knowledgePointName`: 知识点名称（英文）
- `knowledgePointNameFr`: 知识点名称（法语）

#### 学生答题信息（初始为null）
- `studentAnswer`: 学生答案
- `isCorrect`: 是否正确（0=错误，1=正确）
- `earnedPoints`: 获得的分数

## 难度分配说明

### equalDifficultyDistribution = true（均匀分配）

题目按难度均匀分配：
- **简单（Level 1）**: ⌈n/3⌉ 道题
- **中等（Level 2）**: n/3 道题
- **困难（Level 3）**: n - ⌈n/3⌉ - n/3 道题

**示例：30道题**
- 简单: 10道（⌈30/3⌉ = 10）
- 中等: 10道（30/3 = 10）
- 困难: 10道（30 - 10 - 10 = 10）

**示例：31道题**
- 简单: 11道（⌈31/3⌉ = 11）
- 中等: 10道（31/3 = 10）
- 困难: 10道（31 - 11 - 10 = 10）

### equalDifficultyDistribution = false（随机分配）

不限制难度，从符合条件的题目池中随机选择指定数量的题目。

## 使用示例

### cURL - 使用知识点ID

```bash
curl -X POST http://localhost:8080/api/student-test/generate-random \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 123,
    "gradeId": 8,
    "knowledgePointIds": [1, 2, 3, 5],
    "questionCount": 30,
    "equalDifficultyDistribution": true
  }'
```

### JavaScript/Fetch - 使用知识点ID

```javascript
async function generateTestByKnowledgePoints() {
  try {
    const response = await fetch('http://localhost:8080/api/student-test/generate-random', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        studentId: 123,
        gradeId: 8,
        knowledgePointIds: [1, 2, 3, 5],  // 使用知识点ID
        questionCount: 30,
        equalDifficultyDistribution: true
      })
    });

    const data = await response.json();
    
    if (data.code === 200) {
      console.log('测试ID:', data.data.id);
      console.log('题目数量:', data.data.totalQuestions);
      console.log('难度分布:', {
        简单: data.data.easyCount,
        中等: data.data.mediumCount,
        困难: data.data.hardCount
      });
      
      // 遍历题目列表
      data.data.questions.forEach((question, index) => {
        console.log(`第 ${index + 1} 题:`, question.questionContent);
        console.log('知识点:', question.knowledgePointName);
        console.log('难度:', question.difficultyLevel === 1 ? '简单' : 
                    question.difficultyLevel === 2 ? '中等' : '困难');
      });
    } else {
      console.error('生成失败:', data.message);
    }
  } catch (error) {
    console.error('请求失败:', error);
  }
}
```

### Vue.js - 使用知识点ID

```vue
<template>
  <div>
    <el-form :model="form" label-width="120px">
      <el-form-item label="学生ID">
        <el-input v-model="form.studentId" type="number"></el-input>
      </el-form-item>
      
      <el-form-item label="年级ID">
        <el-input v-model="form.gradeId" type="number"></el-input>
      </el-form-item>
      
      <el-form-item label="知识点">
        <el-select 
          v-model="form.knowledgePointIds" 
          multiple 
          placeholder="请选择知识点"
          style="width: 100%"
        >
          <el-option
            v-for="kp in knowledgePoints"
            :key="kp.id"
            :label="kp.name"
            :value="kp.id"
          ></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="题目数量">
        <el-input-number v-model="form.questionCount" :min="1" :max="100"></el-input-number>
      </el-form-item>
      
      <el-form-item label="均匀分配难度">
        <el-switch v-model="form.equalDifficultyDistribution"></el-switch>
      </el-form-item>
      
      <el-form-item>
        <el-button type="primary" @click="generateTest" :loading="loading">
          生成测试
        </el-button>
      </el-form-item>
    </el-form>
    
    <div v-if="testDetails">
      <h3>{{ testDetails.testName }}</h3>
      <p>总题目: {{ testDetails.totalQuestions }}</p>
      <p>难度分布: 简单 {{ testDetails.easyCount }}, 
                 中等 {{ testDetails.mediumCount }}, 
                 困难 {{ testDetails.hardCount }}</p>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {
        studentId: null,
        gradeId: 8,
        knowledgePointIds: [],
        questionCount: 30,
        equalDifficultyDistribution: true
      },
      knowledgePoints: [
        { id: 1, name: 'Triangles' },
        { id: 2, name: 'Quadrilaterals' },
        { id: 3, name: 'Circles' },
        { id: 5, name: 'Linear Functions' }
      ],
      testDetails: null,
      loading: false
    }
  },
  methods: {
    async generateTest() {
      if (!this.form.studentId || !this.form.gradeId) {
        this.$message.warning('请填写学生ID和年级ID');
        return;
      }
      
      if (!this.form.knowledgePointIds || this.form.knowledgePointIds.length === 0) {
        this.$message.warning('请至少选择一个知识点');
        return;
      }
      
      this.loading = true;
      try {
        const response = await this.$http.post('/api/student-test/generate-random', {
          studentId: this.form.studentId,
          gradeId: this.form.gradeId,
          knowledgePointIds: this.form.knowledgePointIds,  // 使用知识点ID
          questionCount: this.form.questionCount,
          equalDifficultyDistribution: this.form.equalDifficultyDistribution
        });
        
        if (response.data.code === 200) {
          this.testDetails = response.data.data;
          this.$message.success('生成测试成功');
        } else {
          this.$message.error(response.data.message);
        }
      } catch (error) {
        console.error('生成测试失败:', error);
        this.$message.error('生成测试失败');
      } finally {
        this.loading = false;
      }
    }
  }
}
</script>
```

### Axios - 使用知识点ID

```javascript
import axios from 'axios';

const generateTest = async () => {
  try {
    const response = await axios.post('/api/student-test/generate-random', {
      studentId: 123,
      gradeId: 8,
      knowledgePointIds: [1, 2, 3, 5],  // 使用知识点ID
      questionCount: 30,
      equalDifficultyDistribution: true
    });
    
    if (response.data.code === 200) {
      const testData = response.data.data;
      console.log('测试生成成功:', testData);
      return testData;
    } else {
      throw new Error(response.data.message);
    }
  } catch (error) {
    console.error('生成测试失败:', error);
    throw error;
  }
};
```

## 最佳实践

### 1. 优先使用知识点ID
```javascript
// ✅ 推荐：使用知识点ID，精确筛选
{
  knowledgePointIds: [1, 2, 3]
}

// ⚠️ 不推荐：使用分类ID，范围较广
{
  categoryIds: [2, 4]
}
```

### 2. 合理设置题目数量
- 建议题目数量：10-50道
- 如果使用均匀难度分配，建议题目数量为3的倍数

### 3. 难度分配建议
- **练习模式**: `equalDifficultyDistribution: false`，随机难度
- **测试模式**: `equalDifficultyDistribution: true`，均匀分配

### 4. 知识点选择建议
- 选择2-5个相关知识点，确保有足够的题目
- 避免选择过多知识点，可能导致题目分布不均

## 注意事项

1. **选项解析**: 题目选项是JSON格式字符串，需要使用 `JSON.parse()` 解析
2. **初始状态**: 所有题目的 `studentAnswer`、`isCorrect`、`earnedPoints` 初始为 `null`
3. **测试状态**: `testStatus` 初始为 `1`（进行中），完成测试后变为 `2`（已完成）
4. **筛选优先级**: 如果同时提供知识点ID和分类ID，优先使用知识点ID
5. **题目不足**: 如果指定知识点/分类的题目数量不足，将返回实际可用的题目数量
6. **随机打乱**: 题目顺序会自动打乱，每次生成的顺序可能不同

## 错误处理

### 常见错误及解决方案

| 错误信息 | 原因 | 解决方案 |
|---------|------|---------|
| 学生ID和年级ID不能为空 | 缺少必填参数 | 检查请求参数，确保提供studentId和gradeId |
| 题目数量必须大于0 | questionCount无效 | 确保questionCount是大于0的整数 |
| 生成随机测试失败 | 没有找到符合条件的题目 | 检查知识点ID或分类ID是否正确，或增加题目数量 |
| 没有找到符合条件的简单题/中等题/困难题 | 指定难度的题目不足 | 减少该难度的题目数量，或使用 `equalDifficultyDistribution: false` |

## 版本历史

### v2.0 (2024-01-XX)
- ✅ 新增知识点ID筛选功能
- ✅ 保持向后兼容，支持分类ID筛选
- ✅ 优化筛选逻辑，支持优先级选择

### v1.0 (2023-12-XX)
- ✅ 初始版本，支持分类ID筛选
- ✅ 支持难度均匀分配
- ✅ 返回完整题目详情

## 相关接口

- `POST /api/student-test/batch-submit` - 批量提交答案并生成分析报告
- `GET /api/student-test/answer-details/{testRecordId}` - 查询测试答题详情
- `POST /api/student-test/complete` - 完成测试

