package com.example.fundrisingevents.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ConversionRateResponse {
    private String result;
    private String base_code;
    private String target_code;
    private BigDecimal conversion_rate;
}
