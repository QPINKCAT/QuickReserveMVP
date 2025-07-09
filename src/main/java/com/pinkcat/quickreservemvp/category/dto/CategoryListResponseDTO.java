package com.pinkcat.quickreservemvp.category.dto;

import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Builder.Default
    private List<Category> categories = new ArrayList<>();

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Category{
        @NotNull
        private Long categoryId;

        @NotNull
        private String name;

        @NotNull
        private Integer order;

        private Long topCategoryId;
    }
}
