package com.careersim.careersim.player.dto;

import com.careersim.careersim.player.model.PlayerAttributes;
import com.careersim.careersim.player.model.Position;

import java.util.UUID;

public record PlayerResponseDTO(
        UUID id,
        String name,
        Integer age,
        Position position,
        Integer overall,
        Integer potential,
        PlayerAttributes attributes,
        UUID clubId
) {}