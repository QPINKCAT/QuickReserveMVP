package com.pinkcat.quickreservemvp.order.service;

import com.pinkcat.quickreservemvp.common.enums.OrderStatusEnum;
import com.pinkcat.quickreservemvp.common.enums.PaymentStatusEnum;
import com.pinkcat.quickreservemvp.common.enums.ProductStatusEnum;
import com.pinkcat.quickreservemvp.common.exceptions.ErrorMessageCode;
import com.pinkcat.quickreservemvp.common.exceptions.PinkCatException;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import com.pinkcat.quickreservemvp.customer.repository.CustomerRepository;
import com.pinkcat.quickreservemvp.order.dto.CancelOrderRequestDTO;
import com.pinkcat.quickreservemvp.order.dto.CancelOrderResponseDTO;
import com.pinkcat.quickreservemvp.order.dto.OrderListResponseDTO;
import com.pinkcat.quickreservemvp.order.dto.OrderResponseDTO;
import com.pinkcat.quickreservemvp.order.entity.OrderEntity;
import com.pinkcat.quickreservemvp.order.entity.OrderItemEntity;
import com.pinkcat.quickreservemvp.order.repository.*;
import com.pinkcat.quickreservemvp.payment.entity.PaymentEntity;
import com.pinkcat.quickreservemvp.payment.repository.PaymentRepository;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock private CustomerRepository customerRepository;
    @Mock private OrderCustomRepository orderCustomRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private PaymentRepository paymentRepository;
    @Mock private OrderRepository orderRepository;

    private final Long userPk = 1L;
    private final String orderNum = "ORDER12345";

    private CustomerEntity customer;
    private OrderEntity order;
    private PaymentEntity payment;

    @BeforeEach
    void setup() {
        customer = CustomerEntity.builder()
            .id("user1")
            .name("김고객")
            .build();
        customer.setPk(userPk);

        order = OrderEntity.builder()
            .orderNum(orderNum)
            .customer(customer)
            .build();
        order.setPk(100L);
    }

    @Test
    @DisplayName("주문 목록 조회 성공")
    void getOrderListSuccess() {
        // when
        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(orderCustomRepository.findOrdersByCustomerIdAndStartEnd(eq(userPk), any(), any(), any(Pageable.class)))
            .thenReturn(new PageImpl<>(Collections.emptyList()));

        // then
        OrderListResponseDTO result = orderService.getOrderList(userPk, 1, 10, null, null);
        assertEquals(1, result.getPage());
        assertEquals(10, result.getSize());
        assertTrue(result.getOrders().isEmpty());
    }

    @Test
    @DisplayName("주문 상세 조회 성공")
    void getOrderSuccess() {
        payment = PaymentEntity.builder()
            .totalPrice(5000)
            .status(PaymentStatusEnum.COMPLETED)
            .order(order)
            .build();
        payment.setPk(1L);

        System.out.println(payment.getStatus());

        // when
        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(orderRepository.findByOrderNum(orderNum)).thenReturn(Optional.of(order));
        when(orderItemRepository.findAllByOrderNum(orderNum)).thenReturn(createOrderItems(order));
        when(paymentRepository.findByOrderNum(orderNum)).thenReturn(Optional.of(payment));
        // then
        OrderResponseDTO result = orderService.getOrder(userPk, orderNum);
        assertEquals(orderNum, result.getOrderNum());
        assertEquals(10, result.getOrderItems().size());
    }

    @Test
    @DisplayName("주문 상세 조회 실패 : 존재하지 않는 주문")
    void getOrderFailNoSuchOrder() {
        // when
        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(orderRepository.findByOrderNum(orderNum)).thenReturn(Optional.empty());

        // then
        PinkCatException ex = assertThrows(PinkCatException.class, () ->
            orderService.getOrder(userPk, orderNum));
        assertEquals(ErrorMessageCode.NO_SUCH_ORDER, ex.getErrorMessageCode());
    }

    @Test
    @DisplayName("주문 취소 성공")
    void cancelOrderSuccess() {

        OrderItemEntity orderItem = OrderItemEntity.builder()
            .originalPrice(2000)
            .salePrice(1500)
            .quantity(2)
            .status(OrderStatusEnum.COMPLETED)
            .order(order)
            .build();
        orderItem.setPk(101L);

        payment = PaymentEntity.builder()
            .totalPrice(5000)
            .status(PaymentStatusEnum.COMPLETED)
            .order(order)
            .build();
        payment.setPk(1L);

        when(paymentRepository.save(any(PaymentEntity.class))).thenAnswer(i
            -> i.getArgument(0));

        CancelOrderRequestDTO request = CancelOrderRequestDTO.builder()
            .orderItemIds(List.of(101L))
            .build();

        // when
        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(orderRepository.findByOrderNum(orderNum)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByPk(101L)).thenReturn(Optional.of(orderItem));
        when(paymentRepository.findByOrderPk(order.getPk())).thenReturn(Optional.of(payment));

        // then
        CancelOrderResponseDTO response = orderService.cancelOrder(userPk, orderNum, request);
        assertEquals(order.getPk(), response.getOrderId());
        assertEquals(3000, response.getTotalRefundedPrice());
    }


    @Test
    @DisplayName("주문 취소 실패 : 존재하지 않는 주문")
    void cancelOrderFailNoSuchOrder() {
        // given
        CancelOrderRequestDTO request = CancelOrderRequestDTO.builder()
            .orderItemIds(List.of(1L))
            .build();

        // when
        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(orderRepository.findByOrderNum(orderNum)).thenReturn(Optional.empty());

        // then
        PinkCatException ex = assertThrows(PinkCatException.class, () ->
            orderService.cancelOrder(userPk, orderNum, request));
        assertEquals(ErrorMessageCode.NO_SUCH_ORDER, ex.getErrorMessageCode());
    }

    @Test
    @DisplayName("주문 취소 실패 : 존재하지 않는 주문 상품")
    void cancelOrderFailNoSuchOrderItem() {
        // given
        CancelOrderRequestDTO request = CancelOrderRequestDTO.builder()
            .orderItemIds(List.of(1L))
            .build();

        // when
        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(orderRepository.findByOrderNum(orderNum)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByPk(1L)).thenReturn(Optional.empty());

        // then
        PinkCatException ex = assertThrows(PinkCatException.class, () ->
            orderService.cancelOrder(userPk, orderNum, request));
        assertEquals(ErrorMessageCode.NO_SUCH_ORDER_ITEM, ex.getErrorMessageCode());
    }


    @Test
    @DisplayName("주문 취소 실패 : 결제 정보 없음")
    void cancelOrderFailNoSuchPayment() {
        // given
        OrderItemEntity item = OrderItemEntity.builder()
            .originalPrice(1000)
            .salePrice(800)
            .quantity(1)
            .status(OrderStatusEnum.COMPLETED)
            .build();
        item.setPk(1L);

        CancelOrderRequestDTO request = CancelOrderRequestDTO.builder()
            .orderItemIds(List.of(1L))
            .build();

        // when
        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(orderRepository.findByOrderNum(orderNum)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByPk(1L)).thenReturn(Optional.of(item));
        when(paymentRepository.findByOrderPk(order.getPk())).thenReturn(Optional.empty());

        // then
        PinkCatException ex = assertThrows(PinkCatException.class, () ->
            orderService.cancelOrder(userPk, orderNum, request));
        assertEquals(ErrorMessageCode.NO_SUCH_PAYMENT, ex.getErrorMessageCode());
    }


    private List<OrderItemEntity> createOrderItems(OrderEntity order) {
        List<OrderItemEntity> orderItems = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            ProductEntity product = ProductEntity.builder()
                .productName("테스트상품" + i)
                .price(80000)
                .productStatus(ProductStatusEnum.ON)
                .reviewCnt(5)
                .avgRating(8.2F)
                .stock(100)
                .build();
            product.setPk((long) i);

            OrderItemEntity orderItem = OrderItemEntity.builder()
                .product(product)
                .order(order)
                .quantity(i)
                .originalPrice(product.getPrice())
                .status(OrderStatusEnum.COMPLETED)
                .build();
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
        orderRepository.deleteAll();
        orderItemRepository.deleteAll();
        paymentRepository.deleteAll();
    }
}
