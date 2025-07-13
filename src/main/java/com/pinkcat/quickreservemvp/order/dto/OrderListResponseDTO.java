package com.pinkcat.quickreservemvp.order.dto;

import com.pinkcat.quickreservemvp.common.enums.PaymentStatusEnum;
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
public class OrderListResponseDTO {
    @NotNull
    @Min(1)
    private Integer page;

    @NotNull
    private Integer totalPages;

    @NotNull
    @Min(1)
    private Integer size;

    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Order {
        @NotNull
        private String orderNum;

        @NotNull
        private LocalDateTime orderDate;

        @Setter
        @NotNull
        private PaymentStatusEnum paymentStatus;

        @Setter
        @Builder.Default
        private List<Item> items = new ArrayList<>();
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Item {
        @NotNull
        private Long categoryId;

        @NotNull
        private String categoryName;

        @NotNull
        private Long productId;

        @NotNull
        private String productName;

        private String thumbnail;

        @NotNull
        @Min(0)
        private Integer originalPrice;

        @Min(0)
        private Integer discountPrice;

        @NotNull
        @Min(1)
        private Integer quantity;
    }
}
