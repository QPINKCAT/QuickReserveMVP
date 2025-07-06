package com.pinkcat.quickreservemvp.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProductInfoResponseDTO {
    @NotNull
    private Long productId;

    @NotNull
    @Builder.Default
    private List<Category> categories = new ArrayList<>();

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    @Min(0)
    private Integer price;

    private String discountRate;

    @Min(0)
    private Integer finalPrice;

    @NotNull
    private String avgRating;

    @NotNull
    private Integer reviewCount;

    @NotNull
    @Builder.Default
    private List<Image> images = new ArrayList<>();

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class Category {
        @NotNull
        private Long categoryId;

        @NotNull
        private String name;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class Image {
        @NotNull
        private String url;

        @NotNull
        private Integer order;
    }

}
