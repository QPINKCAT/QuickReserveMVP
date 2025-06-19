package com.pinkcat.quickreservemvp.wish.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WishListResponseDTO {
    // paging?

    @Builder.Default
    private List<WishProduct> wishList = new ArrayList<>();
    
    @Getter
    @AllArgsConstructor
    public static class WishProduct {
        private Long wishPk;
        private Long productPk;
        private String thumbnail;
        private String name;
        private Integer price;
        private Float avg_rating;
        private String status;
    }
}
