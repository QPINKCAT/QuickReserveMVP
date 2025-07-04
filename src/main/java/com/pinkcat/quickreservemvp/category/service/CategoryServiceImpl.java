package com.pinkcat.quickreservemvp.category.service;

import com.pinkcat.quickreservemvp.category.dto.CategoryListResponseDTO;
import com.pinkcat.quickreservemvp.category.dto.CategoryListResponseDTO.Category;
import com.pinkcat.quickreservemvp.category.dto.CategoryProductListResponseDTO;
import com.pinkcat.quickreservemvp.category.entity.CategoryEntity;
import com.pinkcat.quickreservemvp.category.entity.CategoryProductEntity;
import com.pinkcat.quickreservemvp.category.repository.CategoryCustomRepository;
import com.pinkcat.quickreservemvp.category.repository.CategoryProductRepository;
import com.pinkcat.quickreservemvp.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final CategoryCustomRepository categoryCustomRepository;
    private final CategoryProductRepository categoryProductRepository;

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

    @Override
    public CategoryProductListResponseDTO getCategoryProducts(long category, int page, int size) {

        log.info("===================\ncategoryId : {}\n=============", category);
        List<Long> subCategories = categoryCustomRepository.findSubCategories(category);
        for (Long id : subCategories) {
            CategoryEntity c = categoryRepository.findCategoryEntityByPk(id);
            log.info("카테고리명 : {}, 상위카테고리id : {}", c.getName(), c.getTopCategory() == null ? null : entity.getTopCategory().getPk());
            List<CategoryProductEntity> products = categoryProductRepository.findAllByCategory(c);


        }

        List<CategoryProductListResponseDTO> products = new ArrayList<>();
        return CategoryProductListResponseDTO.builder()
                .build();
    }
}
