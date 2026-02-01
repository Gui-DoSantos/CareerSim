package com.careersim.careersim.match.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerMatchStatsDTO {

    // Estatísticas gerais
    private Long totalMatches;
    private Long matchesPlayed;
    private Long wins;
    private Long draws;
    private Long losses;
    private Double winRate;

    // Estatísticas ofensivas
    private Long totalGoals;
    private Long totalAssists;
    private Double goalsPerMatch;
    private Double assistsPerMatch;

    // Estatísticas de desempenho
    private BigDecimal averageRating;
    private Long manOfTheMatchCount;

    // Disciplina
    private Long totalYellowCards;
    private Long totalRedCards;

    // Melhor performance
    private BigDecimal bestRating;
    private Integer mostGoalsInMatch;
}