# Feature AI Course 接口文档

> 本文档记录 feature_ai_course 分支新增/修改的接口

## 目录

1. [AI测试生成接口](#1-ai测试生成接口)
2. [课程生成接口](#2-课程生成接口)
3. [知识点查询接口](#3-知识点查询接口)
4. [学习计划接口](#4-学习计划接口)

---

## 1. AI测试生成接口

### 1.1 根据单个知识点生成AI测试

**接口地址**: `POST /api/ai-test/generate-single`

**请求参数**:
```json
{
  "studentId": 48,
  "knowledgePointId": 627,
  "questionCount": 5,
  "difficultyLevel": 2,
  "testName": "测试名称",
  "testNameFr": "Nom du test"
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| studentId | Integer | 是 | 学生ID |
| knowledgePointId | Integer | 是 | 知识点ID |
| questionCount | Integer | 是 | 题目数量 |
| difficultyLevel | Integer | 否 | 难度等级(1-简单,2-中等,3-困难)，不传则自动确定 |
| saveToQuestionBank | Boolean | 否 | 是否保存到题库，默认true |
| testName | String | 否 | 测试名称 |
| testNameFr | String | 否 | 测试名称(法语) |

**响应示例**:
```json
{
  "code": 200,
  "message": "AI测试生成成功",
  "data": {
    "id": 135,
    "studentId": 48,
    "testId": 155,
    "testName": "测试名称",
    "testNameFr": "Nom du test",
    "startTime": "2026-01-21 07:50:22",
    "timeLimit": 60,
    "totalQuestions": 5,
    "totalPoints": 5,
    "testStatus": 1,
    "createAt": "2026-01-20 23:50:22",
    "questions": [
      {
        "questionId": 183,
        "sortOrder": 1,
        "points": 1,
        "questionTitle": "Solve Sine Equation",
        "questionTitleFr": "Résoudre une équation de sinus",
        "questionContent": "Find the solutions of the equation \\(2\\sin(x) = \\sqrt{3}\\) in the interval \\([0, 2\\pi)\\).",
        "questionContentFr": "Trouvez les solutions de l'équation \\(2\\sin(x)=\\sqrt{3}\\) dans l'intervalle \\([0, 2\\pi)\\).",
        "options": "[\"A. \\(x = \frac{\\pi}{6}, \frac{5\\pi}{6}\\)\", \"B. \\(x = \frac{\\pi}{3}, \frac{2\\pi}{3}\\)\", \"C. \\(x = \frac{\\pi}{4}, \frac{3\\pi}{4}\\)\", \"D. \\(x = \frac{\\pi}{5}, \frac{4\\pi}{5}\\)\"]",
        "optionsFr": "[\"A. \\(x=\frac{\\pi}{6},\frac{5\\pi}{6}\\)\", \"B. \\(x=\frac{\\pi}{3},\frac{2\\pi}{3}\\)\", \"C. \\(x=\frac{\\pi}{4},\frac{3\\pi}{4}\\)\", \"D. \\(x=\frac{\\pi}{5},\frac{4\\pi}{5}\\)\"]",
        "correctAnswer": "B",
        "correctAnswerFr": "B",
        "answerExplanation": "First, isolate \\(\\sin(x)\\) in the equation \\(2\\sin(x)=\\sqrt{3}\\). Divide both sides by 2 to get \\(\\sin(x)=\frac{\\sqrt{3}}{2}\\). We know that the sine function \\(y = \\sin(x)\\) has a value of \\(\frac{\\sqrt{3}}{2}\\) at \\(x=\frac{\\pi}{3}\\) and \\(x = \frac{2\\pi}{3}\\) in the interval \\([0, 2\\pi)\\) because the sine function represents the y - coordinate on the unit circle. So the solutions of the equation \\(2\\sin(x)=\\sqrt{3}\\) in the given interval are \\(x=\frac{\\pi}{3},\frac{2\\pi}{3}\\).",
        "answerExplanationFr": "Tout d'abord, isolez \\(\\sin(x)\\) dans l'équation \\(2\\sin(x)=\\sqrt{3}\\). Divisez les deux côtés par 2 pour obtenir \\(\\sin(x)=\frac{\\sqrt{3}}{2}\\). Nous savons que la fonction sinus \\(y = \\sin(x)\\) a une valeur de \\(\frac{\\sqrt{3}}{2}\\) à \\(x=\frac{\\pi}{3}\\) et \\(x=\frac{2\\pi}{3}\\) dans l'intervalle \\([0, 2\\pi)\\) car la fonction sinus représente la coordonnée y sur le cercle unité. Donc, les solutions de l'équation \\(2\\sin(x)=\\sqrt{3}\\) dans l'intervalle donné sont \\(x=\frac{\\pi}{3},\frac{2\\pi}{3}\\).",
        "difficultyLevel": 2,
        "knowledgePointId": 627,
        "knowledgePointName": "Basic Trigonometric Equations",
        "knowledgePointNameFr": "Équations trigonométriques de base"
      },
      {
        "questionId": 184,
        "sortOrder": 2,
        "points": 1,
        "questionTitle": "Solve Cosine Equation",
        "questionTitleFr": "Résoudre une équation de cosinus",
        "questionContent": "What are the solutions of the equation \\(\\cos(2x)=-\frac{1}{2}\\) in the interval \\([0, \\pi]\\)?",
        "questionContentFr": "Quelles sont les solutions de l'équation \\(\\cos(2x)=-\frac{1}{2}\\) dans l'intervalle \\([0, \\pi]\\)?",
        "options": "[\"A. \\(x=\frac{\\pi}{6}, \frac{5\\pi}{6}\\)\", \"B. \\(x=\frac{\\pi}{3}, \frac{2\\pi}{3}\\)\", \"C. \\(x=\frac{\\pi}{4}, \frac{3\\pi}{4}\\)\", \"D. \\(x=\frac{\\pi}{5}, \frac{4\\pi}{5}\\)\"]",
        "optionsFr": "[\"A. \\(x=\frac{\\pi}{6},\frac{5\\pi}{6}\\)\", \"B. \\(x=\frac{\\pi}{3},\frac{2\\pi}{3}\\)\", \"C. \\(x=\frac{\\pi}{4},\frac{3\\pi}{4}\\)\", \"D. \\(x=\frac{\\pi}{5},\frac{4\\pi}{5}\\)\"]",
        "correctAnswer": "B",
        "correctAnswerFr": "B",
        "answerExplanation": "Let \\(t = 2x\\). Then the equation becomes \\(\\cos(t)=-\frac{1}{2}\\). The solutions of \\(\\cos(t)=-\frac{1}{2}\\) are \\(t=\frac{2\\pi}{3}+2k\\pi\\) and \\(t=\frac{4\\pi}{3}+2k\\pi\\), \\(k\\in\\mathbb{Z}\\). Since \\(t = 2x\\) and \\(x\\in[0,\\pi]\\), then \\(t\\in[0, 2\\pi]\\). For \\(t=\frac{2\\pi}{3}\\), \\(2x=\frac{2\\pi}{3}\\), so \\(x=\frac{\\pi}{3}\\). For \\(t=\frac{4\\pi}{3}\\), \\(2x=\frac{4\\pi}{3}\\), so \\(x=\frac{2\\pi}{3}\\).",
        "answerExplanationFr": "Posons \\(t = 2x\\). Alors l'équation devient \\(\\cos(t)=-\frac{1}{2}\\). Les solutions de \\(\\cos(t)=-\frac{1}{2}\\) sont \\(t=\frac{2\\pi}{3}+2k\\pi\\) et \\(t=\frac{4\\pi}{3}+2k\\pi\\), \\(k\\in\\mathbb{Z}\\). Puisque \\(t = 2x\\) et \\(x\\in[0,\\pi]\\), alors \\(t\\in[0, 2\\pi]\\). Pour \\(t=\frac{2\\pi}{3}\\), \\(2x=\frac{2\\pi}{3}\\), donc \\(x=\frac{\\pi}{3}\\). Pour \\(t=\frac{4\\pi}{3}\\), \\(2x=\frac{4\\pi}{3}\\), donc \\(x=\frac{2\\pi}{3}\\).",
        "difficultyLevel": 2,
        "knowledgePointId": 627,
        "knowledgePointName": "Basic Trigonometric Equations",
        "knowledgePointNameFr": "Équations trigonométriques de base"
      },
      {
        "questionId": 185,
        "sortOrder": 3,
        "points": 1,
        "questionTitle": "Solve Tangent Equation",
        "questionTitleFr": "Résoudre une équation de tangente",
        "questionContent": "Solve the equation \\(\tan(3x)= 1\\) in the interval \\([0, \frac{\\pi}{2})\\).",
        "questionContentFr": "Résolvez l'équation \\(\tan(3x)=1\\) dans l'intervalle \\([0,\frac{\\pi}{2})\\).",
        "options": "[\"A. \\(x=\frac{\\pi}{12}, \frac{5\\pi}{12}\\)\", \"B. \\(x=\frac{\\pi}{6}, \frac{\\pi}{2}\\)\", \"C. \\(x=\frac{\\pi}{4}, \frac{3\\pi}{4}\\)\", \"D. \\(x=\frac{\\pi}{8}, \frac{3\\pi}{8}\\)\"]",
        "optionsFr": "[\"A. \\(x=\frac{\\pi}{12},\frac{5\\pi}{12}\\)\", \"B. \\(x=\frac{\\pi}{6},\frac{\\pi}{2}\\)\", \"C. \\(x=\frac{\\pi}{4},\frac{3\\pi}{4}\\)\", \"D. \\(x=\frac{\\pi}{8},\frac{3\\pi}{8}\\)\"]",
        "correctAnswer": "A",
        "correctAnswerFr": "A",
        "answerExplanation": "Let \\(t = 3x\\). The equation \\(\tan(t)=1\\) has solutions \\(t=\frac{\\pi}{4}+k\\pi\\), \\(k\\in\\mathbb{Z}\\). Since \\(x\\in[0,\frac{\\pi}{2})\\), then \\(t = 3x\\in[0,\frac{3\\pi}{2})\\). For \\(k = 0\\), \\(t=\frac{\\pi}{4}\\), so \\(3x=\frac{\\pi}{4}\\) and \\(x=\frac{\\pi}{12}\\). For \\(k = 1\\), \\(t=\frac{\\pi}{4}+\\pi=\frac{5\\pi}{4}\\), so \\(3x=\frac{5\\pi}{4}\\) and \\(x=\frac{5\\pi}{12}\\).",
        "answerExplanationFr": "Posons \\(t = 3x\\). L'équation \\(\tan(t)=1\\) a des solutions \\(t=\frac{\\pi}{4}+k\\pi\\), \\(k\\in\\mathbb{Z}\\). Puisque \\(x\\in[0,\frac{\\pi}{2})\\), alors \\(t = 3x\\in[0,\frac{3\\pi}{2})\\). Pour \\(k = 0\\), \\(t=\frac{\\pi}{4}\\), donc \\(3x=\frac{\\pi}{4}\\) et \\(x=\frac{\\pi}{12}\\). Pour \\(k = 1\\), \\(t=\frac{\\pi}{4}+\\pi=\frac{5\\pi}{4}\\), donc \\(3x=\frac{5\\pi}{4}\\) et \\(x=\frac{5\\pi}{12}\\).",
        "difficultyLevel": 2,
        "knowledgePointId": 627,
        "knowledgePointName": "Basic Trigonometric Equations",
        "knowledgePointNameFr": "Équations trigonométriques de base"
      },
      {
        "questionId": 186,
        "sortOrder": 4,
        "points": 1,
        "questionTitle": "Solve Sine - Cosine Equation",
        "questionTitleFr": "Résoudre une équation de sinus - cosinus",
        "questionContent": "Find the solutions of \\(\\sin(x)=\\cos(x)\\) in the interval \\([0, 2\\pi)\\).",
        "questionContentFr": "Trouvez les solutions de \\(\\sin(x)=\\cos(x)\\) dans l'intervalle \\([0, 2\\pi)\\).",
        "options": "[\"A. \\(x=\frac{\\pi}{4}, \frac{5\\pi}{4}\\)\", \"B. \\(x=\frac{\\pi}{3}, \frac{2\\pi}{3}\\)\", \"C. \\(x=\frac{\\pi}{6}, \frac{5\\pi}{6}\\)\", \"D. \\(x=\frac{\\pi}{5}, \frac{4\\pi}{5}\\)\"]",
        "optionsFr": "[\"A. \\(x=\frac{\\pi}{4},\frac{5\\pi}{4}\\)\", \"B. \\(x=\frac{\\pi}{3},\frac{2\\pi}{3}\\)\", \"C. \\(x=\frac{\\pi}{6},\frac{5\\pi}{6}\\)\", \"D. \\(x=\frac{\\pi}{5},\frac{4\\pi}{5}\\)\"]",
        "correctAnswer": "A",
        "correctAnswerFr": "A",
        "answerExplanation": "If \\(\\sin(x)=\\cos(x)\\), then \\(\frac{\\sin(x)}{\\cos(x)} = 1\\) (assuming \\(\\cos(x)\neq0\\)). Since \\(\frac{\\sin(x)}{\\cos(x)}=\tan(x)\\), the equation becomes \\(\tan(x)=1\\). The solutions of \\(\tan(x)=1\\) in the interval \\([0, 2\\pi)\\) are \\(x=\frac{\\pi}{4}\\) and \\(x=\frac{\\pi}{4}+\\pi=\frac{5\\pi}{4}\\).",
        "answerExplanationFr": "Si \\(\\sin(x)=\\cos(x)\\), alors \\(\frac{\\sin(x)}{\\cos(x)} = 1\\) (en supposant que \\(\\cos(x)\neq0\\)). Puisque \\(\frac{\\sin(x)}{\\cos(x)}=\tan(x)\\), l'équation devient \\(\tan(x)=1\\). Les solutions de \\(\tan(x)=1\\) dans l'intervalle \\([0, 2\\pi)\\) sont \\(x=\frac{\\pi}{4}\\) et \\(x=\frac{\\pi}{4}+\\pi=\frac{5\\pi}{4}\\).",
        "difficultyLevel": 2,
        "knowledgePointId": 627,
        "knowledgePointName": "Basic Trigonometric Equations",
        "knowledgePointNameFr": "Équations trigonométriques de base"
      },
      {
        "questionId": 187,
        "sortOrder": 5,
        "points": 1,
        "questionTitle": "Solve Equation with Inverse Function",
        "questionTitleFr": "Résoudre une équation avec une fonction inverse",
        "questionContent": "If \\(\tan^{- 1}(x)=\frac{\\pi}{6}\\), what is the value of \\(x\\)?",
        "questionContentFr": "Si \\(\tan^{-1}(x)=\frac{\\pi}{6}\\), quelle est la valeur de \\(x\\)?",
        "options": "[\"A. \\(\frac{\\sqrt{3}}{3}\\)\", \"B. \\(\\sqrt{3}\\)\", \"C. \\(\frac{1}{2}\\)\", \"D. \\(\frac{\\sqrt{2}}{2}\\)\"]",
        "optionsFr": "[\"A. \\(\frac{\\sqrt{3}}{3}\\)\", \"B. \\(\\sqrt{3}\\)\", \"C. \\(\frac{1}{2}\\)\", \"D. \\(\frac{\\sqrt{2}}{2}\\)\"]",
        "correctAnswer": "A",
        "correctAnswerFr": "A",
        "answerExplanation": "By the definition of the inverse tangent function, if \\(\tan^{-1}(x)=\theta\\), then \\(x = \tan(\theta)\\). Given \\(\tan^{-1}(x)=\frac{\\pi}{6}\\), then \\(x=\tan(\frac{\\pi}{6})\\). We know that \\(\tan(\frac{\\pi}{6})=\frac{\\sqrt{3}}{3}\\).",
        "answerExplanationFr": "Par définition de la fonction tangente inverse, si \\(\tan^{-1}(x)=\theta\\), alors \\(x=\tan(\theta)\\). Étant donné que \\(\tan^{-1}(x)=\frac{\\pi}{6}\\), alors \\(x=\tan(\frac{\\pi}{6})\\). Nous savons que \\(\tan(\frac{\\pi}{6})=\frac{\\sqrt{3}}{3}\\).",
        "difficultyLevel": 2,
        "knowledgePointId": 627,
        "knowledgePointName": "Basic Trigonometric Equations",
        "knowledgePointNameFr": "Équations trigonométriques de base"
      }
    ],
    "easyCount": 0,
    "mediumCount": 5,
    "hardCount": 0
  }
}
```

---

### 1.2 根据知识类型生成AI测试

**接口地址**: `POST /api/ai-test/generate-by-category`

**请求参数**:
```json
{
  "studentId": 48,
  "categoryId": 74,
  "gradeId": 12,
  "questionCount": 10,
  "difficultyLevel": 2,
  "testName": "分类测试",
  "testNameFr": "Test par Catégorie"
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| studentId | Integer | 是 | 学生ID |
| categoryId | Integer | 是 | 知识分类ID |
| gradeId | Integer | 否 | 年级ID |
| questionCount | Integer | 是 | 题目数量 |
| difficultyLevel | Integer | 是 | 难度等级(1-简单,2-中等,3-困难) |
| saveToQuestionBank | Boolean | 否 | 是否保存到题库，默认true |
| testName | String | 否 | 测试名称 |
| testNameFr | String | 否 | 测试名称(法语) |

**响应示例**:
```json
{
  "code": 200,
  "message": "AI测试生成成功",
  "data": {
    "id": 136,
    "studentId": 48,
    "testId": 156,
    "testName": "分类测试",
    "testNameFr": "Test par Catégorie",
    "startTime": "2026-01-21 07:59:24",
    "timeLimit": 60,
    "totalQuestions": 15,
    "totalPoints": 15,
    "testStatus": 1,
    "createAt": "2026-01-20 23:59:23",
    "questions": [
      {
        "questionId": 199,
        "sortOrder": 1,
        "points": 1,
        "questionTitle": "Find Exact Value",
        "questionTitleFr": "Trouver la valeur exacte",
        "questionContent": "Find the exact value of cos(75°) using sum and difference identities. Which of the following is the correct value?",
        "questionContentFr": "Trouvez la valeur exacte de cos(75°) en utilisant les identités de somme et de différence. Quelle est la valeur correcte parmi les suivantes?",
        "options": "[\"A. (√6 - √2)/4\", \"B. (√6 + √2)/4\", \"C. (√2 - √6)/4\", \"D. (-√6 - √2)/4\"]",
        "optionsFr": "[\"A. (√6 - √2)/4\", \"B. (√6 + √2)/4\", \"C. (√2 - √6)/4\", \"D. (-√6 - √2)/4\"]",
        "correctAnswer": "A",
        "correctAnswerFr": "A",
        "answerExplanation": "We can write 75° as 45°+30°. The sum identity for cosine is cos(A + B)=cosAcosB - sinAsinB. Let A = 45° and B = 30°. We know that cos45° = √2/2, cos30° = √3/2, sin45° = √2/2, and sin30° = 1/2. Then cos(75°)=cos(45° + 30°)=cos45°cos30°-sin45°sin30°. Substitute the values: (√2/2)×(√3/2)-(√2/2)×(1/2)=(√6/4)-(√2/4)=(√6 - √2)/4. ",
        "answerExplanationFr": "Nous pouvons écrire 75° comme 45°+30°. L'identité de somme pour le cosinus est cos(A + B)=cosAcosB - sinAsinB. Posons A = 45° et B = 30°. Nous savons que cos45° = √2/2, cos30° = √3/2, sin45° = √2/2 et sin30° = 1/2. Alors cos(75°)=cos(45° + 30°)=cos45°cos30°-sin45°sin30°. Substituons les valeurs: (√2/2)×(√3/2)-(√2/2)×(1/2)=(√6/4)-(√2/4)=(√6 - √2)/4. ",
        "difficultyLevel": 2,
        "knowledgePointId": 625,
        "knowledgePointName": "Sum and Difference Identities",
        "knowledgePointNameFr": "Identités de somme et différence"
      },
      {
        "questionId": 195,
        "sortOrder": 2,
        "points": 1,
        "questionTitle": "Special Triangle Application",
        "questionTitleFr": "Application d'un triangle spécial",
        "questionContent": "In a 30 - 60 - 90 triangle, if the side opposite the 30° angle is 5 units long, what is the length of the hypotenuse?",
        "questionContentFr": "Dans un triangle 30 - 60 - 90, si le côté opposé à l'angle de 30° mesure 5 unités de longueur, quelle est la longueur de l'hypoténuse?",
        "options": "[\"A. 5 units\", \"B. 10 units\", \"C. 5√3 units\", \"D. 10√3 units\"]",
        "optionsFr": "[\"A. 5 unités\", \"B. 10 unités\", \"C. 5√3 unités\", \"D. 10√3 unités\"]",
        "correctAnswer": "B",
        "correctAnswerFr": "B",
        "answerExplanation": "1. In a 30 - 60 - 90 triangle, the relationships between the sides are as follows: if the side opposite the 30° angle is \\(x\\), the side opposite the 60° angle is \\(x\\sqrt{3}\\), and the hypotenuse is \\(2x\\). 2. Given that the side opposite the 30° angle (\\(x\\)) is 5 units long. 3. To find the length of the hypotenuse, we use the formula for the hypotenuse in a 30 - 60 - 90 triangle, which is \\(2x\\). Substitute \\(x = 5\\) into the formula, we get \\(2\times5=10\\) units. So, the length of the hypotenuse is 10 units.",
        "answerExplanationFr": "1. Dans un triangle 30 - 60 - 90, les relations entre les côtés sont les suivantes : si le côté opposé à l'angle de 30° est \\(x\\), le côté opposé à l'angle de 60° est \\(x\\sqrt{3}\\) et l'hypoténuse est \\(2x\\). 2. Étant donné que le côté opposé à l'angle de 30° (\\(x\\)) mesure 5 unités de longueur. 3. Pour trouver la longueur de l'hypoténuse, nous utilisons la formule pour l'hypoténuse dans un triangle 30 - 60 - 90, qui est \\(2x\\). Substituez \\(x = 5\\) dans la formule, nous obtenons \\(2\times5 = 10\\) unités. Donc, la longueur de l'hypoténuse est de 10 unités.",
        "difficultyLevel": 2,
        "knowledgePointId": 621,
        "knowledgePointName": "Special Angles",
        "knowledgePointNameFr": "Angles spéciaux"
      },
      {
        "questionId": 191,
        "sortOrder": 3,
        "points": 1,
        "questionTitle": "Find the Complementary Angle",
        "questionTitleFr": "Trouver l'angle complémentaire",
        "questionContent": "If an angle measures 35 degrees, what is the measure of its complementary angle? ",
        "questionContentFr": "Si un angle mesure 35 degrés, quelle est la mesure de son angle complémentaire? ",
        "options": "[\"A. 55 degrees\", \"B. 65 degrees\", \"C. 145 degrees\", \"D. 155 degrees\"]",
        "optionsFr": "[\"A. 55 degrés\", \"B. 65 degrés\", \"C. 145 degrés\", \"D. 155 degrés\"]",
        "correctAnswer": "A",
        "correctAnswerFr": "A",
        "answerExplanation": "Two angles are complementary if the sum of their measures is 90 degrees. Let the complementary angle be x. Then we have the equation 35 + x=90. To solve for x, we subtract 35 from both sides of the equation: x = 90 - 35. So x = 55 degrees. The answer is A.",
        "answerExplanationFr": "Deux angles sont complémentaires si la somme de leurs mesures est de 90 degrés. Soit l'angle complémentaire x. Alors nous avons l'équation 35 + x = 90. Pour résoudre pour x, nous soustrayons 35 des deux côtés de l'équation : x = 90 - 35. Ainsi, x = 55 degrés. La réponse est A.",
        "difficultyLevel": 2,
        "knowledgePointId": 619,
        "knowledgePointName": "Angle and Degree Measure",
        "knowledgePointNameFr": "Mesure d'angle et degré"
      },
      {
        "questionId": 200,
        "sortOrder": 4,
        "points": 1,
        "questionTitle": "Trig Equation Real - World Application",
        "questionTitleFr": "Application pratique d'une équation trigonométrique",
        "questionContent": "A ladder is leaning against a wall. The angle between the ladder and the ground is \\( \theta \\), and the length of the ladder is 10 meters. The height where the ladder touches the wall is \\( h \\) meters. If \\( h = 5\\sqrt{3}\\) meters, which of the following equations can be used to find the angle \\( \theta \\)?",
        "questionContentFr": "Une échelle est appuyée contre un mur. L'angle entre l'échelle et le sol est \\( \theta \\), et la longueur de l'échelle est de 10 mètres. La hauteur où l'échelle touche le mur est de \\( h \\) mètres. Si \\( h = 5\\sqrt{3}\\) mètres, quelle des équations suivantes peut être utilisée pour trouver l'angle \\( \theta \\)?",
        "options": "[\"A. \\( \\sin\theta=\frac{10}{5\\sqrt{3}}\\) \", \"B. \\( \\sin\theta=\frac{5\\sqrt{3}}{10}\\) \", \"C. \\( \\cos\theta=\frac{5\\sqrt{3}}{10}\\) \", \"D. \\( \tan\theta=\frac{10}{5\\sqrt{3}}\\) \"]",
        "optionsFr": "[\"A. \\( \\sin\theta=\frac{10}{5\\sqrt{3}}\\) \", \"B. \\( \\sin\theta=\frac{5\\sqrt{3}}{10}\\) \", \"C. \\( \\cos\theta=\frac{5\\sqrt{3}}{10}\\) \", \"D. \\( \tan\theta=\frac{10}{5\\sqrt{3}}\\) \"]",
        "correctAnswer": "B",
        "correctAnswerFr": "B",
        "answerExplanation": "1. First, recall the definitions of trigonometric functions in a right - triangle:\n   - In a right - triangle, if we consider the angle \\( \theta \\) between the hypotenuse and the adjacent side to the angle, \\(\\sin\theta=\frac{\text{opposite}}{\text{hypotenuse}}\\), \\(\\cos\theta=\frac{\text{adjacent}}{\text{hypotenuse}}\\), and \\(\tan\theta=\frac{\text{opposite}}{\text{adjacent}}\\).\n   - In the problem of the ladder leaning against the wall, the length of the ladder is the hypotenuse (\\(c = 10\\) meters), the height where the ladder touches the wall is the opposite side to the angle \\( \theta \\) (\\(a=5\\sqrt{3}\\) meters), and the distance from the base of the ladder to the wall is the adjacent side.\n2. Then, apply the sine function:\n   - According to the definition of the sine function \\(\\sin\theta=\frac{\text{opposite}}{\text{hypotenuse}}\\). Here, the opposite side to the angle \\( \theta \\) is the height \\(h = 5\\sqrt{3}\\) meters and the hypotenuse is the length of the ladder, which is 10 meters.\n   - So, \\(\\sin\theta=\frac{5\\sqrt{3}}{10}\\).\n3. Analyze the other options:\n   - Option A: \\(\\sin\theta=\frac{10}{5\\sqrt{3}}\\) is incorrect because the formula for sine has the opposite side over the hypotenuse, and here the values are reversed.\n   - Option C: \\(\\cos\theta=\frac{5\\sqrt{3}}{10}\\) is incorrect because the cosine function is defined as \\(\\cos\theta=\frac{\text{adjacent}}{\text{hypotenuse}}\\), and \\(5\\sqrt{3}\\) is the opposite side, not the adjacent side.\n   - Option D: \\(\tan\theta=\frac{10}{5\\sqrt{3}}\\) is incorrect because the tangent function is \\(\tan\theta=\frac{\text{opposite}}{\text{adjacent}}\\), and the values are reversed and also the correct formula for tangent is not applied properly in this option.",
        "answerExplanationFr": "1. Tout d'abord, rappelez les définitions des fonctions trigonométriques dans un triangle rectangle :\n   - Dans un triangle rectangle, si nous considérons l'angle \\( \theta \\) entre l'hypoténuse et le côté adjacent à l'angle, \\(\\sin\theta=\frac{\text{opposé}}{\text{hypoténuse}}\\), \\(\\cos\theta=\frac{\text{adjacent}}{\text{hypoténuse}}\\) et \\(\tan\theta=\frac{\text{opposé}}{\text{adjacent}}\\).\n   - Dans le problème de l'échelle appuyée contre le mur, la longueur de l'échelle est l'hypoténuse (\\(c = 10\\) mètres), la hauteur où l'échelle touche le mur est le côté opposé à l'angle \\( \theta \\) (\\(a = 5\\sqrt{3}\\) mètres), et la distance de la base de l'échelle au mur est le côté adjacent.\n2. Ensuite, appliquez la fonction sinus :\n   - Selon la définition de la fonction sinus, \\(\\sin\theta=\frac{\text{opposé}}{\text{hypoténuse}}\\). Ici, le côté opposé à l'angle \\( \theta \\) est la hauteur \\(h = 5\\sqrt{3}\\) mètres et l'hypoténuse est la longueur de l'échelle, qui est de 10 mètres.\n   - Donc, \\(\\sin\theta=\frac{5\\sqrt{3}}{10}\\).\n3. Analysez les autres options :\n   - Option A : \\(\\sin\theta=\frac{10}{5\\sqrt{3}}\\) est incorrecte car la formule du sinus a le côté opposé sur l'hypoténuse, et ici les valeurs sont inversées.\n   - Option C : \\(\\cos\theta=\frac{5\\sqrt{3}}{10}\\) est incorrecte car la fonction cosinus est définie comme \\(\\cos\theta=\frac{\text{adjacent}}{\text{hypoténuse}}\\), et \\(5\\sqrt{3}\\) est le côté opposé, pas le côté adjacent.\n   - Option D : \\(\tan\theta=\frac{10}{5\\sqrt{3}}\\) est incorrecte car la fonction tangente est \\(\tan\theta=\frac{\text{opposé}}{\text{adjacent}}\\), et les valeurs sont inversées et la formule correcte de la tangente n'est pas appliquée correctement dans cette option.",
        "difficultyLevel": 2,
        "knowledgePointId": 628,
        "knowledgePointName": "Applications of Trigonometric Equations",
        "knowledgePointNameFr": "Applications des équations trigonométriques"
      },
      {
        "questionId": 188,
        "sortOrder": 5,
        "points": 1,
        "questionTitle": "Translation of a Triangle",
        "questionTitleFr": "Translation d'un triangle",
        "questionContent": "A triangle with vertices at (1, 2), (3, 4), and (5, 2) is translated 3 units to the right and 2 units down. What are the new coordinates of the vertex that was originally at (3, 4)?",
        "questionContentFr": "Un triangle dont les sommets sont situés en (1, 2), (3, 4) et (5, 2) est translaté de 3 unités vers la droite et de 2 unités vers le bas. Quelles sont les nouvelles coordonnées du sommet qui était initialement en (3, 4)?",
        "options": "[\"A. (6, 6)\", \"B. (0, 2)\", \"C. (6, 2)\", \"D. (3, 6)\"]",
        "optionsFr": "[\"A. (6, 6)\", \"B. (0, 2)\", \"C. (6, 2)\", \"D. (3, 6)\"]",
        "correctAnswer": "C",
        "correctAnswerFr": "C",
        "answerExplanation": "To translate a point (x, y) 3 units to the right and 2 units down, we use the transformation rule (x + 3, y - 2). For the point (3, 4), we substitute x = 3 and y = 4 into the rule. So, x + 3 = 3+ 3 = 6 and y - 2 = 4 - 2 = 2. The new coordinates are (6, 2).",
        "answerExplanationFr": "Pour translater un point (x, y) de 3 unités vers la droite et de 2 unités vers le bas, nous utilisons la règle de transformation (x + 3, y - 2). Pour le point (3, 4), nous substituons x = 3 et y = 4 dans la règle. Donc, x + 3 = 3 + 3 = 6 et y - 2 = 4 - 2 = 2. Les nouvelles coordonnées sont (6, 2).",
        "difficultyLevel": 2,
        "knowledgePointId": 596,
        "knowledgePointName": "Transformational Geometry",
        "knowledgePointNameFr": "Géométrie transformationnelle"
      },
      {
        "questionId": 198,
        "sortOrder": 6,
        "points": 1,
        "questionTitle": "Simplify Trig Expression",
        "questionTitleFr": "Simplifier une expression trigonométrique",
        "questionContent": "Simplify the expression sin(α + β)cosβ - cos(α + β)sinβ using sum and difference identities. Which of the following is the correct simplified form?",
        "questionContentFr": "Simplifiez l'expression sin(α + β)cosβ - cos(α + β)sinβ en utilisant les identités de somme et de différence. Quelle est la forme simplifiée correcte parmi les suivantes?",
        "options": "[\"A. sin(α - β)\", \"B. sinα\", \"C. cosα\", \"D. cos(α - β)\"]",
        "optionsFr": "[\"A. sin(α - β)\", \"B. sinα\", \"C. cosα\", \"D. cos(α - β)\"]",
        "correctAnswer": "B",
        "correctAnswerFr": "B",
        "answerExplanation": "We know the difference identity for sine is sin(A - B)=sinAcosB - cosAsinB. In the given expression sin(α + β)cosβ - cos(α + β)sinβ, let A = α + β and B = β. Then, by applying the sine - difference identity, we get sin((α + β)-β). Simplifying the argument of the sine function, (α + β)-β = α. So, sin((α + β)-β)=sinα. ",
        "answerExplanationFr": "Nous savons que l'identité de différence pour le sinus est sin(A - B)=sinAcosB - cosAsinB. Dans l'expression donnée sin(α + β)cosβ - cos(α + β)sinβ, posons A = α + β et B = β. Ensuite, en appliquant l'identité de différence du sinus, nous obtenons sin((α + β)-β). En simplifiant l'argument de la fonction sinus, (α + β)-β = α. Donc, sin((α + β)-β)=sinα. ",
        "difficultyLevel": 2,
        "knowledgePointId": 625,
        "knowledgePointName": "Sum and Difference Identities",
        "knowledgePointNameFr": "Identités de somme et différence"
      },
      {
        "questionId": 189,
        "sortOrder": 7,
        "points": 1,
        "questionTitle": "Reflection of a Point",
        "questionTitleFr": "Réflexion d'un point",
        "questionContent": "A point (2, -3) is reflected over the y - axis. What are the coordinates of the reflected point?",
        "questionContentFr": "Un point (2, -3) est reflété par rapport à l'axe des y. Quelles sont les coordonnées du point réfléchi?",
        "options": "[\"A. (-2, -3)\", \"B. (2, 3)\", \"C. (-2, 3)\", \"D. (3, -2)\"]",
        "optionsFr": "[\"A. (-2, -3)\", \"B. (2, 3)\", \"C. (-2, 3)\", \"D. (3, -2)\"]",
        "correctAnswer": "A",
        "correctAnswerFr": "A",
        "answerExplanation": "When a point (x, y) is reflected over the y - axis, the transformation rule is (-x, y). For the point (2, -3), we substitute x = 2 and y=-3. So, -x=-2 and y remains - 3. The coordinates of the reflected point are (-2, -3).",
        "answerExplanationFr": "Lorsqu'un point (x, y) est reflété par rapport à l'axe des y, la règle de transformation est (-x, y). Pour le point (2, -3), nous substituons x = 2 et y = - 3. Donc, -x=-2 et y reste - 3. Les coordonnées du point réfléchi sont (-2, -3).",
        "difficultyLevel": 2,
        "knowledgePointId": 596,
        "knowledgePointName": "Transformational Geometry",
        "knowledgePointNameFr": "Géométrie transformationnelle"
      },
      {
        "questionId": 196,
        "sortOrder": 8,
        "points": 1,
        "questionTitle": "Pythagorean Identity Application",
        "questionTitleFr": "Application de l'identité pythagoricienne",
        "questionContent": "Given that sinθ = 3/5 and θ is in the second quadrant, what is the value of cosθ using the Pythagorean identity sin²θ + cos²θ = 1?",
        "questionContentFr": "Sachant que sinθ = 3/5 et que θ est dans le deuxième quadrant, quelle est la valeur de cosθ en utilisant l'identité pythagoricienne sin²θ + cos²θ = 1?",
        "options": "[\"A. 4/5\", \"B. -4/5\", \"C. 3/4\", \"D. -3/4\"]",
        "optionsFr": "[\"A. 4/5\", \"B. -4/5\", \"C. 3/4\", \"D. -3/4\"]",
        "correctAnswer": "B",
        "correctAnswerFr": "B",
        "answerExplanation": "1. Start with the Pythagorean identity sin²θ + cos²θ = 1. We know that sinθ = 3/5, so sin²θ=(3/5)² = 9/25. 2. Substitute sin²θ into the identity: 9/25+cos²θ = 1. 3. Solve for cos²θ: cos²θ = 1 - 9/25. Calculate 1 - 9/25 = 25/25 - 9/25 = 16/25. 4. Take the square root of both sides: cosθ = ±√(16/25)=±4/5. 5. Since θ is in the second quadrant, cosine is negative in the second quadrant. So, cosθ=-4/5.",
        "answerExplanationFr": "1. Commencez par l'identité pythagoricienne sin²θ + cos²θ = 1. Nous savons que sinθ = 3/5, donc sin²θ=(3/5)² = 9/25. 2. Substituez sin²θ dans l'identité : 9/25+cos²θ = 1. 3. Résolvez pour cos²θ : cos²θ = 1 - 9/25. Calculez 1 - 9/25 = 25/25 - 9/25 = 16/25. 4. Prenez la racine carrée des deux côtés : cosθ = ±√(16/25)=±4/5. 5. Comme θ est dans le deuxième quadrant, le cosinus est négatif dans le deuxième quadrant. Donc, cosθ=-4/5.",
        "difficultyLevel": 2,
        "knowledgePointId": 624,
        "knowledgePointName": "Basic Trigonometric Identities",
        "knowledgePointNameFr": "Identités trigonométriques de base"
      },
      {
        "questionId": 187,
        "sortOrder": 9,
        "points": 1,
        "questionTitle": "Solve Equation with Inverse Function",
        "questionTitleFr": "Résoudre une équation avec une fonction inverse",
        "questionContent": "If \\(\tan^{- 1}(x)=\frac{\\pi}{6}\\), what is the value of \\(x\\)?",
        "questionContentFr": "Si \\(\tan^{-1}(x)=\frac{\\pi}{6}\\), quelle est la valeur de \\(x\\)?",
        "options": "[\"A. \\(\frac{\\sqrt{3}}{3}\\)\", \"B. \\(\\sqrt{3}\\)\", \"C. \\(\frac{1}{2}\\)\", \"D. \\(\frac{\\sqrt{2}}{2}\\)\"]",
        "optionsFr": "[\"A. \\(\frac{\\sqrt{3}}{3}\\)\", \"B. \\(\\sqrt{3}\\)\", \"C. \\(\frac{1}{2}\\)\", \"D. \\(\frac{\\sqrt{2}}{2}\\)\"]",
        "correctAnswer": "A",
        "correctAnswerFr": "A",
        "answerExplanation": "By the definition of the inverse tangent function, if \\(\tan^{-1}(x)=\theta\\), then \\(x = \tan(\theta)\\). Given \\(\tan^{-1}(x)=\frac{\\pi}{6}\\), then \\(x=\tan(\frac{\\pi}{6})\\). We know that \\(\tan(\frac{\\pi}{6})=\frac{\\sqrt{3}}{3}\\).",
        "answerExplanationFr": "Par définition de la fonction tangente inverse, si \\(\tan^{-1}(x)=\theta\\), alors \\(x=\tan(\theta)\\). Étant donné que \\(\tan^{-1}(x)=\frac{\\pi}{6}\\), alors \\(x=\tan(\frac{\\pi}{6})\\). Nous savons que \\(\tan(\frac{\\pi}{6})=\frac{\\sqrt{3}}{3}\\).",
        "difficultyLevel": 2,
        "knowledgePointId": 627,
        "knowledgePointName": "Basic Trigonometric Equations",
        "knowledgePointNameFr": "Équations trigonométriques de base"
      },
      {
        "questionId": 190,
        "sortOrder": 10,
        "points": 1,
        "questionTitle": "Degree to Radian Conversion",
        "questionTitleFr": "Conversion de degrés en radians",
        "questionContent": "What is the equivalent of 270 degrees in radians? ",
        "questionContentFr": "Quelle est l'équivalence de 270 degrés en radians? ",
        "options": "[\"A. π/2\", \"B. 3π/2\", \"C. π\", \"D. 2π\"]",
        "optionsFr": "[\"A. π/2\", \"B. 3π/2\", \"C. π\", \"D. 2π\"]",
        "correctAnswer": "B",
        "correctAnswerFr": "B",
        "answerExplanation": "To convert degrees to radians, we use the formula: radians = degrees × (π/180). For 270 degrees, we substitute into the formula: 270 × (π/180). First, simplify 270/180. The greatest - common divisor of 270 and 180 is 90. So, 270÷90 = 3 and 180÷90 = 2. Then 270×(π/180)=3π/2 radians. So the answer is B.",
        "answerExplanationFr": "Pour convertir des degrés en radians, nous utilisons la formule : radians = degrés × (π/180). Pour 270 degrés, nous substituons dans la formule : 270 × (π/180). Tout d'abord, simplifions 270/180. Le plus grand diviseur commun de 270 et 180 est 90. Ainsi, 270÷90 = 3 et 180÷90 = 2. Ensuite, 270×(π/180)=3π/2 radians. Donc la réponse est B.",
        "difficultyLevel": 2,
        "knowledgePointId": 619,
        "knowledgePointName": "Angle and Degree Measure",
        "knowledgePointNameFr": "Mesure d'angle et degré"
      },
      {
        "questionId": 193,
        "sortOrder": 11,
        "points": 1,
        "questionTitle": "Determine Trig Value",
        "questionTitleFr": "Déterminer la valeur trigonométrique",
        "questionContent": "If an angle \\( \theta\\) in standard position has its terminal side passing through the point \\((-3,4)\\), what is the value of \\( \\sin\theta\\)?",
        "questionContentFr": "Si un angle \\( \theta\\) en position standard a son côté terminal passant par le point \\((- 3,4)\\), quelle est la valeur de \\( \\sin\theta\\)?",
        "options": "[\"A. \\(\\frac{3}{5}\\)\", \"B. \\(\\frac{4}{5}\\)\", \"C. \\(-\\frac{3}{5}\\)\", \"D. \\(-\\frac{4}{5}\\)\"]",
        "optionsFr": "[\"A. \\(\\frac{3}{5}\\)\", \"B. \\(\\frac{4}{5}\\)\", \"C. \\(-\\frac{3}{5}\\)\", \"D. \\(-\\frac{4}{5}\\)\"]",
        "correctAnswer": "B",
        "correctAnswerFr": "B",
        "answerExplanation": "1. Recall the definition of the sine function for an angle in standard position: If the terminal side of an angle \\( \theta\\) in standard position passes through the point \\((x,y)\\), then \\( \\sin\theta=\frac{y}{r}\\), where \\(r=\\sqrt{x^{2}+y^{2}}\\). 2. Given the point \\((x = - 3,y = 4)\\), first calculate \\(r\\): \\(r=\\sqrt{(-3)^{2}+4^{2}}=\\sqrt{9 + 16}=\\sqrt{25}=5\\). 3. Then, find \\( \\sin\theta\\) using the formula \\( \\sin\theta=\frac{y}{r}\\). Substitute \\(y = 4\\) and \\(r = 5\\) into the formula, we get \\( \\sin\theta=\frac{4}{5}\\). So the answer is B.",
        "answerExplanationFr": "1. Rappelez la définition de la fonction sinus pour un angle en position standard : Si le côté terminal d'un angle \\( \theta\\) en position standard passe par le point \\((x,y)\\), alors \\( \\sin\theta=\frac{y}{r}\\), où \\(r=\\sqrt{x^{2}+y^{2}}\\). 2. Étant donné le point \\((x=-3,y = 4)\\), calculez d'abord \\(r\\) : \\(r=\\sqrt{(-3)^{2}+4^{2}}=\\sqrt{9 + 16}=\\sqrt{25}=5\\). 3. Ensuite, trouvez \\( \\sin\theta\\) en utilisant la formule \\( \\sin\theta=\frac{y}{r}\\). Substituez \\(y = 4\\) et \\(r = 5\\) dans la formule, nous obtenons \\( \\sin\theta=\frac{4}{5}\\). Donc, la réponse est B.",
        "difficultyLevel": 2,
        "knowledgePointId": 620,
        "knowledgePointName": "Angles in Standard Position",
        "knowledgePointNameFr": "Angles en position standard"
      },
      {
        "questionId": 184,
        "sortOrder": 12,
        "points": 1,
        "questionTitle": "Solve Cosine Equation",
        "questionTitleFr": "Résoudre une équation de cosinus",
        "questionContent": "What are the solutions of the equation \\(\\cos(2x)=-\frac{1}{2}\\) in the interval \\([0, \\pi]\\)?",
        "questionContentFr": "Quelles sont les solutions de l'équation \\(\\cos(2x)=-\frac{1}{2}\\) dans l'intervalle \\([0, \\pi]\\)?",
        "options": "[\"A. \\(x=\frac{\\pi}{6}, \frac{5\\pi}{6}\\)\", \"B. \\(x=\frac{\\pi}{3}, \frac{2\\pi}{3}\\)\", \"C. \\(x=\frac{\\pi}{4}, \frac{3\\pi}{4}\\)\", \"D. \\(x=\frac{\\pi}{5}, \frac{4\\pi}{5}\\)\"]",
        "optionsFr": "[\"A. \\(x=\frac{\\pi}{6},\frac{5\\pi}{6}\\)\", \"B. \\(x=\frac{\\pi}{3},\frac{2\\pi}{3}\\)\", \"C. \\(x=\frac{\\pi}{4},\frac{3\\pi}{4}\\)\", \"D. \\(x=\frac{\\pi}{5},\frac{4\\pi}{5}\\)\"]",
        "correctAnswer": "B",
        "correctAnswerFr": "B",
        "answerExplanation": "Let \\(t = 2x\\). Then the equation becomes \\(\\cos(t)=-\frac{1}{2}\\). The solutions of \\(\\cos(t)=-\frac{1}{2}\\) are \\(t=\frac{2\\pi}{3}+2k\\pi\\) and \\(t=\frac{4\\pi}{3}+2k\\pi\\), \\(k\\in\\mathbb{Z}\\). Since \\(t = 2x\\) and \\(x\\in[0,\\pi]\\), then \\(t\\in[0, 2\\pi]\\). For \\(t=\frac{2\\pi}{3}\\), \\(2x=\frac{2\\pi}{3}\\), so \\(x=\frac{\\pi}{3}\\). For \\(t=\frac{4\\pi}{3}\\), \\(2x=\frac{4\\pi}{3}\\), so \\(x=\frac{2\\pi}{3}\\).",
        "answerExplanationFr": "Posons \\(t = 2x\\). Alors l'équation devient \\(\\cos(t)=-\frac{1}{2}\\). Les solutions de \\(\\cos(t)=-\frac{1}{2}\\) sont \\(t=\frac{2\\pi}{3}+2k\\pi\\) et \\(t=\frac{4\\pi}{3}+2k\\pi\\), \\(k\\in\\mathbb{Z}\\). Puisque \\(t = 2x\\) et \\(x\\in[0,\\pi]\\), alors \\(t\\in[0, 2\\pi]\\). Pour \\(t=\frac{2\\pi}{3}\\), \\(2x=\frac{2\\pi}{3}\\), donc \\(x=\frac{\\pi}{3}\\). Pour \\(t=\frac{4\\pi}{3}\\), \\(2x=\frac{4\\pi}{3}\\), donc \\(x=\frac{2\\pi}{3}\\).",
        "difficultyLevel": 2,
        "knowledgePointId": 627,
        "knowledgePointName": "Basic Trigonometric Equations",
        "knowledgePointNameFr": "Équations trigonométriques de base"
      },
      {
        "questionId": 192,
        "sortOrder": 13,
        "points": 1,
        "questionTitle": "Find Reference Angle",
        "questionTitleFr": "Trouver l'angle de référence",
        "questionContent": "An angle of 300° is in standard position. What is its reference angle?",
        "questionContentFr": "Un angle de 300° est en position standard. Quel est son angle de référence?",
        "options": "[\"A. 30°\", \"B. 60°\", \"C. 90°\", \"D. 120°\"]",
        "optionsFr": "[\"A. 30°\", \"B. 60°\", \"C. 90°\", \"D. 120°\"]",
        "correctAnswer": "B",
        "correctAnswerFr": "B",
        "answerExplanation": "1. First, recall the concept of a reference angle: The reference angle is the acute angle formed between the terminal side of the given angle in standard position and the x - axis. 2. For an angle \\( \theta\\) in the fourth quadrant (\\(270^{\\circ}<\theta < 360^{\\circ}\\)), the formula to find the reference angle \\( \\alpha\\) is \\( \\alpha=360^{\\circ}-\theta\\). 3. Given \\( \theta = 300^{\\circ}\\), substitute it into the formula: \\( \\alpha=360^{\\circ}-300^{\\circ}=60^{\\circ}\\). So the reference angle of 300° is 60°, and the answer is B.",
        "answerExplanationFr": "1. Tout d'abord, rappelez le concept d'angle de référence : L'angle de référence est l'angle aigu formé entre le côté terminal de l'angle donné en position standard et l'axe des x. 2. Pour un angle \\( \theta\\) dans le quatrième quadrant (\\(270^{\\circ}<\theta < 360^{\\circ}\\)), la formule pour trouver l'angle de référence \\( \\alpha\\) est \\( \\alpha = 360^{\\circ}-\theta\\). 3. Étant donné \\( \theta=300^{\\circ}\\), substituez - le dans la formule : \\( \\alpha=360^{\\circ}-300^{\\circ}=60^{\\circ}\\). Donc, l'angle de référence de 300° est 60°, et la réponse est B.",
        "difficultyLevel": 2,
        "knowledgePointId": 620,
        "knowledgePointName": "Angles in Standard Position",
        "knowledgePointNameFr": "Angles en position standard"
      },
      {
        "questionId": 194,
        "sortOrder": 14,
        "points": 1,
        "questionTitle": "Special Angle Value",
        "questionTitleFr": "Valeur d'un angle spécial",
        "questionContent": "What is the exact value of sin(120°)?",
        "questionContentFr": "Quelle est la valeur exacte de sin(120°)?",
        "options": "[\"A. -√3/2\", \"B. √3/2\", \"C. -1/2\", \"D. 1/2\"]",
        "optionsFr": "[\"A. -√3/2\", \"B. √3/2\", \"C. -1/2\", \"D. 1/2\"]",
        "correctAnswer": "B",
        "correctAnswerFr": "B",
        "answerExplanation": "1. First, note that 120° can be written as 180° - 60°. We know the identity sin(A - B)=sinAcosB - cosAsinB. For A = 180° and B = 60°, sin(180° - 60°)=sin180°cos60° - cos180°sin60°. 2. Recall the values of special angles: sin180° = 0, cos180°=- 1, cos60° = 1/2, and sin60° = √3/2. 3. Substitute these values into the formula: sin(180° - 60°)=0×(1/2)-(-1)×(√3/2). 4. Simplify the expression: 0 - (-√3/2)=√3/2. So, the exact value of sin(120°) is √3/2.",
        "answerExplanationFr": "1. Tout d'abord, remarquez que 120° peut être écrit comme 180° - 60°. Nous connaissons l'identité sin(A - B)=sinAcosB - cosAsinB. Pour A = 180° et B = 60°, sin(180° - 60°)=sin180°cos60° - cos180°sin60°. 2. Rappelez - vous les valeurs des angles spéciaux : sin180° = 0, cos180°=- 1, cos60° = 1/2 et sin60° = √3/2. 3. Substituez ces valeurs dans la formule : sin(180° - 60°)=0×(1/2)-(-1)×(√3/2). 4. Simplifiez l'expression : 0 - (-√3/2)=√3/2. Donc, la valeur exacte de sin(120°) est √3/2.",
        "difficultyLevel": 2,
        "knowledgePointId": 621,
        "knowledgePointName": "Special Angles",
        "knowledgePointNameFr": "Angles spéciaux"
      },
      {
        "questionId": 197,
        "sortOrder": 15,
        "points": 1,
        "questionTitle": "Quotient Identity Use",
        "questionTitleFr": "Utilisation de l'identité de quotient",
        "questionContent": "If tanθ = 7/24 and cosθ = 24/25, what is the value of sinθ using the quotient identity tanθ = sinθ/cosθ?",
        "questionContentFr": "Si tanθ = 7/24 et cosθ = 24/25, quelle est la valeur de sinθ en utilisant l'identité de quotient tanθ = sinθ/cosθ?",
        "options": "[\"A. 7/25\", \"B. 24/25\", \"C. 7/24\", \"D. 25/7\"]",
        "optionsFr": "[\"A. 7/25\", \"B. 24/25\", \"C. 7/24\", \"D. 25/7\"]",
        "correctAnswer": "A",
        "correctAnswerFr": "A",
        "answerExplanation": "1. Recall the quotient identity tanθ = sinθ/cosθ. 2. We are given that tanθ = 7/24 and cosθ = 24/25. 3. Rearrange the quotient identity to solve for sinθ: sinθ = tanθ×cosθ. 4. Substitute the given values into the formula: sinθ=(7/24)×(24/25). 5. Multiply the fractions: (7×24)/(24×25). The 24 in the numerator and denominator cancels out, leaving sinθ = 7/25.",
        "answerExplanationFr": "1. Rappelez-vous l'identité de quotient tanθ = sinθ/cosθ. 2. Nous savons que tanθ = 7/24 et cosθ = 24/25. 3. Réarrangez l'identité de quotient pour résoudre pour sinθ : sinθ = tanθ×cosθ. 4. Substituez les valeurs données dans la formule : sinθ=(7/24)×(24/25). 5. Multipliez les fractions : (7×24)/(24×25). Le 24 dans le numérateur et le dénominateur s'annule, laissant sinθ = 7/25.",
        "difficultyLevel": 2,
        "knowledgePointId": 624,
        "knowledgePointName": "Basic Trigonometric Identities",
        "knowledgePointNameFr": "Identités trigonométriques de base"
      }
    ],
    "easyCount": 0,
    "mediumCount": 15,
    "hardCount": 0
  }
}
```

---

## 2. 课程生成接口

### 2.1 根据知识点生成课程

**接口地址**: `POST /course/generate`

**请求参数**:
```json
{
  "knowledgePointId":"631",
  "studentId":"48"
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| studentId | Integer | 是 | 学生ID |
| knowledgePointId | Integer | 是 | 知识点ID |
| difficultyLevel | Integer | 否 | 难度等级，不传则自动确定 |

**响应示例**:
```json
{
  "code": 200,
  "message": "课程生成成功",
  "data": {
    "id": 6,
    "studentId": 48,
    "knowledgePointId": 631,
    "difficultyLevel": 1,
    "courseTitle": "Introduction to Logarithmic Functions",
    "courseTitleFr": "Introduction aux fonctions logarithmiques",
    "explanation": "Logarithmic functions are the inverse of exponential functions. An exponential function looks like \\(y = a^x\\), where \\(a\\) is a positive number and \\(x\\) is the exponent. A logarithmic function, written as \\(y=\\log_a(x)\\), answers the question: 'To what power do we need to raise the base \\(a\\) to get \\(x\\)'? For example, if we have \\(\\log_2(8)\\), we're asking 'What power do we raise 2 to, to get 8?' Since \\(2^3 = 8\\), then \\(\\log_2(8)=3\\).\n\nGraphing logarithmic functions helps us visualize how they behave. The graph of \\(y = \\log_a(x)\\) has a vertical asymptote at \\(x = 0\\) (the y - axis) and passes through the point \\((1,0)\\) because \\(a^0=1\\) for any positive \\(a\neq1\\).\n\nLogarithmic properties are rules that make it easier to work with logarithmic expressions. Some important properties are: \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\), \\(\\log_a(\frac{M}{N})=\\log_a(M)-\\log_a(N)\\), and \\(\\log_a(M^p)=p\\log_a(M)\\). These properties are useful in many real - world scenarios, like measuring the intensity of earthquakes (Richter scale), the loudness of sounds (decibel scale), and in finance for calculating compound interest.",
    "explanationFr": "Les fonctions logarithmiques sont l'inverse des fonctions exponentielles. Une fonction exponentielle a la forme \\(y = a^x\\), où \\(a\\) est un nombre positif et \\(x\\) est l'exposant. Une fonction logarithmique, écrite sous la forme \\(y=\\log_a(x)\\), répond à la question : 'À quelle puissance devons - nous élever la base \\(a\\) pour obtenir \\(x\\)'? Par exemple, si nous avons \\(\\log_2(8)\\), nous nous demandons 'À quelle puissance devons - nous élever 2 pour obtenir 8?' Puisque \\(2^3 = 8\\), alors \\(\\log_2(8)=3\\).\n\nLe tracé des fonctions logarithmiques nous aide à visualiser leur comportement. Le graphe de \\(y = \\log_a(x)\\) a une asymptote verticale en \\(x = 0\\) (l'axe des y) et passe par le point \\((1,0)\\) car \\(a^0 = 1\\) pour tout \\(a\\) positif différent de 1.\n\nLes propriétés logarithmiques sont des règles qui facilitent le travail avec les expressions logarithmiques. Certaines propriétés importantes sont : \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\), \\(\\log_a(\frac{M}{N})=\\log_a(M)-\\log_a(N)\\) et \\(\\log_a(M^p)=p\\log_a(M)\\). Ces propriétés sont utiles dans de nombreux scénarios du monde réel, comme la mesure de l'intensité des tremblements de terre (échelle de Richter), la mesure du volume des sons (échelle des décibels) et en finance pour calculer les intérêts composés.",
    "examples": "**Example 1: Evaluating a Logarithm**\nFind the value of \\(\\log_3(27)\\).\nStep 1: Recall the definition of a logarithm. We need to find the exponent \\(x\\) such that \\(3^x = 27\\).\nStep 2: Since \\(3^3=27\\), then \\(\\log_3(27)=3\\).\n\n**Example 2: Using Logarithmic Properties**\nSimplify \\(\\log_5(25x)\\).\nStep 1: Use the property \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\). Here, \\(M = 25\\) and \\(N=x\\).\nStep 2: We know that \\(\\log_5(25)\\) because \\(5^2 = 25\\), so \\(\\log_5(25)=2\\). Then \\(\\log_5(25x)=\\log_5(25)+\\log_5(x)=2+\\log_5(x)\\).\n\n**Example 3: Graphing a Logarithmic Function**\nGraph \\(y=\\log_2(x)\\).\nStep 1: Find some key points. When \\(x = 1\\), \\(y=\\log_2(1)=0\\) (since \\(2^0 = 1\\)). When \\(x = 2\\), \\(y=\\log_2(2)=1\\) (since \\(2^1 = 2\\)). When \\(x = 4\\), \\(y=\\log_2(4)=2\\) (since \\(2^2 = 4\\)).\nStep 2: Draw a vertical asymptote at \\(x = 0\\). Then plot the points \\((1,0)\\), \\((2,1)\\), and \\((4,2)\\) and connect them with a smooth curve that approaches the asymptote as \\(x\\) gets closer to 0.",
    "examplesFr": "**Exemple 1 : Évaluation d'un logarithme**\nTrouvez la valeur de \\(\\log_3(27)\\).\nÉtape 1 : Rappelez - vous la définition d'un logarithme. Nous devons trouver l'exposant \\(x\\) tel que \\(3^x = 27\\).\nÉtape 2 : Puisque \\(3^3 = 27\\), alors \\(\\log_3(27)=3\\).\n\n**Exemple 2 : Utilisation des propriétés logarithmiques**\nSimplifiez \\(\\log_5(25x)\\).\nÉtape 1 : Utilisez la propriété \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\). Ici, \\(M = 25\\) et \\(N=x\\).\nÉtape 2 : Nous savons que \\(\\log_5(25)\\) car \\(5^2 = 25\\), donc \\(\\log_5(25)=2\\). Alors \\(\\log_5(25x)=\\log_5(25)+\\log_5(x)=2+\\log_5(x)\\).\n\n**Exemple 3 : Tracé d'une fonction logarithmique**\nTracez le graphe de \\(y=\\log_2(x)\\).\nÉtape 1 : Trouvez quelques points clés. Lorsque \\(x = 1\\), \\(y=\\log_2(1)=0\\) (puisque \\(2^0 = 1\\)). Lorsque \\(x = 2\\), \\(y=\\log_2(2)=1\\) (puisque \\(2^1 = 2\\)). Lorsque \\(x = 4\\), \\(y=\\log_2(4)=2\\) (puisque \\(2^2 = 4\\)).\nÉtape 2 : Tracez une asymptote verticale en \\(x = 0\\). Ensuite, placez les points \\((1,0)\\), \\((2,1)\\) et \\((4,2)\\) et reliez - les avec une courbe lisse qui s'approche de l'asymptote lorsque \\(x\\) se rapproche de 0.",
    "keySummary": "1. Definition of a logarithmic function: \\(y=\\log_a(x)\\) means \\(a^y=x\\).\n2. Key graph features: Vertical asymptote at \\(x = 0\\), passes through \\((1,0)\\).\n3. Logarithmic properties:\n   - \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\)\n   - \\(\\log_a(\frac{M}{N})=\\log_a(M)-\\log_a(N)\\)\n   - \\(\\log_a(M^p)=p\\log_a(M)\\)",
    "keySummaryFr": "1. Définition d'une fonction logarithmique : \\(y=\\log_a(x)\\) signifie \\(a^y=x\\).\n2. Caractéristiques clés du graphe : Asymptote verticale en \\(x = 0\\), passe par le point \\((1,0)\\).\n3. Propriétés logarithmiques :\n   - \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\)\n   - \\(\\log_a(\frac{M}{N})=\\log_a(M)-\\log_a(N)\\)\n   - \\(\\log_a(M^p)=p\\log_a(M)\\)",
    "additionalInfo": "**Tips**: \n- When evaluating logarithms, try to rewrite the number inside the logarithm as a power of the base. For example, if you have \\(\\log_4(64)\\), think '4 to what power is 64?' Since \\(4^3 = 64\\), the answer is 3.\n- When graphing, always start by finding the vertical asymptote and a few key points.\n\n**Common mistakes to avoid**: \n- Forgetting the base of the logarithm. \\(\\log(x)\\) usually means \\(\\log_{10}(x)\\) in most math courses, but in some contexts it could mean \\(\\log_e(x)\\) (natural logarithm).\n- Misapplying the logarithmic properties. For example, \\(\\log_a(M + N)\neq\\log_a(M)+\\log_a(N)\\).\n\n**Connections to other topics**: Logarithmic functions are closely related to exponential functions. They are also used in solving exponential equations. In calculus, logarithms are important for integration and differentiation problems.",
    "additionalInfoFr": "**Conseils** : \n- Lors de l'évaluation des logarithmes, essayez de réécrire le nombre à l'intérieur du logarithme comme une puissance de la base. Par exemple, si vous avez \\(\\log_4(64)\\), demandez - vous '4 à quelle puissance donne 64?' Puisque \\(4^3 = 64\\), la réponse est 3.\n- Lors du tracé du graphe, commencez toujours par trouver l'asymptote verticale et quelques points clés.\n\n**Erreurs courantes à éviter** : \n- Oublier la base du logarithme. \\(\\log(x)\\) signifie généralement \\(\\log_{10}(x)\\) dans la plupart des cours de mathématiques, mais dans certains contextes, cela pourrait signifier \\(\\log_e(x)\\) (logarithme naturel).\n- Appliquer incorrectement les propriétés logarithmiques. Par exemple, \\(\\log_a(M + N)\neq\\log_a(M)+\\log_a(N)\\).\n\n**Liens avec d'autres sujets** : Les fonctions logarithmiques sont étroitement liées aux fonctions exponentielles. Elles sont également utilisées pour résoudre les équations exponentielles. En calcul différentiel et intégral, les logarithmes sont importants pour les problèmes d'intégration et de différentiation.",
    "generationSource": "AI",
    "modelId": "ep-20250421140255-d6sfx",
    "promptUsed": "Please generate a comprehensive course for the following knowledge point:\n\nKnowledge Point Name: Logarithmic Functions\nKnowledge Point Name (French): Fonctions logarithmiques\nDescription: Understanding logarithmic functions\nLearning Objectives: Understand logarithmic functions; Graph log functions; Understand log properties\nDifficulty Level: Easy (beginner)\n\nPlease generate the course content in the following JSON format:\n{\n  \"courseTitle\": \"Course title in English\",\n  \"courseTitleFr\": \"Course title in French\",\n  \"explanation\": \"Detailed explanation of the knowledge point in English, suitable for Easy (beginner) level\",\n  \"explanationFr\": \"Detailed explanation in French\",\n  \"examples\": \"2-3 worked examples with step-by-step solutions in English\",\n  \"examplesFr\": \"Same examples in French\",\n  \"keySummary\": \"Key points and formulas to remember in English\",\n  \"keySummaryFr\": \"Key summary in French\",\n  \"additionalInfo\": \"Tips, common mistakes to avoid, and connections to other topics in English\",\n  \"additionalInfoFr\": \"Additional info in French\"\n}\n\nImportant requirements:\n1. Content must be appropriate for the Easy (beginner) difficulty level\n2. Use clear, student-friendly language\n3. Include practical examples relevant to real-world applications\n4. Ensure French translations are accurate and natural\n5. Return ONLY valid JSON, no additional text\n",
    "knowledgePoint": {
      "id": 631,
      "gradeId": 12,
      "categoryId": 73,
      "pointName": "Logarithmic Functions",
      "pointNameFr": "Fonctions logarithmiques",
      "pointCode": "GR12_ALG_026",
      "description": "Understanding logarithmic functions",
      "descriptionFr": "Comprendre les fonctions logarithmiques",
      "content": "Understand logarithmic functions as inverses of exponential functions. Graph logarithmic functions. Understand properties of logarithms. Apply logarithmic functions to solve problems.",
      "contentFr": "Comprendre les fonctions logarithmiques comme inverses des fonctions exponentielles. Représenter graphiquement des fonctions logarithmiques. Comprendre les propriétés des logarithmes. Appliquer des fonctions logarithmiques pour résoudre des problèmes.",
      "difficultyLevel": 3,
      "sortOrder": 37,
      "learningObjectives": "Understand logarithmic functions; Graph log functions; Understand log properties",
      "learningObjectivesFr": "Comprendre les fonctions logarithmiques; Représenter graphiquement des fonctions log; Comprendre les propriétés des logs",
      "createAt": "2025-12-31 00:31:57",
      "updateAt": "2025-12-31 00:31:57",
      "deleteFlag": "N"
    }
  }
}
```

---

### 2.2 根据ID查询课程

**接口地址**: `GET /course/{id}`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Integer | 是 | 课程ID (路径参数) |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 6,
    "studentId": 48,
    "knowledgePointId": 631,
    "difficultyLevel": 1,
    "courseTitle": "Introduction to Logarithmic Functions",
    "courseTitleFr": "Introduction aux fonctions logarithmiques",
    "explanation": "Logarithmic functions are the inverse of exponential functions. An exponential function looks like \\(y = a^x\\), where \\(a\\) is a positive number and \\(x\\) is the exponent. A logarithmic function, written as \\(y=\\log_a(x)\\), answers the question: 'To what power do we need to raise the base \\(a\\) to get \\(x\\)'? For example, if we have \\(\\log_2(8)\\), we're asking 'What power do we raise 2 to, to get 8?' Since \\(2^3 = 8\\), then \\(\\log_2(8)=3\\).\n\nGraphing logarithmic functions helps us visualize how they behave. The graph of \\(y = \\log_a(x)\\) has a vertical asymptote at \\(x = 0\\) (the y - axis) and passes through the point \\((1,0)\\) because \\(a^0=1\\) for any positive \\(a\neq1\\).\n\nLogarithmic properties are rules that make it easier to work with logarithmic expressions. Some important properties are: \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\), \\(\\log_a(\frac{M}{N})=\\log_a(M)-\\log_a(N)\\), and \\(\\log_a(M^p)=p\\log_a(M)\\). These properties are useful in many real - world scenarios, like measuring the intensity of earthquakes (Richter scale), the loudness of sounds (decibel scale), and in finance for calculating compound interest.",
    "explanationFr": "Les fonctions logarithmiques sont l'inverse des fonctions exponentielles. Une fonction exponentielle a la forme \\(y = a^x\\), où \\(a\\) est un nombre positif et \\(x\\) est l'exposant. Une fonction logarithmique, écrite sous la forme \\(y=\\log_a(x)\\), répond à la question : 'À quelle puissance devons - nous élever la base \\(a\\) pour obtenir \\(x\\)'? Par exemple, si nous avons \\(\\log_2(8)\\), nous nous demandons 'À quelle puissance devons - nous élever 2 pour obtenir 8?' Puisque \\(2^3 = 8\\), alors \\(\\log_2(8)=3\\).\n\nLe tracé des fonctions logarithmiques nous aide à visualiser leur comportement. Le graphe de \\(y = \\log_a(x)\\) a une asymptote verticale en \\(x = 0\\) (l'axe des y) et passe par le point \\((1,0)\\) car \\(a^0 = 1\\) pour tout \\(a\\) positif différent de 1.\n\nLes propriétés logarithmiques sont des règles qui facilitent le travail avec les expressions logarithmiques. Certaines propriétés importantes sont : \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\), \\(\\log_a(\frac{M}{N})=\\log_a(M)-\\log_a(N)\\) et \\(\\log_a(M^p)=p\\log_a(M)\\). Ces propriétés sont utiles dans de nombreux scénarios du monde réel, comme la mesure de l'intensité des tremblements de terre (échelle de Richter), la mesure du volume des sons (échelle des décibels) et en finance pour calculer les intérêts composés.",
    "examples": "**Example 1: Evaluating a Logarithm**\nFind the value of \\(\\log_3(27)\\).\nStep 1: Recall the definition of a logarithm. We need to find the exponent \\(x\\) such that \\(3^x = 27\\).\nStep 2: Since \\(3^3=27\\), then \\(\\log_3(27)=3\\).\n\n**Example 2: Using Logarithmic Properties**\nSimplify \\(\\log_5(25x)\\).\nStep 1: Use the property \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\). Here, \\(M = 25\\) and \\(N=x\\).\nStep 2: We know that \\(\\log_5(25)\\) because \\(5^2 = 25\\), so \\(\\log_5(25)=2\\). Then \\(\\log_5(25x)=\\log_5(25)+\\log_5(x)=2+\\log_5(x)\\).\n\n**Example 3: Graphing a Logarithmic Function**\nGraph \\(y=\\log_2(x)\\).\nStep 1: Find some key points. When \\(x = 1\\), \\(y=\\log_2(1)=0\\) (since \\(2^0 = 1\\)). When \\(x = 2\\), \\(y=\\log_2(2)=1\\) (since \\(2^1 = 2\\)). When \\(x = 4\\), \\(y=\\log_2(4)=2\\) (since \\(2^2 = 4\\)).\nStep 2: Draw a vertical asymptote at \\(x = 0\\). Then plot the points \\((1,0)\\), \\((2,1)\\), and \\((4,2)\\) and connect them with a smooth curve that approaches the asymptote as \\(x\\) gets closer to 0.",
    "examplesFr": "**Exemple 1 : Évaluation d'un logarithme**\nTrouvez la valeur de \\(\\log_3(27)\\).\nÉtape 1 : Rappelez - vous la définition d'un logarithme. Nous devons trouver l'exposant \\(x\\) tel que \\(3^x = 27\\).\nÉtape 2 : Puisque \\(3^3 = 27\\), alors \\(\\log_3(27)=3\\).\n\n**Exemple 2 : Utilisation des propriétés logarithmiques**\nSimplifiez \\(\\log_5(25x)\\).\nÉtape 1 : Utilisez la propriété \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\). Ici, \\(M = 25\\) et \\(N=x\\).\nÉtape 2 : Nous savons que \\(\\log_5(25)\\) car \\(5^2 = 25\\), donc \\(\\log_5(25)=2\\). Alors \\(\\log_5(25x)=\\log_5(25)+\\log_5(x)=2+\\log_5(x)\\).\n\n**Exemple 3 : Tracé d'une fonction logarithmique**\nTracez le graphe de \\(y=\\log_2(x)\\).\nÉtape 1 : Trouvez quelques points clés. Lorsque \\(x = 1\\), \\(y=\\log_2(1)=0\\) (puisque \\(2^0 = 1\\)). Lorsque \\(x = 2\\), \\(y=\\log_2(2)=1\\) (puisque \\(2^1 = 2\\)). Lorsque \\(x = 4\\), \\(y=\\log_2(4)=2\\) (puisque \\(2^2 = 4\\)).\nÉtape 2 : Tracez une asymptote verticale en \\(x = 0\\). Ensuite, placez les points \\((1,0)\\), \\((2,1)\\) et \\((4,2)\\) et reliez - les avec une courbe lisse qui s'approche de l'asymptote lorsque \\(x\\) se rapproche de 0.",
    "keySummary": "1. Definition of a logarithmic function: \\(y=\\log_a(x)\\) means \\(a^y=x\\).\n2. Key graph features: Vertical asymptote at \\(x = 0\\), passes through \\((1,0)\\).\n3. Logarithmic properties:\n   - \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\)\n   - \\(\\log_a(\frac{M}{N})=\\log_a(M)-\\log_a(N)\\)\n   - \\(\\log_a(M^p)=p\\log_a(M)\\)",
    "keySummaryFr": "1. Définition d'une fonction logarithmique : \\(y=\\log_a(x)\\) signifie \\(a^y=x\\).\n2. Caractéristiques clés du graphe : Asymptote verticale en \\(x = 0\\), passe par le point \\((1,0)\\).\n3. Propriétés logarithmiques :\n   - \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\)\n   - \\(\\log_a(\frac{M}{N})=\\log_a(M)-\\log_a(N)\\)\n   - \\(\\log_a(M^p)=p\\log_a(M)\\)",
    "additionalInfo": "**Tips**: \n- When evaluating logarithms, try to rewrite the number inside the logarithm as a power of the base. For example, if you have \\(\\log_4(64)\\), think '4 to what power is 64?' Since \\(4^3 = 64\\), the answer is 3.\n- When graphing, always start by finding the vertical asymptote and a few key points.\n\n**Common mistakes to avoid**: \n- Forgetting the base of the logarithm. \\(\\log(x)\\) usually means \\(\\log_{10}(x)\\) in most math courses, but in some contexts it could mean \\(\\log_e(x)\\) (natural logarithm).\n- Misapplying the logarithmic properties. For example, \\(\\log_a(M + N)\neq\\log_a(M)+\\log_a(N)\\).\n\n**Connections to other topics**: Logarithmic functions are closely related to exponential functions. They are also used in solving exponential equations. In calculus, logarithms are important for integration and differentiation problems.",
    "additionalInfoFr": "**Conseils** : \n- Lors de l'évaluation des logarithmes, essayez de réécrire le nombre à l'intérieur du logarithme comme une puissance de la base. Par exemple, si vous avez \\(\\log_4(64)\\), demandez - vous '4 à quelle puissance donne 64?' Puisque \\(4^3 = 64\\), la réponse est 3.\n- Lors du tracé du graphe, commencez toujours par trouver l'asymptote verticale et quelques points clés.\n\n**Erreurs courantes à éviter** : \n- Oublier la base du logarithme. \\(\\log(x)\\) signifie généralement \\(\\log_{10}(x)\\) dans la plupart des cours de mathématiques, mais dans certains contextes, cela pourrait signifier \\(\\log_e(x)\\) (logarithme naturel).\n- Appliquer incorrectement les propriétés logarithmiques. Par exemple, \\(\\log_a(M + N)\neq\\log_a(M)+\\log_a(N)\\).\n\n**Liens avec d'autres sujets** : Les fonctions logarithmiques sont étroitement liées aux fonctions exponentielles. Elles sont également utilisées pour résoudre les équations exponentielles. En calcul différentiel et intégral, les logarithmes sont importants pour les problèmes d'intégration et de différentiation.",
    "generationSource": "AI",
    "modelId": "ep-20250421140255-d6sfx",
    "promptUsed": "Please generate a comprehensive course for the following knowledge point:\n\nKnowledge Point Name: Logarithmic Functions\nKnowledge Point Name (French): Fonctions logarithmiques\nDescription: Understanding logarithmic functions\nLearning Objectives: Understand logarithmic functions; Graph log functions; Understand log properties\nDifficulty Level: Easy (beginner)\n\nPlease generate the course content in the following JSON format:\n{\n  \"courseTitle\": \"Course title in English\",\n  \"courseTitleFr\": \"Course title in French\",\n  \"explanation\": \"Detailed explanation of the knowledge point in English, suitable for Easy (beginner) level\",\n  \"explanationFr\": \"Detailed explanation in French\",\n  \"examples\": \"2-3 worked examples with step-by-step solutions in English\",\n  \"examplesFr\": \"Same examples in French\",\n  \"keySummary\": \"Key points and formulas to remember in English\",\n  \"keySummaryFr\": \"Key summary in French\",\n  \"additionalInfo\": \"Tips, common mistakes to avoid, and connections to other topics in English\",\n  \"additionalInfoFr\": \"Additional info in French\"\n}\n\nImportant requirements:\n1. Content must be appropriate for the Easy (beginner) difficulty level\n2. Use clear, student-friendly language\n3. Include practical examples relevant to real-world applications\n4. Ensure French translations are accurate and natural\n5. Return ONLY valid JSON, no additional text\n",
    "createAt": "2026-01-21 00:16:09",
    "updateAt": "2026-01-21 00:16:09",
    "deleteFlag": "0"
  }
}
```

---

### 2.3 根据学生ID查询课程列表

**接口地址**: `GET /course/student/{studentId}`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| studentId | Integer | 是 | 学生ID (路径参数) |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 6,
      "studentId": 48,
      "knowledgePointId": 631,
      "difficultyLevel": 1,
      "courseTitle": "Introduction to Logarithmic Functions",
      "courseTitleFr": "Introduction aux fonctions logarithmiques",
      "explanation": "Logarithmic functions are the inverse of exponential functions. An exponential function looks like \\(y = a^x\\), where \\(a\\) is a positive number and \\(x\\) is the exponent. A logarithmic function, written as \\(y=\\log_a(x)\\), answers the question: 'To what power do we need to raise the base \\(a\\) to get \\(x\\)'? For example, if we have \\(\\log_2(8)\\), we're asking 'What power do we raise 2 to, to get 8?' Since \\(2^3 = 8\\), then \\(\\log_2(8)=3\\).\n\nGraphing logarithmic functions helps us visualize how they behave. The graph of \\(y = \\log_a(x)\\) has a vertical asymptote at \\(x = 0\\) (the y - axis) and passes through the point \\((1,0)\\) because \\(a^0=1\\) for any positive \\(a\neq1\\).\n\nLogarithmic properties are rules that make it easier to work with logarithmic expressions. Some important properties are: \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\), \\(\\log_a(\frac{M}{N})=\\log_a(M)-\\log_a(N)\\), and \\(\\log_a(M^p)=p\\log_a(M)\\). These properties are useful in many real - world scenarios, like measuring the intensity of earthquakes (Richter scale), the loudness of sounds (decibel scale), and in finance for calculating compound interest.",
      "explanationFr": "Les fonctions logarithmiques sont l'inverse des fonctions exponentielles. Une fonction exponentielle a la forme \\(y = a^x\\), où \\(a\\) est un nombre positif et \\(x\\) est l'exposant. Une fonction logarithmique, écrite sous la forme \\(y=\\log_a(x)\\), répond à la question : 'À quelle puissance devons - nous élever la base \\(a\\) pour obtenir \\(x\\)'? Par exemple, si nous avons \\(\\log_2(8)\\), nous nous demandons 'À quelle puissance devons - nous élever 2 pour obtenir 8?' Puisque \\(2^3 = 8\\), alors \\(\\log_2(8)=3\\).\n\nLe tracé des fonctions logarithmiques nous aide à visualiser leur comportement. Le graphe de \\(y = \\log_a(x)\\) a une asymptote verticale en \\(x = 0\\) (l'axe des y) et passe par le point \\((1,0)\\) car \\(a^0 = 1\\) pour tout \\(a\\) positif différent de 1.\n\nLes propriétés logarithmiques sont des règles qui facilitent le travail avec les expressions logarithmiques. Certaines propriétés importantes sont : \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\), \\(\\log_a(\frac{M}{N})=\\log_a(M)-\\log_a(N)\\) et \\(\\log_a(M^p)=p\\log_a(M)\\). Ces propriétés sont utiles dans de nombreux scénarios du monde réel, comme la mesure de l'intensité des tremblements de terre (échelle de Richter), la mesure du volume des sons (échelle des décibels) et en finance pour calculer les intérêts composés.",
      "examples": "**Example 1: Evaluating a Logarithm**\nFind the value of \\(\\log_3(27)\\).\nStep 1: Recall the definition of a logarithm. We need to find the exponent \\(x\\) such that \\(3^x = 27\\).\nStep 2: Since \\(3^3=27\\), then \\(\\log_3(27)=3\\).\n\n**Example 2: Using Logarithmic Properties**\nSimplify \\(\\log_5(25x)\\).\nStep 1: Use the property \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\). Here, \\(M = 25\\) and \\(N=x\\).\nStep 2: We know that \\(\\log_5(25)\\) because \\(5^2 = 25\\), so \\(\\log_5(25)=2\\). Then \\(\\log_5(25x)=\\log_5(25)+\\log_5(x)=2+\\log_5(x)\\).\n\n**Example 3: Graphing a Logarithmic Function**\nGraph \\(y=\\log_2(x)\\).\nStep 1: Find some key points. When \\(x = 1\\), \\(y=\\log_2(1)=0\\) (since \\(2^0 = 1\\)). When \\(x = 2\\), \\(y=\\log_2(2)=1\\) (since \\(2^1 = 2\\)). When \\(x = 4\\), \\(y=\\log_2(4)=2\\) (since \\(2^2 = 4\\)).\nStep 2: Draw a vertical asymptote at \\(x = 0\\). Then plot the points \\((1,0)\\), \\((2,1)\\), and \\((4,2)\\) and connect them with a smooth curve that approaches the asymptote as \\(x\\) gets closer to 0.",
      "examplesFr": "**Exemple 1 : Évaluation d'un logarithme**\nTrouvez la valeur de \\(\\log_3(27)\\).\nÉtape 1 : Rappelez - vous la définition d'un logarithme. Nous devons trouver l'exposant \\(x\\) tel que \\(3^x = 27\\).\nÉtape 2 : Puisque \\(3^3 = 27\\), alors \\(\\log_3(27)=3\\).\n\n**Exemple 2 : Utilisation des propriétés logarithmiques**\nSimplifiez \\(\\log_5(25x)\\).\nÉtape 1 : Utilisez la propriété \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\). Ici, \\(M = 25\\) et \\(N=x\\).\nÉtape 2 : Nous savons que \\(\\log_5(25)\\) car \\(5^2 = 25\\), donc \\(\\log_5(25)=2\\). Alors \\(\\log_5(25x)=\\log_5(25)+\\log_5(x)=2+\\log_5(x)\\).\n\n**Exemple 3 : Tracé d'une fonction logarithmique**\nTracez le graphe de \\(y=\\log_2(x)\\).\nÉtape 1 : Trouvez quelques points clés. Lorsque \\(x = 1\\), \\(y=\\log_2(1)=0\\) (puisque \\(2^0 = 1\\)). Lorsque \\(x = 2\\), \\(y=\\log_2(2)=1\\) (puisque \\(2^1 = 2\\)). Lorsque \\(x = 4\\), \\(y=\\log_2(4)=2\\) (puisque \\(2^2 = 4\\)).\nÉtape 2 : Tracez une asymptote verticale en \\(x = 0\\). Ensuite, placez les points \\((1,0)\\), \\((2,1)\\) et \\((4,2)\\) et reliez - les avec une courbe lisse qui s'approche de l'asymptote lorsque \\(x\\) se rapproche de 0.",
      "keySummary": "1. Definition of a logarithmic function: \\(y=\\log_a(x)\\) means \\(a^y=x\\).\n2. Key graph features: Vertical asymptote at \\(x = 0\\), passes through \\((1,0)\\).\n3. Logarithmic properties:\n   - \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\)\n   - \\(\\log_a(\frac{M}{N})=\\log_a(M)-\\log_a(N)\\)\n   - \\(\\log_a(M^p)=p\\log_a(M)\\)",
      "keySummaryFr": "1. Définition d'une fonction logarithmique : \\(y=\\log_a(x)\\) signifie \\(a^y=x\\).\n2. Caractéristiques clés du graphe : Asymptote verticale en \\(x = 0\\), passe par le point \\((1,0)\\).\n3. Propriétés logarithmiques :\n   - \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\)\n   - \\(\\log_a(\frac{M}{N})=\\log_a(M)-\\log_a(N)\\)\n   - \\(\\log_a(M^p)=p\\log_a(M)\\)",
      "additionalInfo": "**Tips**: \n- When evaluating logarithms, try to rewrite the number inside the logarithm as a power of the base. For example, if you have \\(\\log_4(64)\\), think '4 to what power is 64?' Since \\(4^3 = 64\\), the answer is 3.\n- When graphing, always start by finding the vertical asymptote and a few key points.\n\n**Common mistakes to avoid**: \n- Forgetting the base of the logarithm. \\(\\log(x)\\) usually means \\(\\log_{10}(x)\\) in most math courses, but in some contexts it could mean \\(\\log_e(x)\\) (natural logarithm).\n- Misapplying the logarithmic properties. For example, \\(\\log_a(M + N)\neq\\log_a(M)+\\log_a(N)\\).\n\n**Connections to other topics**: Logarithmic functions are closely related to exponential functions. They are also used in solving exponential equations. In calculus, logarithms are important for integration and differentiation problems.",
      "additionalInfoFr": "**Conseils** : \n- Lors de l'évaluation des logarithmes, essayez de réécrire le nombre à l'intérieur du logarithme comme une puissance de la base. Par exemple, si vous avez \\(\\log_4(64)\\), demandez - vous '4 à quelle puissance donne 64?' Puisque \\(4^3 = 64\\), la réponse est 3.\n- Lors du tracé du graphe, commencez toujours par trouver l'asymptote verticale et quelques points clés.\n\n**Erreurs courantes à éviter** : \n- Oublier la base du logarithme. \\(\\log(x)\\) signifie généralement \\(\\log_{10}(x)\\) dans la plupart des cours de mathématiques, mais dans certains contextes, cela pourrait signifier \\(\\log_e(x)\\) (logarithme naturel).\n- Appliquer incorrectement les propriétés logarithmiques. Par exemple, \\(\\log_a(M + N)\neq\\log_a(M)+\\log_a(N)\\).\n\n**Liens avec d'autres sujets** : Les fonctions logarithmiques sont étroitement liées aux fonctions exponentielles. Elles sont également utilisées pour résoudre les équations exponentielles. En calcul différentiel et intégral, les logarithmes sont importants pour les problèmes d'intégration et de différentiation.",
      "generationSource": "AI",
      "modelId": "ep-20250421140255-d6sfx",
      "promptUsed": "Please generate a comprehensive course for the following knowledge point:\n\nKnowledge Point Name: Logarithmic Functions\nKnowledge Point Name (French): Fonctions logarithmiques\nDescription: Understanding logarithmic functions\nLearning Objectives: Understand logarithmic functions; Graph log functions; Understand log properties\nDifficulty Level: Easy (beginner)\n\nPlease generate the course content in the following JSON format:\n{\n  \"courseTitle\": \"Course title in English\",\n  \"courseTitleFr\": \"Course title in French\",\n  \"explanation\": \"Detailed explanation of the knowledge point in English, suitable for Easy (beginner) level\",\n  \"explanationFr\": \"Detailed explanation in French\",\n  \"examples\": \"2-3 worked examples with step-by-step solutions in English\",\n  \"examplesFr\": \"Same examples in French\",\n  \"keySummary\": \"Key points and formulas to remember in English\",\n  \"keySummaryFr\": \"Key summary in French\",\n  \"additionalInfo\": \"Tips, common mistakes to avoid, and connections to other topics in English\",\n  \"additionalInfoFr\": \"Additional info in French\"\n}\n\nImportant requirements:\n1. Content must be appropriate for the Easy (beginner) difficulty level\n2. Use clear, student-friendly language\n3. Include practical examples relevant to real-world applications\n4. Ensure French translations are accurate and natural\n5. Return ONLY valid JSON, no additional text\n",
      "createAt": "2026-01-21 00:16:09",
      "updateAt": "2026-01-21 00:16:09",
      "deleteFlag": "0"
    },
    {
      "id": 5,
      "studentId": 48,
      "knowledgePointId": 631,
      "difficultyLevel": 1,
      "courseTitle": "Introduction to Logarithmic Functions",
      "courseTitleFr": "Introduction aux fonctions logarithmiques",
      "explanation": "Logarithmic functions are the inverse of exponential functions. An exponential function looks like \\(y = a^x\\), where \\(a\\) is a positive number other than 1 and \\(x\\) is the exponent. A logarithmic function, on the other hand, helps us find the exponent when we know the base \\(a\\) and the result \\(y\\). It is written as \\(y=\\log_{a}x\\), which means \\(a^y = x\\). For example, if we have the exponential equation \\(2^3=8\\), the equivalent logarithmic form is \\(\\log_{2}8 = 3\\). Graphing logarithmic functions can show us how they behave. The graph of \\(y = \\log_{a}x\\) has a vertical asymptote at \\(x = 0\\) and passes through the point \\((1,0)\\) because \\(a^0 = 1\\) for any positive \\(aå\neq1\\). Logarithmic functions have some important properties. The product rule states that \\(\\log_{a}(MN)=\\log_{a}M+\\log_{a}N\\). For example, \\(\\log_{10}(2\times5)=\\log_{10}2+\\log_{10}5\\). The quotient rule is \\(\\log_{a}\frac{M}{N}=\\log_{a}M - \\log_{a}N\\), and the power rule is \\(\\log_{a}M^p=p\\log_{a}M\\). In real - world applications, logarithms are used in measuring the loudness of sounds (decibels), the magnitude of earthquakes (Richter scale), and in finance for calculating compound interest.",
      "explanationFr": "Les fonctions logarithmiques sont l'inverse des fonctions exponentielles. Une fonction exponentielle a la forme \\(y = a^x\\), où \\(a\\) est un nombre positif différent de 1 et \\(x\\) est l'exposant. Une fonction logarithmique, en revanche, nous permet de trouver l'exposant lorsque nous connaissons la base \\(a\\) et le résultat \\(y\\). Elle s'écrit \\(y=\\log_{a}x\\), ce qui signifie \\(a^y = x\\). Par exemple, si nous avons l'équation exponentielle \\(2^3 = 8\\), la forme logarithmique équivalente est \\(\\log_{2}8=3\\). Le tracé des fonctions logarithmiques nous permet de voir comment elles se comportent. Le graphe de \\(y = \\log_{a}x\\) a une asymptote verticale en \\(x = 0\\) et passe par le point \\((1,0)\\) car \\(a^0 = 1\\) pour tout \\(a\\gt0\\) différent de 1. Les fonctions logarithmiques ont des propriétés importantes. La règle du produit stipule que \\(\\log_{a}(MN)=\\log_{a}M+\\log_{a}N\\). Par exemple, \\(\\log_{10}(2\times5)=\\log_{10}2+\\log_{10}5\\). La règle du quotient est \\(\\log_{a}\frac{M}{N}=\\log_{a}M-\\log_{a}N\\), et la règle de puissance est \\(\\log_{a}M^p = p\\log_{a}M\\). Dans les applications du monde réel, les logarithmes sont utilisés pour mesurer le volume sonore (en décibels), la magnitude des tremblements de terre (échelle de Richter) et en finance pour calculer l'intérêt composé.",
      "examples": "Example 1: Convert the exponential equation \\(3^4 = 81\\) to logarithmic form. Step 1: Recall the relationship between exponential and logarithmic forms \\(a^y=x\\) is equivalent to \\(y = \\log_{a}x\\). Here, \\(a = 3\\), \\(y = 4\\), and \\(x = 81\\). Step 2: Substitute these values into the logarithmic form. So, \\(\\log_{3}81=4\\). Example 2: Simplify \\(\\log_{5}(25\times125)\\) using the product rule. Step 1: Recall the product rule \\(\\log_{a}(MN)=\\log_{a}M+\\log_{a}N\\). Here, \\(a = 5\\), \\(M = 25\\), and \\(N = 125\\). Step 2: Calculate \\(\\log_{5}25\\) and \\(\\log_{5}125\\) separately. Since \\(5^2 = 25\\), \\(\\log_{5}25 = 2\\), and since \\(5^3=125\\), \\(\\log_{5}125 = 3\\). Step 3: Apply the product rule. \\(\\log_{5}(25\times125)=\\log_{5}25+\\log_{5}125=2 + 3=5\\).",
      "examplesFr": "Exemple 1: Convertir l'équation exponentielle \\(3^4 = 81\\) en forme logarithmique. Étape 1: Rappelez la relation entre les formes exponentielle et logarithmique \\(a^y=x\\) est équivalent à \\(y=\\log_{a}x\\). Ici, \\(a = 3\\), \\(y = 4\\) et \\(x = 81\\). Étape 2: Substituez ces valeurs dans la forme logarithmique. Donc, \\(\\log_{3}81 = 4\\). Exemple 2: Simplifiez \\(\\log_{5}(25\times125)\\) en utilisant la règle du produit. Étape 1: Rappelez la règle du produit \\(\\log_{a}(MN)=\\log_{a}M+\\log_{a}N\\). Ici, \\(a = 5\\), \\(M = 25\\) et \\(N = 125\\). Étape 2: Calculez \\(\\log_{5}25\\) et \\(\\log_{5}125\\) séparément. Puisque \\(5^2 = 25\\), \\(\\log_{5}25 = 2\\), et puisque \\(5^3=125\\), \\(\\log_{5}125 = 3\\). Étape 3: Appliquez la règle du produit. \\(\\log_{5}(25\times125)=\\log_{5}25+\\log_{5}125=2 + 3=5\\).",
      "keySummary": "Key points to remember: 1. A logarithmic function \\(y=\\log_{a}x\\) is the inverse of the exponential function \\(y = a^x\\). 2. The relationship between exponential and logarithmic forms is \\(a^y=x\\) is equivalent to \\(y=\\log_{a}x\\). 3. Graph of \\(y=\\log_{a}x\\) has a vertical asymptote at \\(x = 0\\) and passes through \\((1,0)\\). 4. Logarithm properties: Product rule \\(\\log_{a}(MN)=\\log_{a}M+\\log_{a}N\\), Quotient rule \\(\\log_{a}\frac{M}{N}=\\log_{a}M-\\log_{a}N\\), Power rule \\(\\log_{a}M^p=p\\log_{a}M\\).",
      "keySummaryFr": "Points clés à retenir : 1. Une fonction logarithmique \\(y=\\log_{a}x\\) est l'inverse de la fonction exponentielle \\(y = a^x\\). 2. La relation entre les formes exponentielle et logarithmique est que \\(a^y=x\\) est équivalent à \\(y=\\log_{a}x\\). 3. Le graphe de \\(y=\\log_{a}x\\) a une asymptote verticale en \\(x = 0\\) et passe par le point \\((1,0)\\). 4. Propriétés des logarithmes : Règle du produit \\(\\log_{a}(MN)=\\log_{a}M+\\log_{a}N\\), Règle du quotient \\(\\log_{a}\frac{M}{N}=\\log_{a}M-\\log_{a}N\\), Règle de puissance \\(\\log_{a}M^p=p\\log_{a}M\\).",
      "additionalInfo": "Tips: - When converting between exponential and logarithmic forms, always identify the base \\(a\\), the exponent \\(y\\), and the result \\(x\\) correctly. - When using logarithm properties, make sure to apply them carefully, especially when dealing with negative signs in the quotient rule. Common mistakes: - Forgetting that the argument of a logarithmic function (\\(x\\) in \\(\\log_{a}x\\)) must be positive. Logarithms are not defined for non - positive values. - Misapplying the logarithm properties, for example, thinking \\(\\log_{a}(M + N)=\\log_{a}M+\\log_{a}N\\) (this is incorrect). Connections to other topics: Logarithmic functions are closely related to exponential functions as they are inverses of each other. They are also used in solving exponential equations and in various scientific and financial calculations.",
      "additionalInfoFr": "Conseils : - Lors de la conversion entre les formes exponentielle et logarithmique, identifiez toujours correctement la base \\(a\\), l'exposant \\(y\\) et le résultat \\(x\\). - Lorsque vous utilisez les propriétés des logarithmes, assurez - vous de les appliquer avec soin, en particulier lorsqu'il s'agit de signes négatifs dans la règle du quotient. Erreurs courantes : - Oublier que l'argument d'une fonction logarithmique (\\(x\\) dans \\(\\log_{a}x\\)) doit être positif. Les logarithmes ne sont pas définis pour les valeurs non positives. - Appliquer incorrectement les propriétés des logarithmes, par exemple, penser que \\(\\log_{a}(M + N)=\\log_{a}M+\\log_{a}N\\) (ce qui est incorrect). Liens avec d'autres sujets : Les fonctions logarithmiques sont étroitement liées aux fonctions exponentielles car elles sont leurs inverses. Elles sont également utilisées pour résoudre les équations exponentielles et dans divers calculs scientifiques et financiers.",
      "generationSource": "AI",
      "modelId": "ep-20250421140255-d6sfx",
      "promptUsed": "Please generate a comprehensive course for the following knowledge point:\n\nKnowledge Point Name: Logarithmic Functions\nKnowledge Point Name (French): Fonctions logarithmiques\nDescription: Understanding logarithmic functions\nLearning Objectives: Understand logarithmic functions; Graph log functions; Understand log properties\nDifficulty Level: Easy (beginner)\n\nPlease generate the course content in the following JSON format:\n{\n  \"courseTitle\": \"Course title in English\",\n  \"courseTitleFr\": \"Course title in French\",\n  \"explanation\": \"Detailed explanation of the knowledge point in English, suitable for Easy (beginner) level\",\n  \"explanationFr\": \"Detailed explanation in French\",\n  \"examples\": \"2-3 worked examples with step-by-step solutions in English\",\n  \"examplesFr\": \"Same examples in French\",\n  \"keySummary\": \"Key points and formulas to remember in English\",\n  \"keySummaryFr\": \"Key summary in French\",\n  \"additionalInfo\": \"Tips, common mistakes to avoid, and connections to other topics in English\",\n  \"additionalInfoFr\": \"Additional info in French\"\n}\n\nImportant requirements:\n1. Content must be appropriate for the Easy (beginner) difficulty level\n2. Use clear, student-friendly language\n3. Include practical examples relevant to real-world applications\n4. Ensure French translations are accurate and natural\n5. Return ONLY valid JSON, no additional text\n",
      "createAt": "2026-01-07 06:29:09",
      "updateAt": "2026-01-07 06:29:40",
      "deleteFlag": "0"
    }
  ]
}
```

---

### 2.4 根据知识点ID查询课程列表

**接口地址**: `GET /course/knowledge-point/{knowledgePointId}`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| knowledgePointId | Integer | 是 | 知识点ID (路径参数) |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 6,
      "studentId": 48,
      "knowledgePointId": 631,
      "difficultyLevel": 1,
      "courseTitle": "Introduction to Logarithmic Functions",
      "courseTitleFr": "Introduction aux fonctions logarithmiques",
      "explanation": "Logarithmic functions are the inverse of exponential functions. An exponential function looks like \\(y = a^x\\), where \\(a\\) is a positive number and \\(x\\) is the exponent. A logarithmic function, written as \\(y=\\log_a(x)\\), answers the question: 'To what power do we need to raise the base \\(a\\) to get \\(x\\)'? For example, if we have \\(\\log_2(8)\\), we're asking 'What power do we raise 2 to, to get 8?' Since \\(2^3 = 8\\), then \\(\\log_2(8)=3\\).\n\nGraphing logarithmic functions helps us visualize how they behave. The graph of \\(y = \\log_a(x)\\) has a vertical asymptote at \\(x = 0\\) (the y - axis) and passes through the point \\((1,0)\\) because \\(a^0=1\\) for any positive \\(a\neq1\\).\n\nLogarithmic properties are rules that make it easier to work with logarithmic expressions. Some important properties are: \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\), \\(\\log_a(\frac{M}{N})=\\log_a(M)-\\log_a(N)\\), and \\(\\log_a(M^p)=p\\log_a(M)\\). These properties are useful in many real - world scenarios, like measuring the intensity of earthquakes (Richter scale), the loudness of sounds (decibel scale), and in finance for calculating compound interest.",
      "explanationFr": "Les fonctions logarithmiques sont l'inverse des fonctions exponentielles. Une fonction exponentielle a la forme \\(y = a^x\\), où \\(a\\) est un nombre positif et \\(x\\) est l'exposant. Une fonction logarithmique, écrite sous la forme \\(y=\\log_a(x)\\), répond à la question : 'À quelle puissance devons - nous élever la base \\(a\\) pour obtenir \\(x\\)'? Par exemple, si nous avons \\(\\log_2(8)\\), nous nous demandons 'À quelle puissance devons - nous élever 2 pour obtenir 8?' Puisque \\(2^3 = 8\\), alors \\(\\log_2(8)=3\\).\n\nLe tracé des fonctions logarithmiques nous aide à visualiser leur comportement. Le graphe de \\(y = \\log_a(x)\\) a une asymptote verticale en \\(x = 0\\) (l'axe des y) et passe par le point \\((1,0)\\) car \\(a^0 = 1\\) pour tout \\(a\\) positif différent de 1.\n\nLes propriétés logarithmiques sont des règles qui facilitent le travail avec les expressions logarithmiques. Certaines propriétés importantes sont : \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\), \\(\\log_a(\frac{M}{N})=\\log_a(M)-\\log_a(N)\\) et \\(\\log_a(M^p)=p\\log_a(M)\\). Ces propriétés sont utiles dans de nombreux scénarios du monde réel, comme la mesure de l'intensité des tremblements de terre (échelle de Richter), la mesure du volume des sons (échelle des décibels) et en finance pour calculer les intérêts composés.",
      "examples": "**Example 1: Evaluating a Logarithm**\nFind the value of \\(\\log_3(27)\\).\nStep 1: Recall the definition of a logarithm. We need to find the exponent \\(x\\) such that \\(3^x = 27\\).\nStep 2: Since \\(3^3=27\\), then \\(\\log_3(27)=3\\).\n\n**Example 2: Using Logarithmic Properties**\nSimplify \\(\\log_5(25x)\\).\nStep 1: Use the property \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\). Here, \\(M = 25\\) and \\(N=x\\).\nStep 2: We know that \\(\\log_5(25)\\) because \\(5^2 = 25\\), so \\(\\log_5(25)=2\\). Then \\(\\log_5(25x)=\\log_5(25)+\\log_5(x)=2+\\log_5(x)\\).\n\n**Example 3: Graphing a Logarithmic Function**\nGraph \\(y=\\log_2(x)\\).\nStep 1: Find some key points. When \\(x = 1\\), \\(y=\\log_2(1)=0\\) (since \\(2^0 = 1\\)). When \\(x = 2\\), \\(y=\\log_2(2)=1\\) (since \\(2^1 = 2\\)). When \\(x = 4\\), \\(y=\\log_2(4)=2\\) (since \\(2^2 = 4\\)).\nStep 2: Draw a vertical asymptote at \\(x = 0\\). Then plot the points \\((1,0)\\), \\((2,1)\\), and \\((4,2)\\) and connect them with a smooth curve that approaches the asymptote as \\(x\\) gets closer to 0.",
      "examplesFr": "**Exemple 1 : Évaluation d'un logarithme**\nTrouvez la valeur de \\(\\log_3(27)\\).\nÉtape 1 : Rappelez - vous la définition d'un logarithme. Nous devons trouver l'exposant \\(x\\) tel que \\(3^x = 27\\).\nÉtape 2 : Puisque \\(3^3 = 27\\), alors \\(\\log_3(27)=3\\).\n\n**Exemple 2 : Utilisation des propriétés logarithmiques**\nSimplifiez \\(\\log_5(25x)\\).\nÉtape 1 : Utilisez la propriété \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\). Ici, \\(M = 25\\) et \\(N=x\\).\nÉtape 2 : Nous savons que \\(\\log_5(25)\\) car \\(5^2 = 25\\), donc \\(\\log_5(25)=2\\). Alors \\(\\log_5(25x)=\\log_5(25)+\\log_5(x)=2+\\log_5(x)\\).\n\n**Exemple 3 : Tracé d'une fonction logarithmique**\nTracez le graphe de \\(y=\\log_2(x)\\).\nÉtape 1 : Trouvez quelques points clés. Lorsque \\(x = 1\\), \\(y=\\log_2(1)=0\\) (puisque \\(2^0 = 1\\)). Lorsque \\(x = 2\\), \\(y=\\log_2(2)=1\\) (puisque \\(2^1 = 2\\)). Lorsque \\(x = 4\\), \\(y=\\log_2(4)=2\\) (puisque \\(2^2 = 4\\)).\nÉtape 2 : Tracez une asymptote verticale en \\(x = 0\\). Ensuite, placez les points \\((1,0)\\), \\((2,1)\\) et \\((4,2)\\) et reliez - les avec une courbe lisse qui s'approche de l'asymptote lorsque \\(x\\) se rapproche de 0.",
      "keySummary": "1. Definition of a logarithmic function: \\(y=\\log_a(x)\\) means \\(a^y=x\\).\n2. Key graph features: Vertical asymptote at \\(x = 0\\), passes through \\((1,0)\\).\n3. Logarithmic properties:\n   - \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\)\n   - \\(\\log_a(\frac{M}{N})=\\log_a(M)-\\log_a(N)\\)\n   - \\(\\log_a(M^p)=p\\log_a(M)\\)",
      "keySummaryFr": "1. Définition d'une fonction logarithmique : \\(y=\\log_a(x)\\) signifie \\(a^y=x\\).\n2. Caractéristiques clés du graphe : Asymptote verticale en \\(x = 0\\), passe par le point \\((1,0)\\).\n3. Propriétés logarithmiques :\n   - \\(\\log_a(MN)=\\log_a(M)+\\log_a(N)\\)\n   - \\(\\log_a(\frac{M}{N})=\\log_a(M)-\\log_a(N)\\)\n   - \\(\\log_a(M^p)=p\\log_a(M)\\)",
      "additionalInfo": "**Tips**: \n- When evaluating logarithms, try to rewrite the number inside the logarithm as a power of the base. For example, if you have \\(\\log_4(64)\\), think '4 to what power is 64?' Since \\(4^3 = 64\\), the answer is 3.\n- When graphing, always start by finding the vertical asymptote and a few key points.\n\n**Common mistakes to avoid**: \n- Forgetting the base of the logarithm. \\(\\log(x)\\) usually means \\(\\log_{10}(x)\\) in most math courses, but in some contexts it could mean \\(\\log_e(x)\\) (natural logarithm).\n- Misapplying the logarithmic properties. For example, \\(\\log_a(M + N)\neq\\log_a(M)+\\log_a(N)\\).\n\n**Connections to other topics**: Logarithmic functions are closely related to exponential functions. They are also used in solving exponential equations. In calculus, logarithms are important for integration and differentiation problems.",
      "additionalInfoFr": "**Conseils** : \n- Lors de l'évaluation des logarithmes, essayez de réécrire le nombre à l'intérieur du logarithme comme une puissance de la base. Par exemple, si vous avez \\(\\log_4(64)\\), demandez - vous '4 à quelle puissance donne 64?' Puisque \\(4^3 = 64\\), la réponse est 3.\n- Lors du tracé du graphe, commencez toujours par trouver l'asymptote verticale et quelques points clés.\n\n**Erreurs courantes à éviter** : \n- Oublier la base du logarithme. \\(\\log(x)\\) signifie généralement \\(\\log_{10}(x)\\) dans la plupart des cours de mathématiques, mais dans certains contextes, cela pourrait signifier \\(\\log_e(x)\\) (logarithme naturel).\n- Appliquer incorrectement les propriétés logarithmiques. Par exemple, \\(\\log_a(M + N)\neq\\log_a(M)+\\log_a(N)\\).\n\n**Liens avec d'autres sujets** : Les fonctions logarithmiques sont étroitement liées aux fonctions exponentielles. Elles sont également utilisées pour résoudre les équations exponentielles. En calcul différentiel et intégral, les logarithmes sont importants pour les problèmes d'intégration et de différentiation.",
      "generationSource": "AI",
      "modelId": "ep-20250421140255-d6sfx",
      "promptUsed": "Please generate a comprehensive course for the following knowledge point:\n\nKnowledge Point Name: Logarithmic Functions\nKnowledge Point Name (French): Fonctions logarithmiques\nDescription: Understanding logarithmic functions\nLearning Objectives: Understand logarithmic functions; Graph log functions; Understand log properties\nDifficulty Level: Easy (beginner)\n\nPlease generate the course content in the following JSON format:\n{\n  \"courseTitle\": \"Course title in English\",\n  \"courseTitleFr\": \"Course title in French\",\n  \"explanation\": \"Detailed explanation of the knowledge point in English, suitable for Easy (beginner) level\",\n  \"explanationFr\": \"Detailed explanation in French\",\n  \"examples\": \"2-3 worked examples with step-by-step solutions in English\",\n  \"examplesFr\": \"Same examples in French\",\n  \"keySummary\": \"Key points and formulas to remember in English\",\n  \"keySummaryFr\": \"Key summary in French\",\n  \"additionalInfo\": \"Tips, common mistakes to avoid, and connections to other topics in English\",\n  \"additionalInfoFr\": \"Additional info in French\"\n}\n\nImportant requirements:\n1. Content must be appropriate for the Easy (beginner) difficulty level\n2. Use clear, student-friendly language\n3. Include practical examples relevant to real-world applications\n4. Ensure French translations are accurate and natural\n5. Return ONLY valid JSON, no additional text\n",
      "createAt": "2026-01-21 00:16:09",
      "updateAt": "2026-01-21 00:16:09",
      "deleteFlag": "0"
    },
    {
      "id": 5,
      "studentId": 48,
      "knowledgePointId": 631,
      "difficultyLevel": 1,
      "courseTitle": "Introduction to Logarithmic Functions",
      "courseTitleFr": "Introduction aux fonctions logarithmiques",
      "explanation": "Logarithmic functions are the inverse of exponential functions. An exponential function looks like \\(y = a^x\\), where \\(a\\) is a positive number other than 1 and \\(x\\) is the exponent. A logarithmic function, on the other hand, helps us find the exponent when we know the base \\(a\\) and the result \\(y\\). It is written as \\(y=\\log_{a}x\\), which means \\(a^y = x\\). For example, if we have the exponential equation \\(2^3=8\\), the equivalent logarithmic form is \\(\\log_{2}8 = 3\\). Graphing logarithmic functions can show us how they behave. The graph of \\(y = \\log_{a}x\\) has a vertical asymptote at \\(x = 0\\) and passes through the point \\((1,0)\\) because \\(a^0 = 1\\) for any positive \\(aå\neq1\\). Logarithmic functions have some important properties. The product rule states that \\(\\log_{a}(MN)=\\log_{a}M+\\log_{a}N\\). For example, \\(\\log_{10}(2\times5)=\\log_{10}2+\\log_{10}5\\). The quotient rule is \\(\\log_{a}\frac{M}{N}=\\log_{a}M - \\log_{a}N\\), and the power rule is \\(\\log_{a}M^p=p\\log_{a}M\\). In real - world applications, logarithms are used in measuring the loudness of sounds (decibels), the magnitude of earthquakes (Richter scale), and in finance for calculating compound interest.",
      "explanationFr": "Les fonctions logarithmiques sont l'inverse des fonctions exponentielles. Une fonction exponentielle a la forme \\(y = a^x\\), où \\(a\\) est un nombre positif différent de 1 et \\(x\\) est l'exposant. Une fonction logarithmique, en revanche, nous permet de trouver l'exposant lorsque nous connaissons la base \\(a\\) et le résultat \\(y\\). Elle s'écrit \\(y=\\log_{a}x\\), ce qui signifie \\(a^y = x\\). Par exemple, si nous avons l'équation exponentielle \\(2^3 = 8\\), la forme logarithmique équivalente est \\(\\log_{2}8=3\\). Le tracé des fonctions logarithmiques nous permet de voir comment elles se comportent. Le graphe de \\(y = \\log_{a}x\\) a une asymptote verticale en \\(x = 0\\) et passe par le point \\((1,0)\\) car \\(a^0 = 1\\) pour tout \\(a\\gt0\\) différent de 1. Les fonctions logarithmiques ont des propriétés importantes. La règle du produit stipule que \\(\\log_{a}(MN)=\\log_{a}M+\\log_{a}N\\). Par exemple, \\(\\log_{10}(2\times5)=\\log_{10}2+\\log_{10}5\\). La règle du quotient est \\(\\log_{a}\frac{M}{N}=\\log_{a}M-\\log_{a}N\\), et la règle de puissance est \\(\\log_{a}M^p = p\\log_{a}M\\). Dans les applications du monde réel, les logarithmes sont utilisés pour mesurer le volume sonore (en décibels), la magnitude des tremblements de terre (échelle de Richter) et en finance pour calculer l'intérêt composé.",
      "examples": "Example 1: Convert the exponential equation \\(3^4 = 81\\) to logarithmic form. Step 1: Recall the relationship between exponential and logarithmic forms \\(a^y=x\\) is equivalent to \\(y = \\log_{a}x\\). Here, \\(a = 3\\), \\(y = 4\\), and \\(x = 81\\). Step 2: Substitute these values into the logarithmic form. So, \\(\\log_{3}81=4\\). Example 2: Simplify \\(\\log_{5}(25\times125)\\) using the product rule. Step 1: Recall the product rule \\(\\log_{a}(MN)=\\log_{a}M+\\log_{a}N\\). Here, \\(a = 5\\), \\(M = 25\\), and \\(N = 125\\). Step 2: Calculate \\(\\log_{5}25\\) and \\(\\log_{5}125\\) separately. Since \\(5^2 = 25\\), \\(\\log_{5}25 = 2\\), and since \\(5^3=125\\), \\(\\log_{5}125 = 3\\). Step 3: Apply the product rule. \\(\\log_{5}(25\times125)=\\log_{5}25+\\log_{5}125=2 + 3=5\\).",
      "examplesFr": "Exemple 1: Convertir l'équation exponentielle \\(3^4 = 81\\) en forme logarithmique. Étape 1: Rappelez la relation entre les formes exponentielle et logarithmique \\(a^y=x\\) est équivalent à \\(y=\\log_{a}x\\). Ici, \\(a = 3\\), \\(y = 4\\) et \\(x = 81\\). Étape 2: Substituez ces valeurs dans la forme logarithmique. Donc, \\(\\log_{3}81 = 4\\). Exemple 2: Simplifiez \\(\\log_{5}(25\times125)\\) en utilisant la règle du produit. Étape 1: Rappelez la règle du produit \\(\\log_{a}(MN)=\\log_{a}M+\\log_{a}N\\). Ici, \\(a = 5\\), \\(M = 25\\) et \\(N = 125\\). Étape 2: Calculez \\(\\log_{5}25\\) et \\(\\log_{5}125\\) séparément. Puisque \\(5^2 = 25\\), \\(\\log_{5}25 = 2\\), et puisque \\(5^3=125\\), \\(\\log_{5}125 = 3\\). Étape 3: Appliquez la règle du produit. \\(\\log_{5}(25\times125)=\\log_{5}25+\\log_{5}125=2 + 3=5\\).",
      "keySummary": "Key points to remember: 1. A logarithmic function \\(y=\\log_{a}x\\) is the inverse of the exponential function \\(y = a^x\\). 2. The relationship between exponential and logarithmic forms is \\(a^y=x\\) is equivalent to \\(y=\\log_{a}x\\). 3. Graph of \\(y=\\log_{a}x\\) has a vertical asymptote at \\(x = 0\\) and passes through \\((1,0)\\). 4. Logarithm properties: Product rule \\(\\log_{a}(MN)=\\log_{a}M+\\log_{a}N\\), Quotient rule \\(\\log_{a}\frac{M}{N}=\\log_{a}M-\\log_{a}N\\), Power rule \\(\\log_{a}M^p=p\\log_{a}M\\).",
      "keySummaryFr": "Points clés à retenir : 1. Une fonction logarithmique \\(y=\\log_{a}x\\) est l'inverse de la fonction exponentielle \\(y = a^x\\). 2. La relation entre les formes exponentielle et logarithmique est que \\(a^y=x\\) est équivalent à \\(y=\\log_{a}x\\). 3. Le graphe de \\(y=\\log_{a}x\\) a une asymptote verticale en \\(x = 0\\) et passe par le point \\((1,0)\\). 4. Propriétés des logarithmes : Règle du produit \\(\\log_{a}(MN)=\\log_{a}M+\\log_{a}N\\), Règle du quotient \\(\\log_{a}\frac{M}{N}=\\log_{a}M-\\log_{a}N\\), Règle de puissance \\(\\log_{a}M^p=p\\log_{a}M\\).",
      "additionalInfo": "Tips: - When converting between exponential and logarithmic forms, always identify the base \\(a\\), the exponent \\(y\\), and the result \\(x\\) correctly. - When using logarithm properties, make sure to apply them carefully, especially when dealing with negative signs in the quotient rule. Common mistakes: - Forgetting that the argument of a logarithmic function (\\(x\\) in \\(\\log_{a}x\\)) must be positive. Logarithms are not defined for non - positive values. - Misapplying the logarithm properties, for example, thinking \\(\\log_{a}(M + N)=\\log_{a}M+\\log_{a}N\\) (this is incorrect). Connections to other topics: Logarithmic functions are closely related to exponential functions as they are inverses of each other. They are also used in solving exponential equations and in various scientific and financial calculations.",
      "additionalInfoFr": "Conseils : - Lors de la conversion entre les formes exponentielle et logarithmique, identifiez toujours correctement la base \\(a\\), l'exposant \\(y\\) et le résultat \\(x\\). - Lorsque vous utilisez les propriétés des logarithmes, assurez - vous de les appliquer avec soin, en particulier lorsqu'il s'agit de signes négatifs dans la règle du quotient. Erreurs courantes : - Oublier que l'argument d'une fonction logarithmique (\\(x\\) dans \\(\\log_{a}x\\)) doit être positif. Les logarithmes ne sont pas définis pour les valeurs non positives. - Appliquer incorrectement les propriétés des logarithmes, par exemple, penser que \\(\\log_{a}(M + N)=\\log_{a}M+\\log_{a}N\\) (ce qui est incorrect). Liens avec d'autres sujets : Les fonctions logarithmiques sont étroitement liées aux fonctions exponentielles car elles sont leurs inverses. Elles sont également utilisées pour résoudre les équations exponentielles et dans divers calculs scientifiques et financiers.",
      "generationSource": "AI",
      "modelId": "ep-20250421140255-d6sfx",
      "promptUsed": "Please generate a comprehensive course for the following knowledge point:\n\nKnowledge Point Name: Logarithmic Functions\nKnowledge Point Name (French): Fonctions logarithmiques\nDescription: Understanding logarithmic functions\nLearning Objectives: Understand logarithmic functions; Graph log functions; Understand log properties\nDifficulty Level: Easy (beginner)\n\nPlease generate the course content in the following JSON format:\n{\n  \"courseTitle\": \"Course title in English\",\n  \"courseTitleFr\": \"Course title in French\",\n  \"explanation\": \"Detailed explanation of the knowledge point in English, suitable for Easy (beginner) level\",\n  \"explanationFr\": \"Detailed explanation in French\",\n  \"examples\": \"2-3 worked examples with step-by-step solutions in English\",\n  \"examplesFr\": \"Same examples in French\",\n  \"keySummary\": \"Key points and formulas to remember in English\",\n  \"keySummaryFr\": \"Key summary in French\",\n  \"additionalInfo\": \"Tips, common mistakes to avoid, and connections to other topics in English\",\n  \"additionalInfoFr\": \"Additional info in French\"\n}\n\nImportant requirements:\n1. Content must be appropriate for the Easy (beginner) difficulty level\n2. Use clear, student-friendly language\n3. Include practical examples relevant to real-world applications\n4. Ensure French translations are accurate and natural\n5. Return ONLY valid JSON, no additional text\n",
      "createAt": "2026-01-07 06:29:09",
      "updateAt": "2026-01-07 06:29:40",
      "deleteFlag": "0"
    }
  ]
}
```

---

### 2.5 获取建议难度级别

**接口地址**: `GET /course/difficulty-level`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| studentId | Integer | 是 | 学生ID |
| knowledgePointId | Integer | 是 | 知识点ID |

**请求参数**:
```json
  "knowledgePointId"="631"
  "studentId"="48"
```

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": 2
}
```

---

### 2.6 删除课程

**接口地址**: `DELETE /course/{id}`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Integer | 是 | 课程ID (路径参数) |

**响应示例**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": true
}
```

---

## 3. 知识点查询接口

### 3.1 根据分类ID和难度等级查询知识点

**接口地址**: `GET /api/math/knowledge/category/{categoryId}/difficulty/{difficultyLevel}`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| categoryId | Integer | 是 | 知识分类ID (路径参数) |
| difficultyLevel | Integer | 是 | 难度等级：1-简单，2-中等，3-困难 (路径参数) |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 631,
      "gradeId": 12,
      "categoryId": 73,
      "pointName": "Logarithmic Functions",
      "pointNameFr": "Fonctions logarithmiques",
      "pointCode": "GR12_ALG_026",
      "description": "Understanding logarithmic functions",
      "descriptionFr": "Comprendre les fonctions logarithmiques",
      "content": "Understand logarithmic functions as inverses of exponential functions...",
      "contentFr": "Comprendre les fonctions logarithmiques comme inverses des fonctions exponentielles...",
      "difficultyLevel": 2,
      "sortOrder": 37,
      "learningObjectives": "Understand logarithmic functions; Graph log functions; Understand log properties",
      "learningObjectivesFr": "Comprendre les fonctions logarithmiques; Représenter graphiquement des fonctions log; Comprendre les propriétés des logs",
      "createAt": "2025-12-31 00:31:57",
      "updateAt": "2025-12-31 00:31:57",
      "deleteFlag": "N"
    }
  ]
}
```

---

### 3.2 根据学生ID和知识点ID查询历史测试列表

**接口地址**: `GET /api/student-test-record/student/{studentId}/knowledge-point/{knowledgePointId}`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| studentId | Integer | 是 | 学生ID (路径参数) |
| knowledgePointId | Integer | 是 | 知识点ID (路径参数) |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 135,
      "studentId": 48,
      "testId": 155,
      "testName": "三角函数测试",
      "testNameFr": "Test de trigonométrie",
      "startTime": "2026-01-21 07:50:22",
      "endTime": "2026-01-21 08:30:00",
      "timeLimit": 60,
      "totalQuestions": 5,
      "answeredQuestions": 5,
      "correctAnswers": 4,
      "totalPoints": 5,
      "earnedPoints": 4,
      "testStatus": 3,
      "createAt": "2026-01-20 23:50:22"
    }
  ]
}
```

