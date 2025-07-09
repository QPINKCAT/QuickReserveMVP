package com.pinkcat.quickreservemvp.wish.service;

import com.pinkcat.quickreservemvp.product.dto.AddWishResponseDTO;
import com.pinkcat.quickreservemvp.wish.dto.DeleteWishlistRequestDTO;
import com.pinkcat.quickreservemvp.wish.dto.WishlistResponseDTO;

public interface WishService {
    WishlistResponseDTO getWishlist(Long userPk, int page, int size);
    boolean deleteWishlist(Long userPk, DeleteWishlistRequestDTO request);

    AddWishResponseDTO addWishlist(Long userPk, Long productId);
}
