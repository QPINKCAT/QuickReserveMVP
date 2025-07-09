package com.pinkcat.quickreservemvp.category.service;

import com.pinkcat.quickreservemvp.category.dto.CategoryListResponseDTO;
import com.pinkcat.quickreservemvp.category.dto.CategoryProductListResponseDTO;

public interface CategoryService {
    CategoryListResponseDTO getCategories();
    CategoryProductListResponseDTO getCategoryProducts(long category, int page, int size);
}
