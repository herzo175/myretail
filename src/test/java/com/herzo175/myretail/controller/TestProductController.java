package com.herzo175.myretail.controller;

import com.herzo175.myretail.model.CurrentPrice;
import com.herzo175.myretail.model.Product;
import com.herzo175.myretail.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TestProductController {
    @Mock
    ProductService productService;

    @InjectMocks
    ProductController controller;

    @Test
    public void testGetProductById() {
        Product product = new Product();

        product.setName("product name");
        Mockito.when(productService.getProduct(Mockito.anyLong())).thenReturn(product);

        ResponseEntity<Product> response = controller.getProductById(12345);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
    }

    @Test
    public void testUpdateProduct() {
        CurrentPrice currentPrice = new CurrentPrice();

        currentPrice.setCurrency_code("USD");
        ResponseEntity<Object> response = controller.updateCurrentPrice(12345, currentPrice);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
