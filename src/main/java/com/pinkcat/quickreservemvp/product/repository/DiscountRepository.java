package com.pinkcat.quickreservemvp.product.repository;

import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import com.pinkcat.quickreservemvp.product.entity.DiscountEntity;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;

public interface DiscountRepository extends ActiveRepository<DiscountEntity, Long> {
    DiscountEntity findByProduct(ProductEntity product);
}
