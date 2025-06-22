package com.pinkcat.quickreservemvp.wish.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class WishlistResponseDTO {

    @NotNull
    private int page;

    @NotNull
    private int totalPages;

    @NotNull
    private int size;

    @NotNull
    @Builder.Default
    private List<WishProduct> wishlist = new ArrayList<>();
    
    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class WishProduct {
        @NotNull
        private Long wishId;

        @NotNull
        private Long productId;

        @NotNull
        private String thumbnail;

        @NotNull
        private String name;

        @NotNull
        @Min(1)
        private Integer price;

        @NotNull
        @Min(1)
        private Float avgRating;

        @NotNull
        private String status;
    }
}
