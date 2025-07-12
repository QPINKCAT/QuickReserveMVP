package com.pinkcat.quickreservemvp.product.repository;

import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import com.pinkcat.quickreservemvp.product.entity.DiscountEntity;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface DiscountRepository extends ActiveRepository<DiscountEntity, Long> {
    Optional<DiscountEntity> findByProduct(ProductEntity product);

    @Query("select d.discountPrice " +
            "from DiscountEntity d " +
            "where d.product = :product " +
            "and d.startAt <= :now " +
            "and d.endAt > :now ")
    Optional<Integer> findValidDiscountPriceByProduct(@Param("product") ProductEntity product, @Param("now") LocalDateTime now);
}
