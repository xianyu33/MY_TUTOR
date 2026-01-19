# 老师列表查询接口文档

## 概述

本文档描述了查询老师列表的两个接口，包括查询未审批的老师列表和已审批通过的老师列表。两个接口都需要管理员权限（type=9）。

---

## 1. 查询未审批的老师列表

### 接口信息

- **接口路径**: `/parent/unapprovedTeachers`
- **请求方式**: `POST`
- **权限要求**: 管理员权限（type=9）
- **功能描述**: 查询所有未审批的老师列表，支持按名称、电话、邮箱进行模糊查询

### 请求头

```
Authorization: Bearer {token}
Content-Type: application/json
```

### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | String | 否 | 老师名称或账号（模糊查询） |
| tel | String | 否 | 电话（模糊查询） |
| email | String | 否 | 邮箱（模糊查询） |

### 请求示例

```json
{
  "name": "张",
  "tel": "138",
  "email": "example"
}
```

**cURL 示例**:
```bash
curl -X POST "http://your-domain/parent/unapprovedTeachers" \
  -H "Authorization: Bearer your-token" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "张",
    "tel": "138",
    "email": "example"
  }'
```

**JavaScript/Fetch 示例**:
```javascript
fetch('/parent/unapprovedTeachers', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer ' + token,
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    name: '张',
    tel: '138',
    email: 'example'
  })
})
.then(response => response.json())
.then(data => console.log(data));
```

### 响应示例

**成功响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "userAccount": "teacher001",
      "username": "张老师",
      "sex": "1",
      "age": 30,
      "password": null,
      "tel": "13800138000",
      "country": "CHN",
      "email": "teacher001@example.com",
      "grade": null,
      "school": "XX中学",
      "type": 1,
      "approvalStatus": 0,
      "createAt": "2024-01-01 10:00:00",
      "createBy": null,
      "updateAt": "2024-01-01 10:00:00",
      "updateBy": null,
      "deleteFlag": "0"
    }
  ]
}
```

**错误响应**:
```json
{
  "code": 403,
  "message": "无权限，仅管理员可操作",
  "data": null
}
```

### 响应字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Integer | 老师ID |
| userAccount | String | 用户账号 |
| username | String | 用户名 |
| sex | String | 性别：1-男，2-女 |
| age | Integer | 年龄 |
| password | String | 密码（返回时已隐藏，值为null） |
| tel | String | 电话 |
| country | String | 国家 |
| email | String | 邮箱 |
| grade | String | 年级 |
| school | String | 学校 |
| type | Integer | 类型：0-家长，1-老师 |
| approvalStatus | Integer | 审批状态：0-未审批，1-已审批 |
| createAt | String | 创建时间 |
| createBy | String | 创建人 |
| updateAt | String | 更新时间 |
| updateBy | String | 更新人 |
| deleteFlag | String | 删除标志：0-正常，1-删除 |

---

## 2. 查询已审批通过的老师列表

### 接口信息

- **接口路径**: `/parent/approvedTeachers`
- **请求方式**: `POST`
- **权限要求**: 管理员权限（type=9）
- **功能描述**: 查询所有已审批通过的老师列表，支持按名称、电话、邮箱进行模糊查询

### 请求头

```
Authorization: Bearer {token}
Content-Type: application/json
```

### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | String | 否 | 老师名称或账号（模糊查询） |
| tel | String | 否 | 电话（模糊查询） |
| email | String | 否 | 邮箱（模糊查询） |

### 请求示例

```json
{
  "name": "李",
  "tel": "139",
  "email": "teacher"
}
```

**cURL 示例**:
```bash
curl -X POST "http://your-domain/parent/approvedTeachers" \
  -H "Authorization: Bearer your-token" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "李",
    "tel": "139",
    "email": "teacher"
  }'
```

**JavaScript/Fetch 示例**:
```javascript
fetch('/parent/approvedTeachers', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer ' + token,
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    name: '李',
    tel: '139',
    email: 'teacher'
  })
})
.then(response => response.json())
.then(data => console.log(data));
```

### 响应示例

**成功响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 2,
      "userAccount": "teacher002",
      "username": "李老师",
      "sex": "2",
      "age": 28,
      "password": null,
      "tel": "13900139000",
      "country": "CHN",
      "email": "teacher002@example.com",
      "grade": null,
      "school": "YY中学",
      "type": 1,
      "approvalStatus": 1,
      "createAt": "2024-01-02 10:00:00",
      "createBy": null,
      "updateAt": "2024-01-03 15:30:00",
      "updateBy": "admin",
      "deleteFlag": "0"
    }
  ]
}
```

**错误响应**:
```json
{
  "code": 403,
  "message": "无权限，仅管理员可操作",
  "data": null
}
```

### 响应字段说明

响应字段与"查询未审批的老师列表"接口相同，主要区别在于 `approvalStatus` 字段值为 `1`（已审批）。

---

## 权限说明

两个接口都需要管理员权限，系统会通过以下方式校验：

1. 从请求头 `Authorization` 中获取 JWT token
2. 验证 token 有效性
3. 从 token 中获取用户名
4. 查询用户信息，验证 `type` 字段是否为 `9`（管理员）
5. 如果验证失败，返回 403 错误

## 注意事项

1. **密码字段**: 返回结果中的 `password` 字段始终为 `null`，系统会自动隐藏敏感信息
2. **查询条件**: 所有查询参数都是可选的，可以只传部分参数，也可以不传任何参数（查询全部）
3. **模糊查询**: `name` 参数会同时匹配 `username` 和 `userAccount` 字段
4. **排序**: 结果按创建时间倒序排列（最新的在前）
5. **过滤条件**: 系统自动过滤已删除的记录（`delete_flag = '0'`）

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 403 | 无权限，仅管理员可操作 |
| 401 | 未登录或token无效 |
| 500 | 服务器内部错误 |

