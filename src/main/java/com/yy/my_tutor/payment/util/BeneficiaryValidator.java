package com.yy.my_tutor.payment.util;

import com.yy.my_tutor.user.domain.GuardianStudentRel;
import com.yy.my_tutor.user.domain.User;
import com.yy.my_tutor.user.mapper.GuardianStudentRelMapper;
import com.yy.my_tutor.user.mapper.UserMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 校验付款人与受益学生的归属关系。两种合法情况:
 * 1. 学生自付: payerUserId == beneficiaryStudentId,且该学生存在
 * 2. 家长/监护人付款: 在 guardian_student_rel 表中存在 (guardian_id=payerUserId, student_id=beneficiaryStudentId) 的有效关系
 */
@Component
public class BeneficiaryValidator {

    @Resource
    private UserMapper userMapper;

    @Resource
    private GuardianStudentRelMapper guardianStudentRelMapper;

    /**
     * 断言付款人对受益学生有支付权限,否则抛出 PaymentException。
     *
     * @param payerUserId            付款人 user.id
     * @param beneficiaryStudentId   受益学生 user.id
     */
    public void assertAccessible(Integer payerUserId, Integer beneficiaryStudentId) {
        if (beneficiaryStudentId == null) {
            throw PaymentException.of("PAYMENT_BENEFICIARY_REQUIRED", "Beneficiary student is required.");
        }
        if (payerUserId == null) {
            throw PaymentException.of("PAYMENT_PAYER_REQUIRED", "Payer is required.");
        }

        // 情况 1: 学生自付 — payerUserId == beneficiaryStudentId,且学生记录存在
        if (payerUserId.equals(beneficiaryStudentId)) {
            User u = userMapper.findById(beneficiaryStudentId);
            if (u != null) {
                return;
            }
        }

        // 情况 2: 家长/监护人付款 — 必须存在有效的 GuardianStudentRel 关系
        GuardianStudentRel rel = guardianStudentRelMapper.findUnique(payerUserId, beneficiaryStudentId);
        if (rel == null || "Y".equalsIgnoreCase(rel.getDeleteFlag())) {
            throw PaymentException.of("PAYMENT_BENEFICIARY_INVALID", "This payer is not authorized for the selected student.");
        }
    }
}
