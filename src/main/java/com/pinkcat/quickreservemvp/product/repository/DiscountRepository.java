package com.pinkcat.quickreservemvp.product.repository;

import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import com.pinkcat.quickreservemvp.product.entity.DiscountEntity;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;

import java.util.Optional;

public interface DiscountRepository extends ActiveRepository<DiscountEntity, Long> {
    Optional<DiscountEntity> findByProduct(ProductEntity product);
}
