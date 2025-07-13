package com.pinkcat.quickreservemvp.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderResponseDTO {
    @NotNull
    private Long orderId;

    @NotNull
    private String orderNum;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private Customer customer;

    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Customer {
        @NotNull
        private String customerId;

        @NotNull
        private String name;

        @NotNull
        private String phoneNumber;

        @NotNull
        private String email;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class OrderItem {
        private Long orderItemId;
        private Integer sumPrice;
        private Integer originalPrice;
        private Integer salePrice;
        private Integer quantity;
        private String status;
        private Product product;
        private Payment payment;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product {
        private Long productId;
        private String name;
        private String description;
        private Integer price;
        private String status;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Payment {
        private Long paymentId;
        private String status;
        private Integer totalPrice;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
