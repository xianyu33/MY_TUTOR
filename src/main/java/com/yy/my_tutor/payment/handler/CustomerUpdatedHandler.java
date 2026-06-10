package com.yy.my_tutor.payment.handler;

import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.yy.my_tutor.payment.domain.entity.PaymentCustomer;
import com.yy.my_tutor.payment.mapper.PaymentCustomerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class CustomerUpdatedHandler implements EventHandler {

    @Resource private PaymentCustomerMapper customerMapper;

    @Override public String eventType() { return "customer.updated"; }

    @Override
    public HandlerResult handle(Event event) {
        Customer remote = (Customer) event.getDataObjectDeserializer().getObject().orElse(null);
        if (remote == null) return HandlerResult.SKIPPED;
        PaymentCustomer local = customerMapper.selectByStripeId(remote.getId());
        if (local == null) return HandlerResult.SKIPPED;
        String defaultPm = remote.getInvoiceSettings() == null ? null : remote.getInvoiceSettings().getDefaultPaymentMethod();
        if (defaultPm != null && !defaultPm.equals(local.getDefaultPaymentMethod())) {
            local.setDefaultPaymentMethod(defaultPm);
            customerMapper.updateById(local);
            log.info("Customer {} default_payment_method → {}", remote.getId(), defaultPm);
        }
        return HandlerResult.SUCCESS;
    }
}
