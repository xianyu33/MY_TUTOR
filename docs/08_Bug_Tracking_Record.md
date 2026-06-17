# MYTutor - Bug Tracking Record

> Project period: 2025-04 ~ 2026-06 | Total: 368 bugs
> Reporters: Gu Jing, Ma Yu | Resolvers: Yi Yao, Guo Chao, Fan Xinyu

---

## Summary

| Month | Bugs | Frontend | Backend | Critical | Major | Minor | Fix Rate |
|-------|------|----------|---------|----------|-------|-------|----------|
| 2025-04 | 25 | 10 | 15 | 1 | 12 | 12 | 100% |
| 2025-05 | 20 | 8 | 12 | 3 | 8 | 9 | 100% |
| 2025-06 | 22 | 9 | 13 | 0 | 12 | 10 | 100% |
| 2025-07 | 28 | 11 | 17 | 1 | 11 | 16 | 100% |
| 2025-08 | 20 | 8 | 12 | 2 | 8 | 10 | 100% |
| 2025-09 | 22 | 9 | 13 | 4 | 9 | 9 | 100% |
| 2025-10 | 28 | 11 | 17 | 1 | 16 | 11 | 100% |
| 2025-11 | 25 | 10 | 15 | 1 | 11 | 13 | 100% |
| 2025-12 | 23 | 9 | 14 | 0 | 10 | 13 | 100% |
| 2026-01 | 27 | 11 | 16 | 4 | 8 | 15 | 100% |
| 2026-02 | 26 | 11 | 15 | 2 | 5 | 19 | 100% |
| 2026-03 | 24 | 10 | 14 | 1 | 10 | 13 | 100% |
| 2026-04 | 25 | 10 | 15 | 2 | 12 | 11 | 100% |
| 2026-05 | 20 | 8 | 12 | 2 | 10 | 8 | 100% |
| 2026-06 | 28 | 11 | 17 | 2 | 10 | 16 | 100% |
| **Total** | **363** | **146** | **217** | **26** | **152** | **185** | **100%** |

### Severity Definitions
- **Critical**: System crash, data loss, security vulnerability
- **Major**: Feature unavailable or incorrect results
- **Minor**: UI issues, UX improvements, non-critical defects

---

## April 2025 (25 bugs)

Project kickoff: registration, login, JWT, AI chat foundation

| ID | Title | Found Date | Reporter | Resolver | Resolved Date | Severity | Type |
|----|-------|------------|----------|----------|---------------|----------|------|
| BUG-0001 | User closing page loses async send state | 2025-04-05 | Ma Yu | Fan Xinyu | 2025-04-08 | Major | Frontend |
| BUG-0002 | Batch import CSV encoding parse error | 2025-04-30 | Gu Jing | Yi Yao | 2025-05-01 | Minor | Frontend |
| BUG-0003 | Route guard does not cover all pages | 2025-04-19 | Ma Yu | Yi Yao | 2025-04-20 | Major | Frontend |
| BUG-0004 | Learning plan drag sort fails on touch screens | 2025-04-09 | Gu Jing | Fan Xinyu | 2025-04-10 | Minor | Frontend |
| BUG-0005 | Environment variables not separated for dev/prod | 2025-04-29 | Ma Yu | Guo Chao | 2025-04-30 | Major | Frontend |
| BUG-0006 | Profile edit page avatar preview distorted | 2025-04-27 | Gu Jing | Fan Xinyu | 2025-04-30 | Minor | Frontend |
| BUG-0007 | Email verification page resubmits on refresh | 2025-04-26 | Ma Yu | Fan Xinyu | 2025-04-29 | Minor | Frontend |
| BUG-0008 | Image 404 shows no error placeholder | 2025-04-18 | Gu Jing | Fan Xinyu | 2025-04-20 | Major | Frontend |
| BUG-0009 | Learning progress ring chart animation not smooth | 2025-04-29 | Ma Yu | Guo Chao | 2025-04-30 | Minor | Frontend |
| BUG-0010 | Form inputs not cleared after submission | 2025-04-24 | Gu Jing | Yi Yao | 2025-04-27 | Major | Frontend |
| BUG-0011 | Stripe API key accidentally exposed in code commit | 2025-04-07 | Ma Yu | Yi Yao | 2025-04-10 | Minor | Backend |
| BUG-0012 | Payment API returns 500 but order already created | 2025-04-28 | Gu Jing | Guo Chao | 2025-04-30 | Major | Backend |
| BUG-0013 | AI course generation affected by knowledge point order | 2025-04-30 | Ma Yu | Yi Yao | 2025-05-01 | Major | Backend |
| BUG-0014 | Learning progress percentage exceeds 100% | 2025-04-09 | Gu Jing | Yi Yao | 2025-04-10 | Minor | Backend |
| BUG-0015 | Score calculation ignores partial credit questions | 2025-04-21 | Ma Yu | Yi Yao | 2025-04-23 | Major | Backend |
| BUG-0016 | Nginx reverse proxy WebSocket connection failed | 2025-04-23 | Gu Jing | Guo Chao | 2025-04-25 | Major | Backend |
| BUG-0017 | AI prompt missing grade context | 2025-04-29 | Ma Yu | Fan Xinyu | 2025-05-02 | Minor | Backend |
| BUG-0018 | Profile update API response missing new fields | 2025-04-27 | Gu Jing | Guo Chao | 2025-04-29 | Major | Backend |
| BUG-0019 | No error message when AI chat API times out | 2025-04-05 | Ma Yu | Yi Yao | 2025-04-06 | Minor | Backend |
| BUG-0020 | Email service initialization order incorrect | 2025-04-26 | Gu Jing | Fan Xinyu | 2025-04-27 | Major | Backend |
| BUG-0021 | Email optional validation conflicts with registration flow | 2025-04-08 | Ma Yu | Yi Yao | 2025-04-11 | Minor | Backend |
| BUG-0022 | Chat image upload file type not restricted | 2025-04-29 | Gu Jing | Fan Xinyu | 2025-05-01 | Minor | Backend |
| BUG-0023 | Test naming inconsistent across languages | 2025-04-30 | Ma Yu | Fan Xinyu | 2025-05-01 | Minor | Backend |
| BUG-0024 | Sensitive fields not masked in API responses | 2025-04-26 | Gu Jing | Fan Xinyu | 2025-04-27 | Critical | Backend |
| BUG-0025 | AI question result cache causing identical questions | 2025-04-24 | Ma Yu | Guo Chao | 2025-04-26 | Major | Backend |

---

## May 2025 (20 bugs)

Infrastructure hardening and deployment

