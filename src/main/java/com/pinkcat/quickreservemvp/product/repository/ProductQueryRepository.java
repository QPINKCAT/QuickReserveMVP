package com.pinkcat.quickreservemvp.product.repository;

import com.pinkcat.quickreservemvp.category.entity.QCategoryProductEntity;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import com.pinkcat.quickreservemvp.product.entity.QProductEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductQueryRepository implements ProductCustomRepository {
    private final JPAQueryFactory queryFactory;

    QCategoryProductEntity categoryProductEntity = QCategoryProductEntity.categoryProductEntity;
    QProductEntity productEntity = QProductEntity.productEntity;

    @Override
    public Page<ProductEntity> searchProduct(Long categoryId, int page, int size, String keyword, Integer minPrice, Integer maxPrice,
                                 LocalDateTime start, LocalDateTime end, Integer minRating, Integer maxRating, Pageable pageable){

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(categoryProductEntity.category.pk.eq(categoryId));
        if (keyword != null && !keyword.isEmpty()) builder.and(productEntity.productName.contains(keyword));
        if (minPrice != null) builder.and(productEntity.price.goe(minPrice));
        if (maxPrice != null) builder.and(productEntity.price.loe(maxPrice));
        if (start != null) builder.and(productEntity.saleStartAt.after(start));
        if (end != null) builder.and(productEntity.saleEndAt.before(end));
        if (minRating != null) builder.and(productEntity.avgRating.goe(minRating));
        if (maxRating != null) builder.and(productEntity.avgRating.loe(maxRating));

        List<ProductEntity> products = queryFactory.select(productEntity)
                .from(categoryProductEntity)
                .join(productEntity).on(categoryProductEntity.product.pk.eq(productEntity.pk))
                .where(builder)
                .orderBy(productEntity.productName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(productEntity.count())
                .from(categoryProductEntity)
                .join(productEntity).on(categoryProductEntity.product.pk.eq(productEntity.pk))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(products, pageable, totalCount == null ? 0 : totalCount);
    }
}
