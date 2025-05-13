package com.example.fundrisingevents.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class CollectionBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private FundraisingEvent assignedEvent;

    @OneToMany(mappedBy = "collectionBox", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoxCurrency> currencies = new ArrayList<>();

    public boolean isEmpty() {
        return currencies.isEmpty() || currencies.stream().allMatch(entry -> entry.getAmount().compareTo(BigDecimal.ZERO) == 0);
    }
}
