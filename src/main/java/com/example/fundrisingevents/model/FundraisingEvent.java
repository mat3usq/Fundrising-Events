package com.example.fundrisingevents.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class FundraisingEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Event name cannot be empty")
    @Size(max = 100, message = "Event name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Currency cannot be empty")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    @Column(nullable = false, updatable = false)
    private String currency;

    @Column(precision = 20, scale = 2)
    private BigDecimal accountBalance = BigDecimal.ZERO;
}
