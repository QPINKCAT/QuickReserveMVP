package com.pinkcat.quickreservemvp.customer.service;

import com.pinkcat.quickreservemvp.common.exceptions.ErrorMessageCode;
import com.pinkcat.quickreservemvp.common.exceptions.PinkCatException;
import com.pinkcat.quickreservemvp.customer.dto.CustomerGetResponseDto;
import com.pinkcat.quickreservemvp.customer.dto.CustomerUpdatePasswordRequestDto;
import com.pinkcat.quickreservemvp.customer.dto.CustomerUpdateRequestDto;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import com.pinkcat.quickreservemvp.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
  private final CustomerRepository customerRepository;
  private final PasswordEncoder passwordEncoder;

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

  @Transactional
  public void updatePassword(Long customerPk, CustomerUpdatePasswordRequestDto dto) {
    CustomerEntity customer =
        customerRepository
            .findByPkAndActiveTrue(customerPk)
            .orElseThrow(
                () -> {
                  log.warn("[비밀번호 변경 실패] 비활성화 계정/계정 없음: customerPk={}", customerPk);
                  return new PinkCatException(
                      "비활성화된 계정입니다. 관리자에게 문의해주세요.", ErrorMessageCode.CUSTOMER_INACTIVE);
                });

    if (dto.getNewPassword() == null || dto.getNewPassword().isBlank()) {
      log.warn("[비밀번호 변경 실패] 새 비밀번호 값 누락: customerPk={}", customerPk);
      throw new PinkCatException("비밀번호는 필수 값입니다.", ErrorMessageCode.INVALID_PASSWORD);
    }

    if (passwordEncoder.matches(dto.getNewPassword(), customer.getPassword())) {
      log.warn("[비밀번호 변경 실패] 새 비밀번호 기존과 동일: customerPk={}", customerPk);
      throw new PinkCatException("새 비밀번호가 기존과 같습니다.", ErrorMessageCode.INVALID_PASSWORD);
    }

    if (!passwordEncoder.matches(dto.getPassword(), customer.getPassword())) {
      log.warn("[비밀번호 변경 실패] 기존 비밀번호 불일치: customerPk={}", customerPk);
      throw new PinkCatException("현재 비밀번호가 일치하지 않습니다.", ErrorMessageCode.INVALID_PASSWORD);
    }

    customer.updateEncordedPassword(passwordEncoder.encode(dto.getNewPassword()));

    log.info("[비밀번호 변경 성공] customerPk={}", customerPk);

    customerRepository.save(customer);
  }

  @Override
  @Transactional
  public void delete(Long customerPk) {
    CustomerEntity customer =
        customerRepository
            .findByPkAndActiveTrue(customerPk)
            .orElseThrow(
                () -> {
                  log.warn("[회원 삭제 실패] 비활성화 계정/계정 없음: customerPk={}", customerPk);
                  throw new PinkCatException(
                      "이미 탈퇴 처리된 계정입니다.", ErrorMessageCode.CUSTOMER_INACTIVE);
                });

    customer.setActive(false);
    customerRepository.save(customer);

    log.info("[회원 삭제 성공] customerPk={}", customerPk);
  }
}
