package com.example.fundrisingevents.repository;

import com.example.fundrisingevents.model.FundraisingEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundraisingEventRepository extends JpaRepository<FundraisingEvent, Long> {
}