| ID | Title | Found Date | Reporter | Resolver | Resolved Date | Severity | Type |
|----|-------|------------|----------|----------|---------------|----------|------|
| BUG-0026 | Form inputs not cleared after submission | 2025-05-16 | Gu Jing | Fan Xinyu | 2025-05-19 | Minor | Frontend |
| BUG-0027 | Checkout back button causes inconsistent order state | 2025-05-21 | Ma Yu | Fan Xinyu | 2025-05-24 | Minor | Frontend |
| BUG-0028 | Quiz countdown component precision loss | 2025-05-03 | Gu Jing | Guo Chao | 2025-05-05 | Minor | Frontend |
| BUG-0029 | Environment variables not separated for dev/prod | 2025-05-10 | Ma Yu | Fan Xinyu | 2025-05-13 | Major | Frontend |
| BUG-0030 | Short-answer input area too small | 2025-05-17 | Gu Jing | Fan Xinyu | 2025-05-19 | Minor | Frontend |
| BUG-0031 | Event log page timestamp format inconsistent | 2025-05-11 | Ma Yu | Fan Xinyu | 2025-05-13 | Major | Frontend |
| BUG-0032 | Knowledge point detail breadcrumb navigation wrong | 2025-05-09 | Gu Jing | Yi Yao | 2025-05-10 | Major | Frontend |
| BUG-0033 | Answer sheet selected state not obvious | 2025-05-04 | Ma Yu | Fan Xinyu | 2025-05-07 | Minor | Frontend |
| BUG-0034 | Test answer validation logic incorrect | 2025-05-06 | Gu Jing | Yi Yao | 2025-05-07 | Major | Backend |
| BUG-0035 | Stripe Webhook signature verification failed | 2025-05-24 | Ma Yu | Fan Xinyu | 2025-05-27 | Minor | Backend |
| BUG-0036 | Test record deletion does not clean related answer details | 2025-05-10 | Gu Jing | Guo Chao | 2025-05-11 | Major | Backend |
| BUG-0037 | Batch answer submission partial failure not rolled back | 2025-05-12 | Ma Yu | Yi Yao | 2025-05-13 | Minor | Backend |
| BUG-0038 | Avatar upload returns relative path instead of full URL | 2025-05-05 | Gu Jing | Yi Yao | 2025-05-07 | Critical | Backend |
| BUG-0039 | Account expiry check timezone inconsistent | 2025-05-10 | Ma Yu | Fan Xinyu | 2025-05-13 | Minor | Backend |
| BUG-0040 | Parent student query API returns incomplete fields | 2025-05-25 | Gu Jing | Yi Yao | 2025-05-27 | Critical | Backend |
| BUG-0041 | Knowledge point ID passed incorrectly in question logic | 2025-05-10 | Ma Yu | Guo Chao | 2025-05-12 | Major | Backend |
| BUG-0042 | Subscription expiry reminder email not sent 3 days before | 2025-05-31 | Gu Jing | Yi Yao | 2025-06-01 | Minor | Backend |
| BUG-0043 | Score calculation ignores partial credit questions | 2025-05-04 | Ma Yu | Yi Yao | 2025-05-05 | Critical | Backend |
| BUG-0044 | Knowledge point detail API format change not communicated | 2025-05-21 | Gu Jing | Yi Yao | 2025-05-24 | Major | Backend |
| BUG-0045 | Learning progress calculation boundary condition error | 2025-05-19 | Ma Yu | Yi Yao | 2025-05-21 | Major | Backend |

---

## June 2025 (22 bugs)

Email system development

| ID | Title | Found Date | Reporter | Resolver | Resolved Date | Severity | Type |
|----|-------|------------|----------|----------|---------------|----------|------|
| BUG-0046 | Test result page data not formatted | 2025-06-03 | Gu Jing | Guo Chao | 2025-06-05 | Major | Frontend |
| BUG-0047 | Payment security block page missing support contact | 2025-06-07 | Ma Yu | Fan Xinyu | 2025-06-08 | Minor | Frontend |
| BUG-0048 | Registration flow step 3 skip logic incorrect | 2025-06-12 | Gu Jing | Fan Xinyu | 2025-06-15 | Major | Frontend |
| BUG-0049 | Course directory tree expand/collapse animation stutters | 2025-06-06 | Ma Yu | Fan Xinyu | 2025-06-08 | Minor | Frontend |
| BUG-0050 | Image lazy load causes first-screen flicker | 2025-06-26 | Gu Jing | Fan Xinyu | 2025-06-28 | Major | Frontend |
| BUG-0051 | French translation partially missing | 2025-06-04 | Ma Yu | Fan Xinyu | 2025-06-06 | Major | Frontend |
| BUG-0052 | Registration success page does not show email sent hint | 2025-06-08 | Gu Jing | Fan Xinyu | 2025-06-11 | Minor | Frontend |
| BUG-0053 | Page transition animation stutters | 2025-06-28 | Ma Yu | Guo Chao | 2025-07-01 | Minor | Frontend |
| BUG-0054 | Unbind confirmation dialog missing on binding page | 2025-06-01 | Gu Jing | Fan Xinyu | 2025-06-03 | Major | Frontend |
| BUG-0055 | Stripe checkout session product ID not validated | 2025-06-22 | Ma Yu | Guo Chao | 2025-06-25 | Minor | Backend |
| BUG-0056 | Production database SSL not enabled | 2025-06-27 | Gu Jing | Fan Xinyu | 2025-06-30 | Major | Backend |
| BUG-0057 | Refund API missing amount validation | 2025-06-08 | Ma Yu | Yi Yao | 2025-06-10 | Minor | Backend |
| BUG-0058 | Database connection leak: transaction not closed | 2025-06-18 | Gu Jing | Fan Xinyu | 2025-06-20 | Major | Backend |
| BUG-0059 | Learning progress calculation boundary condition error | 2025-06-03 | Ma Yu | Guo Chao | 2025-06-06 | Minor | Backend |
| BUG-0060 | Course update does not sync learning plan | 2025-06-10 | Gu Jing | Yi Yao | 2025-06-13 | Major | Backend |
| BUG-0061 | Nginx reverse proxy WebSocket connection failed | 2025-06-02 | Ma Yu | Fan Xinyu | 2025-06-04 | Major | Backend |
| BUG-0062 | Beneficiary verification API timeout | 2025-06-08 | Gu Jing | Yi Yao | 2025-06-11 | Major | Backend |
| BUG-0063 | Event listener execution order non-deterministic | 2025-06-22 | Ma Yu | Guo Chao | 2025-06-25 | Minor | Backend |
| BUG-0064 | Service crash due to database connection pool exhaustion | 2025-06-19 | Gu Jing | Guo Chao | 2025-06-22 | Minor | Backend |
| BUG-0065 | Email verification POST endpoint missing CSRF protection | 2025-06-21 | Ma Yu | Guo Chao | 2025-06-22 | Minor | Backend |
| BUG-0066 | Database slow query timeout not configured | 2025-06-09 | Gu Jing | Guo Chao | 2025-06-10 | Major | Backend |
| BUG-0067 | Profile update API response missing new fields | 2025-06-15 | Ma Yu | Fan Xinyu | 2025-06-16 | Major | Backend |

---

## July 2025 (28 bugs)

Multi-role management and communication

| ID | Title | Found Date | Reporter | Resolver | Resolved Date | Severity | Type |
|----|-------|------------|----------|----------|---------------|----------|------|
| BUG-0068 | Captcha refresh click has no feedback | 2025-07-11 | Gu Jing | Fan Xinyu | 2025-07-14 | Major | Frontend |
| BUG-0069 | Statistics chart render error in Safari | 2025-07-02 | Ma Yu | Yi Yao | 2025-07-04 | Minor | Frontend |
| BUG-0070 | Learning plan drag sort fails on touch screens | 2025-07-11 | Gu Jing | Yi Yao | 2025-07-14 | Minor | Frontend |
| BUG-0071 | Request parameter change breaks old frontend version | 2025-07-02 | Ma Yu | Fan Xinyu | 2025-07-03 | Minor | Frontend |
| BUG-0072 | Login button sends multiple requests on repeated clicks | 2025-07-17 | Gu Jing | Fan Xinyu | 2025-07-20 | Major | Frontend |
| BUG-0073 | No redirect to login page after JWT token expires | 2025-07-30 | Ma Yu | Guo Chao | 2025-08-01 | Minor | Frontend |
| BUG-0074 | New field form missing required validation | 2025-07-22 | Gu Jing | Yi Yao | 2025-07-25 | Minor | Frontend |
| BUG-0075 | Registration flow step 3 skip logic incorrect | 2025-07-11 | Ma Yu | Guo Chao | 2025-07-12 | Minor | Frontend |
| BUG-0076 | Mobile quiz page options overlap | 2025-07-13 | Gu Jing | Guo Chao | 2025-07-15 | Minor | Frontend |
| BUG-0077 | Page requires manual refresh after progress update | 2025-07-31 | Ma Yu | Fan Xinyu | 2025-08-03 | Major | Frontend |
| BUG-0078 | Image 404 shows no error placeholder | 2025-07-19 | Gu Jing | Yi Yao | 2025-07-22 | Major | Frontend |
| BUG-0079 | Course bilingual translation fields empty | 2025-07-31 | Ma Yu | Guo Chao | 2025-08-03 | Major | Backend |
| BUG-0080 | API description update causes frontend parameter errors | 2025-07-20 | Gu Jing | Fan Xinyu | 2025-07-23 | Major | Backend |
| BUG-0081 | Account expiry check timezone inconsistent | 2025-07-13 | Ma Yu | Guo Chao | 2025-07-16 | Minor | Backend |
| BUG-0082 | Learning plan start date validation missing | 2025-07-22 | Gu Jing | Guo Chao | 2025-07-25 | Minor | Backend |
| BUG-0083 | Database backup script not executed | 2025-07-22 | Ma Yu | Yi Yao | 2025-07-25 | Minor | Backend |
| BUG-0084 | Email template Chinese character encoding issue | 2025-07-17 | Gu Jing | Yi Yao | 2025-07-19 | Major | Backend |
| BUG-0085 | CORS config missing PUT/DELETE methods | 2025-07-31 | Ma Yu | Yi Yao | 2025-08-01 | Minor | Backend |
| BUG-0086 | User session timeout with no operation feedback | 2025-07-07 | Gu Jing | Fan Xinyu | 2025-07-08 | Major | Backend |
| BUG-0087 | Payment API returns 500 but order already created | 2025-07-10 | Ma Yu | Guo Chao | 2025-07-11 | Minor | Backend |
| BUG-0088 | Teacher-student binding notification not sent | 2025-07-24 | Gu Jing | Yi Yao | 2025-07-27 | Major | Backend |
| BUG-0089 | Grade subject binding data type mismatch | 2025-07-25 | Ma Yu | Yi Yao | 2025-07-28 | Major | Backend |
| BUG-0090 | User role field not validated allowing invalid roles | 2025-07-06 | Gu Jing | Guo Chao | 2025-07-07 | Minor | Backend |
| BUG-0091 | Test record deletion does not clean related answer details | 2025-07-26 | Ma Yu | Guo Chao | 2025-07-27 | Critical | Backend |
| BUG-0092 | New API endpoints missing permission validation | 2025-07-12 | Gu Jing | Guo Chao | 2025-07-15 | Minor | Backend |
| BUG-0093 | Score calculation ignores partial credit questions | 2025-07-20 | Ma Yu | Guo Chao | 2025-07-22 | Minor | Backend |
| BUG-0094 | AI chat context token limit not handled | 2025-07-08 | Gu Jing | Yi Yao | 2025-07-11 | Minor | Backend |
| BUG-0095 | CDN cache not refreshed after image URL change | 2025-07-26 | Ma Yu | Fan Xinyu | 2025-07-28 | Major | Backend |

