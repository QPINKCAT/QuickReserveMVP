package com.pinkcat.quickreservemvp.cart.repository;

import com.pinkcat.quickreservemvp.cart.entity.CartEntity;
import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends ActiveRepository<CartEntity, Long> {

    Optional<CartEntity> findCartEntityByCustomerAndProduct(CustomerEntity customer, ProductEntity product);

    @Query("select c " +
            "from CartEntity c " +
            "order by c.createdAt desc")
    List<CartEntity> findCartEntitiesByCustomerPk(Long customerPk);

    Optional<CartEntity> findCartEntityByPk(Long cartPk);
}
