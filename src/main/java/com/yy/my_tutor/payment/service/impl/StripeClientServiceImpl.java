package com.yy.my_tutor.payment.service.impl;

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
import com.stripe.net.Webhook;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodDetachParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.PriceUpdateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.ProductUpdateParams;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.SetupIntentCreateParams;
import com.stripe.param.SubscriptionCancelParams;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.SubscriptionUpdateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.yy.my_tutor.payment.service.StripeClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class StripeClientServiceImpl implements StripeClientService {

    @Override
    public Customer createCustomer(String email, Integer userId, String name) throws StripeException {
        CustomerCreateParams.Builder b = CustomerCreateParams.builder()
                .putMetadata("local_user_id", String.valueOf(userId));
        if (email != null) b.setEmail(email);
        if (name != null) b.setName(name);
        return Customer.create(b.build());
    }

    @Override
    public Customer retrieveCustomer(String stripeCustomerId) throws StripeException {
        return Customer.retrieve(stripeCustomerId);
    }

    @Override
    public Customer updateCustomerDefaultPaymentMethod(String stripeCustomerId, String paymentMethodId) throws StripeException {
        CustomerUpdateParams params = CustomerUpdateParams.builder()
                .setInvoiceSettings(CustomerUpdateParams.InvoiceSettings.builder()
                        .setDefaultPaymentMethod(paymentMethodId)
                        .build())
                .build();
        return Customer.retrieve(stripeCustomerId).update(params);
    }

    @Override
    public Product createProduct(String name, String description, Map<String, String> metadata) throws StripeException {
        ProductCreateParams.Builder b = ProductCreateParams.builder().setName(name);
        if (description != null) b.setDescription(description);
        if (metadata != null) metadata.forEach(b::putMetadata);
        return Product.create(b.build());
    }

    @Override
    public Product updateProduct(String stripeProductId, Boolean active, String name) throws StripeException {
        ProductUpdateParams.Builder b = ProductUpdateParams.builder();
        if (active != null) b.setActive(active);
        if (name != null) b.setName(name);
        return Product.retrieve(stripeProductId).update(b.build());
    }

    @Override
    public Price createPrice(String stripeProductId, String currency, Long unitAmount,
                             String billingInterval, Integer intervalCount,
                             Map<String, String> metadata) throws StripeException {
        PriceCreateParams.Builder b = PriceCreateParams.builder()
                .setProduct(stripeProductId)
                .setCurrency(currency)
                .setUnitAmount(unitAmount);
        if (billingInterval != null) {
            PriceCreateParams.Recurring.Interval interval =
                    PriceCreateParams.Recurring.Interval.valueOf(billingInterval.toUpperCase());
            PriceCreateParams.Recurring.Builder rb = PriceCreateParams.Recurring.builder()
                    .setInterval(interval);
            if (intervalCount != null) rb.setIntervalCount(Long.valueOf(intervalCount));
            b.setRecurring(rb.build());
        }
        if (metadata != null) metadata.forEach(b::putMetadata);
        return Price.create(b.build());
    }

    @Override
    public Price updatePriceActive(String stripePriceId, boolean active) throws StripeException {
        PriceUpdateParams params = PriceUpdateParams.builder().setActive(active).build();
        return Price.retrieve(stripePriceId).update(params);
    }

    @Override
    public Session createCheckoutSession(String stripeCustomerId,
                                         String stripePriceId,
                                         String mode,
                                         String successUrl,
                                         String cancelUrl,
                                         Long expiresAtEpochSeconds,
                                         Map<String, String> metadata) throws StripeException {
        SessionCreateParams.Mode sessionMode = "subscription".equals(mode)
                ? SessionCreateParams.Mode.SUBSCRIPTION
                : SessionCreateParams.Mode.PAYMENT;

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setPrice(stripePriceId)
                .setQuantity(1L)
                .build();

        SessionCreateParams.Builder b = SessionCreateParams.builder()
                .setMode(sessionMode)
                .setCustomer(stripeCustomerId)
                .addLineItem(lineItem)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl);

        if (expiresAtEpochSeconds != null) b.setExpiresAt(expiresAtEpochSeconds);
        if (metadata != null) {
            metadata.forEach(b::putMetadata);
            // subscription 模式下,metadata 还要透传到 Subscription 对象,便于 Webhook 中定位
            if (sessionMode == SessionCreateParams.Mode.SUBSCRIPTION) {
                SessionCreateParams.SubscriptionData.Builder sdb = SessionCreateParams.SubscriptionData.builder();
                metadata.forEach(sdb::putMetadata);
                b.setSubscriptionData(sdb.build());
            }
        }
        return Session.create(b.build());
    }

    @Override
    public Session retrieveCheckoutSession(String stripeSessionId) throws StripeException {
        return Session.retrieve(stripeSessionId);
    }

    @Override
    public Session createSetupSession(String stripeCustomerId, String returnUrl,
                                      Map<String, String> metadata) throws StripeException {
        SessionCreateParams.Builder b = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.SETUP)
                .setCustomer(stripeCustomerId)
                .setSuccessUrl(returnUrl)
                .setCancelUrl(returnUrl);
        if (metadata != null) metadata.forEach(b::putMetadata);
        return Session.create(b.build());
    }

    @Override
    public SetupIntent createSetupIntent(String stripeCustomerId, Map<String, String> metadata) throws StripeException {
        SetupIntentCreateParams.Builder b = SetupIntentCreateParams.builder()
                .setCustomer(stripeCustomerId)
                .setUsage(SetupIntentCreateParams.Usage.OFF_SESSION)
                .addPaymentMethodType("card");
        if (metadata != null) metadata.forEach(b::putMetadata);
        return SetupIntent.create(b.build());
    }

    @Override
    public SetupIntent retrieveSetupIntent(String setupIntentId) throws StripeException {
        return SetupIntent.retrieve(setupIntentId);
    }

    @Override
    public PaymentMethod retrievePaymentMethod(String paymentMethodId) throws StripeException {
        return PaymentMethod.retrieve(paymentMethodId);
    }

    @Override
    public PaymentMethod detachPaymentMethod(String paymentMethodId) throws StripeException {
        return PaymentMethod.retrieve(paymentMethodId).detach(PaymentMethodDetachParams.builder().build());
    }

    @Override
    public PaymentIntent createAndConfirmPaymentIntent(String stripeCustomerId, String paymentMethodId,
                                                       Long amount, String currency,
                                                       Map<String, String> metadata) throws StripeException {
        PaymentIntentCreateParams.Builder b = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
                .setCustomer(stripeCustomerId)
                .setPaymentMethod(paymentMethodId)
                .setConfirm(true)
                .setOffSession(false)
                .addPaymentMethodType("card");
        if (metadata != null) metadata.forEach(b::putMetadata);
        return PaymentIntent.create(b.build());
    }

    @Override
    public Subscription createSubscription(String stripeCustomerId, String paymentMethodId,
                                           String stripePriceId, Map<String, String> metadata) throws StripeException {
        SubscriptionCreateParams.Item item = SubscriptionCreateParams.Item.builder()
                .setPrice(stripePriceId)
                .build();
        SubscriptionCreateParams.Builder b = SubscriptionCreateParams.builder()
                .setCustomer(stripeCustomerId)
                .setDefaultPaymentMethod(paymentMethodId)
                .addItem(item)
                .setPaymentBehavior(SubscriptionCreateParams.PaymentBehavior.DEFAULT_INCOMPLETE)
                .addExpand("latest_invoice.payment_intent");
        if (metadata != null) metadata.forEach(b::putMetadata);
        return Subscription.create(b.build());
    }

    @Override
    public Subscription retrieveSubscription(String stripeSubId) throws StripeException {
        return Subscription.retrieve(stripeSubId);
    }

    @Override
    public Subscription cancelSubscriptionAtPeriodEnd(String stripeSubId, boolean cancelAtPeriodEnd) throws StripeException {
        SubscriptionUpdateParams params = SubscriptionUpdateParams.builder()
                .setCancelAtPeriodEnd(cancelAtPeriodEnd)
                .build();
        return Subscription.retrieve(stripeSubId).update(params);
    }

    @Override
    public Subscription cancelSubscriptionImmediately(String stripeSubId) throws StripeException {
        return Subscription.retrieve(stripeSubId).cancel(SubscriptionCancelParams.builder().build());
    }

    @Override
    public Subscription updateSubscriptionDefaultPaymentMethod(String stripeSubId, String paymentMethodId) throws StripeException {
        SubscriptionUpdateParams params = SubscriptionUpdateParams.builder()
                .setDefaultPaymentMethod(paymentMethodId)
                .build();
        return Subscription.retrieve(stripeSubId).update(params);
    }
    @Override
    public Refund createRefund(String paymentIntentId, Long amount, String reason,
                               Map<String, String> metadata) throws StripeException {
        RefundCreateParams.Builder b = RefundCreateParams.builder()
                .setPaymentIntent(paymentIntentId);
        if (amount != null) b.setAmount(amount);
        if (reason != null) {
            try {
                b.setReason(RefundCreateParams.Reason.valueOf(reason.toUpperCase()));
            } catch (IllegalArgumentException e) {
                log.warn("Unknown refund reason {}, will be omitted", reason);
            }
        }
        if (metadata != null) metadata.forEach(b::putMetadata);
        return Refund.create(b.build());
    }
    @Override
    public Event constructWebhookEvent(String rawBody, String signatureHeader, String webhookSecret)
            throws SignatureVerificationException {
        return Webhook.constructEvent(rawBody, signatureHeader, webhookSecret);
    }
}
