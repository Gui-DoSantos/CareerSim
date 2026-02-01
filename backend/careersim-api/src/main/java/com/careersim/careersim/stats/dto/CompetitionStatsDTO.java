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
public class CompetitionStatsDTO {

    private String competition;         // Nome da competição (ex: "La Liga")

    //  PARTIDAS
    private Long totalMatches;
    private Long wins;
    private Long draws;
    private Long losses;

    //  GOLS E ASSISTÊNCIAS
    private Long totalGoals;
    private Long totalAssists;
    private Double goalsPerMatch;

    // DESEMPENHO
    private BigDecimal averageRating;
    private Long manOfTheMatchCount;

    //  MINUTOS
    private Long totalMinutesPlayed;
}