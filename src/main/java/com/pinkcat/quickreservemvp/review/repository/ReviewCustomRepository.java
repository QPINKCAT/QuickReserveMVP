package com.pinkcat.quickreservemvp.review.repository;

import com.pinkcat.quickreservemvp.review.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewCustomRepository {
    Page<ReviewEntity> findProductReviewsByConditions(Long productPk, Integer minRating, Integer maxRating, Pageable pageable);
}
