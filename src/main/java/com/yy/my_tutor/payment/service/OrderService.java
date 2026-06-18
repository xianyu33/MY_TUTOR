package com.yy.my_tutor.payment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yy.my_tutor.payment.domain.dto.OrderDetailDTO;
import com.yy.my_tutor.payment.domain.dto.OrderListItemDTO;
import com.yy.my_tutor.user.domain.User;

public interface OrderService {
    IPage<OrderListItemDTO> listMyOrders(Integer payerUserId, String status, Integer beneficiaryStudentId,
                                         int page, int size);
    IPage<OrderListItemDTO> listMyOrders(User payer, String status, Integer beneficiaryStudentId,
                                         int page, int size);

    OrderDetailDTO getMyOrderDetail(String orderNo, Integer payerUserId);
    OrderDetailDTO getMyOrderDetail(String orderNo, User payer);

    /** 后台:全量订单 */
    IPage<OrderListItemDTO> listAllOrders(String status, Integer payerUserId, Integer beneficiaryStudentId,
                                          int page, int size);
}
