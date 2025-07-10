package com.pinkcat.quickreservemvp.review.service;

import com.pinkcat.quickreservemvp.review.dto.WriteReviewRequestDTO;
import com.pinkcat.quickreservemvp.review.dto.WriteReviewResponseDTO;

public interface ReviewService {
    WriteReviewResponseDTO addReview(Long userPk, Long orderItemId, WriteReviewRequestDTO request);
}
