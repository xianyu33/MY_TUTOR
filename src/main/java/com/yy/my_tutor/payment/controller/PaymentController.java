package com.yy.my_tutor.payment.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.payment.domain.dto.AnnualLicenseQuoteDTO;
import com.yy.my_tutor.payment.domain.dto.AnnualLicenseSeatUsageDTO;
import com.yy.my_tutor.payment.domain.dto.CheckoutSessionResponse;
import com.yy.my_tutor.payment.domain.dto.CreateCheckoutRequest;
import com.yy.my_tutor.payment.domain.dto.DirectPaymentResponse;
import com.yy.my_tutor.payment.domain.dto.PaymentMethodDTO;
import com.yy.my_tutor.payment.domain.dto.SetupIntentResponse;
import com.yy.my_tutor.payment.domain.entity.PaymentOrder;
import com.yy.my_tutor.payment.service.AnnualLicenseService;
import com.yy.my_tutor.payment.service.DirectPaymentService;
import com.yy.my_tutor.payment.service.CheckoutService;
import com.yy.my_tutor.payment.service.PaymentMethodService;
import com.yy.my_tutor.payment.util.PaymentSecurityUtil;
import com.yy.my_tutor.user.domain.User;
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
    @Resource private AnnualLicenseService annualLicenseService;
    @Resource private PaymentMethodService paymentMethodService;
    @Resource private PaymentSecurityUtil securityUtil;

    @PostMapping("/checkout")
    public RespResult<CheckoutSessionResponse> createCheckout(@RequestBody CreateCheckoutRequest req) {
        User payer = securityUtil.currentUser();
        return RespResult.data(checkoutService.createSession(req, payer));
    }

    @PostMapping("/direct-pay")
    public RespResult<DirectPaymentResponse> directPay(@RequestBody CreateCheckoutRequest req) {
        User payer = securityUtil.currentUser();
        if (annualLicenseService.supports(req)) {
            return RespResult.data(annualLicenseService.pay(req, payer));
        }
        return RespResult.data(directPaymentService.pay(req, payer));
    }

    @GetMapping("/annual-license/quote")
    public RespResult<AnnualLicenseQuoteDTO> annualLicenseQuote(@RequestParam Integer productId,
                                                                @RequestParam Integer quantity) {
        return RespResult.data(annualLicenseService.quote(productId, quantity));
    }

    @GetMapping("/annual-license/seat-usage")
    public RespResult<AnnualLicenseSeatUsageDTO> annualLicenseSeatUsage() {
        User teacher = securityUtil.currentUser();
        return RespResult.data(annualLicenseService.seatUsage(teacher));
    }

    @GetMapping("/orders/{orderNo}/status")
    public RespResult<Map<String, Object>> getOrderStatus(@PathVariable String orderNo) {
        User payer = securityUtil.currentUser();
        PaymentOrder order = checkoutService.getOrderStatus(orderNo, payer);
        Map<String, Object> result = new HashMap<>();
        result.put("status", order.getStatus());
        result.put("paidAt", order.getPaidAt());
        result.put("failureReason", order.getFailureReason());
        return RespResult.data(result);
    }

    @PostMapping("/payment-methods/setup-session")
    public RespResult<CheckoutSessionResponse> createSetupSession(@RequestBody Map<String, String> body) {
        User payer = securityUtil.currentUser();
        String returnUrl = body.get("returnUrl");
        return RespResult.data(checkoutService.createSetupSession(payer, returnUrl));
    }

    @PostMapping("/payment-methods/setup-intent")
    public RespResult<SetupIntentResponse> createSetupIntent() {
        User payer = securityUtil.currentUser();
        return RespResult.data(paymentMethodService.createSetupIntent(payer));
    }

    @GetMapping("/payment-methods")
    public RespResult<List<PaymentMethodDTO>> listPaymentMethods() {
        User payer = securityUtil.currentUser();
        return RespResult.data(paymentMethodService.listMy(payer));
    }

    @PostMapping("/payment-methods/{id}/default")
    public RespResult<PaymentMethodDTO> setDefaultPaymentMethod(@PathVariable Integer id) {
        User payer = securityUtil.currentUser();
        return RespResult.data(paymentMethodService.setDefault(payer, id));
    }

    @DeleteMapping("/payment-methods/{id}")
    public RespResult<Void> detachPaymentMethod(@PathVariable Integer id) {
        User payer = securityUtil.currentUser();
        paymentMethodService.detach(payer, id);
        return RespResult.data(null);
    }
}
