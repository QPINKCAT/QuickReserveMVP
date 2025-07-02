package com.pinkcat.quickreservemvp.customer.controller;

import com.pinkcat.quickreservemvp.common.security.principal.UserPrincipal;
import com.pinkcat.quickreservemvp.customer.dto.CustomerGetResponseDto;
import com.pinkcat.quickreservemvp.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mypage/me")
@RequiredArgsConstructor
public class CustomerController {
  private final CustomerService customerService;

  @GetMapping
  public ResponseEntity<CustomerGetResponseDto> getMyInfo(
      @AuthenticationPrincipal UserPrincipal user) {
    return ResponseEntity.ok(customerService.getMyInfo(user.getUserPk()));
  }
}
