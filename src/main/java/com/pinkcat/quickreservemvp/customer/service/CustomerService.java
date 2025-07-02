package com.pinkcat.quickreservemvp.customer.service;

import com.pinkcat.quickreservemvp.customer.dto.CustomerGetResponseDto;
import com.pinkcat.quickreservemvp.customer.dto.CustomerUpdatePasswordRequestDto;
import com.pinkcat.quickreservemvp.customer.dto.CustomerUpdateRequestDto;

public interface CustomerService {
  CustomerGetResponseDto getMyInfo(Long customerPk);

  void updateMyInfo(Long customerPk, CustomerUpdateRequestDto dto);

  void delete(Long userPk);

  void updatePassword(Long userPk, CustomerUpdatePasswordRequestDto dto);
}
