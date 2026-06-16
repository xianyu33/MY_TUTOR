package com.yy.my_tutor.payment.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.payment.domain.dto.CheckoutSessionResponse;
import com.yy.my_tutor.payment.domain.dto.CreateCheckoutRequest;
import com.yy.my_tutor.payment.domain.dto.DirectPaymentResponse;
import com.yy.my_tutor.payment.domain.dto.PaymentMethodDTO;
import com.yy.my_tutor.payment.domain.dto.SetupIntentResponse;
import com.yy.my_tutor.payment.domain.entity.PaymentOrder;
import com.yy.my_tutor.payment.service.DirectPaymentService;
import com.yy.my_tutor.payment.service.CheckoutService;
import com.yy.my_tutor.payment.service.PaymentMethodService;
import com.yy.my_tutor.payment.util.PaymentSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Resource private CheckoutService checkoutService;
    @Resource private DirectPaymentService directPaymentService;
    @Resource private PaymentMethodService paymentMethodService;
    @Resource private PaymentSecurityUtil securityUtil;

    @PostMapping("/checkout")
    public RespResult<CheckoutSessionResponse> createCheckout(@RequestBody CreateCheckoutRequest req) {
        Integer payerUserId = securityUtil.currentUserId();
        return RespResult.data(checkoutService.createSession(req, payerUserId));
    }

    @PostMapping("/direct-pay")
    public RespResult<DirectPaymentResponse> directPay(@RequestBody CreateCheckoutRequest req) {
        Integer payerUserId = securityUtil.currentUserId();
        return RespResult.data(directPaymentService.pay(req, payerUserId));
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

    @PostMapping("/payment-methods/setup-intent")
    public RespResult<SetupIntentResponse> createSetupIntent() {
        Integer payerUserId = securityUtil.currentUserId();
        return RespResult.data(paymentMethodService.createSetupIntent(payerUserId));
    }

    @GetMapping("/payment-methods")
    public RespResult<List<PaymentMethodDTO>> listPaymentMethods() {
        Integer payerUserId = securityUtil.currentUserId();
        return RespResult.data(paymentMethodService.listMy(payerUserId));
    }

    @PostMapping("/payment-methods/{id}/default")
    public RespResult<PaymentMethodDTO> setDefaultPaymentMethod(@PathVariable Integer id) {
        Integer payerUserId = securityUtil.currentUserId();
        return RespResult.data(paymentMethodService.setDefault(payerUserId, id));
    }

    @DeleteMapping("/payment-methods/{id}")
    public RespResult<Void> detachPaymentMethod(@PathVariable Integer id) {
        Integer payerUserId = securityUtil.currentUserId();
        paymentMethodService.detach(payerUserId, id);
        return RespResult.data(null);
    }
}
