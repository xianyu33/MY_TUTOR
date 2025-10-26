# Learning Progress Initialization Documentation

## Overview

When a new student is registered, the system automatically initializes their learning progress data based on their grade level. This includes:

1. **Student-Category Binding**: Creates binding relationships between students and knowledge categories
2. **Learning Progress Records**: Initializes learning progress for each knowledge point in the student's grade
3. **Progress Status**: Sets all knowledge points to "Not Started" status with 0% completion

## Database Schema

### Tables Involved

1. **`user`**: Student information
2. **`grade`**: Grade information
3. **`knowledge_category`**: Knowledge point categories
4. **`knowledge_point`**: Individual knowledge points
5. **`student_category_binding`**: Binding relationship between students and categories
6. **`learning_progress`**: Learning progress for each student and knowledge point

### Schema Details

#### student_category_binding Table
```sql
CREATE TABLE IF NOT EXISTS `student_category_binding` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT 'Binding Relationship ID',
  `student_id` INT NOT NULL COMMENT 'Student ID',
  `category_id` INT NOT NULL COMMENT 'Category ID',
  `grade_id` INT NOT NULL COMMENT 'Grade ID',
  `binding_status` TINYINT DEFAULT 1 COMMENT 'Binding Status: 1-Bound, 2-Learning, 3-Completed',
  `overall_progress` DECIMAL(5,2) DEFAULT 0.00 COMMENT 'Overall Learning Progress Percentage',
  `total_knowledge_points` INT DEFAULT 0 COMMENT 'Total Number of Knowledge Points in This Category',
  `completed_knowledge_points` INT DEFAULT 0 COMMENT 'Number of Completed Knowledge Points',
  `in_progress_knowledge_points` INT DEFAULT 0 COMMENT 'Number of In-Progress Knowledge Points',
  `not_started_knowledge_points` INT DEFAULT 0 COMMENT 'Number of Not-Started Knowledge Points',
  `total_study_duration` INT DEFAULT 0 COMMENT 'Total Study Duration (minutes)',
  `last_study_time` DATETIME COMMENT 'Last Study Time',
  `start_time` DATETIME COMMENT 'Start Study Time',
  `complete_time` DATETIME COMMENT 'Complete Time',
  `notes` TEXT COMMENT 'Study Notes',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Create Time',
  `create_by` VARCHAR(50) COMMENT 'Created By',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update Time',
  `update_by` VARCHAR(50) COMMENT 'Updated By',
  `delete_flag` CHAR(1) DEFAULT 'N' COMMENT 'Delete Flag: Y-Deleted, N-Not Deleted'
);
```

#### learning_progress Table
```sql
CREATE TABLE IF NOT EXISTS `learning_progress` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT 'Progress ID',
  `user_id` INT NOT NULL COMMENT 'Student ID',
  `knowledge_point_id` INT NOT NULL COMMENT 'Knowledge Point ID',
  `knowledge_category_id` INT COMMENT 'Category ID',
  `progress_status` TINYINT DEFAULT 1 COMMENT 'Learning Status: 1-Not Started, 2-In Progress, 3-Completed',
  `completion_percentage` DECIMAL(5,2) DEFAULT 0.00 COMMENT 'Completion Percentage',
  `start_time` DATETIME COMMENT 'Start Learning Time',
  `complete_time` DATETIME COMMENT 'Complete Learning Time',
  `study_duration` INT DEFAULT 0 COMMENT 'Study Duration (minutes)',
  `last_study_time` DATETIME COMMENT 'Last Study Time',
  `notes` TEXT COMMENT 'Study Notes',
  `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Create Time',
  `create_by` VARCHAR(50) COMMENT 'Created By',
  `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update Time',
  `update_by` VARCHAR(50) COMMENT 'Updated By',
  `delete_flag` CHAR(1) DEFAULT 'N' COMMENT 'Delete Flag: Y-Deleted, N-Not Deleted'
);
```

## Initialization Process

### 1. Student Registration Flow

