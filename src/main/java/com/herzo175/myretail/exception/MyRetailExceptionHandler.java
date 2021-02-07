package com.herzo175.myretail.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class MyRetailExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(MyRetailExceptionHandler.class);

    @ExceptionHandler(MyRetailException.class)
    public ResponseEntity<Object> handleMyRetailException(MyRetailException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }
}