**说明**: 返回该学生在指定知识点相关的所有历史测试记录

---

### 3.3 根据测试记录ID获取测试详情（包含题目和解析）

**接口地址**: `GET /api/student-test/detail/{testRecordId}`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| testRecordId | Integer | 是 | 测试记录ID (路径参数) |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 135,
    "studentId": 48,
    "testId": 155,
    "testName": "三角函数测试",
    "testNameFr": "Test de trigonométrie",
    "startTime": "2026-01-21 07:50:22",
    "timeLimit": 60,
    "totalQuestions": 5,
    "totalPoints": 5,
    "testStatus": 1,
    "createAt": "2026-01-20 23:50:22",
    "questions": [
      {
        "questionId": 183,
        "sortOrder": 1,
        "points": 1,
        "questionTitle": "Solve Sine Equation",
        "questionTitleFr": "Résoudre une équation de sinus",
        "questionContent": "Find the solutions of the equation...",
        "questionContentFr": "Trouvez les solutions de l'équation...",
        "options": "[\"A. ...\", \"B. ...\", \"C. ...\", \"D. ...\"]",
        "optionsFr": "[\"A. ...\", \"B. ...\", \"C. ...\", \"D. ...\"]",
        "correctAnswer": "B",
        "correctAnswerFr": "B",
        "answerExplanation": "First, isolate sin(x)... So the answer is B.",
        "answerExplanationFr": "Tout d'abord, isolez sin(x)... Donc la réponse est B.",
        "difficultyLevel": 2,
        "knowledgePointId": 627,
        "knowledgePointName": "Basic Trigonometric Equations",
        "knowledgePointNameFr": "Équations trigonométriques de base"
      }
    ],
    "easyCount": 0,
    "mediumCount": 5,
    "hardCount": 0
  }
}
```

**说明**: 返回测试详情，包含完整的题目列表（题目内容、选项、正确答案、解析等）

---

### 3.4 根据测试记录ID查询测试报告

**接口地址**: `GET /api/test-analysis-report/record/{testRecordId}`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| testRecordId | Integer | 是 | 测试记录ID (路径参数) |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 15,
      "testRecordId": 135,
      "studentId": 48,
      "testId": 155,
      "reportType": 1,
      "reportTitle": "测试分析报告_135",
      "reportTitleFr": "Rapport d'analyse de test_135",
      "reportContent": "测试分析报告内容...",
      "overallScore": 80.00,
      "totalPoints": 5,
      "earnedPoints": 4,
      "accuracyRate": 80.00,
      "strongKnowledgePoints": "[627, 631]",
      "strongPointsSummary": "掌握良好的知识点: Basic Trigonometric Equations, Logarithmic Functions",
      "needsImprovementPoints": "[620]",
      "needsImprovementSummary": "需要加强的知识点: Angles in Standard Position",
      "weakKnowledgePoints": "[]",
      "weakPointsSummary": "",
      "recommendations": "建议多练习相关知识点的题目...",
      "recommendationsFr": "Il est recommandé de pratiquer davantage...",
      "analysisData": "{\"knowledgePointScores\": {...}}",
      "createAt": "2026-01-21 08:30:00"
    }
  ]
}
```

