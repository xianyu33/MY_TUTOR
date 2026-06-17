# MYTutor - AI Model Development Documentation

> Last Updated: 2026-06-13

## AI Integration Overview

MYTutor integrates AI capabilities through **Volcano Ark (火山方舟)**, ByteDance's AI platform, to power intelligent tutoring features.

---

## 1. AI Chat Tutor (Volcano Ark)

### Model
- **Provider**: Volcano Ark (ByteDance)
- **Model**: DeepSeek / Doubao series
- **Integration Point**: `com.yy.my_tutor.ark.service.ArkAIService`

### Capabilities
- Context-aware math tutoring conversations
- Multi-turn dialogue with conversation history
- Image understanding (uploaded images in chat)
- Encrypted message storage

### Architecture

```
User Message → ArkClientController → ArkAIService → Volcano Ark API
                    ↑                      ↓
              ChatMessageMapper    AI Response (streaming)
                    ↑                      ↓
              MySQL (encrypted)    User receives response
```

### Key Implementation Details
- **Context Management**: Previous messages loaded as context for continuity
- **Streaming**: Responses streamed to client for real-time display
- **Encryption**: Chat messages encrypted before storage
- **File Upload**: Images uploaded and processed as part of conversation context
- **Large File Support**: Streaming upload for files up to 500MB (no OOM)

### API Endpoints
| Endpoint | Method | Description |
|----------|--------|-------------|
| `/ark/chat` | POST | Send message to AI tutor |
| `/ark/upload` | POST | Upload file/image for AI analysis |

---

## 2. AI Test Generation

### Model
- **Provider**: Volcano Ark
- **Integration Point**: `com.yy.my_tutor.test.service.impl.AITestGenerateServiceImpl`

### Capabilities
- Generate practice tests based on knowledge points
- Adaptive difficulty adjustment
- Multiple question types (choice, fill-in, essay)
- Bilingual content (English/French)

### Test Generation Flow

```
1. Student selects knowledge points
2. System queries knowledge point details + mastery level
3. AI generates questions based on:
   - Knowledge point content
   - Student's current mastery level
   - Target difficulty distribution
4. Questions stored in question bank
5. Test assembled with difficulty-balanced selection
```

### Question Types
| Type | Code | Description |
|------|------|-------------|
| Multiple Choice | 1 | 4 options, single correct answer |
| Fill-in-the-blank | 2 | Short answer required |
| Essay/Solution | 3 | Step-by-step solution required |

### Difficulty Levels
| Level | Code | Description |
|-------|------|-------------|
| Easy | 1 | Basic concept application |
| Medium | 2 | Multi-step problem solving |
| Hard | 3 | Complex reasoning and synthesis |

### Adaptive Test Generation
- **Integration Point**: `com.yy.my_tutor.test.service.impl.AdaptiveTestServiceImpl`
- Adjusts difficulty based on student's historical performance
- Uses `knowledge_mastery` table to determine appropriate difficulty
- Balances question distribution across difficulty levels

---

## 3. AI Course Generation

### Model
- **Provider**: Volcano Ark
- **Integration Point**: `com.yy.my_tutor.course.service.impl.CourseGenerateServiceImpl`

### Capabilities
- Generate structured courses from knowledge points
- Course content with lessons and exercises
- Learning objectives and prerequisites
- Bilingual course content (English/French)

### Course Generation Flow

```
1. Select knowledge points for course
2. AI generates course outline with:
   - Learning objectives
   - Lesson structure
   - Practice exercises
   - Assessment criteria
3. Course content stored with translation support
4. Student can follow structured learning path
```

### Translation Service
- **Integration Point**: `com.yy.my_tutor.course.service.impl.CourseTranslationService`
- Auto-translates course content between English and French
- Preserves mathematical notation and formatting

---

## 4. Question Pool Management

### Architecture Evolution

| Phase | Period | Approach |
|-------|--------|----------|
| v1 | 2025-10 ~ 2026-02 | Cron-based scheduled refill |
| v2 | 2026-02+ | Event-driven refill |

### Event-Driven Architecture (Current)
- **Trigger**: When question count for a knowledge point drops below threshold
- **Action**: Event published → Listener triggers AI generation
- **Benefit**: Real-time replenishment, no lag waiting for cron schedule

```
Question Pool Low → Event Published → QuestionPoolFillJob (listener)
                                            ↓
                                    AI generates new questions
                                            ↓
                                    Questions saved to pool
```

---

## 5. AI Prompt Engineering

### Test Generation Prompt Structure
```
System: You are a math education expert. Generate questions based on:
- Knowledge point: {point_name}
- Difficulty: {difficulty_level}
- Question type: {question_type}
- Grade level: {grade_name}

Output format: JSON with question_content, options, correct_answer, answer_explanation
```

### Course Generation Prompt Structure
```
System: You are a curriculum designer. Create a structured course for:
- Knowledge points: {point_list}
- Grade level: {grade_name}
- Language: {locale}

Output format: JSON with course outline, lessons, exercises, assessments
```

### Chat Tutor Prompt Structure
```
System: You are a patient math tutor. Help the student understand concepts.
- Use step-by-step explanations
- Provide examples when helpful
- Encourage the student
- Language: Match student's language preference
```

---

## 6. AI Model Performance & Optimization

### Response Time Targets
| Feature | Target | Actual |
|---------|--------|--------|
| Chat response (first token) | < 2s | ~1.5s |
| Test generation (10 questions) | < 30s | ~20s |
| Course generation | < 60s | ~45s |

### Optimization Strategies
- **Streaming**: Chat responses streamed token-by-token
- **Caching**: Frequently generated question patterns cached
- **Async**: Email and non-critical AI tasks processed asynchronously
- **Context Window**: Limited to last N messages to control token usage
- **Batch Processing**: Question pool refill processes in batches

### Cost Management
- Token usage monitored per API call
- Question pool refill rate-limited to prevent excessive API calls
- Caching reduces redundant AI calls for similar queries

---

## 7. Future AI Enhancements

| Feature | Priority | Target |
|---------|----------|--------|
| AI Voice Tutoring | Medium | 2026 Q4 |
| Handwriting Recognition | Medium | 2027 Q1 |
| Emotion Detection in Learning | Low | 2027 Q2 |
| Personalized Learning Path AI | High | 2026 Q3 |
| Multi-modal Question Input | Medium | 2027 Q1 |
