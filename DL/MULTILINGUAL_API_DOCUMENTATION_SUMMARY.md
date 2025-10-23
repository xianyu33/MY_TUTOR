# 多语言API接口文档更新总结

## 更新概述

为了支持MY_TUTOR系统的中法双语显示和知识点图标功能，已对相关API接口文档进行了全面更新。

## 更新的文档文件

### 1. MATH_API_DOCUMENTATION.md
**更新内容：**
- ✅ 添加多语言支持说明
- ✅ 新增多语言API接口章节
- ✅ 更新数据库表结构说明（标注多语言支持）
- ✅ 添加知识点图标功能说明
- ✅ 更新注意事项，包含多语言相关说明

**新增接口：**
- `GET /api/multilingual/categories` - 获取分类列表（多语言）
- `GET /api/multilingual/knowledge-points` - 获取知识点列表（多语言）
- `GET /api/multilingual/questions` - 获取题目列表（多语言）
- `GET /api/multilingual/knowledge-points/{id}` - 获取知识点详情（多语言）
- `GET /api/multilingual/questions/{id}` - 获取题目详情（多语言）
- `GET /api/multilingual/languages` - 获取支持的语言列表

### 2. STUDENT_TEST_API_DOCUMENTATION.md
**更新内容：**
- ✅ 添加多语言支持说明
- ✅ 更新所有API接口，添加 `language` 参数
- ✅ 更新返回示例，包含多语言字段
- ✅ 添加多语言使用说明章节
- ✅ 更新注意事项，包含多语言相关说明

**更新的接口：**
- `POST /api/student-test/generate-random` - 添加 `language` 参数
- `POST /api/student-test/start` - 添加 `language` 参数
- `POST /api/student-test/submit-answer` - 添加 `language` 参数
- 所有返回示例都包含多语言字段

### 3. api.md
**更新内容：**
- ✅ 添加系统概述和多语言支持说明
- ✅ 新增多语言API接口示例章节
- ✅ 添加学生测试功能多语言示例
- ✅ 更新注意事项，包含多语言相关说明
- ✅ 添加相关文档链接

## 多语言支持特性

### 支持的语言
- `zh` - 中文（默认）
- `fr` - 法语

### 多语言字段
**知识点分类表 (knowledge_category):**
- `category_name_fr` - 分类名称（法语）
- `description_fr` - 分类描述（法语）

**知识点表 (knowledge_point):**
- `point_name_fr` - 知识点名称（法语）
- `description_fr` - 知识点描述（法语）
- `content_fr` - 知识点内容（法语）
- `learning_objectives_fr` - 学习目标（法语）
- `icon_url` - 图标文件URL路径
- `icon_class` - 图标CSS类名

**题目表 (question):**
- `question_title_fr` - 题目标题（法语）
- `question_content_fr` - 题目内容（法语）
- `options_fr` - 选项（法语）
- `correct_answer_fr` - 正确答案（法语）
- `answer_explanation_fr` - 答案解释（法语）

**测试表 (test):**
- `test_name_fr` - 测试名称（法语）

**学生测试记录表 (student_test_record):**
- `test_name_fr` - 测试名称（法语，冗余）

**学生测试答题表 (student_test_answer):**
- `question_content_fr` - 题目内容（法语，冗余）
- `correct_answer_fr` - 正确答案（法语，冗余）
- `student_answer_fr` - 学生答案（法语）

### 回退机制
- 如果法语内容为空，自动回退到中文内容
- 无效语言代码自动回退到默认语言（中文）
- 确保系统在任何情况下都能正常显示内容

## API使用示例

### 1. 获取法语分类列表
```bash
GET /api/multilingual/categories?language=fr
```

### 2. 获取法语知识点列表
```bash
GET /api/multilingual/knowledge-points?gradeId=1&language=fr
```

### 3. 生成法语测试
```bash
POST /api/student-test/generate-random?studentId=1&gradeId=9&language=fr
```

### 4. 提交法语答案
```bash
POST /api/student-test/submit-answer?testRecordId=1&questionId=1&studentAnswer=A&language=fr
```

## 图标功能

### 图标字段
- `iconUrl` - 图标文件URL路径
- `iconClass` - 图标CSS类名

### 使用方式
- **图片图标**: 使用 `iconUrl` 字段存储图片路径
- **CSS图标**: 使用 `iconClass` 字段存储CSS类名
- **回退机制**: 优先使用图片图标，回退到CSS图标

### 示例
```json
{
  "iconUrl": "/icons/numbers.png",
  "iconClass": "icon-numbers"
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

- **数学知识点API**: `MATH_API_DOCUMENTATION.md`
- **学生测试功能API**: `STUDENT_TEST_API_DOCUMENTATION.md`
- **多语言支持**: `MULTILINGUAL_SUPPORT_README.md`
- **知识点图标**: `KNOWLEDGE_POINT_ICONS_README.md`
- **数据库架构**: `DATABASE_SCHEMA_README.md`
- **代码更新说明**: `CODE_UPDATE_DOCUMENTATION.md`

## 部署说明

### 新项目部署
```sql
source test_system_schema.sql;
```

### 现有项目升级
```sql
source add_french_fields.sql;
```

通过以上更新，MY_TUTOR系统的API接口文档已完全支持多语言功能，开发者可以根据文档轻松集成中法双语显示和知识点图标功能。
