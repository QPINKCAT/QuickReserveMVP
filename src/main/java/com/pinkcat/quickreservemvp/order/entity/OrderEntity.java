package com.pinkcat.quickreservemvp.order.entity;

import com.pinkcat.quickreservemvp.common.model.BaseEntity;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "product_order")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AttributeOverride(name = "pk", column = @Column(name = "product_order_pk"))
public class OrderEntity extends BaseEntity {
    @Comment("주문번호")
    @Column(name = "product_order_num", length = 10, unique = true, nullable = false)
    private String orderNum;

    @Comment("고객")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_pk", nullable = false, updatable = false)
    private CustomerEntity customer;
}
