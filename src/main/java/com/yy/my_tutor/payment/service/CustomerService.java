package com.yy.my_tutor.payment.service;

import com.yy.my_tutor.payment.domain.entity.PaymentCustomer;

public interface CustomerService {
    /** 取或懒创建当前 user 的 Stripe Customer(整个支付流的入口) */
    PaymentCustomer getOrCreate(Integer userId);
}
