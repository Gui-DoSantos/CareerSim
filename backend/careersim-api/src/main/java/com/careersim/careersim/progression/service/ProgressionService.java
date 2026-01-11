package com.careersim.careersim. progression.service;

import com. careersim.careersim. player.model.Player;
import com.careersim.careersim.player.repository.PlayerRepository;
import com.careersim.careersim.progression.dto.GainXpResponseDTO;
import com.careersim.careersim.progression. dto.PlayerProgressionDTO;
import com. careersim.careersim. progression.repository.PlayerSkillNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream. Collectors;

@Service
@RequiredArgsConstructor
public class ProgressionService {

    private final PlayerRepository playerRepository;
    private final PlayerSkillNodeRepository playerSkillNodeRepository;


    @Transactional
    public GainXpResponseDTO gainExperience(UUID playerId, Integer xpAmount) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player não encontrado"));

        int xpBefore = player.getExperience();
        int levelBefore = player.getLevel();
        int xpNeeded = player.calculateXpForNextLevel();

        boolean leveledUp = player.gainExperience(xpAmount);

        playerRepository.save(player);

        int pointsGained = leveledUp ? 3 : 0;

        return new GainXpResponseDTO(
                xpAmount,
                player.getExperience(),
                player.calculateXpForNextLevel(),
                player.getLevel(),
                leveledUp,
                pointsGained
        );
    }

  -
    @Transactional(readOnly = true)
    public PlayerProgressionDTO getPlayerProgression(UUID playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player não encontrado"));

        List<String> unlockedNodeIds = playerSkillNodeRepository.findByPlayerId(playerId)
                .stream()
                .map(psn -> psn.getSkillNode().getId())
                .collect(Collectors.toList());

        long totalNodesUnlocked = playerSkillNodeRepository.countByPlayerId(playerId);

        return new PlayerProgressionDTO(
                player.getId(),
                player. getName(),
                player.getLevel(),
                player.getExperience(),
                player.calculateXpForNextLevel(),
                player.getTrainingPoints(),
                (int) totalNodesUnlocked,
                unlockedNodeIds
        );
    }


    @Transactional
    public void resetProgression(UUID playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player não encontrado"));


        player.setLevel(1);
        player.setExperience(0);
        player.setTrainingPoints(0);

        // Remove todos os nós desbloqueados
        playerSkillNodeRepository.deleteByPlayerId(playerId);

        // Reset dos atributos para valores iniciais
        player.setAttributes(
                com.careersim.careersim.player.model.PlayerAttributes
                        .createForPosition(player.getPosition(), player.getOverall())
        );

        playerRepository.save(player);
    }
}