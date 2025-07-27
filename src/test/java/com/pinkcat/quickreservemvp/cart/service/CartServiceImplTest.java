package com.pinkcat.quickreservemvp.cart.service;

import com.pinkcat.quickreservemvp.cart.dto.UpdateCartItemRequestDTO;
import com.pinkcat.quickreservemvp.cart.entity.CartEntity;
import com.pinkcat.quickreservemvp.cart.repository.CartRepository;
import com.pinkcat.quickreservemvp.common.enums.ProductStatusEnum;
import com.pinkcat.quickreservemvp.common.exceptions.PinkCatException;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import com.pinkcat.quickreservemvp.customer.repository.CustomerRepository;
import com.pinkcat.quickreservemvp.product.dto.AddCartRequestDTO;
import com.pinkcat.quickreservemvp.product.dto.AddCartResponseDTO;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import com.pinkcat.quickreservemvp.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ProductRepository productRepository;

    private CustomerEntity customer;
    private ProductEntity product;

    @BeforeEach
    void setUp() {
        customer = CustomerEntity.builder().id("customer1").build();
        customer.setPk(1L);

        product = ProductEntity.builder()
                .productName("테스트상품")
                .price(10000)
                .productStatus(ProductStatusEnum.ON)
                .build();
        product.setPk(1L);
    }

    @DisplayName("장바구니 상품 추가 성공")
    @Test
    void addCartSuccess() {
        AddCartRequestDTO request = AddCartRequestDTO.builder()
                .quantity(2).build();

        when(customerRepository.findByPkAndActiveTrue(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findByPk(1L)).thenReturn(Optional.of(product));
        when(cartRepository.findCartEntityByCustomerAndProduct(customer, product)).thenReturn(Optional.empty());

        AddCartResponseDTO response = cartService.addCart(customer.getPk(), product.getPk(), request);

        assertThat(response.getResult()).isEqualTo("장바구니에 상품을 추가했습니다.");
    }

    @DisplayName("장바구니 수량 수정 성공")
    @Test
    void updateCartSuccess() {
        AddCartRequestDTO request = AddCartRequestDTO.builder()
                .quantity(3).build();

        CartEntity cart = CartEntity.builder().product(product).customer(customer).quantity(2).build();

        when(customerRepository.findByPkAndActiveTrue(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findByPk(1L)).thenReturn(Optional.of(product));
        when(cartRepository.findCartEntityByCustomerAndProduct(customer, product)).thenReturn(Optional.of(cart));

        AddCartResponseDTO response = cartService.addCart(1L, 1L, request);

        assertEquals("상품 수량이 수정되었습니다.", response.getResult());
        assertEquals(5, cart.getQuantity());
        verify(cartRepository).save(cart);
    }

    @DisplayName("장바구니 수량 0 미만으로 수정시 실패")
    @Test
    void updateCart_whenQuantityIsZero_thenThrowException() {
        UpdateCartItemRequestDTO request = new UpdateCartItemRequestDTO();
        ReflectionTestUtils.setField(request, "cartItemId", 10L);
        ReflectionTestUtils.setField(request, "quantity", 0);

        CartEntity cart = CartEntity.builder().product(product).customer(customer).quantity(2).build();
        cart.setPk(10L);
        when(cartRepository.findCartEntityByPk(10L)).thenReturn(Optional.of(cart));

        PinkCatException e = assertThrows(PinkCatException.class, () ->
                cartService.updateCart(1L, request));

        assertEquals("장바구니 상품의 최소 수량은 1개입니다.", e.getMessage());
    }
}
