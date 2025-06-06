package com.github.jbence1994.webshop.product;

import org.junit.jupiter.api.Test;

import static com.github.jbence1994.webshop.photo.PhotoTestConstants.PHOTO_FILE_NAME;
import static com.github.jbence1994.webshop.product.ProductTestObject.product1;
import static com.github.jbence1994.webshop.product.ProductTestObject.product1WithPhotos;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductTests {
    private final Product product = product1();
    private final Product productWithPhotos = product1WithPhotos();

    @Test
    public void addPhotoTest() {
        product.addPhoto(PHOTO_FILE_NAME);

        assertEquals(1, product.getPhotos().size());
    }

    @Test
    public void removePhotoTest_HappyPath() {
        productWithPhotos.removePhoto(PHOTO_FILE_NAME);

        assertTrue(productWithPhotos.getPhotos().isEmpty());
    }

    @Test
    public void removePhotoTest_UnhappyPath() {
        assertThrows(
                NullPointerException.class,
                () -> product.removePhoto(PHOTO_FILE_NAME)
        );
    }
}
