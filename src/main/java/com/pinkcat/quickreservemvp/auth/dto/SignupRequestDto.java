package com.pinkcat.quickreservemvp.auth.dto;

import com.pinkcat.quickreservemvp.common.enums.GenderEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SignupRequestDto {

  @NotBlank private final String id;

  @NotBlank private final String name;

  @NotBlank private final String password;

  @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 핸드폰 번호 형식이 아닙니다.")
  private final String phoneNumber;

  @Email(message = "올바른 이메일 주소 형식이 아닙니다.")
  private final String email;

  private final GenderEnum gender;
}
