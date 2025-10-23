# 家长/老师-学生关系管理 API 文档

## 模块概述

本模块提供家长/老师与学生关系的管理功能，支持多对多关系绑定、解绑、查询等操作。

## 数据库表结构

### 核心表说明

1. **parent** - 家长/老师表：存储家长和老师信息，通过 `type` 字段区分（0-家长，1-老师）
2. **user** - 用户表：充当学生表，存储学生信息
3. **guardian_student_rel** - 关系关联表：管理家长/老师与学生的多对多关系

## API 接口文档

### 1. 关系管理 API

#### 1.1 绑定关系
- **URL**: `POST /api/guardian-rel/bind`
- **描述**: 绑定家长/老师与学生关系
- **参数**:
  - `guardianId` (Integer, 必填) - 家长/老师ID
  - `guardianType` (Integer, 必填) - 类型：0-家长，1-老师
  - `studentId` (Integer, 必填) - 学生ID（user表ID）
  - `relation` (String, 可选) - 关系描述：父亲/母亲/监护人/班主任/任课老师等
  - `operator` (String, 可选) - 操作人，默认"system"
- **响应**: 返回绑定成功的关系记录

**示例请求**:
```bash
POST /api/guardian-rel/bind?guardianId=1&guardianType=0&studentId=100&relation=父亲&operator=admin
```

**示例响应**:
```json
{
  "code": 200,
  "message": "绑定成功",
  "data": {
    "id": 1,
    "guardianId": 1,
    "guardianType": 0,
    "studentId": 100,
    "relation": "父亲",
    "startAt": "2024-01-01 10:00:00",
    "endAt": null,
    "createAt": "2024-01-01 10:00:00",
    "createBy": "admin",
    "updateAt": "2024-01-01 10:00:00",
    "updateBy": "admin",
    "deleteFlag": "0"
  }
}
```

#### 1.2 按监护人查询关系
- **URL**: `GET /api/guardian-rel/guardian/{guardianId}/type/{guardianType}`
- **描述**: 查询指定家长/老师的所有学生关系
- **参数**:
  - `guardianId` (Integer) - 家长/老师ID
  - `guardianType` (Integer) - 类型：0-家长，1-老师
- **响应**: 返回该监护人的所有学生关系列表

**示例请求**:
```bash
GET /api/guardian-rel/guardian/1/type/0
```

**示例响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "guardianId": 1,
      "guardianType": 0,
      "studentId": 100,
      "relation": "父亲",
      "startAt": "2024-01-01 10:00:00",
      "endAt": null,
      "createAt": "2024-01-01 10:00:00",
      "createBy": "admin",
      "updateAt": "2024-01-01 10:00:00",
      "updateBy": "admin",
      "deleteFlag": "0"
    }
  ]
}
```

#### 1.3 按学生查询关系
- **URL**: `GET /api/guardian-rel/student/{studentId}`
- **描述**: 查询指定学生的所有监护人关系
- **参数**:
  - `studentId` (Integer) - 学生ID（user表ID）
- **响应**: 返回该学生的所有监护人关系列表

**示例请求**:
```bash
GET /api/guardian-rel/student/100
```

**示例响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "guardianId": 1,
      "guardianType": 0,
      "studentId": 100,
      "relation": "父亲",
      "startAt": "2024-01-01 10:00:00",
      "endAt": null,
      "createAt": "2024-01-01 10:00:00",
      "createBy": "admin",
      "updateAt": "2024-01-01 10:00:00",
      "updateBy": "admin",
      "deleteFlag": "0"
    },
    {
      "id": 2,
      "guardianId": 5,
      "guardianType": 1,
      "studentId": 100,
      "relation": "班主任",
      "startAt": "2024-01-01 10:00:00",
      "endAt": null,
      "createAt": "2024-01-01 10:00:00",
      "createBy": "admin",
      "updateAt": "2024-01-01 10:00:00",
      "updateBy": "admin",
      "deleteFlag": "0"
    }
  ]
}
```

