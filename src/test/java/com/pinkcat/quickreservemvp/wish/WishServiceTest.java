package com.pinkcat.quickreservemvp.wish;

import com.pinkcat.quickreservemvp.common.enums.GenderEnum;
import com.pinkcat.quickreservemvp.common.enums.ProductStatusEnum;
import com.pinkcat.quickreservemvp.common.exceptions.ErrorMessageCode;
import com.pinkcat.quickreservemvp.common.exceptions.PinkCatException;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import com.pinkcat.quickreservemvp.customer.repository.CustomerRepository;
import com.pinkcat.quickreservemvp.product.dto.AddWishResponseDTO;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import com.pinkcat.quickreservemvp.product.repository.ProductImageRepository;
import com.pinkcat.quickreservemvp.product.repository.ProductRepository;
import com.pinkcat.quickreservemvp.wish.dto.DeleteWishlistRequestDTO;
import com.pinkcat.quickreservemvp.wish.dto.WishlistResponseDTO;
import com.pinkcat.quickreservemvp.wish.entity.CustomerProductWishEntity;
import com.pinkcat.quickreservemvp.wish.repository.CustomerProductWishRepository;
import com.pinkcat.quickreservemvp.wish.repository.WishRepository;
import com.pinkcat.quickreservemvp.wish.service.WishServiceImpl;
import org.junit.jupiter.api.AfterEach;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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

    @Mock
    private CustomerProductWishRepository customerProductWishRepository;

    private List<CustomerProductWishEntity> wishes;

    private CustomerEntity customer;
    private CustomerProductWishEntity wish;
    private ProductEntity product;

    private final Long userPk = 1L;
    private final Long productPk = 1L;
    private final Long wishPk = 1L;

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
        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));

        wishes = new ArrayList<>();
        for(int i = 1; i <= 20; i++) {
            product = ProductEntity.builder()
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

        WishlistResponseDTO result = wishService.getWishlist(userPk, page, size);

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

        WishlistResponseDTO result = wishService.getWishlist(userPk, page, size);

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

        WishlistResponseDTO result = wishService.getWishlist(userPk, page, size);

        // then
        assertEquals(1, result.getPage());
        assertEquals(20, result.getSize());
        assertEquals(0, result.getTotalPages());
        assertEquals(0, result.getWishlist().size());
    }

    @DisplayName("위시리스트 추가 성공")
    @Test
    void addWishSuccess() {
        // given
        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(productRepository.findByPk(productPk)).thenReturn(Optional.of(product));
        when(customerProductWishRepository.findCustomerProductWishEntityByProductAndCustomer(product, customer))
            .thenReturn(Optional.empty());

        // when
        AddWishResponseDTO result = wishService.addWishlist(userPk, productPk);

        // then
        assertEquals("위시리스트에 상품을 담았습니다.", result.getResult());
        verify(customerProductWishRepository).save(any(CustomerProductWishEntity.class));
    }

    @DisplayName("위시리스트 추가 실패 : 판매 중지 상품")
    @Test
    void addWishFailProductOff() {
        // given
        product = ProductEntity.builder()
                .productStatus(ProductStatusEnum.OFF)
                .build();
        product.setPk(1L);

        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(productRepository.findByPk(productPk)).thenReturn(Optional.of(product));

        // when
        AddWishResponseDTO result = wishService.addWishlist(userPk, productPk);

        // then
        assertEquals("판매 중지된 상품입니다.", result.getResult());
        verify(customerProductWishRepository, never()).save(any());
    }

    @DisplayName("위시리스트 추가 실패 : 존재하지 않는 상품")
    @Test
    void addWishFailNoSuchProduct() {
        // given
        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(productRepository.findByPk(productPk)).thenReturn(Optional.empty());

        // when + then
        PinkCatException e = assertThrows(PinkCatException.class,
            () -> wishService.addWishlist(userPk, productPk));

        assertEquals(ErrorMessageCode.NO_SUCH_PRODUCT, e.getErrorMessageCode());
    }

    @DisplayName("위시리스트 추가 실패 : 이미 위시리스트에 존재하는 상품")
    @Test
    void addWishFailAlreadyExists() {
        // given
        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(productRepository.findByPk(productPk)).thenReturn(Optional.of(product));
        when(customerProductWishRepository.findCustomerProductWishEntityByProductAndCustomer(product, customer))
            .thenReturn(Optional.of(CustomerProductWishEntity.builder().build()));

        // when
        AddWishResponseDTO result = wishService.addWishlist(userPk, productPk);

        // then
        assertEquals("이미 위시리스트에 존재하는 상품입니다.", result.getResult());
        verify(customerProductWishRepository, never()).save(any());
    }

    @DisplayName("위시리스트 삭제 성공")
    @Test
    void deleteWishSuccess() {
        // given
        DeleteWishlistRequestDTO request = DeleteWishlistRequestDTO.builder()
            .wishId(wishPk)
            .build();

        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(wishRepository.findByPk(wishPk)).thenReturn(Optional.of(wish));

        // when
        boolean result = wishService.deleteWishlist(userPk, request);

        // then
        assertTrue(result);
        verify(wishRepository).delete(wish);
    }

    @DisplayName("위시리스트 삭제 실패 : 존재하지 않는 위시 상품")
    @Test
    void deleteWishFailNoSuchWish() {
        // given
        DeleteWishlistRequestDTO request = DeleteWishlistRequestDTO.builder()
            .wishId(wishPk)
            .build();

        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(wishRepository.findByPk(wishPk)).thenReturn(Optional.empty());

        // when & then
        PinkCatException e = assertThrows(PinkCatException.class, () -> {
            wishService.deleteWishlist(userPk, request);
        });

        assertEquals(ErrorMessageCode.NO_SUCH_WISH_ITEM, e.getErrorMessageCode());
        verify(wishRepository, never()).delete(any());
    }

    @AfterEach
    void tearDown(){
        customerRepository.deleteAll();
        wishRepository.deleteAll();
        productImageRepository.deleteAll();
        productRepository.deleteAll();
        customerProductWishRepository.deleteAll();
    }
}
