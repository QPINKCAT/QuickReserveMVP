package com.pinkcat.quickreservemvp.product.repository;

import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;

import java.util.Optional;

public interface ProductRepository extends ActiveRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByPk(Long productId);
}
