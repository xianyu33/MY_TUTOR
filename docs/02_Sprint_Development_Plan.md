# MYTutor - Sprint & Development Plan

> Based on Git commit history analysis (2025-04-01 to 2026-06-13)

## Sprint Summary

| Sprint | Period | Focus Area | Commits | Contributors |
|--------|--------|------------|---------|--------------|
| Sprint 1 | 2025-04-01 ~ 2025-04-28 | Project Foundation | 11 | yy |
| Sprint 2 | 2025-06-30 ~ 2025-07-28 | Communication & Roles | 18 | yy |
| Sprint 3 | 2025-10-08 ~ 2025-11-06 | AI Learning System | 22 | yy |
| Sprint 4 | 2025-12-03 ~ 2026-01-30 | Course & Learning Plan | 16 | yiyao, guochao1 |
| Sprint 5 | 2026-02-09 ~ 2026-03-23 | User Profile & Optimization | 14 | yiyao, guochao1 |
| Sprint 6 | 2026-04-11 ~ 2026-04-23 | Email & Scoring | 10 | yiyao |
| Sprint 7 | 2026-06-09 ~ 2026-06-13 | Payment Integration | 5 | yiyao, guochao |

---

## Sprint 1: Project Foundation (2025-04-01 ~ 2025-04-28)

**Sprint Goal**: Establish the core Spring Boot application with user management and AI chat capabilities.

### User Stories
- As a user, I want to register an account so I can access the platform
- As a user, I want to log in securely with JWT authentication
- As a student, I want to chat with an AI tutor about math problems
- As a developer, I want CORS configured for frontend integration

### Task Breakdown

| Date | Type | Task | Assignee |
|------|------|------|----------|
| 2025-04-01 | feat | Project initialization (Spring Boot, Maven, MySQL) | yy |
| 2025-04-14 | feat | User management module (CRUD, registration, login) | yy |
| 2025-04-17 | feat | AES password encryption, security config | yy |
| 2025-04-19 | feat | CORS configuration for cross-origin requests | yy |
| 2025-04-21 | feat | AI chat with Volcano Ark (context-aware conversation) | yy |
| 2025-04-22 | feat | Chat message encryption | yy |
| 2025-04-23 | fix | Database schema optimization | yy |
| 2025-04-24 | feat | JWT authentication, token-based login | yy |
| 2025-04-24 | feat | API documentation generation | yy |
| 2025-04-24 | fix | URL configuration fix | yy |
| 2025-04-28 | fix | Bug fixes and stabilization | yy |

### Sprint Retrospective
- **What went well**: Rapid project setup, core features delivered on time
- **What to improve**: Need automated testing, better error handling
- **Action items**: Add unit tests, implement global exception handler

---

## Sprint 2: Communication & Roles (2025-06-30 ~ 2025-07-28)

**Sprint Goal**: Implement email communication, multi-role user management, and chat enhancements.

### User Stories
- As a new user, I want to receive a verification email after registration
- As a parent, I want to view and manage my children's accounts
- As a user, I want to upload images in chat conversations
- As a teacher, I want to see my student sessions

### Task Breakdown

| Date | Type | Task | Assignee |
|------|------|------|----------|
| 2025-06-30 | feat | Email sending via SMTP | yy |
| 2025-07-03 | feat | Email verification for registration | yy |
| 2025-07-07 | feat | Batch user creation, activity logging | yy |
| 2025-07-08 | feat | Historical message insertion and listing | yy |
| 2025-07-09 | feat | Markdown content support | yy |
| 2025-07-12 | fix | Email initialization, registration error fix | yy |
| 2025-07-13 | feat | User role differentiation (Parent/Student/Teacher) | yy |
| 2025-07-13 | fix | Connection info fix, role fix | yy |
| 2025-07-14 | feat | Image upload in chat | yy |
| 2025-07-14 | fix | Merge conflict resolution | yy |
| 2025-07-16 | fix | Code cleanup | yy |
| 2025-07-17 | fix | List query ordering (descending) | yy |
| 2025-07-19 | feat | GoDaddy email integration | yy |
| 2025-07-20 | feat | Query students under parent | yy |
| 2025-07-28 | feat | Additional API endpoints | yy |
| 2025-07-28 | fix | Prompt message refinement | yy |

### Sprint Retrospective
- **What went well**: Multi-role system implemented, email system working
- **What to improve**: Email reliability, need async processing
- **Action items**: Migrate to async email sending, add email retry logic

---

## Sprint 3: AI Learning System (2025-10-08 ~ 2025-11-06)

**Sprint Goal**: Build the AI-powered knowledge point system, test generation, and learning progress tracking.

### User Stories
- As a teacher, I want to create and manage knowledge points for different grades
- As a student, I want AI to generate practice tests based on my knowledge level
- As a parent, I want to track my child's learning progress
- As a student, I want to submit answers and get immediate feedback

### Task Breakdown

| Date | Type | Task | Assignee |
|------|------|------|----------|
| 2025-10-08 | feat | Teacher role, math knowledge point system | yy |
| 2025-10-09 | feat | Teacher-student binding relationship | yy |
| 2025-10-13 | feat | Binding relationship refinement | yy |
| 2025-10-13 | fix | Code optimization | yy |
| 2025-10-14 | feat | Verification code (captcha) system | yy |
| 2025-10-14 | fix | Remove unused code | yy |
| 2025-10-21 | feat | Grade-subject capability binding | yy |
| 2025-10-22 | feat | Verification code API update | yy |
| 2025-10-22 | fix | Configuration update | yy |
| 2025-10-23 | feat | Knowledge point binding logic | yy |
| 2025-10-26 | feat | AI test generation content refinement | yy |
| 2025-10-26 | feat | Learning progress for parent/teacher views | yy |
| 2025-10-27 | feat | Batch answer submission API | yy |
| 2025-10-29 | fix | API modifications | yy |
| 2025-10-31 | feat | Knowledge point detail API | yy |
| 2025-11-01 | fix | Question generation logic update | yy |
| 2025-11-03 | feat | Question generation improvement | yy |
| 2025-11-05 | fix | Test question bug fixes | yy |
| 2025-11-06 | fix | Test question naming fixes | yy |

