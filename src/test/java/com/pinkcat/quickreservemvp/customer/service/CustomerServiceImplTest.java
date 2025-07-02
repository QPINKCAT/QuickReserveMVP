package com.pinkcat.quickreservemvp.customer.service;

import com.pinkcat.quickreservemvp.common.enums.GenderEnum;
import com.pinkcat.quickreservemvp.common.exceptions.ErrorMessageCode;
import com.pinkcat.quickreservemvp.common.exceptions.PinkCatException;
import com.pinkcat.quickreservemvp.customer.dto.CustomerGetResponseDto;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import com.pinkcat.quickreservemvp.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {
  @Mock private CustomerRepository customerRepository;

  @InjectMocks private CustomerServiceImpl customerService;

  private CustomerEntity activeCustomer;
  private CustomerEntity inactiveCustomer;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);

    activeCustomer =
        CustomerEntity.builder()
            .id("activetestuser")
            .name("활성화테스트유저")
            .password("encoded_pass")
            .phoneNumber("010-1234-5678")
            .email("activetest@example.com")
            .gender(GenderEnum.MALE)
            .build();
    activeCustomer.setPk(1L);

    inactiveCustomer =
        CustomerEntity.builder()
            .id("inactivetestuser")
            .name("비활성화테스트유저")
            .password("encoded_pass")
            .phoneNumber("010-2345-5678")
            .email("inactivetest@example.com")
            .gender(GenderEnum.FEMALE)
            .build();
    inactiveCustomer.setActive(false);
    inactiveCustomer.setPk(2L);
  }

  @Nested
  class GetMyInfoTest {
    @Test
    void 성공() {
      // given
      when(customerRepository.findByPkAndActiveTrue(activeCustomer.getPk()))
          .thenReturn(Optional.of(activeCustomer));

      // when
      CustomerGetResponseDto result = customerService.getMyInfo(activeCustomer.getPk());

      // then
      assertNotNull(result);
      assertEquals("activetestuser", result.getCustomerId());
      assertEquals("활성화테스트유저", result.getCustomerName());
      assertEquals("010-1234-5678", result.getCustomerPhoneNumber());
      assertEquals("activetest@example.com", result.getCustomerEmail());
      assertEquals(GenderEnum.MALE, result.getCustomerGender());
    }

    @Test
    void 실패_비활성화된계정() {
      // given
      when(customerRepository.findByPkAndActiveTrue(inactiveCustomer.getPk()))
          .thenReturn(Optional.empty());

      // when
      PinkCatException ex =
          assertThrows(PinkCatException.class, () -> customerService.getMyInfo(2L));

      // then
      assertEquals(ErrorMessageCode.CUSTOMER_INACTIVE, ex.getPinkCatErrorMessageCode());
    }
  }
}
