package com.github.jbence1994.webshop.common;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Collections;
import java.util.List;

import static com.github.jbence1994.webshop.common.FieldErrorTestObject.fieldError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTests {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handleMissingServletRequestPartExceptionTest() {
        var exception = new MissingServletRequestPartException("file");

        var result = globalExceptionHandler.handleMissingServletRequestPartException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Required part 'file' is missing.", result.getBody().error());
    }

    @Test
    public void handleConstraintViolationExceptionTest() {
        ConstraintViolation<?> constraintViolation = mock(ConstraintViolation.class);
        var constraintViolations = Collections.singleton(constraintViolation);
        var exception = new ConstraintViolationException(constraintViolations);
        when(constraintViolation.getMessage()).thenReturn("The file must not be empty.");

        var result = globalExceptionHandler.handleConstraintViolationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("The file must not be empty.", result.getBody().error());
    }

    @Test
    public void handleValidationErrorsTest() {
        var bindingResult = mock(BindingResult.class);
        var exception = mock(MethodArgumentNotValidException.class);

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError()));
        when(exception.getBindingResult()).thenReturn(bindingResult);

        var result = globalExceptionHandler.handleValidationErrors(exception);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
    }
}
