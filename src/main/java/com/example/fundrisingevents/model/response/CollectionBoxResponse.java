package com.example.fundrisingevents.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectionBoxResponse {
    private Long id;
    private boolean isAssigned;
    private boolean isEmpty;

    public CollectionBoxResponse(Long id, boolean isAssigned, boolean isEmpty) {
        this.id = id;
        this.isAssigned = isAssigned;
        this.isEmpty = isEmpty;
    }
}