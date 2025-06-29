package com.pinkcat.quickreservemvp.category.repository;

import com.pinkcat.quickreservemvp.category.dto.CategoryDTO;
import com.pinkcat.quickreservemvp.category.entity.CategoryEntity;
import com.pinkcat.quickreservemvp.category.entity.QCategoryEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public CategoryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<CategoryEntity> findSubCategories(Long categoryId) {
        QCategoryEntity parent = new QCategoryEntity("parent");
        QCategoryEntity child = new QCategoryEntity("child");

        List<CategoryEntity> result = queryFactory
                .select(Projections.fields(CategoryEntity.class,
                        parent.pk,
                        child.pk))
                .from(child)
                .leftJoin(parent).on(parent.pk.eq(child.topCategory.pk))
                .orderBy(parent.pk.asc(), child.order.asc())
                .fetch();

        return result;
    }
}
