package com.herzo175.myretail.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Product {
    private long id;
    private String name;
    private CurrentPrice current_price;
}
