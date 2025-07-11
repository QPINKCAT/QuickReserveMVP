package com.pinkcat.quickreservemvp.hotdeal.controller;

import com.pinkcat.quickreservemvp.common.enums.SortEnum;
import com.pinkcat.quickreservemvp.common.enums.SortPivotEnum;
import com.pinkcat.quickreservemvp.common.model.BaseResponse;
import com.pinkcat.quickreservemvp.hotdeal.dto.HotDealListResponseDTO;
import com.pinkcat.quickreservemvp.hotdeal.service.HotDealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/hotdeals")
public class HotDealController {
    private final HotDealService hotDealService;

    @GetMapping("")
    @ResponseBody
    public BaseResponse<HotDealListResponseDTO> getHotDealList(
        @RequestParam(value = "page") Integer page,
        @RequestParam(value = "size") Integer size,
        @RequestParam(value = "sortPivot", required = false) SortPivotEnum sortPivot,
        @RequestParam(value = "sort", required = false) SortEnum sort){
        return new BaseResponse<>(hotDealService.getHotDealList(page, size, sortPivot, sort));
    }
}
