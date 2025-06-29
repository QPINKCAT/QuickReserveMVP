package com.pinkcat.quickreservemvp.auth.dto;

import com.pinkcat.quickreservemvp.common.enums.GenderEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupRequestDto {

  @NotBlank private String id;

  @NotBlank private String name;

  @NotBlank
  @Size(max = 20, message = "비밀번호는 최대 20자까지 가능합니다.")
  private String password;

  @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 핸드폰 번호 형식이 아닙니다.")
  private String phoneNumber;

  @Email(message = "올바른 이메일 주소 형식이 아닙니다.")
  private String email;

  private GenderEnum gender;
}
