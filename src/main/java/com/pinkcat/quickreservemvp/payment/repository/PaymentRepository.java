package com.pinkcat.quickreservemvp.payment.repository;

import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import com.pinkcat.quickreservemvp.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends ActiveRepository<PaymentEntity, Long> {
    @Query("select p " +
            "from PaymentEntity p " +
            "join OrderEntity o on p.order = o " +
            "where o.orderNum = :orderNum")
    Optional<PaymentEntity> findByOrderNum(@Param("orderNum") String orderNum);
}
