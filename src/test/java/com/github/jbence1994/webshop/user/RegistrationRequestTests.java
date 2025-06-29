package com.github.jbence1994.webshop.user;

import org.junit.jupiter.api.Test;

import static com.github.jbence1994.webshop.user.RegistrationRequestTestObject.registrationRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;

public class RegistrationRequestTests {
    private final RegistrationRequest registrationRequest = registrationRequest();

    @Test
    public void getProfileTest() {
        var result = registrationRequest.getProfile();

        assertThat(result, allOf(
                hasProperty("firstName", equalTo(registrationRequest().getProfile().getFirstName())),
                hasProperty("middleName", equalTo(registrationRequest().getProfile().getMiddleName())),
                hasProperty("lastName", equalTo(registrationRequest().getProfile().getLastName())),
                hasProperty("dateOfBirth", equalTo(registrationRequest().getProfile().getDateOfBirth())),
                hasProperty("phoneNumber", equalTo(registrationRequest().getProfile().getPhoneNumber()))
        ));
    }

    @Test
    public void getAddressTest() {
        var result = registrationRequest.getAddress();

        assertThat(result, allOf(
                hasProperty("addressLine", equalTo(registrationRequest().getProfile().getAddress().getAddressLine())),
                hasProperty("municipality", equalTo(registrationRequest().getProfile().getAddress().getMunicipality())),
                hasProperty("province", equalTo(registrationRequest().getProfile().getAddress().getProvince())),
                hasProperty("postalCode", equalTo(registrationRequest().getProfile().getAddress().getPostalCode())),
                hasProperty("country", equalTo(registrationRequest().getProfile().getAddress().getCountry()))
        ));
    }
}
