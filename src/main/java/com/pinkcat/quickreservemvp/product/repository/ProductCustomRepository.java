package com.pinkcat.quickreservemvp.product.repository;

import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ProductCustomRepository {
    Page<ProductEntity> searchProduct(Long categoryId, int page, int size, String keyword, Integer minPrice, Integer maxPrice,
                              LocalDateTime start, LocalDateTime end, Integer minRating, Integer maxRating, Pageable pageable);
}
