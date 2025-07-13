package com.pinkcat.quickreservemvp.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductSearchResponseDTO {
    @NotNull
    @Min(0)
    private Integer page;

    @NotNull
    @Min(0)

    private Integer totalPages;
    @NotNull
    @Min(0)
    private Integer size;

    @Builder.Default
    List<Product> products = new ArrayList<>();

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product {
        @NotNull
        private Long productId;

        @NotNull
        private String name;

        private String thumbnail;

        @NotNull
        private Integer price;

        private Integer discountPrice;

        private String discountRate;

        @NotNull
        private String avgRating;

        @NotNull
        private String status;

        @NotNull
        private LocalDateTime saleStartAt;

        private LocalDateTime saleEndAt;
    }
}
