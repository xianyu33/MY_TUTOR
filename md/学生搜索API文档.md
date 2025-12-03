# 学生搜索API文档

## 接口说明

**接口地址**: `POST /user/search/students`

**功能说明**: 根据名称动态查询学生列表，支持姓名和账号的模糊搜索

## 请求参数

| 参数名      | 类型 | 必填 | 说明 |
|----------|------|------|------|
| username | String | 否 | 学生姓名或账号（模糊查询）|

## 响应示例

### 不传参数（查询所有学生）
```bash
POST /user/search/students
```

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 100,
      "userAccount": "student001",
      "username": "张小明",
      "sex": "M",
      "age": 12,
      "tel": "13800138000",
      "country": "中国",
      "email": "xiaoming@example.com",
      "grade": "8",
      "role": "S",
      "createAt": "2023-09-01 08:00:00",
      "createBy": "admin",
      "updateAt": "2024-01-15 10:30:00",
      "updateBy": "admin",
      "deleteFlag": "0"
    },
    {
      "id": 101,
      "userAccount": "student002",
      "username": "李小红",
      "sex": "F",
      "age": 11,
      "tel": "13900139000",
      "country": "中国",
      "email": "xiaohong@example.com",
      "grade": "7",
      "role": "S",
      "createAt": "2023-09-01 08:00:00",
      "createBy": "admin",
      "updateAt": "2024-01-15 10:30:00",
      "updateBy": "admin",
      "deleteFlag": "0"
    }
  ]
}
```

### 带搜索关键字
```bash
post /user/search/students
```

**请求**:
```json
{
    "username": "y"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 100,
      "userAccount": "student001",
      "username": "张小明",
      "sex": "M",
      "age": 12,
      "tel": "13800138000",
      "country": "中国",
      "email": "xiaoming@example.com",
      "grade": "8",
      "role": "S",
      "createAt": "2023-09-01 08:00:00",
      "createBy": "admin",
      "updateAt": "2024-01-15 10:30:00",
      "updateBy": "admin",
      "deleteFlag": "0"
    }
  ]
}
```

### 按账号搜索
```bash
GET /user/search/students?name=student001
```

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 100,
      "userAccount": "student001",
      "username": "张小明",
      "sex": "M",
      "age": 12,
      "tel": "13800138000",
      "country": "中国",
      "email": "xiaoming@example.com",
      "grade": "8",
      "role": "S",
      "createAt": "2023-09-01 08:00:00",
      "createBy": "admin",
      "updateAt": "2024-01-15 10:30:00",
      "updateBy": "admin",
      "deleteFlag": "0"
    }
  ]
}
```

## 字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Integer | 学生ID |
| userAccount | String | 学生账号 |
| username | String | 学生姓名 |
| sex | String | 性别（M-男，F-女）|
| age | Integer | 年龄 |
| tel | String | 电话 |
| country | String | 国家 |
| email | String | 邮箱 |
| grade | String | 年级 |
| role | String | 角色（S-学生）|
| createAt | Date | 创建时间 |
| updateAt | Date | 更新时间 |

## 使用示例

### JavaScript/Fetch
```javascript
// 搜索姓名为"小"的学生
const response = await fetch('http://localhost:8080/user/search/students?name=小');
const data = await response.json();
console.log('搜索结果:', data.data);
```

### Vue.js
```vue
<template>
  <div class="student-search">
    <input 
      v-model="searchKeyword" 
      placeholder="输入学生姓名或账号"
      @input="searchStudents"
    />
    
    <div v-if="loading">加载中...</div>
    
    <div v-else-if="students.length === 0" class="no-results">
      未找到学生
    </div>
    
    <div v-else class="student-list">
      <div 
        v-for="student in students" 
        :key="student.id" 
        class="student-item"
      >
        <h3>{{ student.username }}</h3>
        <p>账号: {{ student.userAccount }}</p>
        <p>年级: {{ student.grade }}年级</p>
        <p>年龄: {{ student.age }}岁</p>
        <p>电话: {{ student.tel }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchKeyword: '',
      students: [],
      loading: false
    }
  },
  methods: {
    async searchStudents() {
      if (this.searchKeyword.trim() === '') {
        this.students = [];
        return;
      }
      
      this.loading = true;
      try {
        const response = await this.$http.get('/user/search/students', {
          params: {
            name: this.searchKeyword
          }
        });
        
        if (response.data.code === 200) {
          this.students = response.data.data;
        }
      } catch (error) {
        console.error('搜索失败:', error);
      } finally {
        this.loading = false;
      }
    }
  }
}
</script>

<style>
.student-search {
  padding: 20px;
}

.student-search input {
  width: 300px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.student-item {
  border: 1px solid #ddd;
  padding: 15px;
  margin: 10px 0;
  border-radius: 4px;
}

.student-item h3 {
  margin-top: 0;
  color: #333;
}

.no-results {
  text-align: center;
  color: #999;
  padding: 20px;
}
</style>
```

### React
```jsx
import React, { useState, useEffect } from 'react';

function StudentSearch() {
  const [keyword, setKeyword] = useState('');
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(false);
  
  useEffect(() => {
    if (keyword.trim() === '') {
      setStudents([]);
      return;
    }
    
    setLoading(true);
    fetch(`/user/search/students?name=${encodeURIComponent(keyword)}`)
      .then(res => res.json())
      .then(data => {
        if (data.code === 200) {
          setStudents(data.data);
        }
      })
      .catch(err => console.error('搜索失败:', err))
      .finally(() => setLoading(false));
  }, [keyword]);
  
  return (
    <div className="student-search">
      <input
        type="text"
        value={keyword}
        onChange={(e) => setKeyword(e.target.value)}
        placeholder="输入学生姓名或账号"
      />
      
      {loading && <div>加载中...</div>}
      
      {!loading && students.length === 0 && (
        <div>未找到学生</div>
      )}
      
      {!loading && students.map(student => (
        <div key={student.id} className="student-item">
          <h3>{student.username}</h3>
          <p>账号: {student.userAccount}</p>
          <p>年级: {student.grade}年级</p>
          <p>年龄: {student.age}岁</p>
        </div>
      ))}
    </div>
  );
}
```

### cURL
```bash
# 搜索所有学生
curl "http://localhost:8080/user/search/students"

# 搜索姓名包含"小"的学生
curl "http://localhost:8080/user/search/students?name=小"

# 搜索账号包含"student001"的学生
curl "http://localhost:8080/user/search/students?name=student001"
```

## 搜索规则

1. **模糊匹配**: 支持姓名和账号的模糊搜索
2. **可选参数**: `name` 参数为可选，不传则返回所有学生
3. **无密码**: 返回的学生信息不包含密码字段
4. **逻辑删除**: 只返回未删除的学生（delete_flag='0'）
5. **结果排序**: 按创建时间倒序排列

## 应用场景

1. **学生管理** - 老师/管理员快速搜索学生
2. **家长绑定** - 家长根据姓名搜索并绑定孩子
3. **自动完成** - 输入框自动提示学生姓名
4. **学生选择器** - 选择学生时的搜索功能

## 注意事项

- 搜索关键字可以是姓名的一部分
- 搜索关键字也可以是账号的一部分
- 返回的学生信息已隐藏敏感字段（密码）
- 只返回有效的学生记录（未删除）
