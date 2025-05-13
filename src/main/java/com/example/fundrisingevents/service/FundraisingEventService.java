package com.example.fundrisingevents.service;

import com.example.fundrisingevents.model.FundraisingEvent;
import com.example.fundrisingevents.model.response.FinancialReportResponse;
import com.example.fundrisingevents.repository.FundraisingEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FundraisingEventService {

    private final FundraisingEventRepository eventRepository;
    private final CurrencyExchangeService exchangeService;

    public FundraisingEventService(FundraisingEventRepository eventRepository, CurrencyExchangeService currencyExchangeService) {
        this.eventRepository = eventRepository;
        this.exchangeService = currencyExchangeService;
    }

    public FundraisingEvent createEvent(String name, String currency) {
        exchangeService.isCurrencyValid(currency);

        FundraisingEvent event = new FundraisingEvent();
        event.setName(name);
        event.setCurrency(currency.toUpperCase());

        return eventRepository.save(event);
    }

    public List<FinancialReportResponse> getFinancialReport() {
        return eventRepository
                .findAll()
                .stream()
                .map(event -> new FinancialReportResponse(
                        event.getName(),
                        event.getAccountBalance(),
                        event.getCurrency()
                ))
                .toList();
    }
}