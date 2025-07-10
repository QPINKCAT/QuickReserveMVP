package com.pinkcat.quickreservemvp.product.service;

import com.pinkcat.quickreservemvp.product.dto.ProductInfoResponseDTO;
import com.pinkcat.quickreservemvp.product.dto.ProductReviewListResponseDTO;

public interface ProductService {
    ProductInfoResponseDTO getProductInfo(Long productId);

    ProductReviewListResponseDTO getProductReviews(Long productId, int page, int size, Integer minRating, Integer maxRating);
}
