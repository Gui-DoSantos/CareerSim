package com.careersim.careersim.stats.controller;

import com.careersim.careersim.statistics.dto.CareerStatsDTO;
import com.careersim.careersim.statistics.dto.CompetitionStatsDTO;
import com.careersim.careersim.statistics.dto.SeasonStatsDTO;

import com.careersim.careersim.stats.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/players/{playerId}/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;


    // ESTATISTICAS DA CARREIRA TODA

    @GetMapping("/career")
    public ResponseEntity<CareerStatsDTO> getCareerStats(@PathVariable UUID playerId) {
        CareerStatsDTO stats = statisticsService.getCareerStats(playerId);
        return ResponseEntity.ok(stats);
    }


    // ESTATISTICAS POR TEMPORADA

    @GetMapping("/seasons")
    public ResponseEntity<List<SeasonStatsDTO>> getSeasonStats(@PathVariable UUID playerId) {
        List<SeasonStatsDTO> stats = statisticsService.getSeasonStats(playerId);
        return ResponseEntity.ok(stats);
    }


    // ESTATÍSTICAS POR COMPETIÇÃO

    @GetMapping("/competitions")
    public ResponseEntity<List<CompetitionStatsDTO>> getCompetitionStats(@PathVariable UUID playerId) {
        List<CompetitionStatsDTO> stats = statisticsService.getCompetitionStats(playerId);
        return ResponseEntity.ok(stats);
    }
}