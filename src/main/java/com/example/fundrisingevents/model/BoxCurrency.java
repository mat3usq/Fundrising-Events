package com.example.fundrisingevents.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class BoxCurrency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Currency cannot be empty")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    private String currency;

    @Column(precision = 20, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "box_id")
    private CollectionBox collectionBox;
}