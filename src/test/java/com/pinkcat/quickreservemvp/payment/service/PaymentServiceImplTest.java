package com.pinkcat.quickreservemvp.payment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pinkcat.quickreservemvp.common.exceptions.ErrorMessageCode;
import com.pinkcat.quickreservemvp.common.exceptions.PinkCatException;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import com.pinkcat.quickreservemvp.customer.repository.CustomerRepository;
import com.pinkcat.quickreservemvp.order.repository.OrderItemRepository;
import com.pinkcat.quickreservemvp.order.repository.OrderRepository;
import com.pinkcat.quickreservemvp.payment.dto.PaymentRequestDTO;
import com.pinkcat.quickreservemvp.payment.repository.PaymentRepository;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import com.pinkcat.quickreservemvp.product.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    private CustomerEntity customer;
    private ProductEntity product;

    @BeforeEach
    void setUp() {
        customer = CustomerEntity.builder().build();

        product = ProductEntity.builder()
            .stock(10)
            .price(10000)
            .saleStartAt(LocalDateTime.now().minusDays(10))
            .saleEndAt(LocalDateTime.now().plusDays(10))
            .build();
    }

    private PaymentRequestDTO.Item buildItem(int quantity) {
        return PaymentRequestDTO.Item.builder()
            .productId(1L)
            .cartItemId(999L)
            .productName("테스트상품")
            .unitPrice(10000)
            .quantity(quantity)
            .sumPrice(quantity * 10000)
            .discountPrice(0)
            .build();
    }

    @DisplayName("결제 성공")
    @Test
    void paymentSuccess() {
        // given
        PaymentRequestDTO.Item item = buildItem(2);
        PaymentRequestDTO request = PaymentRequestDTO.builder()
            .totalPrice(20000)
            .items(List.of(item))
            .build();

        when(customerRepository.findByPkAndActiveTrue(any())).thenReturn(Optional.of(customer));
        when(productRepository.findByPk(any())).thenReturn(Optional.of(product));
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(productRepository.findByPk(item.getProductId())).thenReturn(Optional.of(product));
        when(orderRepository.existsByOrderNum(any())).thenReturn(false);

        // when
        String orderNumber = paymentService.payment(1L, request);

        // then
        assertNotNull(orderNumber);
        assertTrue(orderNumber.startsWith("O"));
        verify(paymentRepository).save(any());
        verify(orderItemRepository).save(any());
    }

    @DisplayName("결제 실패 : 재고 부족")
    @Test
    void paymentFailOutOfStock() {
        // given
        product = ProductEntity.builder()
            .stock(1)
            .build();
        product.setPk(1L);

        PaymentRequestDTO.Item item = buildItem(3);
        PaymentRequestDTO request = PaymentRequestDTO.builder()
            .totalPrice(30000)
            .items(List.of(item))
            .build();

        when(customerRepository.findByPkAndActiveTrue(any())).thenReturn(Optional.of(customer));
        when(productRepository.findByPk(any())).thenReturn(Optional.of(product));

        // when & then
        PinkCatException e = assertThrows(PinkCatException.class, () -> {
            paymentService.payment(1L, request);
        });

        assertEquals(ErrorMessageCode.OUT_OF_STOCK, e.getErrorMessageCode());
    }

    @DisplayName("결제 실패 : 판매중이 아닌 상품")
    @Test
    void paymentFailProductUnavailable() {
        // given
        product = ProductEntity.builder()
            .stock(10)
            .saleStartAt(LocalDateTime.now().plusDays(1))
            .saleEndAt(LocalDateTime.now().plusDays(10))
            .build();

        PaymentRequestDTO.Item item = buildItem(1);
        PaymentRequestDTO request = PaymentRequestDTO.builder()
            .totalPrice(10000)
            .items(List.of(item))
            .build();

        when(customerRepository.findByPkAndActiveTrue(any())).thenReturn(Optional.of(customer));
        when(productRepository.findByPk(any())).thenReturn(Optional.of(product));

        // when & then
        PinkCatException e = assertThrows(PinkCatException.class, () -> {
            paymentService.payment(1L, request);
        });

        assertEquals(ErrorMessageCode.PRODUCT_UNAVAILABLE, e.getErrorMessageCode());
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
        productRepository.deleteAll();
        paymentRepository.deleteAll();
        orderRepository.deleteAll();
        orderItemRepository.deleteAll();
    }
}

