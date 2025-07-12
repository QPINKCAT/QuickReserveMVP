package com.pinkcat.quickreservemvp.payment.service;

import com.pinkcat.quickreservemvp.payment.dto.PaymentRequestDTO;

public interface PaymentService {
     String payment(Long userId, Long orderId, PaymentRequestDTO request);
}
