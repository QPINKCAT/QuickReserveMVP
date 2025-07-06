package com.pinkcat.quickreservemvp.product.service;

import com.pinkcat.quickreservemvp.product.dto.ProductInfoResponseDTO;

public interface ProductService {
    ProductInfoResponseDTO getProductInfo(Long productId);
}
