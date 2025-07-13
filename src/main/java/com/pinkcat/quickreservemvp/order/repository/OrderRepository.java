package com.pinkcat.quickreservemvp.order.repository;

import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import com.pinkcat.quickreservemvp.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends ActiveRepository<OrderEntity, Long> {

    boolean existsByOrderNum(String string);

    @Query("select o " +
            "from OrderEntity o " +
            "where o.pk = :orderPk")
    Optional<OrderEntity> findByOrderPk(@Param("orderPk") Long orderPk);
}
