package com.github.jbence1994.webshop.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ProductControllerExceptionHandlerTests {

    @InjectMocks
    private ProductControllerExceptionHandler productControllerExceptionHandler;

    @Test
    public void handleProductNotFoundExceptionTest() {
        var result = productControllerExceptionHandler
                .handleProductNotFoundException(new ProductNotFoundException(1L));

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("No product was found with the given ID: #1.", result.getBody().error());
    }
}
