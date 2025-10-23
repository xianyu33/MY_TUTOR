# MY_TUTOR 多语言支持 - 代码更新说明

## 概述

本文档说明了为支持多语言功能（中法双语）而对MY_TUTOR系统进行的代码更新。

## 更新的文件

### 1. 实体类 (Domain Classes)

#### KnowledgePoint.java
**文件路径：** `src/main/java/com/yy/my_tutor/math/domain/KnowledgePoint.java`

**新增字段：**
- `pointNameFr` - 知识点名称（法语）
- `descriptionFr` - 知识点描述（法语）
- `contentFr` - 知识点内容详情（法语）
- `learningObjectivesFr` - 学习目标（法语）
- `iconUrl` - 知识点图标URL
- `iconClass` - 知识点图标CSS类名

#### KnowledgeCategory.java
**文件路径：** `src/main/java/com/yy/my_tutor/math/domain/KnowledgeCategory.java`

**新增字段：**
- `categoryNameFr` - 分类名称（法语）
- `descriptionFr` - 分类描述（法语）

#### Question.java
**文件路径：** `src/main/java/com/yy/my_tutor/math/domain/Question.java`

**新增字段：**
- `questionTitleFr` - 题目标题（法语）
- `questionContentFr` - 题目内容（法语）
- `optionsFr` - 选项（法语，JSON格式）
- `correctAnswerFr` - 正确答案（法语）
- `answerExplanationFr` - 答案解释（法语）

#### Test.java
**文件路径：** `src/main/java/com/yy/my_tutor/test/domain/Test.java`

**新增字段：**
- `testNameFr` - 测试名称（法语）

#### StudentTestRecord.java
**文件路径：** `src/main/java/com/yy/my_tutor/test/domain/StudentTestRecord.java`

**新增字段：**
- `testNameFr` - 测试名称（法语，冗余）

#### StudentTestAnswer.java
**文件路径：** `src/main/java/com/yy/my_tutor/test/domain/StudentTestAnswer.java`

**新增字段：**
- `questionContentFr` - 题目内容（法语，冗余）
- `correctAnswerFr` - 正确答案（法语，冗余）
- `studentAnswerFr` - 学生答案（法语）

### 2. MyBatis映射文件 (Mapper XML)

#### KnowledgePointMapper.xml
**文件路径：** `src/main/resources/mapper/KnowledgePointMapper.xml`

**更新内容：**
- 更新 `BaseResultMap` 添加新字段映射
- 更新 `Base_Column_List` 包含新字段

#### KnowledgeCategoryMapper.xml
**文件路径：** `src/main/resources/mapper/KnowledgeCategoryMapper.xml`

**更新内容：**
- 更新 `BaseResultMap` 添加法语字段映射
- 更新 `Base_Column_List` 包含法语字段

#### QuestionMapper.xml
**文件路径：** `src/main/resources/mapper/QuestionMapper.xml`

**更新内容：**
- 更新 `BaseResultMap` 添加法语字段映射
- 更新 `Base_Column_List` 包含法语字段

### 3. 工具类 (Utility Classes)

#### MultilingualUtil.java
**文件路径：** `src/main/java/com/yy/my_tutor/util/MultilingualUtil.java`

**功能：**
- 提供多语言文本获取方法
- 支持知识点分类、知识点、题目、测试等实体的多语言处理
- 语言验证和默认语言处理
- 统一的语言切换逻辑

**主要方法：**
- `getCategoryName()` - 获取分类名称
- `getKnowledgePointName()` - 获取知识点名称
- `getQuestionContent()` - 获取题目内容
- `getTestName()` - 获取测试名称
- `isValidLanguage()` - 验证语言代码
- `getValidLanguage()` - 获取有效语言代码

### 4. 控制器 (Controllers)

#### MultilingualController.java
**文件路径：** `src/main/java/com/yy/my_tutor/math/controller/MultilingualController.java`

**功能：**
- 提供多语言API接口
- 展示如何使用多语言功能
- 支持通过 `language` 参数切换语言