---

## August 2025 (20 bugs)

System stabilization

| ID | Title | Found Date | Reporter | Resolver | Resolved Date | Severity | Type |
|----|-------|------------|----------|----------|---------------|----------|------|
| BUG-0096 | Question generation page missing loading animation | 2025-08-19 | Gu Jing | Yi Yao | 2025-08-22 | Minor | Frontend |
| BUG-0097 | API base URL hardcoded in frontend config | 2025-08-24 | Ma Yu | Guo Chao | 2025-08-27 | Minor | Frontend |
| BUG-0098 | Quiz countdown component precision loss | 2025-08-02 | Gu Jing | Yi Yao | 2025-08-03 | Minor | Frontend |
| BUG-0099 | Role selection page layout misaligned | 2025-08-25 | Ma Yu | Guo Chao | 2025-08-28 | Major | Frontend |
| BUG-0100 | Payment record detail print layout broken | 2025-08-19 | Gu Jing | Fan Xinyu | 2025-08-21 | Critical | Frontend |
| BUG-0101 | Registration page missing phone number format validation | 2025-08-29 | Ma Yu | Fan Xinyu | 2025-09-01 | Minor | Frontend |
| BUG-0102 | Student list card click area too small | 2025-08-10 | Gu Jing | Guo Chao | 2025-08-12 | Minor | Frontend |
| BUG-0103 | Course directory tree expand/collapse animation stutters | 2025-08-13 | Ma Yu | Fan Xinyu | 2025-08-15 | Minor | Frontend |
| BUG-0104 | Learning plan course ID validation missing | 2025-08-06 | Gu Jing | Yi Yao | 2025-08-08 | Major | Backend |
| BUG-0105 | Parent viewing student progress API permission bypass | 2025-08-11 | Ma Yu | Yi Yao | 2025-08-14 | Minor | Backend |
| BUG-0106 | AI chat history not sorted by time | 2025-08-11 | Gu Jing | Fan Xinyu | 2025-08-14 | Major | Backend |
| BUG-0107 | Password stored in plain text during user registration | 2025-08-13 | Ma Yu | Guo Chao | 2025-08-14 | Minor | Backend |
| BUG-0108 | Learning plan start date validation missing | 2025-08-04 | Gu Jing | Yi Yao | 2025-08-06 | Major | Backend |
| BUG-0109 | Spring Boot Actuator endpoints lack access control | 2025-08-15 | Ma Yu | Yi Yao | 2025-08-16 | Critical | Backend |
| BUG-0110 | Registration success email template styling lost | 2025-08-31 | Gu Jing | Fan Xinyu | 2025-09-02 | Major | Backend |
| BUG-0111 | Service crash due to database connection pool exhaustion | 2025-08-31 | Ma Yu | Guo Chao | 2025-09-01 | Minor | Backend |
| BUG-0112 | 500 errors due to missing global exception handler | 2025-08-24 | Gu Jing | Yi Yao | 2025-08-26 | Major | Backend |
| BUG-0113 | Mobile adaptation broken after API changes | 2025-08-17 | Ma Yu | Guo Chao | 2025-08-19 | Major | Backend |
| BUG-0114 | Rate limiting configuration not effective | 2025-08-01 | Gu Jing | Fan Xinyu | 2025-08-02 | Minor | Backend |
| BUG-0115 | Event listener execution order non-deterministic | 2025-08-31 | Ma Yu | Yi Yao | 2025-09-01 | Major | Backend |

---

## September 2025 (22 bugs)

AI feature planning and pre-development

| ID | Title | Found Date | Reporter | Resolver | Resolved Date | Severity | Type |
|----|-------|------------|----------|----------|---------------|----------|------|
| BUG-0116 | Enter to send conflicts with newline in chat input | 2025-09-15 | Gu Jing | Yi Yao | 2025-09-18 | Major | Frontend |
| BUG-0117 | Email send status polling frequency too high | 2025-09-26 | Ma Yu | Fan Xinyu | 2025-09-28 | Major | Frontend |
| BUG-0118 | Captcha refresh click has no feedback | 2025-09-27 | Gu Jing | Fan Xinyu | 2025-09-28 | Minor | Frontend |
| BUG-0119 | Checkout page missing loading state on load | 2025-09-02 | Ma Yu | Guo Chao | 2025-09-04 | Critical | Frontend |
| BUG-0120 | Knowledge point tree stutters when fully expanded | 2025-09-02 | Gu Jing | Fan Xinyu | 2025-09-03 | Major | Frontend |
| BUG-0121 | Registration step numbers wrong after skipping verification | 2025-09-10 | Ma Yu | Yi Yao | 2025-09-12 | Major | Frontend |
| BUG-0122 | Email input format validation too loose | 2025-09-21 | Gu Jing | Fan Xinyu | 2025-09-24 | Major | Frontend |
| BUG-0123 | No redirect to login page after JWT token expires | 2025-09-29 | Ma Yu | Fan Xinyu | 2025-10-01 | Minor | Frontend |
| BUG-0124 | Countdown component resets after page navigation | 2025-09-13 | Gu Jing | Yi Yao | 2025-09-15 | Minor | Frontend |
| BUG-0125 | Entitlement service cache has no expiration | 2025-09-28 | Ma Yu | Yi Yao | 2025-10-01 | Minor | Backend |
| BUG-0126 | AI chat history loading missing pagination | 2025-09-04 | Gu Jing | Fan Xinyu | 2025-09-05 | Major | Backend |
| BUG-0127 | Knowledge point data structure causing slow queries | 2025-09-05 | Ma Yu | Fan Xinyu | 2025-09-08 | Minor | Backend |
| BUG-0128 | AI question API response time exceeds 30 seconds | 2025-09-25 | Gu Jing | Fan Xinyu | 2025-09-26 | Major | Backend |
| BUG-0129 | Async email sending failure with no retry | 2025-09-24 | Ma Yu | Yi Yao | 2025-09-27 | Major | Backend |
| BUG-0130 | Knowledge point detail API format change not communicated | 2025-09-08 | Gu Jing | Guo Chao | 2025-09-09 | Major | Backend |
| BUG-0131 | API documentation parameter names do not match implementation | 2025-09-07 | Ma Yu | Guo Chao | 2025-09-10 | Critical | Backend |
| BUG-0132 | Captcha validation case-sensitive | 2025-09-22 | Gu Jing | Guo Chao | 2025-09-24 | Minor | Backend |
| BUG-0133 | Refund API missing amount validation | 2025-09-29 | Ma Yu | Yi Yao | 2025-09-30 | Critical | Backend |
| BUG-0134 | Event listener execution order non-deterministic | 2025-09-20 | Gu Jing | Yi Yao | 2025-09-21 | Minor | Backend |
| BUG-0135 | Email verification POST endpoint missing CSRF protection | 2025-09-28 | Ma Yu | Yi Yao | 2025-09-29 | Critical | Backend |
| BUG-0136 | Question numbers not recalculated after difficulty sort | 2025-09-15 | Gu Jing | Guo Chao | 2025-09-16 | Minor | Backend |
| BUG-0137 | Registration email content missing username | 2025-09-25 | Ma Yu | Yi Yao | 2025-09-26 | Minor | Backend |

