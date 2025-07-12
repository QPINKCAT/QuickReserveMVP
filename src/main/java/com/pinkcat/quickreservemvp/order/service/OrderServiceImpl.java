package com.pinkcat.quickreservemvp.order.service;

import com.pinkcat.quickreservemvp.category.entity.CategoryEntity;
import com.pinkcat.quickreservemvp.category.repository.CategoryRepository;
import com.pinkcat.quickreservemvp.common.exceptions.ErrorMessageCode;
import com.pinkcat.quickreservemvp.common.exceptions.PinkCatException;
import com.pinkcat.quickreservemvp.customer.repository.CustomerRepository;
import com.pinkcat.quickreservemvp.order.dto.OrderListResponseDTO;
import com.pinkcat.quickreservemvp.order.dto.OrderListResponseDTO.Order;
import com.pinkcat.quickreservemvp.order.dto.OrderListResponseDTO.Item;
import com.pinkcat.quickreservemvp.order.repository.OrderCustomRepository;
import com.pinkcat.quickreservemvp.order.repository.OrderItemRepository;
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
            List<Item> items = orderItemRepository.findByOrderNum(o.getOrderNum()).stream().map(i -> {
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
                        .discountPrice(i.getSaledPrice())
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

    private LocalDateTime stringToDate(String date){
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8, 10));
        return LocalDateTime.of(year,month,day,0,0,0);
    }
}
