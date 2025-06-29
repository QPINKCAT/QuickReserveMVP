package com.pinkcat.quickreservemvp.category.service;

import com.pinkcat.quickreservemvp.category.dto.CategoryListResponseDTO;
import com.pinkcat.quickreservemvp.category.dto.CategoryListResponseDTO.Category;
import com.pinkcat.quickreservemvp.category.entity.CategoryEntity;
import com.pinkcat.quickreservemvp.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryListResponseDTO getCategories() {
        List<Category> categories = new ArrayList<>();

        List<CategoryEntity> allCategories = categoryRepository.findAll();
        for (CategoryEntity category : allCategories) {
            categories.add(Category.builder()
                    .categoryId(category.getPk())
                    .name(category.getName())
                    .order(category.getOrder())
                    .topCategoryId(category.getTopCategory().getPk())
                    .build());
        }

        return CategoryListResponseDTO.builder()
                .categories(categories)
                .build();

    }
}
