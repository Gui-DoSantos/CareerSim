package com.careersim.careersim.league.dto;

import com.careersim.careersim.league.model.Country;

import java.util.UUID;

public record LeagueResponseDTO(
        UUID id,
        String name,
        Country country,
        Integer prestige
) {}