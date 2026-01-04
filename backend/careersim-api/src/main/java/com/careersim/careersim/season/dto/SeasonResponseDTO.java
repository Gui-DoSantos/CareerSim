package com.careersim.careersim.season.dto;

import com.careersim.careersim.season.model.SeasonStatus;
import java.util.UUID;

public record SeasonResponseDTO(
        UUID id,
        UUID playerId,
        Integer year,
        Integer currentEvent,
        SeasonStatus status
) {}