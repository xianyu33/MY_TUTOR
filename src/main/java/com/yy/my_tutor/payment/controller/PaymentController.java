package com.yy.my_tutor.payment.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.payment.domain.dto.CheckoutSessionResponse;
import com.yy.my_tutor.payment.domain.dto.CreateCheckoutRequest;
import com.yy.my_tutor.payment.domain.entity.PaymentOrder;
import com.yy.my_tutor.payment.service.CheckoutService;
import com.yy.my_tutor.payment.util.PaymentSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Resource private CheckoutService checkoutService;
    @Resource private PaymentSecurityUtil securityUtil;

    @PostMapping("/checkout")
    public RespResult<CheckoutSessionResponse> createCheckout(@RequestBody CreateCheckoutRequest req) {
        Integer payerUserId = securityUtil.currentUserId();
        return RespResult.data(checkoutService.createSession(req, payerUserId));
    }

    @GetMapping("/orders/{orderNo}/status")
    public RespResult<Map<String, Object>> getOrderStatus(@PathVariable String orderNo) {
        Integer payerUserId = securityUtil.currentUserId();
        PaymentOrder order = checkoutService.getOrderStatus(orderNo, payerUserId);
        Map<String, Object> result = new HashMap<>();
        result.put("status", order.getStatus());
        result.put("paidAt", order.getPaidAt());
        result.put("failureReason", order.getFailureReason());
        return RespResult.data(result);
    }

    @PostMapping("/payment-methods/setup-session")
    public RespResult<CheckoutSessionResponse> createSetupSession(@RequestBody Map<String, String> body) {
        Integer payerUserId = securityUtil.currentUserId();
        String returnUrl = body.get("returnUrl");
        return RespResult.data(checkoutService.createSetupSession(payerUserId, returnUrl));
    }
}