**说明**: 返回测试分析报告列表，包含得分、知识点掌握情况分析和学习建议

---

## 4. 学习计划接口

### 4.1 获取学生所有学习计划

**接口地址**: `GET /api/study-plan/list/{studentId}`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| studentId | Integer | 是 | 学生ID (路径参数) |

**响应示例**:
```json
{
  "code": 200,
  "message": "获取学习计划列表成功",
  "data": [
    {
      "id": 19,
      "studentId": 48,
      "categoryId": 73,
      "planName": "Number and Algebra",
      "planNameFr": "Nombres et Algèbre",
      "categoryCode": "NUMBER_ALGEBRA",
      "description": "Number recognition, operations, algebraic expressions, equations, and inequalities",
      "descriptionFr": "Reconnaissance des nombres, opérations, expressions algébriques, équations et inégalités",
      "gradeId": 12,
      "progressPercentage": 0.00,
      "totalKnowledgePoints": 33,
      "completedKnowledgePoints": 0,
      "inProgressKnowledgePoints": 0,
      "notStartedKnowledgePoints": 33,
      "planStatus": 1,
      "totalStudyDuration": 0,
      "createAt": "2026-01-03 08:07:34",
      "updateAt": "2026-01-11 22:47:13"
    },
    {
      "id": 20,
      "studentId": 48,
      "categoryId": 74,
      "planName": "Geometry",
      "planNameFr": "Géométrie",
      "categoryCode": "GEOMETRY",
      "description": "Shape recognition, measurement, transformations, and spatial relationships",
      "descriptionFr": "Reconnaissance des formes, mesure, transformation et relations spatiales",
      "gradeId": 12,
      "progressPercentage": 0.00,
      "totalKnowledgePoints": 11,
      "completedKnowledgePoints": 0,
      "inProgressKnowledgePoints": 0,
      "notStartedKnowledgePoints": 11,
      "planStatus": 1,
      "totalStudyDuration": 0,
      "createAt": "2026-01-03 08:07:34",
      "updateAt": "2026-01-11 22:47:14"
    },
    {
      "id": 21,
      "studentId": 48,
      "categoryId": 75,
      "planName": "Statistics and Probability",
      "planNameFr": "Statistiques et Probabilité",
      "categoryCode": "STATISTICS_PROBABILITY",
      "description": "Data collection, organization, analysis, and probability calculations",
      "descriptionFr": "Collecte, organisation, analyse de données et calculs de probabilité",
      "gradeId": 12,
      "progressPercentage": 0.00,
      "totalKnowledgePoints": 0,
      "completedKnowledgePoints": 0,
      "inProgressKnowledgePoints": 0,
      "notStartedKnowledgePoints": 0,
      "planStatus": 1,
      "totalStudyDuration": 0,
      "createAt": "2026-01-03 08:07:34",
      "updateAt": "2026-01-03 08:07:34"
    },
    {
      "id": 22,
      "studentId": 48,
      "categoryId": 76,
      "planName": "Comprehensive Application",
      "planNameFr": "Application Complète",
      "categoryCode": "COMPREHENSIVE",
      "description": "Comprehensive application of mathematical knowledge to solve real-world problems",
      "descriptionFr": "Application complète des connaissances mathématiques pour résoudre des problèmes réels",
      "gradeId": 12,
      "progressPercentage": 0.00,
      "totalKnowledgePoints": 0,
      "completedKnowledgePoints": 0,
      "inProgressKnowledgePoints": 0,
      "notStartedKnowledgePoints": 0,
      "planStatus": 1,
      "totalStudyDuration": 0,
      "createAt": "2026-01-03 08:07:34",
      "updateAt": "2026-01-03 08:07:34"
    }
  ]
}
```

