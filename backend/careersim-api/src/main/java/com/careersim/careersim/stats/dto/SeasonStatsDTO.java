package com.careersim.careersim.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeasonStatsDTO {

    private Integer season;

    //  PARTIDAS
    private Long totalMatches;
    private Long wins;
    private Long draws;
    private Long losses;

    // GOLS E ASSISTÊNCIAS
    private Long totalGoals;
    private Long totalAssists;
    private Double goalsPerMatch;
    private Double assistsPerMatch;

    // DESEMPENHO
    private BigDecimal averageRating;
    private Long manOfTheMatchCount;

    //  MINUTOS
    private Long totalMinutesPlayed;

    // DISCIPLINA
    private Long yellowCards;
    private Long redCards;
}