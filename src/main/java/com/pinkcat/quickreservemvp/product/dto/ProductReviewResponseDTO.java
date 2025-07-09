package com.pinkcat.quickreservemvp.product.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProductReviewResponseDTO {
    @NotNull
    private int page;

    @NotNull
    private int totalPages;

    @NotNull
    private int size;

    @NotNull
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class Review {
        @NotNull
        private String customerId;

        @NotNull
        private String productName;

        @NotNull
        private Integer rating;

        private String comment;

        @NotNull
        private LocalDateTime createdAt;
    }

}
