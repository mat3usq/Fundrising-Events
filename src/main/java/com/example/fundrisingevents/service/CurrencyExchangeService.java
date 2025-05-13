package com.example.fundrisingevents.service;

import com.example.fundrisingevents.model.response.ConversionRateResponse;
import com.example.fundrisingevents.model.response.ExchangeRateResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.*;
import reactor.core.publisher.Mono;

@Service
public class CurrencyExchangeService {
    // ExchangeRate-API -> https://www.exchangerate-api.com/
    private static final String API_KEY = "5964ac65dd798942b0baba8a";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY;
    private final WebClient webClient;

    public CurrencyExchangeService(Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(API_URL).build();
    }

    public void isCurrencyValid(String currency) {
        try {
            fetchExchangeRates(currency);
        } catch (Exception e) {
            throw new RuntimeException("Error during fetching currency " + currency);
        }
    }

    private ExchangeRateResponse fetchExchangeRates(String baseCurrency) {
        ExchangeRateResponse response = webClient.get()
                .uri("/latest/" + baseCurrency)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> {
                    if (res.statusCode() == HttpStatus.NOT_FOUND)
                        return Mono.error(new IllegalArgumentException("Unknown base currency: " + baseCurrency));
                    return Mono.error(new RuntimeException("API error: " + res.statusCode()));
                })
                .bodyToMono(ExchangeRateResponse.class)
                .block();

        if (response != null && !"success".equals(response.getResult()))
            throw new RuntimeException("API returned error: " + response.getResult());

        return response;
    }

    public ConversionRateResponse fetchConversionRate(String baseCurrency, String targetCurrency) {
        ConversionRateResponse response = webClient.get()
                .uri("/pair/" + baseCurrency + "/" + targetCurrency)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> {
                    if (res.statusCode() == HttpStatus.NOT_FOUND)
                        return Mono.error(new IllegalArgumentException("Invalid currency pair: " + baseCurrency + "/" + targetCurrency));
                    return Mono.error(new RuntimeException("API error: " + res.statusCode()));
                })
                .bodyToMono(ConversionRateResponse.class)
                .block();

        if (response != null && !"success".equals(response.getResult()))
            throw new RuntimeException("API returned error: " + response.getResult());

        return response;
    }
}