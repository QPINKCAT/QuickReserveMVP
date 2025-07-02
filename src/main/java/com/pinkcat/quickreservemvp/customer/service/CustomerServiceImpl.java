package com.pinkcat.quickreservemvp.customer.service;

import com.pinkcat.quickreservemvp.common.exceptions.ErrorMessageCode;
import com.pinkcat.quickreservemvp.common.exceptions.PinkCatException;
import com.pinkcat.quickreservemvp.customer.dto.CustomerGetResponseDto;
import com.pinkcat.quickreservemvp.customer.dto.CustomerUpdateRequestDto;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import com.pinkcat.quickreservemvp.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
  private final CustomerRepository customerRepository;

  @Override
  public CustomerGetResponseDto getMyInfo(Long customerPk) {
    CustomerEntity customer =
        customerRepository
            .findByPkAndActiveTrue(customerPk)
            .orElseThrow(
                () -> {
                  log.warn("[내 정보 조회 실패] 비활성화 계정/계정 없음: customerPk={}", customerPk);
                  throw new PinkCatException(
                      "비활성화된 계정입니다. 관리자에게 문의해주세요.", ErrorMessageCode.CUSTOMER_INACTIVE);
                });

    return new CustomerGetResponseDto(
        customer.getPk(),
        customer.getId(),
        customer.getName(),
        customer.getPhoneNumber(),
        customer.getEmail(),
        customer.getGender());
  }

  @Override
  @Transactional
  public void updateMyInfo(Long customerPk, CustomerUpdateRequestDto dto) {
    CustomerEntity customer =
        customerRepository
            .findByPkAndActiveTrue(customerPk)
            .orElseThrow(
                () -> {
                  log.warn("[내 정보 수정 실패] 비활성화 계정/계정 없음: customerPk={}", customerPk);
                  throw new PinkCatException(
                      "비활성화된 계정입니다. 관리자에게 문의해주세요.", ErrorMessageCode.CUSTOMER_INACTIVE);
                });

    customer.updateInfo(dto);
    customerRepository.save(customer);

    log.info("[내 정보 수정 성공] customerPk={}", customerPk);
  }
}
