package com.pinkcat.quickreservemvp.category.entity;

import com.pinkcat.quickreservemvp.common.model.BaseEntity;
import jakarta.persistence.*;
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
@AttributeOverride(name = "pk", column = @Column(name = "category_pk"))
public class CategoryEntity extends BaseEntity {
    @Comment("카테고리명")
    @Column(name = "category_name")
    private String name;

    @Comment("카테고리 순서")
    @Column(name = "category_order")
    private Integer order;

    @Comment("상위 카테고리")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_pk", nullable = false, updatable = false)
    private CategoryEntity topCategory;
}
