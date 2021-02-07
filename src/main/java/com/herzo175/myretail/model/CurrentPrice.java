package com.herzo175.myretail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@Getter
@Setter
public class CurrentPrice {
    @Id
    @JsonIgnore
    public long id;

    private float value;
    private String currency_code;
}