---

## October 2025 (28 bugs)

AI learning system core development

| ID | Title | Found Date | Reporter | Resolver | Resolved Date | Severity | Type |
|----|-------|------------|----------|----------|---------------|----------|------|
| BUG-0138 | Payment page currency symbol incorrect | 2025-10-10 | Gu Jing | Fan Xinyu | 2025-10-12 | Major | Frontend |
| BUG-0139 | Search box debounce delay too long | 2025-10-31 | Ma Yu | Fan Xinyu | 2025-11-01 | Major | Frontend |
| BUG-0140 | Dropdown menu position shifts on scroll | 2025-10-20 | Gu Jing | Fan Xinyu | 2025-10-21 | Major | Frontend |
| BUG-0141 | Question numbers displayed non-consecutive | 2025-10-01 | Ma Yu | Guo Chao | 2025-10-03 | Minor | Frontend |
| BUG-0142 | Sensitive information logged to browser console | 2025-10-26 | Gu Jing | Fan Xinyu | 2025-10-28 | Major | Frontend |
| BUG-0143 | Question editor save button has no state feedback | 2025-10-27 | Ma Yu | Fan Xinyu | 2025-10-30 | Major | Frontend |
| BUG-0144 | Message timestamp format inconsistent | 2025-10-11 | Gu Jing | Fan Xinyu | 2025-10-14 | Major | Frontend |
| BUG-0145 | Chart component crashes when data is zero | 2025-10-11 | Ma Yu | Fan Xinyu | 2025-10-12 | Minor | Frontend |
| BUG-0146 | Grade selector missing search | 2025-10-10 | Gu Jing | Fan Xinyu | 2025-10-13 | Major | Frontend |
| BUG-0147 | Form validation error message position unstable | 2025-10-19 | Ma Yu | Guo Chao | 2025-10-20 | Major | Frontend |
| BUG-0148 | Password strength indicator calculation incorrect | 2025-10-26 | Gu Jing | Guo Chao | 2025-10-28 | Minor | Frontend |
| BUG-0149 | Stripe API key accidentally exposed in code commit | 2025-10-07 | Ma Yu | Fan Xinyu | 2025-10-10 | Major | Backend |
| BUG-0150 | Payment security tool false-positive on legitimate requests | 2025-10-25 | Gu Jing | Yi Yao | 2025-10-28 | Minor | Backend |
| BUG-0151 | Knowledge point query API returns unpaginated large payload | 2025-10-31 | Ma Yu | Yi Yao | 2025-11-03 | Minor | Backend |
| BUG-0152 | Test naming inconsistent across languages | 2025-10-27 | Gu Jing | Yi Yao | 2025-10-30 | Minor | Backend |
| BUG-0153 | Combined query returns incorrect results | 2025-10-26 | Ma Yu | Guo Chao | 2025-10-28 | Major | Backend |
| BUG-0154 | Test name change not reflected in historical records | 2025-10-10 | Gu Jing | Yi Yao | 2025-10-11 | Major | Backend |
| BUG-0155 | Registration API missing required field validation | 2025-10-14 | Ma Yu | Yi Yao | 2025-10-16 | Minor | Backend |
| BUG-0156 | JWT secret hardcoded in configuration file | 2025-10-16 | Gu Jing | Yi Yao | 2025-10-19 | Minor | Backend |
| BUG-0157 | Batch answer submission partial failure not rolled back | 2025-10-30 | Ma Yu | Yi Yao | 2025-11-02 | Critical | Backend |
| BUG-0158 | Subscription expiry reminder email not sent 3 days before | 2025-10-31 | Gu Jing | Guo Chao | 2025-11-02 | Minor | Backend |
| BUG-0159 | Teacher-student binding notification not sent | 2025-10-20 | Ma Yu | Fan Xinyu | 2025-10-23 | Major | Backend |
| BUG-0160 | Async email sending failure with no retry | 2025-10-08 | Gu Jing | Guo Chao | 2025-10-11 | Major | Backend |
| BUG-0161 | Question pool scheduled task and event-driven coexistence conflict | 2025-10-20 | Ma Yu | Guo Chao | 2025-10-21 | Minor | Backend |
| BUG-0162 | GoDaddy SMTP authentication failure | 2025-10-21 | Gu Jing | Fan Xinyu | 2025-10-23 | Major | Backend |
| BUG-0163 | Learning plan course ID validation missing | 2025-10-26 | Ma Yu | Yi Yao | 2025-10-28 | Major | Backend |
| BUG-0164 | AI generated content includes non-math information | 2025-10-08 | Gu Jing | Guo Chao | 2025-10-11 | Minor | Backend |
| BUG-0165 | Password change API does not verify old password | 2025-10-23 | Ma Yu | Yi Yao | 2025-10-26 | Major | Backend |

---

## November 2025 (25 bugs)

Question generation logic optimization

| ID | Title | Found Date | Reporter | Resolver | Resolved Date | Severity | Type |
|----|-------|------------|----------|----------|---------------|----------|------|
| BUG-0166 | LaTeX formula render component loads slowly | 2025-11-21 | Gu Jing | Yi Yao | 2025-11-24 | Major | Frontend |
| BUG-0167 | 404 after frontend route refresh | 2025-11-22 | Ma Yu | Fan Xinyu | 2025-11-24 | Major | Frontend |
| BUG-0168 | Registration step numbers wrong after skipping verification | 2025-11-04 | Gu Jing | Fan Xinyu | 2025-11-05 | Minor | Frontend |
| BUG-0169 | Question result page old/new API format conflict | 2025-11-13 | Ma Yu | Fan Xinyu | 2025-11-15 | Major | Frontend |
| BUG-0170 | Route switch loses component state | 2025-11-05 | Gu Jing | Guo Chao | 2025-11-06 | Major | Frontend |
| BUG-0171 | Subscription status page data delayed | 2025-11-18 | Ma Yu | Fan Xinyu | 2025-11-21 | Minor | Frontend |
| BUG-0172 | Payment page currency symbol incorrect | 2025-11-25 | Gu Jing | Yi Yao | 2025-11-28 | Minor | Frontend |
| BUG-0173 | Beneficiary verification ID upload preview rotation wrong | 2025-11-29 | Ma Yu | Guo Chao | 2025-12-01 | Minor | Frontend |
| BUG-0174 | Teacher student list missing sort feature | 2025-11-07 | Gu Jing | Guo Chao | 2025-11-10 | Minor | Frontend |
| BUG-0175 | Course progress ring chart value overflow | 2025-11-26 | Ma Yu | Fan Xinyu | 2025-11-27 | Major | Frontend |
| BUG-0176 | Database status not updated after async email send | 2025-11-16 | Gu Jing | Guo Chao | 2025-11-19 | Minor | Backend |
| BUG-0177 | Verification token expired error page unfriendly | 2025-11-01 | Ma Yu | Fan Xinyu | 2025-11-03 | Major | Backend |
| BUG-0178 | New API endpoints missing permission validation | 2025-11-28 | Gu Jing | Yi Yao | 2025-12-01 | Minor | Backend |
| BUG-0179 | Spring Boot Actuator endpoints lack access control | 2025-11-06 | Ma Yu | Fan Xinyu | 2025-11-08 | Minor | Backend |
| BUG-0180 | Knowledge point detail API format change not communicated | 2025-11-01 | Gu Jing | Yi Yao | 2025-11-04 | Major | Backend |
| BUG-0181 | Scheduled task executed repeatedly | 2025-11-02 | Ma Yu | Fan Xinyu | 2025-11-03 | Major | Backend |
| BUG-0182 | Registration email content missing username | 2025-11-10 | Gu Jing | Guo Chao | 2025-11-13 | Minor | Backend |
| BUG-0183 | Out of memory on large file upload | 2025-11-14 | Ma Yu | Yi Yao | 2025-11-17 | Minor | Backend |
| BUG-0184 | Registration API missing required field validation | 2025-11-18 | Gu Jing | Yi Yao | 2025-11-21 | Major | Backend |
| BUG-0185 | Email verification POST endpoint missing CSRF protection | 2025-11-03 | Ma Yu | Fan Xinyu | 2025-11-06 | Minor | Backend |
| BUG-0186 | Database slow query timeout not configured | 2025-11-05 | Gu Jing | Yi Yao | 2025-11-08 | Minor | Backend |
| BUG-0187 | Historical test detail API N+1 query issue | 2025-11-25 | Ma Yu | Guo Chao | 2025-11-28 | Critical | Backend |
| BUG-0188 | GoDaddy SMTP authentication failure | 2025-11-23 | Gu Jing | Guo Chao | 2025-11-25 | Minor | Backend |
| BUG-0189 | AI course generation affected by knowledge point order | 2025-11-02 | Ma Yu | Yi Yao | 2025-11-03 | Major | Backend |
| BUG-0190 | Event-driven question pool events not persisted | 2025-11-15 | Gu Jing | Guo Chao | 2025-11-18 | Major | Backend |

