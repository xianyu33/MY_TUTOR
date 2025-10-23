
# MY_TUTOR API 接口文档

## 系统概述

MY_TUTOR 是一个智能数学学习系统，提供完整的数学知识点学习、测试、统计等功能。**现已支持中法双语显示和知识点图标功能。**

## 多语言支持

### 支持的语言
- `zh` - 中文（默认）
- `fr` - 法语

### 多语言API接口
系统提供专门的多语言API接口，位于 `/api/multilingual/` 路径下：
- `GET /api/multilingual/categories` - 获取分类列表（多语言）
- `GET /api/multilingual/knowledge-points` - 获取知识点列表（多语言）
- `GET /api/multilingual/questions` - 获取题目列表（多语言）
- `GET /api/multilingual/languages` - 获取支持的语言列表

### 语言参数使用
所有多语言API接口都支持通过 `language` 参数指定语言：
- 默认语言：中文 (`zh`)
- 无效语言代码会自动回退到默认语言
- 法语内容为空时自动回退到中文内容

### 知识点图标功能
知识点支持图标显示：
- `iconUrl` - 图标文件URL路径
- `iconClass` - 图标CSS类名
- 优先使用图片图标，回退到CSS图标

## 接口说明

### 认证说明

```json
除登录注册外，所有接口需携带请求头：
Authorization，
例如：
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5aXlhbyIsImlhdCI6MTc0NTM5ODEzOSwiZXhwIjoxNzQ1NDg0NTM5fQ.-6_J41io0Drj6EEHyJg5xh5GyMrh2s3Kkwu-tx-nfMJe8oGxUzknuqkXMOKDqEmMQXCKoIS5N_h3SCt3d7WnYA
```



### 1.问答接口

#### url

```
39.106.32.28:9009/chat
```

#### 请求方式

```
POST
```

#### 请求格式

```
application/json
```

#### 入参样例

```json
{
  "stream": true,
  "stream_options": {
    "include_usage": true
  },
  "messages": [
    {
      "role": "user", // user 用户消息 用户提问 
      "content": "今天是星期几"
    }
  ],
  "user_id": "123124131",
  "conversation_id": "07227fb3-b5a9-4b94-a0f8-f83337222a2b"   //首次对话无该字段，后续对话传入该字段，代表是一次对话
}
-- 带图片的入参方式
{
  "stream": false,
  "messages": [
    {
      "role": "system",
      "content": "你是一个老师角色，不仅局限于学科问题回答，正常的问答也支持，英语回答"
    },
    {
      "content": "42配置信息",
      "role": "user"
    },
    {
      "role": "user",
      "content": [
        {
          "type": "image_url",
          "image_url" : {
            "url": "https://test-yy.tos-cn-shanghai.volces.com/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20250714104219.png?X-Tos-Algorithm=TOS4-HMAC-SHA256&X-Tos-Credential=AKLTMjdmZDRlZmU4YmQxNDZhZDlhYjkyMmJlYzMwNmFlNjc%2F20250714%2Fcn-shanghai%2Ftos%2Frequest&X-Tos-Expires=3600&X-Tos-Date=20250714T024320Z&X-Tos-Signature=517aeb0f3cb60e283ab2cdd724aa72f6301ac9ceecf86e0191a3e958a191f951&X-Tos-SignedHeaders=host"
          }
        }
      ]
    }
  ],
  "user_id": 11
}




```

#### 返回样例

```json
https://www.volcengine.com/docs/82379/1298454  参照 响应参数-流式调用

```

### 2.注册
#### 学生注册
#### url

```
39.106.32.28:9009/user/register
```

#### 请求方式

```
POST
```

#### 请求格式

```
application/json
```

#### 入参样例

```json
{
  "userAccount": "yy23", //用户账号
  "username": "yy", //用户名
  "sex": "1", //性别  1-男 2-女
  "age": 10, //年龄
  "password": "YI37nVty2I0MSPRCk1Nh4A==", //密码  aes加密
  "tel": "15892333333", //电话
  "country": "CHN", //国家
  "email": "100049846@qq.com",  //email
  "grade": "9"//年级 
}

```

