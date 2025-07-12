package com.pinkcat.quickreservemvp.category.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryProductListResponseDTO {
    @NotNull
    @Min(1)
    private Integer page;

    @NotNull
    @Min(1)
    private Integer totalPages;

    @NotNull
    private Integer size;

    @Builder.Default
    private List<Product> products = new ArrayList<>();

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Product {
        @NotNull
        private Long productId;

        private String thumbnail;

        @NotNull
        private Integer price;

        private Integer discountPrice;

        private String discountRate;

        @NotNull
        private String avgRating;

        @NotNull
        private LocalDateTime saleStartAt;

        private LocalDateTime saleEndAt;

    }

}
