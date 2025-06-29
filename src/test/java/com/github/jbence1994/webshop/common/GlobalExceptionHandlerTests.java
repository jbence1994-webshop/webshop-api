package com.github.jbence1994.webshop.common;

import com.github.jbence1994.webshop.cart.EmptyCartException;
import com.github.jbence1994.webshop.image.ImageDeletionException;
import com.github.jbence1994.webshop.image.ImageUploadException;
import com.github.jbence1994.webshop.image.InvalidFileExtensionException;
import com.github.jbence1994.webshop.product.ProductNotFoundException;
import com.github.jbence1994.webshop.user.UserNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.github.jbence1994.webshop.cart.CartTestConstants.CART_ID;
import static com.github.jbence1994.webshop.common.FieldErrorTestObject.fieldError;
import static com.github.jbence1994.webshop.common.FileTestConstants.MAX_UPLOAD_SIZE;
import static com.github.jbence1994.webshop.common.ObjectErrorTestObject.objectError1;
import static com.github.jbence1994.webshop.common.ObjectErrorTestObject.objectError2;
import static com.github.jbence1994.webshop.image.ImageTestConstants.JPEG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTests {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    private static Stream<Arguments> objectErrorParams() {
        return Stream.of(
                Arguments.of("ConfirmPassword", objectError1(), "user.confirmPassword", "Confirm password does not match the password."),
                Arguments.of("ConfirmNewPassword", objectError2(), "confirmNewPassword", "Confirm new password does not match the new password.")
        );
    }

    private static Stream<Arguments> imageUploadOrImageDeletionExceptionParams() {
        return Stream.of(
                Arguments.of(
                        "ImageUploadException",
                        new ImageUploadException("The photo could not be uploaded successfully."),
                        "The photo could not be uploaded successfully."
                ),
                Arguments.of(
                        "ImageUploadException",
                        new ImageDeletionException("The photo could not be deleted successfully."),
                        "The photo could not be deleted successfully."
                )
        );
    }

    @Test
    public void handleHttpMessageNotReadableExceptionTest() {
        var result = globalExceptionHandler.handleHttpMessageNotReadableException();

        assertThat(result.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(result.getBody(), not(nullValue()));
        assertThat(result.getBody().error(), equalTo("Invalid request body."));
    }

    @Test
    public void handleMissingServletRequestPartExceptionTest() {
        var exception = new MissingServletRequestPartException("file");

        var result = globalExceptionHandler.handleMissingServletRequestPartException(exception);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(result.getBody(), not(nullValue()));
        assertThat(result.getBody().error(), equalTo("Required part 'file' is missing."));
    }

    @Test
    public void handleMaxUploadSizeExceededExceptionTest() {
        var exception = new MaxUploadSizeExceededException(MAX_UPLOAD_SIZE);

        var result = globalExceptionHandler.handleMaxUploadSizeExceededException(exception);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.PAYLOAD_TOO_LARGE));
        assertThat(result.getBody(), not(nullValue()));
        assertThat(result.getBody().error(), equalTo("Maximum upload size of 1048576 bytes exceeded"));
    }

    @Test
    public void handleAccessDeniedExceptionTest() {
        var result = globalExceptionHandler.handleAccessDeniedException(new AccessDeniedException("Invalid old password."));

        assertThat(result.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));
        assertThat(result.getBody(), not(nullValue()));
        assertThat(result.getBody().error(), equalTo("Invalid old password."));
    }

    @Test
    public void handleConstraintViolationExceptionTest() {
        ConstraintViolation<?> constraintViolation = mock(ConstraintViolation.class);
        var constraintViolations = Collections.singleton(constraintViolation);
        var exception = new ConstraintViolationException(constraintViolations);
        when(constraintViolation.getMessage()).thenReturn("The file must not be empty.");

        var result = globalExceptionHandler.handleConstraintViolationException(exception);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(result.getBody(), not(nullValue()));
        assertThat(result.getBody().error(), equalTo("The file must not be empty."));
    }

    @Test
    public void handleProductNotFoundExceptionTest() {
        var result = globalExceptionHandler.handleProductNotFoundException(new ProductNotFoundException(1L));

        assertThat(result.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertThat(result.getBody(), not(nullValue()));
        assertThat(result.getBody().error(), equalTo("No product was found with the given ID: #1."));
    }

    @Test
    public void handleUserNotFoundExceptionTest() {
        var result = globalExceptionHandler.handleUserNotFoundException(new UserNotFoundException(1L));

        assertThat(result.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertThat(result.getBody(), not(nullValue()));
        assertThat(result.getBody().error(), equalTo("No user was found with the given ID: #1."));
    }

    @Test
    public void handleEmptyCartExceptionTest() {
        var result = globalExceptionHandler.handleEmptyCartException(new EmptyCartException(CART_ID));

        assertThat(result.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(result.getBody(), not(nullValue()));
        assertThat(result.getBody().error(), equalTo("Cart with the given ID: 00492884-e657-4c6a-abaa-aef8f4240a69 is empty."));
    }

    @Test
    public void handleInvalidFileExtensionExceptionTest() {
        var result = globalExceptionHandler.handleInvalidFileExtensionException(new InvalidFileExtensionException(JPEG));

        assertThat(result.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(result.getBody(), not(nullValue()));
        assertThat(result.getBody().error(), equalTo("Invalid file extension: .jpeg"));
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("imageUploadOrImageDeletionExceptionParams")
    public void handleImageUploadOrImageDeletionExceptionTests(
            String testCase,
            RuntimeException exception,
            String expectedExceptionMessage
    ) {
        var result = globalExceptionHandler.handleImageUploadOrImageDeletionException(exception);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(result.getBody(), not(nullValue()));
        assertThat(result.getBody().error(), equalTo(expectedExceptionMessage));
    }

    @Test
    public void handleValidationErrorsTest_WithFieldError() {
        var bindingResult = mock(BindingResult.class);
        var exception = mock(MethodArgumentNotValidException.class);

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError()));
        when(exception.getBindingResult()).thenReturn(bindingResult);

        var result = globalExceptionHandler.handleValidationErrors(exception);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(result.getBody(), not(nullValue()));
        assertThat(result.getBody().size(), equalTo(1));
        assertThat(result.getBody().getFirst().field(), equalTo("name"));
        assertThat(result.getBody().getFirst().message(), equalTo("Name must be not empty."));
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("objectErrorParams")
    public void handleValidationErrorsTests_WithObjectError(
            String testCase,
            ObjectError error,
            String fieldName,
            String message
    ) {
        var bindingResult = mock(BindingResult.class);
        var exception = mock(MethodArgumentNotValidException.class);

        when(bindingResult.getAllErrors()).thenReturn(List.of(error));
        when(exception.getBindingResult()).thenReturn(bindingResult);

        var result = globalExceptionHandler.handleValidationErrors(exception);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(result.getBody(), not(nullValue()));
        assertThat(result.getBody().size(), equalTo(1));
        assertThat(result.getBody().getFirst().field(), equalTo(fieldName));
        assertThat(result.getBody().getFirst().message(), equalTo(message));
    }
}
