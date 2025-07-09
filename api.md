

#### EX:

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
```

#### 返回样例

```json
https://www.volcengine.com/docs/82379/1298454  参照 响应参数-流式调用

```

### 2.注册

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


### 3.登录

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
  "password": "YI37nVty2I0MSPRCk1Nh4A=="
}
```

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
    }
}

```

### 4.创建会话，获取上下文id

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
### 5.带上下文的问答接口

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


### 6.新增家长用户

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

### 7.验证码发送

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

### 8.验证码校验

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

### 9.账号校验是否存在

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





### 12.根据用户id查询对话列表

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

### 13.根据conversationId查询对话详情

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
