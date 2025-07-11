package com.pinkcat.quickreservemvp.review.service;

import com.pinkcat.quickreservemvp.review.dto.ReviewResponseDTO;
import com.pinkcat.quickreservemvp.review.dto.UpdateReviewRequestDTO;
import com.pinkcat.quickreservemvp.review.dto.WriteReviewRequestDTO;
import com.pinkcat.quickreservemvp.review.dto.CUDReviewResponseDTO;

public interface ReviewService {
    ReviewResponseDTO getReview(Long reviewId);
    CUDReviewResponseDTO addReview(Long userPk, Long orderItemId, WriteReviewRequestDTO request);
    CUDReviewResponseDTO updateReview(Long userPk, Long reviewId, UpdateReviewRequestDTO request);

}
