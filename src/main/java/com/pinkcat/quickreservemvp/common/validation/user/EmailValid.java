package com.pinkcat.quickreservemvp.common.validation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailValid {
  String message() default "{user.email.invalid}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
