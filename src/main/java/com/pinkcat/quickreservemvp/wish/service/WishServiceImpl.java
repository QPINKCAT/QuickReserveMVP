package com.pinkcat.quickreservemvp.wish.service;

import com.pinkcat.quickreservemvp.common.exceptions.ErrorMessageCode;
import com.pinkcat.quickreservemvp.common.exceptions.PinkCatException;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import com.pinkcat.quickreservemvp.customer.repository.CustomerRepository;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import com.pinkcat.quickreservemvp.product.repository.ProductImageRepository;
import com.pinkcat.quickreservemvp.product.repository.ProductRepository;
import com.pinkcat.quickreservemvp.wish.dto.DeleteWishlistRequestDTO;
import com.pinkcat.quickreservemvp.wish.dto.WishlistResponseDTO;
import com.pinkcat.quickreservemvp.wish.entity.CustomerProductWishEntity;
import com.pinkcat.quickreservemvp.wish.repository.WishRepository;
import jakarta.persistence.EntityNotFoundException;
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

    private final CustomerRepository customerRepository;
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public WishlistResponseDTO getWishlist(Long userPk, int page, int size) {

        CustomerEntity customer = customerRepository.findByPkAndActiveTrue(userPk).orElseThrow(() ->
            new PinkCatException("비활성화된 계정입니다. 관리자에게 문의해주세요.", ErrorMessageCode.CUSTOMER_INACTIVE));

        List<WishlistResponseDTO.WishProduct> wishlist = new ArrayList<>();

        // 위시리스트 추가 내림차순 정렬
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<CustomerProductWishEntity> wishResult =
            wishRepository.findAllByCustomerPk(customer.getPk(), pageable);

        for (CustomerProductWishEntity wish : wishResult) {
            log.info("Wish: {}", wish);
            Optional<ProductEntity> product = productRepository.findByPk(wish.getProduct().getPk());
            if (product.isPresent()) {

                Optional<String> thumbnail = productImageRepository.findThumbnailByProductPk(product.get().getPk());
                wishlist.add(
                    WishlistResponseDTO.WishProduct.builder()
                        .wishId(wish.getPk())
                        .productId(product.get().getPk())
                        .thumbnail(thumbnail.orElse(null))
                        .name(product.get().getProductName())
                        .price(product.get().getPrice())
                        .avgRating(product.get().getAvgRating())
                        .status(String.valueOf(product.get().getProductStatus()))
                        .build());
            }
        }

        return WishlistResponseDTO.builder()
            .page(page)
            .totalPages(wishResult.getTotalPages())
            .size(size)
            .wishlist(wishlist)
            .build();
    }

    @Override
    @Transactional
    public boolean deleteWishlist(Long userPk, DeleteWishlistRequestDTO request) {

      CustomerEntity customer = customerRepository.findByPkAndActiveTrue(userPk).orElseThrow(() ->
              new PinkCatException("비활성화된 계정입니다. 관리자에게 문의해주세요.", ErrorMessageCode.CUSTOMER_INACTIVE));

      Optional<CustomerProductWishEntity> wish = wishRepository.findByPk(request.getWishId());
        wishRepository.delete(wish.orElseThrow(() -> new EntityNotFoundException("데이터가 없습니다.")));
        return true;
    }
}
