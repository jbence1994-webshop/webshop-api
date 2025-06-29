package com.github.jbence1994.webshop.user;

import com.github.jbence1994.webshop.common.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = UserController.class)
public class UserControllerExceptionHandler {

    @ExceptionHandler(exception = {
            EmailAlreadyExistsException.class,
            PhoneNumberAlreadyExistsException.class,
    })
    public ResponseEntity<ErrorDto> handleEmailOrPhoneNumberAlreadyExistsException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDto(exception.getMessage()));
    }
}
