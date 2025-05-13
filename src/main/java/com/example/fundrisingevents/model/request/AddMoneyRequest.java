package com.example.fundrisingevents.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AddMoneyRequest {
    private String currency;
    private BigDecimal amount;
}