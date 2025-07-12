package com.pinkcat.quickreservemvp.hotdeal.dto;

import com.pinkcat.quickreservemvp.common.enums.ProductStatusEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HotDealResponseDTO {
    @NotNull
    @Min(1)
    private Integer page;

    @NotNull
    @Min(1)
    private Integer totalPages;

    @NotNull
    @Min(1)
    private Integer size;


    @NotNull
    private Long hotDealId;

    @NotNull
    private String name;

    private String description;

    private String thumbnail;

    private LocalDateTime startAt;
    
    private LocalDateTime endAt;

    @Builder.Default
    private List<Product> products = new ArrayList<>();

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product {
        @NotNull
        private Long productId;

        @NotNull
        private String name;

        @NotNull
        private String thumbnail;

        @NotNull
        private Integer price;

        @NotNull
        private ProductStatusEnum status;

        @NotNull
        private Integer stock;

        private String salePercent;

        private Integer salePrice;

        private LocalDateTime saleStartAt;

        private LocalDateTime saleEndAt;
    }


}
