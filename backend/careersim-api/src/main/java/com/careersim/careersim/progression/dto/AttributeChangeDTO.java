package com.careersim.careersim.progression.dto;

public record AttributeChangeDTO(
        String attributeName,
        Integer oldValue,
        Integer newValue,
        Integer increase
) {}