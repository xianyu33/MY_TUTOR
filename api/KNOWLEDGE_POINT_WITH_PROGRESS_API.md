# 知识点列表查询API（包含学习进度）

## 接口说明

根据学生ID、年级ID和知识点分类ID查询知识点列表，返回知识点详细信息以及学生的学习进度信息。

## 接口信息

- **URL**: `/api/math/knowledge/query-with-progress`
- **请求方式**: `POST`
- **请求格式**: `application/json`

## 请求参数

### Request Body

```json
{
  "studentId": 1,
  "gradeId": 2,
  "categoryId": 3
}
```

### 参数说明

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| studentId | Integer | 是 | 学生用户ID |
| gradeId | Integer | 是 | 年级ID |
| categoryId | Integer | 是 | 知识点分类ID |

## 响应数据

### 响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "gradeId": 2,
      "categoryId": 3,
      "pointName": "整数运算",
      "pointNameFr": "Opérations sur les entiers",
      "pointCode": "NUM_OP_001",
      "description": "学习整数的加减乘除运算",
      "descriptionFr": "Apprendre les opérations arithmétiques sur les entiers",
      "content": "整数运算包括加法、减法、乘法和除法...",
      "contentFr": "Les opérations sur les entiers incluent l'addition, la soustraction, la multiplication et la division...",
      "iconUrl": "/icons/number-operation.png",
      "iconClass": "icon-number-operation",
      "difficultyLevel": 2,
      "difficultyName": "中等",
      "sortOrder": 1,
      "learningObjectives": "掌握整数四则运算",
      "learningObjectivesFr": "Maîtriser les quatre opérations sur les entiers",
      "categoryName": "数与代数",
      "categoryNameFr": "Nombres et algèbre",
      "categoryCode": "NUM_ALG",
      "gradeName": "二年级",
      "gradeLevel": 2,
      "progressStatus": 2,
      "completionPercentage": 65.50,
      "startTime": "2024-01-15 10:30:00",
      "completeTime": null,
      "studyDuration": 120,
      "lastStudyTime": "2024-01-20 14:20:00",
      "notes": "需要加强练习乘法运算",
      "createAt": "2024-01-01 08:00:00",
      "updateAt": "2024-01-20 14:20:00"
    }
  ]
}
```

### 响应字段说明

#### 知识点基本信息

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Integer | 知识点ID |
| gradeId | Integer | 年级ID |
| categoryId | Integer | 分类ID |
| pointName | String | 知识点名称（中文） |
| pointNameFr | String | 知识点名称（法语） |
| pointCode | String | 知识点编码 |
| description | String | 知识点描述（中文） |
| descriptionFr | String | 知识点描述（法语） |
| content | String | 知识点内容详情（中文） |
| contentFr | String | 知识点内容详情（法语） |
| iconUrl | String | 知识点图标URL |
| iconClass | String | 知识点图标CSS类名 |
| difficultyLevel | Integer | 难度等级：1-简单，2-中等，3-困难 |
| difficultyName | String | 难度名称：简单/中等/困难 |
| sortOrder | Integer | 排序序号 |
| learningObjectives | String | 学习目标（中文） |
| learningObjectivesFr | String | 学习目标（法语） |

#### 分类信息

| 字段名 | 类型 | 说明 |
|--------|------|------|
| categoryName | String | 分类名称（中文） |
| categoryNameFr | String | 分类名称（法语） |
| categoryCode | String | 分类编码 |

#### 年级信息

| 字段名 | 类型 | 说明 |
|--------|------|------|
| gradeName | String | 年级名称 |
| gradeLevel | Integer | 年级等级（1-12） |

#### 学习进度信息

| 字段名 | 类型 | 说明 |
|--------|------|------|
| progressStatus | Integer | 学习状态：1-未开始，2-学习中，3-已完成 |
| completionPercentage | BigDecimal | 完成百分比（0.00-100.00） |
| startTime | Date | 开始学习时间 |
| completeTime | Date | 完成学习时间（未完成时为null） |
| studyDuration | Integer | 学习时长（分钟） |
| lastStudyTime | Date | 最后学习时间 |
| notes | String | 学习笔记 |

#### 时间信息

| 字段名 | 类型 | 说明 |
|--------|------|------|
| createAt | Date | 创建时间 |
| updateAt | Date | 更新时间 |

## 请求示例

### cURL示例

```bash
curl -X POST http://localhost:8080/api/math/knowledge/query-with-progress \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 1,
    "gradeId": 2,
    "categoryId": 3
  }'
```

### JavaScript (fetch)示例

```javascript
fetch('http://localhost:8080/api/math/knowledge/query-with-progress', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    studentId: 1,
    gradeId: 2,
    categoryId: 3
  })
})
.then(response => response.json())
.then(data => {
  console.log('Success:', data);
})
.catch((error) => {
  console.error('Error:', error);
});
```

### Java (OkHttp)示例

```java
import okhttp3.*;
import java.io.IOException;

public class KnowledgePointQueryExample {
    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();
        
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, 
            "{\"studentId\":1,\"gradeId\":2,\"categoryId\":3}");
        
        Request request = new Request.Builder()
            .url("http://localhost:8080/api/math/knowledge/query-with-progress")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### Python (requests)示例

```python
import requests
import json

url = "http://localhost:8080/api/math/knowledge/query-with-progress"

payload = {
    "studentId": 1,
    "gradeId": 2,
    "categoryId": 3
}

headers = {
    "Content-Type": "application/json"
}

response = requests.post(url, headers=headers, data=json.dumps(payload))

if response.status_code == 200:
    data = response.json()
    print(json.dumps(data, indent=2, ensure_ascii=False))
else:
    print(f"Error: {response.status_code}")
    print(response.text)
```

## 注意事项

1. **参数验证**：所有三个参数（studentId、gradeId、categoryId）都是必填的
2. **学习进度**：如果学生没有该知识点的学习进度记录，则：
   - `progressStatus` 默认为 `1`（未开始）
   - `completionPercentage` 默认为 `0.00`
   - `studyDuration` 默认为 `0`
   - 其他进度相关字段为 `null`
3. **排序**：返回结果按 `sortOrder` 升序排列，如果 `sortOrder` 相同则按知识点ID排序
4. **多语言支持**：响应数据包含中文字段和法语字段（如果数据库中有对应数据）
5. **空结果**：如果查询没有匹配的知识点，返回空数组 `[]`

## 错误处理

### 参数缺失错误

```json
{
  "code": 400,
  "message": "请求参数错误",
  "data": null
}
```

### 服务器错误

```json
{
  "code": 500,
  "message": "服务器内部错误",
  "data": null
}
```

## 使用场景

1. **学习进度查询**：学生查看自己在某个分类下的所有知识点及学习进度
2. **学习规划**：根据学习进度情况，规划下一步学习内容
3. **进度统计**：统计学生在某个分类下的学习完成情况
4. **学习报告**：生成学生学习报告，展示知识点掌握情况

## 相关接口

- [知识点详情查询](./KNOWLEDGE_POINT_DETAILS_API.md)
- [学习进度管理](./LEARNING_PROGRESS_CATEGORY_RELATION_API.md)
- [知识点分类查询](./MATH_API_DOCUMENTATION.md)

