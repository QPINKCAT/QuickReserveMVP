package com.pinkcat.quickreservemvp.order.repository;

import com.pinkcat.quickreservemvp.order.dto.OrderListResponseDTO.Order;
import com.pinkcat.quickreservemvp.order.entity.QOrderEntity;
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
@RequiredArgsConstructor
@Repository
public class OrderQueryRepository implements OrderCustomRepository {
    private final JPAQueryFactory queryFactory;

    QOrderEntity orderEntity = QOrderEntity.orderEntity;

    @Override
    public Page<Order> findOrdersByCustomerIdAndStartEnd(Long customerId, LocalDateTime start, LocalDateTime end, Pageable pageable){

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(orderEntity.customer.pk.eq(customerId));
        if (start != null) builder.and(orderEntity.createdAt.after(start));
        if (end != null) builder.and(orderEntity.createdAt.before(end.plusDays(1)));

        List<Order> orders = queryFactory
                .select(orderEntity)
                .from(orderEntity)
                .where(builder)
                .orderBy(orderEntity.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch().stream().map(o -> Order.builder()
                        .orderNum(o.getOrderNum())
                        .orderDate(o.getCreatedAt())
                        .build()).toList();

        Long totalCount = queryFactory
                .select(orderEntity.count())
                .from(orderEntity)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(orders, pageable, totalCount == null ? 0 : totalCount);
    }
}
