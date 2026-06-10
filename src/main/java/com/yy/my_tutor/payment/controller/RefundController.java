package com.yy.my_tutor.payment.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.payment.domain.dto.RefundRequest;
import com.yy.my_tutor.payment.domain.entity.PaymentRefund;
import com.yy.my_tutor.payment.service.RefundService;
import com.yy.my_tutor.payment.util.PaymentSecurityUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class RefundController {

    @Resource private RefundService refundService;
    @Resource private PaymentSecurityUtil securityUtil;

    @PostMapping("/api/admin/payment/orders/{orderNo}/refunds")
    public RespResult<PaymentRefund> refund(@PathVariable String orderNo, @RequestBody RefundRequest req) {
        securityUtil.requireAdmin();
        Integer uid = securityUtil.currentUserId();
        return RespResult.data(refundService.refundOrder(orderNo, req, uid));
    }
}
