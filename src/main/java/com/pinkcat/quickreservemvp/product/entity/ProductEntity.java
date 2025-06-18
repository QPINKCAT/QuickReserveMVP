package com.pinkcat.quickreservemvp.product.entity;

import com.pinkcat.quickreservemvp.common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ProductEntity extends BaseEntity {
    @Id
    private Long productPk;

}
