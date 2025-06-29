package com.pinkcat.quickreservemvp.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class CategoryDTO {
    private Long categoryId;
    private Long topCategoryId;
    private List<CategoryDTO> categories;

    @Builder
    public CategoryDTO(Long categoryId, Long topCategoryId, List<CategoryDTO> categories) {
        this.categoryId = categoryId;
        this.topCategoryId = topCategoryId;
        this.categories = new ArrayList<>();
    }
}
