package com.pinkcat.quickreservemvp.review.service;

import com.pinkcat.quickreservemvp.common.enums.OrderStatusEnum;
import com.pinkcat.quickreservemvp.common.enums.ProductStatusEnum;
import com.pinkcat.quickreservemvp.common.exceptions.ErrorMessageCode;
import com.pinkcat.quickreservemvp.common.exceptions.PinkCatException;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import com.pinkcat.quickreservemvp.customer.repository.CustomerRepository;
import com.pinkcat.quickreservemvp.order.entity.OrderItemEntity;
import com.pinkcat.quickreservemvp.order.repository.OrderItemRepository;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import com.pinkcat.quickreservemvp.product.repository.ProductRepository;
import com.pinkcat.quickreservemvp.review.dto.WriteReviewRequestDTO;
import com.pinkcat.quickreservemvp.review.dto.WriteReviewResponseDTO;
import com.pinkcat.quickreservemvp.review.entity.ReviewEntity;
import com.pinkcat.quickreservemvp.review.repository.ReviewRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService{

    private final CustomerRepository customerRepository;
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    /*
        1. 로그인 한 & 상품 구매를 확정한(=order_item > status > used) 유저가 상품 리뷰를 등록한다
            1. 위 조건을 충족하지 않을 경우 리뷰를 작성할 수 없으며, 리뷰 작성 버튼을 노출하지 않는다
        2. 리뷰 등록 시
            1. 평점만 등록하는 것도 가능하다 (= 리뷰는 필수가 아님)
            2. 글은 300자 이내로 작성해야 한다
        3. 상품 구매 확정(=used) 후 2주 이내만 등록 가능
        4. 등록 후 전체 평점에 평점이 반영 되어야한다
     */

    @Override
    @Transactional
    public WriteReviewResponseDTO addReview(Long userPk, Long orderItemId, WriteReviewRequestDTO request){

        CustomerEntity customer = customerRepository.findByPkAndActiveTrue(userPk).orElseThrow(() ->
            new PinkCatException("비활성화된 계정입니다. 관리자에게 문의해주세요.", ErrorMessageCode.CUSTOMER_INACTIVE));

        OrderItemEntity orderItem = orderItemRepository.findByPk(orderItemId).orElseThrow(() ->
            new PinkCatException("존재하지 않는 주문 상품입니다.", ErrorMessageCode.NO_SUCH_ORDER_ITEM));

        ProductEntity product = productRepository.findByPk(request.getProductId()).orElseThrow(() ->
            new PinkCatException("존재하지 않는 상품입니다.", ErrorMessageCode.NO_SUCH_PRODUCT));

        if (product.getProductStatus() == ProductStatusEnum.OFF){
            return WriteReviewResponseDTO.builder()
                .result("판매중지된 상품의 리뷰를 작성할 수 없습니다.")
                .build();
        }

        // 리뷰를 두 개 이상 쓸 수 없음
        if (reviewRepository.findByOrderItemAndCustomer(orderItem, customer).isPresent()){
            return WriteReviewResponseDTO.builder()
                .result("이미 리뷰를 작성했습니다.")
                .build();
        }

        // 상품 구매 확정 후 2주 이내만 등록 가능
        if ((orderItem.getStatus() == OrderStatusEnum.USED) &&
            LocalDateTime.now(ZoneId.of("Asia/Seoul")).isBefore(orderItem.getUpdatedAt().plusDays(14L))){
            reviewRepository.save(ReviewEntity.builder()
                    .orderItem(orderItem)
                    .rating(request.getRating())
                    .comment(request.getComment())
                    .customer(customer)
                .build());

            float total = product.getAvgRating() * product.getReviewCnt();
            int updatedReviewCnt = product.getReviewCnt()+1;
            float updatedAvgRating = total / updatedReviewCnt;

            product.setReviewCnt(updatedReviewCnt);
            product.setAvgRating(updatedAvgRating);
            productRepository.save(product);

            return WriteReviewResponseDTO.builder()
                .result("리뷰가 성공적으로 등록되었습니다.")
                .build();
        }

        return WriteReviewResponseDTO.builder()
            .result("리뷰 작성에 실패했습니다.")
            .build();
    }
}