---

## December 2025 (23 bugs)

Knowledge-point tests and historical records

| ID | Title | Found Date | Reporter | Resolver | Resolved Date | Severity | Type |
|----|-------|------------|----------|----------|---------------|----------|------|
| BUG-0191 | Pagination jump input lacks validation | 2025-12-05 | Ma Yu | Fan Xinyu | 2025-12-08 | Major | Frontend |
| BUG-0192 | Payment retry creates duplicate orders after failure | 2025-12-04 | Gu Jing | Fan Xinyu | 2025-12-07 | Minor | Frontend |
| BUG-0193 | Page requires manual refresh after progress update | 2025-12-12 | Ma Yu | Fan Xinyu | 2025-12-15 | Major | Frontend |
| BUG-0194 | Question generation page missing loading animation | 2025-12-04 | Gu Jing | Guo Chao | 2025-12-06 | Major | Frontend |
| BUG-0195 | Refund form missing reason selection | 2025-12-06 | Ma Yu | Fan Xinyu | 2025-12-09 | Minor | Frontend |
| BUG-0196 | Test generation page step bar navigation broken | 2025-12-31 | Gu Jing | Fan Xinyu | 2026-01-02 | Major | Frontend |
| BUG-0197 | Knowledge point detail breadcrumb navigation wrong | 2025-12-31 | Ma Yu | Fan Xinyu | 2026-01-03 | Minor | Frontend |
| BUG-0198 | Search box debounce delay too long | 2025-12-26 | Gu Jing | Guo Chao | 2025-12-28 | Minor | Frontend |
| BUG-0199 | Static asset path incorrect after frontend build | 2025-12-06 | Ma Yu | Guo Chao | 2025-12-08 | Minor | Frontend |
| BUG-0200 | Learning plan course ID validation missing | 2025-12-05 | Gu Jing | Guo Chao | 2025-12-06 | Minor | Backend |
| BUG-0201 | Production database SSL not enabled | 2025-12-31 | Ma Yu | Yi Yao | 2026-01-03 | Minor | Backend |
| BUG-0202 | Combined query returns incorrect results | 2025-12-31 | Gu Jing | Yi Yao | 2026-01-03 | Minor | Backend |
| BUG-0203 | AI generated multiple-choice correct answer marked wrong | 2025-12-30 | Ma Yu | Yi Yao | 2026-01-02 | Minor | Backend |
| BUG-0204 | Score calculation ignores partial credit questions | 2025-12-30 | Gu Jing | Yi Yao | 2026-01-01 | Minor | Backend |
| BUG-0205 | Student test record statistics aggregation error | 2025-12-08 | Ma Yu | Fan Xinyu | 2025-12-10 | Major | Backend |
| BUG-0206 | Stripe API key accidentally exposed in code commit | 2025-12-29 | Gu Jing | Guo Chao | 2025-12-30 | Minor | Backend |
| BUG-0207 | Payment order created without login prompt | 2025-12-24 | Ma Yu | Yi Yao | 2025-12-26 | Major | Backend |
| BUG-0208 | AI chat stream interruption with no reconnect mechanism | 2025-12-08 | Gu Jing | Yi Yao | 2025-12-11 | Major | Backend |
| BUG-0209 | Course generation knowledge point association incomplete | 2025-12-04 | Ma Yu | Yi Yao | 2025-12-07 | Major | Backend |
| BUG-0210 | Event listener execution order non-deterministic | 2025-12-24 | Gu Jing | Fan Xinyu | 2025-12-26 | Minor | Backend |
| BUG-0211 | Avatar upload returns relative path instead of full URL | 2025-12-16 | Ma Yu | Yi Yao | 2025-12-18 | Major | Backend |
| BUG-0212 | Email queue backlog not processed | 2025-12-27 | Gu Jing | Guo Chao | 2025-12-29 | Minor | Backend |
| BUG-0213 | Chat message encrypted storage failed | 2025-12-25 | Ma Yu | Yi Yao | 2025-12-28 | Major | Backend |

---

## January 2026 (27 bugs)

Course and learning plan system

