package com.pinkcat.quickreservemvp.customer.dto;

import com.pinkcat.quickreservemvp.common.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CustomerGetResponseDto {
  private Long customerPk;
  private String customerId;
  private String customerName;
  private String customerPhoneNumber;
  private String customerEmail;
  private GenderEnum customerGender;
}