### Sprint Retrospective
- **What went well**: Core AI learning system functional, knowledge point system complete
- **What to improve**: Test generation quality, need more question types
- **Action items**: Improve AI prompt engineering, add adaptive difficulty

---

## Sprint 4: Course & Learning Plan (2025-12-03 ~ 2026-01-30)

**Sprint Goal**: Implement AI course generation, learning plan management, and comprehensive API documentation.

### User Stories
- As a student, I want AI to generate a personalized course based on knowledge points
- As a student, I want a structured learning plan with milestones
- As a developer, I want complete API documentation

### Task Breakdown

| Date | Type | Task | Assignee |
|------|------|------|----------|
| 2025-12-03 | feat | Additional information fields | yiyao |
| 2025-12-05 | feat | Knowledge-point-based test generation, historical tests | yiyao |
| 2025-12-08 | feat | Additional APIs | yiyao |
| 2026-01-03 | fix | Query issue fix | yiyao |
| 2026-01-07 | feat | Knowledge-point-based course generation | guochao1 |
| 2026-01-09 | feat | Test generation module | guochao1 |
| 2026-01-14 | feat | Test generation + knowledge point query | guochao1 |
| 2026-01-19 | feat | Additional APIs | yiyao |
| 2026-01-19 | feat | Learning plan module | guochao1 |
| 2026-01-21 | feat | API documentation | guochao1 |
| 2026-01-21 | fix | API documentation + bug fixes | guochao1 |
| 2026-01-28 | feat | Feature update + API documentation | guochao1 |

### Sprint Retrospective
- **What went well**: Team expanded (guochao1 joined), course system functional
- **What to improve**: Better branch management, code review process
- **Action items**: Establish PR review workflow, add integration tests

---

## Sprint 5: User Profile & Optimization (2026-02-09 ~ 2026-03-23)

**Sprint Goal**: Add user profile features, optimize existing systems, and improve architecture.

### User Stories
- As a user, I want to upload an avatar and update my profile
- As a developer, I want the question pool refill to be event-driven instead of cron-based
- As a student, I want progress-based questions

### Task Breakdown

| Date | Type | Task | Assignee |
|------|------|------|----------|
| 2026-02-09 | feat | Additional request parameters | yiyao |
| 2026-02-10 | feat | Course optimization | guochao1 |
| 2026-02-23 | feat | Progress-based questions | yiyao |
| 2026-02-23 | fix | Progress issue fixes (2 commits) | yiyao |
| 2026-02-24 | fix | Prompt fix | yiyao |
| 2026-02-24 | feat | Avatar upload, personal info management | yiyao |
| 2026-02-25 | fix | Bug fix, return value fix | yiyao |
| 2026-02-26 | feat | Question pool: cron → event-driven | guochao1 |
| 2026-03-04 | feat | Additional APIs | yiyao |
| 2026-03-04 | fix | Image address fix | yiyao |
| 2026-03-18 | fix | Test generation API optimization | guochao1 |
| 2026-03-18 | fix | Difficulty sorting | guochao1 |
| 2026-03-23 | feat | Registration success email | yiyao |

### Sprint Retrospective
- **What went well**: Architecture improvement (event-driven), user profile features
- **What to improve**: More thorough testing before commit
- **Action items**: Add automated testing pipeline, improve code coverage

---

## Sprint 6: Email & Scoring (2026-04-11 ~ 2026-04-23)

**Sprint Goal**: Improve email verification flow, add scoring system, and refine existing features.

### Task Breakdown

| Date | Type | Task | Assignee |
|------|------|------|----------|
| 2026-04-11 | feat | Email optional validation | yiyao |
| 2026-04-11 | fix | Test name correction | yiyao |
| 2026-04-13 | feat | Scoring system (2 commits) | yiyao |
| 2026-04-14 | fix | Remove email validation (adjustment) | yiyao |
| 2026-04-15 | fix | Description update | yiyao |
| 2026-04-17 | fix | API modification | yiyao |
| 2026-04-17 | fix | Version update | yiyao |
| 2026-04-23 | fix | Bug fix | yiyao |
| 2026-04-23 | fix | Registration email → async sending | yiyao |

---

## Sprint 7: Payment Integration (2026-06-09 ~ 2026-06-13)

**Sprint Goal**: Integrate Stripe payment system, add email verification with code in URL, and account validity period.

### Task Breakdown

| Date | Type | Task | Assignee |
|------|------|------|----------|
| 2026-06-09 | feat | Email verification with code in URL | yiyao |
| 2026-06-09 | feat | User account validity period (1 month default) | yiyao |
| 2026-06-10 | feat | Stripe payment system integration | guochao |
| 2026-06-13 | fix | Payment API refinement | guochao |
| 2026-06-13 | fix | Remove exposed API key | guochao |

### Sprint Retrospective
- **What went well**: Stripe integration completed quickly
- **Security concern**: API key was briefly exposed in commit — immediately fixed
- **Action items**: Add pre-commit hook to scan for secrets, implement key vault