#### 1.4 更新关系描述
- **URL**: `PUT /api/guardian-rel/{id}/relation`
- **描述**: 更新关系描述
- **参数**:
  - `id` (Long) - 关系记录ID
  - `relation` (String) - 新的关系描述
  - `operator` (String, 可选) - 操作人，默认"system"
- **响应**: 返回更新后的关系记录

**示例请求**:
```bash
PUT /api/guardian-rel/1/relation?relation=监护人&operator=admin
```

**示例响应**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "id": 1,
    "guardianId": 1,
    "guardianType": 0,
    "studentId": 100,
    "relation": "监护人",
    "startAt": "2024-01-01 10:00:00",
    "endAt": null,
    "createAt": "2024-01-01 10:00:00",
    "createBy": "admin",
    "updateAt": "2024-01-01 11:00:00",
    "updateBy": "admin",
    "deleteFlag": "0"
  }
}
```

#### 1.5 解绑关系
- **URL**: `DELETE /api/guardian-rel/{id}`
- **描述**: 解绑家长/老师与学生关系（逻辑删除）
- **参数**:
  - `id` (Long) - 关系记录ID
- **响应**: 返回操作结果

**示例请求**:
```bash
DELETE /api/guardian-rel/1
```

**示例响应**:
```json
{
  "code": 200,
  "message": "解绑成功",
  "data": null
}
```

## 业务场景示例

### 场景1：家长绑定孩子
```bash
# 1. 家长ID=1，学生ID=100，关系为"父亲"
POST /api/guardian-rel/bind?guardianId=1&guardianType=0&studentId=100&relation=父亲

# 2. 查询该家长的所有孩子
GET /api/guardian-rel/guardian/1/type/0
```

### 场景2：老师管理学生
```bash
# 1. 老师ID=5，学生ID=100，关系为"班主任"
POST /api/guardian-rel/bind?guardianId=5&guardianType=1&studentId=100&relation=班主任

# 2. 查询该老师的所有学生
GET /api/guardian-rel/guardian/5/type/1
```

### 场景3：查看学生的所有监护人
```bash
# 查询学生ID=100的所有监护人（家长+老师）
GET /api/guardian-rel/student/100
```

### 场景4：关系变更
```bash
# 1. 更新关系描述
PUT /api/guardian-rel/1/relation?relation=监护人

# 2. 解绑关系
DELETE /api/guardian-rel/1
```

## 数据模型

### GuardianStudentRel 实体
```json
{
  "id": "关系记录ID (Long)",
  "guardianId": "家长/老师ID (Integer)",
  "guardianType": "类型：0-家长，1-老师 (Integer)",
  "studentId": "学生ID (Integer)",
  "relation": "关系描述 (String)",
  "startAt": "关系开始时间 (Date)",
  "endAt": "关系结束时间 (Date, 空表示有效)",
  "createAt": "创建时间 (Date)",
  "createBy": "创建人 (String)",
  "updateAt": "更新时间 (Date)",
  "updateBy": "更新人 (String)",
  "deleteFlag": "删除标志：0-正常，1-删除 (String)"
}
```

## 注意事项

1. **唯一性约束**: 同一家长/老师与同一学生只能有一条有效关系记录
2. **逻辑删除**: 解绑操作使用逻辑删除，不会物理删除数据
3. **外键约束**: `guardian_id` 关联 `parent.id`，`student_id` 关联 `user.id`
4. **类型区分**: 通过 `guardian_type` 字段区分家长(0)和老师(1)
5. **关系描述**: `relation` 字段支持自定义关系描述，如"父亲"、"母亲"、"班主任"、"任课老师"等
6. **时间管理**: `start_at` 记录关系开始时间，`end_at` 为空表示关系有效
7. **操作审计**: 所有操作都会记录创建人、更新人、创建时间、更新时间

## 错误码说明

- `200`: 操作成功
- `500`: 服务器内部错误
- `400`: 请求参数错误
- `404`: 资源不存在

## 扩展功能建议

1. **批量绑定**: 支持一次绑定多个学生
2. **关系历史**: 记录关系变更历史
3. **权限控制**: 根据用户角色限制操作权限
4. **通知机制**: 关系变更时发送通知
5. **统计分析**: 提供关系统计报表

