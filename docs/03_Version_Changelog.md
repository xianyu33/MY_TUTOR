# MYTutor - Version Update Log (Changelog)

> All versions derived from Git commit history

## Versioning Convention

- **Major (X.0.0)**: Significant feature releases / architecture changes
- **Minor (x.Y.0)**: New feature modules
- **Patch (x.y.Z)**: Bug fixes and minor improvements

---

## v1.0.0 - Project Foundation (2025-04-28)

**Release Date**: 2025-04-28

### Added
- Spring Boot project initialization with Maven
- User registration and login system
- AES password encryption with custom `AesPasswordEncoder`
- JWT token-based authentication
- CORS configuration for frontend integration
- AI chat integration with Volcano Ark (context-aware conversation)
- Chat message encryption
- MySQL database with initial schema
- API documentation generation

### Fixed
- URL configuration issues
- Database schema optimization
- Various startup bugs

---

## v1.1.0 - Communication & Multi-Role (2025-07-28)

**Release Date**: 2025-07-28

### Added
- Email sending via SMTP
- Email verification for user registration
- Batch user creation functionality
- Activity logging system
- Chat history storage and retrieval
- Markdown content support in chat
- User role system: Parent (P), Student (S), Teacher (T)
- Guardian session management for parents
- Image upload support in chat conversations
- GoDaddy email integration for production
- Parent → Student query API
- Additional API endpoints

### Fixed
- Email initialization errors
- Registration error handling
- Connection info issues
- Role-based access control bugs
- List query ordering (now descending by default)
- Merge conflict resolution

---

## v2.0.0 - AI Learning System (2025-11-06)

**Release Date**: 2025-11-06

### Added
- Teacher role with full capabilities
- Math knowledge point system (grade → category → knowledge point hierarchy)
- Teacher-Student binding relationship management
- Verification code (captcha) system
- Grade-subject capability binding
- Knowledge point binding and association logic
- **AI-powered test generation** based on knowledge points
- Learning progress tracking for parents and teachers
- Student batch answer submission API
- Knowledge point detail API
- Improved question generation with difficulty levels

### Fixed
- Removed unused code
- Verification code API improvements
- Configuration updates
- Test question generation logic refinements
- Test question naming corrections
- Multiple test question bug fixes

---

## v2.1.0 - Course & Learning Plan (2026-01-30)

**Release Date**: 2026-01-30

### Added
- Knowledge-point-based test generation
- Historical test record system
- **AI course generation** from knowledge points
- **Learning plan module** (study plan creation and management)
- Comprehensive API documentation update
- Feature update documentation

### Fixed
- Query issue fixes
- API documentation corrections
- Bug fixes in course generation

---

## v2.2.0 - User Profile & Architecture (2026-03-23)

**Release Date**: 2026-03-23

### Added
- Progress-based question generation
- **Avatar upload** and personal info management
- Registration success email notification
- Additional API endpoints

### Changed
- **Architecture: Question pool refill migrated from cron job to event-driven** (significant improvement)
- Course content and structure optimization
- Test generation API optimization
- Difficulty sorting algorithm improvement

### Fixed
- Progress calculation issues (2 fixes)
- Prompt message corrections
- Image address handling fixes
- Return value corrections

---

## v2.3.0 - Email & Scoring (2026-04-23)

**Release Date**: 2026-04-23

### Added
- Email optional validation (non-required field)
- **Scoring system** for test results
- Score calculation and percentage tracking

### Changed
- **Registration email sending migrated to async** (performance improvement)
- Email validation logic adjusted (removed mandatory verification)
- API descriptions updated
- Version numbering updated

### Fixed
- Test name corrections
- API response modifications
- General bug fixes

---

## v3.0.0 - Payment & Security (2026-06-13)

**Release Date**: 2026-06-13

### Added
- **Stripe payment system integration**
  - Checkout session creation
  - Product and price management
  - Subscription management (create, update, cancel)
  - Webhook event handling (15+ event types)
  - Refund processing
  - Payment customer management
  - Order tracking and management
  - Stripe event logging
  - Entitlement service with caching
- **Email verification with code in URL** (click-to-verify flow)
  - GET endpoint for direct email link verification
  - POST endpoint for frontend verification
  - Redis-based token storage with 24-hour expiry
- **User account validity period** (1 month default on registration)
  - `emailVerified` field in user table
  - `expire_time` field in user table
  - Login validation checks email verification and account expiration

### Changed
- Email verification link now points directly to backend API
- Large file upload optimization (streaming, no temp files)
- Multipart and Tomcat configuration for 500MB+ file uploads

### Security
- Removed accidentally exposed Stripe API key
- Payment security utilities added
- Beneficiary validation

---

## Upcoming

### v3.1.0 (Planned: 2026 Q3)
- Mobile app (React Native)
- Multi-language support (French, Chinese)
- Advanced analytics dashboard

### v4.0.0 (Planned: 2026 Q4)
- AI voice tutoring mode
- Curriculum alignment system
- Offline mode with sync