---

### 4.2 获取单个计划详情

**接口地址**: `GET /api/study-plan/{studentId}/{categoryId}`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| studentId | Integer | 是 | 学生ID (路径参数) |
| categoryId | Integer | 是 | 分类ID (路径参数) |

**响应示例**:
```json
{
  "code": 200,
  "message": "获取学习计划详情成功",
  "data": {
    "id": 22,
    "studentId": 48,
    "categoryId": 76,
    "planName": "Comprehensive Application",
    "planNameFr": "Application Complète",
    "categoryCode": "COMPREHENSIVE",
    "description": "Comprehensive application of mathematical knowledge to solve real-world problems",
    "descriptionFr": "Application complète des connaissances mathématiques pour résoudre des problèmes réels",
    "gradeId": 12,
    "progressPercentage": 0.00,
    "totalKnowledgePoints": 0,
    "completedKnowledgePoints": 0,
    "inProgressKnowledgePoints": 0,
    "notStartedKnowledgePoints": 0,
    "planStatus": 1,
    "totalStudyDuration": 0,
    "createAt": "2026-01-03 08:07:34",
    "updateAt": "2026-01-03 08:07:34"
  }
}
```

---

### 4.3 完成知识点学习

**接口地址**: `POST /api/study-plan/complete-knowledge`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| studentId | Integer | 是 | 学生ID |
| knowledgePointId | Integer | 是 | 知识点ID |