#### 返回样例

```json
{
  "code": 200,
  "message": "注册成功",
  "data": true
}

```
#### 家长/老师注册
#### url

```
39.106.32.28:9009/parent/add
```

#### 请求方式

```
POST
```

#### 请求格式

```
application/json
```

#### 入参样例

```json
{
  "userAccount": "yy23", //用户账号
  "username": "yy", //用户名
  "sex": "1", //性别  1-男 2-女
  "age": 10, //年龄
  "password": "YI37nVty2I0MSPRCk1Nh4A==", //密码  aes加密
  "tel": "15892333333", //电话
  "country": "CHN", //国家
  "email": "100049846@qq.com",  //email
  "grade": "9"//年级 ,
  "type": "0" //0-家长 1-老师
}

```

#### 返回样例

```json
{
  "code": 200,
  "message": "注册成功",
  "data": true
}

```


### 3.获取登录验证码

#### url

```
39.106.32.28:9009/user/getLoginCaptcha
```

#### 请求方式

```
POST
```

#### 请求格式

```
application/json
```

#### 入参样例

```json
{}
```

#### 返回样例

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

**说明：**
- 验证码为4位数字
- 验证码有效期为5分钟
- `captcha`: 验证码值，用于后端验证
- `captchaId`: 验证码唯一标识符，用于登录时验证
- `captchaImage`: base64编码的验证码图片，用于前端显示
- 验证码存储在Redis中，key格式为：`CAPTCHA:{captchaId}`

### 4.用户登录

#### url

```
39.106.32.28:9009/user/login
```

#### 请求方式

```
POST
```

#### 请求格式

```
application/json
```

#### 入参样例

```json
{
  "userAccount": "yy",
  "password": "YI37nVty2I0MSPRCk1Nh4A==",
  "captcha": "1234",
  "captchaId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}
```

**参数说明：**
- `userAccount`: 用户账号
- `password`: 密码（AES加密）
- `captcha`: 验证码（4位数字）
- `captchaId`: 验证码唯一标识符（从获取验证码接口返回）

**错误处理：**
- 验证码ID为空：`验证码ID不能为空`
- 验证码为空：`验证码不能为空`
- 验证码过期：`验证码已过期，请重新获取`
- 验证码错误：`验证码错误`
- 用户名或密码错误：`Incorrect username or password`

#### 返回样例

```json
{
    "code": 200,
    "message": "登录成功",
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
        "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5aXlhbyIsImlhdCI6MTc0NTQ2Njk1MCwiZXhwIjoxNzQ1NDg4NTUwfQ.W0xCZKnVxl19I9xKeJV5gAo6BjJgLfygo75m4AJb8VoyakdpSZHW_UZzv0-qKUGfIkZjfo2i0blQhDMKZsHX2w"
        "role": "P"  //P-家长  S-学生
    }
}

```

### 5.创建会话，获取上下文id

#### url

```
39.106.32.28:9009/context
```

#### 请求方式

```
POST
```

#### 请求格式

```
application/json
```

#### 入参样例

```json
{}
```

#### 返回样例

```json
{
  "code": 200,
  "data": "f559eccf-7e6d-4885-bae9-5665e9953ae8",
  "message": null
}

```
### 6.带上下文的问答接口

#### url

```
39.106.32.28:9009/context/chat
```

#### 请求方式

```
POST
```

#### 请求格式

```
application/json
```

#### 入参样例

