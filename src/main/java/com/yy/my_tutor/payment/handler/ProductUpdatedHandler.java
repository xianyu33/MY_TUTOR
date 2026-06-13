package com.yy.my_tutor.payment.handler;

import com.stripe.model.Event;
import com.stripe.model.Product;
import com.yy.my_tutor.payment.domain.entity.PaymentProduct;
import com.yy.my_tutor.payment.mapper.PaymentProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class ProductUpdatedHandler implements EventHandler {

    @Resource private PaymentProductMapper productMapper;

    @Override public String eventType() { return "product.updated"; }

    @Override
    public HandlerResult handle(Event event) {
        Product remote = EventObjectExtractor.get(event, Product.class);
        if (remote == null) return HandlerResult.SKIPPED;
        PaymentProduct local = productMapper.selectByStripeId(remote.getId());
        if (local == null) return HandlerResult.SKIPPED;
        boolean dirty = false;
        Integer newStatus = Boolean.TRUE.equals(remote.getActive()) ? 1 : 0;
        if (!newStatus.equals(local.getStatus())) { local.setStatus(newStatus); dirty = true; }
        if (remote.getName() != null && !remote.getName().equals(local.getName())) {
            local.setName(remote.getName()); dirty = true;
        }
        if (dirty) {
            local.setUpdateBy("STRIPE_WEBHOOK");
            productMapper.updateById(local);
            log.info("Product {} synced from Stripe", remote.getId());
        }
        return HandlerResult.SUCCESS;
    }
}
