package com.yy.my_tutor.payment.service;

import com.yy.my_tutor.payment.domain.dto.AnnualLicenseQuoteDTO;
import com.yy.my_tutor.payment.domain.dto.CreateCheckoutRequest;
import com.yy.my_tutor.payment.domain.dto.DirectPaymentResponse;
import com.yy.my_tutor.user.domain.User;

public interface AnnualLicenseService {
    boolean supports(CreateCheckoutRequest req);

    AnnualLicenseQuoteDTO quote(Integer productId, Integer quantity);

    DirectPaymentResponse pay(CreateCheckoutRequest req, User payer);

    boolean activateForTeacherBind(Integer teacherId, Integer studentId, String operator);
}
