package com.pinkcat.quickreservemvp.hotdeal.entity;


import com.pinkcat.quickreservemvp.common.model.BaseEntity;
import com.pinkcat.quickreservemvp.order.entity.OrderItemEntity;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "hot_deal_product")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AttributeOverride(name = "pk", column = @Column(name = "hot_deal_product_pk"))
public class HotDealProductEntity extends BaseEntity {
    @Comment("상품")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_pk", nullable = false, updatable = false)
    private ProductEntity product;

    @Comment("핫딜")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hot_deal_pk", nullable = false, updatable = false)
    private HotDealEntity hotDeal;
}
