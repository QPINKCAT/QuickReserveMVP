package com.pinkcat.quickreservemvp.cart.service;

import com.pinkcat.quickreservemvp.cart.dto.CartItemListResponseDTO;
import com.pinkcat.quickreservemvp.cart.dto.CartItemListResponseDTO.Item;
import com.pinkcat.quickreservemvp.cart.dto.DeleteCartItemRequestDTO;
import com.pinkcat.quickreservemvp.cart.dto.UpdateCartItemRequestDTO;
import com.pinkcat.quickreservemvp.cart.entity.CartEntity;
import com.pinkcat.quickreservemvp.cart.repository.CartRepository;
import com.pinkcat.quickreservemvp.common.enums.ProductStatusEnum;
import com.pinkcat.quickreservemvp.common.exceptions.ErrorMessageCode;
import com.pinkcat.quickreservemvp.common.exceptions.PinkCatException;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import com.pinkcat.quickreservemvp.customer.repository.CustomerRepository;
import com.pinkcat.quickreservemvp.product.dto.AddCartRequestDTO;
import com.pinkcat.quickreservemvp.product.dto.AddCartResponseDTO;
import com.pinkcat.quickreservemvp.product.entity.ProductEntity;
import com.pinkcat.quickreservemvp.product.repository.DiscountRepository;
import com.pinkcat.quickreservemvp.product.repository.ProductImageRepository;
import com.pinkcat.quickreservemvp.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService{

    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final DiscountRepository discountRepository;

    @Transactional
    public AddCartResponseDTO addCart(Long userPk, Long productId, AddCartRequestDTO request){
        CustomerEntity customer = customerRepository.findByPkAndActiveTrue(userPk).orElseThrow(() ->
            new PinkCatException("비활성화된 계정입니다. 관리자에게 문의해주세요.", ErrorMessageCode.CUSTOMER_INACTIVE));

        ProductEntity product = productRepository.findByPk(productId).orElseThrow(() ->
            new PinkCatException("존재하지 않는 상품입니다.", ErrorMessageCode.NO_SUCH_PRODUCT));

        if (product.getProductStatus() == ProductStatusEnum.OFF){
            return AddCartResponseDTO.builder()
                .result("판매 중지된 상품입니다.")
                .build();
        }

        cartRepository.save(
            CartEntity.builder()
                .customer(customer)
                .product(product)
                .quantity(request.getQuantity())
                .build()
        );

        if (cartRepository.findCartEntityByCustomerAndProduct(customer, product).isPresent()){
            return AddCartResponseDTO.builder()
                .result("상품 수량이 수정되었습니다.")
                .build();
        }

        return AddCartResponseDTO.builder()
            .result("장바구니에 상품을 추가했습니다.")
            .build();
    }

    @Override
    public CartItemListResponseDTO getCart(Long userPk) {

        // [1] 로그인하지 않은 유저가 조회 시 프론트 캐시를 꺼내온다
        if (userPk == null){
            return CartItemListResponseDTO.builder()
                .build();
        }

        // [2] 로그인 한 유저가 조회할 경우
        List<Item> items = cartRepository.findCartEntitiesByCustomerPk(userPk).stream().map(c -> {
            ProductEntity product = c.getProduct();
            Integer discountPrice = discountRepository.findValidDiscountPriceByProduct(product,
                    LocalDateTime.now(ZoneId.of("Asia/Seoul"))).orElse(null);
            float discountRate = discountPrice != null ? (float) (product.getPrice() - discountPrice) / product.getPrice() : 0f;
            String discountRateString = String.format("%.2f", discountRate * 100);
            String thumbnail = productImageRepository.findThumbnailByProductPk(product.getPk()).orElse(null);
            return Item.builder()
                    .cartItemId(c.getPk())
                    .productId(product.getPk())
                    .name(product.getProductName())
                    .thumbnail(thumbnail)
                    .unitPrice(product.getPrice())
                    .discountPrice(discountPrice)
                    .discountRate(discountRateString)
                    .quantity(c.getQuantity())
                    .build();
        }).toList();


        return CartItemListResponseDTO.builder()
                .items(items)
                .build();
    }

    @Override
    @Transactional
    public Boolean updateCart(Long userPk, UpdateCartItemRequestDTO request){
        // [1] 로그인하지 않은 유저가 수정 시 프론트 캐시에서 수정을 진행한다
        if (userPk == null) return false;

        // [2] 로그인한 유저가 삭제 시 수량을 수정한다
        CartEntity cart = cartRepository.findCartEntityByPk(request.getCartItemId()).orElseThrow(()->
                new PinkCatException("존재하지 않는 장바구니 상품입니다.", ErrorMessageCode.NO_SUCH_CART_ITEM));

        if (request.getQuantity() <= 0) throw new PinkCatException("장바구니 상품의 최소 수량은 1개입니다.", ErrorMessageCode.NO_SUCH_CATEGORY);
        cart.setQuantity(request.getQuantity());
        cartRepository.save(cart);
        return true;
    }

    @Override
    @Transactional
    public Boolean deleteCart(Long userPk, DeleteCartItemRequestDTO request){
        // [1] 로그인하지 않은 유저가 삭제 시 프론트 캐시에서 삭제를 진행한다
        if (userPk == null) return false;

        // [2] 로그인한 유저가 삭제 시 DB 삭제를 진행한다.
        CartEntity cart = cartRepository.findCartEntityByPk(request.getCartItemId()).orElseThrow(()->
                new PinkCatException("존재하지 않는 장바구니 상품입니다.", ErrorMessageCode.NO_SUCH_CART_ITEM));

        cartRepository.delete(cart);
        return true;
    }
}
