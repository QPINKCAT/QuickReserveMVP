package com.pinkcat.quickreservemvp.hotdeal.repository;

import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import com.pinkcat.quickreservemvp.hotdeal.entity.HotDealProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HoteDealProductRepository extends ActiveRepository<HotDealProductEntity, Long> {
    Page<HotDealProductEntity> findAllByHotDealPk(Long hotDealPk, Pageable pageable);

}