```json
{
  "messages": [
    {
      "content": "今天什么日子",
      "role": "user"
    }
  ],
  "stream": true,  //是否流式
  "context_id": "ctx-20250421140817-ltlzk",  //4.创建会话，获取上下文id返回的id
  "enc_param": "12312jdkiasjdoijd2o3jrf2w....." //post请求的所有参数转JSON字符串后AES加密串
  //样例:{"context_id":"ctx-20250422164821-tdjcw","messages":[{"content":"上一次的问题是什么","role":"user"}],"stream":true} 这个字符串加密后
  //c+BgdZrd5g3UhMHNQlENqghSxnlyEDMP/BqqVMzDxCA5z0b4Rr8LTGjZphLIR9BfIwhjTQeXgCklLz2DcSb0jo2aP9W0e/kPFD/Ez1TzmP+XpP4Cvs5hVU9mFhvoW7EPM29j2AakyR1tEbCC2ukpAKehlZVCi+oY2iKLLqWj2IY=
}
```
#### 实际入参样例
```json
{
    "enc_param": "c+BgdZrd5g3UhMHNQlENqghSxnlyEDMP/BqqVMzDxCA5z0b4Rr8LTGjZphLIR9BfIwhjTQeXgCklLz2DcSb0jo2aP9W0e/kPFD/Ez1TzmP+XpP4Cvs5hVU9mFhvoW7EPM29j2AakyR1tEbCC2ukpAKehlZVCi+oY2iKLLqWj2IY="
}
```

#### 返回样例

```json
https://www.volcengine.com/docs/82379/1298454  参照 响应参数-流式调用

```


### 7.新增家长用户

#### url

```
/parent/addWithUsers
```

#### 请求方式

```
POST
```

#### 请求格式

```
application/json
```

#### 入参样例

```json
{
  "parent": {
    "userAccount": "parent002",
    "username": "李四",
    "sex": "2",
    "age": 38,
    "password": "abcdef",
    "tel": "13900000000",
    "country": "china",
    "email": "parent002@example.com",
    "grade": "无"
  },
  "users": [
    {
      "userAccount": "student001",
      "username": "李小四",
      "sex": "1",
      "age": 10,
      "password": "stu123",
      "tel": "13900000001",
      "country": "china",
      "email": "student001@example.com",
      "grade": "7"
    },
    {
      "userAccount": "student002",
      "username": "李小五",
      "sex": "2",
      "age": 8,
      "password": "stu456",
      "tel": "13900000002",
      "country": "中国",
      "email": "student002@example.com",
      "grade": "7"
    }
  ]
}

```
#### 返回样例
```json
  {
  "code": 200,
  "msg": "新增家长和学生成功",
  "data": true
  }
```

### 8.验证码发送

#### url

```
/user/verificationCode
```

#### 请求方式

```
POST
```

#### 请求格式

```
application/json
```

#### 入参样例

```json
{
  "email": "229268931@qq.com"
}

```
#### 返回样例
```json
{
  "code": 200,
  "message": "发送成功",
  "data": null
}
```

### 9.验证码校验

#### url

```
/user/verification
```

#### 请求方式

```
POST
```

#### 请求格式

```
application/json
```

#### 入参样例

```json
{
  "email": "229268931@qq.com",
  "verificationCode": "753882"
}

```
#### 实际入参样例
```json
{
  "code": 200,
  "message": "验证通过",
  "data": null
}

```

### 10.账号校验是否存在

#### url

```
/user/existAccount
```

#### 请求方式

```
POST
```

#### 请求格式

```
application/json
```

#### 入参样例

```json
{
  "userAccount": "yyy"
}

```
#### 返回样例
```json
# 不可创建
{
  "code": 500,
  "message": "账号已存在",
  "data": null
}

# 可创建
{
"code": 200,
"message": "账号可使用",
"data": null
}


```





### 13.根据用户id查询对话列表

查询用户的对话列表
#### url

```
/chat/message/user/{userId}
```

#### 请求方式

```
GET
```


