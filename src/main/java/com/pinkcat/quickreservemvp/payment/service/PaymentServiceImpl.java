package com.pinkcat.quickreservemvp.payment.service;

import com.pinkcat.quickreservemvp.common.enums.OrderStatusEnum;
import com.pinkcat.quickreservemvp.common.enums.PaymentStatusEnum;
import com.pinkcat.quickreservemvp.common.exceptions.ErrorMessageCode;
import com.pinkcat.quickreservemvp.common.exceptions.PinkCatException;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import com.pinkcat.quickreservemvp.customer.repository.CustomerRepository;
import com.pinkcat.quickreservemvp.order.entity.OrderEntity;
import com.pinkcat.quickreservemvp.order.entity.OrderItemEntity;
import com.pinkcat.quickreservemvp.order.repository.OrderItemRepository;
import com.pinkcat.quickreservemvp.order.repository.OrderRepository;
import com.pinkcat.quickreservemvp.payment.dto.PaymentRequestDTO;
import com.pinkcat.quickreservemvp.payment.dto.PaymentRequestDTO.Item;
import com.pinkcat.quickreservemvp.payment.entity.PaymentEntity;
import com.pinkcat.quickreservemvp.payment.repository.PaymentRepository;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import com.pinkcat.quickreservemvp.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public String payment(Long userId, PaymentRequestDTO request) {
        CustomerEntity customer = customerRepository.findByPkAndActiveTrue(userId).orElseThrow(() ->
                new PinkCatException("비활성화된 계정입니다. 관리자에게 문의해주세요.", ErrorMessageCode.CUSTOMER_INACTIVE));

        List<Item> items = request.getItems();

        items.forEach(item -> {
            ProductEntity product = productRepository.findByPk(item.getProductId()).orElseThrow(() ->
                    new PinkCatException("존재하지 않는 상품입니다", ErrorMessageCode.NO_SUCH_PRODUCT));

            // [1] 결제 불가
            // 상품 재고가 부족한 경우
            if (product.getStock() < item.getQuantity()) throw new PinkCatException("재고가 부족합니다", ErrorMessageCode.OUT_OF_STOCK);
            // 상품 판매기간이 지난 경우
            if (product.getSaleStartAt() != null && !isAvailable(product.getSaleStartAt(), product.getSaleEndAt())) {
                throw new PinkCatException("현재 구매가 불가능한 상품입니다.", ErrorMessageCode.PRODUCT_UNAVAILABLE);
            }
        });

        // [2] 정상 처리 시
        // 주문번호를 채번한다
        String orderNumber = generateOrderNumber();

        // 주문 내역 저장
        OrderEntity order = orderRepository.save(OrderEntity.builder()
                .customer(customer)
                .orderNum(orderNumber)
                .build());

        for (Item item : items) {
            // 주문 아이템을 등록한다
            ProductEntity product = productRepository.findByPk(item.getProductId()).orElseThrow(() ->
                    new PinkCatException("존재하지 않는 상품입니다", ErrorMessageCode.NO_SUCH_PRODUCT));

            orderItemRepository.save(
                OrderItemEntity.builder()
                    .order(order)
                    .product(product)
                    .originalPrice(product.getPrice())
                    .status(OrderStatusEnum.COMPLETED)
                    .salePrice(item.getDiscountPrice())
                    .quantity(item.getQuantity())
                    .build());

            // 장바구니 아이템 목록에서 결제된 아이템을 삭제한다
            customerRepository.deleteById(item.getCartItemId());
        }

        // 결제 내역 저장
        paymentRepository.save(PaymentEntity.builder()
                .order(order)
                .status(PaymentStatusEnum.COMPLETED)
                .totalPrice(request.getTotalPrice())
                .build());

        return orderNumber;
    }

    private boolean isAvailable(LocalDateTime start, LocalDateTime end) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        return start.isBefore(now) && end.isAfter(now);
    }

    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Integer LENGTH = 10;
    private String generateOrderNumber() {
        StringBuilder sb = new StringBuilder();
        sb.append("O");
        Random random = new Random();
        do {
            for (int i = 0; i < LENGTH-1; i++) {
                int randomIdx = random.nextInt(CHARACTERS.length());
                char ch = CHARACTERS.charAt(randomIdx);
                sb.append(ch);
            }
        } while (orderRepository.existsByOrderNum(sb.toString()));

        return sb.toString();
    }
}
