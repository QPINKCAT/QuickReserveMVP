package com.pinkcat.quickreservemvp.product.entity;

import com.pinkcat.quickreservemvp.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "discount")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AttributeOverride(name = "pk", column = @Column(name = "discount_pk"))
public class DiscountEntity extends BaseEntity {
    @Comment("할인 적용 가격")
    @Column(name = "discount_price")
    private Integer discountPrice;

    @Comment("할인 시작일시")
    @Column(name = "discount_start_at")
    private LocalDateTime startAt;

    @Comment("할인 종료일시")
    @Column(name = "discount_end_at")
    private LocalDateTime endAt;

    @Comment("상품")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_pk", nullable = false, updatable = false)
    private ProductEntity product;
}
