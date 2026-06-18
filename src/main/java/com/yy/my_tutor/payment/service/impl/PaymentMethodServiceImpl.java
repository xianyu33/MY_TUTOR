package com.yy.my_tutor.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.stripe.model.SetupIntent;
import com.yy.my_tutor.payment.domain.dto.PaymentMethodDTO;
import com.yy.my_tutor.payment.domain.dto.SetupIntentResponse;
import com.yy.my_tutor.payment.domain.entity.PaymentCustomer;
import com.yy.my_tutor.payment.domain.entity.PaymentUserPaymentMethod;
import com.yy.my_tutor.payment.mapper.PaymentCustomerMapper;
import com.yy.my_tutor.payment.mapper.PaymentMethodMapper;
import com.yy.my_tutor.payment.service.CustomerService;
import com.yy.my_tutor.payment.service.PaymentMethodService;
import com.yy.my_tutor.payment.service.StripeClientService;
import com.yy.my_tutor.payment.util.PaymentException;
import com.yy.my_tutor.payment.util.PaymentUserRoleUtil;
import com.yy.my_tutor.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    @Resource private PaymentMethodMapper paymentMethodMapper;
    @Resource private PaymentCustomerMapper customerMapper;
    @Resource private StripeClientService stripeClient;
    @Resource private CustomerService customerService;

    public PaymentMethodServiceImpl() {
    }

    PaymentMethodServiceImpl(PaymentMethodMapper paymentMethodMapper,
                             PaymentCustomerMapper customerMapper,
                             StripeClientService stripeClient) {
        this.paymentMethodMapper = paymentMethodMapper;
        this.customerMapper = customerMapper;
        this.stripeClient = stripeClient;
    }

    @Override
    public SetupIntentResponse createSetupIntent(Integer payerUserId) {
        User payer = new User();
        payer.setId(payerUserId);
        payer.setRole("S");
        return createSetupIntent(payer);
    }

    @Override
    public SetupIntentResponse createSetupIntent(User payer) {
        PaymentCustomer customer = customerService.getOrCreate(payer);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("payer_user_id", String.valueOf(payer.getId()));
        metadata.put("payer_role", PaymentUserRoleUtil.roleOf(payer));
        try {
            SetupIntent setupIntent = stripeClient.createSetupIntent(customer.getStripeCustomerId(), metadata);
            return new SetupIntentResponse(setupIntent.getId(), setupIntent.getClientSecret());
        } catch (StripeException e) {
            throw PaymentException.of("PAYMENT_STRIPE_UNAVAILABLE", e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentUserPaymentMethod syncAttachedPaymentMethod(PaymentMethod pm, String setupIntentId) throws StripeException {
        if (pm == null || pm.getId() == null || pm.getCustomer() == null) {
            throw PaymentException.of("PAYMENT_METHOD_INVALID");
        }
        PaymentCustomer customer = customerMapper.selectByStripeId(pm.getCustomer());
        if (customer == null) {
            throw PaymentException.of("PAYMENT_CUSTOMER_NOT_FOUND");
        }

        PaymentUserPaymentMethod local = paymentMethodMapper.selectByStripePaymentMethodId(pm.getId());
        boolean inserting = local == null;
        if (local == null) {
            local = new PaymentUserPaymentMethod();
            local.setUserId(customer.getUserId());
            local.setStripeCustomerId(customer.getStripeCustomerId());
            local.setStripePaymentMethodId(pm.getId());
        }
        local.setUserId(customer.getUserId());
        local.setUserRole(customer.getUserRole());
        local.setStripeCustomerId(customer.getStripeCustomerId());
        local.setType(pm.getType() == null ? "card" : pm.getType());
        local.setSetupIntentId(setupIntentId);
        local.setStatus("ACTIVE");
        local.setDeleteFlag("N");
        applyCard(local, pm.getCard());

        boolean shouldDefault = customer.getDefaultPaymentMethod() == null
                || customer.getDefaultPaymentMethod().isEmpty()
                || paymentMethodMapper.countActiveByUserIdAndRole(customer.getUserId(), customer.getUserRole()) == 0;
        local.setIsDefault(shouldDefault ? 1 : (local.getIsDefault() == null ? 0 : local.getIsDefault()));

        if (inserting) {
            paymentMethodMapper.insert(local);
        } else {
            paymentMethodMapper.updateById(local);
        }

        if (shouldDefault) {
            setDefaultInternal(customer, local);
        }
        return local;
    }

    @Override
    public List<PaymentMethodDTO> listMy(Integer payerUserId) {
        User payer = new User();
        payer.setId(payerUserId);
        payer.setRole("S");
        return listMy(payer);
    }

    @Override
    public List<PaymentMethodDTO> listMy(User payer) {
        QueryWrapper<PaymentUserPaymentMethod> qw = new QueryWrapper<>();
        qw.eq("user_id", payer.getId())
                .eq("user_role", PaymentUserRoleUtil.roleOf(payer))
                .eq("delete_flag", "N")
                .eq("status", "ACTIVE");
        qw.orderByDesc("is_default").orderByDesc("create_at");
        return paymentMethodMapper.selectList(qw).stream().map(PaymentMethodDTO::of).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentMethodDTO setDefault(Integer payerUserId, Integer methodId) {
        User payer = new User();
        payer.setId(payerUserId);
        payer.setRole("S");
        return setDefault(payer, methodId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentMethodDTO setDefault(User payer, Integer methodId) {
        PaymentUserPaymentMethod method = mustOwnActive(payer, methodId);
        String userRole = PaymentUserRoleUtil.roleOf(payer);
        PaymentCustomer customer = customerMapper.selectByUserIdAndRole(payer.getId(), userRole);
        if (customer == null) throw PaymentException.of("PAYMENT_CUSTOMER_NOT_FOUND");
        try {
            setDefaultInternal(customer, method);
            return PaymentMethodDTO.of(method);
        } catch (StripeException e) {
            throw PaymentException.of("PAYMENT_STRIPE_UNAVAILABLE", e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void detach(Integer payerUserId, Integer methodId) {
        User payer = new User();
        payer.setId(payerUserId);
        payer.setRole("S");
        detach(payer, methodId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void detach(User payer, Integer methodId) {
        PaymentUserPaymentMethod method = mustOwnActive(payer, methodId);
        try {
            stripeClient.detachPaymentMethod(method.getStripePaymentMethodId());
        } catch (StripeException e) {
            throw PaymentException.of("PAYMENT_STRIPE_UNAVAILABLE", e.getMessage());
        }
        method.setStatus("DETACHED");
        method.setIsDefault(0);
        paymentMethodMapper.updateById(method);

        PaymentCustomer customer = customerMapper.selectByUserIdAndRole(payer.getId(), PaymentUserRoleUtil.roleOf(payer));
        if (customer != null && method.getStripePaymentMethodId().equals(customer.getDefaultPaymentMethod())) {
            customer.setDefaultPaymentMethod(null);
            customerMapper.updateById(customer);
        }
    }

    @Override
    public PaymentUserPaymentMethod defaultMethod(Integer payerUserId) {
        User payer = new User();
        payer.setId(payerUserId);
        payer.setRole("S");
        return defaultMethod(payer);
    }

    @Override
    public PaymentUserPaymentMethod defaultMethod(User payer) {
        PaymentUserPaymentMethod method = paymentMethodMapper.selectDefaultByUserIdAndRole(
                payer.getId(), PaymentUserRoleUtil.roleOf(payer));
        if (method == null) throw PaymentException.of("PAYMENT_METHOD_REQUIRED", "请先绑定默认银行卡");
        return method;
    }

    private void setDefaultInternal(PaymentCustomer customer, PaymentUserPaymentMethod method) throws StripeException {
        stripeClient.updateCustomerDefaultPaymentMethod(customer.getStripeCustomerId(), method.getStripePaymentMethodId());
        paymentMethodMapper.clearDefaultForUserAndRole(customer.getUserId(), customer.getUserRole());
        method.setIsDefault(1);
        paymentMethodMapper.updateById(method);
        customer.setDefaultPaymentMethod(method.getStripePaymentMethodId());
        customerMapper.updateById(customer);
    }

    private PaymentUserPaymentMethod mustOwnActive(User payer, Integer methodId) {
        PaymentUserPaymentMethod method = paymentMethodMapper.selectById(methodId);
        if (method == null || !payer.getId().equals(method.getUserId())
                || !PaymentUserRoleUtil.roleOf(payer).equals(method.getUserRole())
                || !"ACTIVE".equals(method.getStatus())
                || !"N".equals(method.getDeleteFlag())) {
            throw PaymentException.of("PAYMENT_METHOD_NOT_FOUND");
        }
        return method;
    }

    private void applyCard(PaymentUserPaymentMethod local, PaymentMethod.Card card) {
        if (card == null) return;
        local.setBrand(card.getBrand());
        local.setLast4(card.getLast4());
        local.setExpMonth(card.getExpMonth() == null ? null : card.getExpMonth().intValue());
        local.setExpYear(card.getExpYear() == null ? null : card.getExpYear().intValue());
        local.setCountry(card.getCountry());
        local.setFunding(card.getFunding());
    }
}
