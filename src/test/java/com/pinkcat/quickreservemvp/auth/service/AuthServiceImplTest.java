package com.pinkcat.quickreservemvp.auth.service;

import com.pinkcat.quickreservemvp.auth.dto.LoginRequestDto;
import com.pinkcat.quickreservemvp.auth.dto.LoginResponseDto;
import com.pinkcat.quickreservemvp.auth.dto.SignupRequestDto;
import com.pinkcat.quickreservemvp.common.exceptions.ErrorMessageCode;
import com.pinkcat.quickreservemvp.common.exceptions.PinkCatException;
import com.pinkcat.quickreservemvp.common.redis.RefreshTokenStore;
import com.pinkcat.quickreservemvp.common.security.jwt.JwtTokenProvider;
import com.pinkcat.quickreservemvp.user.entity.UserEntity;
import com.pinkcat.quickreservemvp.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

  @Mock private UserRepository userRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private JwtTokenProvider jwtTokenProvider;
  @Mock private RefreshTokenStore refreshTokenStore;

  @InjectMocks private AuthServiceImpl authService;

  private SignupRequestDto signupDto;
  private UserEntity userEntity;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);

    signupDto =
        SignupRequestDto.builder()
            .id("testuser")
            .name("테스트유저")
            .password("rawpass")
            .email("test@example.com")
            .phoneNumber("010-1234-5678")
            .build();

    userEntity =
        UserEntity.builder()
            .userPk(1L)
            .id("testuser")
            .name("테스트유저")
            .password("encoded_pass")
            .email("test@example.com")
            .phoneNumber("010-1234-5678")
            .build();
  }

  @Nested
  class SignupTest {

    @Test
    void 성공() {
      // given
      when(userRepository.findByUserId("testuser")).thenReturn(Optional.empty());
      when(passwordEncoder.encode("rawpass")).thenReturn("encoded_pass");
      when(userRepository.save(any(UserEntity.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // when
      authService.signup(signupDto);

      // then
      ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
      verify(userRepository).save(captor.capture());

      UserEntity saved = captor.getValue();
      assertEquals("testuser", saved.getId());
      assertEquals("테스트유저", saved.getName());
      assertEquals("encoded_pass", saved.getPassword());
    }

    @Test
    void 실패_중복ID() {
      // given
      when(userRepository.findByUserId("testuser")).thenReturn(Optional.of(userEntity));

      // when
      PinkCatException ex =
          assertThrows(PinkCatException.class, () -> authService.signup(signupDto));
      // then
      assertEquals(HttpStatus.CONFLICT, ex.getPinkCatHttpStatus());
      assertEquals(ErrorMessageCode.DUPLICATED_USER_ID, ex.getPinkCatErrorMessageCode());
    }
  }

  @Nested
  class LoginTest {

    @Test
    void 성공() {
      // given
      LoginRequestDto loginDto = new LoginRequestDto("testuser", "rawpass");
      when(userRepository.findByUserId("testuser")).thenReturn(Optional.of(userEntity));
      when(passwordEncoder.matches("rawpass", "encoded_pass")).thenReturn(true);
      when(jwtTokenProvider.createAccessToken(1L)).thenReturn("access-token");
      when(jwtTokenProvider.createRefreshToken(1L)).thenReturn("refresh-token");

      // when
      LoginResponseDto result = authService.login(loginDto);

      // then
      assertEquals("access-token", result.getAccessToken());
      assertEquals("refresh-token", result.getRefreshToken());
      verify(refreshTokenStore).save("testuser", "refresh-token");
    }

    @Test
    void 실패_존재하지않는ID() {
      // given
      LoginRequestDto loginDto = new LoginRequestDto("wronguser", "rawpass");
      when(userRepository.findByUserId("wronguser")).thenReturn(Optional.empty());

      // when
      ResponseStatusException ex =
          assertThrows(ResponseStatusException.class, () -> authService.login(loginDto));

      // then
      assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    }

    @Test
    void 실패_비밀번호불일치() {
      // given
      LoginRequestDto loginDto = new LoginRequestDto("testuser", "wrongpass");
      when(userRepository.findByUserId("testuser")).thenReturn(Optional.of(userEntity));
      when(passwordEncoder.matches("wrongpass", "encoded_pass")).thenReturn(false);

      // when
      ResponseStatusException ex =
          assertThrows(ResponseStatusException.class, () -> authService.login(loginDto));

      // then
      assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    }
  }
}
