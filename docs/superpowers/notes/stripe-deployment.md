# Stripe 部署配置 checklist

## 环境变量(test / live 分别配置)

- `STRIPE_MODE`: `test` 或 `live`
- `STRIPE_API_KEY`: Stripe Dashboard → Developers → API keys 复制 Secret key
- `STRIPE_WEBHOOK_SECRET`: Stripe Dashboard → Developers → Webhooks → 选择 endpoint → Signing secret
- `STRIPE_ADMIN_USER_IDS`: 后台管理员 user_id,逗号分隔(如 `1,2`)

## Stripe Dashboard 设置

### test mode

1. Developers → Webhooks → Add endpoint
2. URL: `https://api.mytutor.top/api/payment/stripe/webhook`(本地开发用 `stripe listen` 转发)
3. 勾选事件(共 13 个):
   - checkout.session.completed
   - checkout.session.expired
   - payment_intent.payment_failed
   - customer.subscription.created
   - customer.subscription.updated
   - customer.subscription.deleted
   - invoice.created
   - invoice.paid
   - invoice.payment_failed
   - charge.refunded
   - customer.updated
   - payment_method.attached
   - product.updated / price.updated / price.deleted
4. 复制 Signing secret → 部署平台环境变量

### live mode

重复上述步骤,使用 live mode 资源(隔离 endpoint、key、商品)。

## 数据库 DDL

部署前必须先在目标 MySQL 跑一次 DDL 建表:

```bash
mysql -h <host> -P <port> -u root -p<password> tutor < sql/payment_schema.sql
```

7 张表:`payment_customer`、`payment_product`、`payment_price`、`payment_order`、`payment_subscription`、`payment_refund`、`stripe_event`。

## 上线前验证

```bash
# 启动 Stripe CLI 转发(本地)
stripe listen --forward-to localhost:9009/api/payment/stripe/webhook

# 触发测试事件
stripe trigger checkout.session.completed
stripe trigger invoice.paid
stripe trigger charge.refunded

# 检查事件落表
mysql -h <host> -P <port> -u root -p<password> tutor -e \
"SELECT stripe_event_id, event_type, process_status FROM stripe_event ORDER BY id DESC LIMIT 10;"
```

## 商品初始化

后台用 admin 用户登录,POST `/api/admin/payment/products` 创建初始商品。每个商品的所有 Price(N 币种 × M 周期)在同一事务中创建。

## 已知约束

1. **本地 JDK 必须 8**:本机默认 JDK 17 与 Lombok 不兼容,运行 mvn 命令前必须 `export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)`。
2. **stripe-java 27.1.2 依赖 Gson 2.10.1**:pom.xml 已显式 override(Spring Boot 2.4.4 BOM 默认 2.8.6,会缺 `ReflectionAccessFilter` 类导致 NoClassDefFoundError)。
3. **`/api/**` 全部 permitAll**:项目现状,支付接口在 controller 层手动 `securityUtil.currentUserId()` 校验 JWT;后台接口额外 `securityUtil.requireAdmin()`(白名单从 `STRIPE_ADMIN_USER_IDS` 注入)。

## 监控指标(待接入项目监控栈)

- `stripe_event` WHERE process_status=2(失败事件数)
- `payment_order` WHERE status='PENDING' AND create_at < NOW() - INTERVAL 30 MINUTE(堆积订单)
- `payment_subscription` WHERE status='past_due'(欠费订阅数)

## 端到端验收清单

- [ ] 学生本人付费购买课程 → `payment_order.status=PAID` → `EntitlementService.canAccessCourse(studentId, courseId)` 返回 true
- [ ] 家长付费给学生购买课程 → 学生获权,家长不获权
- [ ] 卡被拒(`4000 0000 0000 9995`)→ `FAILED`
- [ ] 24h 未支付 → `EXPIRED`
- [ ] 后台全额退款 → `REFUNDED` + 权益失效
- [ ] 后台部分退款 → `PARTIALLY_REFUNDED`,权益不变(本设计默认)
- [ ] 首期月付订阅 → `payment_subscription.status='active'`、`current_period_end` 正确
- [ ] `stripe trigger invoice.paid` 模拟续费 → 新增 `PAID` 订单 + 周期推进
- [ ] 用户周期末取消 → `cancel_at_period_end=1`,仍 active;Test Clock 推进 → deleted + 失效
- [ ] 续费失败 → `past_due` + `hasActiveSubscription` false
- [ ] 后台强制立即取消 → `canceled` + 权益立失
- [ ] Stripe Dashboard "Resend" 同事件两次 → `stripe_event` 仅一行成功,handler 只跑一次
- [ ] 错误签名 → 400 不入库
- [ ] handler 故意抛异常 → `process_status=2`,Stripe 重试后修复
