package com.pinkcat.quickreservemvp.wish.controller;

import com.pinkcat.quickreservemvp.common.model.BaseResponse;
import com.pinkcat.quickreservemvp.wish.dto.DeleteWishlistRequestDTO;
import com.pinkcat.quickreservemvp.wish.dto.WishlistResponseDTO;
import com.pinkcat.quickreservemvp.wish.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/wish")
@RequiredArgsConstructor
public class WishController {
    private final WishService wishService;

    @GetMapping("")
    @ResponseBody
    public BaseResponse<WishlistResponseDTO> getWishlist(
        // authentication 추가 필요
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "20") int size) {
      return new BaseResponse<>(wishService.getWishlist(page, size));
    }

    @DeleteMapping("")
    @ResponseBody
    public BaseResponse<Boolean> delete(@RequestBody DeleteWishlistRequestDTO request) {
       return new BaseResponse<>(wishService.deleteWishlist(request));
    }
}
