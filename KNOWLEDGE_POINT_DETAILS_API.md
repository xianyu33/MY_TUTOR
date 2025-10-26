# Knowledge Point Details Query API Documentation

## Overview

Query knowledge point details by grade and category, including student learning progress, difficulty information, and more.

## API Endpoint

### Get Knowledge Point Details with Progress

**Endpoint**: `GET /api/student/course/knowledge-points/{userId}/grade/{gradeId}/category/{categoryId}`

**Description**: Query knowledge point details by student ID, grade ID, and category ID, including student learning progress and difficulty information.

**Path Parameters**:
- `userId` (required): Student ID
- `gradeId` (required): Grade ID
- `categoryId` (required): Category ID

**Response Example**:
```json
{
  "code": 200,
  "message": "Get knowledge point details successfully",
  "data": [
    {
      "id": 101,
      "gradeId": 7,
      "categoryId": 1,
      "pointName": "Positive and Negative Numbers",
      "pointNameFr": "Nombres positifs et négatifs",
      "pointCode": "POSITIVE_NEGATIVE_NUMBERS",
      "description": "Understanding positive and negative numbers",
      "descriptionFr": "Reconnaissance des nombres positifs et négatifs",
      "content": "Understand the concept of positive and negative numbers, master their representation and comparison",
      "contentFr": "Comprendre le concept des nombres positifs et négatifs, maîtriser leur représentation et leur comparaison",
      "iconUrl": "/icons/positive-negative.png",
      "iconClass": "icon-positive-negative",
      "difficultyLevel": 1,
      "sortOrder": 1,
      "learningObjectives": "Be able to identify positive and negative numbers, understand their position relationship on the number line",
      "learningObjectivesFr": "Être capable d'identifier les nombres positifs et négatifs, comprendre leur position sur la ligne numérique",
      
      "categoryName": "Number and Algebra",
      "categoryNameFr": "Nombres et Algèbre",
      "categoryCode": "NUMBER_ALGEBRA",
      
      "gradeName": "Grade 7",
      "gradeLevel": 7,
      
      "progressStatus": 2,
      "completionPercentage": 75.00,
      "startTime": "2023-12-15 09:00:00",
      "completeTime": null,
      "studyDuration": 60,
      "lastStudyTime": "2023-12-20 14:30:00",
      "notes": "Need more practice",
      
      "difficultyName": "Easy",
      "difficultyDescription": "Basic concepts that are easy to understand and master",
      
      "createAt": "2023-12-01 10:00:00",
      "updateAt": "2023-12-20 14:30:00"
    },
    {
      "id": 102,
      "gradeId": 7,
      "categoryId": 1,
      "pointName": "Rational Numbers",
      "pointNameFr": "Nombres rationnels",
      "pointCode": "RATIONAL_NUMBERS",
      "description": "Concept and classification of rational numbers",
      "descriptionFr": "Concept et classification des nombres rationnels",
      "content": "Understand the definition of rational numbers, master their classification and representation",
      "contentFr": "Comprendre la définition des nombres rationnels, maîtriser leur classification et méthodes de représentation",
      "iconUrl": "/icons/rational-numbers.png",
      "iconClass": "icon-rational",
      "difficultyLevel": 2,
      "sortOrder": 2,
      "learningObjectives": "Be able to distinguish rational and irrational numbers, master rational number classification",
      "learningObjectivesFr": "Être capable de distinguer les nombres rationnels et irrationnels, maîtriser la classification des nombres rationnels",
      
      "categoryName": "Number and Algebra",
      "categoryNameFr": "Nombres et Algèbre",
      "categoryCode": "NUMBER_ALGEBRA",
      
      "gradeName": "Grade 7",
      "gradeLevel": 7,
      
      "progressStatus": 1,
      "completionPercentage": 0.00,
      "startTime": null,
      "completeTime": null,
      "studyDuration": 0,
      "lastStudyTime": null,
      "notes": null,
      
      "difficultyName": "Medium",
      "difficultyDescription": "Moderate difficulty requiring more practice",
      
      "createAt": "2023-12-01 10:00:00",
      "updateAt": "2023-12-01 10:00:00"
    },
    {
      "id": 113,
      "gradeId": 7,
      "categoryId": 1,
      "pointName": "Exponentiation of Rational Numbers",
      "pointNameFr": "Puissance de nombres rationnels",
      "pointCode": "RATIONAL_POWER",
      "description": "Exponentiation operations with rational numbers",
      "descriptionFr": "Opérations de puissance avec nombres rationnels",
      "content": "Understand the concept of exponentiation, master the rules of exponentiation",
      "contentFr": "Comprendre le concept de puissance, maîtriser les règles de calcul des puissances",
      "iconUrl": "/icons/power.png",
      "iconClass": "icon-power",
      "difficultyLevel": 3,
      "sortOrder": 10,
      "learningObjectives": "Be able to perform exponentiation operations with rational numbers, understand the meaning of exponentiation",
      "learningObjectivesFr": "Être capable d'effectuer des opérations de puissance avec nombres rationnels, comprendre la signification des puissances",
      
      "categoryName": "Number and Algebra",
      "categoryNameFr": "Nombres et Algèbre",
      "categoryCode": "NUMBER_ALGEBRA",
      
      "gradeName": "Grade 7",
      "gradeLevel": 7,
      
      "progressStatus": 3,
      "completionPercentage": 100.00,
      "startTime": "2023-12-10 10:00:00",
      "completeTime": "2023-12-18 16:00:00",
      "studyDuration": 120,
      "lastStudyTime": "2023-12-18 16:00:00",
      "notes": "Mastered",
      
      "difficultyName": "Hard",
      "difficultyDescription": "Advanced concepts requiring significant effort",
      
      "createAt": "2023-12-01 10:00:00",
      "updateAt": "2023-12-18 16:00:00"
    }
  ]
}
```

