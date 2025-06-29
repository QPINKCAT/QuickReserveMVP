package com.pinkcat.quickreservemvp.product.entity;

import com.pinkcat.quickreservemvp.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "category")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEntity extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "category_pk")
//    private Long categoryPk;

    @Comment("카테고리명")
    @Column(name = "category_name")
    private String name;

    @Comment("카테고리 순서")
    @Column(name = "category_order")
    private Integer order;

    @Comment("상위 카테고리")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pk", nullable = false, updatable = false)
    private CategoryEntity topCategory;
}
