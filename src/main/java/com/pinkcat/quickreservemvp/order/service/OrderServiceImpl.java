package com.pinkcat.quickreservemvp.order.service;

import com.pinkcat.quickreservemvp.category.entity.CategoryEntity;
import com.pinkcat.quickreservemvp.category.repository.CategoryRepository;
import com.pinkcat.quickreservemvp.common.enums.OrderStatusEnum;
import com.pinkcat.quickreservemvp.common.enums.PaymentStatusEnum;
import com.pinkcat.quickreservemvp.common.exceptions.ErrorMessageCode;
import com.pinkcat.quickreservemvp.common.exceptions.PinkCatException;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import com.pinkcat.quickreservemvp.customer.repository.CustomerRepository;
import com.pinkcat.quickreservemvp.order.dto.CancelOrderRequestDTO;
import com.pinkcat.quickreservemvp.order.dto.CancelOrderResponseDTO;
import com.pinkcat.quickreservemvp.order.dto.CancelOrderResponseDTO.Refund;
import com.pinkcat.quickreservemvp.order.dto.OrderListResponseDTO;
import com.pinkcat.quickreservemvp.order.dto.OrderListResponseDTO.Order;
import com.pinkcat.quickreservemvp.order.dto.OrderListResponseDTO.Item;
import com.pinkcat.quickreservemvp.order.dto.OrderResponseDTO;
import com.pinkcat.quickreservemvp.order.dto.OrderResponseDTO.Customer;
import com.pinkcat.quickreservemvp.order.dto.OrderResponseDTO.OrderItem;
import com.pinkcat.quickreservemvp.order.dto.OrderResponseDTO.Product;
import com.pinkcat.quickreservemvp.order.dto.OrderResponseDTO.Payment;
import com.pinkcat.quickreservemvp.order.entity.OrderEntity;
import com.pinkcat.quickreservemvp.order.entity.OrderItemEntity;
import com.pinkcat.quickreservemvp.order.repository.OrderCustomRepository;
import com.pinkcat.quickreservemvp.order.repository.OrderItemRepository;
import com.pinkcat.quickreservemvp.order.repository.OrderRepository;
import com.pinkcat.quickreservemvp.payment.entity.PaymentEntity;
import com.pinkcat.quickreservemvp.payment.repository.PaymentRepository;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import com.pinkcat.quickreservemvp.product.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final CustomerRepository customerRepository;
    private final OrderCustomRepository orderCustomRepository;
    private final OrderItemRepository orderItemRepository;
    private final CategoryRepository categoryRepository;
    private final PaymentRepository paymentRepository;
    private final ProductImageRepository productImageRepository;
    private final OrderRepository orderRepository;

    @Override
    public OrderListResponseDTO getOrderList(Long userPk, Integer page, Integer size, String start, String end){

        // [1] 고객 유효성 검사
        customerRepository.findByPkAndActiveTrue(userPk).orElseThrow(() ->
                new PinkCatException("비활성화된 계정입니다. 관리자에게 문의해주세요.", ErrorMessageCode.CUSTOMER_INACTIVE));

        Pageable pageable = PageRequest.of(page-1, size);

        // [2] 고객 id, start ~ end 기간 내 주문건들 찾기

        LocalDateTime startAt = start == null ? null : stringToDate(start);
        LocalDateTime endAt = end == null ? null : stringToDate(end);
        Page<Order> orderList = orderCustomRepository.findOrdersByCustomerIdAndStartEnd(userPk, startAt, endAt, pageable);

        // [3] 주문 pk로 product_order_item 찾고, product_id 로 상품 정보 찾기 + discount price 찾기
        List<Order> orders = orderList.stream().peek(o -> {
            PaymentEntity payment = paymentRepository.findByOrderNum(o.getOrderNum()).orElseThrow(() ->
                    new PinkCatException("존재하지 않는 주문입니다", ErrorMessageCode.NO_SUCH_ORDER));

            o.setPaymentStatus(payment.getStatus());
            List<Item> items = orderItemRepository.findAllByOrderNum(o.getOrderNum()).stream().map(i -> {
                ProductEntity product = i.getProduct();
                CategoryEntity category = categoryRepository.findCategoryEntityByProduct(product).orElseThrow(()->
                        new PinkCatException("존재하지 않는 카테고리입니다.", ErrorMessageCode.NO_SUCH_CATEGORY));
                String thumbnail = productImageRepository.findThumbnailByProductPk(product.getPk()).orElse(null);
                return Item.builder()
                        .categoryId(category.getPk())
                        .categoryName(category.getName())
                        .productId(product.getPk())
                        .productName(product.getProductName())
                        .thumbnail(thumbnail)
                        .originalPrice(i.getOriginalPrice())
                        .discountPrice(i.getSalePrice())
                        .quantity(i.getQuantity())
                        .build();
            }).toList();
            o.setItems(items);
        }).toList();


        return OrderListResponseDTO.builder()
                .page(page)
                .size(size)
                .totalPages(orderList.getTotalPages())
                .orders(orders)
                .build();
    }

    @Override
    public OrderResponseDTO getOrder(Long userPk, String orderNum) {

        // [1] 유효성 검사
        CustomerEntity customerEntity = customerRepository.findByPkAndActiveTrue(userPk).orElseThrow(() ->
                new PinkCatException("비활성화된 계정입니다. 관리자에게 문의해주세요.", ErrorMessageCode.CUSTOMER_INACTIVE));

        OrderEntity order = orderRepository.findByOrderNum(orderNum).orElseThrow(() ->
                new PinkCatException("존재하지 않는 주문입니다.", ErrorMessageCode.NO_SUCH_ORDER));

        // [2] 주문 상세 내역 조회

        Customer customer = Customer.builder()
                .customerId(customerEntity.getId())
                .name(customerEntity.getName())
                .phoneNumber(customerEntity.getPhoneNumber())
                .email(customerEntity.getEmail())
                .build();

        List<OrderItem> orderItems = orderItemRepository.findAllByOrderNum(orderNum).stream().map(oi -> {
            ProductEntity productEntity = oi.getProduct();
            Product product = Product.builder()
                    .productId(productEntity.getPk())
                    .name(productEntity.getProductName())
                    .description(productEntity.getProductDescription())
                    .price(productEntity.getPrice())
                    .status(productEntity.getProductStatus().name())
                    .build();

            PaymentEntity paymentEntity = paymentRepository.findByOrderNum(order.getOrderNum()).orElseThrow(() ->
                    new PinkCatException("결제건이 존재하지 않습니다.", ErrorMessageCode.NO_SUCH_PAYMENT));
            Payment payment = Payment.builder()
                    .paymentId(paymentEntity.getPk())
                    .status(paymentEntity.getStatus().name())
                    .totalPrice(paymentEntity.getTotalPrice())
                    .createdAt(paymentEntity.getCreatedAt())
                    .updatedAt(paymentEntity.getUpdatedAt())
                    .build();

            int sumPrice = oi.getSalePrice() == null ? oi.getOriginalPrice() * oi.getQuantity() : oi.getSalePrice() * oi.getQuantity();

            return OrderItem.builder()
                    .orderItemId(oi.getPk())
                    .sumPrice(sumPrice)
                    .originalPrice(oi.getOriginalPrice())
                    .salePrice(oi.getSalePrice())
                    .quantity(oi.getQuantity())
                    .status(oi.getStatus().name())
                    .product(product)
                    .payment(payment)
                    .build();
        }).toList();


        return OrderResponseDTO.builder()
                .orderId(order.getPk())
                .orderNum(order.getOrderNum())
                .createdAt(order.getCreatedAt())
                .customer(customer)
                .orderItems(orderItems)
                .build();
    }

    @Override
    @Transactional
    public CancelOrderResponseDTO cancelOrder(Long userPk, String orderNum, CancelOrderRequestDTO request) {

        // [1] 유효성 검사
        customerRepository.findByPkAndActiveTrue(userPk).orElseThrow(() ->
                new PinkCatException("비활성화된 계정입니다. 관리자에게 문의해주세요.", ErrorMessageCode.CUSTOMER_INACTIVE));

        OrderEntity order = orderRepository.findByOrderNum(orderNum).orElseThrow(() ->
                new PinkCatException("존재하지 않는 주문입니다.", ErrorMessageCode.NO_SUCH_ORDER));

        // [2] 주문 취소
        AtomicInteger totalRefundedPrice = new AtomicInteger(); // 멀티스레딩 환경에서 race condition 해결을 위해 AtomicInteger 타입으로 지정
        // 주문아이템 id 유효성 검사
        List<Refund> refunds = request.getOrderItemIds().stream().map(e -> {
            OrderItemEntity orderItem = orderItemRepository.findByPk(e).orElseThrow(() ->
                    new PinkCatException("존재하지 않는 주문 상품입니다.", ErrorMessageCode.NO_SUCH_ORDER_ITEM));

            int refundAmountPerItem = orderItem.getSalePrice() == null ? orderItem.getOriginalPrice() * orderItem.getQuantity()
                    : orderItem.getSalePrice() * orderItem.getQuantity();
            totalRefundedPrice.addAndGet(refundAmountPerItem);

            orderItem.setStatus(OrderStatusEnum.CANCELLED);
            orderItem.setActive(false);
            orderItemRepository.save(orderItem);

            return Refund.builder()
                    .orderItemId(orderItem.getPk())
                    .refundedPrice(totalRefundedPrice.get())
                    .build();
        }).toList();

        // [3] 결제 취소 처리
        PaymentEntity payment = paymentRepository.findByOrderPk(order.getPk()).orElseThrow(()->
                new PinkCatException("결제건이 존재하지 않습니다", ErrorMessageCode.NO_SUCH_PAYMENT));
        payment.setStatus(PaymentStatusEnum.CANCELLED);
        payment.setTotalPrice(payment.getTotalPrice() - totalRefundedPrice.get());
        payment = paymentRepository.save(payment);

        return CancelOrderResponseDTO.builder()
                .orderId(order.getPk())
                .totalRefundedPrice(totalRefundedPrice.get())
                .refundedAt(payment.getUpdatedAt())
                .refunds(refunds)
                .build();
    }

    private LocalDateTime stringToDate(String date){
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8, 10));
        return LocalDateTime.of(year,month,day,0,0,0);
    }
}
