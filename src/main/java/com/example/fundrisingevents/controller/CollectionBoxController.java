package com.example.fundrisingevents.controller;

import com.example.fundrisingevents.model.CollectionBox;
import com.example.fundrisingevents.model.request.AddMoneyRequest;
import com.example.fundrisingevents.model.response.CollectionBoxResponse;
import com.example.fundrisingevents.service.CollectionBoxService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boxes")
public class CollectionBoxController {

    private final CollectionBoxService collectionBoxService;

    public CollectionBoxController(CollectionBoxService collectionBoxService) {
        this.collectionBoxService = collectionBoxService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCollectionBox() {
        try {
            CollectionBox newBox = collectionBoxService.createCollectionBox();
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Collection box created", "data", newBox));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllBoxes() {
        try {
            List<CollectionBoxResponse> boxes = collectionBoxService.getAllCollectionBoxes();
            return ResponseEntity.ok(Map.of("message", "Success", "data", boxes));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{boxId}/delete")
    public ResponseEntity<?> deleteCollectionBox(@PathVariable Long boxId) {
        try {
            collectionBoxService.deleteCollectionBox(boxId);
            return ResponseEntity.ok(Map.of("message", "Collection box with id " + boxId + " deleted"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{boxId}/assign/{eventId}")
    public ResponseEntity<?> assignToEvent(@PathVariable Long boxId, @PathVariable Long eventId) {
        try {
            CollectionBox updatedBox = collectionBoxService.assignToEvent(boxId, eventId);
            return ResponseEntity.ok(Map.of("message", "Box " + boxId + " assigned to event " + eventId, "data", updatedBox));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{boxId}/addMoney")
    public ResponseEntity<?> addMoney(@PathVariable Long boxId, @RequestBody AddMoneyRequest request) {
        try {
            collectionBoxService.addMoney(boxId, request.getCurrency(), request.getAmount());
            return ResponseEntity.ok(Map.of("message", "Added " + request.getAmount() + " " + request.getCurrency().toUpperCase() + " to box " + boxId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{boxId}/empty")
    public ResponseEntity<?> emptyBox(@PathVariable Long boxId) {
        try {
            String transferredMoney = collectionBoxService.emptyCollectionBox(boxId);
            return ResponseEntity.ok(Map.of("message", "Box " + boxId + " emptied", "transfered", transferredMoney));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}