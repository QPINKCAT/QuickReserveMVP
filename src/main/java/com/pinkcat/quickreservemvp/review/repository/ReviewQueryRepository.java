package com.pinkcat.quickreservemvp.review.repository;

import com.pinkcat.quickreservemvp.order.entity.QOrderItemEntity;
import com.pinkcat.quickreservemvp.product.entity.QProductEntity;
import com.pinkcat.quickreservemvp.review.entity.QReviewEntity;
import com.pinkcat.quickreservemvp.review.entity.ReviewEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class ReviewQueryRepository implements ReviewCustomRepository{
    private final JPAQueryFactory queryFactory;

    public ReviewQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QReviewEntity reviewEntity = QReviewEntity.reviewEntity;
    QOrderItemEntity orderItemEntity = QOrderItemEntity.orderItemEntity;
    QProductEntity productEntity = QProductEntity.productEntity;

    @Override
    public Page<ReviewEntity> findProductReviewsByConditions(Long productPk, Integer minRating, Integer maxRating, Pageable pageable){

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(reviewEntity.orderItem.product.pk.eq(productPk));
        if (minRating != null) builder.and(reviewEntity.rating.goe(minRating));
        if (maxRating != null) builder.and(reviewEntity.rating.loe(maxRating));

        List<ReviewEntity> reviews = queryFactory
                .select(reviewEntity)
                .from(reviewEntity)
                .join(orderItemEntity).on(reviewEntity.orderItem.pk.eq(orderItemEntity.pk))
                .join(productEntity).on(orderItemEntity.product.pk.eq(productEntity.pk))
                .where(builder)
                .orderBy(reviewEntity.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory
                .select(reviewEntity.count())
                .from(reviewEntity)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(reviews, pageable, totalCount == null ? 0 : totalCount);
    }
}
