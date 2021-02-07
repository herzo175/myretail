package com.herzo175.myretail.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class MyRetailException extends RuntimeException {
    private String message;
    private HttpStatus status;

    public MyRetailException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}
