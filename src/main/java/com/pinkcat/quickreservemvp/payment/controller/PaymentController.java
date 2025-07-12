package com.pinkcat.quickreservemvp.payment.controller;

import com.pinkcat.quickreservemvp.common.model.BaseResponse;
import com.pinkcat.quickreservemvp.common.security.principal.UserPrincipal;
import com.pinkcat.quickreservemvp.payment.dto.PaymentRequestDTO;
import com.pinkcat.quickreservemvp.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("")
    @ResponseBody
    public BaseResponse<String> payment(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam("orderId") Long orderId,
            @RequestBody PaymentRequestDTO request){
        return new BaseResponse<>(paymentService.payment(user.getUserPk(), orderId, request));
    }
}
