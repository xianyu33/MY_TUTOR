package com.yy.my_tutor.payment.util;

import lombok.Getter;

/** 业务异常,由 CustomExceptionHandler 统一拦截转 RespResult.error */
@Getter
public class PaymentException extends RuntimeException {
    private final String code;

    public PaymentException(String code, String message) {
        super(message);
        this.code = code;
    }

    public static PaymentException of(String code) {
        return new PaymentException(code, code);
    }

    public static PaymentException of(String code, String message) {
        return new PaymentException(code, message);
    }
}
