# 课程接口文档

> 课程模块采用双表结构：`course`（主表，存储进度）+ `course_content`（内容表，按语言存储）

## 前端调用流程

### 首次进入课程页面

```
1. GET /api/course/difficulty-level        ← 获取建议难度（可选，也可让用户自选）
2. GET /api/course/detail                  ← 查询是否已有课程
   ├─ 有课程 → 展示课程内容和进度
   └─ 无课程 → 展示"开始学习"按钮
```

### 学习主流程（循环 4 次）

```
3. POST /api/course/generate/stream        ← 流式生成当前阶段内容（首次或下一阶段）
   ├─ 收到 token → 实时渲染 AI 内容
   ├─ 收到 [STAGE_DONE] → 内容输出完毕
   ├─ 收到 JSON → 解析更新课程状态
   ├─ 收到 [NO_DATA] → 提示"课程已全部完成"
   └─ 收到 [ERROR] xxx → 显示错误提示

4. 用户学习当前阶段内容...

5. POST /api/course/{id}/complete-stage    ← 用户标记当前阶段完成
   └─ 返回更新后的 progress（25% → 50% → 75% → 100%）

6. 回到步骤 3，生成下一阶段（直到 4 个阶段全部完成）
```

### 辅助查询（随时可调用）

```
GET /api/course/{id}?language=en           ← 查看课程详情（含指定语言内容）
GET /api/course/student/{studentId}        ← 查看学生的课程列表和进度
GET /api/course/knowledge-point/{id}       ← 查看某知识点的课程列表
DELETE /api/course/{id}                    ← 删除课程
```

---

## 目录

