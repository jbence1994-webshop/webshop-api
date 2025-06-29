package com.github.jbence1994.webshop.common;

import org.springframework.validation.ObjectError;

public final class ObjectErrorTestObject {
    public static ObjectError objectError1() {
        return new ObjectError(
                "registrationRequest",
                "Confirm password does not match the password."
        );
    }

    public static ObjectError objectError2() {
        return new ObjectError(
                "changePasswordRequest",
                "Confirm new password does not match the new password."
        );
    }
}
