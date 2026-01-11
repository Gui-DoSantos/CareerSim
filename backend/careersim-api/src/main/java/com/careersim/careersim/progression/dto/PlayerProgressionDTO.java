package com.careersim.careersim.progression.dto;

import java.util.List;
import java.util.UUID;

public record PlayerProgressionDTO(
        UUID playerId,
        String playerName,
        Integer level,
        Integer currentXp,
        Integer xpForNextLevel,
        Integer trainingPoints,
        Integer totalNodesUnlocked,
        List<String> unlockedNodeIds  // IDs dos nós já desbloqueados
) {}