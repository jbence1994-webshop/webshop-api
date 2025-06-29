package com.github.jbence1994.webshop.common;

import com.github.jbence1994.webshop.cart.EmptyCartException;
import com.github.jbence1994.webshop.image.ImageDeletionException;
import com.github.jbence1994.webshop.image.ImageUploadException;
import com.github.jbence1994.webshop.image.InvalidFileExtensionException;
import com.github.jbence1994.webshop.product.ProductNotFoundException;
import com.github.jbence1994.webshop.user.ChangePasswordRequest;
import com.github.jbence1994.webshop.user.ConfirmNewPassword;
import com.github.jbence1994.webshop.user.ConfirmUserPassword;
import com.github.jbence1994.webshop.user.RegistrationRequest;
import com.github.jbence1994.webshop.user.UserNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleHttpMessageNotReadableException() {
        return ResponseEntity.badRequest().body(new ErrorDto("Invalid request body."));
    }

    @ExceptionHandler(exception = MissingServletRequestPartException.class)
    public ResponseEntity<ErrorDto> handleMissingServletRequestPartException(MissingServletRequestPartException exception) {
        var fieldName = exception.getRequestPartName();
        return ResponseEntity.badRequest().body(new ErrorDto(String.format("Required part '%s' is missing.", fieldName)));
    }

    @ExceptionHandler(exception = MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorDto> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(new ErrorDto(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleAccessDeniedException(AccessDeniedException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDto(exception.getMessage()));
    }

    @ExceptionHandler(exception = ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException exception) {
        var allMessages = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));

        return ResponseEntity.badRequest().body(new ErrorDto(allMessages));
    }

    @ExceptionHandler(exception = ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFoundException(ProductNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(exception.getMessage()));
    }

    @ExceptionHandler(exception = UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(exception.getMessage()));
    }

    @ExceptionHandler(exception = EmptyCartException.class)
    public ResponseEntity<ErrorDto> handleEmptyCartException(EmptyCartException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(exception.getMessage()));
    }

    @ExceptionHandler(exception = InvalidFileExtensionException.class)
    public ResponseEntity<ErrorDto> handleInvalidFileExtensionException(InvalidFileExtensionException exception) {
        return ResponseEntity.badRequest().body(new ErrorDto(exception.getMessage()));
    }

    @ExceptionHandler(exception = {
            ImageUploadException.class,
            ImageDeletionException.class
    })
    public ResponseEntity<ErrorDto> handleImageUploadOrImageDeletionException(RuntimeException exception) {
        return ResponseEntity.internalServerError().body(new ErrorDto(exception.getMessage()));
    }

    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorDto>> handleValidationErrors(MethodArgumentNotValidException exception) {
        var validationErrors = new ArrayList<ValidationErrorDto>();

        exception.getBindingResult().getFieldErrors().forEach(error ->
                validationErrors.add(new ValidationErrorDto(error.getField(), error.getDefaultMessage()))
        );

        var confirmPasswordDefaultMessage = RegistrationRequest.class.getAnnotation(ConfirmUserPassword.class).message();
        var confirmNewPasswordDefaultMessage = ChangePasswordRequest.class.getAnnotation(ConfirmNewPassword.class).message();

        exception.getBindingResult().getAllErrors().forEach(error -> {
            addIfMatches(validationErrors, error, "user.confirmPassword", confirmPasswordDefaultMessage);
            addIfMatches(validationErrors, error, "confirmNewPassword", confirmNewPasswordDefaultMessage);
        });

        return ResponseEntity.badRequest().body(validationErrors);
    }

    private void addIfMatches(
            List<ValidationErrorDto> validationErrors,
            ObjectError error,
            String fieldName,
            String expectedMessage
    ) {
        var message = error.getDefaultMessage();

        if (message != null && message.equals(expectedMessage)) {
            validationErrors.add(new ValidationErrorDto(fieldName, message));
        }
    }
}
