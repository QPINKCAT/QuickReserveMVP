package com.pinkcat.quickreservemvp.order.repository;

import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import com.pinkcat.quickreservemvp.order.entity.OrderItemEntity;
import java.util.Optional;

public interface OrderItemRepository extends ActiveRepository<OrderItemEntity, Long> {
    Optional<OrderItemEntity> findByPk(Long orderItemPk);
}
