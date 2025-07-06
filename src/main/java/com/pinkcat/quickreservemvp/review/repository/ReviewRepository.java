package com.pinkcat.quickreservemvp.review.repository;

import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import com.pinkcat.quickreservemvp.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends ActiveRepository<ReviewEntity, Long> {
    @Query("select coalesce(count(r.pk),0) " +
            "from ReviewEntity r " +
            "join OrderItemEntity oi on r.orderItem = oi " +
            "join ProductEntity p on oi.product = p " +
            "where p.pk = :productPk")
    Integer countReviewsByProductPk(@Param("productPk") Long productPk);

}
