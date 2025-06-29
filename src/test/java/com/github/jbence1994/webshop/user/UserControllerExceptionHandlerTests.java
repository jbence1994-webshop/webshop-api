package com.github.jbence1994.webshop.user;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static com.github.jbence1994.webshop.user.ProfileTestConstants.PHONE_NUMBER;
import static com.github.jbence1994.webshop.user.UserTestConstants.EMAIL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

@ExtendWith(MockitoExtension.class)
public class UserControllerExceptionHandlerTests {

    @InjectMocks
    private UserControllerExceptionHandler userControllerExceptionHandler;

    private static Stream<Arguments> handleEmailOrPhoneNumberAlreadyExistsExceptionParams() {
        return Stream.of(
                Arguments.of(
                        "EmailAlreadyExistsException",
                        new EmailAlreadyExistsException(EMAIL),
                        "Email address 'juhasz.bence.zsolt@gmail.com' is already in use. Please use a different."
                ),
                Arguments.of(
                        "PhoneNumberAlreadyExistsException",
                        new PhoneNumberAlreadyExistsException(PHONE_NUMBER),
                        "Phone number '+36501323566' is already registered. Please use a different."
                )
        );
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("handleEmailOrPhoneNumberAlreadyExistsExceptionParams")
    public void handleEmailOrPhoneNumberAlreadyExistsExceptionTests(
            String testCase,
            RuntimeException exception,
            String expectedExceptionMessage
    ) {
        var result = userControllerExceptionHandler.handleEmailOrPhoneNumberAlreadyExistsException(exception);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.CONFLICT));
        assertThat(result.getBody(), not(nullValue()));
        assertThat(result.getBody().error(), equalTo(expectedExceptionMessage));
    }
}
