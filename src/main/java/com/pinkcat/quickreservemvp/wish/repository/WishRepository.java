package com.pinkcat.quickreservemvp.wish.repository;

import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import com.pinkcat.quickreservemvp.wish.entity.CustomerProductWishEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishRepository extends ActiveRepository<CustomerProductWishEntity, Long> {
    Page<CustomerProductWishEntity> findAllByCustomerPk(Long customerPk, Pageable pageable);

    CustomerProductWishEntity findByPk(Long pk);

}
