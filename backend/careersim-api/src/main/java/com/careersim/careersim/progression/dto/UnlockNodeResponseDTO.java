package com.careersim.careersim. progression.dto;

import java.util.List;

public record UnlockNodeResponseDTO(
        String nodeId,
        String nodeName,
        Integer pointsSpent,
        Integer remainingPoints,
        List<AttributeChangeDTO> attributeChanges,  // O que mudou nos atributos
        Integer newOverall  // Overall recalculado
) {}