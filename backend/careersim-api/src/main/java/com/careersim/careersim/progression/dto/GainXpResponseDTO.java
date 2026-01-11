package com.careersim.careersim. progression.dto;

public record GainXpResponseDTO(
        Integer xpGained,
        Integer currentXp,
        Integer xpForNextLevel,
        Integer currentLevel,
        Boolean leveledUp,           // true se subiu de nível
        Integer pointsGained         // 3 se subiu, 0 se não
) {}