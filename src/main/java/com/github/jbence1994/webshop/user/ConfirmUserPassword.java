package com.github.jbence1994.webshop.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ConfirmUserPasswordValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfirmUserPassword {
    String message() default "Confirm password does not match the password.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
