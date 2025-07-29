package com.pinkcat.quickreservemvp.service;

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
import com.pinkcat.quickreservemvp.review.dto.CUDReviewResponseDTO;
import com.pinkcat.quickreservemvp.review.dto.ReviewResponseDTO;
import com.pinkcat.quickreservemvp.review.dto.UpdateReviewRequestDTO;
import com.pinkcat.quickreservemvp.review.dto.WriteReviewRequestDTO;
import com.pinkcat.quickreservemvp.review.entity.ReviewEntity;
import com.pinkcat.quickreservemvp.review.repository.ReviewRepository;
import com.pinkcat.quickreservemvp.review.service.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplTest {

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderItemRepository orderItemRepository;

    private final Long userPk = 1L;
    private final Long productPk = 10L;
    private final Long orderItemPk = 100L;
    private final Long reviewPk = 200L;

    private CustomerEntity customer;
    private ProductEntity product;
    private OrderItemEntity orderItem;
    private ReviewEntity review;

    @BeforeEach
    void setUp() {
        customer = CustomerEntity.builder()
            .name("김고객")
            .build();
        customer.setPk(userPk);

        product = ProductEntity.builder()
            .productStatus(ProductStatusEnum.ON)
            .reviewCnt(1)
            .avgRating(5.0f)
            .build();
        product.setPk(productPk);

        orderItem = OrderItemEntity.builder()
            .product(product)
            .status(OrderStatusEnum.USED)
            .build();
        orderItem.setPk(orderItemPk);
        orderItem.setUpdatedAt(LocalDateTime.now().minusDays(1));

        review = ReviewEntity.builder()
            .orderItem(orderItem)
            .customer(customer)
            .rating(7)
            .comment("좋아요")
            .build();
        review.setPk(reviewPk);
    }

    @DisplayName("리뷰 조회 성공")
    @Test
    void getReviewSuccess() {
        when(reviewRepository.findByPk(reviewPk)).thenReturn(Optional.of(review));

        ReviewResponseDTO result = reviewService.getReview(reviewPk);

        assertEquals(7, result.getRating());
        assertEquals("좋아요", result.getComment());
    }

    @DisplayName("리뷰 조회 실패 : 존재하지 않는 리뷰")
    @Test
    void getReviewFail() {
        when(reviewRepository.findByPk(reviewPk)).thenReturn(Optional.empty());
        PinkCatException e = assertThrows(PinkCatException.class, () -> reviewService.getReview(reviewPk));
        assertEquals(ErrorMessageCode.NO_SUCH_REVIEW, e.getErrorMessageCode());
    }

    @DisplayName("리뷰 등록 성공")
    @Test
    void addReviewSuccess() {
        WriteReviewRequestDTO request = WriteReviewRequestDTO.builder()
            .productId(productPk)
            .rating(9)
            .comment("훌륭해요!")
            .build();

        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(orderItemRepository.findByPk(orderItemPk)).thenReturn(Optional.of(orderItem));
        when(productRepository.findByPk(productPk)).thenReturn(Optional.of(product));
        when(reviewRepository.findByOrderItemAndCustomer(orderItem, customer)).thenReturn(Optional.empty());

        CUDReviewResponseDTO result = reviewService.addReview(userPk, orderItemPk, request);

        assertEquals("리뷰가 성공적으로 등록되었습니다.", result.getResult());
    }

    @DisplayName("리뷰 등록 실패 : 판매중지 상품")
    @Test
    void addReviewFailProductStatusOff() {
        product.setProductStatus(ProductStatusEnum.OFF);

        WriteReviewRequestDTO request = WriteReviewRequestDTO.builder()
            .productId(productPk)
            .rating(8)
            .comment("비활성 상품")
            .build();

        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(orderItemRepository.findByPk(orderItemPk)).thenReturn(Optional.of(orderItem));
        when(productRepository.findByPk(productPk)).thenReturn(Optional.of(product));

        CUDReviewResponseDTO result = reviewService.addReview(userPk, orderItemPk, request);

        assertEquals("판매중지된 상품의 리뷰를 작성할 수 없습니다.", result.getResult());
    }

    @DisplayName("리뷰 등록 실패 : 중복 리뷰 등록")
    @Test
    void addReviewFailExistingReview() {
        WriteReviewRequestDTO request = WriteReviewRequestDTO.builder()
            .productId(productPk)
            .rating(6)
            .comment("중복")
            .build();

        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(orderItemRepository.findByPk(orderItemPk)).thenReturn(Optional.of(orderItem));
        when(productRepository.findByPk(productPk)).thenReturn(Optional.of(product));
        when(reviewRepository.findByOrderItemAndCustomer(orderItem, customer)).thenReturn(Optional.of(review));

        CUDReviewResponseDTO result = reviewService.addReview(userPk, orderItemPk, request);

        assertEquals("이미 리뷰를 작성했습니다.", result.getResult());
    }

    @DisplayName("리뷰 등록 실패 : 리뷰 작성 기간 지남")
    @Test
    void addReviewFailExpired() {
        orderItem.setUpdatedAt(LocalDateTime.now().minusDays(20));

        WriteReviewRequestDTO request = WriteReviewRequestDTO.builder()
            .productId(productPk)
            .rating(5)
            .comment("기한초과")
            .build();

        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(orderItemRepository.findByPk(orderItemPk)).thenReturn(Optional.of(orderItem));
        when(productRepository.findByPk(productPk)).thenReturn(Optional.of(product));
        when(reviewRepository.findByOrderItemAndCustomer(orderItem, customer)).thenReturn(Optional.empty());

        CUDReviewResponseDTO result = reviewService.addReview(userPk, orderItemPk, request);

        assertEquals("리뷰 작성에 실패했습니다.", result.getResult());
    }

    @DisplayName("리뷰 수정 성공")
    @Test
    void updateReviewSuccess() {
        UpdateReviewRequestDTO request = UpdateReviewRequestDTO.builder()
            .rating(8)
            .comment("수정된 리뷰")
            .build();

        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(reviewRepository.findByPk(reviewPk)).thenReturn(Optional.of(review));
        when(orderItemRepository.findByPk(orderItemPk)).thenReturn(Optional.of(orderItem));
        when(productRepository.findByPk(productPk)).thenReturn(Optional.of(product));

        CUDReviewResponseDTO result = reviewService.updateReview(userPk, reviewPk, request);

        assertEquals("수정이 완료됐습니다.", result.getResult());
    }

    @DisplayName("리뷰 수정 실패 : 판매중지 상품")
    @Test
    void updateReviewFailProductStatusOff() {
        product.setProductStatus(ProductStatusEnum.OFF);

        UpdateReviewRequestDTO request = UpdateReviewRequestDTO.builder()
            .rating(8)
            .comment("수정 실패")
            .build();

        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(reviewRepository.findByPk(reviewPk)).thenReturn(Optional.of(review));
        when(orderItemRepository.findByPk(orderItemPk)).thenReturn(Optional.of(orderItem));
        when(productRepository.findByPk(productPk)).thenReturn(Optional.of(product));

        CUDReviewResponseDTO result = reviewService.updateReview(userPk, reviewPk, request);

        assertEquals("판매중지된 상품의 리뷰를 수정할 수 없습니다.", result.getResult());
    }

    @DisplayName("리뷰 수정 실패 : 존재하지 않는 상품")
    @Test
    void updateReviewFailNoSuchProduct() {
        UpdateReviewRequestDTO request = UpdateReviewRequestDTO.builder()
            .rating(8)
            .comment("없는 상품")
            .build();

        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(reviewRepository.findByPk(reviewPk)).thenReturn(Optional.of(review));
        when(orderItemRepository.findByPk(orderItemPk)).thenReturn(Optional.of(orderItem));
        when(productRepository.findByPk(productPk)).thenReturn(Optional.empty());

        PinkCatException e = assertThrows(PinkCatException.class,
            () -> reviewService.updateReview(userPk, reviewPk, request));
        assertEquals(ErrorMessageCode.NO_SUCH_PRODUCT, e.getErrorMessageCode());
    }

    @DisplayName("리뷰 수정 실패 : 존재하지 않는 리뷰")
    @Test
    void updateReviewFailNoSuchReview() {
        UpdateReviewRequestDTO request = UpdateReviewRequestDTO.builder()
            .rating(8)
            .comment("없는 리뷰")
            .build();

        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(reviewRepository.findByPk(reviewPk)).thenReturn(Optional.empty());

        PinkCatException e = assertThrows(PinkCatException.class,
            () -> reviewService.updateReview(userPk, reviewPk, request));

        assertEquals(ErrorMessageCode.NO_SUCH_REVIEW, e.getErrorMessageCode());
    }

    @DisplayName("리뷰 삭제 성공")
    @Test
    void deleteReviewSuccess() {
        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(reviewRepository.findByPk(reviewPk)).thenReturn(Optional.of(review));

        CUDReviewResponseDTO result = reviewService.deleteReview(userPk, reviewPk);

        assertEquals("삭제가 완료됐습니다.", result.getResult());
        verify(reviewRepository).delete(review);
    }

    @DisplayName("리뷰 삭제 실패")
    @Test
    void deleteReviewFailInvalidUser() {
        CustomerEntity testUser = CustomerEntity.builder()
            .name("테스트")
            .build();
        testUser.setPk(999L);

        review = ReviewEntity.builder()
            .customer(testUser)
            .orderItem(orderItem)
            .build();
        review.setPk(reviewPk);

        when(customerRepository.findByPkAndActiveTrue(userPk)).thenReturn(Optional.of(customer));
        when(reviewRepository.findByPk(reviewPk)).thenReturn(Optional.of(review));

        PinkCatException e = assertThrows(PinkCatException.class,
            () -> reviewService.deleteReview(userPk, reviewPk));

        assertEquals(ErrorMessageCode.INVALID_USER, e.getErrorMessageCode());
    }
}
