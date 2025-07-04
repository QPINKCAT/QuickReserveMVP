package com.pinkcat.quickreservemvp.category.repository;

import com.pinkcat.quickreservemvp.category.entity.CategoryEntity;
import java.util.List;

public interface CategoryCustomRepository {

    List<Long> findSubCategories(Long categoryId);
}
