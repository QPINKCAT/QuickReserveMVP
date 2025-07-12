package com.pinkcat.quickreservemvp.payment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class PaymentRequestDTO {
    @NotNull
    private Integer totalPrice;

    @NotNull
    @Builder.Default
    List<Item> items = new ArrayList<>();

    @Builder
    @Getter
    public static class Item {
        @NotNull
        private Long cartItemId;

        @NotNull
        private Long productId;

        @NotNull
        private String productName;

        private String thumbnail;

        @NotNull
        @Min(0)
        private Integer unitPrice;

        @Min(0)
        private Integer discountPrice;

        @NotNull
        @Min(0)
        private Integer quantity;

        @NotNull
        @Min(0)
        private Integer sumPrice;
    }
}
