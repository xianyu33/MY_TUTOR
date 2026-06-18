package com.yy.my_tutor.payment.service.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.yy.my_tutor.payment.config.StripeConfig;
import com.yy.my_tutor.payment.domain.entity.PaymentCustomer;
import com.yy.my_tutor.payment.mapper.PaymentCustomerMapper;
import com.yy.my_tutor.payment.service.CustomerService;
import com.yy.my_tutor.payment.service.StripeClientService;
import com.yy.my_tutor.payment.util.PaymentException;
import com.yy.my_tutor.payment.util.PaymentUserRoleUtil;
import com.yy.my_tutor.user.domain.User;
import com.yy.my_tutor.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource private PaymentCustomerMapper customerMapper;
    @Resource private StripeClientService stripeClient;
    @Resource private UserMapper userMapper;
    @Resource private StripeConfig stripeConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentCustomer getOrCreate(Integer userId) {
        User user = userMapper.findById(userId);
        if (user == null && !stripeConfig.isLocalAuthBypassEnabled()) {
            throw PaymentException.of("PAYMENT_USER_NOT_FOUND");
        }
        if (user == null) {
            user = new User();
            user.setId(userId);
            user.setRole("S");
            user.setEmail(stripeConfig.getLocalAuthBypass().getCustomerEmail());
            user.setUsername(stripeConfig.getLocalAuthBypass().getCustomerName());
        }
        return getOrCreate(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentCustomer getOrCreate(User user) {
        if (user == null || user.getId() == null) {
            throw PaymentException.of("PAYMENT_USER_NOT_FOUND");
        }
        String userRole = PaymentUserRoleUtil.roleOf(user);
        PaymentCustomer existing = customerMapper.selectByUserIdAndRole(user.getId(), userRole);
        if (existing != null) return existing;

        String email = user.getEmail();
        String name = user.getUsername();

        Customer stripeCustomer;
        try {
            stripeCustomer = stripeClient.createCustomer(email, user.getId(), name);
        } catch (StripeException e) {
            log.error("Stripe createCustomer failed for userId {} role {}", user.getId(), userRole, e);
            throw PaymentException.of("PAYMENT_STRIPE_UNAVAILABLE", e.getMessage());
        }

        PaymentCustomer c = new PaymentCustomer();
        c.setUserId(user.getId());
        c.setUserRole(userRole);
        c.setStripeCustomerId(stripeCustomer.getId());
        c.setEmail(email);
        c.setCreateBy(String.valueOf(user.getId()));
        c.setUpdateBy(String.valueOf(user.getId()));
        customerMapper.insert(c);
        return c;
    }
}
