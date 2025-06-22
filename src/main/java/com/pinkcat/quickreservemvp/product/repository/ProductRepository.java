package com.pinkcat.quickreservemvp.product.repository;

import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends ActiveRepository<ProductEntity, Long> {

    ProductEntity findByPk(Long pk);
}
