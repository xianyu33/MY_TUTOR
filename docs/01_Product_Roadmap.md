# MYTutor - Product Roadmap

> Last Updated: 2026-06-13 | Project Start: 2025-04-01

## Vision

MYTutor is an AI-powered K-12 math tutoring platform that provides personalized learning experiences through intelligent question generation, adaptive testing, and comprehensive progress tracking for students, parents, and teachers.

## Phase Overview

| Phase | Period | Theme | Status |
|-------|--------|-------|--------|
| Phase 1 | 2025 Q2 | Foundation & Core Infrastructure | Completed |
| Phase 2 | 2025 Q3 | Communication & User Management | Completed |
| Phase 3 | 2025 Q4 | AI-Powered Learning System | Completed |
| Phase 4 | 2026 Q1 | Course System & Learning Plans | Completed |
| Phase 5 | 2026 Q2 | Payment & Monetization | In Progress |

---

## Phase 1: Foundation & Core Infrastructure (2025 Q2)

**Goal**: Establish the technical foundation and core user management system.

| Milestone | Target Date | Status | Key Deliverables |
|-----------|-------------|--------|------------------|
| Project Initialization | 2025-04-01 | Done | Spring Boot project setup, database design |
| User Management | 2025-04-14 | Done | User registration, login, JWT authentication |
| Encryption & Security | 2025-04-17 | Done | AES password encoding, security configuration |
| CORS Configuration | 2025-04-19 | Done | Cross-origin support for frontend integration |
| AI Chat Foundation | 2025-04-21 | Done | Context-aware AI conversation with Volcano Ark |
| Chat Encryption | 2025-04-22 | Done | End-to-end message encryption |
| Database Optimization | 2025-04-23 | Done | Schema refinement, indexing |
| Authentication System | 2025-04-24 | Done | JWT token-based auth, API documentation |
| Bug Fixes & Stabilization | 2025-04-28 | Done | URL fixes, configuration corrections |

---

## Phase 2: Communication & User Management (2025 Q3)

**Goal**: Build communication features and multi-role user management.

| Milestone | Target Date | Status | Key Deliverables |
|-----------|-------------|--------|------------------|
| Email System | 2025-06-30 | Done | Email sending via SMTP |
| Email Verification | 2025-07-03 | Done | Registration email verification flow |
| Batch User Management | 2025-07-07 | Done | Bulk user creation, activity logging |
| Chat History | 2025-07-08 | Done | Historical message storage and retrieval |
| Email Infrastructure | 2025-07-12 | Done | GoDaddy email integration, registration error fixes |
| User Roles | 2025-07-13 | Done | Parent/Student/Teacher role differentiation, guardian session management |
| Image Upload in Chat | 2025-07-14 | Done | Image upload support in conversations |
| GoDaddy Email | 2025-07-19 | Done | Production email via GoDaddy SMTP |
| Parent-Student Management | 2025-07-20 | Done | Query students under parent account |
| API Extensions | 2025-07-28 | Done | Additional API endpoints, prompt refinements |

---

## Phase 3: AI-Powered Learning System (2025 Q4)

**Goal**: Build the core AI-driven learning, testing, and knowledge management system.

| Milestone | Target Date | Status | Key Deliverables |
|-----------|-------------|--------|------------------|
| Teacher Role & Knowledge Points | 2025-10-08 | Done | Teacher role, math knowledge point system |
| Teacher-Student Binding | 2025-10-09 | Done | Teacher-student relationship management |
| Verification Code System | 2025-10-14 | Done | Captcha/verification code for security |
| Grade-Subject Binding | 2025-10-21 | Done | Grade and subject capability binding |
| Knowledge Point Binding | 2025-10-23 | Done | Knowledge point association logic |
| AI Test Generation | 2025-10-26 | Done | AI-powered test question generation |
| Learning Progress Tracking | 2025-10-26 | Done | Progress display for parents/teachers |
| Batch Answer Submission | 2025-10-27 | Done | Student batch answer submission API |
| Knowledge Point Details | 2025-10-31 | Done | Detailed knowledge point API |
| Test Generation Refinement | 2025-11-03 | Done | Improved question generation logic |
| Bug Fixes & Stabilization | 2025-11-05 | Done | Test question fixes, naming corrections |
| Knowledge-Based Test Generation | 2025-12-05 | Done | Generate tests from knowledge points, historical test records |

---

## Phase 4: Course System & Learning Plans (2026 Q1)

**Goal**: Build the course generation system, learning plans, and user profile features.

| Milestone | Target Date | Status | Key Deliverables |
|-----------|-------------|--------|------------------|
| Course Generation | 2026-01-07 | Done | AI course generation from knowledge points |
| Learning Plan System | 2026-01-19 | Done | Study plan creation and management |
| API Documentation | 2026-01-21 | Done | Comprehensive API documentation update |
| Course Optimization | 2026-02-10 | Done | Course content and structure improvements |
| Progress Questions | 2026-02-23 | Done | Progress-based question generation |
| User Profile | 2026-02-24 | Done | Avatar upload, personal info management |
| Event-Driven Architecture | 2026-02-26 | Done | Question pool refill: cron → event-driven |
| Test Generation Optimization | 2026-03-18 | Done | API optimization, difficulty sorting |
| Registration Email | 2026-03-23 | Done | Registration success email notification |

---

## Phase 5: Payment & Monetization (2026 Q2)

**Goal**: Integrate Stripe payment system and implement subscription model.

| Milestone | Target Date | Status | Key Deliverables |
|-----------|-------------|--------|------------------|
| Email Verification Flow | 2026-04-11 | Done | Email verification with async sending |
| Scoring System | 2026-04-13 | Done | Test scoring and result calculation |
| Async Email | 2026-04-23 | Done | Registration email → async sending |
| Email Verification Link | 2026-06-09 | Done | Click-to-verify email link with code in URL |
| Account Validity Period | 2026-06-09 | Done | User account expiration (1 month default) |
| Stripe Payment Integration | 2026-06-10 | Done | Full Stripe payment system with checkout, subscriptions, webhooks |
| Payment API Refinement | 2026-06-13 | Done | Payment endpoint fixes, security key management |

---

## Upcoming (2026 Q3+)

| Feature | Priority | Description |
|---------|----------|-------------|
| Mobile App (React Native) | High | iOS/Android native app |
| Multi-language Support | High | French, Chinese localization |
| Advanced Analytics Dashboard | Medium | Teacher/parent analytics dashboard |
| AI Tutor Voice Mode | Medium | Voice-based AI tutoring |
| Curriculum Alignment | Medium | Provincial/state curriculum standards mapping |
| Parent Payment Portal | High | Self-service subscription management |
| Offline Mode | Low | Offline question practice with sync |
