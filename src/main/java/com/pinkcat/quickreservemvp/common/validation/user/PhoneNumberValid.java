package com.pinkcat.quickreservemvp.common.validation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumberValid {
  String message() default "{user.phone.invalid}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