#### 返回
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 3,
      "conversationId": "23699ff5-04a4-4555-abe4-9043972d0f85",
      "userId": "123124131",
      "type": null,
      "context": null,
      "createTime": "2025-07-08 01:39:51",
      "title": "9年级数据学习的内容"
    },
    {
      "id": 4,
      "conversationId": "07227fb3-b5a9-4b94-a0f8-f83337222a2b",
      "userId": "123124131",
      "type": null,
      "context": null,
      "createTime": "2025-07-08 05:48:23",
      "title": "9年级数据学习的内容"
    }
  ]
}


```

### 14.根据conversationId查询对话详情

查询用户的对话列表
#### url

```
/chat/message-detail/conversation/{conversationId}
```

#### 请求方式

```
GET
```


#### 返回
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 5,
      "userId": "123124131",
      "sort": null,
      "type": "q",  //回答类型  a:回答 q:问题
      "context": "9年级数据学习的内容",      //内容
      "createTime": "2025-07-08 13:48:06",
      "conversationId": "07227fb3-b5a9-4b94-a0f8-f83337222a2b",
      "messageId": null
    },
    {
      "id": 6,
      "userId": "123124131",
      "sort": null,
      "type": "a",      //回答类型  a:回答 q:问题
      "context": "data:{\"id\":\"021751982503622cfdb5492872ab5355d14cd98fab21436a7ece9\",\"choices\":[{\"delta\":{\"content\":\"In\",\"role\":\"assistant\"},\"index\":0}],\"created\":1751982503,\"model\":\"doubao-lite-32k-240828\",\"object\":\"chat.completion.chunk\",\"metadata\":{}}",
      "createTime": "2025-07-08 13:48:09",
      "conversationId": "07227fb3-b5a9-4b94-a0f8-f83337222a2b",
      "messageId": null
    }
  ]
}


```

### 15.家长查看对话列表（包含自己和孩子的所有列表）

家长查看对话列表（包含自己和孩子的所有列表）
根据登录判断是否为家长，然后判断是否调用该接口
#### url

```
/chat/message/parent/{parentId}
```

#### 请求方式

```
GET
```


#### 返回
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "yours": {
      "id": 25,
      "userAccount": "yyy",
      "username": "yyy",
      "sex": "1",
      "age": 18,
      "password": "e10adc3949ba59abbe56e057f20f883e",
      "tel": "15892334013",
      "country": "Calanda",
      "email": "229268931@qq.com",
      "grade": "11",
      "createAt": "2025-07-13 09:03:33",
      "deleteFlag": "0",
      "chatMessages": [
        {
          "id": 74,
          "conversationId": "be92b9b1-ed54-4091-81d3-63813bb6fa0f",
          "userId": "25",
          "createTime": "2025-07-13 01:18:45",
          "title": "how are you"
        },
        {
          "id": 75,
          "conversationId": "f260f0f0-a240-4b7c-b054-9d9661fe544b",
          "userId": "25",
          "createTime": "2025-07-13 01:21:07",
          "title": "三角函数"
        },
        {
          "id": 76,
          "conversationId": "1ce905d8-ed89-4114-a161-ea9667c25d63",
          "userId": "25",
          "createTime": "2025-07-13 01:21:24",
          "title": "ok ,how are you"
        }
      ]
    },
    "jane": {
      "id": 27,
      "userAccount": "jane",
      "username": "jane",
      "sex": "2",
      "age": 10,
      "password": "e10adc3949ba59abbe56e057f20f883e",
      "tel": "15892334013",
      "country": "calanda",
      "email": "",
      "grade": "9",
      "createAt": "2025-07-13 09:03:33",
      "updateAt": "2025-07-13 09:03:33",
      "deleteFlag": "0",
      "chatMessages": [
        {
          "id": 73,
          "conversationId": "97a446cd-0ddf-4571-9d22-4a2e924c8959",
          "userId": "27",
          "createTime": "2025-07-13 01:03:46",
          "title": "who are you",
          "username": "jane"
        }
      ]
    }
  }
}


