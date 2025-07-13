package com.pinkcat.quickreservemvp.order.dto;

import jakarta.validation.constraints.Min;
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
public class CancelOrderResponseDTO {
    @NotNull
    private Long orderId;

    @NotNull
    @Min(0)
    private Integer totalRefundedPrice;

    @NotNull
    private LocalDateTime refundedAt;

    @Builder.Default
    List<Refund> refunds = new ArrayList<>();

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Refund {
        @NotNull
        private Long orderItemId;

        @NotNull
        @Min(0)
        private Integer refundedPrice;
    }
}
