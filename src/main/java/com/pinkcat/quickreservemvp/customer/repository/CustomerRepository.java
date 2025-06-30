package com.pinkcat.quickreservemvp.customer.repository;

import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;

import java.util.Optional;

public interface CustomerRepository extends ActiveRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findById(String userId);
}
