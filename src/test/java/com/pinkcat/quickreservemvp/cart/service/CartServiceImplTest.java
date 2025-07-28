package com.pinkcat.quickreservemvp.cart.service;

import com.pinkcat.quickreservemvp.cart.dto.CartItemListResponseDTO;
import com.pinkcat.quickreservemvp.cart.dto.DeleteCartItemRequestDTO;
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
import com.pinkcat.quickreservemvp.product.repository.DiscountRepository;
import com.pinkcat.quickreservemvp.product.repository.ProductImageRepository;
import com.pinkcat.quickreservemvp.product.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
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
    @Mock
    private DiscountRepository discountRepository;
    @Mock
    private ProductImageRepository productImageRepository;

    private CustomerEntity customer;
    private ProductEntity product;
    private CartEntity cart;

    private final List<CartEntity> cartItems = new ArrayList<>();
    private final long customerPk = 1L;
    private long cartPk = 1L;

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

        cart = CartEntity.builder()
            .product(product)
            .customer(customer)
            .quantity(2)
            .build();
        cart.setPk(10L);
        cartItems.add(cart);
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
    void updateCartFailQuantityLessThanZero() {
        UpdateCartItemRequestDTO request = new UpdateCartItemRequestDTO();
        ReflectionTestUtils.setField(request, "cartItemId", 10L);
        ReflectionTestUtils.setField(request, "quantity", 0);

        when(cartRepository.findCartEntityByPk(10L)).thenReturn(Optional.of(cart));

        PinkCatException e = assertThrows(PinkCatException.class, () ->
                cartService.updateCart(1L, request));

        assertEquals("장바구니 상품의 최소 수량은 1개입니다.", e.getMessage());
    }

    @DisplayName("장바구니 아이템 조회 성공")
    @Test
    void getCartSuccess() {

        when(cartRepository.findCartEntitiesByCustomerPk(customerPk)).thenReturn(cartItems);

        // when
        CartItemListResponseDTO response = cartService.getCart(customerPk);

        // then
        assertNotNull(response);
        assertEquals(1, response.getItems().size());
        assertEquals("테스트상품", response.getItems().get(0).getName());
        assertEquals(2, response.getItems().get(0).getQuantity());
    }

    @DisplayName("장바구니 아이템 삭제 성공")
    @Test
    void deleteCartSuccess() {
        DeleteCartItemRequestDTO request = DeleteCartItemRequestDTO.builder()
            .cartItemId(cartPk)
            .build();

        when(cartRepository.findCartEntityByPk(cartPk)).thenReturn(Optional.of(cart));

        // when
        cartService.deleteCart(customerPk, request);

        // then
        verify(cartRepository, times(1)).delete(cart);
    }

    @DisplayName("장바구니 아이템 삭제 실패 : 존재하지 않는 상품")
    @Test
    void deleteCartFailNoSuchCartItem() {
        // given
        cartPk = 999L;
        when(cartRepository.findCartEntityByPk(cartPk)).thenReturn(Optional.empty());

        // when
        DeleteCartItemRequestDTO request = DeleteCartItemRequestDTO.builder()
            .cartItemId(cartPk)
            .build();

        // then
        PinkCatException e = assertThrows(PinkCatException.class, () ->
            cartService.deleteCart(1L, request));

        assertEquals("존재하지 않는 장바구니 상품입니다.", e.getMessage());
    }

    @AfterEach
    void tearDown(){
        customerRepository.deleteAll();
        cartRepository.deleteAll();
        productRepository.deleteAll();
        discountRepository.deleteAll();
        productImageRepository.deleteAll();
    }
}
