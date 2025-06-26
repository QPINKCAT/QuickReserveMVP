package com.pinkcat.quickreservemvp.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginRequestDto {
  @NotBlank private String userId;
  @NotBlank private String password;
}
