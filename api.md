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
      "role": "system", //角色 system 系统消息，比如提示词
      "content": "你是一个老师，英语回答"
    },
    {
      "role": "user", // user 用户消息 用户提问 
      "content": "9年级数据学习的内容"
    }
  ]
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
    "user": {
      "id": 4,
      "userAccount": "yy",
      "username": "yy",
      "sex": "sex_33ac30c1791c",
      "age": 10,
      "password": null,
      "tel": "tel_973d448759cc",
      "country": "country_622039f7e0c8",
      "email": "email_047505e4e480",
      "grade": "grade_1f8d76bf5c32",
      "createAt": "2025-04-14 09:12:46",
      "createBy": null,
      "updateAt": "2025-04-14 09:12:46",
      "updateBy": null,
      "deleteFlag": "0"
    },
    "token": {
      "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5eSIsImV4cCI6MTc0NDcwODM3MCwiaWF0IjoxNzQ0NjIxOTcwfQ.doDqtsAVjZ1wjadEMUksS1VpqZ-8Ea4k1PfG4kMWjZSTwsrJjm1EM0pTFGa2pbKdftfXBbUTjDHa5SnelYvjNw",
      "tokenType": "Bearer"
    }
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
  "id": "ctx-20250421142907-q2ttn", //对话的时候传入，作为上下文的缓存
  "model": "ep-20250421140255-d6sfx",
  "ttl": 3600,
  "truncation_strategy": {
    "type": "rolling_tokens",
    "rolling_tokens": true
  },
  "usage": {
    "prompt_tokens": 113,
    "completion_tokens": 0,
    "total_tokens": 113,
    "prompt_tokens_details": {
      "cached_tokens": 0
    }
  },
  "mode": "session"
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
