package com.pinkcat.quickreservemvp.order.entity;

import com.pinkcat.quickreservemvp.common.model.BaseEntity;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "order")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_pk")
    private Long orderPk;

    @Comment("주문번호")
    @Column(name = "order_num", length = 10, unique = true, nullable = false)
    private String orderNum;

    @Comment("고객")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_pk", nullable = false, updatable = false)
    private CustomerEntity customer;
}
