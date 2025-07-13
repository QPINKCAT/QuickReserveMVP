package com.pinkcat.quickreservemvp.order.repository;

import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import com.pinkcat.quickreservemvp.order.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends ActiveRepository<OrderItemEntity, Long> {
    Optional<OrderItemEntity> findByPk(Long orderItemPk);

    @Query("select oi " +
            "from OrderItemEntity oi " +
            "join OrderEntity o on oi.order = o " +
            "where o.orderNum = :orderNum")
    List<OrderItemEntity> findByOrderNum(@Param("orderNum") String orderNum);

    List<OrderItemEntity> findAllByOrderPk(Long orderPk);
}
