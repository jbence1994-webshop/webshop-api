package com.github.jbence1994.webshop.photo;

import com.github.jbence1994.webshop.product.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.github.jbence1994.webshop.photo.DownloadPhotoDtoTestObject.downloadPhotoDto;
import static com.github.jbence1994.webshop.photo.MultipartFileTestObject.multipartFile;
import static com.github.jbence1994.webshop.photo.PhotoTestConstants.PHOTO_FILE_NAME;
import static com.github.jbence1994.webshop.photo.PhotoTestConstants.PHOTO_URL;
import static com.github.jbence1994.webshop.photo.UploadPhotoDtoTestObject.jpegUploadPhotoDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductPhotoControllerTests {

    @Mock
    private PhotoService<Product, ProductPhoto> productPhotoService;

    @Mock
    private PhotoMapper photoMapper;

    @Mock
    private PhotoUrlBuilder photoUrlBuilder;

    @InjectMocks
    private ProductPhotoController productPhotoController;

    @Test
    public void uploadProductPhotoTest() {
        when(photoMapper.toDto(any())).thenReturn(jpegUploadPhotoDto());
        when(productPhotoService.uploadPhoto(any(), any())).thenReturn(downloadPhotoDto());
        when(photoUrlBuilder.buildUrl(any())).thenReturn(PHOTO_URL);

        var result = productPhotoController.uploadProductPhoto(1L, multipartFile());

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(PHOTO_FILE_NAME, result.getBody().fileName());
        assertEquals(PHOTO_URL, result.getBody().url());
    }

    @Test
    public void getProductPhotosTest() {
        when(productPhotoService.getPhotos(any())).thenReturn(List.of(downloadPhotoDto()));

        var result = productPhotoController.getProductPhotos(1L);

        assertEquals(1, result.size());
    }

    @Test
    public void deleteProductPhotoTest() {
        doNothing().when(productPhotoService).deletePhoto(any(), any());

        var result = productPhotoController.deleteProductPhoto(1L, PHOTO_FILE_NAME);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
    }
}
