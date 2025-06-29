package com.pinkcat.quickreservemvp.auth.controller;

import com.pinkcat.quickreservemvp.auth.dto.LoginRequestDto;
import com.pinkcat.quickreservemvp.auth.dto.LoginResponseDto;
import com.pinkcat.quickreservemvp.auth.dto.SignupRequestDto;
import com.pinkcat.quickreservemvp.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<Void> signup(@RequestBody @Valid SignupRequestDto dto) {
    authService.signup(dto);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto dto) {
    return ResponseEntity.ok(authService.login(dto));
  }
}
