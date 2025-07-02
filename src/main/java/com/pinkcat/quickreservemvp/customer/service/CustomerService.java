package com.pinkcat.quickreservemvp.customer.service;

import com.pinkcat.quickreservemvp.customer.dto.CustomerGetResponseDto;

public interface CustomerService {
  CustomerGetResponseDto getMyInfo(Long customerPk);
}
