package com.pinkcat.quickreservemvp.hotdeal.service;

import com.pinkcat.quickreservemvp.common.enums.SortEnum;
import com.pinkcat.quickreservemvp.common.enums.SortPivotEnum;
import com.pinkcat.quickreservemvp.common.exceptions.ErrorMessageCode;
import com.pinkcat.quickreservemvp.common.exceptions.PinkCatException;
import com.pinkcat.quickreservemvp.hotdeal.dto.HotDealListResponseDTO;
import com.pinkcat.quickreservemvp.hotdeal.dto.HotDealListResponseDTO.HotDeal;
import com.pinkcat.quickreservemvp.hotdeal.dto.HotDealResponseDTO;
import com.pinkcat.quickreservemvp.hotdeal.dto.HotDealResponseDTO.Product;
import com.pinkcat.quickreservemvp.hotdeal.entity.HotDealEntity;
import com.pinkcat.quickreservemvp.hotdeal.entity.HotDealProductEntity;
import com.pinkcat.quickreservemvp.hotdeal.repository.HotDealRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import com.pinkcat.quickreservemvp.hotdeal.repository.HoteDealProductRepository;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import com.pinkcat.quickreservemvp.product.repository.DiscountRepository;
import com.pinkcat.quickreservemvp.product.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotDealServiceImpl implements HotDealService{

    private final HotDealRepository hotDealRepository;
    private final HoteDealProductRepository hoteDealProductRepository;
    private final DiscountRepository discountRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public HotDealListResponseDTO getHotDealList(Integer page, Integer size, SortPivotEnum sortPivot, SortEnum sort){
        Pageable pageable = PageRequest.of(page-1, size);
        if (sortPivot != null) {
            if (sort == SortEnum.ASC)
                pageable = PageRequest.of(page - 1, size, Sort.by(sortPivot.name()).ascending());
            else
                pageable = PageRequest.of(page - 1, size, Sort.by(sortPivot.name()).descending());
        }

        Page<HotDealEntity> hotDeals = hotDealRepository.findAllHotDeals(pageable);
        List<HotDeal> hotDealList = hotDeals.stream().map(e ->
            HotDeal.builder()
                .hotDealPk(e.getPk())
                .name(e.getName())
                .description(e.getDescription())
                .startAt(e.getStartAt())
                .endAt(e.getEndAt())
                .build()).toList();

        return HotDealListResponseDTO.builder()
            .page(page)
            .totalPages(hotDeals.getTotalPages())
            .size(size)
            .hotDealList(hotDealList)
            .build();
    }


    /*
    삭제된 핫딜 && hot_deal end_at < today && public_status ≠ all 인 경우 조회되지 않는다.
     */
    @Override
    public HotDealResponseDTO getHotDealDetail(Integer page, Integer size, Long hotDealPk){

        HotDealEntity hotDeal = hotDealRepository.findByPk(hotDealPk).orElseThrow(() ->
            new PinkCatException("존재하지 않는 핫딜입니다.", ErrorMessageCode.NO_SUCH_HOT_DEAL));

        if (hotDeal.getActive() == false && isValidHotDeal(hotDeal.getStartAt(), hotDeal.getEndAt())) {
            throw new PinkCatException("유효하지 않은 핫딜입니다.", ErrorMessageCode.INVALID_HOT_DEAL);
        }

        Pageable pageable = PageRequest.of(page-1, size);
        Page<HotDealProductEntity> productList = hoteDealProductRepository.findAllByHotDealPk(hotDealPk, pageable);
        List<Product> products = productList.stream()
            .map(p -> {
                ProductEntity product = p.getProduct();

                Integer salePrice = discountRepository.findSalePriceByProduct(product).orElse(null);
                float salePercent = salePrice != null ? (float) (product.getPrice() - salePrice) / product.getPrice() : 0f;
                String thumbnail = productImageRepository.findThumbnailByProductPk(product.getPk()).orElse(null);
                String salePercentString = String.format("%.2f", salePercent * 100);

                return Product.builder()
                    .productId(product.getPk())
                    .name(product.getProductName())
                    .thumbnail(thumbnail)
                    .price(product.getPrice())
                    .status(product.getProductStatus())
                    .stock(product.getStock())
                    .salePercent(salePercentString)
                    .salePrice(salePrice)
                    .saleStartAt(product.getSaleStartAt())
                    .saleEndAt(product.getSaleEndAt())
                    .build();
                }).toList();

        return HotDealResponseDTO.builder()
                .page(page)
                .size(size)
                .totalPages(productList.getTotalPages())
                .hotDealId(hotDealPk)
                .name(hotDeal.getName())
                .description(hotDeal.getDescription())
                .thumbnail(hotDeal.getThumbnail())
                .startAt(hotDeal.getStartAt())
                .endAt(hotDeal.getEndAt())
                .products(products)
            .build();
    }


    private boolean isValidHotDeal(LocalDateTime startAt, LocalDateTime endAt){
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        return startAt.isAfter(now) && endAt.isBefore(now);
    }
}
