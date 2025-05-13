package com.example.fundrisingevents.controller;

import com.example.fundrisingevents.model.FundraisingEvent;
import com.example.fundrisingevents.model.response.FinancialReportResponse;
import com.example.fundrisingevents.service.FundraisingEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class FundraisingEventController {

    private final FundraisingEventService eventService;

    public FundraisingEventController(FundraisingEventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEvent(@RequestBody FundraisingEvent eventRequest) {
        try {
            FundraisingEvent newEvent = eventService.createEvent(eventRequest.getName(), eventRequest.getCurrency());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Event created", "data", newEvent));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/financialReport")
    public ResponseEntity<?> getFinancialReport() {
        try {
            List<FinancialReportResponse> report = eventService.getFinancialReport();
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Report generation failed: " + e.getMessage()));
        }
    }
}