**主要接口：**
- `GET /api/multilingual/categories` - 获取分类列表（多语言）
- `GET /api/multilingual/knowledge-points` - 获取知识点列表（多语言）
- `GET /api/multilingual/questions` - 获取题目列表（多语言）
- `GET /api/multilingual/knowledge-points/{id}` - 获取知识点详情（多语言）
- `GET /api/multilingual/questions/{id}` - 获取题目详情（多语言）
- `GET /api/multilingual/languages` - 获取支持的语言列表

## 使用示例

### 1. 后端服务使用

```java
// 在Service层使用多语言工具
@Autowired
private MultilingualUtil multilingualUtil;

public List<KnowledgePoint> getKnowledgePointsWithLanguage(Integer gradeId, String language) {
    List<KnowledgePoint> knowledgePoints = knowledgePointService.findKnowledgePointsByGrade(gradeId);
    
    // 根据语言偏好处理数据
    knowledgePoints.forEach(kp -> {
        String displayName = MultilingualUtil.getKnowledgePointName(kp, language);
        String displayDescription = MultilingualUtil.getKnowledgePointDescription(kp, language);
        // 设置显示用的字段...
    });
    
    return knowledgePoints;
}
```

### 2. API调用示例

```bash
# 获取中文分类列表
curl "http://localhost:8080/api/multilingual/categories?language=zh"

# 获取法语分类列表
curl "http://localhost:8080/api/multilingual/categories?language=fr"

# 获取知识点列表（法语）
curl "http://localhost:8080/api/multilingual/knowledge-points?gradeId=1&language=fr"
```

### 3. 前端使用示例

```javascript
// Vue.js 组件中使用
export default {
  data() {
    return {
      currentLanguage: 'zh',
      knowledgePoints: []
    }
  },
  methods: {
    async loadKnowledgePoints() {
      const response = await this.$http.get(`/api/multilingual/knowledge-points?language=${this.currentLanguage}`);
      this.knowledgePoints = response.data.data;
    },
    switchLanguage(language) {
      this.currentLanguage = language;
      this.loadKnowledgePoints();
    }
  }
}
```

## 数据库迁移

### 1. 新项目部署
```sql
source test_system_schema.sql;
```

### 2. 现有项目升级
```sql
source add_french_fields.sql;
```

## 配置说明

### 1. 支持的语言
- `zh` - 中文（默认）
- `fr` - 法语

### 2. 语言参数
- API接口通过 `language` 参数指定语言
- 默认语言为中文 (`zh`)
- 无效语言代码会自动回退到默认语言

### 3. 回退机制
- 如果法语字段为空或不存在，自动回退到中文内容
- 确保系统在任何情况下都能正常显示内容

## 注意事项

### 1. 数据一致性
- 确保中法双语内容在语义上保持一致
- 建议在添加新内容时同步更新两种语言

### 2. 性能考虑
- 法语字段会增加存储空间
- 通过索引优化查询性能
- 考虑使用缓存减少数据库查询

### 3. 扩展性
- 可以轻松添加其他语言支持
- 遵循相同的命名规范：`字段名_fr`、`字段名_en` 等

### 4. 测试建议
- 测试不同语言下的API响应
- 验证回退机制是否正常工作
- 检查前端多语言切换功能

## 后续开发建议

### 1. 服务层增强
- 在现有的Service类中添加多语言支持方法
- 创建专门的多语言Service接口

### 2. 缓存优化
- 为多语言内容添加Redis缓存
- 实现语言切换时的缓存更新机制

### 3. 管理界面
- 开发多语言内容管理界面
- 支持批量导入多语言数据

### 4. 国际化框架
- 考虑集成Spring的国际化框架
- 支持更复杂的多语言场景

通过以上代码更新，MY_TUTOR系统现在完全支持中法双语显示，可以根据用户的语言偏好灵活切换显示内容，大大提升了系统的国际化能力和用户体验。
