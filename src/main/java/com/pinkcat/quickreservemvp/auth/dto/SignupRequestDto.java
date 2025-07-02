package com.pinkcat.quickreservemvp.auth.dto;

import com.pinkcat.quickreservemvp.common.enums.GenderEnum;
import com.pinkcat.quickreservemvp.common.validation.ValidationPatterns;
import com.pinkcat.quickreservemvp.common.validation.user.EmailValid;
import com.pinkcat.quickreservemvp.common.validation.user.NameValid;
import com.pinkcat.quickreservemvp.common.validation.user.PasswordValid;
import com.pinkcat.quickreservemvp.common.validation.user.PhoneNumberValid;
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

  @NotBlank
  @Pattern(regexp = ValidationPatterns.ID, message = "{user.id.invalid}")
  private String id;

  @NotBlank @NameValid private String name;

  @NotBlank @PasswordValid private String password;

  @NotBlank @PhoneNumberValid private String phoneNumber;

  @NotBlank @EmailValid private String email;

  private GenderEnum gender;
}
