package com.pinkcat.quickreservemvp.order.service;

import com.pinkcat.quickreservemvp.order.dto.OrderListResponseDTO;
import com.pinkcat.quickreservemvp.order.dto.OrderResponseDTO;

public interface OrderService {
    OrderListResponseDTO getOrderList(Long userPk, Integer page, Integer size, String start, String end);
    OrderResponseDTO getOrder(Long userPk, Long orderId);
}
