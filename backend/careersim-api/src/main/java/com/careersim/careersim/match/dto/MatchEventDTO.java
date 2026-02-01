package com.careersim.careersim.match.dto;

import com.careersim.careersim.match.model.MatchEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchEventDTO {

    private UUID eventId;
    private UUID matchId;
    private UUID playerId;
    private String playerName;
    private MatchEventType eventType;
    private Integer minute;
    private String description;
}