package com.herzo175.myretail.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.herzo175.myretail.exception.ProductNotFound;
import com.herzo175.myretail.exception.ServiceUnavailable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Repository
public class RedskyDAO {
    @Value("${redsky.endpoint}")
    private String redskyEndpoint = "filler";

    private static final Logger logger = LoggerFactory.getLogger(RedskyDAO.class);
    private final String getProductFormat = "%s/pdp/tcin/%d?key=candidate";

    public JsonNode getRedskyProduct(long id) throws ServiceUnavailable, JsonProcessingException, ProductNotFound {
        // Get URL for product
        String url = String.format(getProductFormat, this.redskyEndpoint, id);
        RestTemplate rt = new RestTemplate();

        // Make request to API
        try {
            ResponseEntity<String> response = rt.getForEntity(url, String.class);
            ObjectMapper mapper = new ObjectMapper();

            // Convert result to JSON
            return mapper.readTree(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            String msg = "Product " + id + " not found";

            logger.error(msg, e);
            throw new ProductNotFound(msg);
        } catch (HttpClientErrorException e) {
            String msg = "An error occurred fetching product info";

            logger.error(msg, e);
            throw new ServiceUnavailable(msg);
        }
    }
}
