package com.careersim.careersim.match.dto;

import com.careersim.careersim.match.model.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchDetailsDTO {

    private UUID matchId;
    private String clubName;
    private String opponentName;
    private String competition;
    private LocalDateTime matchDate;
    private Boolean isHome;
    private MatchStatus status;
    private Integer playerTeamScore;
    private Integer opponentTeamScore;
    private String result;
    private String outcome;


    private MatchPerformanceDTO playerPerformance;


    @Builder.Default
    private List<MatchEventDTO> events = new ArrayList<>();


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}