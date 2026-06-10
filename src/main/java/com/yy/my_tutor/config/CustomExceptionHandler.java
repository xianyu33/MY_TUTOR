package com.yy.my_tutor.config;


import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.payment.util.PaymentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author yy
 */
@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public RespResult handleGlobalException(Exception e){
        log.error(e.getMessage());
        e.printStackTrace();
        return RespResult.error(400, e.getMessage());
    }

    @ExceptionHandler(PaymentException.class)
    public RespResult<?> handlePaymentException(PaymentException e) {
        log.warn("PaymentException [{}]: {}", e.getCode(), e.getMessage());
        return RespResult.error(e.getMessage());
    }

}
