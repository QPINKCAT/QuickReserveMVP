package com.pinkcat.quickreservemvp.wish.service;

import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import com.pinkcat.quickreservemvp.product.repository.ProductImageRepository;
import com.pinkcat.quickreservemvp.product.repository.ProductRepository;
import com.pinkcat.quickreservemvp.wish.dto.WishlistResponseDTO;
import com.pinkcat.quickreservemvp.wish.entity.CustomerProductWishEntity;
import com.pinkcat.quickreservemvp.wish.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

  @Override
  public WishlistResponseDTO getWishlist(int page, int size) {

    Long customerPk = 1L; // auth 추가 이후 수정

    List<WishlistResponseDTO.WishProduct> wishlist = new ArrayList<>();

    // 위시리스트 추가 내림차순 정렬
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
    Page<CustomerProductWishEntity> wishResult =
        wishRepository.findAllByCustomerPk(customerPk, pageable);

    for (CustomerProductWishEntity wish : wishResult) {
      log.info("Wish: {}", wish);
      ProductEntity product = productRepository.findByPk(wish.getProduct().getPk());
      Optional<String> thumbnail = productImageRepository.findThumbnailByProductPk(product.getPk());
      wishlist.add(
          WishlistResponseDTO.WishProduct.builder()
              .wishId(wish.getPk())
              .productId(product.getPk())
              .thumbnail(thumbnail.orElse(null))
              .name(product.getProductName())
              .price(product.getPrice())
              .avgRating(product.getAvgRating())
              .status(String.valueOf(product.getProductStatus()))
              .build());
    }

    return WishlistResponseDTO.builder()
        .page(page)
        .totalPages(wishResult.getTotalPages())
        .size(size)
        .wishlist(wishlist)
        .build();
    }
}
