# 家长查询学生学习进度API增强文档

## 接口说明

**接口地址**: `POST /api/guardian-rel/guardian/{guardianId}/type/{guardianType}`

**功能说明**: 根据家长/老师的ID和类型查询绑定的学生详细信息，包括各知识点类型的学习百分比和学习情况

## 新增功能

在原有学生信息的基础上，新增了**各知识点类型的学习情况统计**，包括：

### 1. 学习进度统计
- **totalKnowledgePoints**: 总知识点数
- **completedKnowledgePoints**: 已完成知识点数
- **inProgressKnowledgePoints**: 学习中知识点数
- **notStartedKnowledgePoints**: 未开始知识点数
- **overallProgress**: 整体完成进度百分比

### 2. 难度分布统计
- **easyCount**: 简单难度知识点数
- **mediumCount**: 中等难度知识点数
- **hardCount**: 困难难度知识点数

## 响应示例

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "relationId": 1,
      "relation": "儿子",
      "studentId": 100,
      "studentAccount": "student001",
      "studentName": "张小明",
      "studentSex": "M",
      "studentAge": 12,
      "studentTel": "13800138000",
      "studentCountry": "中国",
      "studentEmail": "xiaoming@example.com",
      "studentGrade": "8",
      "studentRole": "S",
      
      "categoryLearningProgress": [
        {
          "categoryId": 1,
          "categoryName": "数与代数",
          "categoryNameFr": "Nombres et Algèbre",
          "totalKnowledgePoints": 15,
          "completedKnowledgePoints": 8,
          "inProgressKnowledgePoints": 5,
          "notStartedKnowledgePoints": 2,
          "overallProgress": 65.33,
          "easyCount": 5,
          "mediumCount": 7,
          "hardCount": 3
        },
        {
          "categoryId": 2,
          "categoryName": "几何",
          "categoryNameFr": "Géométrie",
          "totalKnowledgePoints": 12,
          "completedKnowledgePoints": 6,
          "inProgressKnowledgePoints": 4,
          "notStartedKnowledgePoints": 2,
          "overallProgress": 58.33,
          "easyCount": 4,
          "mediumCount": 5,
          "hardCount": 3
        },
        {
          "categoryId": 3,
          "categoryName": "统计与概率",
          "categoryNameFr": "Statistiques et Probabilité",
          "totalKnowledgePoints": 8,
          "completedKnowledgePoints": 3,
          "inProgressKnowledgePoints": 3,
          "notStartedKnowledgePoints": 2,
          "overallProgress": 45.00,
          "easyCount": 2,
          "mediumCount": 4,
          "hardCount": 2
        },
        {
          "categoryId": 4,
          "categoryName": "综合与实践",
          "categoryNameFr": "Compréhension et Pratique",
          "totalKnowledgePoints": 10,
          "completedKnowledgePoints": 2,
          "inProgressKnowledgePoints": 3,
          "notStartedKnowledgePoints": 5,
          "overallProgress": 25.00,
          "easyCount": 3,
          "mediumCount": 4,
          "hardCount": 3
        }
      ]
    }
  ]
}
```

## 知识点类型学习情况字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| categoryId | Integer | 分类ID |
| categoryName | String | 分类名称（中文）|
| categoryNameFr | String | 分类名称（法语）|
| totalKnowledgePoints | Integer | 总知识点数 |
| completedKnowledgePoints | Integer | 已完成知识点数 |
| inProgressKnowledgePoints | Integer | 学习中知识点数 |
| notStartedKnowledgePoints | Integer | 未开始知识点数 |
| overallProgress | BigDecimal | 整体完成进度百分比 |
| easyCount | Integer | 简单难度知识点数 |
| mediumCount | Integer | 中等难度知识点数 |
| hardCount | Integer | 困难难度知识点数 |

## 进度状态说明

### 学习进度状态（progressStatus）
- **1**: 未开始
- **2**: 学习中
- **3**: 已完成

### 计算逻辑

```java
overallProgress = (所有知识点的完成百分比总和) / (总知识点数)

