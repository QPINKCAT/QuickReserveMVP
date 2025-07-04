package com.pinkcat.quickreservemvp.common.validation.user;

import com.pinkcat.quickreservemvp.common.validation.ValidationPatterns;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberValid, String> {
  private static final Pattern PATTERN = Pattern.compile(ValidationPatterns.PHONE_NUMBER);

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isBlank()) return true;
    return PATTERN.matcher(value).matches();
  }
}