1. [数据结构说明](#1-数据结构说明)
2. [生成课程（非流式）](#2-生成课程非流式)
3. [流式生成课程（SSE）](#3-流式生成课程sse)
4. [标记当前阶段完成](#4-标记当前阶段完成)
5. [根据ID查询课程](#5-根据id查询课程)
6. [根据学生ID和知识点ID查询课程](#6-根据学生id和知识点id查询课程)
7. [根据学生ID查询课程列表](#7-根据学生id查询课程列表)
8. [根据知识点ID查询课程列表](#8-根据知识点id查询课程列表)
9. [获取建议难度级别](#9-获取建议难度级别)
10. [删除课程](#10-删除课程)

---

## 1. 数据结构说明

### Course（课程主体）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Integer | 课程ID |
| studentId | Integer | 学生ID |
| knowledgePointId | Integer | 知识点ID |
| difficultyLevel | Integer | 难度等级：1-简单，2-中等，3-困难 |
| currentStage | Integer | 当前已生成阶段：0-未开始，1-4对应已生成的阶段 |
| completedStage | Integer | 用户已完成的阶段：0-未完成，1-4对应已完成的阶段 |
| progress | Integer | 学习进度百分比（计算字段：completedStage × 25） |
| content | CourseContent | 指定语言的课程内容（查询时填充） |
| knowledgePoint | KnowledgePoint | 关联的知识点对象（生成时填充） |
| createAt | String | 创建时间 |
| updateAt | String | 更新时间 |

### CourseContent（课程内容，按语言存储）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Integer | 内容ID |
| courseId | Integer | 关联的课程ID |
| language | String | 语言：en-英语，fr-法语 |
| courseTitle | String | 课程标题 |
| explanation | String | 阶段1 Understand：概念定义、关键要素、简单示例、适用边界 |
| examples | String | 阶段2 Apply：典型场景、复杂案例、实现步骤、常见错误 |
| keySummary | String | 阶段3 Master：步骤拆解、对比分析、原理理解、扩展应用 |
| additionalInfo | String | 阶段4 Evaluate：场景判断、方案选择、错误诊断、学习总结 |
| createAt | String | 创建时间 |
| updateAt | String | 更新时间 |

### 四阶段学习流程

```
生成 Stage 1 → completedStage=0, progress=0%
    ↓ 用户学习
调用 complete-stage → completedStage=1, progress=25%
    ↓
生成 Stage 2 → completedStage=1, progress=25%
    ↓ 用户学习
调用 complete-stage → completedStage=2, progress=50%
    ↓
...以此类推，直到 Stage 4 完成，progress=100%
```

**规则**：必须先标记完成当前阶段，才能生成下一阶段。

---

## 2. 生成课程（非流式）

**接口地址**: `POST /api/course/generate`

**请求参数**:
```json
{
  "knowledgePointId": 631,
  "studentId": 48,
  "difficultyLevel": 1,
  "language": "en"
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| studentId | Integer | 是 | 学生ID |
| knowledgePointId | Integer | 是 | 知识点ID |
| difficultyLevel | Integer | 否 | 难度等级(1-简单,2-中等,3-困难)，不传则自动确定 |
| language | String | 否 | 语言，默认 "en" |

**成功响应**:
```json
{
  "code": 200,
  "message": "课程生成成功",
  "data": {
    "id": 1,
    "studentId": 48,
    "knowledgePointId": 631,
    "difficultyLevel": 1,
    "currentStage": 1,
    "completedStage": 0,
    "progress": 0,
    "content": {
      "id": 1,
      "courseId": 1,
      "language": "en",
      "courseTitle": "Logarithmic Functions",
      "explanation": "Stage 1 内容...",
      "examples": null,
      "keySummary": null,
      "additionalInfo": null
    },
    "knowledgePoint": {
      "id": 631,
      "pointName": "Logarithmic Functions"
    },
    "createAt": "2026-02-09 12:00:00"
  }
}
```

**未完成当前阶段时调用**:
```json
{
  "code": 500,
  "message": "请先完成当前阶段的学习（阶段 1），再生成下一阶段"
}
```

**已全部生成时调用**（返回完整课程数据）:
```json
{
  "code": 200,
  "message": "课程生成成功",
  "data": {
    "currentStage": 4,
    "completedStage": 3,
    "progress": 75,
    "content": { "..." }
  }
}
```

---

## 3. 流式生成课程（SSE）

**接口地址**: `POST /api/course/generate/stream`

**Content-Type**: `text/event-stream`

**请求参数**: 同 [2. 生成课程](#2-生成课程非流式)

### SSE 事件流状态

| 事件内容 | 含义 | 前端处理 |
|---------|------|---------|
| 普通文本 | AI 生成的 token 增量 | 追加到页面实时渲染 |
| `[STAGE_DONE]` | 当前阶段流式输出完毕 | 停止追加文本，可显示加载态 |
| `{...}` JSON 对象 | 最终的 Course 完整数据 | 解析 JSON 更新课程状态 |
| `[NO_DATA]` | 4 个阶段已全部生成 | 提示用户课程已完成 |
| `[ERROR] xxx` | 业务错误 | 显示错误提示信息 |

### 正常流程时序

```
data: token1
data: token2
data: ...
data: [STAGE_DONE]
data: {"id":1,"currentStage":1,"completedStage":0,"progress":0,"content":{...}}
```

### 前端判断逻辑参考

```javascript
eventSource.onmessage = (event) => {
  const data = event.data;

  if (data === '[NO_DATA]') {
    // 课程已全部生成完毕
  } else if (data === '[STAGE_DONE]') {
    // 当前阶段输出完毕，等待最终 JSON 数据
  } else if (data.startsWith('[ERROR]')) {
    // 业务错误，显示提示信息
    const errorMsg = data.substring(8);
  } else if (data.startsWith('{')) {
    // 最终 Course JSON 数据
    const course = JSON.parse(data);
  } else {
    // AI 生成的 token，追加渲染
  }
};
```

---

## 4. 标记当前阶段完成

**接口地址**: `POST /api/course/{id}/complete-stage`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Integer | 是 | 课程ID（路径参数） |

**成功响应**:
```json
{
  "code": 200,
  "message": "阶段完成标记成功",
  "data": {
    "id": 1,
    "studentId": 48,
    "knowledgePointId": 631,
    "difficultyLevel": 1,
    "currentStage": 1,
    "completedStage": 1,
    "progress": 25
  }
}
```

**没有可完成的阶段**:
```json
{
  "code": 500,
  "message": "当前阶段已完成，没有新的阶段需要标记完成"
}
```

**课程不存在**:
```json
{
  "code": 500,
  "message": "课程不存在: 999"
}
```

---

## 5. 根据ID查询课程

**接口地址**: `GET /api/course/{id}`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Integer | 是 | 课程ID（路径参数） |
| language | String | 否 | 语言，默认 "en" |

**请求示例**: `GET /api/course/1?language=en`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "studentId": 48,
    "knowledgePointId": 631,
    "difficultyLevel": 1,
    "currentStage": 2,
    "completedStage": 1,
    "progress": 25,
    "content": {
      "id": 1,
      "courseId": 1,
      "language": "en",
      "courseTitle": "Logarithmic Functions",
      "explanation": "Stage 1 内容...",
      "examples": "Stage 2 内容...",
      "keySummary": null,
      "additionalInfo": null
    }
  }
}
```

---

## 6. 根据学生ID和知识点ID查询课程

**接口地址**: `GET /api/course/detail`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| studentId | Integer | 是 | 学生ID |
| knowledgePointId | Integer | 是 | 知识点ID |
| language | String | 否 | 语言，默认 "en" |

**请求示例**: `GET /api/course/detail?studentId=48&knowledgePointId=631&language=fr`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "studentId": 48,
    "knowledgePointId": 631,
    "difficultyLevel": 1,
    "currentStage": 1,
    "completedStage": 0,
    "progress": 0,
    "content": {
      "id": 2,
      "courseId": 1,
      "language": "fr",
      "courseTitle": "Fonctions logarithmiques",
      "explanation": "Stage 1 内容（法语）..."
    }
  }
}
```

**课程不存在**:
```json
{
  "code": 500,
  "message": "课程不存在"
}
```

---

## 7. 根据学生ID查询课程列表

**接口地址**: `GET /api/course/student/{studentId}`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| studentId | Integer | 是 | 学生ID（路径参数） |

**请求示例**: `GET /api/course/student/48`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "studentId": 48,
      "knowledgePointId": 631,
      "difficultyLevel": 1,
      "currentStage": 2,
      "completedStage": 1,
      "progress": 25
    },
    {
      "id": 2,
      "studentId": 48,
      "knowledgePointId": 632,
      "difficultyLevel": 2,
      "currentStage": 4,
      "completedStage": 4,
      "progress": 100
    }
  ]
}
```

> 注意：列表接口不返回 content 内容，需要查看具体内容请调用 [根据ID查询课程](#5-根据id查询课程)

---

## 8. 根据知识点ID查询课程列表

**接口地址**: `GET /api/course/knowledge-point/{knowledgePointId}`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| knowledgePointId | Integer | 是 | 知识点ID（路径参数） |

**请求示例**: `GET /api/course/knowledge-point/631`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "studentId": 48,
      "knowledgePointId": 631,
      "difficultyLevel": 1,
      "currentStage": 3,
      "completedStage": 2,
      "progress": 50
    }
  ]
}
```

---

## 9. 获取建议难度级别

**接口地址**: `GET /api/course/difficulty-level`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| studentId | Integer | 是 | 学生ID |
| knowledgePointId | Integer | 是 | 知识点ID |

**请求示例**: `GET /api/course/difficulty-level?studentId=48&knowledgePointId=631`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": 2
}
```

---

## 10. 删除课程

**接口地址**: `DELETE /api/course/{id}`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Integer | 是 | 课程ID（路径参数） |

**响应示例**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": true
}
```
