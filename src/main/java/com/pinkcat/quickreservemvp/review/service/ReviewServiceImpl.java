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
import com.pinkcat.quickreservemvp.review.dto.ReviewResponseDTO;
import com.pinkcat.quickreservemvp.review.dto.UpdateReviewRequestDTO;
import com.pinkcat.quickreservemvp.review.dto.WriteReviewRequestDTO;
import com.pinkcat.quickreservemvp.review.dto.CUDReviewResponseDTO;
import com.pinkcat.quickreservemvp.review.entity.ReviewEntity;
import com.pinkcat.quickreservemvp.review.repository.ReviewRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
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

    @Override
    public ReviewResponseDTO getReview(Long reviewId){
        log.info("====리뷰 조회");

        ReviewEntity review = reviewRepository.findByPk(reviewId).orElseThrow(() ->
            new PinkCatException("존재하지 않는 리뷰입니다.", ErrorMessageCode.NO_SUCH_REVIEW));

        // active 타입 논의 필요
//        if (review.getActive() == 0) throw new PinkCatException("존재하지 않는 리뷰입니다", ErrorMessageCode.NO_SUCH_REVIEW);

        return ReviewResponseDTO.builder()
            .rating(review.getRating())
            .comment(review.getComment())
            .createdAt(review.getCreatedAt())
            .updatedAt(review.getUpdatedAt())
            .build();
    }

    @Override
    @Transactional
    public CUDReviewResponseDTO addReview(Long userPk, Long orderItemId, WriteReviewRequestDTO request){

        CustomerEntity customer = customerRepository.findByPkAndActiveTrue(userPk).orElseThrow(() ->
            new PinkCatException("비활성화된 계정입니다. 관리자에게 문의해주세요.", ErrorMessageCode.CUSTOMER_INACTIVE));

        OrderItemEntity orderItem = orderItemRepository.findByPk(orderItemId).orElseThrow(() ->
            new PinkCatException("존재하지 않는 주문 상품입니다.", ErrorMessageCode.NO_SUCH_ORDER_ITEM));

        ProductEntity product = productRepository.findByPk(request.getProductId()).orElseThrow(() ->
            new PinkCatException("존재하지 않는 상품입니다.", ErrorMessageCode.NO_SUCH_PRODUCT));

        if (product.getProductStatus() == ProductStatusEnum.OFF){
            return CUDReviewResponseDTO.builder()
                .result("판매중지된 상품의 리뷰를 작성할 수 없습니다.")
                .build();
        }

        // 리뷰를 두 개 이상 쓸 수 없음
        if (reviewRepository.findByOrderItemAndCustomer(orderItem, customer).isPresent()){
            return CUDReviewResponseDTO.builder()
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

            return CUDReviewResponseDTO.builder()
                .result("리뷰가 성공적으로 등록되었습니다.")
                .build();
        }

        return CUDReviewResponseDTO.builder()
            .result("리뷰 작성에 실패했습니다.")
            .build();
    }

    @Override
    @Transactional
    public CUDReviewResponseDTO updateReview(Long userPk, Long reviewId, UpdateReviewRequestDTO request){
        CustomerEntity customer =  customerRepository.findByPkAndActiveTrue(userPk).orElseThrow(() ->
            new PinkCatException("비활성화된 계정입니다. 관리자에게 문의해주세요.", ErrorMessageCode.CUSTOMER_INACTIVE));

        if (!Objects.equals(customer.getPk(), userPk)) {
            throw new PinkCatException("본인이 작성한 리뷰가 아닙니다.", ErrorMessageCode.INVALID_USER);
        }

        ReviewEntity review = reviewRepository.findByPk(reviewId).orElseThrow(() ->
            new PinkCatException("존재하지 않는 리뷰입니다.", ErrorMessageCode.NO_SUCH_REVIEW));

        OrderItemEntity orderItem = orderItemRepository.findByPk(review.getOrderItem().getPk()).orElseThrow(() ->
            new PinkCatException("존재하지 않는 주문 상품입니다.", ErrorMessageCode.NO_SUCH_ORDER_ITEM));

        ProductEntity product = productRepository.findByPk(orderItem.getProduct().getPk()).orElseThrow(() ->
            new PinkCatException("존재하지 않는 상품입니다.", ErrorMessageCode.NO_SUCH_PRODUCT));

        if (product.getProductStatus() == ProductStatusEnum.OFF){
            return CUDReviewResponseDTO.builder()
                .result("판매중지된 상품의 리뷰를 수정할 수 없습니다.")
                .build();
        }

        review.setRating(review.getRating());
        review.setComment(request.getComment());
        reviewRepository.save(review);

        float total = product.getAvgRating() * product.getReviewCnt();
        float updatedAvgRating = total / product.getReviewCnt();

        product.setAvgRating(updatedAvgRating);
        productRepository.save(product);

        return CUDReviewResponseDTO.builder()
            .result("수정이 완료됐습니다.")
            .build();
    }

    @Override
    @Transactional
    public CUDReviewResponseDTO deleteReview(Long userPk, Long reviewId){
        CustomerEntity customer =  customerRepository.findByPkAndActiveTrue(userPk).orElseThrow(() ->
            new PinkCatException("비활성화된 계정입니다. 관리자에게 문의해주세요.", ErrorMessageCode.CUSTOMER_INACTIVE));

        ReviewEntity review = reviewRepository.findByPk(reviewId).orElseThrow(() ->
            new PinkCatException("존재하지 않는 리뷰입니다.", ErrorMessageCode.NO_SUCH_REVIEW));

        if (!Objects.equals(customer.getPk(), userPk)) {
            throw new PinkCatException("본인이 작성한 리뷰가 아닙니다.", ErrorMessageCode.INVALID_USER);
        }

        reviewRepository.delete(review);

        return CUDReviewResponseDTO.builder()
            .result("삭제가 완료됐습니다.")
            .build();
    }
}
