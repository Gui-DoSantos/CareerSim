package com.careersim.careersim.match.controller;

import com.careersim.careersim.match.dto.*;
import com.careersim.careersim.match.model.MatchStatus;
import com.careersim.careersim.match.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;


    // CRIAR PARTIDA

    @PostMapping("/players/{playerId}/matches")
    public ResponseEntity<MatchDetailsDTO> createMatch(
            @PathVariable UUID playerId,
            @Valid @RequestBody CreateMatchRequest request
    ) {
        MatchDetailsDTO match = matchService.createMatch(playerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(match);
    }

    // SIMULAR PARTIDA

    @PostMapping("/matches/{matchId}/simulate")
    public ResponseEntity<MatchResultDTO> simulateMatch(
            @PathVariable UUID matchId,
            @RequestBody(required = false) SimulateMatchRequest request
    ) {
        if (request == null) {
            request = new SimulateMatchRequest();
        }

        MatchResultDTO result = matchService.simulateMatch(matchId, request);
        return ResponseEntity.ok(result);
    }


    // VER DETALHES DE UMA PARTIDA

    @GetMapping("/matches/{matchId}")
    public ResponseEntity<MatchDetailsDTO> getMatchDetails(@PathVariable UUID matchId) {
        MatchDetailsDTO match = matchService.getMatchDetails(matchId);
        return ResponseEntity.ok(match);
    }


    // LISTAR TODAS AS PARTIDAS DE UM PLAYER

    @GetMapping("/players/{playerId}/matches")
    public ResponseEntity<List<MatchListDTO>> getPlayerMatches(@PathVariable UUID playerId) {
        List<MatchListDTO> matches = matchService.getPlayerMatches(playerId);
        return ResponseEntity.ok(matches);
    }


    // LISTAR PARTIDAS POR STATUS

    @GetMapping("/players/{playerId}/matches/status/{status}")
    public ResponseEntity<List<MatchListDTO>> getPlayerMatchesByStatus(
            @PathVariable UUID playerId,
            @PathVariable MatchStatus status
    ) {
        List<MatchListDTO> matches = matchService.getPlayerMatchesByStatus(playerId, status);
        return ResponseEntity.ok(matches);
    }


    // PRÓXIMAS PARTIDAS (AGENDADAS)

    @GetMapping("/players/{playerId}/matches/upcoming")
    public ResponseEntity<List<MatchListDTO>> getUpcomingMatches(@PathVariable UUID playerId) {
        List<MatchListDTO> matches = matchService.getUpcomingMatches(playerId);
        return ResponseEntity.ok(matches);
    }


    // ÚLTIMAS PARTIDAS (FINALIZADAS)

    @GetMapping("/players/{playerId}/matches/recent")
    public ResponseEntity<List<MatchListDTO>> getRecentMatches(@PathVariable UUID playerId) {
        List<MatchListDTO> matches = matchService.getRecentMatches(playerId);
        return ResponseEntity.ok(matches);
    }


    // ESTATÍSTICAS GERAIS DO PLAYER

    @GetMapping("/players/{playerId}/matches/stats")
    public ResponseEntity<PlayerMatchStatsDTO> getPlayerStats(@PathVariable UUID playerId) {
        PlayerMatchStatsDTO stats = matchService.getPlayerStats(playerId);
        return ResponseEntity.ok(stats);
    }
}