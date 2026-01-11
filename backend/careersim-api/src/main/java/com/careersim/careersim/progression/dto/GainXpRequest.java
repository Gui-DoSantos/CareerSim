package com.careersim.careersim.progression.dto;

public record GainXpRequest(
        Integer xpAmount
) {
    public GainXpRequest {
        if (xpAmount == null || xpAmount <= 0) {
            throw new IllegalArgumentException("XP deve ser maior que zero");
        }
    }
}