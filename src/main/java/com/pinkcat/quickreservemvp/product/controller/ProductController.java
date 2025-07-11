package com.pinkcat.quickreservemvp.product.controller;

import com.pinkcat.quickreservemvp.cart.service.CartService;
import com.pinkcat.quickreservemvp.common.model.BaseResponse;
import com.pinkcat.quickreservemvp.common.security.principal.UserPrincipal;
import com.pinkcat.quickreservemvp.product.dto.AddWishResponseDTO;
import com.pinkcat.quickreservemvp.product.dto.ProductInfoResponseDTO;
import com.pinkcat.quickreservemvp.product.dto.ProductReviewListResponseDTO;
import com.pinkcat.quickreservemvp.product.dto.AddCartRequestDTO;
import com.pinkcat.quickreservemvp.product.dto.AddCartResponseDTO;
import com.pinkcat.quickreservemvp.product.service.ProductService;
import com.pinkcat.quickreservemvp.wish.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final WishService wishService;
    private final CartService cartService;

    @GetMapping("/{productId}")
    @ResponseBody
    public BaseResponse<ProductInfoResponseDTO> getProductInfo(
        @PathVariable Long productId){
        return new BaseResponse<>(productService.getProductInfo(productId));
    }

    @GetMapping("/{productId}/reviews")
    @ResponseBody
    public BaseResponse<ProductReviewListResponseDTO> getProductInfo(
            @PathVariable Long productId,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "minRating", required = false) Integer minRating ,
            @RequestParam(value = "maxRating", required = false) Integer maxRating){
        return new BaseResponse<>(productService.getProductReviews(productId, page, size, minRating, maxRating));
    }

    @PostMapping("/{productId}/wish")
    @ResponseBody
    public BaseResponse<AddWishResponseDTO> addWish(
        @AuthenticationPrincipal UserPrincipal user,
        @PathVariable Long productId){
        return new BaseResponse<>(wishService.addWishlist(user.getUserPk(), productId));
    }

    @PostMapping("/{productId}/cart")
    @ResponseBody
    public BaseResponse<AddCartResponseDTO> addWish(
//        @AuthenticationPrincipal UserPrincipal user,
        @PathVariable Long productId,
        @RequestBody AddCartRequestDTO request){
        return new BaseResponse<>(cartService.addCart(1L, productId, request));
    }
}
