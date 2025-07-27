package com.pinkcat.quickreservemvp.wish;

import com.pinkcat.quickreservemvp.common.enums.GenderEnum;
import com.pinkcat.quickreservemvp.common.enums.ProductStatusEnum;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import com.pinkcat.quickreservemvp.customer.repository.CustomerRepository;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import com.pinkcat.quickreservemvp.product.repository.ProductImageRepository;
import com.pinkcat.quickreservemvp.product.repository.ProductRepository;
import com.pinkcat.quickreservemvp.wish.dto.WishlistResponseDTO;
import com.pinkcat.quickreservemvp.wish.entity.CustomerProductWishEntity;
import com.pinkcat.quickreservemvp.wish.repository.WishRepository;
import com.pinkcat.quickreservemvp.wish.service.WishServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class WishServiceTest {

    @InjectMocks
    private WishServiceImpl wishService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private WishRepository wishRepository;

    @Mock
    private ProductImageRepository productImageRepository;

    @Mock
    private ProductRepository productRepository;

    private List<CustomerProductWishEntity> wishes;

    private CustomerEntity customer;
    private CustomerProductWishEntity wish;

    @BeforeEach
    void setUp() {
        customer = CustomerEntity.builder()
                .id("customer1")
                .email("customer1@gmail.com")
                .name("김고객")
                .phoneNumber("010-1234-1234")
                .password("password1")
                .gender(GenderEnum.FEMALE)
                .build();
        customer.setPk(1L);
        when(customerRepository.findByPkAndActiveTrue(1L)).thenReturn(Optional.of(customer));

        wishes = new ArrayList<>();
        for(int i = 1; i <= 20; i++) {
            ProductEntity product = ProductEntity.builder()
                    .productName("상품" + i)
                    .price(10000*i)
                    .avgRating(4.3f)
                    .productDescription("description" + i)
                    .productStatus(ProductStatusEnum.ON)
                    .build();
            product.setPk((long) i);
            product.setActive(true);
            product.setCreatedAt(LocalDateTime.now());
            product.setUpdatedAt(null);
            wish = CustomerProductWishEntity.builder()
                    .customer(customer)
                    .product(product)
                    .build();
            wish.setPk((long) i);
            wishes.add(wish);

            lenient().when(productRepository.findByPk((long) i)).thenReturn(Optional.of(product));
            lenient().when(productImageRepository.findThumbnailByProductPk((long) i)).thenReturn("thumbnail".describeConstable());

        }
    }

    @Test
    @DisplayName("위시리스트 조회 테스트")
    void getWishlistSuccess() {
        int page = 1;
        int size = 20;

        // when
        Page<CustomerProductWishEntity> mockPage = new PageImpl<>(wishes.subList(0, 20), PageRequest.of(page-1, size), wishes.size());
        when(wishRepository.findAllByCustomerPk(anyLong(), any(Pageable.class))).thenReturn(mockPage);

        WishlistResponseDTO result = wishService.getWishlist(1L, page, size);

        // then
        assertEquals(1, result.getPage());
        assertEquals(20, result.getSize());
        assertEquals(1, result.getTotalPages());
        assertEquals(20, result.getWishlist().size());

    }

    @Test
    @DisplayName("위시리스트 조회 테스트 : 2페이지")
    void getWishlistSecondPageSuccess() {
        int page = 2;
        int size = 10;

        // when
        Page<CustomerProductWishEntity> mockPage = new PageImpl<>(wishes.subList(10, 20), PageRequest.of(page-1, size), wishes.size());
        when(wishRepository.findAllByCustomerPk(anyLong(), any(Pageable.class))).thenReturn(mockPage);

        WishlistResponseDTO result = wishService.getWishlist(1L, page, size);

        // then
        assertEquals(2, result.getPage());
        assertEquals(10, result.getSize());
        assertEquals(2, result.getTotalPages());
        assertEquals(10, result.getWishlist().size());
    }



    @Test
    @DisplayName("위시리스트 비어있을 때")
    void getWishlistEmptySuccess() {
        int page = 1;
        int size = 20;

        // when
        Page<CustomerProductWishEntity> mockPage = new PageImpl<>(new ArrayList<>(), PageRequest.of(page-1, size), 0);
        when(wishRepository.findAllByCustomerPk(anyLong(), any(Pageable.class))).thenReturn(mockPage);

        WishlistResponseDTO result = wishService.getWishlist(1L, page, size);

        // then
        assertEquals(1, result.getPage());
        assertEquals(20, result.getSize());
        assertEquals(0, result.getTotalPages());
        assertEquals(0, result.getWishlist().size());
    }

}
