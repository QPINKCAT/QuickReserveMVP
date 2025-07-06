package com.pinkcat.quickreservemvp.product.service;

import com.pinkcat.quickreservemvp.category.entity.CategoryEntity;
import com.pinkcat.quickreservemvp.category.entity.CategoryProductEntity;
import com.pinkcat.quickreservemvp.category.repository.CategoryCustomRepository;
import com.pinkcat.quickreservemvp.category.repository.CategoryProductRepository;
import com.pinkcat.quickreservemvp.category.repository.CategoryRepository;
import com.pinkcat.quickreservemvp.common.exceptions.ErrorMessageCode;
import com.pinkcat.quickreservemvp.common.exceptions.PinkCatException;
import com.pinkcat.quickreservemvp.product.dto.ProductInfoResponseDTO;
import com.pinkcat.quickreservemvp.product.dto.ProductInfoResponseDTO.Category;
import com.pinkcat.quickreservemvp.product.dto.ProductInfoResponseDTO.Image;
import com.pinkcat.quickreservemvp.product.entity.DiscountEntity;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import com.pinkcat.quickreservemvp.product.repository.DiscountRepository;
import com.pinkcat.quickreservemvp.product.repository.ProductImageRepository;
import com.pinkcat.quickreservemvp.product.repository.ProductRepository;
import com.pinkcat.quickreservemvp.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryProductRepository categoryProductRepository;
    private final CategoryCustomRepository categoryCustomRepository;
    private final DiscountRepository discountRepository;
    private final ReviewRepository reviewRepository;
    private final ProductImageRepository productImageRepository;


    @Override
    public ProductInfoResponseDTO getProductInfo(Long productId) {

        // 상품 찾기
        ProductEntity product = productRepository.findByPk(productId).orElseThrow(() ->
            new PinkCatException("존재하지 않는 상품 정보입니다.", ErrorMessageCode.NO_SUCH_PRODUCT));

        // 상위 카테고리 목록 찾기
        List<Category> categories = new ArrayList<>();
        // 상품의 카테고리 찾기
        CategoryProductEntity categoryProduct = categoryProductRepository.findByProduct(product)
                .orElseThrow(() -> new PinkCatException("카테고리가 존재하지 않는 상품입니다.", ErrorMessageCode.NO_SUCH_CATEGORY_PRODUCT));
        List<Long> topCategoryIds = categoryCustomRepository.findParentCategoryIds(categoryProduct.getCategory().getPk());
        for (Long topCategoryId : topCategoryIds) {
            CategoryEntity category = categoryRepository.findCategoryEntityByPk(topCategoryId)
                      .orElseThrow(() -> new PinkCatException("존재하지 않는 카테고리입니다.", ErrorMessageCode.NO_SUCH_CATEGORY));
            categories.add(Category.builder()
                            .categoryId(category.getPk())
                            .name(category.getName())
                            .build());
        }
        categories.sort(Comparator.comparing(Category::getCategoryId));

        int price = product.getPrice();

        // 할인 정보 찾기
        DiscountEntity discount = discountRepository.findByProduct(product).orElse(null);
        float rate = discount == null ? 0 : (float) (price - discount.getDiscountPrice()) / price;
        int finalPrice = discount == null ? product.getPrice() : discount.getDiscountPrice();
        String discountRate = rate == 0 ? "0" : String.valueOf(rate);

        // 리뷰 개수 찾기
        Integer reviewCnt = reviewRepository.countReviewsByProductPk(productId);

        // 이미지 목록 찾기
        List<Image> images = productImageRepository.findAllImagesByProduct(product).stream()
                .map(i -> Image.builder()
                        .url(i.getUrl())
                        .order(i.getOrder())
                        .build()).toList();

        return ProductInfoResponseDTO.builder()
                .productId(product.getPk())
                .categories(categories)
                .name(product.getProductName())
                .price(price)
                .avgRating(product.getAvgRating().toString())
                .discountRate(discountRate)
                .finalPrice(finalPrice)
                .avgRating(product.getAvgRating().toString())
                .reviewCount(reviewCnt)
                .images(images)
                .build();
    }
}
