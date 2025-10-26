# Enhanced Bound Categories API Documentation

## Overview

The enhanced bound categories API returns comprehensive information about student's category learning progress, including difficulty distribution, descriptions, and detailed progress statistics.

## API Endpoint

### Get Student Bound Categories with Progress

**Endpoint**: `GET /api/student/course/bound-categories-with-progress/{userId}`

**Description**: Returns a list of categories bound to a student, including learning progress statistics and difficulty distribution.

**Request Parameters**:
- `userId` (path parameter): Student ID

**Response Example**:
```json
{
  "code": 200,
  "message": "Get student bound categories successfully",
  "data": [
    {
      "id": 1,
      "categoryName": "Number and Algebra",
      "categoryNameFr": "Nombres et Algèbre",
      "categoryCode": "NUMBER_ALGEBRA",
      "description": "Number recognition, operations, algebraic expressions, equations, and inequalities",
      "descriptionFr": "Reconnaissance des nombres, opérations, expressions algébriques, équations et inégalités",
      "gradeId": 7,
      "iconUrl": "/icons/category/numbers-algebra.png",
      "iconClass": "icon-category-numbers",
      "sortOrder": 1,
      
      "totalKnowledgePoints": 13,
      "completedKnowledgePoints": 5,
      "inProgressKnowledgePoints": 3,
      "notStartedKnowledgePoints": 5,
      "overallProgress": 38.46,
      
      "easyCount": 5,
      "mediumCount": 6,
      "hardCount": 2,
      "averageDifficulty": 1.77,
      
      "totalStudyDuration": 450,
      "lastStudyTime": "2023-12-21 14:30:00",
      "startTime": "2023-12-15 09:00:00",
      "completeTime": null,
      "bindingStatus": 2,
      
      "createAt": "2023-12-15 09:00:00",
      "updateAt": "2023-12-21 14:30:00"
    },
    {
      "id": 2,
      "categoryName": "Geometry",
      "categoryNameFr": "Géométrie",
      "categoryCode": "GEOMETRY",
      "description": "Shape recognition, measurement, transformations, and spatial relationships",
      "descriptionFr": "Reconnaissance des formes, mesure, transformation et relations spatiales",
      "gradeId": 7,
      "iconUrl": "/icons/category/geometry.png",
      "iconClass": "icon-category-geometry",
      "sortOrder": 2,
      
      "totalKnowledgePoints": 10,
      "completedKnowledgePoints": 2,
      "inProgressKnowledgePoints": 4,
      "notStartedKnowledgePoints": 4,
      "overallProgress": 25.00,
      
      "easyCount": 3,
      "mediumCount": 5,
      "hardCount": 2,
      "averageDifficulty": 1.90,
      
      "totalStudyDuration": 320,
      "lastStudyTime": "2023-12-21 16:00:00",
      "startTime": "2023-12-15 09:00:00",
      "completeTime": null,
      "bindingStatus": 2,
      
      "createAt": "2023-12-15 09:00:00",
      "updateAt": "2023-12-21 16:00:00"
    }
  ]
}
```

## Response Fields

### Basic Category Information
- `id`: Category ID
- `categoryName`: Category name (English)
- `categoryNameFr`: Category name (French)
- `categoryCode`: Category code
- `description`: Category description (English)
- `descriptionFr`: Category description (French)
- `gradeId`: Grade ID
- `iconUrl`: Category icon URL
- `iconClass`: CSS class for category icon
- `sortOrder`: Sort order

### Learning Progress Statistics
- `totalKnowledgePoints`: Total number of knowledge points in the category
- `completedKnowledgePoints`: Number of completed knowledge points
- `inProgressKnowledgePoints`: Number of in-progress knowledge points
- `notStartedKnowledgePoints`: Number of not-started knowledge points
- `overallProgress`: Overall completion percentage (0-100)

### Difficulty Distribution
- `easyCount`: Number of easy difficulty knowledge points (difficulty level = 1)
- `mediumCount`: Number of medium difficulty knowledge points (difficulty level = 2)
- `hardCount`: Number of hard difficulty knowledge points (difficulty level = 3)
- `averageDifficulty`: Average difficulty level (1.0-3.0)

### Learning Time Statistics
- `totalStudyDuration`: Total study duration in minutes
- `lastStudyTime`: Last study time
- `startTime`: Start learning time
- `completeTime`: Complete time (null if not completed)

### Binding Status
- `bindingStatus`: Binding status (1=Bound, 2=Learning, 3=Completed)
- `createAt`: Creation time
- `updateAt`: Last update time

## Difficulty Levels

- **1 - Easy**: Basic concepts that are easy to understand and master
- **2 - Medium**: Moderate difficulty requiring more practice
- **3 - Hard**: Advanced concepts requiring significant effort

## Progress Status

- **Completed**: All knowledge points in the category are completed (100%)
- **Learning**: Student is actively learning in this category (1%-99%)
- **Not Started**: No progress made in this category (0%)

## Usage Examples

### cURL
```bash
curl -X GET "http://localhost:8080/api/student/course/bound-categories-with-progress/123" \
  -H "Content-Type: application/json"
```

### JavaScript/Fetch
```javascript
fetch('http://localhost:8080/api/student/course/bound-categories-with-progress/123')
  .then(response => response.json())
  .then(data => {
    console.log('Categories:', data.data);
    data.data.forEach(category => {
      console.log(`Category: ${category.categoryName}`);
      console.log(`Progress: ${category.overallProgress}%`);
      console.log(`Easy: ${category.easyCount}, Medium: ${category.mediumCount}, Hard: ${category.hardCount}`);
    });
  });
```

### Java/Spring RestTemplate
```java
RestTemplate restTemplate = new RestTemplate();
ResponseEntity<RespResult> response = restTemplate.getForEntity(
    "http://localhost:8080/api/student/course/bound-categories-with-progress/123",
    RespResult.class
);
List<CategoryWithProgress> categories = (List<CategoryWithProgress>) response.getBody().getData();
```

## Comparison with Original API

### Original Endpoint
- `GET /api/student/course/bound-categories/{userId}`
- Returns: Basic category information only

### Enhanced Endpoint
- `GET /api/student/course/bound-categories-with-progress/{userId}`
- Returns: Category information + learning progress + difficulty distribution

## Migration Guide

If you're currently using the original bound-categories endpoint and want to switch to the enhanced version:

**Before:**
```javascript
fetch('/api/student/course/bound-categories/123')
```

**After:**
```javascript
fetch('/api/student/course/bound-categories-with-progress/123')
// Now you get additional fields: totalKnowledgePoints, completedKnowledgePoints, 
// inProgressKnowledgePoints, notStartedKnowledgePoints, overallProgress, 
// easyCount, mediumCount, hardCount, averageDifficulty, totalStudyDuration, etc.
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

### No Bound Categories
```json
{
  "code": 200,
  "message": "Get student bound categories successfully",
  "data": []
}
```
