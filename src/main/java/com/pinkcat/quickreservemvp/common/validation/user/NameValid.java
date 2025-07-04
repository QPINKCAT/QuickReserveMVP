package com.pinkcat.quickreservemvp.common.validation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NameValid {
  String message() default "{user.name.invalid}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
