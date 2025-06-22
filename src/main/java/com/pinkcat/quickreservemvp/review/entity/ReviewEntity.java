package com.pinkcat.quickreservemvp.review.entity;

import com.pinkcat.quickreservemvp.common.model.BaseEntity;
import com.pinkcat.quickreservemvp.order.entity.OrderItemEntity;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "customer_product_review")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_product_review_pk")
    private Long reviewPk;

    @Comment("평점, min : 1, max : 10")
    @Column(name = "customer_product_review_rating", nullable = false)
    private Integer rating;

    @Comment("리뷰")
    @Column(name = "customer_product_review_comment")
    private String comment;

    @Comment("주문아이템")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_order_item_pk", nullable = false, updatable = false)
    private OrderItemEntity orderItem;

    @Comment("고객")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_pk", nullable = false, updatable = false)
    private CustomerEntity customer;
}
