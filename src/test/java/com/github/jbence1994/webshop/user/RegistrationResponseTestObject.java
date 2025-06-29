package com.github.jbence1994.webshop.user;

import static com.github.jbence1994.webshop.user.UserTestConstants.EMAIL;

public final class RegistrationResponseTestObject {
    public static RegistrationResponse registrationResponse() {
        return new RegistrationResponse(1L, EMAIL, "Successful registration.");
    }
}
