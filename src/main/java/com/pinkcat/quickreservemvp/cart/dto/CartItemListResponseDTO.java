package com.pinkcat.quickreservemvp.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartItemListResponseDTO {
    @Builder.Default
    List<Item> items = new ArrayList<>();

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Item {
        @NotNull
        private Long cartItemId;

        @NotNull
        private Long productId;

        @NotNull
        private String name;

        private String thumbnail;

        @NotNull
        @Min(0)
        private Integer unitPrice;

        private Integer discountPrice;

        private String discountRate;

        @NotNull
        @Min(0)
        private Integer quantity;


    }
}
