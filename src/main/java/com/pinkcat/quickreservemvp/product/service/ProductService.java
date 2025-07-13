package com.pinkcat.quickreservemvp.product.service;

import com.pinkcat.quickreservemvp.product.dto.ProductInfoResponseDTO;
import com.pinkcat.quickreservemvp.product.dto.ProductReviewListResponseDTO;
import com.pinkcat.quickreservemvp.product.dto.ProductSearchResponseDTO;

public interface ProductService {
    ProductInfoResponseDTO getProductInfo(Long productId);
    ProductReviewListResponseDTO getProductReviews(Long productId, int page, int size, Integer minRating, Integer maxRating);
    ProductSearchResponseDTO search(Long categoryId, int page, int size, String keyword, Integer minPrice,
                          Integer maxPrice, String start, String end, Integer minRating, Integer maxRating);
}
