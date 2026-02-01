package com.careersim.careersim.statistics.dto;

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
public class CareerStatsDTO {

    private UUID playerId;
    private String playerName;

    //  PARTIDAS
    private Long totalMatches;
    private Long matchesPlayed;
    private Long matchesStarted;
    private Long wins;
    private Long draws;
    private Long losses;
    private Double winRate;

    //  GOLS E ASSISTÊNCIAS
    private Long totalGoals;
    private Long totalAssists;
    private Long goalContributions;
    private Double goalsPerMatch;
    private Double assistsPerMatch;

    // FINALIZAÇÃO
    private Long totalShots;
    private Long shotsOnTarget;
    private Double shotAccuracy;

    //  PASSES
    private Long totalPasses;
    private Long passesCompleted;
    private Double passAccuracy;

    // DEFESA
    private Long totalTackles;
    private Long tacklesWon;
    private Double tackleSuccessRate;
    private Long totalInterceptions;

    //  DISCIPLINA
    private Long totalYellowCards;
    private Long totalRedCards;
    private Long totalFoulsCommitted;
    private Long totalFoulsSuffered;

    //  DESEMPENHO
    private BigDecimal averageRating;
    private BigDecimal bestRating;
    private BigDecimal worstRating;
    private Long manOfTheMatchCount;
    private Double manOfTheMatchRate;

    // MINUTOS JOGADOS
    private Long totalMinutesPlayed;
    private Double averageMinutesPerMatch;

    // RECORDES
    private Integer mostGoalsInMatch;
    private Integer mostAssistsInMatch;
}