## Response Fields

### Basic Information
- `id`: Knowledge point ID
- `gradeId`: Grade ID
- `categoryId`: Category ID
- `pointName`: Knowledge point name (English)
- `pointNameFr`: Knowledge point name (French)
- `pointCode`: Knowledge point code
- `description`: Knowledge point description (English)
- `descriptionFr`: Knowledge point description (French)
- `content`: Knowledge point content (English)
- `contentFr`: Knowledge point content (French)
- `iconUrl`: Icon URL
- `iconClass`: CSS class name
- `difficultyLevel`: Difficulty level (1-Easy, 2-Medium, 3-Hard)
- `sortOrder`: Sort order
- `learningObjectives`: Learning objectives (English)
- `learningObjectivesFr`: Learning objectives (French)

### Category Information
- `categoryName`: Category name (English)
- `categoryNameFr`: Category name (French)
- `categoryCode`: Category code

### Grade Information
- `gradeName`: Grade name
- `gradeLevel`: Grade level

### Learning Progress
- `progressStatus`: Learning status
  - 1 = Not Started
  - 2 = In Progress
  - 3 = Completed
- `completionPercentage`: Completion percentage (0-100)
- `startTime`: Start learning time
- `completeTime`: Complete learning time
- `studyDuration`: Study duration (minutes)
- `lastStudyTime`: Last study time
- `notes`: Study notes

### Difficulty Information
- `difficultyName`: Difficulty name (Easy/Medium/Hard)
- `difficultyDescription`: Difficulty description

### Time Information
- `createAt`: Creation time
- `updateAt`: Update time

## Difficulty Levels

### Definitions
- **1 - Easy**: Basic concepts that are easy to understand and master
- **2 - Medium**: Moderate difficulty requiring more practice
- **3 - Hard**: Advanced concepts requiring significant effort

### Descriptions
- **Easy**: "Basic concepts that are easy to understand and master"
- **Medium**: "Moderate difficulty requiring more practice"
- **Hard**: "Advanced concepts requiring significant effort"

## Learning Status

- **Not Started (1)**: Student has not started learning this knowledge point
- **In Progress (2)**: Student is currently learning this knowledge point
- **Completed (3)**: Student has completed learning this knowledge point

## Usage Examples

### cURL
```bash
curl -X GET "http://localhost:8080/api/student/course/knowledge-points/123/grade/7/category/1" \
  -H "Content-Type: application/json"
```

