package com.yy.my_tutor.payment.domain.dto;

import com.yy.my_tutor.payment.domain.entity.PaymentUserPaymentMethod;
import lombok.Data;

@Data
public class PaymentMethodDTO {
    private Integer id;
    private String brand;
    private String last4;
    private Integer expMonth;
    private Integer expYear;
    private String status;
    private Integer isDefault;

    public static PaymentMethodDTO of(PaymentUserPaymentMethod method) {
        if (method == null) return null;
        PaymentMethodDTO dto = new PaymentMethodDTO();
        dto.id = method.getId();
        dto.brand = method.getBrand();
        dto.last4 = method.getLast4();
        dto.expMonth = method.getExpMonth();
        dto.expYear = method.getExpYear();
        dto.status = method.getStatus();
        dto.isDefault = method.getIsDefault();
        return dto;
    }
}