```java
public boolean registerStudentWithCoursesAndTest(User user) {
    // 1. Register student
    boolean registerResult = userService.register(user);
    
    // 2. Get registered student ID
    User registeredUser = userMapper.findByUserAccount(user.getUserAccount());
    
    // 3. Assign courses based on grade
    Integer gradeLevel = parseGradeLevel(user.getGrade());
    boolean assignResult = assignCoursesByGrade(registeredUser.getId(), gradeLevel);
    
    // 4. Generate test questions
    Grade grade = gradeService.findGradeByLevel(gradeLevel);
    Integer testId = generateTestForGrade(grade.getId());
    
    return true;
}
```

### 2. Course Assignment Flow

```java
public boolean assignCoursesByGrade(Integer userId, Integer gradeLevel) {
    // 1. Get grade information by grade level
    Grade grade = gradeService.findGradeByLevel(gradeLevel);
    
    // 2. Get all knowledge points for the grade
    List<KnowledgePoint> knowledgePoints = knowledgePointService.findKnowledgePointsByGradeId(grade.getId());
    
    // 3. Check if courses already assigned
    List<LearningProgress> existingProgress = learningProgressService.findLearningProgressByUserId(userId);
    
    // 4. Create student-category bindings
    int bindingCount = studentCategoryBindingService.batchCreateStudentCategoryBindings(userId, grade.getId());
    
    // 5. Create learning progress for each knowledge point
    for (KnowledgePoint knowledgePoint : knowledgePoints) {
        LearningProgress progress = new LearningProgress();
        progress.setUserId(userId);
        progress.setKnowledgePointId(knowledgePoint.getId());
        progress.setKnowledgeCategoryId(knowledgePoint.getCategoryId());
        progress.setProgressStatus(1); // Not Started
        progress.setCompletionPercentage(BigDecimal.ZERO);
        progress.setStudyDuration(0);
        progress.setDeleteFlag("N");
        
        learningProgressService.insertLearningProgress(progress);
    }
    
    return true;
}
```

### 3. Batch Create Student-Category Bindings

```java
public int batchCreateStudentCategoryBindings(Integer userId, Integer gradeId) {
    // 1. Get all categories for the grade
    List<KnowledgeCategory> categories = knowledgeCategoryService.findKnowledgeCategoriesByGradeId(gradeId);
    
    // 2. Count knowledge points for each category
    Map<Integer, Integer> categoryPointCounts = new HashMap<>();
    for (KnowledgeCategory category : categories) {
        int count = knowledgePointService.countKnowledgePointsByCategory(category.getId());
        categoryPointCounts.put(category.getId(), count);
    }
    
    // 3. Create binding records
    int createdCount = 0;
    for (KnowledgeCategory category : categories) {
        StudentCategoryBinding binding = new StudentCategoryBinding();
        binding.setStudentId(userId);
        binding.setCategoryId(category.getId());
        binding.setGradeId(gradeId);
        binding.setBindingStatus(1); // Bound
        binding.setOverallProgress(BigDecimal.ZERO);
        binding.setTotalKnowledgePoints(categoryPointCounts.get(category.getId()));
        binding.setCompletedKnowledgePoints(0);
        binding.setInProgressKnowledgePoints(0);
        binding.setNotStartedKnowledgePoints(categoryPointCounts.get(category.getId()));
        binding.setTotalStudyDuration(0);
        binding.setDeleteFlag("N");
        
        studentCategoryBindingMapper.insert(binding);
        createdCount++;
    }
    
    return createdCount;
}
```

## Progress Status Definitions

### Learning Progress Status
- **1 - Not Started**: Student has not started learning this knowledge point
- **2 - In Progress**: Student is currently learning this knowledge point
- **3 - Completed**: Student has completed learning this knowledge point

### Binding Status
- **1 - Bound**: Category is bound to student
- **2 - Learning**: Student is actively learning in this category
- **3 - Completed**: Student has completed all knowledge points in this category

## Initialization Data Example

### Example: Grade 7 (Junior High Year 1)