```

### 16.上传文件

上传文件，对话使用
#### url

```
/update
```

#### 请求方式

```
POST
```
from-data
#### 入参
```json
file
```


#### 返回
```json
{
  "code": 200,
  "message": "上传成功",
  "data": "https://test-yy.tos-cn-shanghai.volces.com/%E5%B1%9E%E5%9C%B0%E5%8C%96%E4%BF%A1%E6%81%AF.txt?X-Tos-Algorithm=TOS4-HMAC-SHA256&X-Tos-Credential=AKLTMjdmZDRlZmU4YmQxNDZhZDlhYjkyMmJlYzMwNmFlNjc%2F20250714%2Fcn-shanghai%2Ftos%2Frequest&X-Tos-Expires=3600&X-Tos-Date=20250714T023329Z&X-Tos-Signature=7b959d8226f48564b2470573c771a6e15dcce1c774dc1abf9191e048b4e16eb8&X-Tos-SignedHeaders=host"
}

```


### 17.根据家长id查询学生信息

#### url

```
/parent/findChild
```

#### 请求方式

```
POST
```
#### 入参
```json
{
  "id": 18
}
```


#### 返回
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 20,
      "userAccount": "222",
      "username": "2",
      "sex": "1",
      "age": 10,
      "password": "1f8dfa2cb79ed9b036de542847b30dbb",
      "tel": "2",
      "country": "2",
      "email": "",
      "grade": "2",
      "createAt": "2025-07-12 09:09:28",
      "updateAt": "2025-07-12 09:09:28",
      "deleteFlag": "0"
    },
    {
      "id": 21,
      "userAccount": "333",
      "username": "3",
      "sex": "1",
      "age": 10,
      "password": "1f8dfa2cb79ed9b036de542847b30dbb",
      "tel": "3",
      "country": "3",
      "email": "",
      "grade": "3",
      "createAt": "2025-07-12 09:09:28",
      "updateAt": "2025-07-12 09:09:28",
      "deleteFlag": "0"
    }
  ]
}

```


### 18.查询个人信息

#### url

```
/user/findById
```

#### 请求方式

```
POST
```
#### 入参
```json
{
  "id": 18,
  "role": "S"    //S-学生   P-家长
}
```
#### 返回
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 27,
    "userAccount": "jane",
    "username": "jane",
    "sex": "2",
    "age": 10,
    "tel": "15892334013",
    "country": "calanda",
    "email": "",
    "grade": "9",
    "createAt": "2025-07-13 09:03:33",
    "updateAt": "2025-07-13 09:03:33",
    "deleteFlag": "0"
  }
}

```
### 19.更新个人信息

#### url

```
/user/edit
```

#### 请求方式

```
POST
```
#### 入参
```json
{
  "id": 27,
  "userAccount": "jane",
  "username": "jane",
  "sex": "2",
  "age": 10,
  "tel": "15892334013",
  "country": "calanda",
  "email": "jane@qq.com",
  "grade": "9",
  "role": "S"    //S-学生   P-家长
}
```
#### 返回
```json
{
  "code": 200,
  "message": "更新成功"
}

