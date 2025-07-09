package com.pinkcat.quickreservemvp.wish.repository;

import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import com.pinkcat.quickreservemvp.wish.entity.CustomerProductWishEntity;
import java.util.Optional;

public interface CustomerProductWishRepository extends ActiveRepository<CustomerProductWishEntity, Long> {

    Optional<CustomerProductWishEntity> findCustomerProductWishEntityByProductAndCustomer(ProductEntity product, CustomerEntity customer);
}
