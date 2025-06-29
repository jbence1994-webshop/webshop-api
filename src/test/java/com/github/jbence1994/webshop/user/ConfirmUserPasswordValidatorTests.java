package com.github.jbence1994.webshop.user;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static com.github.jbence1994.webshop.user.RegistrationRequestTestObject.registrationRequest;
import static com.github.jbence1994.webshop.user.RegistrationRequestTestObject.registrationRequestWithBlankConfirmPassword;
import static com.github.jbence1994.webshop.user.RegistrationRequestTestObject.registrationRequestWithBlankPassword;
import static com.github.jbence1994.webshop.user.RegistrationRequestTestObject.registrationRequestWithInvalidConfirmPassword;
import static com.github.jbence1994.webshop.user.RegistrationRequestTestObject.registrationRequestWithNullConfirmPassword;
import static com.github.jbence1994.webshop.user.RegistrationRequestTestObject.registrationRequestWithNullPassword;
import static com.github.jbence1994.webshop.user.RegistrationRequestTestObject.registrationRequestWithNullUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class ConfirmUserPasswordValidatorTests {

    @Mock
    private ConstraintValidatorContext context;

    @InjectMocks
    private ConfirmUserPasswordValidator confirmUserPasswordValidator;

    @BeforeEach
    public void setUp() {
        confirmUserPasswordValidator.initialize(mock(ConfirmUserPassword.class));
    }

    private static Stream<Arguments> isNotValidParams() {
        return Stream.of(
                Arguments.of("User is null", registrationRequestWithNullUser()),
                Arguments.of("Password is null", registrationRequestWithNullPassword()),
                Arguments.of("Confirm password is null", registrationRequestWithNullConfirmPassword()),
                Arguments.of("Password is blank", registrationRequestWithBlankPassword()),
                Arguments.of("Confirm password is blank", registrationRequestWithBlankConfirmPassword()),
                Arguments.of("Confirm password is invalid", registrationRequestWithInvalidConfirmPassword())
        );
    }

    @Test
    public void isValidTest_HappyPath() {
        var result = confirmUserPasswordValidator.isValid(registrationRequest(), context);

        assertThat(result, is(true));
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("isNotValidParams")
    public void isValidTest_UnhappyPaths(
            String testCase,
            RegistrationRequest request
    ) {
        var result = confirmUserPasswordValidator.isValid(request, context);

        assertThat(result, is(false));
    }
}
