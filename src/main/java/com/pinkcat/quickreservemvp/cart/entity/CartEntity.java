package com.pinkcat.quickreservemvp.cart.entity;

import com.pinkcat.quickreservemvp.common.model.BaseEntity;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
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
@Table(name = "cart_item")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartEntity extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "cart_item_pk")
//    private Long cartItemPk;

    @Comment("수량")
    @Column(name = "cart_item_quantity", nullable = false)
    private Integer quantity;

    @Comment("고객")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_pk", nullable = false, updatable = false)
    private CustomerEntity customer;

    @Comment("상품")
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "product_pk", nullable = false, updatable = false)
    private ProductEntity product;
}
