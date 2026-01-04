package com.careersim.careersim.season.dto;

import java.util.UUID;

public record CreateSeasonRequestDTO(
        UUID playerId,
        Integer year
) {}