package com.pinkcat.quickreservemvp.cart.controller;

import com.pinkcat.quickreservemvp.cart.dto.CartItemListResponseDTO;
import com.pinkcat.quickreservemvp.cart.dto.DeleteCartItemRequestDTO;
import com.pinkcat.quickreservemvp.cart.dto.UpdateCartItemRequestDTO;
import com.pinkcat.quickreservemvp.cart.service.CartService;
import com.pinkcat.quickreservemvp.common.model.BaseResponse;
import com.pinkcat.quickreservemvp.common.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping("")
    @ResponseBody
    public BaseResponse<CartItemListResponseDTO> getCart(
            @AuthenticationPrincipal UserPrincipal user){
        Long customer = user == null ? null : user.getUserPk();
        return new BaseResponse<>(cartService.getCart(customer));
    }

    @PatchMapping("")
    @ResponseBody
    public BaseResponse<Boolean> updateCart(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody UpdateCartItemRequestDTO request){
        Long customer = user == null ? null : user.getUserPk();
        return new BaseResponse<>(cartService.updateCart(customer, request));
    }

    @DeleteMapping("")
    @ResponseBody
    public BaseResponse<Boolean> updateCart(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody DeleteCartItemRequestDTO request){
        Long customer = user == null ? null : user.getUserPk();
        return new BaseResponse<>(cartService.deleteCart(customer, request));
    }
}

