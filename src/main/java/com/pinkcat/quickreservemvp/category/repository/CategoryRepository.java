package com.pinkcat.quickreservemvp.category.repository;

import com.pinkcat.quickreservemvp.category.entity.CategoryEntity;
import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends ActiveRepository<CategoryEntity, Long> {
    @Query("select c " +
            "from CategoryEntity c")
    public List<CategoryEntity> findAllCategories();
}
