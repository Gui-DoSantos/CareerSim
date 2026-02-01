package com.careersim.careersim.match.dto;

import com.careersim.careersim.match.model.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchListDTO {

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

    // Resumo da performance
    private Integer goals;
    private Integer assists;
    private String rating;
}