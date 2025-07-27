//package com.pinkcat.quickreservemvp.wish;
//
//import com.pinkcat.quickreservemvp.common.enums.GenderEnum;
//import com.pinkcat.quickreservemvp.common.enums.ProductStatusEnum;
//import com.pinkcat.quickreservemvp.common.security.principal.UserPrincipal;
//import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
//import com.pinkcat.quickreservemvp.wish.controller.WishController;
//import com.pinkcat.quickreservemvp.wish.dto.WishlistResponseDTO;
//import com.pinkcat.quickreservemvp.wish.service.WishService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.List;
//
//@ExtendWith(MockitoExtension.class)
//class WishControllerTest {
//    @InjectMocks
//    private WishController wishController;
//
//    @Mock
//    private WishService wishService;
//
//    private MockMvc mockMvc;
//
//    private CustomerEntity customer;
//
//    @BeforeEach
//    public void setUp(){
//        mockMvc = MockMvcBuilders.standaloneSetup(wishController).build();
//        customer = CustomerEntity.builder()
//                .id("customer1")
//                .email("customer1@gmail.com")
//                .name("김고객")
//                .phoneNumber("010-1234-1234")
//                .password("password1")
//                .gender(GenderEnum.FEMALE)
//                .build();
//        customer.setPk(1L);
//    }
//
//    @DisplayName("위시리스트 조회 성공")
//    @Test
//    void getWishlistSuccess() throws Exception {
//        int page = 1;
//        int size = 20;
//
//    WishlistResponseDTO response =
//        wishlistResponse(
//            page,
//            size,
//            List.of(
//                WishlistResponseDTO.WishProduct.builder()
//                    .wishId(1L)
//                    .productId(1L)
//                    .thumbnail("./thumbnail1.jpg")
//                    .name("테스트 상품 1")
//                    .price(70000)
//                    .avgRating(3.5f)
//                    .status(ProductStatusEnum.ON.toString())
//                    .build(),
//                WishlistResponseDTO.WishProduct.builder()
//                        .wishId(2L)
//                        .productId(2L)
//                        .thumbnail("./thumbnail2.jpg")
//                        .name("테스트 상품 2")
//                        .price(100000)
//                        .avgRating(4.7f)
//                        .status(ProductStatusEnum.ON.toString())
//                        .build()
//            ));
//
//    // when
//    when(wishService.getWishlist(customer.getPk(), page, size)).thenReturn(response);
//
//    // then
//        mockMvc.perform(get("/api/wish")
//                .param("page", String.valueOf(page))
//                .param("size", String.valueOf(size)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.page").value(1))
//                .andExpect(jsonPath("$.data.size").value(20))
//                .andExpect(jsonPath("$.data.wishlist[0].wishId").value(1))
//                .andExpect(jsonPath("$.data.wishlist[0].name").value("테스트 상품 1"))
//                .andExpect(jsonPath("$.data.wishlist[0].price").value("70000"))
//                .andExpect(jsonPath("$.data.wishlist[1].wishId").value(2))
//                .andExpect(jsonPath("$.data.wishlist[1].name").value("테스트 상품 2"))
//                .andExpect(jsonPath("$.data.wishlist[1].price").value("100000"));
//
//    }
//
//    private WishlistResponseDTO wishlistResponse(int page, int size, List<WishlistResponseDTO.WishProduct> list){
//        return WishlistResponseDTO.builder()
//                .page(page)
//                .size(size)
//                .wishlist(list)
//                .build();
//    }
//}
