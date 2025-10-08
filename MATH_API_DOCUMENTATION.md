# 数学知识点学习系统 API 文档

## 系统概述

本系统为数学知识点学习系统，支持不同年级的数学知识点管理、学习进度跟踪、题目练习和统计分析等功能。

## 数据库表结构

### 核心表说明

1. **grade** - 年级表：管理不同年级信息
2. **knowledge_category** - 知识点分类表：管理数学知识点分类（如数与代数、几何等）
3. **knowledge_point** - 知识点表：存储具体的数学知识点内容
4. **learning_progress** - 学习进度表：记录学生对知识点的学习进度
5. **learning_content** - 学习内容记录表：记录具体的学习内容（视频、文档、练习等）
6. **question** - 问题表：存储练习题和测试题
7. **student_answer** - 学生答题记录表：记录学生的答题情况
8. **learning_statistics** - 学习统计表：统计学生的学习数据

## API 接口文档

### 1. 年级管理 API

#### 1.1 查询所有年级
- **URL**: `GET /api/math/grade/list`
- **描述**: 获取所有年级列表
- **响应**: 返回年级列表

#### 1.2 根据ID查询年级
- **URL**: `GET /api/math/grade/{id}`
- **参数**: id - 年级ID
- **响应**: 返回指定年级信息

#### 1.3 根据年级等级查询年级
- **URL**: `GET /api/math/grade/level/{gradeLevel}`
- **参数**: gradeLevel - 年级等级（1-12）
- **响应**: 返回指定年级信息

#### 1.4 新增年级
- **URL**: `POST /api/math/grade/add`
- **请求体**: Grade对象
- **响应**: 返回新增的年级信息

#### 1.5 更新年级
- **URL**: `PUT /api/math/grade/update`
- **请求体**: Grade对象
- **响应**: 返回更新后的年级信息

#### 1.6 删除年级
- **URL**: `DELETE /api/math/grade/{id}`
- **参数**: id - 年级ID
- **响应**: 返回操作结果

### 2. 知识点分类管理 API

#### 2.1 查询所有分类
- **URL**: `GET /api/math/category/list`
- **描述**: 获取所有知识点分类列表
- **响应**: 返回分类列表

#### 2.2 根据ID查询分类
- **URL**: `GET /api/math/category/{id}`
- **参数**: id - 分类ID
- **响应**: 返回指定分类信息

#### 2.3 根据编码查询分类
- **URL**: `GET /api/math/category/code/{categoryCode}`
- **参数**: categoryCode - 分类编码
- **响应**: 返回指定分类信息

#### 2.4 新增分类
- **URL**: `POST /api/math/category/add`
- **请求体**: KnowledgeCategory对象
- **响应**: 返回新增的分类信息

#### 2.5 更新分类
- **URL**: `PUT /api/math/category/update`
- **请求体**: KnowledgeCategory对象
- **响应**: 返回更新后的分类信息

#### 2.6 删除分类
- **URL**: `DELETE /api/math/category/{id}`
- **参数**: id - 分类ID
- **响应**: 返回操作结果

### 3. 知识点管理 API

#### 3.1 查询所有知识点
- **URL**: `GET /api/math/knowledge/list`
- **描述**: 获取所有知识点列表
- **响应**: 返回知识点列表

#### 3.2 根据ID查询知识点
- **URL**: `GET /api/math/knowledge/{id}`
- **参数**: id - 知识点ID
- **响应**: 返回指定知识点信息

#### 3.3 根据年级ID查询知识点
- **URL**: `GET /api/math/knowledge/grade/{gradeId}`
- **参数**: gradeId - 年级ID
- **响应**: 返回指定年级的知识点列表

#### 3.4 根据分类ID查询知识点
- **URL**: `GET /api/math/knowledge/category/{categoryId}`
- **参数**: categoryId - 分类ID
- **响应**: 返回指定分类的知识点列表

#### 3.5 根据年级和分类查询知识点
- **URL**: `GET /api/math/knowledge/grade/{gradeId}/category/{categoryId}`
- **参数**: gradeId - 年级ID, categoryId - 分类ID
- **响应**: 返回指定年级和分类的知识点列表

