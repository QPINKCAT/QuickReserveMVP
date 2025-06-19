package com.pinkcat.quickreservemvp.order.entity;

import com.pinkcat.quickreservemvp.common.enums.StatusEnum;
import com.pinkcat.quickreservemvp.common.model.BaseEntity;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import com.pinkcat.quickreservemvp.user.entity.UserEntity;
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
@Table(name = "order_item")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_pk")
    private Long orderItemPk;

    @Comment("할인전 가격")
    @Column(name = "order_item_original_price")
    private Integer originalPrice;

    @Comment("할인 가격")
    @Column(name = "order_item_saled_price")
    private Integer saledPrice;

    @Comment("수량")
    @Column(name = "order_item_quantity")
    private Integer quantity;

    @Comment("주문 상태")
    @Column(name = "order_item_status", nullable = false)
    private StatusEnum status;

    @Comment("유저")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk", nullable = false, updatable = false)
    private UserEntity user;

    @Comment("상품")
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "product_pk", nullable = false, updatable = false)
    private ProductEntity product;

    @Comment("주문")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_pk", nullable = false, updatable = false)
    private OrderEntity order;
}
