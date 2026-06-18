package com.yy.my_tutor.payment.service;

import com.yy.my_tutor.payment.domain.entity.PaymentCustomer;
import com.yy.my_tutor.user.domain.User;

public interface CustomerService {
    /** 取或懒创建当前 user 的 Stripe Customer(整个支付流的入口) */
    PaymentCustomer getOrCreate(Integer userId);

    /** 取或懒创建当前付款主体的 Stripe Customer,使用 id + role 区分学生和老师。 */
    PaymentCustomer getOrCreate(User user);
}
