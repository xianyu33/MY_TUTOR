# MY_TUTOR 多语言支持说明

## 概述

MY_TUTOR 测试系统现已支持中法双语显示。所有核心表都添加了法语冗余字段，可以根据用户的语言偏好显示相应的内容。

## 支持法语的表字段

### 1. 知识点分类表 (knowledge_category)
- `category_name_fr` - 分类名称（法语）
- `description_fr` - 分类描述（法语）

### 2. 知识点表 (knowledge_point)
- `point_name_fr` - 知识点名称（法语）
- `description_fr` - 知识点描述（法语）
- `content_fr` - 知识点内容详情（法语）
- `learning_objectives_fr` - 学习目标（法语）

### 3. 题目表 (question)
- `question_title_fr` - 题目标题（法语）
- `question_content_fr` - 题目内容（法语）
- `options_fr` - 选项（法语，JSON格式）
- `correct_answer_fr` - 正确答案（法语）
- `answer_explanation_fr` - 答案解释（法语）

### 4. 测试表 (test)
- `test_name_fr` - 测试名称（法语）

### 5. 学生测试记录表 (student_test_record)
- `test_name_fr` - 测试名称（法语，冗余）

### 6. 学生测试答题详情表 (student_test_answer)
- `question_content_fr` - 题目内容（法语，冗余）
- `correct_answer_fr` - 正确答案（法语，冗余）
- `student_answer_fr` - 学生答案（法语）

## 示例数据

### 知识点分类示例
```sql
-- 中文
category_name: '数与代数'
description: '数的认识、运算、代数式等'

-- 法语
category_name_fr: 'Nombres et Algèbre'
description_fr: 'Reconnaissance des nombres, opérations, expressions algébriques, etc.'
```

### 知识点示例
```sql
-- 中文
point_name: '数的认识'
description: '认识1-100的数字'
content: '学习1-100数字的读写和大小比较'

-- 法语
point_name_fr: 'Reconnaissance des nombres'
description_fr: 'Reconnaissance des nombres de 1 à 100'
content_fr: 'Apprendre la lecture, l\'écriture et la comparaison des nombres de 1 à 100'
```

### 题目示例
```sql
-- 中文
question_title: '数字认识'
question_content: '下列哪个数字最大？'
options: '["A. 15", "B. 25", "C. 35", "D. 45"]'
correct_answer: 'D'
answer_explanation: '45是最大的数字'

-- 法语
question_title_fr: 'Reconnaissance des nombres'
question_content_fr: 'Quel est le plus grand nombre parmi les suivants ?'
options_fr: '["A. 15", "B. 25", "C. 35", "D. 45"]'
correct_answer_fr: 'D'
answer_explanation_fr: '45 est le plus grand nombre'
```

### 测试示例
```sql
-- 中文
test_name: '一年级数学基础测试'

-- 法语
test_name_fr: 'Test de base en mathématiques de première année'
```

## 使用方式

### 1. 前端显示逻辑
```javascript
// 根据用户语言偏好选择显示字段
function getDisplayText(item, field, language = 'zh') {
    if (language === 'fr' && item[field + '_fr']) {
        return item[field + '_fr'];
    }
    return item[field];
}

// 示例使用
const categoryName = getDisplayText(category, 'category_name', userLanguage);
const questionContent = getDisplayText(question, 'question_content', userLanguage);
```

### 2. 后端API响应
```java
// 可以根据请求头或参数决定返回哪种语言的内容
@GetMapping("/questions/{id}")
public Question getQuestion(@PathVariable Integer id, 
                           @RequestHeader(value = "Accept-Language", defaultValue = "zh") String language) {
    Question question = questionService.findById(id);
    
    if ("fr".equals(language)) {
        // 返回法语内容
        question.setQuestionContent(question.getQuestionContentFr());
        question.setOptions(question.getOptionsFr());
        question.setCorrectAnswer(question.getCorrectAnswerFr());
        question.setAnswerExplanation(question.getAnswerExplanationFr());
    }
    
    return question;
}
```

### 3. 数据库查询
```sql
-- 查询法语内容
SELECT 
    point_name_fr as point_name,
    description_fr as description,
    content_fr as content
FROM knowledge_point 
WHERE id = 1;

-- 查询中法双语内容
SELECT 
    point_name,
    point_name_fr,
    description,
    description_fr,
    content,
    content_fr
FROM knowledge_point 
WHERE id = 1;
```

## 扩展支持

### 添加新语言
如果需要支持其他语言（如英语、西班牙语等），可以按照相同的模式添加新的字段：

```sql
-- 添加英语字段示例
ALTER TABLE knowledge_category ADD COLUMN category_name_en VARCHAR(50) COMMENT '分类名称（英语）';
ALTER TABLE knowledge_category ADD COLUMN description_en TEXT COMMENT '分类描述（英语）';

-- 添加索引
CREATE INDEX idx_category_name_en ON knowledge_category(category_name_en);
```

### 语言配置表
可以考虑创建一个语言配置表来管理支持的语言：

```sql
CREATE TABLE language_config (
    id INT PRIMARY KEY AUTO_INCREMENT,
    language_code VARCHAR(10) NOT NULL COMMENT '语言代码',
    language_name VARCHAR(50) NOT NULL COMMENT '语言名称',
    is_active TINYINT DEFAULT 1 COMMENT '是否启用',
    sort_order INT DEFAULT 0 COMMENT '排序',
    create_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO language_config (language_code, language_name, sort_order) VALUES
('zh', '中文', 1),
('fr', 'Français', 2),
('en', 'English', 3);
```

## 注意事项

1. **数据一致性**：确保中法双语内容在语义上保持一致
2. **性能考虑**：法语字段会增加存储空间，但提供了更好的查询性能
3. **索引优化**：为法语字段创建了相应的索引以支持快速查询
4. **空值处理**：法语字段允许为空，当没有法语内容时回退到中文显示
5. **字符集**：使用 utf8mb4 字符集支持完整的 Unicode 字符，包括法语特殊字符

## 维护建议

1. **内容更新**：更新中文内容时，同步更新对应的法语内容
2. **翻译质量**：确保法语翻译的准确性和专业性
3. **测试覆盖**：在测试时验证两种语言的内容显示正确
4. **用户反馈**：收集用户对多语言功能的反馈，持续改进

通过这种设计，MY_TUTOR 系统可以灵活地支持多语言显示，为用户提供更好的国际化体验。