例如：
- 分类下有10个知识点
- 每个知识点的完成百分比分别为: 100%, 80%, 60%, 0%, 0%, 100%, 50%, 20%, 0%, 0%
- 总和 = 410%
- overallProgress = 410 / 10 = 41.00%
```

## 使用示例

### Vue.js
```vue
<template>
  <div class="student-detail">
    <h2>学生详情</h2>
    <div v-for="student in students" :key="student.studentId">
      <h3>{{ student.studentName }}</h3>
      <p>关系: {{ student.relation }}</p>
      
      <div class="category-progress">
        <h4>知识点类型学习情况</h4>
        <div 
          v-for="category in student.categoryLearningProgress" 
          :key="category.categoryId"
          class="category-item"
        >
          <h5>{{ category.categoryName }}</h5>
          <p>总知识点: {{ category.totalKnowledgePoints }}</p>
          <p>已完成: {{ category.completedKnowledgePoints }}</p>
          <p>学习中: {{ category.inProgressKnowledgePoints }}</p>
          <p>未开始: {{ category.notStartedKnowledgePoints }}</p>
          <div class="progress-bar">
            <div 
              class="progress-fill" 
              :style="{ width: category.overallProgress + '%' }"
            >
              {{ category.overallProgress.toFixed(1) }}%
            </div>
          </div>
          <p>难度分布: 简单({{ category.easyCount }}) 中等({{ category.mediumCount }}) 困难({{ category.hardCount }})</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      students: []
    }
  },
  async mounted() {
    const guardianId = this.$route.params.id;
    const guardianType = this.$route.params.type;
    
    const response = await this.$http.post(
      `/api/guardian-rel/guardian/${guardianId}/type/${guardianType}`
    );
    
    if (response.data.code === 200) {
      this.students = response.data.data;
    }
  }
}
</script>

<style>
.category-progress {
  margin-top: 20px;
}

.category-item {
  border: 1px solid #ddd;
  padding: 15px;
  margin: 10px 0;
  border-radius: 4px;
}

.progress-bar {
  width: 100%;
  height: 20px;
  background-color: #f0f0f0;
  border-radius: 10px;
  overflow: hidden;
  margin: 10px 0;
}

.progress-fill {
  height: 100%;
  background-color: #4CAF50;
  transition: width 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 12px;
}
</style>
```

### React
```jsx
function StudentProgress({ guardianId, guardianType }) {
  const [students, setStudents] = useState([]);
  
  useEffect(() => {
    fetch(`/api/guardian-rel/guardian/${guardianId}/type/${guardianType}`, {
      method: 'POST'
    })
    .then(res => res.json())
    .then(data => {
      if (data.code === 200) {
        setStudents(data.data);
      }
    });
  }, [guardianId, guardianType]);
  
  return (
    <div>
      {students.map(student => (
        <div key={student.studentId}>
          <h3>{student.studentName}</h3>
          <div className="category-progress">
            {student.categoryLearningProgress?.map(category => (
              <div key={category.categoryId} className="category-item">
                <h5>{category.categoryName}</h5>
                <p>进度: {category.overallProgress.toFixed(1)}%</p>
                <p>已完成: {category.completedKnowledgePoints}/{category.totalKnowledgePoints}</p>
                <div className="progress-bar">
                  <div 
                    className="progress-fill"
                    style={{ width: `${category.overallProgress}%` }}
                  />
                </div>
              </div>
            ))}
          </div>
        </div>
      ))}
    </div>
  );
}
```

## 应用场景

1. **家长查看孩子学习情况** - 了解孩子在各个知识点类型的掌握程度
2. **学习报告生成** - 生成详细的学习进度报告
3. **学习计划制定** - 根据薄弱环节制定学习计划
4. **老师监控学生** - 跟踪学生的整体学习进度

## 注意事项

1. 只返回学生有学习进度记录的知识点分类
2. 如果没有学习进度记录，`categoryLearningProgress` 为空数组
3. 进度百分比保留2位小数
4. 难度分布统计基于知识点的 `difficulty_level` 字段

## 数据计算逻辑

### 统计流程
1. 获取学生的所有学习进度记录
2. 获取所有知识点分类
3. 为每个分类：
   - 统计该分类下的所有知识点
   - 遍历每个知识点，检查学习进度状态
   - 统计已完成/学习中/未开始的数量
   - 计算整体进度百分比
   - 统计简单/中等/困难知识点数量
4. 按分类ID排序返回结果

### 进度状态判断
```java
if (progress == null) {
    notStartedCount++;  // 未开始
} else if (progress.getProgressStatus() == 3) {
    completedCount++;   // 已完成
} else if (progress.getProgressStatus() == 2) {
    inProgressCount++;  // 学习中
} else {
    notStartedCount++;   // 未开始
}
```
