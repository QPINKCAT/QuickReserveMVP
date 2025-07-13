package com.pinkcat.quickreservemvp.order.repository;

import com.pinkcat.quickreservemvp.order.dto.OrderListResponseDTO.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface OrderCustomRepository {
    Page<Order> findOrdersByCustomerIdAndStartEnd(Long customerId, LocalDateTime start, LocalDateTime end, Pageable pageable);
}
