# 根据知识点和学生ID查询测试记录列表接口文档

## 接口说明

**接口地址**: `POST /api/student-test-record/student/{studentId}/knowledge-points`

**功能说明**: 根据学生ID和知识点ID列表查询包含指定知识点的测试记录列表

**版本**: v1.0

## 请求参数

### 路径参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| studentId | Integer | ✅ 是 | 学生ID |

### 请求体参数

使用 `@RequestBody` 接收 JSON 格式的请求参数：

```json
[1, 2, 3, 5]
```

**说明**: 知识点ID列表（数组格式）

## 请求示例

### cURL

```bash
curl -X POST http://localhost:8080/api/student-test-record/student/123/knowledge-points \
  -H "Content-Type: application/json" \
  -d '[1, 2, 3, 5]'
```

### JavaScript/Fetch

```javascript
async function queryTestRecordsByKnowledgePoints() {
  try {
    const response = await fetch('http://localhost:8080/api/student-test-record/student/123/knowledge-points', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify([1, 2, 3, 5])  // 知识点ID列表
    });

    const data = await response.json();
    
    if (data.code === 200) {
      console.log('查询成功，找到', data.data.length, '条测试记录');
      data.data.forEach((record, index) => {
        console.log(`记录 ${index + 1}:`, {
          id: record.id,
          testName: record.testName,
          totalQuestions: record.totalQuestions,
          earnedPoints: record.earnedPoints,
          scorePercentage: record.scorePercentage,
          testStatus: record.testStatus,
          createAt: record.createAt
        });
      });
    } else {
      console.error('查询失败:', data.message);
    }
  } catch (error) {
    console.error('请求失败:', error);
  }
}
```

### Axios

```javascript
import axios from 'axios';

const queryTestRecords = async (studentId, knowledgePointIds) => {
  try {
    const response = await axios.post(
      `/api/student-test-record/student/${studentId}/knowledge-points`,
      knowledgePointIds
    );
    
    if (response.data.code === 200) {
      return response.data.data;
    } else {
      throw new Error(response.data.message);
    }
  } catch (error) {
    console.error('查询失败:', error);
    throw error;
  }
};

// 使用示例
const records = await queryTestRecords(123, [1, 2, 3, 5]);
```

### Vue.js

```vue
<template>
  <div>
    <el-form :model="form" label-width="120px">
      <el-form-item label="学生ID">
        <el-input v-model="form.studentId" type="number"></el-input>
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
      
      <el-form-item>
        <el-button type="primary" @click="queryRecords" :loading="loading">
          查询测试记录
        </el-button>
      </el-form-item>
    </el-form>
    
    <el-table v-if="records.length > 0" :data="records" style="width: 100%">
      <el-table-column prop="id" label="记录ID" width="100"></el-table-column>
      <el-table-column prop="testName" label="测试名称"></el-table-column>
      <el-table-column prop="totalQuestions" label="总题目数" width="100"></el-table-column>
      <el-table-column prop="earnedPoints" label="得分" width="100"></el-table-column>
      <el-table-column prop="scorePercentage" label="得分率" width="100">
        <template slot-scope="scope">
          {{ scope.row.scorePercentage }}%
        </template>
      </el-table-column>
      <el-table-column prop="testStatus" label="状态" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.testStatus === 1" type="warning">进行中</el-tag>
          <el-tag v-else-if="scope.row.testStatus === 2" type="success">已完成</el-tag>
          <el-tag v-else type="info">已超时</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createAt" label="创建时间" width="180"></el-table-column>
    </el-table>
    
    <el-empty v-else description="暂无测试记录"></el-empty>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {
        studentId: null,
        knowledgePointIds: []
      },
      knowledgePoints: [
        { id: 1, name: 'Triangles' },
        { id: 2, name: 'Quadrilaterals' },
        { id: 3, name: 'Circles' },
        { id: 5, name: 'Linear Functions' }
      ],
      records: [],
      loading: false
    }
  },
  methods: {
    async queryRecords() {
      if (!this.form.studentId) {
        this.$message.warning('请填写学生ID');
        return;
      }
      
      if (!this.form.knowledgePointIds || this.form.knowledgePointIds.length === 0) {
        this.$message.warning('请至少选择一个知识点');
        return;
      }
      
      this.loading = true;
      try {
        const response = await this.$http.post(
          `/api/student-test-record/student/${this.form.studentId}/knowledge-points`,
          this.form.knowledgePointIds
        );
        
        if (response.data.code === 200) {
          this.records = response.data.data;
          this.$message.success(`查询成功，找到 ${this.records.length} 条测试记录`);
        } else {
          this.$message.error(response.data.message);
        }
      } catch (error) {
        console.error('查询失败:', error);
        this.$message.error('查询失败');
      } finally {
        this.loading = false;
      }
    }
  }
}
</script>
```

## 响应示例

