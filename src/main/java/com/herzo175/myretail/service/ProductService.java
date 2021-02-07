package com.herzo175.myretail.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.herzo175.myretail.dao.CurrentPriceRepository;
import com.herzo175.myretail.dao.RedskyDAO;
import com.herzo175.myretail.exception.MyRetailException;
import com.herzo175.myretail.exception.ProductNotFound;
import com.herzo175.myretail.exception.ServiceUnavailable;
import com.herzo175.myretail.model.CurrentPrice;
import com.herzo175.myretail.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private RedskyDAO redskyDAO;

    @Autowired
    private CurrentPriceRepository currentPriceRepository;

    /**
     * Gets a product given its ID
     * @param id ID of product to retrieve
     * @return Retrieved product
     * @throws MyRetailException Thrown if product or pricing info not found or underlying service is down
     */
    public Product getProduct(long id) throws MyRetailException {
        try {
            // Get product from redsky
            JsonNode redskyProduct = this.redskyDAO.getRedskyProduct(id);

            // Get pricing info from mongoDB
            Optional<CurrentPrice> currentPrice = this.currentPriceRepository.findById(id);

            // Create product with combined info
            Product product = new Product();

            product.setCurrent_price(currentPrice.orElseThrow());
            product.setId(id);
            product.setName(redskyProduct.get("product").get("item").get("product_description").get("title").asText());

            return product;
        } catch (ProductNotFound | NoSuchElementException e) {
            throw new MyRetailException(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ServiceUnavailable | JsonProcessingException e) {
            throw new MyRetailException(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    /**
     * Updates existing pricing info for a product or creates pricing info if does not yet exist
     * @param id ID of product being updated
     * @param newCurrentPrice Updates or new pricing info
     */
    @Transactional
    public void updatePricingInfo(long id, CurrentPrice newCurrentPrice) {
        Optional<CurrentPrice> maybeCurrentPrice = this.currentPriceRepository.findById(id);

        if (maybeCurrentPrice.isEmpty()) {
            // insert new current price
            newCurrentPrice.setId(id);
            this.currentPriceRepository.insert(newCurrentPrice);
        } else {
            // update fields if present
            CurrentPrice currentPrice = maybeCurrentPrice.get();

            if (newCurrentPrice.getCurrency_code() != null) {
                currentPrice.setCurrency_code(newCurrentPrice.getCurrency_code());
            }

            if (newCurrentPrice.getValue() != 0) {
                currentPrice.setValue(newCurrentPrice.getValue());
            }

            currentPriceRepository.save(currentPrice);
        }
    }
}
