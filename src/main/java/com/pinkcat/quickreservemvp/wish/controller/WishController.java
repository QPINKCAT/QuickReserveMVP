package com.pinkcat.quickreservemvp.wish.controller;

import com.pinkcat.quickreservemvp.common.model.BaseResponse;
import com.pinkcat.quickreservemvp.common.security.principal.UserPrincipal;
import com.pinkcat.quickreservemvp.wish.dto.DeleteWishlistRequestDTO;
import com.pinkcat.quickreservemvp.wish.dto.WishlistResponseDTO;
import com.pinkcat.quickreservemvp.wish.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        @AuthenticationPrincipal UserPrincipal user,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "20") int size) {
      return new BaseResponse<>(wishService.getWishlist(user.getUserPk(), page, size));
    }

    @DeleteMapping("")
    @ResponseBody
    public BaseResponse<Boolean> delete(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody DeleteWishlistRequestDTO request) {
       return new BaseResponse<>(wishService.deleteWishlist(user.getUserPk(), request));
    }
}
