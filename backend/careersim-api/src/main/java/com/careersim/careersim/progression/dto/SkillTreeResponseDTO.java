package com.careersim.careersim. progression.dto;

import com.careersim.careersim.progression.model.SkillCategory;

import java.util.List;
import java.util.Map;

public record SkillTreeResponseDTO(
        Map<SkillCategory, List<SkillNodeDTO>> categories
) {}