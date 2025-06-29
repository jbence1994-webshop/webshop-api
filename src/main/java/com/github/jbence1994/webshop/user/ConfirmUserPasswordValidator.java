package com.github.jbence1994.webshop.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConfirmUserPasswordValidator implements ConstraintValidator<ConfirmUserPassword, RegistrationRequest> {

    @Override
    public void initialize(ConfirmUserPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(RegistrationRequest request, ConstraintValidatorContext constraintValidatorContext) {
        if (
                request.getUser() == null ||
                        request.getUser().getPassword() == null ||
                        request.getUser().getConfirmPassword() == null
        ) {
            return false;
        }

        if (request.getUser().getPassword().isBlank() || request.getUser().getConfirmPassword().isBlank()) {
            return false;
        }

        return request.getUser().getPassword().equals(request.getUser().getConfirmPassword());
    }
}
