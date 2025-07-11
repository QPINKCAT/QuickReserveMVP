package com.pinkcat.quickreservemvp.hotdeal.service;

import com.pinkcat.quickreservemvp.common.enums.SortEnum;
import com.pinkcat.quickreservemvp.common.enums.SortPivotEnum;
import com.pinkcat.quickreservemvp.hotdeal.dto.HotDealListResponseDTO;
import com.pinkcat.quickreservemvp.hotdeal.dto.HotDealListResponseDTO.HotDeal;
import com.pinkcat.quickreservemvp.hotdeal.entity.HotDealEntity;
import com.pinkcat.quickreservemvp.hotdeal.repository.HotDealRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotDealServiceImpl implements HotDealService{

    private final HotDealRepository hotDealRepository;

    @Override
    public HotDealListResponseDTO getHotDealList(Integer page, Integer size, SortPivotEnum sortPivot, SortEnum sort){
        Pageable pageable = PageRequest.of(page-1, size);
        if (sortPivot != null) {
            if (sort == SortEnum.ASC)
                pageable = PageRequest.of(page - 1, size, Sort.by(sortPivot.name()).ascending());
            else
                pageable = PageRequest.of(page - 1, size, Sort.by(sortPivot.name()).descending());
        }

        Page<HotDealEntity> hotDeals = hotDealRepository.findAllHotDeals(pageable);
        List<HotDeal> hotDealList = hotDeals.stream().map(e ->
            HotDeal.builder()
                .hotDealPk(e.getPk())
                .name(e.getName())
                .description(e.getDescription())
                .startAt(e.getStartAt())
                .endAt(e.getEndAt())
                .build()).toList();

        return HotDealListResponseDTO.builder()
            .page(page)
            .totalPages(hotDeals.getTotalPages())
            .size(size)
            .hotDealList(hotDealList)
            .build();
    }
}
