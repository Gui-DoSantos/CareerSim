package com.careersim.careersim.progression.controller;

import com.careersim.careersim.progression.dto.*;
import com.careersim. careersim.progression.service. ProgressionService;
import com.careersim.careersim.progression.service.SkillTreeService;
import lombok.RequiredArgsConstructor;
import org.springframework. http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/players/{playerId}/progression")
@RequiredArgsConstructor
public class ProgressionController {

    private final ProgressionService progressionService;
    private final SkillTreeService skillTreeService;


     // /api/players/{playerId}/progression
     // Retorna status de progressão do player (level, xp, pontos)

    @GetMapping
    public ResponseEntity<PlayerProgressionDTO> getProgression(@PathVariable UUID playerId) {
        PlayerProgressionDTO progression = progressionService.getPlayerProgression(playerId);
        return ResponseEntity.ok(progression);
    }


     // POST /api/players/{playerId}/progression/gain-xp

    @PostMapping("/gain-xp")
    public ResponseEntity<GainXpResponseDTO> gainExperience(
            @PathVariable UUID playerId,
            @RequestBody GainXpRequest request
    ) {
        GainXpResponseDTO response = progressionService.gainExperience(playerId, request. xpAmount());
        return ResponseEntity. ok(response);
    }


     // GET /api/players/{playerId}/progression/skill-tree
     // Retorna a skill tree completa com status de desbloqueio

    @GetMapping("/skill-tree")
    public ResponseEntity<SkillTreeResponseDTO> getSkillTree(@PathVariable UUID playerId) {
        SkillTreeResponseDTO skillTree = skillTreeService.getSkillTree(playerId);
        return ResponseEntity.ok(skillTree);
    }


    // POST /api/players/{playerId}/progression/unlock-node
     // Desbloqueia um nó da skill tree

    @PostMapping("/unlock-node")
    public ResponseEntity<UnlockNodeResponseDTO> unlockNode(
            @PathVariable UUID playerId,
            @RequestBody UnlockNodeRequest request
    ) {
        UnlockNodeResponseDTO response = skillTreeService.unlockNode(playerId, request.nodeId());
        return ResponseEntity.ok(response);
    }


     // DELETE /api/players/{playerId}/progression/reset
     // Reset da arvore

    @DeleteMapping("/reset")
    public ResponseEntity<Void> resetProgression(@PathVariable UUID playerId) {
        progressionService.resetProgression(playerId);
        return ResponseEntity.noContent().build();
    }
}