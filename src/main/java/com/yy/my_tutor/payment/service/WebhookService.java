package com.yy.my_tutor.payment.service;

public interface WebhookService {
    /**
     * 处理 Stripe 推送(已通过签名校验后由 Controller 调用):
     *   1. INSERT IGNORE 入 stripe_event 表;若冲突直接 SKIPPED
     *   2. 调度对应 handler
     *   3. 写回 process_status / last_error / processed_at
     * @return true=成功(包括跳过);false=失败,需要 Controller 返回 5xx 让 Stripe 重试
     */
    boolean process(String rawBody, String signatureHeader);
}
