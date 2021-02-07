package com.herzo175.myretail.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.herzo175.myretail.dao.CurrentPriceRepository;
import com.herzo175.myretail.dao.RedskyDAO;
import com.herzo175.myretail.exception.MyRetailException;
import com.herzo175.myretail.exception.ProductNotFound;
import com.herzo175.myretail.exception.ServiceUnavailable;
import com.herzo175.myretail.model.CurrentPrice;
import com.herzo175.myretail.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
public class TestProductService {

    @Mock
    RedskyDAO redskyDAO;

    @Mock
    CurrentPriceRepository currentPriceRepository;

    @InjectMocks
    ProductService productService;

    @Test
    public void testGetProduct() throws ProductNotFound, ServiceUnavailable, JsonProcessingException {
        // Create heavily nested product information
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode parentNode = objectMapper.createObjectNode();
        ObjectNode productNode = objectMapper.createObjectNode();
        ObjectNode itemNode = objectMapper.createObjectNode();
        ObjectNode productDescriptionNode = objectMapper.createObjectNode();

        parentNode.set("product", productNode);
        productNode.set("item", itemNode);
        itemNode.set("product_description", productDescriptionNode);
        productDescriptionNode.put("title", "my_title");

        // Create pricing info
        CurrentPrice currentPrice = new CurrentPrice();

        currentPrice.setValue((float) 12.34);
        currentPrice.setCurrency_code("USD");

        System.out.println("RedskyDAO:" +  redskyDAO);
        System.out.println("CurrentPriceRepository:" + currentPriceRepository);

        // Make mocks return info
        Mockito.when(redskyDAO.getRedskyProduct(Mockito.anyLong())).thenReturn(parentNode);
        Mockito.when(currentPriceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(currentPrice));

        Product product = productService.getProduct(12345);

        assertEquals((float) 12.34, product.getCurrent_price().getValue());
    }

    @Test
    public void testGetProductNotFound() throws JsonProcessingException {
        Mockito.when(redskyDAO.getRedskyProduct(Mockito.anyLong())).thenThrow(new ProductNotFound("Product not found"));

        try {
            productService.getProduct(12345);
            fail("Was supposed to throw my retail exception");
        } catch (MyRetailException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    public void testGetProductCurrentPriceUnavailable() throws JsonProcessingException {
        // Create heavily nested product information
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode parentNode = objectMapper.createObjectNode();
        ObjectNode productNode = objectMapper.createObjectNode();
        ObjectNode itemNode = objectMapper.createObjectNode();
        ObjectNode productDescriptionNode = objectMapper.createObjectNode();

        parentNode.set("product", productNode);
        productNode.set("item", itemNode);
        itemNode.set("product_description", productDescriptionNode);
        productDescriptionNode.put("title", "my_title");

        // Make mocks return info
        Mockito.when(redskyDAO.getRedskyProduct(Mockito.anyLong())).thenReturn(parentNode);
        Mockito.when(currentPriceRepository.findById(Mockito.anyLong())).thenThrow(new NoSuchElementException());

        try {
            productService.getProduct(12345);
            fail("Was supposed to throw my retail exception");
        } catch (MyRetailException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    public void testGetProductServiceUnavailable() throws JsonProcessingException {
        Mockito
                .when(redskyDAO.getRedskyProduct(Mockito.anyLong()))
                .thenThrow(new ServiceUnavailable("An error occurred fetching product info"));

        try {
            productService.getProduct(12345);
            fail("Was supposed to throw my retail exception");
        } catch (MyRetailException e) {
            assertEquals(HttpStatus.SERVICE_UNAVAILABLE, e.getStatus());
        }
    }

    @Test
    public void createPricingInfo() {
        CurrentPrice currentPrice = new CurrentPrice();

        currentPrice.setValue((float) 12.34);
        currentPrice.setCurrency_code("USD");

        Mockito.when(currentPriceRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        productService.updatePricingInfo(12345, currentPrice);

        Mockito.verify(currentPriceRepository, Mockito.times(1)).insert(currentPrice);
    }

    @Test
    public void updatePricingInfoAllFields() {
        CurrentPrice currentPrice = new CurrentPrice();

        currentPrice.setValue((float) 13.57);
        currentPrice.setCurrency_code("GBP");

        CurrentPrice newCurrentPrice = new CurrentPrice();

        newCurrentPrice.setValue((float) 12.34);
        newCurrentPrice.setCurrency_code("USD");

        Mockito.when(currentPriceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(currentPrice));

        productService.updatePricingInfo(12345, newCurrentPrice);

        Mockito.verify(currentPriceRepository, Mockito.times(1)).save(currentPrice);

        assertEquals("USD", currentPrice.getCurrency_code());
        assertEquals((float) 12.34, currentPrice.getValue());
    }

    @Test
    public void updatePricingInfoNoFields() {
        CurrentPrice currentPrice = new CurrentPrice();

        currentPrice.setValue((float) 13.57);
        currentPrice.setCurrency_code("GBP");

        CurrentPrice newCurrentPrice = new CurrentPrice();

        Mockito.when(currentPriceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(currentPrice));

        productService.updatePricingInfo(12345, newCurrentPrice);

        Mockito.verify(currentPriceRepository, Mockito.times(1)).save(currentPrice);

        assertEquals("GBP", currentPrice.getCurrency_code());
        assertEquals((float) 13.57, currentPrice.getValue());
    }
}
