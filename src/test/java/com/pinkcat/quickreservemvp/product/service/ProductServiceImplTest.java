package com.pinkcat.quickreservemvp.product.service;

import com.pinkcat.quickreservemvp.category.entity.CategoryEntity;
import com.pinkcat.quickreservemvp.category.entity.CategoryProductEntity;
import com.pinkcat.quickreservemvp.category.repository.CategoryCustomRepository;
import com.pinkcat.quickreservemvp.category.repository.CategoryProductRepository;
import com.pinkcat.quickreservemvp.category.repository.CategoryRepository;
import com.pinkcat.quickreservemvp.common.enums.ProductStatusEnum;
import com.pinkcat.quickreservemvp.common.exceptions.PinkCatException;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import com.pinkcat.quickreservemvp.order.entity.OrderItemEntity;
import com.pinkcat.quickreservemvp.product.dto.ProductInfoResponseDTO;
import com.pinkcat.quickreservemvp.product.dto.ProductReviewListResponseDTO;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import com.pinkcat.quickreservemvp.product.entity.ProductImageEntity;
import com.pinkcat.quickreservemvp.product.repository.DiscountRepository;
import com.pinkcat.quickreservemvp.product.repository.ProductImageRepository;
import com.pinkcat.quickreservemvp.product.repository.ProductRepository;
import com.pinkcat.quickreservemvp.review.entity.ReviewEntity;
import com.pinkcat.quickreservemvp.review.repository.ReviewCustomRepository;
import com.pinkcat.quickreservemvp.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock private ProductRepository productRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private CategoryProductRepository categoryProductRepository;
    @Mock private CategoryCustomRepository categoryCustomRepository;
    @Mock private DiscountRepository discountRepository;
    @Mock private ReviewRepository reviewRepository;
    @Mock private ReviewCustomRepository reviewCustomRepository;
    @Mock private ProductImageRepository productImageRepository;

    private CustomerEntity customer;
    private ProductEntity product;
    private CategoryEntity category;
    private CategoryProductEntity categoryProduct;
    private ProductImageEntity productImage;
    private ReviewEntity review;
    private OrderItemEntity orderItem;

    @BeforeEach
    void setup() {
        customer = CustomerEntity.builder()
                .name("김고객")
                .id("user1")
                .build();
        customer.setPk(1L);

        product = ProductEntity.builder()
                .productName("테스트 상품")
                .price(10000)
                .avgRating(4.5f)
                .productStatus(ProductStatusEnum.ON)
                .build();
        product.setPk(1L);

        category = CategoryEntity.builder()
                .name("카테고리")
                .build();
        category.setPk(1L);

        categoryProduct = CategoryProductEntity.builder()
                .product(product).
                category(category)
                .build();
        categoryProduct.setPk(1L);

        productImage = ProductImageEntity.builder()
                .product(product)
                .url("image.jpg")
                .order(1)
                .build();
        productImage.setPk(1L);

        orderItem = OrderItemEntity.builder()
                .product(product)
                .build();

        review = ReviewEntity.builder()
                .customer(customer)
                .orderItem(orderItem)
                .rating(5)
                .comment("좋아요")
                .build();
        review.setPk(1L);
        review.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));

    }

    @DisplayName("상품 상세 조회 성공")
    @Test
    void getProductInfoSuccess() {

        when(productRepository.findByPk(1L)).thenReturn(Optional.of(product));
        when(categoryProductRepository.findByProduct(product)).thenReturn(Optional.of(categoryProduct));
        when(categoryCustomRepository.findParentCategoryIds(1L)).thenReturn(List.of(1L));
        when(categoryRepository.findCategoryEntityByPk(1L)).thenReturn(Optional.of(category));
        when(discountRepository.findValidDiscountPriceByProduct(eq(product), any())).thenReturn(Optional.of(8000));
        when(reviewRepository.countReviewsByProductPk(1L)).thenReturn(10);
        when(productImageRepository.findAllImagesByProduct(product)).thenReturn(List.of(
                ProductImageEntity.builder().url("image.jpg").order(1).build()
        ));

        ProductInfoResponseDTO dto = productService.getProductInfo(1L);

        assertEquals(1L, dto.getProductId());
        assertEquals("테스트 상품", dto.getName());
        assertEquals(1, dto.getCategories().size());
        assertEquals("카테고리", dto.getCategories().get(0).getName());
        assertEquals("20.00", dto.getDiscountRate());
        assertEquals(8000, dto.getDiscountPrice());
        assertEquals("4.5", dto.getAvgRating());
        assertEquals(10, dto.getReviewCount());
        assertEquals(1, dto.getImages().size());
    }

    @DisplayName("존재하지 않는 상품 조회 시 실패")
    @Test
    void getProductInfoFailNoSuchProduct() {
        when(productRepository.findByPk(999L)).thenReturn(Optional.empty());

        PinkCatException e = assertThrows(PinkCatException.class, () -> {
            productService.getProductInfo(999L);
        });

        assertEquals("존재하지 않는 상품 정보입니다.", e.getMessage());
    }

    @DisplayName("리뷰 조회 성공")
    @Test
    void getProductReviewsSuccess() {
        Page<ReviewEntity> reviewPage = new PageImpl<>(List.of(review));

        when(reviewCustomRepository.findProductReviewsByConditions(eq(1L), isNull(), isNull(), any()))
                .thenReturn(reviewPage);

        ProductReviewListResponseDTO dto = productService.getProductReviews(1L, 1, 10, null, null);

        assertEquals(1, dto.getPage());
        assertEquals(1, dto.getReviews().size());
        assertEquals("user1", dto.getReviews().get(0).getCustomerId());
    }
}
