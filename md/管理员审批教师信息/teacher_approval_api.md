# 教师审批管理接口文档

## 接口说明

本文档包含两个接口，用于管理员对教师注册申请进行审批管理。所有接口都需要管理员权限（type=9）。

---

## 接口1：查询未审批的教师

### 基本信息

**接口地址**: `POST /parent/unapprovedTeachers`

**功能说明**: 查询所有未审批的教师列表，支持按名称、电话、邮箱进行模糊查询

**请求方式**: `POST`

**Content-Type**: `application/json`

**权限要求**: 需要管理员权限（type=9），需要在请求头中携带有效的JWT Token

### 请求头

```
Authorization: Bearer <JWT_TOKEN>
```

### 请求参数

```json
{
  "name": "张",
  "tel": "138",
  "email": "example"
}
```

### 参数说明

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | String | 否 | 教师名称或账号（模糊查询，匹配username或userAccount字段） |
| tel | String | 否 | 电话号码（模糊查询） |
| email | String | 否 | 邮箱地址（模糊查询） |

**注意**: 
- 所有查询参数都是可选的，可以只提供部分参数进行筛选
- 所有查询都是模糊匹配（LIKE查询）
- 如果所有参数都为空，则返回所有未审批的教师
- 多个条件之间是AND关系

### 响应示例

#### 成功响应

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 101,
      "userAccount": "teacher001",
      "username": "张老师",
      "sex": "男",
      "age": 35,
      "password": null,
      "tel": "13800138001",
      "country": "中国",
      "email": "zhang@example.com",
      "grade": null,
      "school": "第一中学",
      "type": 1,
      "approvalStatus": 0,
      "createAt": "2023-12-20 10:30:00",
      "createBy": "system",
      "updateAt": "2023-12-20 10:30:00",
      "updateBy": null,
      "deleteFlag": "0"
    },
    {
      "id": 102,
      "userAccount": "teacher002",
      "username": "李老师",
      "sex": "女",
      "age": 28,
      "password": null,
      "tel": "13900139002",
      "country": "中国",
      "email": "li@example.com",
      "grade": null,
      "school": "第二中学",
      "type": 1,
      "approvalStatus": 0,
      "createAt": "2023-12-21 14:20:00",
      "createBy": "system",
      "updateAt": "2023-12-21 14:20:00",
      "updateBy": null,
      "deleteFlag": "0"
    }
  ]
}
```

#### 空结果响应

```json
{
  "code": 200,
  "message": "操作成功",
  "data": []
}
```

### 响应字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Integer | 教师ID |
| userAccount | String | 用户账号 |
| username | String | 用户名 |
| sex | String | 性别 |
| age | Integer | 年龄 |
| password | String | 密码（返回时已隐藏，值为null） |
| tel | String | 电话号码 |
| country | String | 国家 |
| email | String | 邮箱地址 |
| grade | String | 年级（教师通常为null） |
| school | String | 学校 |
| type | Integer | 类型（1-老师） |
| approvalStatus | Integer | 审批状态（0-未审批，1-已审批） |
| createAt | String | 创建时间 |
| createBy | String | 创建人 |
| updateAt | String | 更新时间 |
| updateBy | String | 更新人 |
| deleteFlag | String | 删除标志（0-未删除，1-已删除） |

### 使用示例

#### cURL

```bash
# 查询所有未审批的教师
curl -X POST http://localhost:8080/parent/unapprovedTeachers \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{}'

# 按名称查询
curl -X POST http://localhost:8080/parent/unapprovedTeachers \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "张"
  }'

# 按电话查询
curl -X POST http://localhost:8080/parent/unapprovedTeachers \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "tel": "138"
  }'

# 组合查询
curl -X POST http://localhost:8080/parent/unapprovedTeachers \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "张",
    "email": "example"
  }'
```

#### JavaScript/Fetch

```javascript
// 查询所有未审批的教师
async function getUnapprovedTeachers(filters = {}) {
  try {
    const response = await fetch('/parent/unapprovedTeachers', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: JSON.stringify(filters)
    });
    
    const result = await response.json();
    if (result.code === 200) {
      console.log('未审批教师列表:', result.data);
      return result.data;
    } else {
      console.error('查询失败:', result.message);
      return [];
    }
  } catch (error) {
    console.error('请求异常:', error);
    return [];
  }
}

// 使用示例
getUnapprovedTeachers({ name: '张' });
```

#### Vue.js

```javascript
// 在Vue组件中使用
export default {
  data() {
    return {
      teachers: [],
      searchForm: {
        name: '',
        tel: '',
        email: ''
      }
    }
  },
  methods: {
    async fetchUnapprovedTeachers() {
      try {
        const response = await this.$http.post('/parent/unapprovedTeachers', this.searchForm, {
          headers: {
            'Authorization': `Bearer ${this.$store.state.token}`
          }
        });
        
        if (response.data.code === 200) {
          this.teachers = response.data.data;
        } else {
          this.$message.error(response.data.message);
        }
      } catch (error) {
        this.$message.error('查询失败');
        console.error(error);
      }
    }
  },
  mounted() {
    this.fetchUnapprovedTeachers();
  }
}
```

### 错误处理

#### 未登录或Token无效

```json
{
  "code": 500,
  "message": "未登录或token无效",
  "data": null
}
```

#### 无权限（非管理员）

```json
{
  "code": 500,
  "message": "无权限，仅管理员可操作",
  "data": null
}
```

---

## 接口2：审批通过教师

### 基本信息

**接口地址**: `POST /parent/approveTeacher/{id}`

**功能说明**: 审批通过指定的教师，将教师的审批状态从0（未审批）更新为1（已审批）

**请求方式**: `POST`

**Content-Type**: `application/json`

**权限要求**: 需要管理员权限（type=9），需要在请求头中携带有效的JWT Token

### 请求头

```
Authorization: Bearer <JWT_TOKEN>
```

### 请求参数

#### Path Parameters

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Integer | 是 | 教师ID（路径参数） |

### 响应示例

#### 成功响应

```json
{
  "code": 200,
  "message": "审批通过成功",
  "data": true
}
```

#### 失败响应

```json
{
  "code": 500,
  "message": "审批失败，老师不存在或已被审批",
  "data": null
}
```

### 使用示例

#### cURL

```bash
curl -X POST http://localhost:8080/parent/approveTeacher/101 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### JavaScript/Fetch