### JavaScript/Fetch
```javascript
fetch('http://localhost:8080/api/student/course/knowledge-points/123/grade/7/category/1')
  .then(response => response.json())
  .then(data => {
    console.log('Knowledge points:', data.data);
    data.data.forEach(point => {
      console.log(`Point: ${point.pointName}`);
      console.log(`Difficulty: ${point.difficultyName} (${point.difficultyLevel})`);
      console.log(`Progress: ${point.completionPercentage}%`);
      console.log(`Status: ${point.progressStatus === 1 ? 'Not Started' : point.progressStatus === 2 ? 'In Progress' : 'Completed'}`);
    });
  });
```

### Vue.js Example
```vue
<template>
  <div>
    <h3>{{ categoryName }}</h3>
    <div v-for="point in knowledgePoints" :key="point.id" class="point-card">
      <h4>{{ point.pointName }}</h4>
      <p>Difficulty: {{ point.difficultyName }}</p>
      <p>Progress: {{ point.completionPercentage }}%</p>
      <p>Study Duration: {{ point.studyDuration }} minutes</p>
      <div class="progress-bar">
        <div :style="{width: point.completionPercentage + '%'}"></div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      knowledgePoints: [],
      categoryName: ''
    }
  },
  methods: {
    async fetchKnowledgePoints(userId, gradeId, categoryId) {
      try {
        const response = await this.$http.get(
          `/api/student/course/knowledge-points/${userId}/grade/${gradeId}/category/${categoryId}`
        );
        this.knowledgePoints = response.data.data;
        if (this.knowledgePoints.length > 0) {
          this.categoryName = this.knowledgePoints[0].categoryName;
        }
      } catch (error) {
        console.error('Failed to fetch knowledge points:', error);
      }
    }
  },
  mounted() {
    this.fetchKnowledgePoints(123, 7, 1);
  }
}
</script>
```

### Java/Spring RestTemplate
```java
RestTemplate restTemplate = new RestTemplate();
ResponseEntity<RespResult> response = restTemplate.getForEntity(
    "http://localhost:8080/api/student/course/knowledge-points/123/grade/7/category/1",
    RespResult.class
);

List<KnowledgePointWithProgress> knowledgePoints = 
    (List<KnowledgePointWithProgress>) response.getBody().getData();

for (KnowledgePointWithProgress point : knowledgePoints) {
    System.out.println("Point Name: " + point.getPointName());
    System.out.println("Difficulty: " + point.getDifficultyName());
    System.out.println("Progress: " + point.getCompletionPercentage() + "%");
    System.out.println("Study Duration: " + point.getStudyDuration() + " minutes");
}
```

## Error Responses

### Student Not Found
```json
{
  "code": 404,
  "message": "Student not found",
  "data": null
}
```

### Grade Not Found
```json
{
  "code": 404,
  "message": "Grade not found",
  "data": null
}
```

### Category Not Found
```json
{
  "code": 404,
  "message": "Category not found",
  "data": null
}
```

### No Knowledge Points Found
```json
{
  "code": 200,
  "message": "Get knowledge point details successfully",
  "data": []
}
```

## FAQ

### Q1: What if the student has no learning progress for a knowledge point?
**A**: The system automatically sets the progress status to "Not Started" (1), completion percentage to 0%, and study duration to 0 minutes.

### Q2: What's the difference between `categoryName` and `categoryNameFr`?
**A**: `categoryName` is the English category name, `categoryNameFr` is the French category name, supporting multilingual display.

### Q3: How to filter knowledge points by progress status?
**A**: You can filter by the `progressStatus` field on the frontend:
- `progressStatus === 1`: Not Started
- `progressStatus === 2`: In Progress
- `progressStatus === 3`: Completed

### Q4: How is study duration calculated?
**A**: `studyDuration` is in minutes, and the system automatically accumulates based on actual student learning time.

## Best Practices

1. **Sort Display**: Returned data is already sorted by `sortOrder`
2. **Group Display**: Group by `difficultyLevel` for display
3. **Progress Display**: Use `completionPercentage` for progress bars
4. **Status Filter**: Filter by `progressStatus` for different statuses
5. **Multilingual Support**: Display English or French content based on user preference

## Summary

This API provides comprehensive knowledge point details, including:
- ✨ Basic information and descriptions
- 📊 Student learning progress
- 📈 Difficulty levels and descriptions
- ⏱️ Study duration statistics
- 🌐 Multilingual support (English/French)
- 📝 Study notes
- 🎯 Learning objectives

This enables the frontend to build rich and detailed knowledge point learning pages.
