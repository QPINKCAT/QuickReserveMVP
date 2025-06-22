package com.pinkcat.quickreservemvp.wish.service;

import com.pinkcat.quickreservemvp.wish.dto.WishlistResponseDTO;

public interface WishService {
    WishlistResponseDTO getWishlist(int page, int size);
}