```javascript
async function approveTeacher(teacherId) {
  try {
    const response = await fetch(`/parent/approveTeacher/${teacherId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });
    
    const result = await response.json();
    if (result.code === 200) {
      console.log('审批成功:', result.message);
      return true;
    } else {
      console.error('审批失败:', result.message);
      return false;
    }
  } catch (error) {
    console.error('请求异常:', error);
    return false;
  }
}

// 使用示例
approveTeacher(101);
```

#### Vue.js

```javascript
// 在Vue组件中使用
export default {
  methods: {
    async handleApprove(teacherId) {
      try {
        const response = await this.$http.post(
          `/parent/approveTeacher/${teacherId}`,
          {},
          {
            headers: {
              'Authorization': `Bearer ${this.$store.state.token}`
            }
          }
        );
        
        if (response.data.code === 200) {
          this.$message.success('审批成功');
          // 刷新列表
          this.fetchUnapprovedTeachers();
        } else {
          this.$message.error(response.data.message);
        }
      } catch (error) {
        this.$message.error('审批失败');
        console.error(error);
      }
    }
  }
}
```

### 错误处理

#### 未登录或Token无效

```json
{
  "code": 500,
  "message": "未登录或token无效",
  "data": null
}
```

#### 无权限（非管理员）

```json
{
  "code": 500,
  "message": "无权限，仅管理员可操作",
  "data": null
}
```

#### 教师不存在或已被审批

```json
{
  "code": 500,
  "message": "审批失败，老师不存在或已被审批",
  "data": null
}
```

**注意**: 以下情况会导致审批失败：
- 教师ID不存在
- 教师已被审批（approval_status = 1）
- 教师不是老师类型（type != 1）
- 教师已被删除（delete_flag != '0'）

---

## 业务说明

### 审批流程

1. **教师注册**：
   - 教师注册时，系统自动设置 `type = 1`（老师类型）
   - 系统自动设置 `approval_status = 0`（未审批状态）

2. **查询未审批教师**：
   - 管理员使用 `/parent/unapprovedTeachers` 接口查询所有未审批的教师
   - 支持按名称、电话、邮箱进行筛选
   - 返回结果按创建时间倒序排列（最新的在前）

3. **审批通过**：
   - 管理员使用 `/parent/approveTeacher/{id}` 接口审批通过教师
   - 系统将 `approval_status` 从 0 更新为 1
   - 同时更新 `update_at` 字段为当前时间

### 权限说明

- **管理员类型**: `type = 9`
- **教师类型**: `type = 1`
- **家长类型**: `type = 0`

### 审批状态说明

- `0` - 未审批：教师已注册但尚未通过管理员审批
- `1` - 已审批：教师已通过管理员审批，可以正常使用系统

### 安全说明

1. **密码保护**：
   - 查询未审批教师接口返回的数据中，`password` 字段会被设置为 `null`，不返回实际密码

2. **权限验证**：
   - 所有接口都需要在请求头中携带有效的JWT Token
   - 系统会验证Token的有效性
   - 系统会验证当前用户是否为管理员（type=9）

3. **数据过滤**：
   - 查询接口只返回未审批的教师（`approval_status = 0`）
   - 查询接口只返回未删除的教师（`delete_flag = '0'`）
   - 审批接口只处理老师类型的用户（`type = 1`）

---

## 完整示例

### 场景：管理员审批教师申请

#### 步骤1：查询未审批的教师

```bash
POST /parent/unapprovedTeachers
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json

{
  "name": "张"
}
```

**响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 101,
      "username": "张老师",
      "email": "zhang@example.com",
      "approvalStatus": 0
    }
  ]
}
```

#### 步骤2：审批通过教师

```bash
POST /parent/approveTeacher/101
Authorization: Bearer <ADMIN_TOKEN>
```

**响应**：
```json
{
  "code": 200,
  "message": "审批通过成功",
  "data": true
}
```

#### 步骤3：再次查询验证（该教师不应再出现在未审批列表中）

```bash
POST /parent/unapprovedTeachers
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json

{
  "name": "张"
}
```

**响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": []
}
```

---

## 数据库说明

### parent表相关字段

- `type`: 用户类型（0-家长，1-老师）
- `approval_status`: 审批状态（0-未审批，1-已审批）
- `delete_flag`: 删除标志（'0'-未删除，'1'-已删除）

### 查询条件

查询未审批教师时，系统会应用以下条件：
- `type = 1`（必须是老师）
- `approval_status = 0`（必须是未审批状态）
- `delete_flag = '0'`（必须未删除）

### 更新操作

审批通过教师时，系统会执行：
```sql
UPDATE parent 
SET approval_status = 1, update_at = NOW() 
WHERE id = #{id} AND type = 1 AND delete_flag = '0'
```

---

## 版本历史

- **v1.0** (2023-12-21): 初始版本，支持查询未审批教师和审批通过教师功能

