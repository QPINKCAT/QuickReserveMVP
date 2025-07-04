package com.pinkcat.quickreservemvp.category.repository;

import com.pinkcat.quickreservemvp.category.entity.QCategoryEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
public class CategoryQueryRepository implements CategoryCustomRepository {
    private final JPAQueryFactory queryFactory;

    public CategoryQueryRepository(final JPAQueryFactory queryFactory){
        this.queryFactory = queryFactory;
    }

     // 자기 자신 하위의 카테고리들을 조회하는 쿼리
    @Override
    public List<Long> findSubCategories(Long categoryId) {
        QCategoryEntity c1 = QCategoryEntity.categoryEntity;
        QCategoryEntity c2 = QCategoryEntity.categoryEntity;

        List<Long> categoryIds = queryFactory
            .select(c1.pk)
            .from(c1)
            .leftJoin(c2).on(c2.pk.eq(c1.topCategory.pk))
            .where(c1.pk.eq(categoryId).or(c1.topCategory.pk.eq(categoryId)))
            .fetch();

        return categoryIds;
        
    }
}