### 成功响应

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1001,
      "studentId": 123,
      "testId": 5001,
      "testName": "Assessment",
      "testNameFr": "Évaluation",
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
      "createBy": null,
      "updateAt": "2023-12-21 11:30:00",
      "updateBy": null,
      "deleteFlag": "N"
    },
    {
      "id": 1002,
      "studentId": 123,
      "testId": 5002,
      "testName": "Assessment",
      "testNameFr": "Évaluation",
      "startTime": "2023-12-20 14:00:00",
      "endTime": "2023-12-20 15:00:00",
      "submitTime": "2023-12-20 15:00:00",
      "timeSpent": 60,
      "totalQuestions": 25,
      "answeredQuestions": 25,
      "correctAnswers": 20,
      "totalPoints": 25,
      "earnedPoints": 20,
      "scorePercentage": 80.00,
      "testStatus": 2,
      "createAt": "2023-12-20 14:00:00",
      "createBy": null,
      "updateAt": "2023-12-20 15:00:00",
      "updateBy": null,
      "deleteFlag": "N"
    }
  ]
}
```

### 失败响应

#### 知识点ID列表为空

```json
{
  "code": 500,
  "message": "知识点ID列表不能为空",
  "data": null
}
```

#### 未找到记录

```json
{
  "code": 200,
  "message": "success",
  "data": []
}
```

## 响应字段说明

### 测试记录字段

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Integer | 测试记录ID |
| studentId | Integer | 学生ID |
| testId | Integer | 测试ID |
| testName | String | 测试名称（英文） |
| testNameFr | String | 测试名称（法语） |
| startTime | String | 开始时间（格式：yyyy-MM-dd HH:mm:ss） |
| endTime | String | 结束时间（格式：yyyy-MM-dd HH:mm:ss，可能为null） |
| submitTime | String | 提交时间（格式：yyyy-MM-dd HH:mm:ss，可能为null） |
| timeSpent | Integer | 用时（分钟） |
| totalQuestions | Integer | 总题目数 |
| answeredQuestions | Integer | 已回答题目数 |
| correctAnswers | Integer | 正确答题数 |
| totalPoints | Integer | 总分 |
| earnedPoints | Integer | 得分 |
| scorePercentage | BigDecimal | 得分率（百分比，如83.33） |
| testStatus | Integer | 测试状态（1=进行中，2=已完成，3=已超时） |
| createAt | String | 创建时间（格式：yyyy-MM-dd HH:mm:ss） |
| createBy | String | 创建人（可能为null） |
| updateAt | String | 更新时间（格式：yyyy-MM-dd HH:mm:ss） |
| updateBy | String | 更新人（可能为null） |
| deleteFlag | String | 删除标志（N=未删除，Y=已删除） |

## 查询逻辑说明

### 关联查询

接口通过以下表关联查询：
1. `student_test_record` (学生测试记录表)
2. `test` (测试表)
3. `test_question` (测试题目关联表)
4. `question` (题目表)

### 查询条件

- `student_test_record.student_id = {studentId}`
- `question.knowledge_point_id IN ({knowledgePointIds})`
- `student_test_record.delete_flag = 'N'`
- `test.delete_flag = 'N'`
- `question.delete_flag = 'N'`

### 去重处理

由于一个测试可能包含多个相同知识点的题目，查询结果使用 `DISTINCT` 去重，确保每个测试记录只返回一次。

### 排序规则

结果按 `create_at DESC` 排序，即按创建时间倒序排列（最新的在前）。

## 使用场景

### 1. 查询学生在特定知识点上的测试记录

```javascript
// 查询学生在"三角形"和"四边形"知识点上的所有测试记录
const records = await queryTestRecords(123, [1, 2]);
```

### 2. 分析学生在特定知识点的学习进度

```javascript
// 查询学生在某个知识点上的所有测试记录，分析学习进度
const records = await queryTestRecords(studentId, [knowledgePointId]);

// 计算平均分
const avgScore = records
  .filter(r => r.testStatus === 2)  // 只统计已完成的测试
  .reduce((sum, r) => sum + (r.scorePercentage || 0), 0) / records.length;

console.log('平均得分率:', avgScore.toFixed(2) + '%');
```

### 3. 查看学生最近在特定知识点的测试情况

```javascript
// 查询最近的测试记录
const records = await queryTestRecords(studentId, [1, 2, 3]);
const recentRecords = records.slice(0, 5);  // 取前5条（已按时间倒序）
```

## 注意事项

1. **知识点ID列表不能为空**: 如果传入空数组，接口会返回错误
2. **结果去重**: 如果一个测试包含多个指定知识点，该测试记录只会返回一次
3. **包含关系**: 只要测试中包含任意一个指定的知识点，该测试记录就会被返回
4. **排序**: 结果按创建时间倒序排列，最新的记录在前
5. **删除标志**: 只返回未删除的记录（delete_flag = 'N'）

## 错误处理

### 常见错误

| 错误信息 | 原因 | 解决方案 |
|---------|------|---------|
| 知识点ID列表不能为空 | 请求体为空或空数组 | 确保传入至少一个知识点ID |
| 404 Not Found | 路径参数错误 | 检查studentId是否正确 |
| 500 Internal Server Error | 服务器内部错误 | 检查数据库连接和SQL语句 |

## 相关接口

- `GET /api/student-test-record/student/{studentId}` - 查询学生的所有测试记录
- `GET /api/student-test-record/student/{studentId}/ongoing` - 查询学生进行中的测试记录
- `GET /api/student-test/history/{studentId}` - 查询学生的测试历史记录（分页）
- `POST /api/student-test/generate-random` - 根据知识点生成随机测试

## 版本历史

### v1.0 (2024-01-XX)
- ✅ 初始版本
- ✅ 支持根据学生ID和知识点ID列表查询测试记录
- ✅ 支持多知识点查询（OR关系）
- ✅ 自动去重和排序



