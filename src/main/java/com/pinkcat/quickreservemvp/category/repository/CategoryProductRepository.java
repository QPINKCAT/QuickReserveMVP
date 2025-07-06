package com.pinkcat.quickreservemvp.category.repository;

import com.pinkcat.quickreservemvp.category.entity.CategoryEntity;
import com.pinkcat.quickreservemvp.category.entity.CategoryProductEntity;
import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryProductRepository extends ActiveRepository<CategoryProductEntity, Long> {

    List<CategoryProductEntity> findAllByCategory(CategoryEntity category);

    Optional<CategoryProductEntity> findByProduct(ProductEntity product);
}