#### 3.6 根据编码查询知识点
- **URL**: `GET /api/math/knowledge/code/{pointCode}`
- **参数**: pointCode - 知识点编码
- **响应**: 返回指定知识点信息

#### 3.7 根据难度等级查询知识点
- **URL**: `GET /api/math/knowledge/difficulty/{difficultyLevel}`
- **参数**: difficultyLevel - 难度等级（1-简单，2-中等，3-困难）
- **响应**: 返回指定难度的知识点列表

#### 3.8 新增知识点
- **URL**: `POST /api/math/knowledge/add`
- **请求体**: KnowledgePoint对象
- **响应**: 返回新增的知识点信息

#### 3.9 更新知识点
- **URL**: `PUT /api/math/knowledge/update`
- **请求体**: KnowledgePoint对象
- **响应**: 返回更新后的知识点信息

#### 3.10 删除知识点
- **URL**: `DELETE /api/math/knowledge/{id}`
- **参数**: id - 知识点ID
- **响应**: 返回操作结果

### 4. 学习进度管理 API

#### 4.1 查询用户的学习进度
- **URL**: `GET /api/math/progress/user/{userId}`
- **参数**: userId - 用户ID
- **响应**: 返回用户的学习进度列表

#### 4.2 根据用户ID和知识点ID查询学习进度
- **URL**: `GET /api/math/progress/user/{userId}/knowledge/{knowledgePointId}`
- **参数**: userId - 用户ID, knowledgePointId - 知识点ID
- **响应**: 返回指定学习进度信息

#### 4.3 根据知识点ID查询学习进度
- **URL**: `GET /api/math/progress/knowledge/{knowledgePointId}`
- **参数**: knowledgePointId - 知识点ID
- **响应**: 返回该知识点的所有学习进度

#### 4.4 根据学习状态查询进度
- **URL**: `GET /api/math/progress/user/{userId}/status/{progressStatus}`
- **参数**: userId - 用户ID, progressStatus - 学习状态（1-未开始，2-学习中，3-已完成）
- **响应**: 返回指定状态的学习进度列表

#### 4.5 开始学习知识点
- **URL**: `POST /api/math/progress/start`
- **参数**: userId - 用户ID, knowledgePointId - 知识点ID
- **响应**: 返回学习进度信息

#### 4.6 更新学习进度
- **URL**: `PUT /api/math/progress/update`
- **请求体**: LearningProgress对象
- **响应**: 返回更新后的学习进度信息

#### 4.7 完成学习知识点
- **URL**: `POST /api/math/progress/complete`
- **参数**: userId - 用户ID, knowledgePointId - 知识点ID
- **响应**: 返回完成的学习进度信息

#### 4.8 更新学习进度状态
- **URL**: `PUT /api/math/progress/status`
- **参数**: userId - 用户ID, knowledgePointId - 知识点ID, progressStatus - 学习状态
- **响应**: 返回操作结果

### 5. 学习内容记录管理 API

#### 5.1 查询用户的学习内容记录
- **URL**: `GET /api/math/content/user/{userId}`
- **参数**: userId - 用户ID
- **响应**: 返回用户的学习内容记录列表

#### 5.2 根据用户ID和知识点ID查询学习内容
- **URL**: `GET /api/math/content/user/{userId}/knowledge/{knowledgePointId}`
- **参数**: userId - 用户ID, knowledgePointId - 知识点ID
- **响应**: 返回指定学习内容列表

#### 5.3 根据内容类型查询学习内容
- **URL**: `GET /api/math/content/user/{userId}/type/{contentType}`
- **参数**: userId - 用户ID, contentType - 内容类型（1-视频，2-文档，3-练习，4-测试）
- **响应**: 返回指定类型的学习内容列表

#### 5.4 根据完成状态查询学习内容
- **URL**: `GET /api/math/content/user/{userId}/status/{completionStatus}`
- **参数**: userId - 用户ID, completionStatus - 完成状态（1-未完成，2-已完成）
- **响应**: 返回指定状态的学习内容列表