```

## 多语言API接口示例

### 1. 获取知识点分类列表（多语言）

#### url
```
GET /api/multilingual/categories?language=fr
```

#### 返回样例
```json
{
  "code": 200,
  "message": "获取分类列表成功",
  "data": [
    {
      "id": 1,
      "categoryCode": "NUM_ALG",
      "categoryName": "Nombres et algèbre",
      "description": "Reconnaissance des nombres, opérations, expressions algébriques, etc.",
      "sortOrder": 1
    }
  ]
}
```

### 2. 获取知识点列表（多语言）

#### url
```
GET /api/multilingual/knowledge-points?gradeId=1&language=fr
```

#### 返回样例
```json
{
  "code": 200,
  "message": "获取知识点列表成功",
  "data": [
    {
      "id": 1,
      "pointCode": "NUM_001",
      "pointName": "Reconnaissance des nombres",
      "description": "Reconnaissance des nombres de 1 à 100",
      "content": "Apprendre la lecture, l'écriture et la comparaison des nombres de 1 à 100",
      "learningObjectives": "Maîtriser les nombres de 1 à 100",
      "iconUrl": "/icons/numbers.png",
      "iconClass": "icon-numbers",
      "difficultyLevel": 1,
      "gradeId": 1,
      "categoryId": 1
    }
  ]
}
```

### 3. 获取题目列表（多语言）

#### url
```
GET /api/multilingual/questions?knowledgePointId=1&language=fr
```

#### 返回样例
```json
{
  "code": 200,
  "message": "获取题目列表成功",
  "data": [
    {
      "id": 1,
      "questionType": 1,
      "questionTitle": "Reconnaissance des nombres",
      "questionContent": "Quel est le plus grand nombre parmi les suivants ?",
      "options": "[\"A. 15\", \"B. 25\", \"C. 35\", \"D. 45\"]",
      "correctAnswer": "D",
      "answerExplanation": "45 est le plus grand nombre",
      "difficultyLevel": 1,
      "points": 1,
      "knowledgePointId": 1
    }
  ]
}
```

### 4. 获取支持的语言列表

#### url
```
GET /api/multilingual/languages
```

#### 返回样例
```json
{
  "code": 200,
  "message": "获取支持的语言列表成功",
  "data": [
    {
      "code": "zh",
      "name": "中文",
      "nameEn": "Chinese"
    },
    {
      "code": "fr",
      "name": "Français",
      "nameEn": "French"
    }
  ]
}
```

## 学生测试功能（多语言）

### 1. 生成随机测试（多语言）

#### url
```
POST /api/student-test/generate-random
```

#### 请求参数
```
studentId=1&gradeId=9&difficultyLevel=2&questionCount=15&language=fr
```

#### 返回样例
```json
{
  "code": 200,
  "message": "生成随机测试成功",
  "data": {
    "id": 1,
    "studentId": 1,
    "testId": 1,
    "testName": "Test aléatoire_1703123456789",
    "testNameFr": "Test aléatoire_1703123456789",
    "startTime": "2023-12-21 10:30:00",
    "totalQuestions": 15,
    "totalPoints": 15,
    "testStatus": 1
  }
}
```

### 2. 提交答案（多语言）

#### url
```
POST /api/student-test/submit-answer
```

#### 请求参数
```
testRecordId=1&questionId=1&studentAnswer=A&language=fr
```

#### 返回样例
```json
{
  "code": 200,
  "message": "提交答案成功",
  "data": {
    "id": 1,
    "testRecordId": 1,
    "questionId": 1,
    "questionContent": "Quel est le plus grand nombre ?",
    "questionContentFr": "Quel est le plus grand nombre ?",
    "correctAnswer": "D",
    "correctAnswerFr": "D",
    "studentAnswer": "A",
    "studentAnswerFr": "A",
    "isCorrect": 0,
    "points": 1,
    "earnedPoints": 0,
    "answerTime": "2023-12-21 10:35:00"
  }
}
```

## 注意事项

1. **多语言支持**: 所有多语言API都支持 `language` 参数，无效语言会自动回退到默认语言
2. **数据一致性**: 确保中法双语内容在语义上保持一致
3. **图标支持**: 知识点支持图标显示，提供更好的视觉体验
4. **回退机制**: 法语内容为空时自动回退到中文内容
5. **性能考虑**: 多语言字段会增加存储空间，建议合理使用索引
6. **扩展性**: 可以轻松添加其他语言支持（如英语、西班牙语等）

## 相关文档

- **数学知识点API**: 详见 `MATH_API_DOCUMENTATION.md`
- **学生测试功能API**: 详见 `STUDENT_TEST_API_DOCUMENTATION.md`
- **多语言支持**: 详见 `MULTILINGUAL_SUPPORT_README.md`
- **知识点图标**: 详见 `KNOWLEDGE_POINT_ICONS_README.md`
- **数据库架构**: 详见 `DATABASE_SCHEMA_README.md`
