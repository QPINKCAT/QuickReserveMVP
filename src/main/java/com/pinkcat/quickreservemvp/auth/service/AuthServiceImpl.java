package com.pinkcat.quickreservemvp.auth.service;

import com.pinkcat.quickreservemvp.auth.dto.LoginRequestDto;
import com.pinkcat.quickreservemvp.auth.dto.LoginResponseDto;
import com.pinkcat.quickreservemvp.auth.dto.SignupRequestDto;
import com.pinkcat.quickreservemvp.common.exceptions.ErrorMessageCode;
import com.pinkcat.quickreservemvp.common.exceptions.PinkCatException;
import com.pinkcat.quickreservemvp.common.redis.RefreshTokenStore;
import com.pinkcat.quickreservemvp.common.security.jwt.JwtTokenProvider;
import com.pinkcat.quickreservemvp.customer.entity.CustomerEntity;
import com.pinkcat.quickreservemvp.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final CustomerRepository customerRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenStore refreshTokenStore;

  @Override
  public void signup(SignupRequestDto dto) {
    if (customerRepository.findById(dto.getId()).isPresent()) {
      log.warn("[회원가입 실패] 중복된 ID: userId={}", dto.getId());
      throw new PinkCatException("이미 사용중인 ID 입니다", ErrorMessageCode.DUPLICATED_USER_ID);
    }

    CustomerEntity user =
        CustomerEntity.builder()
            .id(dto.getId())
            .name(dto.getName())
            .password(passwordEncoder.encode(dto.getPassword()))
            .phoneNumber(dto.getPhoneNumber())
            .email(dto.getEmail())
            .gender(dto.getGender())
            .build();
    customerRepository.save(user);
    log.info("[회원가입 성공] customerId={}", user.getId());
  }

  @Override
  public LoginResponseDto login(LoginRequestDto dto) {
    CustomerEntity user =
        customerRepository
            .findById(dto.getUserId())
            .filter(c -> passwordEncoder.matches(dto.getPassword(), c.getPassword()))
            .orElseThrow(
                () -> {
                  log.warn("[로그인 실패] ID 또는 비밀번호 불일치: userId={}", dto.getUserId());
                  return new ResponseStatusException(
                      HttpStatus.UNAUTHORIZED, "사용자 ID 또는 비밀번호가 올바르지 않습니다.");
                });

    String accessToken = jwtTokenProvider.createAccessToken(user.getPk());
    String refreshToken = jwtTokenProvider.createRefreshToken(user.getPk());

    refreshTokenStore.save(user.getId(), refreshToken);
    log.info("[로그인 성공] userId={}", user.getId());
    return LoginResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
  }

  @Override
  public void logout(String userId) {
    refreshTokenStore.delete(userId);
    log.info("[로그아웃 성공] userId={}", userId);
  }
}
