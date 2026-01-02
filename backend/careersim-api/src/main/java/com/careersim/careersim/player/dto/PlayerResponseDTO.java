package com.careersim.careersim.player.dto;

import java.util.UUID;

public record PlayerResponseDTO(
        UUID id,
        String name,
        Integer age,
        String position,
        Integer overall,
        Integer potential,
        UUID clubId
) {}