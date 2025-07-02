package com.pinkcat.quickreservemvp.customer.controller;

import com.pinkcat.quickreservemvp.common.security.principal.UserPrincipal;
import com.pinkcat.quickreservemvp.customer.dto.CustomerGetResponseDto;
import com.pinkcat.quickreservemvp.customer.dto.CustomerUpdateRequestDto;
import com.pinkcat.quickreservemvp.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

  @PatchMapping
  public ResponseEntity<Void> updateMyInfo(
      @AuthenticationPrincipal UserPrincipal user,
      @RequestBody @Valid CustomerUpdateRequestDto dto) {
    customerService.updateMyInfo(user.getUserPk(), dto);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  public ResponseEntity<Void> delete(@AuthenticationPrincipal UserPrincipal user) {
    customerService.delete(user.getUserPk());
    return ResponseEntity.ok().build();
  }
}
