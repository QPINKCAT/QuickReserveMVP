package com.pinkcat.quickreservemvp.customer.dto;

import com.pinkcat.quickreservemvp.common.validation.user.PasswordValid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerUpdatePasswordRequestDto {
  @NotBlank private String password;

  @NotBlank @PasswordValid private String newPassword;
}
