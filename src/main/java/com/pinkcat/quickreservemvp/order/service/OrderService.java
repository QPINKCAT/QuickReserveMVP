package com.pinkcat.quickreservemvp.order.service;

import com.pinkcat.quickreservemvp.order.dto.OrderListResponseDTO;

public interface OrderService {
    OrderListResponseDTO getOrderList(Long userPk, Integer page, Integer size, String start, String end);
}
