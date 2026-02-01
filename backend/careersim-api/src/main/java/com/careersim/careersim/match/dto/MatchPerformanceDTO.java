package com.careersim.careersim.match.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchPerformanceDTO {

    private UUID performanceId;
    private UUID matchId;
    private UUID playerId;
    private Integer minutesPlayed;
    private Integer goals;
    private Integer assists;
    private Integer shots;
    private Integer shotsOnTarget;
    private Integer passesCompleted;
    private Integer passesAttempted;
    private Double passAccuracy;
    private Integer tacklesWon;
    private Integer tacklesAttempted;
    private Double tackleSuccessRate;
    private Integer interceptions;
    private Integer foulsCommitted;
    private Integer foulsSuffered;
    private Integer yellowCards;
    private Integer redCards;
    private BigDecimal rating;
    private Boolean manOfTheMatch;
}