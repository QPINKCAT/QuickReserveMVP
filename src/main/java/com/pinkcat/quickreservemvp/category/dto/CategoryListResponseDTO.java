package com.pinkcat.quickreservemvp.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryListResponseDTO {

    @Builder.Default
    private List<Category> categories = new ArrayList<>();

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Category{
        private Long categoryId;
        private String name;
        private Integer order;
        private Long topCategoryId;
    }
}
