package com.pinkcat.quickreservemvp.product.repository;

import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import com.pinkcat.quickreservemvp.product.entity.ProductImageEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends ActiveRepository<ProductImageEntity, Long> {

    @Query("select i.url " +
            "from ProductImageEntity i " +
            "where " +
            "   i.product.pk = :productPk" +
            "   and i.order = 1")
    Optional<String> findThumbnailByProductPk(@Param("productPk") Long productPk);

    @Query("select i " +
            "from ProductImageEntity i " +
            "where i.product = :product " +
            "order by i.order asc")
    List<ProductImageEntity> findAllImagesByProduct(@Param("product") ProductEntity product);
}
