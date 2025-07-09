package com.pinkcat.quickreservemvp.product.controller;

import com.pinkcat.quickreservemvp.common.model.BaseResponse;
import com.pinkcat.quickreservemvp.product.dto.ProductInfoResponseDTO;
import com.pinkcat.quickreservemvp.product.dto.ProductReviewResponseDTO;
import com.pinkcat.quickreservemvp.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{productId}")
    @ResponseBody
    public BaseResponse<ProductInfoResponseDTO> getProductInfo(
        @PathVariable Long productId){
        return new BaseResponse<>(productService.getProductInfo(productId));
    }

    @GetMapping("/{productId}/reviews")
    @ResponseBody
    public BaseResponse<ProductReviewResponseDTO> getProductInfo(
            @PathVariable Long productId,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "minRating", required = false) Integer minRating ,
            @RequestParam(value = "maxRating", required = false) Integer maxRating){
        return new BaseResponse<>(productService.getProductReviews(productId, page, size, minRating, maxRating));
    }
}
