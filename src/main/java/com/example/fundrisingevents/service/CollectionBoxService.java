package com.example.fundrisingevents.service;

import com.example.fundrisingevents.model.BoxCurrency;
import com.example.fundrisingevents.model.CollectionBox;
import com.example.fundrisingevents.model.FundraisingEvent;
import com.example.fundrisingevents.model.response.CollectionBoxResponse;
import com.example.fundrisingevents.model.response.ConversionRateResponse;
import com.example.fundrisingevents.repository.CollectionBoxRepository;
import com.example.fundrisingevents.repository.FundraisingEventRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class CollectionBoxService {

    private final CollectionBoxRepository collectionBoxRepository;
    private final FundraisingEventRepository eventRepository;
    private final CurrencyExchangeService exchangeService;

    public CollectionBoxService(CollectionBoxRepository collectionBoxRepository, FundraisingEventRepository eventRepository, CurrencyExchangeService exchangeService) {
        this.collectionBoxRepository = collectionBoxRepository;
        this.eventRepository = eventRepository;
        this.exchangeService = exchangeService;
    }

    public CollectionBox createCollectionBox() {
        CollectionBox newBox = new CollectionBox();
        return collectionBoxRepository.save(newBox);
    }

    public List<CollectionBoxResponse> getAllCollectionBoxes() {
        List<CollectionBox> boxes = collectionBoxRepository.findAll();
        return boxes.stream()
                .map(box -> new CollectionBoxResponse(
                        box.getId(),
                        box.getAssignedEvent() != null,
                        box.isEmpty()
                ))
                .toList();
    }

    public void deleteCollectionBox(Long boxId) {
        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new RuntimeException("Collection box not found with id: " + boxId));

        collectionBoxRepository.delete(box);
    }

    @Transactional
    public CollectionBox assignToEvent(Long boxId, Long eventId) {
        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new RuntimeException("Collection box not found with id: " + boxId));

        FundraisingEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Fundraising event not found with id: " + eventId));

        if (!box.isEmpty())
            throw new RuntimeException("Cannot assign non-empty collection box to an event");

        box.setAssignedEvent(event);
        return collectionBoxRepository.save(box);
    }

    @Transactional
    public void addMoney(Long boxId, String currency, BigDecimal amount) {
        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new RuntimeException("Collection box not found with id: " + boxId));

        exchangeService.isCurrencyValid(currency);

        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be greater than 0");

        if (amount.scale() > 2)
            throw new IllegalArgumentException("Amount cannot have more than 2 decimal places");

        box.getCurrencies().stream()
                .filter(bc -> bc.getCurrency().equalsIgnoreCase(currency))
                .findFirst()
                .ifPresentOrElse(
                        bc -> bc.setAmount(bc.getAmount().add(amount)),
                        () -> {
                            BoxCurrency newCurrency = new BoxCurrency();
                            newCurrency.setCurrency(currency.toUpperCase());
                            newCurrency.setAmount(amount);
                            newCurrency.setCollectionBox(box);
                            box.getCurrencies().add(newCurrency);
                        }
                );

        collectionBoxRepository.save(box);
    }

    @Transactional
    public String emptyCollectionBox(Long boxId) {
        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new RuntimeException("Collection box not found with id: " + boxId));

        if (box.getAssignedEvent() == null)
            throw new RuntimeException("Collection box is not assigned to any event");

        FundraisingEvent event = eventRepository.findById(box.getAssignedEvent().getId())
                .orElseThrow(() -> new RuntimeException("Fundraising event no longer exists"));

        String targetCurrency = event.getCurrency();
        BigDecimal transferedMoney = BigDecimal.ZERO;

        for (BoxCurrency currency : box.getCurrencies()) {
            ConversionRateResponse conversionRate = exchangeService.fetchConversionRate(currency.getCurrency(), targetCurrency);
            BigDecimal convertedAmount = currency.getAmount().multiply(conversionRate.getConversion_rate()).setScale(2, RoundingMode.HALF_UP);
            transferedMoney = transferedMoney.add(convertedAmount);
        }

        transferedMoney = transferedMoney.setScale(2, RoundingMode.HALF_UP);
        event.setAccountBalance(event.getAccountBalance().add(transferedMoney));
        eventRepository.save(event);

        box.getCurrencies().clear();
        collectionBoxRepository.save(box);

        return transferedMoney + " " + targetCurrency;
    }
}