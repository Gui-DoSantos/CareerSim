package com.careersim.careersim.club.dto;

import com.careersim.careersim.club.model.ClubTier;
import lombok.Getter;

@Getter
public class CreateClubRequest {
    private String name;
    private ClubTier tier;
}