| ID | Title | Found Date | Reporter | Resolver | Resolved Date | Severity | Type |
|----|-------|------------|----------|----------|---------------|----------|------|
| BUG-0214 | Course detail layout broken on narrow screens | 2026-01-28 | Gu Jing | Fan Xinyu | 2026-01-29 | Minor | Frontend |
| BUG-0215 | Account status not refreshed after renewal success | 2026-01-06 | Ma Yu | Yi Yao | 2026-01-08 | Major | Frontend |
| BUG-0216 | Captcha refresh click has no feedback | 2026-01-09 | Gu Jing | Fan Xinyu | 2026-01-12 | Minor | Frontend |
| BUG-0217 | Event log page timestamp format inconsistent | 2026-01-06 | Ma Yu | Fan Xinyu | 2026-01-09 | Minor | Frontend |
| BUG-0218 | Progress shows blank instead of 0% when zero | 2026-01-08 | Gu Jing | Guo Chao | 2026-01-10 | Minor | Frontend |
| BUG-0219 | No redirect to login page after JWT token expires | 2026-01-14 | Ma Yu | Guo Chao | 2026-01-16 | Minor | Frontend |
| BUG-0220 | Browser cache causes white screen after update | 2026-01-15 | Gu Jing | Fan Xinyu | 2026-01-16 | Minor | Frontend |
| BUG-0221 | Batch import CSV encoding parse error | 2026-01-19 | Ma Yu | Fan Xinyu | 2026-01-22 | Critical | Frontend |
| BUG-0222 | Course list infinite scroll loads duplicate data | 2026-01-29 | Gu Jing | Fan Xinyu | 2026-01-30 | Minor | Frontend |
| BUG-0223 | Checkout page missing loading state on load | 2026-01-13 | Ma Yu | Guo Chao | 2026-01-16 | Critical | Frontend |
| BUG-0224 | Question result PDF export layout broken | 2026-01-28 | Gu Jing | Yi Yao | 2026-01-29 | Major | Frontend |
| BUG-0225 | Subscription expiry reminder email not sent 3 days before | 2026-01-14 | Ma Yu | Fan Xinyu | 2026-01-17 | Minor | Backend |
| BUG-0226 | Registration flow skips verification after email optional change | 2026-01-05 | Gu Jing | Fan Xinyu | 2026-01-07 | Major | Backend |
| BUG-0227 | API description update causes frontend parameter errors | 2026-01-03 | Ma Yu | Yi Yao | 2026-01-05 | Minor | Backend |
| BUG-0228 | Database password stored in plain text in config file | 2026-01-11 | Gu Jing | Yi Yao | 2026-01-14 | Major | Backend |
| BUG-0229 | Scoring system floating-point precision loss | 2026-01-07 | Ma Yu | Guo Chao | 2026-01-08 | Major | Backend |
| BUG-0230 | New request parameters missing type validation | 2026-01-26 | Gu Jing | Yi Yao | 2026-01-27 | Major | Backend |
| BUG-0231 | AI generated short-answer questions missing steps | 2026-01-14 | Ma Yu | Yi Yao | 2026-01-16 | Major | Backend |
| BUG-0232 | Learning progress percentage exceeds 100% | 2026-01-01 | Gu Jing | Guo Chao | 2026-01-02 | Minor | Backend |
| BUG-0233 | Multipart 500MB limit not communicated to users | 2026-01-31 | Ma Yu | Fan Xinyu | 2026-02-01 | Minor | Backend |
| BUG-0234 | GoDaddy SMTP authentication failure | 2026-01-17 | Gu Jing | Yi Yao | 2026-01-18 | Critical | Backend |
| BUG-0235 | Payment customer info out of sync with user info | 2026-01-24 | Ma Yu | Yi Yao | 2026-01-25 | Minor | Backend |
| BUG-0236 | Progress-based question logic ignores knowledge point weights | 2026-01-10 | Gu Jing | Yi Yao | 2026-01-11 | Minor | Backend |
| BUG-0237 | Avatar upload returns relative path instead of full URL | 2026-01-10 | Ma Yu | Yi Yao | 2026-01-11 | Critical | Backend |
| BUG-0238 | Test generation module conflicts with question module | 2026-01-31 | Gu Jing | Guo Chao | 2026-02-02 | Minor | Backend |
| BUG-0239 | Stripe event log table missing cleanup strategy | 2026-01-17 | Ma Yu | Yi Yao | 2026-01-19 | Minor | Backend |
| BUG-0240 | Historical message list returned in wrong order | 2026-01-05 | Gu Jing | Fan Xinyu | 2026-01-07 | Major | Backend |

---

## February 2026 (26 bugs)

User profile and architecture optimization

| ID | Title | Found Date | Reporter | Resolver | Resolved Date | Severity | Type |
|----|-------|------------|----------|----------|---------------|----------|------|
| BUG-0241 | Image lazy load causes first-screen flicker | 2026-02-05 | Ma Yu | Fan Xinyu | 2026-02-06 | Major | Frontend |
| BUG-0242 | Question difficulty label colors not distinct | 2026-02-25 | Gu Jing | Fan Xinyu | 2026-02-26 | Minor | Frontend |
| BUG-0243 | Learning plan progress rolls back after course update | 2026-02-05 | Ma Yu | Fan Xinyu | 2026-02-06 | Major | Frontend |
| BUG-0244 | Payment security block page missing support contact | 2026-02-25 | Gu Jing | Yi Yao | 2026-02-28 | Minor | Frontend |
| BUG-0245 | Subscription cancel confirmation copy unclear | 2026-02-04 | Ma Yu | Fan Xinyu | 2026-02-07 | Minor | Frontend |
| BUG-0246 | Payment success page redirect delay too long | 2026-02-28 | Gu Jing | Fan Xinyu | 2026-03-01 | Minor | Frontend |
| BUG-0247 | Score displays too many decimal places | 2026-02-08 | Ma Yu | Fan Xinyu | 2026-02-11 | Minor | Frontend |
| BUG-0248 | Answer sheet selected state not obvious | 2026-02-10 | Gu Jing | Fan Xinyu | 2026-02-13 | Minor | Frontend |
| BUG-0249 | Question loading progress percentage inaccurate | 2026-02-13 | Ma Yu | Fan Xinyu | 2026-02-15 | Minor | Frontend |
| BUG-0250 | Knowledge point detail breadcrumb navigation wrong | 2026-02-11 | Gu Jing | Yi Yao | 2026-02-14 | Minor | Frontend |
| BUG-0251 | Quiz countdown component precision loss | 2026-02-07 | Ma Yu | Fan Xinyu | 2026-02-10 | Minor | Frontend |
| BUG-0252 | Service crash due to database connection pool exhaustion | 2026-02-15 | Gu Jing | Fan Xinyu | 2026-02-16 | Minor | Backend |
| BUG-0253 | Registration API missing required field validation | 2026-02-21 | Ma Yu | Guo Chao | 2026-02-23 | Minor | Backend |
| BUG-0254 | Stripe test and production environment config mixed | 2026-02-20 | Gu Jing | Guo Chao | 2026-02-21 | Minor | Backend |
| BUG-0255 | AI course generation failure returns no error message | 2026-02-10 | Ma Yu | Yi Yao | 2026-02-12 | Minor | Backend |
| BUG-0256 | GoDaddy SMTP authentication failure | 2026-02-06 | Gu Jing | Guo Chao | 2026-02-07 | Major | Backend |
| BUG-0257 | AI generated short-answer questions missing steps | 2026-02-02 | Ma Yu | Yi Yao | 2026-02-04 | Major | Backend |
| BUG-0258 | Password change API does not verify old password | 2026-02-06 | Gu Jing | Guo Chao | 2026-02-09 | Critical | Backend |
| BUG-0259 | Async email sending failure with no retry | 2026-02-15 | Ma Yu | Fan Xinyu | 2026-02-18 | Critical | Backend |
| BUG-0260 | Batch answer submission partial failure not rolled back | 2026-02-11 | Gu Jing | Yi Yao | 2026-02-14 | Minor | Backend |
| BUG-0261 | Sensitive fields not masked in API responses | 2026-02-20 | Ma Yu | Yi Yao | 2026-02-23 | Minor | Backend |
| BUG-0262 | Teacher-student binding notification not sent | 2026-02-18 | Gu Jing | Yi Yao | 2026-02-20 | Minor | Backend |
| BUG-0263 | Registration flow skips verification after email optional change | 2026-02-17 | Ma Yu | Fan Xinyu | 2026-02-18 | Minor | Backend |
| BUG-0264 | Stripe Webhook signature verification failed | 2026-02-26 | Gu Jing | Yi Yao | 2026-03-01 | Minor | Backend |
| BUG-0265 | Payment customer info out of sync with user info | 2026-02-13 | Ma Yu | Yi Yao | 2026-02-14 | Minor | Backend |
| BUG-0266 | Test naming inconsistent across languages | 2026-02-22 | Gu Jing | Yi Yao | 2026-02-24 | Major | Backend |

---

## March 2026 (24 bugs)

Optimization and email notifications

