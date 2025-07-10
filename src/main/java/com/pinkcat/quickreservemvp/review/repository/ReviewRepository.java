package com.pinkcat.quickreservemvp.review.repository;

import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import com.pinkcat.quickreservemvp.order.entity.OrderItemEntity;
import com.pinkcat.quickreservemvp.review.entity.ReviewEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends ActiveRepository<ReviewEntity, Long> {
    @Query("select coalesce(count(r.pk),0) " +
            "from ReviewEntity r " +
            "join r.orderItem oi " +
            "join oi.product p " +
            "where p.pk = :productPk")
    Integer countReviewsByProductPk(@Param("productPk") Long productPk);

    Optional<ReviewEntity> findByOrderItemAndCustomer(OrderItemEntity orderIten, CustomerEntity customer);
}
