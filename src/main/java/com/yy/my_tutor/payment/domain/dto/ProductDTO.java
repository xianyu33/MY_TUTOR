package com.yy.my_tutor.payment.domain.dto;

import com.yy.my_tutor.payment.domain.entity.PaymentPrice;
import com.yy.my_tutor.payment.domain.entity.PaymentProduct;
import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {
    private Integer id;
    private String stripeProductId;
    private String productType;
    private Integer targetRefId;
    private String name;
    private String nameZh;
    private String nameFr;
    private String description;
    private Integer status;
    private List<PaymentPrice> prices;

    public static ProductDTO from(PaymentProduct p, List<PaymentPrice> prices) {
        ProductDTO d = new ProductDTO();
        d.id = p.getId();
        d.stripeProductId = p.getStripeProductId();
        d.productType = p.getProductType();
        d.targetRefId = p.getTargetRefId();
        d.name = p.getName();
        d.nameZh = p.getNameZh();
        d.nameFr = p.getNameFr();
        d.description = p.getDescription();
        d.status = p.getStatus();
        d.prices = prices;
        return d;
    }
}
