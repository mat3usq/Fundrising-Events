package com.example.fundrisingevents.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class FinancialReportResponse {
    @JsonProperty("Fundraising event name")
    private final String eventName;

    @JsonProperty("Amount")
    private final BigDecimal amount;

    @JsonProperty("Currency")
    private final String currency;

    public FinancialReportResponse(String eventName, BigDecimal amount, String currency) {
        this.eventName = eventName;
        this.amount = amount.setScale(2, java.math.RoundingMode.HALF_UP);
        this.currency = currency;
    }
}