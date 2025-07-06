package com.pinkcat.quickreservemvp.category.repository;

import java.util.List;

public interface CategoryCustomRepository {

    List<Long> findSubCategoryIds(Long categoryId);

    List<Long> findParentCategoryIds(Long categoryId);
}
