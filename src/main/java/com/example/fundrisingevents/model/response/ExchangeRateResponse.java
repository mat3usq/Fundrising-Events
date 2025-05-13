package com.example.fundrisingevents.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class ExchangeRateResponse {
    private String result;
    private String base_code;
    private Map<String, BigDecimal> conversion_rates;
}