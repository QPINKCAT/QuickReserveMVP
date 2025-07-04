package com.pinkcat.quickreservemvp.common.validation.user;

import com.pinkcat.quickreservemvp.common.validation.ValidationPatterns;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class NameValidator implements ConstraintValidator<NameValid, String> {
  private static final Pattern PATTERN = Pattern.compile(ValidationPatterns.NAME);

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isBlank()) return true;
    return PATTERN.matcher(value).matches();
  }
}
