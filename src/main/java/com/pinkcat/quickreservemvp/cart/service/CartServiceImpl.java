package com.pinkcat.quickreservemvp.cart.service;

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
import com.pinkcat.quickreservemvp.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService{

    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

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
}