#### 5.5 新增学习内容记录
- **URL**: `POST /api/math/content/add`
- **请求体**: LearningContent对象
- **响应**: 返回新增的学习内容记录

#### 5.6 更新学习内容记录
- **URL**: `PUT /api/math/content/update`
- **请求体**: LearningContent对象
- **响应**: 返回更新后的学习内容记录

#### 5.7 完成学习内容
- **URL**: `POST /api/math/content/complete`
- **参数**: contentId - 内容ID, score - 得分, feedback - 反馈（可选）
- **响应**: 返回完成的学习内容记录

### 6. 问题管理 API

#### 6.1 查询所有问题
- **URL**: `GET /api/math/question/list`
- **描述**: 获取所有问题列表
- **响应**: 返回问题列表

#### 6.2 根据ID查询问题
- **URL**: `GET /api/math/question/{id}`
- **参数**: id - 问题ID
- **响应**: 返回指定问题信息

#### 6.3 根据知识点ID查询问题
- **URL**: `GET /api/math/question/knowledge/{knowledgePointId}`
- **参数**: knowledgePointId - 知识点ID
- **响应**: 返回指定知识点的问题列表

#### 6.4 根据题目类型查询问题
- **URL**: `GET /api/math/question/type/{questionType}`
- **参数**: questionType - 题目类型（1-选择题，2-填空题，3-计算题，4-应用题）
- **响应**: 返回指定类型的问题列表

#### 6.5 根据难度等级查询问题
- **URL**: `GET /api/math/question/difficulty/{difficultyLevel}`
- **参数**: difficultyLevel - 难度等级（1-简单，2-中等，3-困难）
- **响应**: 返回指定难度的问题列表

#### 6.6 根据知识点和难度查询问题
- **URL**: `GET /api/math/question/knowledge/{knowledgePointId}/difficulty/{difficultyLevel}`
- **参数**: knowledgePointId - 知识点ID, difficultyLevel - 难度等级
- **响应**: 返回指定知识点和难度的问题列表

#### 6.7 随机获取指定数量的题目
- **URL**: `GET /api/math/question/random`
- **参数**: knowledgePointId - 知识点ID, limit - 题目数量（默认10）
- **响应**: 返回随机题目列表

#### 6.8 新增问题
- **URL**: `POST /api/math/question/add`
- **请求体**: Question对象
- **响应**: 返回新增的问题信息

#### 6.9 更新问题
- **URL**: `PUT /api/math/question/update`
- **请求体**: Question对象
- **响应**: 返回更新后的问题信息

#### 6.10 删除问题
- **URL**: `DELETE /api/math/question/{id}`
- **参数**: id - 问题ID
- **响应**: 返回操作结果

### 7. 学生答题记录管理 API

#### 7.1 查询用户的答题记录
- **URL**: `GET /api/math/answer/user/{userId}`
- **参数**: userId - 用户ID
- **响应**: 返回用户的答题记录列表

#### 7.2 根据用户ID和问题ID查询答题记录
- **URL**: `GET /api/math/answer/user/{userId}/question/{questionId}`
- **参数**: userId - 用户ID, questionId - 问题ID
- **响应**: 返回指定答题记录列表

#### 7.3 根据知识点ID查询答题记录
- **URL**: `GET /api/math/answer/user/{userId}/knowledge/{knowledgePointId}`
- **参数**: userId - 用户ID, knowledgePointId - 知识点ID
- **响应**: 返回指定知识点的答题记录列表

#### 7.4 查询正确答题记录
- **URL**: `GET /api/math/answer/user/{userId}/correct`
- **参数**: userId - 用户ID
- **响应**: 返回正确答题记录列表

#### 7.5 查询错误答题记录
- **URL**: `GET /api/math/answer/user/{userId}/incorrect`
- **参数**: userId - 用户ID
- **响应**: 返回错误答题记录列表

#### 7.6 提交答案
- **URL**: `POST /api/math/answer/submit`
- **请求体**: StudentAnswer对象
- **响应**: 返回答题记录信息

