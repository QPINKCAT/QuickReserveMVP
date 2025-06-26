package com.pinkcat.quickreservemvp.common.security.principal;

import com.pinkcat.quickreservemvp.user.entity.UserEntity;
import com.pinkcat.quickreservemvp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user =
        userRepository
            .findById(username)
            .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다: " + username));
    return new UserPrincipal(user);
  }

  public UserPrincipal loadUserByPk(Long userPk) {
    UserEntity user =
        userRepository
            .findById(userPk)
            .orElseThrow(
                () -> {
                  log.warn("토큰 내 사용자 조회 실패: userPk={}", userPk);
                  return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
                });
    return new UserPrincipal(user);
  }
}
