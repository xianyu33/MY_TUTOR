# MYTutor - Database Architecture

> Last Updated: 2026-06-13
> Database: MySQL 8.0+ | Character Set: utf8mb4 | Engine: InnoDB

## Entity Relationship Overview

```
┌─────────────┐     ┌──────────────────┐     ┌─────────────┐
│    grade     │────<│  knowledge_point  │>────│  category   │
└─────────────┘     └──────────────────┘     └─────────────┘
                           │
                     ┌─────┴──────┐
                     │            │
              ┌──────┴───┐  ┌────┴──────────┐
              │ question  │  │learning_progress│
              └──────┬───┘  └───────────────┘
                     │
              ┌──────┴──────────┐
              │ test_question   │
              └──────┬──────────┘
                     │
┌─────────┐   ┌──────┴──────────┐   ┌───────────────────┐
│  user   │──<│student_test_record│>─│student_test_answer │
│(student)│   └──────────────────┘   └───────────────────┘
└────┬────┘
     │
┌────┴──────────────┐     ┌──────────┐
│guardian_student_rel│>────│  parent  │
└───────────────────┘     └──────────┘

┌────────────────────┐     ┌─────────────────┐
│  payment_product   │────<│  payment_price   │
└────────────────────┘     └────────┬─────────┘
                                    │
┌────────────────────┐     ┌────────┴─────────┐
│ payment_customer   │────<│  payment_order    │
└────────────────────┘     └──────────────────┘
                                    │
                           ┌────────┴─────────┐
                           │payment_subscription│
                           └──────────────────┘
```

---

## Table Summary

### User & Role Management (4 tables)

| Table | Description | Key Fields |
|-------|-------------|------------|
| `user` | Student accounts | user_account, email, email_verified, expire_time, role(S/P/T) |
| `parent` | Parent/Teacher accounts | user_account, type(0=parent, 1=teacher) |
| `guardian_student_rel` | Guardian-Student binding | guardian_id, student_id, relation, start_at, end_at |
| `learning_statistics` | Daily learning stats | user_id, statistics_date, total_study_time, average_score |

### Knowledge System (4 tables)

| Table | Description | Key Fields |
|-------|-------------|------------|
| `grade` | Grade levels (1-12) | grade_name, grade_level |
| `knowledge_category` | Knowledge categories | category_name, category_name_fr, category_code |
| `knowledge_point` | Individual knowledge points | grade_id, category_id, point_name, difficulty_level, prerequisite_points |
| `knowledge_mastery` | Student mastery per knowledge point | user_id, knowledge_point_id, mastery_level, test_count |

### Question & Test System (6 tables)

| Table | Description | Key Fields |
|-------|-------------|------------|
| `question` | Question bank | knowledge_point_id, question_type(1=choice, 2=fill, 3=essay), difficulty_level |
| `test` | Test definitions | grade_id, knowledge_point_ids, total_questions, difficulty_level, test_type |
| `test_question` | Test-Question mapping | test_id, question_id, sort_order, points |
| `student_test_record` | Student test attempts | student_id, test_id, earned_points, score_percentage, test_status |
| `student_test_answer` | Individual answer details | test_record_id, question_id, student_answer, is_correct, earned_points |
| `test_analysis_report` | AI-generated analysis reports | test_record_id, report_type, report_content, file_path |

### Learning Progress (2 tables)

| Table | Description | Key Fields |
|-------|-------------|------------|
| `learning_progress` | Per-knowledge-point progress | user_id, knowledge_point_id, progress_status, completion_percentage |
| `learning_content` | Learning content records | user_id, knowledge_point_id, content_type(1=video, 2=doc, 3=practice, 4=test) |

### Payment System (7 tables)

| Table | Description | Key Fields |
|-------|-------------|------------|
| `payment_product` | Stripe products | stripe_product_id, name, type, status |
| `payment_price` | Stripe prices | stripe_price_id, product_id, amount, currency, recurring_interval |
| `payment_customer` | Stripe customers | stripe_customer_id, user_id, email |
| `payment_order` | Payment orders | order_no, customer_id, product_id, amount, status, stripe_session_id |
| `payment_subscription` | Subscriptions | stripe_subscription_id, customer_id, status, current_period_start/end |
| `payment_refund` | Refund records | stripe_refund_id, order_id, amount, status, reason |
| `stripe_event` | Webhook event log | event_id, event_type, processed, processing_result |

---

## Schema Evolution History

### v1.0 (2025-04) - Initial Schema
- `user` table with basic fields (account, password, role)
- `chat_message` and `chat_message_detail` tables

### v1.1 (2025-07) - Role & Communication
- Added `parent` table for parent/teacher accounts
- Added `guardian_student_rel` for binding relationships
- Added email fields to `user` table

### v2.0 (2025-10) - Knowledge System
- Added `grade`, `knowledge_category`, `knowledge_point` tables
- Added `question` table with difficulty levels
- Added `test`, `test_question`, `student_test_record`, `student_test_answer`
- Added `learning_progress`, `learning_content`, `learning_statistics`
- Added `knowledge_mastery` for tracking mastery levels
- Added `test_analysis_report` for AI-generated reports

### v2.1 (2026-01) - Course System
- Added course-related tables (Course, CourseContent)
- Added study plan tables

### v3.0 (2026-06) - Payment & Security
- Added `payment_product`, `payment_price`, `payment_customer`
- Added `payment_order`, `payment_subscription`, `payment_refund`
- Added `stripe_event` for webhook logging
- Added `email_verified` and `expire_time` to `user` table

---

## Index Strategy

| Table | Index | Purpose |
|-------|-------|---------|
| user | uk_user_account | Unique account lookup |
| user | idx_email | Email verification queries |
| user | idx_role | Role-based filtering |
| knowledge_point | idx_grade_category | Knowledge point lookup by grade+category |
| knowledge_point | idx_kp_difficulty | Difficulty-based filtering |
| student_test_record | idx_student_test | Student test history |
| student_test_record | idx_submit_time | Time-based reporting |
| learning_progress | uk_user_knowledge | Unique progress per user+knowledge point |
| payment_order | idx_customer_id | Customer order lookup |
| payment_subscription | idx_stripe_subscription_id | Stripe webhook matching |

---

## Full SQL Schema

The complete database creation script is maintained at:
`sql/complete_database_schema.sql`
