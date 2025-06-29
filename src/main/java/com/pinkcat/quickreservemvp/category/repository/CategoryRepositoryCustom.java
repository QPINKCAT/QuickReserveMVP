package com.pinkcat.quickreservemvp.category.repository;

import com.pinkcat.quickreservemvp.category.dto.CategoryDTO;
import com.pinkcat.quickreservemvp.category.entity.CategoryEntity;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<CategoryEntity> findSubCategories(Long categoryId);
}
