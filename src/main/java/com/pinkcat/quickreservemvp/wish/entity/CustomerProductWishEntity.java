package com.pinkcat.quickreservemvp.wish.entity;

import com.pinkcat.quickreservemvp.common.model.BaseEntity;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "customer_product_wish")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AttributeOverride(name = "pk", column = @Column(name = "customer_product_wish_pk"))
public class CustomerProductWishEntity extends BaseEntity {
    @Comment("고객")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_pk", nullable = false, updatable = false)
    private CustomerEntity customer;

    @Comment("상품")
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "product_pk", nullable = false, updatable = false)
    private ProductEntity product;
}