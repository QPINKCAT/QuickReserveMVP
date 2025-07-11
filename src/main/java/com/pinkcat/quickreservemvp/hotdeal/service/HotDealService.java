package com.pinkcat.quickreservemvp.hotdeal.service;

import com.pinkcat.quickreservemvp.common.enums.SortEnum;
import com.pinkcat.quickreservemvp.common.enums.SortPivotEnum;
import com.pinkcat.quickreservemvp.hotdeal.dto.HotDealListResponseDTO;
import com.pinkcat.quickreservemvp.hotdeal.dto.HotDealResponseDTO;

public interface HotDealService {
    HotDealListResponseDTO getHotDealList(Integer page, Integer size, SortPivotEnum sortPivot, SortEnum sort);
    HotDealResponseDTO getHotDealDetail(Long hotDealPk);
}
