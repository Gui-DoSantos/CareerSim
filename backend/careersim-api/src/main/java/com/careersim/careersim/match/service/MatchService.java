package com.careersim.careersim.match.service;

import com.careersim.careersim.club.model.Club;
import com.careersim.careersim.club.repository.ClubRepository;
import com.careersim.careersim.match.dto.*;
import com.careersim.careersim.match.model.Match;
import com.careersim.careersim.match.model.MatchEvent;
import com.careersim.careersim.match.model.MatchPerformance;
import com.careersim.careersim.match.model.MatchStatus;
import com.careersim.careersim.match.repository.MatchEventRepository;
import com.careersim.careersim.match.repository.MatchPerformanceRepository;
import com.careersim.careersim.match.repository.MatchRepository;
import com.careersim.careersim.player.model.Player;
import com.careersim.careersim.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchPerformanceRepository performanceRepository;
    private final MatchEventRepository eventRepository;
    private final PlayerRepository playerRepository;
    private final ClubRepository clubRepository;
    private final MatchSimulationService simulationService;


    // CRIAR PARTIDA
    @Transactional
    public MatchDetailsDTO createMatch(UUID playerId, CreateMatchRequest request) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player não encontrado"));

        Club club = clubRepository.findById(request.getClubId())
                .orElseThrow(() -> new RuntimeException("Club não encontrado "));

        Match match = new Match();
        match.setPlayer(player);
        match.setClub(club);
        match.setOpponentName(request.getOpponentName());
        match.setCompetition(request.getCompetition());
        match.setMatchDate(request.getMatchDate());
        match.setIsHome(request.getIsHome());
        match.setStatus(MatchStatus.SCHEDULED);

        Match savedMatch = matchRepository.save(match);

        return convertToDetailsDTO(savedMatch);
    }


    // SIMULAR PARTIDA

    @Transactional
    public MatchResultDTO simulateMatch(UUID matchId, SimulateMatchRequest request) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        if (match.getStatus() != MatchStatus.SCHEDULED) {
            throw new RuntimeException("Match cannot be simulated - status: " + match.getStatus());
        }


        MatchResultDTO result = simulationService.simulate(match, request);

        return result;
    }


    // BUSCAR DETALHES DE UMA PARTIDA

    @Transactional(readOnly = true)
    public MatchDetailsDTO getMatchDetails(UUID matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        return convertToDetailsDTO(match);
    }


    // LISTAR PARTIDAS DE UM PLAYER
    @Transactional(readOnly = true)
    public List<MatchListDTO> getPlayerMatches(UUID playerId) {
        List<Match> matches = matchRepository.findByPlayerIdOrderByMatchDateDesc(playerId);
        return matches.stream()
                .map(this::convertToListDTO)
                .collect(Collectors.toList());
    }


    // LISTAR PARTIDAS POR STATUS

    @Transactional(readOnly = true)
    public List<MatchListDTO> getPlayerMatchesByStatus(UUID playerId, MatchStatus status) {
        List<Match> matches = matchRepository.findByPlayerIdAndStatusOrderByMatchDateDesc(playerId, status);
        return matches.stream()
                .map(this::convertToListDTO)
                .collect(Collectors.toList());
    }


    // PRÓXIMAS PARTIDAS (AGENDADAS)
    @Transactional(readOnly = true)
    public List<MatchListDTO> getUpcomingMatches(UUID playerId) {
        List<Match> matches = matchRepository.findUpcomingMatches(playerId);
        return matches.stream()
                .map(this::convertToListDTO)
                .limit(10)
                .collect(Collectors.toList());
    }


    // ÚLTIMAS PARTIDAS (FINALIZADAS)

    @Transactional(readOnly = true)
    public List<MatchListDTO> getRecentMatches(UUID playerId) {
        List<Match> matches = matchRepository.findRecentFinishedMatches(playerId);
        return matches.stream()
                .map(this::convertToListDTO)
                .limit(10)
                .collect(Collectors.toList());
    }


    // ESTATÍSTICAS GERAIS DO PLAYER

    @Transactional(readOnly = true)
    public PlayerMatchStatsDTO getPlayerStats(UUID playerId) {
        Long wins = matchRepository.countWinsByPlayerId(playerId);
        Long draws = matchRepository.countDrawsByPlayerId(playerId);
        Long losses = matchRepository.countLossesByPlayerId(playerId);
        Long totalMatches = wins + draws + losses;

        Long matchesPlayed = performanceRepository.countMatchesPlayedByPlayerId(playerId);
        Long totalGoals = performanceRepository.getTotalGoalsByPlayerId(playerId);
        Long totalAssists = performanceRepository.getTotalAssistsByPlayerId(playerId);
        BigDecimal avgRating = performanceRepository.getAverageRatingByPlayerId(playerId);
        Long manOfTheMatch = performanceRepository.countManOfTheMatchByPlayerId(playerId);
        Long yellowCards = performanceRepository.getTotalYellowCardsByPlayerId(playerId);
        Long redCards = performanceRepository.getTotalRedCardsByPlayerId(playerId);

        // Buscar melhor rating
        List<MatchPerformance> topPerformances = performanceRepository.findTopPerformancesByPlayerId(playerId);
        BigDecimal bestRating = topPerformances.isEmpty() ? BigDecimal.ZERO : topPerformances.get(0).getRating();

        // Calcular médias
        Double goalsPerMatch = matchesPlayed > 0 ? totalGoals.doubleValue() / matchesPlayed : 0.0;
        Double assistsPerMatch = matchesPlayed > 0 ? totalAssists.doubleValue() / matchesPlayed : 0.0;
        Double winRate = totalMatches > 0 ? (wins * 100.0) / totalMatches : 0.0;

        return PlayerMatchStatsDTO.builder()
                .totalMatches(totalMatches)
                .matchesPlayed(matchesPlayed)
                .wins(wins)
                .draws(draws)
                .losses(losses)
                .winRate(Math.round(winRate * 100.0) / 100.0)
                .totalGoals(totalGoals)
                .totalAssists(totalAssists)
                .goalsPerMatch(Math.round(goalsPerMatch * 100.0) / 100.0)
                .assistsPerMatch(Math.round(assistsPerMatch * 100.0) / 100.0)
                .averageRating(avgRating != null ? avgRating.setScale(1, RoundingMode.HALF_UP) : BigDecimal.ZERO)
                .manOfTheMatchCount(manOfTheMatch)
                .totalYellowCards(yellowCards)
                .totalRedCards(redCards)
                .bestRating(bestRating != null ? bestRating.setScale(1, RoundingMode.HALF_UP) : BigDecimal.ZERO)
                .mostGoalsInMatch(0) // TODO: implementar depois
                .build();
    }

    // SEPARAÇÃO MINHA


    private MatchDetailsDTO convertToDetailsDTO(Match match) {
        MatchPerformanceDTO performanceDTO = null;

        if (match.isFinished()) {
            MatchPerformance performance = performanceRepository
                    .findByMatchIdAndPlayerId(match.getId(), match.getPlayer().getId())
                    .orElse(null);

            if (performance != null) {
                performanceDTO = convertToPerformanceDTO(performance);
            }
        }

        List<MatchEventDTO> eventDTOs = eventRepository.findByMatchIdOrderByMinuteAsc(match.getId())
                .stream()
                .map(this::convertToEventDTO)
                .collect(Collectors.toList());

        return MatchDetailsDTO.builder()
                .matchId(match.getId())
                .clubName(match.getClub().getName())
                .opponentName(match.getOpponentName())
                .competition(match.getCompetition())
                .matchDate(match.getMatchDate())
                .isHome(match.getIsHome())
                .status(match.getStatus())
                .playerTeamScore(match.getPlayerTeamScore())
                .opponentTeamScore(match.getOpponentTeamScore())
                .result(match.getResult())
                .outcome(match.getMatchOutcome())
                .playerPerformance(performanceDTO)
                .events(eventDTOs)
                .createdAt(match.getCreatedAt())
                .updatedAt(match.getUpdatedAt())
                .build();
    }

    private MatchListDTO convertToListDTO(Match match) {
        Integer goals = null;
        Integer assists = null;
        String rating = "N/A";

        if (match.isFinished()) {
            MatchPerformance performance = performanceRepository
                    .findByMatchIdAndPlayerId(match.getId(), match.getPlayer().getId())
                    .orElse(null);

            if (performance != null) {
                goals = performance.getGoals();
                assists = performance.getAssists();
                rating = performance.getRating() != null ? performance.getRating().toString() : "N/A";
            }
        }

        return MatchListDTO.builder()
                .matchId(match.getId())
                .clubName(match.getClub().getName())
                .opponentName(match.getOpponentName())
                .competition(match.getCompetition())
                .matchDate(match.getMatchDate())
                .isHome(match.getIsHome())
                .status(match.getStatus())
                .playerTeamScore(match.getPlayerTeamScore())
                .opponentTeamScore(match.getOpponentTeamScore())
                .result(match.getResult())
                .outcome(match.getMatchOutcome())
                .goals(goals)
                .assists(assists)
                .rating(rating)
                .build();
    }

    private MatchPerformanceDTO convertToPerformanceDTO(MatchPerformance performance) {
        return MatchPerformanceDTO.builder()
                .performanceId(performance.getId())
                .matchId(performance.getMatch().getId())
                .playerId(performance.getPlayer().getId())
                .minutesPlayed(performance.getMinutesPlayed())
                .goals(performance.getGoals())
                .assists(performance.getAssists())
                .shots(performance.getShots())
                .shotsOnTarget(performance.getShotsOnTarget())
                .passesCompleted(performance.getPassesCompleted())
                .passesAttempted(performance.getPassesAttempted())
                .passAccuracy(performance.getPassAccuracy())
                .tacklesWon(performance.getTacklesWon())
                .tacklesAttempted(performance.getTacklesAttempted())
                .tackleSuccessRate(performance.getTackleSuccessRate())
                .interceptions(performance.getInterceptions())
                .foulsCommitted(performance.getFoulsCommitted())
                .foulsSuffered(performance.getFoulsSuffered())
                .yellowCards(performance.getYellowCards())
                .redCards(performance.getRedCards())
                .rating(performance.getRating())
                .manOfTheMatch(performance.getManOfTheMatch())
                .build();
    }

    private MatchEventDTO convertToEventDTO(MatchEvent event) {
        String playerName = event.getPlayer() != null
                ? event.getPlayer().getName()
                : "Unknown";

        return MatchEventDTO.builder()
                .eventId(event.getId())
                .matchId(event.getMatch().getId())
                .playerId(event.getPlayer() != null ? event.getPlayer().getId() : null)
                .playerName(playerName)
                .eventType(event.getEventType())
                .minute(event.getMinute())
                .description(event.getEventDescription())
                .build();
    }
}