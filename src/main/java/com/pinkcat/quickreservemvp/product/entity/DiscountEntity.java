package com.pinkcat.quickreservemvp.product.entity;

import com.pinkcat.quickreservemvp.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "discount")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discountPk;

    @Comment("할인이 적용된 가격")
    @Column(name = "discount_price")
    private Integer discountPrice;

    @Comment("할인 시작일")
    @Column(name = "discount_start_at")
    private Long startAt;

    @Comment("할인 종료일")
    @Column(name = "discount_end_at")
    private Long endAt;

    @Comment("상품")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_pk", nullable = false, updatable = false)
    private ProductEntity product;
}
