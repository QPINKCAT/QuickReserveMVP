package com.pinkcat.quickreservemvp.review.service;

import com.pinkcat.quickreservemvp.review.dto.ReviewResponseDTO;
import com.pinkcat.quickreservemvp.review.dto.WriteReviewRequestDTO;
import com.pinkcat.quickreservemvp.review.dto.WriteReviewResponseDTO;

public interface ReviewService {
    ReviewResponseDTO getReview(Long reviewId);
    WriteReviewResponseDTO addReview(Long userPk, Long orderItemId, WriteReviewRequestDTO request);

}