**说明**: 完成知识点学习后，自动更新所属计划的进度百分比

**响应示例**:
```json
{
  "code": 200,
  "message": "知识点学习完成，计划进度已更新",
  "data": {
    "id": 19,
    "studentId": 48,
    "categoryId": 73,
    "planName": "Number and Algebra",
    "planNameFr": "Nombres et Algèbre",
    "categoryCode": "NUMBER_ALGEBRA",
    "description": "Number recognition, operations, algebraic expressions, equations, and inequalities",
    "descriptionFr": "Reconnaissance des nombres, opérations, expressions algébriques, équations et inégalités",
    "gradeId": 12,
    "progressPercentage": 3.03,
    "totalKnowledgePoints": 33,
    "completedKnowledgePoints": 1,
    "inProgressKnowledgePoints": 0,
    "notStartedKnowledgePoints": 32,
    "planStatus": 2,
    "totalStudyDuration": 0,
    "lastStudyTime": "2026-01-21 08:33:13",
    "createAt": "2026-01-03 08:07:34",
    "updateAt": "2026-01-21 00:33:13"
  }
}
```

---

### 4.4 取消完成知识点

**接口地址**: `POST /api/study-plan/uncomplete-knowledge`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| studentId | Integer | 是 | 学生ID |
| knowledgePointId | Integer | 是 | 知识点ID |

