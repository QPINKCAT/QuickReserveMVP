package com.pinkcat.quickreservemvp.category.service;

import com.pinkcat.quickreservemvp.category.dto.CategoryListResponseDTO;
import com.pinkcat.quickreservemvp.category.dto.CategoryListResponseDTO.Category;
import com.pinkcat.quickreservemvp.category.dto.CategoryProductListResponseDTO;
import com.pinkcat.quickreservemvp.category.dto.CategoryProductListResponseDTO.Product;
import com.pinkcat.quickreservemvp.category.entity.CategoryEntity;
import com.pinkcat.quickreservemvp.category.entity.CategoryProductEntity;
import com.pinkcat.quickreservemvp.category.repository.CategoryCustomRepository;
import com.pinkcat.quickreservemvp.category.repository.CategoryProductRepository;
import com.pinkcat.quickreservemvp.category.repository.CategoryRepository;
import com.pinkcat.quickreservemvp.common.exceptions.ErrorMessageCode;
import com.pinkcat.quickreservemvp.common.exceptions.PinkCatException;
import com.pinkcat.quickreservemvp.product.entity.DiscountEntity;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import com.pinkcat.quickreservemvp.product.repository.DiscountRepository;
import com.pinkcat.quickreservemvp.product.repository.ProductImageRepository;
import com.pinkcat.quickreservemvp.product.repository.ProductRepository;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final CategoryCustomRepository categoryCustomRepository;
    private final CategoryProductRepository categoryProductRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;

    @Override
    public CategoryListResponseDTO getCategories() {
        List<Category> categories = new ArrayList<>();

        List<CategoryEntity> allCategories = categoryRepository.findAll();
        for (CategoryEntity category : allCategories) {
            categories.add(Category.builder()
                    .categoryId(category.getPk())
                    .name(category.getName())
                    .order(category.getOrder())
                    .topCategoryId(category.getTopCategory() == null ? null : category.getTopCategory().getPk())
                    .build());
        }

        return CategoryListResponseDTO.builder()
                .categories(categories)
                .build();

    }

    @Override
    public CategoryProductListResponseDTO getCategoryProducts(long category, int page, int size) {
        List<Product> products = new ArrayList<>();

        // 하위 카테고리 전체 조회
        List<Long> subCategories = categoryCustomRepository.findSubCategoryIds(category);

        // 각 카테고리별 상품 조회
        for (Long id : subCategories) {
            CategoryEntity c = categoryRepository.findCategoryEntityByPk(id).orElseThrow(() ->
                    new PinkCatException("존재하지 않는 카테고리입니다.", ErrorMessageCode.NO_SUCH_CATEGORY));

            List<CategoryProductEntity> categoryProducts = categoryProductRepository.findAllByCategory(c);

            for(CategoryProductEntity categoryProduct : categoryProducts){
            ProductEntity product = productRepository.findByPk(categoryProduct.getPk())
                .orElseThrow(() -> new PinkCatException("존재하지 않는 상품입니다.", ErrorMessageCode.NO_SUCH_PRODUCT));

                // 할인 정보 불러오기
                int price = product.getPrice();
                DiscountEntity discount = discountRepository.findByProduct(product).orElse(null);
                // 할인이 적용된 상품이고, 할인 기간 중에 있다면 할인 가격으로 가격 정보 업데이트
                if (discount != null && isApplicable(discount.getStartAt(), discount.getEndAt())) price = discount.getDiscountPrice();

                // 썸네일 정보 불러오기
                String thumbnail = productImageRepository.findThumbnailByProductPk(product.getPk()).orElse(null);

                products.add(Product.builder()
                                .productId(product.getPk())
                                .thumbnail(thumbnail)
                                .price(price)
                                .avgRating(String.valueOf(product.getAvgRating()))
                                .saleStartAt(product.getSaleStartAt())
                                .saleEndAt(product.getSaleEndAt())
                                .build());
            }
        }

        PageRequest pageRequest = PageRequest.of(page-1, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), products.size());
        Page<Product> pageProducts = new PageImpl<>(products.subList(start, end), pageRequest, products.size());

        return CategoryProductListResponseDTO.builder()
                .page(page)
                .totalPages(pageProducts.getTotalPages())
                .size(size)
                .products(products)
                .build();
    }

    private boolean isApplicable(LocalDateTime startAt, LocalDateTime endAt) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        return now.isAfter(startAt) && now.isBefore(endAt);
    }
}
