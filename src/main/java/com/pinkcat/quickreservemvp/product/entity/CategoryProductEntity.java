package com.pinkcat.quickreservemvp.product.entity;

import com.pinkcat.quickreservemvp.common.model.BaseEntity;
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
@Table(name = "category_product")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryProductEntity extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "category_product_pk")
//    private Long categoryProductPk;

    @Comment("카테고리")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_pk", nullable = false, updatable = false)
    private CategoryEntity category;

    @Comment("상품")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_pk", nullable = false, updatable = false)
    private ProductEntity product;
}
