package com.pinkcat.quickreservemvp.review.controller;

import com.pinkcat.quickreservemvp.common.model.BaseResponse;
import com.pinkcat.quickreservemvp.common.security.principal.UserPrincipal;
import com.pinkcat.quickreservemvp.review.dto.WriteReviewRequestDTO;
import com.pinkcat.quickreservemvp.review.dto.WriteReviewResponseDTO;
import com.pinkcat.quickreservemvp.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("")
    @ResponseBody
    public BaseResponse<WriteReviewResponseDTO> addReview(
//        @AuthenticationPrincipal UserPrincipal user,
        @RequestParam(value = "orderItem") Long orderItemId,
        @RequestBody WriteReviewRequestDTO request){
        return new BaseResponse<>(reviewService.addReview(1L, orderItemId, request));
    }
}
