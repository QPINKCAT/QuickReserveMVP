package com.pinkcat.quickreservemvp.category.repository;

import com.pinkcat.quickreservemvp.category.entity.CategoryEntity;
import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends ActiveRepository<CategoryEntity, Long> {
    @Query("select c " +
            "from CategoryEntity c " +
            "order by " +
            "c.pk asc, " +
            "c.order asc," +
            "c.topCategory.pk asc")
    List<CategoryEntity> findAllCategories();

    Optional<CategoryEntity> findCategoryEntityByPk(Long pk);

}
