package com.careersim.careersim.progression.dto;

import com.careersim.careersim.progression.model.SkillCategory;

import java.util.List;

public record SkillNodeDTO(
        String id,                          // "finishing_killer_instinct"
        String name,                        // "Killer Instinct"
        String description,                 // Descrição
        SkillCategory category,             // FINISHING
        Integer tier,                       // 4
        Integer cost,                       // 4 pontos
        String path,                        // "A", "B", "C" ou null
        String icon,                        // nome do ícone
        Integer positionX,                  // posição visual
        Integer positionY,                  // posição visual
        List<SkillNodeEffectDTO> effects,   // lista de efeitos
        List<String> prerequisiteIds,       // IDs dos pré-requisitos
        Boolean unlocked                    // se o player já desbloqueou
) {}