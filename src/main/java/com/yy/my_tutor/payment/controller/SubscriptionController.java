package com.yy.my_tutor.payment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.payment.domain.dto.OrderDetailDTO;
import com.yy.my_tutor.payment.domain.dto.OrderListItemDTO;
import com.yy.my_tutor.payment.domain.dto.SubscriptionDTO;
import com.yy.my_tutor.payment.service.OrderService;
import com.yy.my_tutor.payment.service.SubscriptionService;
import com.yy.my_tutor.payment.util.PaymentSecurityUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class SubscriptionController {

    @Resource private SubscriptionService subscriptionService;
    @Resource private OrderService orderService;
    @Resource private PaymentSecurityUtil securityUtil;

    @GetMapping("/api/payment/subscriptions")
    public RespResult<List<SubscriptionDTO>> listMy(@RequestParam(required = false) String status,
                                                    @RequestParam(required = false) Integer beneficiaryStudentId) {
        Integer uid = securityUtil.currentUserId();
        return RespResult.data(subscriptionService.listMy(uid, status, beneficiaryStudentId));
    }

    @GetMapping("/api/payment/subscriptions/{id}")
    public RespResult<SubscriptionDTO> getDetail(@PathVariable Integer id) {
        Integer uid = securityUtil.currentUserId();
        return RespResult.data(subscriptionService.getDetail(id, uid));
    }

    @PostMapping("/api/payment/subscriptions/{id}/cancel")
    public RespResult<SubscriptionDTO> cancel(@PathVariable Integer id) {
        Integer uid = securityUtil.currentUserId();
        return RespResult.data(subscriptionService.cancelAtPeriodEnd(id, uid));
    }

    @PostMapping("/api/payment/subscriptions/{id}/resume")
    public RespResult<SubscriptionDTO> resume(@PathVariable Integer id) {
        Integer uid = securityUtil.currentUserId();
        return RespResult.data(subscriptionService.resume(id, uid));
    }

    @PostMapping("/api/payment/subscriptions/{id}/payment-method")
    public RespResult<SubscriptionDTO> applyPM(@PathVariable Integer id, @RequestBody Map<String,String> body) {
        Integer uid = securityUtil.currentUserId();
        return RespResult.data(subscriptionService.applyPaymentMethod(id, uid, body.get("paymentMethodId")));
    }

    @GetMapping("/api/payment/orders")
    public RespResult<IPage<OrderListItemDTO>> myOrders(@RequestParam(required = false) String status,
                                                        @RequestParam(required = false) Integer beneficiaryStudentId,
                                                        @RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "20") int size) {
        Integer uid = securityUtil.currentUserId();
        return RespResult.data(orderService.listMyOrders(uid, status, beneficiaryStudentId, page, size));
    }

    @GetMapping("/api/payment/orders/{orderNo}")
    public RespResult<OrderDetailDTO> orderDetail(@PathVariable String orderNo) {
        Integer uid = securityUtil.currentUserId();
        return RespResult.data(orderService.getMyOrderDetail(orderNo, uid));
    }

    @PostMapping("/api/admin/payment/subscriptions/{id}/cancel-now")
    public RespResult<SubscriptionDTO> cancelNow(@PathVariable Integer id) {
        securityUtil.requireAdmin();
        Integer uid = securityUtil.currentUserId();
        return RespResult.data(subscriptionService.cancelNow(id, uid));
    }

    @GetMapping("/api/admin/payment/orders")
    public RespResult<IPage<OrderListItemDTO>> adminOrders(@RequestParam(required = false) String status,
                                                           @RequestParam(required = false) Integer payerUserId,
                                                           @RequestParam(required = false) Integer beneficiaryStudentId,
                                                           @RequestParam(defaultValue = "1") int page,
                                                           @RequestParam(defaultValue = "20") int size) {
        securityUtil.requireAdmin();
        return RespResult.data(orderService.listAllOrders(status, payerUserId, beneficiaryStudentId, page, size));
    }
}
