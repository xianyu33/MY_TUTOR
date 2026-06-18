-- ========================================
-- Stripe 支付集成数据库 schema
-- 设计文档: docs/superpowers/specs/2026-05-25-stripe-payment-design.md
-- 字段惯例完全沿用项目 schema_init.sql 风格
-- ========================================

-- 表 1: Stripe 客户映射(一个 user 对应一个 Stripe Customer)
CREATE TABLE IF NOT EXISTS payment_customer (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  user_id INT NOT NULL COMMENT '本地 user.id(付款人)',
  user_role VARCHAR(8) NOT NULL DEFAULT 'S' COMMENT '付款主体角色:S/T/P',
  stripe_customer_id VARCHAR(64) NOT NULL COMMENT 'Stripe cus_xxx',
  email VARCHAR(120) COMMENT '付款邮箱(冗余,便于排查)',
  default_payment_method VARCHAR(64) COMMENT '默认支付方式 pm_xxx',
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  create_by VARCHAR(50),
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  update_by VARCHAR(50),
  delete_flag CHAR(1) DEFAULT 'N',
  UNIQUE KEY uk_user_role (user_id, user_role),
  UNIQUE KEY uk_stripe_customer_id (stripe_customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Stripe 客户映射';

-- 表 1.1: 用户绑定的支付方式
CREATE TABLE IF NOT EXISTS payment_method (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  user_role VARCHAR(8) NOT NULL DEFAULT 'S' COMMENT '付款主体角色:S/T/P',
  stripe_customer_id VARCHAR(64) NOT NULL,
  stripe_payment_method_id VARCHAR(64) NOT NULL,
  type VARCHAR(32) NOT NULL DEFAULT 'card',
  brand VARCHAR(32),
  last4 VARCHAR(8),
  exp_month INT,
  exp_year INT,
  country VARCHAR(8),
  funding VARCHAR(32),
  status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
  is_default TINYINT DEFAULT 0,
  setup_intent_id VARCHAR(128),
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  delete_flag CHAR(1) DEFAULT 'N',
  UNIQUE KEY uk_stripe_pm (stripe_payment_method_id),
  INDEX idx_user_status (user_id, status),
  INDEX idx_user_role_status (user_id, user_role, status),
  INDEX idx_customer (stripe_customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户支付方式';

-- 表 2: 商品(订阅+一次性统一抽象)
CREATE TABLE IF NOT EXISTS payment_product (
  id INT PRIMARY KEY AUTO_INCREMENT,
  stripe_product_id VARCHAR(64) NOT NULL COMMENT 'Stripe prod_xxx',
  product_type VARCHAR(32) NOT NULL COMMENT 'SUBSCRIPTION_PLAN/COURSE/KNOWLEDGE_PACK/TEST_PACK/ADDON',
  target_ref_id INT COMMENT '业务实体 ID,订阅型为 NULL',
  name VARCHAR(120) NOT NULL,
  name_zh VARCHAR(120),
  name_fr VARCHAR(120),
  description TEXT,
  status TINYINT DEFAULT 1 COMMENT '1-上架,0-下架',
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  create_by VARCHAR(50),
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  update_by VARCHAR(50),
  delete_flag CHAR(1) DEFAULT 'N',
  UNIQUE KEY uk_stripe_product_id (stripe_product_id),
  INDEX idx_type_target (product_type, target_ref_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付商品';

-- 表 3: 价格(一商品 × 多币种 × 多周期)
CREATE TABLE IF NOT EXISTS payment_price (
  id INT PRIMARY KEY AUTO_INCREMENT,
  product_id INT NOT NULL,
  stripe_price_id VARCHAR(64) NOT NULL,
  currency CHAR(3) NOT NULL COMMENT 'ISO-4217 三字母小写',
  unit_amount BIGINT NOT NULL COMMENT '最小货币单位金额',
  billing_interval VARCHAR(16) COMMENT 'month/quarter/year;一次性为 NULL',
  interval_count INT DEFAULT 1,
  status TINYINT DEFAULT 1,
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  create_by VARCHAR(50),
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  update_by VARCHAR(50),
  delete_flag CHAR(1) DEFAULT 'N',
  UNIQUE KEY uk_stripe_price_id (stripe_price_id),
  INDEX idx_product_currency (product_id, currency, billing_interval)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品价格';

-- 表 4: 订单(一次性 + 订阅每期发票)
CREATE TABLE IF NOT EXISTS payment_order (
  id INT PRIMARY KEY AUTO_INCREMENT,
  order_no VARCHAR(32) NOT NULL,
  payer_user_id INT NOT NULL,
  payer_role VARCHAR(8) NOT NULL DEFAULT 'S' COMMENT '付款主体角色:S/T/P',
  beneficiary_student_id INT COMMENT '订阅订单为 NULL,一次性课程购买必填',
  product_id INT NOT NULL,
  price_id INT NOT NULL,
  price_tier_id INT COMMENT '命中的阶梯价格ID',
  quantity INT NOT NULL DEFAULT 1 COMMENT '购买数量',
  unit_amount BIGINT COMMENT '成交单价,最小货币单位',
  currency CHAR(3) NOT NULL,
  amount BIGINT NOT NULL,
  status VARCHAR(32) NOT NULL COMMENT 'PENDING/PAID/FAILED/REFUNDED/PARTIALLY_REFUNDED/EXPIRED',
  stripe_checkout_session_id VARCHAR(128),
  stripe_payment_intent_id VARCHAR(128),
  stripe_payment_method_id VARCHAR(64),
  stripe_invoice_id VARCHAR(128) COMMENT '订阅生成的发票;一次性为 NULL',
  subscription_id INT,
  paid_at DATETIME,
  expire_at DATETIME,
  failure_reason VARCHAR(255),
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  create_by VARCHAR(50),
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  update_by VARCHAR(50),
  delete_flag CHAR(1) DEFAULT 'N',
  UNIQUE KEY uk_order_no (order_no),
  UNIQUE KEY uk_stripe_session (stripe_checkout_session_id),
  UNIQUE KEY uk_stripe_invoice (stripe_invoice_id),
  INDEX idx_payer (payer_user_id, status),
  INDEX idx_payer_role (payer_user_id, payer_role, status),
  INDEX idx_beneficiary (beneficiary_student_id),
  INDEX idx_subscription (subscription_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单';

-- 表 4.1: 价格阶梯(年度授权等数量折扣场景)
CREATE TABLE IF NOT EXISTS payment_price_tier (
  id INT PRIMARY KEY AUTO_INCREMENT,
  price_id INT NOT NULL,
  min_quantity INT NOT NULL,
  max_quantity INT NULL,
  unit_amount BIGINT NOT NULL COMMENT '该阶梯单价,最小货币单位',
  status TINYINT DEFAULT 1,
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  create_by VARCHAR(50),
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  update_by VARCHAR(50),
  delete_flag CHAR(1) DEFAULT 'N',
  INDEX idx_price_quantity (price_id, min_quantity, max_quantity),
  INDEX idx_price_status (price_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付价格阶梯';

-- 表 4.2: 老师年度授权名额账本
CREATE TABLE IF NOT EXISTS teacher_seat_ledger (
  id INT PRIMARY KEY AUTO_INCREMENT,
  teacher_id INT NOT NULL COMMENT '老师ID(parent.id)',
  order_id INT NULL COMMENT '购买订单ID',
  student_id INT NULL COMMENT '激活学生ID',
  change_count INT NOT NULL COMMENT '购买为正数,激活为-1',
  type VARCHAR(32) NOT NULL COMMENT 'PURCHASE/ACTIVATE',
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  create_by VARCHAR(50),
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  update_by VARCHAR(50),
  delete_flag CHAR(1) DEFAULT 'N',
  INDEX idx_teacher (teacher_id, delete_flag),
  INDEX idx_teacher_student (teacher_id, student_id, type, delete_flag),
  INDEX idx_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='老师年度授权名额账本';

-- 表 5: 订阅状态镜像
CREATE TABLE IF NOT EXISTS payment_subscription (
  id INT PRIMARY KEY AUTO_INCREMENT,
  stripe_subscription_id VARCHAR(64) NOT NULL,
  stripe_payment_method_id VARCHAR(64),
  payer_user_id INT NOT NULL,
  beneficiary_student_id INT NOT NULL,
  product_id INT NOT NULL,
  price_id INT NOT NULL,
  currency CHAR(3) NOT NULL,
  status VARCHAR(32) NOT NULL,
  current_period_start DATETIME NOT NULL,
  current_period_end DATETIME NOT NULL,
  cancel_at_period_end TINYINT DEFAULT 0,
  canceled_at DATETIME,
  ended_at DATETIME,
  latest_invoice_id VARCHAR(128),
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  create_by VARCHAR(50),
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  update_by VARCHAR(50),
  delete_flag CHAR(1) DEFAULT 'N',
  UNIQUE KEY uk_stripe_sub_id (stripe_subscription_id),
  INDEX idx_payer (payer_user_id, status),
  INDEX idx_beneficiary (beneficiary_student_id, status),
  INDEX idx_period_end (current_period_end)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订阅状态镜像';

-- 表 6: 退款记录
CREATE TABLE IF NOT EXISTS payment_refund (
  id INT PRIMARY KEY AUTO_INCREMENT,
  order_id INT NOT NULL,
  stripe_refund_id VARCHAR(64) NOT NULL,
  amount BIGINT NOT NULL,
  currency CHAR(3) NOT NULL,
  reason VARCHAR(64),
  reason_detail VARCHAR(500),
  status VARCHAR(32) NOT NULL,
  operator_user_id INT NOT NULL,
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  create_by VARCHAR(50),
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  update_by VARCHAR(50),
  delete_flag CHAR(1) DEFAULT 'N',
  UNIQUE KEY uk_stripe_refund_id (stripe_refund_id),
  INDEX idx_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退款记录';

-- 表 7: Stripe Webhook 事件存档(幂等 + 审计)
CREATE TABLE IF NOT EXISTS stripe_event (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  stripe_event_id VARCHAR(64) NOT NULL,
  event_type VARCHAR(64) NOT NULL,
  api_version VARCHAR(32),
  payload LONGTEXT NOT NULL,
  process_status TINYINT DEFAULT 0 COMMENT '0-pending,1-success,2-failed,3-skipped',
  retry_count INT DEFAULT 0,
  last_error VARCHAR(1000),
  received_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  processed_at DATETIME,
  UNIQUE KEY uk_stripe_event_id (stripe_event_id),
  INDEX idx_type_status (event_type, process_status),
  INDEX idx_received (received_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Stripe Webhook 事件存档';
