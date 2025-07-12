package com.pinkcat.quickreservemvp.cart.service;

import com.pinkcat.quickreservemvp.cart.dto.CartItemListResponseDTO;
import com.pinkcat.quickreservemvp.cart.dto.DeleteCartItemRequestDTO;
import com.pinkcat.quickreservemvp.cart.dto.UpdateCartItemRequestDTO;
import com.pinkcat.quickreservemvp.product.dto.AddCartRequestDTO;
import com.pinkcat.quickreservemvp.product.dto.AddCartResponseDTO;

public interface CartService {
    AddCartResponseDTO addCart(Long userPk, Long productId, AddCartRequestDTO request);
    CartItemListResponseDTO getCart(Long userPk);
    Boolean updateCart(Long userPk, UpdateCartItemRequestDTO request);
    Boolean deleteCart(Long userPk, DeleteCartItemRequestDTO request);
}