**说明**: 取消完成后，自动更新所属计划的进度百分比

**响应示例**:
```json
{
  "code": 200,
  "message": "已取消完成，计划进度已更新",
  "data": {
    "id": 19,
    "studentId": 48,
    "categoryId": 73,
    "planName": "Number and Algebra",
    "planNameFr": "Nombres et Algèbre",
    "categoryCode": "NUMBER_ALGEBRA",
    "description": "Number recognition, operations, algebraic expressions, equations, and inequalities",
    "descriptionFr": "Reconnaissance des nombres, opérations, expressions algébriques, équations et inégalités",
    "gradeId": 12,
    "progressPercentage": 0.00,
    "totalKnowledgePoints": 33,
    "completedKnowledgePoints": 0,
    "inProgressKnowledgePoints": 0,
    "notStartedKnowledgePoints": 33,
    "planStatus": 1,
    "totalStudyDuration": 0,
    "lastStudyTime": "2026-01-21 08:33:13",
    "createAt": "2026-01-03 08:07:34",
    "updateAt": "2026-01-21 00:35:07"
  }
}
```

---

### 4.5 刷新计划进度

**接口地址**: `POST /api/study-plan/refresh/{studentId}/{categoryId}`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| studentId | Integer | 是 | 学生ID (路径参数) |
| categoryId | Integer | 是 | 分类ID (路径参数) |