When a student is registered for Grade 7:

```sql
-- Category Bindings Created
INSERT INTO `student_category_binding` 
(`student_id`, `category_id`, `grade_id`, `binding_status`, 
 `total_knowledge_points`, `completed_knowledge_points`, 
 `in_progress_knowledge_points`, `not_started_knowledge_points`) 
VALUES
(123, 1, 7, 1, 13, 0, 0, 13),  -- Number and Algebra
(123, 2, 7, 1, 10, 0, 0, 10),  -- Geometry
(123, 3, 7, 1, 3, 0, 0, 3),    -- Statistics and Probability
(123, 4, 7, 1, 4, 0, 0, 4);    -- Comprehensive Application

-- Learning Progress Created (Example for first 5 knowledge points)
INSERT INTO `learning_progress` 
(`user_id`, `knowledge_point_id`, `knowledge_category_id`, 
 `progress_status`, `completion_percentage`, `study_duration`) 
VALUES
(123, 101, 1, 1, 0.00, 0),  -- Positive and Negative Numbers
(123, 102, 1, 1, 0.00, 0),  -- Rational Numbers
(123, 103, 1, 1, 0.00, 0),  -- Number Line
(123, 104, 1, 1, 0.00, 0),  -- Opposite Numbers
(123, 105, 1, 1, 0.00, 0);  -- Absolute Value
-- ... (and so on for all 30 knowledge points in Grade 7)
```

## Summary Statistics

### Grade 7 (Junior High Year 1)
- **Total Knowledge Points**: 30
- **Categories**: 4
- **Category Bindings**: 4
- **Learning Progress Records**: 30

### Grade 8 (Junior High Year 2)
- **Total Knowledge Points**: 37
- **Categories**: 4
- **Category Bindings**: 4
- **Learning Progress Records**: 37

### Grade 9 (Junior High Year 3)
- **Total Knowledge Points**: 36
- **Categories**: 4
- **Category Bindings**: 4
- **Learning Progress Records**: 36

## API Endpoints

### Register Student with Courses
```
POST /user/register
```

**Request Body:**
```json
{
  "userAccount": "student001",
  "username": "John Doe",
  "sex": "1",
  "age": 15,
  "password": "encrypted_password",
  "tel": "13800138000",
  "country": "CHN",
  "email": "student001@example.com",
  "grade": "9",
  "role": "S"
}
```

**Response:**
```json
{
  "code": 200,
  "message": "Registration successful, courses assigned automatically",
  "data": true
}
```

### Get Student Learning Progress Stats
```
GET /api/student/course/progress-stats/{userId}
```

**Response:**
```json
{
  "code": 200,
  "message": "Get learning progress stats successful",
  "data": {
    "userId": 123,
    "gradeLevel": 9,
    "gradeName": "Grade 9",
    "totalKnowledgePoints": 36,
    "notStartedCount": 36,
    "inProgressCount": 0,
    "completedCount": 0,
    "overallCompletionPercentage": 0.00,
    "totalStudyDuration": 0,
    "lastStudyTime": null,
    "distribution": {
      "lowProgressCount": 36,
      "mediumProgressCount": 0,
      "highProgressCount": 0,
      "nearCompleteCount": 0,
      "completeCount": 0
    }
  }
}
```

## Key Features

1. **Automatic Initialization**: Learning progress is automatically initialized when a student registers
2. **Grade-Based Assignment**: Knowledge points are assigned based on student's grade level
3. **Category Tracking**: Progress is tracked both at knowledge point level and category level
4. **Duplicate Prevention**: System checks for existing assignments before creating new records
5. **Statistics Integration**: Automatic calculation of progress statistics for each category

## Implementation Notes

- All learning progress records are initialized with `progress_status = 1` (Not Started)
- `completion_percentage` is initialized to `0.00`
- `study_duration` is initialized to `0`
- Student-category bindings are created to track overall progress by category
- The system supports multiple grades (Grades 7, 8, 9 for junior high)
- All data uses English naming conventions for internationalization