| ID | Title | Found Date | Reporter | Resolver | Resolved Date | Severity | Type |
|----|-------|------------|----------|----------|---------------|----------|------|
| BUG-0267 | Email resend button cooldown not displayed | 2026-03-08 | Ma Yu | Fan Xinyu | 2026-03-10 | Minor | Frontend |
| BUG-0268 | Quiz list name display truncated | 2026-03-27 | Gu Jing | Fan Xinyu | 2026-03-28 | Minor | Frontend |
| BUG-0269 | Captcha refresh click has no feedback | 2026-03-06 | Ma Yu | Fan Xinyu | 2026-03-09 | Minor | Frontend |
| BUG-0270 | Page title not updated on route change | 2026-03-20 | Gu Jing | Yi Yao | 2026-03-23 | Major | Frontend |
| BUG-0271 | Course card cover image load failure shows no placeholder | 2026-03-05 | Ma Yu | Yi Yao | 2026-03-06 | Minor | Frontend |
| BUG-0272 | Chat history list missing skeleton screen | 2026-03-30 | Gu Jing | Fan Xinyu | 2026-04-02 | Minor | Frontend |
| BUG-0273 | Question numbers displayed non-consecutive | 2026-03-20 | Ma Yu | Yi Yao | 2026-03-21 | Minor | Frontend |
| BUG-0274 | Batch import file size limit not shown | 2026-03-20 | Gu Jing | Fan Xinyu | 2026-03-21 | Minor | Frontend |
| BUG-0275 | Knowledge point category icon load failure | 2026-03-25 | Ma Yu | Yi Yao | 2026-03-26 | Minor | Frontend |
| BUG-0276 | Learning plan calendar week start day incorrect | 2026-03-29 | Gu Jing | Fan Xinyu | 2026-04-01 | Critical | Frontend |
| BUG-0277 | Image URL missing domain prefix | 2026-03-31 | Ma Yu | Guo Chao | 2026-04-03 | Minor | Backend |
| BUG-0278 | Registration email sending blocks main thread | 2026-03-12 | Gu Jing | Guo Chao | 2026-03-13 | Major | Backend |
| BUG-0279 | AI chat concurrent requests causing context confusion | 2026-03-31 | Ma Yu | Guo Chao | 2026-04-02 | Major | Backend |
| BUG-0280 | AI course generation failure returns no error message | 2026-03-19 | Gu Jing | Fan Xinyu | 2026-03-22 | Minor | Backend |
| BUG-0281 | CORS config missing PUT/DELETE methods | 2026-03-19 | Ma Yu | Yi Yao | 2026-03-20 | Major | Backend |
| BUG-0282 | Subscription benefits not effective immediately after creation | 2026-03-25 | Gu Jing | Fan Xinyu | 2026-03-26 | Major | Backend |
| BUG-0283 | 500 errors due to missing global exception handler | 2026-03-20 | Ma Yu | Yi Yao | 2026-03-23 | Major | Backend |
| BUG-0284 | Payment order created without login prompt | 2026-03-23 | Gu Jing | Guo Chao | 2026-03-26 | Minor | Backend |
| BUG-0285 | Email optional validation conflicts with registration flow | 2026-03-04 | Ma Yu | Yi Yao | 2026-03-07 | Minor | Backend |
| BUG-0286 | Learning plan course ID validation missing | 2026-03-23 | Gu Jing | Yi Yao | 2026-03-25 | Major | Backend |
| BUG-0287 | Stripe event log table missing cleanup strategy | 2026-03-14 | Ma Yu | Guo Chao | 2026-03-17 | Major | Backend |
| BUG-0288 | AI chat history not sorted by time | 2026-03-18 | Gu Jing | Guo Chao | 2026-03-20 | Minor | Backend |
| BUG-0289 | Log files not rotated causing disk full | 2026-03-22 | Ma Yu | Guo Chao | 2026-03-24 | Major | Backend |
| BUG-0290 | Service crash due to database connection pool exhaustion | 2026-03-26 | Gu Jing | Guo Chao | 2026-03-29 | Major | Backend |

---

## April 2026 (25 bugs)

Email verification and scoring system

| ID | Title | Found Date | Reporter | Resolver | Resolved Date | Severity | Type |
|----|-------|------------|----------|----------|---------------|----------|------|
| BUG-0291 | Homepage white screen duration too long | 2026-04-18 | Ma Yu | Yi Yao | 2026-04-19 | Major | Frontend |
| BUG-0292 | Knowledge point tree stutters when fully expanded | 2026-04-25 | Gu Jing | Yi Yao | 2026-04-26 | Minor | Frontend |
| BUG-0293 | Question loading progress percentage inaccurate | 2026-04-03 | Ma Yu | Fan Xinyu | 2026-04-06 | Major | Frontend |
| BUG-0294 | Event log page timestamp format inconsistent | 2026-04-08 | Gu Jing | Fan Xinyu | 2026-04-09 | Major | Frontend |
| BUG-0295 | Registration success page does not show email sent hint | 2026-04-05 | Ma Yu | Fan Xinyu | 2026-04-06 | Major | Frontend |
| BUG-0296 | Question page knowledge selector hierarchy too deep | 2026-04-02 | Gu Jing | Guo Chao | 2026-04-05 | Minor | Frontend |
| BUG-0297 | Subscription status page data delayed | 2026-04-28 | Ma Yu | Fan Xinyu | 2026-04-30 | Minor | Frontend |
| BUG-0298 | Short-answer input area too small | 2026-04-30 | Gu Jing | Fan Xinyu | 2026-05-02 | Major | Frontend |
| BUG-0299 | Knowledge point detail breadcrumb navigation wrong | 2026-04-23 | Ma Yu | Yi Yao | 2026-04-26 | Minor | Frontend |
| BUG-0300 | Test list virtual scroll position offset | 2026-04-21 | Gu Jing | Fan Xinyu | 2026-04-24 | Minor | Frontend |
| BUG-0301 | Database status not updated after async email send | 2026-04-17 | Ma Yu | Guo Chao | 2026-04-20 | Major | Backend |
| BUG-0302 | New field addition causes old data query errors | 2026-04-12 | Gu Jing | Guo Chao | 2026-04-14 | Minor | Backend |
| BUG-0303 | Progress calculation exception when denominator is zero | 2026-04-15 | Ma Yu | Yi Yao | 2026-04-17 | Major | Backend |
| BUG-0304 | Mobile adaptation broken after API changes | 2026-04-10 | Gu Jing | Yi Yao | 2026-04-11 | Minor | Backend |
| BUG-0305 | New API endpoints missing permission validation | 2026-04-28 | Ma Yu | Yi Yao | 2026-04-29 | Major | Backend |
| BUG-0306 | Refund API missing amount validation | 2026-04-01 | Gu Jing | Fan Xinyu | 2026-04-04 | Minor | Backend |
| BUG-0307 | AI chat history loading missing pagination | 2026-04-16 | Ma Yu | Yi Yao | 2026-04-18 | Major | Backend |
| BUG-0308 | Beneficiary verification API timeout | 2026-04-10 | Gu Jing | Yi Yao | 2026-04-13 | Critical | Backend |
| BUG-0309 | Knowledge point query API returns unpaginated large payload | 2026-04-12 | Ma Yu | Yi Yao | 2026-04-13 | Minor | Backend |
| BUG-0310 | Verification token expired error page unfriendly | 2026-04-13 | Gu Jing | Yi Yao | 2026-04-14 | Minor | Backend |
| BUG-0311 | Payment order created without login prompt | 2026-04-13 | Ma Yu | Yi Yao | 2026-04-15 | Critical | Backend |
| BUG-0312 | Redis token storage connection pool exhausted | 2026-04-21 | Gu Jing | Guo Chao | 2026-04-22 | Minor | Backend |
| BUG-0313 | Password stored in plain text during user registration | 2026-04-20 | Ma Yu | Guo Chao | 2026-04-23 | Major | Backend |
| BUG-0314 | Parent student query API returns incomplete fields | 2026-04-24 | Gu Jing | Guo Chao | 2026-04-27 | Major | Backend |
| BUG-0315 | Knowledge point table index does not cover common queries | 2026-04-28 | Ma Yu | Yi Yao | 2026-04-29 | Major | Backend |

---

## May 2026 (20 bugs)

Payment system development

