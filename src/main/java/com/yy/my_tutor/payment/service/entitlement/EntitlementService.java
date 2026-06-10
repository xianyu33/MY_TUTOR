package com.yy.my_tutor.payment.service.entitlement;

public interface EntitlementService {
    boolean hasActiveSubscription(Integer studentId);
    boolean canAccessCourse(Integer studentId, Integer courseId);
    boolean canAccessKnowledgePoint(Integer studentId, Integer knowledgePointId);
    boolean canAccessTestPack(Integer studentId, Integer testPackId);
}
