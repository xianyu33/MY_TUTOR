package com.yy.my_tutor.payment.service;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.Refund;
import com.stripe.model.SetupIntent;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;

import java.util.Map;

public interface StripeClientService {

    // ----- Customer -----
    Customer createCustomer(String email, Integer userId, String name) throws StripeException;
    Customer retrieveCustomer(String stripeCustomerId) throws StripeException;
    Customer updateCustomerDefaultPaymentMethod(String stripeCustomerId, String paymentMethodId) throws StripeException;

    // ----- Product -----
    Product createProduct(String name, String description, Map<String, String> metadata) throws StripeException;
    Product updateProduct(String stripeProductId, Boolean active, String name) throws StripeException;

    // ----- Price -----
    /**
     * @param billingInterval null = 一次性;"month"/"year" 等 = 周期
     * @param intervalCount 仅周期 Price 有意义,Stripe quarter = month + count=3
     */
    Price createPrice(String stripeProductId, String currency, Long unitAmount,
                      String billingInterval, Integer intervalCount,
                      Map<String, String> metadata) throws StripeException;
    Price updatePriceActive(String stripePriceId, boolean active) throws StripeException;

    // ----- Checkout Session -----
    /**
     * @param mode "payment" 或 "subscription"
     */
    Session createCheckoutSession(String stripeCustomerId,
                                  String stripePriceId,
                                  String mode,
                                  String successUrl,
                                  String cancelUrl,
                                  Long expiresAtEpochSeconds,
                                  Map<String, String> metadata) throws StripeException;

    Session retrieveCheckoutSession(String stripeSessionId) throws StripeException;

    /** 创建 setup 模式 Session(用户换卡) */
    Session createSetupSession(String stripeCustomerId, String returnUrl,
                               Map<String, String> metadata) throws StripeException;

    // ----- Payment Method / SetupIntent -----
    SetupIntent createSetupIntent(String stripeCustomerId, Map<String, String> metadata) throws StripeException;
    SetupIntent retrieveSetupIntent(String setupIntentId) throws StripeException;
    PaymentMethod retrievePaymentMethod(String paymentMethodId) throws StripeException;
    PaymentMethod detachPaymentMethod(String paymentMethodId) throws StripeException;

    // ----- Direct payment -----
    PaymentIntent createAndConfirmPaymentIntent(String stripeCustomerId, String paymentMethodId,
                                                Long amount, String currency,
                                                Map<String, String> metadata) throws StripeException;
    Subscription createSubscription(String stripeCustomerId, String paymentMethodId,
                                    String stripePriceId, Map<String, String> metadata) throws StripeException;

    // ----- Subscription -----
    Subscription retrieveSubscription(String stripeSubId) throws StripeException;
    Subscription cancelSubscriptionAtPeriodEnd(String stripeSubId, boolean cancelAtPeriodEnd) throws StripeException;
    Subscription cancelSubscriptionImmediately(String stripeSubId) throws StripeException;
    Subscription updateSubscriptionDefaultPaymentMethod(String stripeSubId, String paymentMethodId) throws StripeException;

    // ----- Refund -----
    /**
     * @param amount null = 全额退款;否则按 amount 部分退款(单位与 charge.amount 一致)
     */
    Refund createRefund(String paymentIntentId, Long amount, String reason,
                        Map<String, String> metadata) throws StripeException;

    // ----- Webhook -----
    Event constructWebhookEvent(String rawBody, String signatureHeader, String webhookSecret)
            throws SignatureVerificationException;
}
