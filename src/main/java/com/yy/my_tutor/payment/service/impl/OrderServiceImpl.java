package com.yy.my_tutor.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yy.my_tutor.payment.domain.dto.OrderDetailDTO;
import com.yy.my_tutor.payment.domain.dto.OrderListItemDTO;
import com.yy.my_tutor.payment.domain.entity.PaymentOrder;
import com.yy.my_tutor.payment.domain.entity.PaymentProduct;
import com.yy.my_tutor.payment.domain.entity.PaymentRefund;
import com.yy.my_tutor.payment.mapper.PaymentOrderMapper;
import com.yy.my_tutor.payment.mapper.PaymentProductMapper;
import com.yy.my_tutor.payment.mapper.PaymentRefundMapper;
import com.yy.my_tutor.payment.service.OrderService;
import com.yy.my_tutor.payment.util.PaymentException;
import com.yy.my_tutor.payment.util.PaymentUserRoleUtil;
import com.yy.my_tutor.user.domain.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource private PaymentOrderMapper orderMapper;
    @Resource private PaymentProductMapper productMapper;
    @Resource private PaymentRefundMapper refundMapper;

    @Override
    public IPage<OrderListItemDTO> listMyOrders(Integer payerUserId, String status, Integer beneficiaryStudentId,
                                                int page, int size) {
        QueryWrapper<PaymentOrder> qw = baseQuery(status, payerUserId, beneficiaryStudentId);
        return queryPage(qw, page, size);
    }

    @Override
    public IPage<OrderListItemDTO> listMyOrders(User payer, String status, Integer beneficiaryStudentId,
                                                int page, int size) {
        QueryWrapper<PaymentOrder> qw = baseQuery(status, payer.getId(), beneficiaryStudentId);
        qw.eq("payer_role", PaymentUserRoleUtil.roleOf(payer));
        return queryPage(qw, page, size);
    }

    @Override
    public OrderDetailDTO getMyOrderDetail(String orderNo, Integer payerUserId) {
        PaymentOrder order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) throw PaymentException.of("PAYMENT_ORDER_NOT_FOUND");
        if (!payerUserId.equals(order.getPayerUserId())) {
            throw PaymentException.of("PAYMENT_ORDER_NOT_OWNED");
        }
        OrderDetailDTO d = new OrderDetailDTO();
        d.setOrder(order);
        PaymentProduct product = productMapper.selectById(order.getProductId());
        if (product != null) d.setProductName(product.getName());
        List<PaymentRefund> refunds = refundMapper.selectList(new QueryWrapper<PaymentRefund>()
                .eq("order_id", order.getId()).eq("delete_flag", "N"));
        d.setRefunds(refunds);
        return d;
    }

    @Override
    public OrderDetailDTO getMyOrderDetail(String orderNo, User payer) {
        PaymentOrder order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) throw PaymentException.of("PAYMENT_ORDER_NOT_FOUND");
        if (!payer.getId().equals(order.getPayerUserId())
                || !PaymentUserRoleUtil.roleOf(payer).equals(order.getPayerRole())) {
            throw PaymentException.of("PAYMENT_ORDER_NOT_OWNED");
        }
        OrderDetailDTO d = new OrderDetailDTO();
        d.setOrder(order);
        PaymentProduct product = productMapper.selectById(order.getProductId());
        if (product != null) d.setProductName(product.getName());
        List<PaymentRefund> refunds = refundMapper.selectList(new QueryWrapper<PaymentRefund>()
                .eq("order_id", order.getId()).eq("delete_flag", "N"));
        d.setRefunds(refunds);
        return d;
    }

    @Override
    public IPage<OrderListItemDTO> listAllOrders(String status, Integer payerUserId, Integer beneficiaryStudentId,
                                                 int page, int size) {
        QueryWrapper<PaymentOrder> qw = baseQuery(status, payerUserId, beneficiaryStudentId);
        return queryPage(qw, page, size);
    }

    private QueryWrapper<PaymentOrder> baseQuery(String status, Integer payerUserId, Integer beneficiaryStudentId) {
        QueryWrapper<PaymentOrder> qw = new QueryWrapper<>();
        qw.eq("delete_flag", "N");
        if (payerUserId != null) qw.eq("payer_user_id", payerUserId);
        if (beneficiaryStudentId != null) qw.eq("beneficiary_student_id", beneficiaryStudentId);
        if (status != null && !status.isEmpty()) qw.eq("status", status);
        qw.orderByDesc("create_at");
        return qw;
    }

    private IPage<OrderListItemDTO> queryPage(QueryWrapper<PaymentOrder> qw, int page, int size) {
        IPage<PaymentOrder> p = orderMapper.selectPage(new Page<>(page, size), qw);
        Map<Integer, String> productNames = new HashMap<>();
        for (PaymentOrder o : p.getRecords()) {
            productNames.computeIfAbsent(o.getProductId(), id -> {
                PaymentProduct prod = productMapper.selectById(id);
                return prod == null ? null : prod.getName();
            });
        }
        Page<OrderListItemDTO> result = new Page<>(p.getCurrent(), p.getSize(), p.getTotal());
        result.setRecords(p.getRecords().stream()
                .map(o -> OrderListItemDTO.from(o, productNames.get(o.getProductId())))
                .collect(Collectors.toList()));
        return result;
    }
}