#### 7.7 更新答题记录
- **URL**: `PUT /api/math/answer/update`
- **请求体**: StudentAnswer对象
- **响应**: 返回更新后的答题记录信息

#### 7.8 统计用户答题情况
- **URL**: `GET /api/math/answer/count/user/{userId}/knowledge/{knowledgePointId}`
- **参数**: userId - 用户ID, knowledgePointId - 知识点ID
- **响应**: 返回答题总数

#### 7.9 统计用户正确答题数
- **URL**: `GET /api/math/answer/count/correct/user/{userId}/knowledge/{knowledgePointId}`
- **参数**: userId - 用户ID, knowledgePointId - 知识点ID
- **响应**: 返回正确答题数

### 8. 学习统计管理 API

#### 8.1 查询用户的学习统计
- **URL**: `GET /api/math/statistics/user/{userId}`
- **参数**: userId - 用户ID
- **响应**: 返回用户的学习统计列表

#### 8.2 根据用户ID和知识点ID查询学习统计
- **URL**: `GET /api/math/statistics/user/{userId}/knowledge/{knowledgePointId}`
- **参数**: userId - 用户ID, knowledgePointId - 知识点ID
- **响应**: 返回指定学习统计信息

#### 8.3 根据掌握程度查询学习统计
- **URL**: `GET /api/math/statistics/user/{userId}/mastery/{masteryLevel}`
- **参数**: userId - 用户ID, masteryLevel - 掌握程度（1-未掌握，2-基本掌握，3-熟练掌握）
- **响应**: 返回指定掌握程度的学习统计列表

#### 8.4 更新学习统计信息
- **URL**: `POST /api/math/statistics/update`
- **参数**: userId - 用户ID, knowledgePointId - 知识点ID
- **响应**: 返回更新后的学习统计信息

#### 8.5 计算并更新掌握程度
- **URL**: `POST /api/math/statistics/mastery/calculate`
- **参数**: userId - 用户ID, knowledgePointId - 知识点ID
- **响应**: 返回计算后的学习统计信息

## 使用示例

### 1. 获取一年级数学知识点
```bash
# 1. 获取年级信息
GET /api/math/grade/level/1

# 2. 获取知识点分类
GET /api/math/category/list

# 3. 获取一年级数与代数知识点
GET /api/math/knowledge/grade/1/category/1
```

### 2. 学生开始学习
```bash
# 1. 开始学习知识点
POST /api/math/progress/start?userId=1&knowledgePointId=1

# 2. 记录学习内容
POST /api/math/content/add
{
  "userId": 1,
  "knowledgePointId": 1,
  "contentType": 1,
  "contentTitle": "认识数字1-10视频",
  "contentUrl": "/videos/numbers-1-10.mp4"
}

# 3. 完成学习内容
POST /api/math/content/complete?contentId=1&score=95&feedback=学习效果很好
```

### 3. 练习题目
```bash
# 1. 获取随机题目
GET /api/math/question/random?knowledgePointId=1&limit=5

# 2. 提交答案
POST /api/math/answer/submit
{
  "userId": 1,
  "questionId": 1,
  "knowledgePointId": 1,
  "userAnswer": "5",
  "isCorrect": 1,
  "score": 10
}

# 3. 完成学习
POST /api/math/progress/complete?userId=1&knowledgePointId=1
```

### 4. 查看学习统计
```bash
# 1. 更新学习统计
POST /api/math/statistics/update?userId=1&knowledgePointId=1

# 2. 计算掌握程度
POST /api/math/statistics/mastery/calculate?userId=1&knowledgePointId=1

# 3. 查看学习统计
GET /api/math/statistics/user/1
```

## 注意事项

1. 所有API都支持跨域访问（CORS）
2. 响应格式统一使用RespResult包装
3. 所有删除操作都是逻辑删除，不会物理删除数据
4. 时间字段使用"yyyy-MM-dd HH:mm:ss"格式
5. 分页查询可以根据需要添加分页参数
6. 建议在生产环境中添加适当的权限验证和参数校验
