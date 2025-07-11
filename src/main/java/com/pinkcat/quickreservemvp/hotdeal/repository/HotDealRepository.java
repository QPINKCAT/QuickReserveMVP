package com.pinkcat.quickreservemvp.hotdeal.repository;

import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import com.pinkcat.quickreservemvp.hotdeal.entity.HotDealEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface HotDealRepository extends ActiveRepository<HotDealEntity, Long> {
    @Query("select h "
        + "from HotDealEntity h")
    Page<HotDealEntity> findAllHotDeals(Pageable pageable);
}
