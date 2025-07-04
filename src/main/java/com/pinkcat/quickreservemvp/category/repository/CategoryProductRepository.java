package com.pinkcat.quickreservemvp.category.repository;

import com.pinkcat.quickreservemvp.category.entity.CategoryEntity;
import com.pinkcat.quickreservemvp.category.entity.CategoryProductEntity;
import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import java.util.List;

public interface CategoryProductRepository extends ActiveRepository<CategoryProductEntity, Long> {

    List<CategoryProductEntity> findAllByCategory(CategoryEntity category);
}
