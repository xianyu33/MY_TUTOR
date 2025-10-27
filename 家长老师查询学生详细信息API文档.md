# 家长/老师查询学生详细信息API文档

## 接口说明

**接口地址**: `POST /api/guardian-rel/guardian/{guardianId}/type/{guardianType}`

**功能说明**: 根据家长/老师的ID和类型查询绑定的学生详细信息（包含学生基本信息和关系信息）

## 请求参数

### 路径参数
- `guardianId` (Integer) - 家长/老师ID
- `guardianType` (Integer) - 类型：0-家长，1-老师

### 请求方式
- **POST** 请求

## 响应示例

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "relationId": 1,
      "relation": "父亲",
      "relationStartAt": "2024-01-01 10:00:00",
      "relationEndAt": null,
      
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
      "studentCreateAt": "2023-09-01 08:00:00",
      "studentUpdateAt": "2024-01-15 10:30:00",
      
      "totalTests": null,
      "completedTests": null,
      "averageScore": null
    },
    {
      "relationId": 2,
      "relation": "女儿",
      "relationStartAt": "2024-01-01 10:00:00",
      "relationEndAt": null,
      
      "studentId": 101,
      "studentAccount": "student002",
      "studentName": "张小红",
      "studentSex": "F",
      "studentAge": 10,
      "studentTel": "13900139000",
      "studentCountry": "中国",
      "studentEmail": "xiaohong@example.com",
      "studentGrade": "6",
      "studentRole": "S",
      "studentCreateAt": "2023-09-01 08:00:00",
      "studentUpdateAt": "2024-01-15 10:30:00",
      
      "totalTests": null,
      "completedTests": null,
      "averageScore": null
    }
  ]
}
```

## 响应字段说明

### 关系信息
- `relationId` - 关系记录ID
- `relation` - 关系描述（如：父亲、母亲、女儿、班主任等）
- `relationStartAt` - 关系开始时间
- `relationEndAt` - 关系结束时间（null表示当前有效）

### 学生基本信息
- `studentId` - 学生ID
- `studentAccount` - 学生账号
- `studentName` - 学生姓名
- `studentSex` - 学生性别（M-男，F-女）
- `studentAge` - 学生年龄
- `studentTel` - 学生电话
- `studentCountry` - 学生国家
- `studentEmail` - 学生邮箱
- `studentGrade` - 学生年级
- `studentRole` - 学生角色（S-学生）
- `studentCreateAt` - 学生创建时间
- `studentUpdateAt` - 学生更新时间

### 学习统计信息（预留）
- `totalTests` - 总测试次数
- `completedTests` - 已完成测试次数
- `averageScore` - 平均得分

## 使用示例

### JavaScript/Fetch
```javascript
const guardianId = 1;  // 家长/老师ID
const guardianType = 0;  // 0-家长，1-老师

const response = await fetch(
  `http://localhost:8080/api/guardian-rel/guardian/${guardianId}/type/${guardianType}`,
  {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    }
  }
);

const data = await response.json();
console.log('绑定的学生:', data.data);
```

### Vue.js
```vue
<template>
  <div class="students-list">
    <h2>我的学生列表</h2>
    
    <div v-for="student in students" :key="student.studentId" class="student-card">
      <div class="student-info">
        <h3>{{ student.studentName }}</h3>
        <p>关系: {{ student.relation }}</p>
        <p>年级: {{ student.studentGrade }}年级</p>
        <p>年龄: {{ student.studentAge }}岁</p>
        <p>性别: {{ student.studentSex === 'M' ? '男' : '女' }}</p>
        <p>电话: {{ student.studentTel }}</p>
        <p>邮箱: {{ student.studentEmail }}</p>
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
.student-card {
  border: 1px solid #ddd;
  padding: 15px;
  margin: 10px 0;
  border-radius: 4px;
}

.student-info h3 {
  color: #333;
  margin-bottom: 10px;
}

.student-info p {
  margin: 5px 0;
  color: #666;
}
</style>
```

### React
```jsx
import React, { useState, useEffect } from 'react';

function StudentList({ guardianId, guardianType }) {
  const [students, setStudents] = useState([]);
  
  useEffect(() => {
    fetch(`/api/guardian-rel/guardian/${guardianId}/type/${guardianType}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      }
    })
    .then(res => res.json())
    .then(data => {
      if (data.code === 200) {
        setStudents(data.data);
      }
    });
  }, [guardianId, guardianType]);
  
  return (
    <div className="students-list">
      <h2>我的学生列表</h2>
      {students.map(student => (
        <div key={student.studentId} className="student-card">
          <h3>{student.studentName}</h3>
          <p>关系: {student.relation}</p>
          <p>年级: {student.studentGrade}年级</p>
          <p>电话: {student.studentTel}</p>
          <p>邮箱: {student.studentEmail}</p>
        </div>
      ))}
    </div>
  );
}
```

### cURL
```bash
# 查询ID为1的家长绑定的学生
curl -X POST "http://localhost:8080/api/guardian-rel/guardian/1/type/0"

# 查询ID为5的老师绑定的学生
curl -X POST "http://localhost:8080/api/guardian-rel/guardian/5/type/1"
```

## 与GET接口的区别

### GET /api/guardian-rel/guardian/{guardianId}/type/{guardianType}
- 返回关系记录列表（`GuardianStudentRel`）
- 包含关系ID、类型、开始结束时间等
- 不包含学生详细信息
- 适用于关系管理场景

### POST /api/guardian-rel/guardian/{guardianId}/type/{guardianType}
- 返回学生详细信息列表（`StudentDetailDTO`）
- 包含完整的学生基本信息和关系信息
- 直接返回学生数据，便于前端展示
- 适用于学生信息展示场景

## 应用场景

1. **家长查看孩子信息** - 家长登录后查看自己的孩子的基本信息
2. **老师查看学生信息** - 老师查看班级学生的详细信息
3. **学生管理界面** - 管理后台展示监护人绑定的学生列表
4. **权限控制** - 基于关系信息控制访问权限

## 注意事项

1. 只有与家长/老师建立了关系的学生才会被返回
2. 返回的数据包含逻辑删除的学生（delete_flag='0'）
3. 如果学生已被删除，不会被返回
4. 可以扩展添加学习统计信息（总测试数、平均分等）
