package com.herzo175.myretail.controller;

import com.herzo175.myretail.model.CurrentPrice;
import com.herzo175.myretail.model.Product;
import com.herzo175.myretail.service.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping(path = "/{id}", produces = "application/json")
    @ApiOperation(value = "Get Product by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Product found"),
            @ApiResponse(code = 404, message = "Product or pricing info not found"),
            @ApiResponse(code = 500, message = "Service Unavailable")
    })
    public ResponseEntity<Product> getProductById(@PathVariable long id) {
        Product product = this.productService.getProduct(id);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}", produces = "application/json")
    @ApiOperation(value = "Update or insert current price information for product")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Price updated or created")
    })
    public ResponseEntity<Object> updateCurrentPrice(@PathVariable long id, @RequestBody CurrentPrice currentPrice) {
        this.productService.updatePricingInfo(id, currentPrice);
        // NOTE: Potentially return 201 if pricing info was created instead of updated
        // (or explicit POST method, although that implies not knowing the product ID)
        return ResponseEntity.noContent().build();
    }
}