**响应示例**:
```json
{
  "code": 200,
  "message": "计划进度刷新成功",
  "data": {
    "id": 19,
    "studentId": 48,
    "categoryId": 73,
    "planName": "Number and Algebra",
    "planNameFr": "Nombres et Algèbre",
    "categoryCode": "NUMBER_ALGEBRA",
    "description": "Number recognition, operations, algebraic expressions, equations, and inequalities",
    "descriptionFr": "Reconnaissance des nombres, opérations, expressions algébriques, équations et inégalités",
    "gradeId": 12,
    "progressPercentage": 0.00,
    "totalKnowledgePoints": 33,
    "completedKnowledgePoints": 0,
    "inProgressKnowledgePoints": 0,
    "notStartedKnowledgePoints": 33,
    "planStatus": 1,
    "totalStudyDuration": 0,
    "lastStudyTime": "2026-01-21 08:33:13",
    "createAt": "2026-01-03 08:07:34",
    "updateAt": "2026-01-21 00:39:17"
  }
}
```

---

### 4.6 根据知识点获取所属计划

**接口地址**: `GET /api/study-plan/by-knowledge/{studentId}/{knowledgePointId}`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| studentId | Integer | 是 | 学生ID (路径参数) |
| knowledgePointId | Integer | 是 | 知识点ID (路径参数) |

**响应示例**:
```json
{
  "code": 200,
  "message": "获取学习计划成功",
  "data": {
    "id": 19,
    "studentId": 48,
    "categoryId": 73,
    "planName": "Number and Algebra",
    "planNameFr": "Nombres et Algèbre",
    "categoryCode": "NUMBER_ALGEBRA",
    "description": "Number recognition, operations, algebraic expressions, equations, and inequalities",
    "descriptionFr": "Reconnaissance des nombres, opérations, expressions algébriques, équations et inégalités",
    "gradeId": 12,
    "progressPercentage": 0.00,
    "totalKnowledgePoints": 33,
    "completedKnowledgePoints": 0,
    "inProgressKnowledgePoints": 0,
    "notStartedKnowledgePoints": 33,
    "planStatus": 1,
    "totalStudyDuration": 0,
    "lastStudyTime": "2026-01-21 08:33:13",
    "createAt": "2026-01-03 08:07:34",
    "updateAt": "2026-01-21 00:40:23"
  }
}
```
