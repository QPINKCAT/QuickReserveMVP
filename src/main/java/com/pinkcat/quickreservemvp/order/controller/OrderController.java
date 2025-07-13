package com.pinkcat.quickreservemvp.order.controller;

import com.pinkcat.quickreservemvp.common.model.BaseResponse;
import com.pinkcat.quickreservemvp.common.security.principal.UserPrincipal;
import com.pinkcat.quickreservemvp.order.dto.CancelOrderResponseDTO;
import com.pinkcat.quickreservemvp.order.dto.CancelOrderRequestDTO;
import com.pinkcat.quickreservemvp.order.dto.OrderListResponseDTO;
import com.pinkcat.quickreservemvp.order.dto.OrderResponseDTO;
import com.pinkcat.quickreservemvp.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/list")
    @ResponseBody
    public BaseResponse<OrderListResponseDTO> getOrderList(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "start", required = false) String start,
            @RequestParam(value = "end", required = false) String end){
        return new BaseResponse<>(orderService.getOrderList(user.getUserPk(), page, size, start, end));
    }

    @GetMapping("")
    @ResponseBody
    public BaseResponse<OrderResponseDTO> getOrder(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(value = "order") String orderNum){
        return new BaseResponse<>(orderService.getOrder(user.getUserPk(), orderNum));
    }

    @PostMapping("")
    @ResponseBody
    public BaseResponse<CancelOrderResponseDTO> cancelOrder(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(value = "order") String orderNum,
            @RequestBody CancelOrderRequestDTO request){
        return new BaseResponse<>(orderService.cancelOrder(user.getUserPk(), orderNum, request));
    }
}
