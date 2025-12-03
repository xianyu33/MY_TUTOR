# 登录验证码功能说明

## 功能概述

为了增强系统安全性，新增了登录验证码功能。用户在登录时需要输入4位数字验证码，验证码有效期为5分钟。

## 实现细节

### 1. 验证码生成
- 使用 `CaptchaUtil.generateCaptcha()` 方法生成4位随机数字验证码
- 使用 `UUID.randomUUID()` 生成唯一的验证码标识符
- 验证码存储在Redis中，key格式为：`CAPTCHA:{captchaId}`
- 验证码有效期为5分钟（300秒）

### 2. 验证码验证
- 登录时验证验证码ID和验证码值
- 验证通过后立即删除Redis中的验证码，防止重复使用
- 验证失败时返回相应的错误信息

### 3. 安全配置
- 新增接口 `/user/getLoginCaptcha` 已添加到安全配置的permitAll列表中
- 登录接口 `/user/login` 保持permitAll权限

## API接口

### 获取登录验证码
```
POST /user/getLoginCaptcha
```

**请求参数：** 无

**返回示例：**
```json
{
  "code": 200,
  "message": "获取验证码成功",
  "data": {
    "captcha": "1234",
    "captchaId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "captchaImage": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAHgAAAAoCAYAAAA16j4lAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAB2AAAAdgB+lymcgAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAANCSURBVHic7Z1NaBNBFMefJ..."
  }
}
```

**字段说明：**
- `captcha`: 验证码值（4位数字），用于后端验证
- `captchaId`: 验证码唯一标识符（UUID），用于登录时验证
- `captchaImage`: base64编码的验证码图片，用于前端显示

### 用户登录（带验证码）
```
POST /user/login
```

**请求参数：**
```json
{
  "userAccount": "yy",
  "password": "YI37nVty2I0MSPRCk1Nh4A==",
  "captcha": "1234",
  "captchaId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}
```

**返回示例：**
```json
{
  "code": 200,
  "message": "login success",
  "data": {
    "id": 9,
    "userAccount": "yyy",
    "username": "yiyao",
    "sex": "1",
    "age": 18,
    "password": null,
    "tel": "15892334013",
    "country": "calada",
    "email": "100049846@qq.com",
    "grade": "9",
    "createAt": "2025-04-21 01:38:58",
    "createBy": null,
    "updateAt": "2025-04-21 01:38:58",
    "updateBy": null,
    "deleteFlag": "0",
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5aXlhbyIsImlhdCI6MTc0NTQ2Njk1MCwiZXhwIjoxNzQ1NDg4NTUwfQ.W0xCZKnVxl19I9xKeJV5gAo6BjJgLfygo75m4AJb8VoyakdpSZHW_UZzv0-qKUGfIkZjfo2i0blQhDMKZsHX2w",
    "role": "P"
  }
}
```

## 错误处理

| 错误情况 | 错误信息 |
|---------|---------|
| 验证码ID为空 | `验证码ID不能为空` |
| 验证码为空 | `验证码不能为空` |
| 验证码过期 | `验证码已过期，请重新获取` |
| 验证码错误 | `验证码错误` |
| 用户名或密码错误 | `Incorrect username or password` |

## 前端集成建议

1. **获取验证码流程：**
   - 调用 `/user/getLoginCaptcha` 接口
   - 从返回的data中获取 `captcha`（验证码值）和 `captchaId`（验证码ID）
   - 在页面上显示验证码供用户输入
   - 保存captchaId用于后续登录验证

2. **登录流程：**
   - 用户输入用户名、密码和验证码
   - 调用 `/user/login` 接口，传入用户名、密码、验证码和captchaId
   - 根据返回结果处理登录成功或失败

3. **用户体验优化：**
   - 验证码过期时自动刷新
   - 登录失败时提供清晰的错误提示
   - 考虑添加验证码刷新按钮
   - 验证码输入框可以自动聚焦

## 技术实现

### 新增文件
- `src/main/java/com/yy/my_tutor/util/CaptchaUtil.java` - 验证码工具类

### 修改文件
- `src/main/java/com/yy/my_tutor/user/controller/UserController.java` - 添加验证码相关接口
- `src/main/java/com/yy/my_tutor/user/domain/User.java` - 添加验证码相关字段
- `src/main/java/com/yy/my_tutor/security/BrowserSecurityConfig.java` - 更新安全配置
- `src/main/java/com/yy/my_tutor/config/RedisUtil.java` - 修复字符串存储问题

### 依赖要求
- Redis - 用于存储验证码
- Spring Boot - 基础框架
- Lombok - 简化代码

## 问题修复

### Redis存储问题
**问题描述：** Redis存储的字符串值多了双引号，如 `"1234"` 而不是 `1234`

**原因分析：** RedisUtil的get方法对所有对象都进行了JSON序列化，导致字符串被重复序列化

**解决方案：** 
- 修改RedisUtil.get()方法，对字符串类型直接返回，避免JSON序列化
- 修改RedisUtil.set()方法，对字符串类型直接存储，避免不必要的处理

**修复后的效果：**
- 存储：`1234` → Redis中存储为 `1234`
- 获取：Redis中 `1234` → 返回 `1234`
- 不再有多余的双引号问题