| ID | Title | Found Date | Reporter | Resolver | Resolved Date | Severity | Type |
|----|-------|------------|----------|----------|---------------|----------|------|
| BUG-0316 | File upload timeout shows no error message | 2026-05-21 | Gu Jing | Guo Chao | 2026-05-24 | Major | Frontend |
| BUG-0317 | Notification toast blocks action buttons | 2026-05-02 | Ma Yu | Fan Xinyu | 2026-05-04 | Major | Frontend |
| BUG-0318 | Parent-student relationship page loads slowly | 2026-05-17 | Gu Jing | Guo Chao | 2026-05-20 | Critical | Frontend |
| BUG-0319 | Test generation page step bar navigation broken | 2026-05-24 | Ma Yu | Fan Xinyu | 2026-05-26 | Minor | Frontend |
| BUG-0320 | Role selection page layout misaligned | 2026-05-19 | Gu Jing | Yi Yao | 2026-05-22 | Major | Frontend |
| BUG-0321 | Payment security block page missing support contact | 2026-05-26 | Ma Yu | Yi Yao | 2026-05-28 | Major | Frontend |
| BUG-0322 | Page title not updated on route change | 2026-05-16 | Gu Jing | Fan Xinyu | 2026-05-19 | Minor | Frontend |
| BUG-0323 | Question page layout broken after new API adaptation | 2026-05-09 | Ma Yu | Guo Chao | 2026-05-10 | Major | Frontend |
| BUG-0324 | AI generated content includes non-math information | 2026-05-23 | Gu Jing | Guo Chao | 2026-05-24 | Major | Backend |
| BUG-0325 | Event-driven question pool events not persisted | 2026-05-07 | Ma Yu | Yi Yao | 2026-05-09 | Minor | Backend |
| BUG-0326 | No error message when AI chat API times out | 2026-05-18 | Gu Jing | Fan Xinyu | 2026-05-21 | Minor | Backend |
| BUG-0327 | Parent student query API returns incomplete fields | 2026-05-08 | Ma Yu | Yi Yao | 2026-05-09 | Major | Backend |
| BUG-0328 | Email verification POST endpoint missing CSRF protection | 2026-05-22 | Gu Jing | Yi Yao | 2026-05-25 | Minor | Backend |
| BUG-0329 | Legacy course data incompatible after course optimization | 2026-05-14 | Ma Yu | Yi Yao | 2026-05-16 | Major | Backend |
| BUG-0330 | Student test record statistics aggregation error | 2026-05-20 | Gu Jing | Yi Yao | 2026-05-23 | Major | Backend |
| BUG-0331 | AI question result cache causing identical questions | 2026-05-12 | Ma Yu | Guo Chao | 2026-05-15 | Minor | Backend |
| BUG-0332 | Historical test detail API N+1 query issue | 2026-05-20 | Gu Jing | Yi Yao | 2026-05-21 | Minor | Backend |
| BUG-0333 | Multipart 500MB limit not communicated to users | 2026-05-08 | Ma Yu | Guo Chao | 2026-05-09 | Major | Backend |
| BUG-0334 | Progress-based question logic ignores knowledge point weights | 2026-05-24 | Gu Jing | Yi Yao | 2026-05-25 | Critical | Backend |
| BUG-0335 | Grade subject binding data type mismatch | 2026-05-31 | Ma Yu | Fan Xinyu | 2026-06-03 | Minor | Backend |

---

## June 2026 (28 bugs)

Payment integration and security hardening

| ID | Title | Found Date | Reporter | Resolver | Resolved Date | Severity | Type |
|----|-------|------------|----------|----------|---------------|----------|------|
| BUG-0336 | Question loading animation out of sync with actual state | 2026-06-25 | Gu Jing | Guo Chao | 2026-06-27 | Minor | Frontend |
| BUG-0337 | Role selection page layout misaligned | 2026-06-27 | Ma Yu | Fan Xinyu | 2026-06-28 | Minor | Frontend |
| BUG-0338 | Knowledge point detail breadcrumb navigation wrong | 2026-06-03 | Gu Jing | Fan Xinyu | 2026-06-05 | Major | Frontend |
| BUG-0339 | Registration step numbers wrong after skipping verification | 2026-06-28 | Ma Yu | Guo Chao | 2026-06-30 | Major | Frontend |
| BUG-0340 | Submit button lacks duplicate-click prevention | 2026-06-07 | Gu Jing | Guo Chao | 2026-06-10 | Critical | Frontend |
| BUG-0341 | Test generation page step bar navigation broken | 2026-06-19 | Ma Yu | Fan Xinyu | 2026-06-22 | Minor | Frontend |
| BUG-0342 | LaTeX formula render component loads slowly | 2026-06-03 | Gu Jing | Fan Xinyu | 2026-06-05 | Minor | Frontend |
| BUG-0343 | Frontend loading state never clears | 2026-06-26 | Ma Yu | Guo Chao | 2026-06-29 | Minor | Frontend |
| BUG-0344 | Captcha input auto-focus not working | 2026-06-11 | Gu Jing | Fan Xinyu | 2026-06-13 | Minor | Frontend |
| BUG-0345 | Browser cache causes white screen after update | 2026-06-12 | Ma Yu | Fan Xinyu | 2026-06-13 | Major | Frontend |
| BUG-0346 | Learning plan drag sort fails on touch screens | 2026-06-24 | Gu Jing | Fan Xinyu | 2026-06-27 | Minor | Frontend |
| BUG-0347 | New request parameters missing type validation | 2026-06-29 | Ma Yu | Yi Yao | 2026-07-02 | Major | Backend |
| BUG-0348 | AI course generation content missing lesson division | 2026-06-07 | Gu Jing | Yi Yao | 2026-06-08 | Major | Backend |
| BUG-0349 | Parent conversation list query missing permission check | 2026-06-26 | Ma Yu | Guo Chao | 2026-06-27 | Major | Backend |
| BUG-0350 | Test generation API performance degraded after optimization | 2026-06-20 | Gu Jing | Yi Yao | 2026-06-22 | Major | Backend |
| BUG-0351 | Redis cache inconsistent with database | 2026-06-18 | Ma Yu | Yi Yao | 2026-06-21 | Minor | Backend |
| BUG-0352 | Grade subject binding data type mismatch | 2026-06-23 | Gu Jing | Yi Yao | 2026-06-25 | Critical | Backend |
| BUG-0353 | Database backup script not executed | 2026-06-10 | Ma Yu | Guo Chao | 2026-06-13 | Minor | Backend |
| BUG-0354 | Teacher-student binding table missing composite unique index | 2026-06-04 | Gu Jing | Yi Yao | 2026-06-07 | Major | Backend |
| BUG-0355 | Knowledge point query API returns unpaginated large payload | 2026-06-19 | Ma Yu | Fan Xinyu | 2026-06-22 | Minor | Backend |
| BUG-0356 | Refund API missing amount validation | 2026-06-07 | Gu Jing | Guo Chao | 2026-06-09 | Minor | Backend |
| BUG-0357 | User not notified when email sending fails | 2026-06-16 | Ma Yu | Guo Chao | 2026-06-18 | Minor | Backend |
| BUG-0358 | Question generation logic produces duplicate questions | 2026-06-16 | Gu Jing | Guo Chao | 2026-06-17 | Minor | Backend |
| BUG-0359 | Course generation knowledge point association incomplete | 2026-06-18 | Ma Yu | Guo Chao | 2026-06-20 | Minor | Backend |
| BUG-0360 | Null pointer exception when registering with empty email | 2026-06-30 | Gu Jing | Yi Yao | 2026-07-03 | Minor | Backend |
| BUG-0361 | Test record deletion does not clean related answer details | 2026-06-19 | Ma Yu | Guo Chao | 2026-06-22 | Minor | Backend |
| BUG-0362 | User session timeout with no operation feedback | 2026-06-01 | Gu Jing | Guo Chao | 2026-06-02 | Major | Backend |
| BUG-0363 | Question pool scheduled task and event-driven coexistence conflict | 2026-06-30 | Ma Yu | Yi Yao | 2026-07-02 | Major | Backend |

---

## Resolver Statistics

| Resolver | Resolved | Share | Focus Area |
|----------|----------|-------|------------|
| Fan Xinyu | 128 | 35.3% | Frontend UI/UX, interaction, compatibility |
| Yi Yao | 132 | 36.4% | Backend security, database, email |
| Guo Chao | 103 | 28.4% | AI integration, payments, architecture |

## Reporter Statistics

| Reporter | Reported | Share |
|----------|----------|-------|
| Gu Jing | 181 | 49.9% |
| Ma Yu | 182 | 50.1% |

## Type Statistics

| Type | Count | Share |
|------|-------|-------|
| Backend | 217 | 59.8% |
| Frontend | 146 | 40.2% |

## Severity Trend by Quarter

| Quarter | Critical | Major | Minor |
|---------|----------|-------|-------|
| 2025 Q2 | 4 | 32 | 31 |
| 2025 Q3 | 7 | 28 | 35 |
| 2025 Q4 | 2 | 37 | 37 |
| 2026 Q1 | 7 | 23 | 47 |
| 2026 Q2 | 6 | 32 | 35 |
