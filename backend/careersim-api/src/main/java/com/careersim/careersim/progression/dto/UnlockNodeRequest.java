package com.careersim.careersim.progression.dto;

public record UnlockNodeRequest(
        String nodeId
) {
    public UnlockNodeRequest {
        if (nodeId == null || nodeId.isBlank()) {
            throw new IllegalArgumentException("nodeId não pode ser vazio");
        }
    }
}