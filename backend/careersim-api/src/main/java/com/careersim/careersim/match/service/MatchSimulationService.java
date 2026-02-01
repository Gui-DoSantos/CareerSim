package com.careersim.careersim.match.service;

import com.careersim.careersim.event.model.EventType;
import com.careersim.careersim.event.service.EventService;
import com.careersim.careersim.match.dto.MatchPerformanceDTO;
import com.careersim.careersim.match.dto.MatchResultDTO;
import com.careersim.careersim.match.dto.SimulateMatchRequest;
import com.careersim.careersim.match.model.*;
import com.careersim.careersim.match.repository.MatchEventRepository;
import com.careersim.careersim.match.repository.MatchPerformanceRepository;
import com.careersim.careersim.match.repository.MatchRepository;
import com.careersim.careersim.player.model.Player;
import com.careersim.careersim.player.model.PlayerAttributes;
import com.careersim.careersim.progression.service.ProgressionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MatchSimulationService {

    private final MatchRepository matchRepository;
    private final MatchPerformanceRepository performanceRepository;
    private final MatchEventRepository eventRepository;
    private final ProgressionService progressionService;
    private final Random random = new Random();
    private final EventService eventService;



    // SIMULAR PARTIDA COMPLETA

    @Transactional
    public MatchResultDTO simulate(Match match, SimulateMatchRequest request) {
        Player player = match.getPlayer();
        PlayerAttributes attrs = player.getAttributes();


        match.setStatus(MatchStatus.IN_PROGRESS);
        matchRepository.save(match);


        int[] scores = calculateMatchScore(player, attrs, match.getIsHome());
        int playerTeamScore = scores[0];
        int opponentScore = scores[1];


        MatchPerformance performance = simulatePlayerPerformance(match, player, attrs, playerTeamScore, opponentScore);
        performanceRepository.save(performance);


        generateMatchEvents(match, player, performance);


        match.setPlayerTeamScore(playerTeamScore);
        match.setOpponentTeamScore(opponentScore);
        match.setStatus(MatchStatus.FINISHED);
        matchRepository.save(match);


        int xpGained = calculateXPGained(performance, match.getMatchOutcome());

        String eventTitle = "Partida finalizada: " + match.getResult();
        String eventDescription = String.format(
                "Resultado: %s. Você marcou %d gol(s) e deu %d assistência(s). Nota: %s. XP ganho: %d",
                match.getMatchOutcome().equals("WIN") ? "Vitória!" :
                        match.getMatchOutcome().equals("DRAW") ? "Empate" : "Derrota",
                performance.getGoals(),
                performance.getAssists(),
                performance.getRating().toString(),
                xpGained
        );

        eventService.createSystemEvent(
                player.getId(),
                EventType.MATCH_RESULT,
                eventTitle,
                eventDescription,
                match.getId(),
                "Match"
        );


        progressionService.gainExperience(player.getId(), xpGained);


        return buildMatchResult(match, performance, xpGained);


    }


    // CALCULAR PLACAR DA PARTIDA

    private int[] calculateMatchScore(Player player, PlayerAttributes attrs, boolean isHome) {
        int playerOverall = attrs.calculateOverall(player.getPosition()); // ← CORREÇÃO!

        int playerTeamStrength = playerOverall + random.nextInt(10) - 5;

        int opponentStrength = 50 + random.nextInt(35);

        if (isHome) {
            playerTeamStrength += 5;
        } else {
            opponentStrength += 5;
        }

        int playerTeamGoals = Math.max(0, (playerTeamStrength / 20) + random.nextInt(4) - 1);
        int opponentGoals = Math.max(0, (opponentStrength / 20) + random.nextInt(4) - 1);

        return new int[]{playerTeamGoals, opponentGoals};
    }


    // SIMULAR PERFORMANCE DO PLAYER

    private MatchPerformance simulatePlayerPerformance(Match match, Player player, PlayerAttributes attrs, int teamGoals, int opponentGoals) {
        MatchPerformance perf = new MatchPerformance();
        perf.setMatch(match);
        perf.setPlayer(player);

        // Minutos jogados
        perf.setMinutesPlayed(random.nextInt(10) < 9 ? 90 : 60 + random.nextInt(30));

        // Gols
        int goalChance = attrs.getFinishing() / 10;
        perf.setGoals(random.nextInt(100) < goalChance ? 1 + random.nextInt(2) : 0);

        // Assistências (baseado em passing)
        int assistChance = attrs.getShortPassing() / 12;
        perf.setAssists(random.nextInt(100) < assistChance ? 1 + random.nextInt(2) : 0);

        // Chutes
        perf.setShots(3 + random.nextInt(8));
        perf.setShotsOnTarget(perf.getShots() / 2 + random.nextInt(3));

        // Passes (baseado em passing)
        int passingLevel = attrs.getShortPassing();
        perf.setPassesAttempted(30 + random.nextInt(40));
        perf.setPassesCompleted((int) (perf.getPassesAttempted() * (passingLevel / 100.0)));

        // Desarmes (baseado em defending)
        int defendingLevel = attrs.getStandingTackle();
        perf.setTacklesAttempted(2 + random.nextInt(6));
        perf.setTacklesWon((int) (perf.getTacklesAttempted() * (defendingLevel / 100.0)));

        // Interceptações
        perf.setInterceptions(random.nextInt(4));

        // Faltas
        perf.setFoulsCommitted(random.nextInt(3));
        perf.setFoulsSuffered(random.nextInt(3));

        // Cartões (baixa chance)
        perf.setYellowCards(random.nextInt(100) < 15 ? 1 : 0);
        perf.setRedCards(random.nextInt(100) < 2 ? 1 : 0);

        // Calcular rating (0.0 - 10.0)
        BigDecimal rating = calculateRating(perf, teamGoals > opponentGoals);
        perf.setRating(rating);

        // hOMEM DA PARTIDA (se rating >= 8.5)
        perf.setManOfTheMatch(rating.compareTo(BigDecimal.valueOf(8.5)) >= 0);

        return perf;
    }

    // CALCULAR RATING
    private BigDecimal calculateRating(MatchPerformance perf, boolean teamWon) {
        double baseRating = 6.0;

        // Bônus por gols
        baseRating += perf.getGoals() * 0.8;

        // Bônus por assistências
        baseRating += perf.getAssists() * 0.5;

        // Bônus por passes
        double passAccuracy = perf.getPassAccuracy();
        if (passAccuracy > 80) baseRating += 0.3;
        if (passAccuracy > 90) baseRating += 0.2;

        // Penalidade por cartões
        baseRating -= perf.getYellowCards() * 0.3;
        baseRating -= perf.getRedCards() * 2.0;

        if (teamWon) {
            baseRating += 0.5;
        }

        baseRating = Math.max(0.0, Math.min(10.0, baseRating));

        return BigDecimal.valueOf(baseRating).setScale(1, RoundingMode.HALF_UP);
    }

    // GERAR EVENTOS DA PARTIDA
    private void generateMatchEvents(Match match, Player player, MatchPerformance performance) {
        // Gols
        for (int i = 0; i < performance.getGoals(); i++) {
            int minute = 1 + random.nextInt(90);
            MatchEvent event = new MatchEvent(match, player, MatchEventType.GOAL, minute, "Gol marcado!");
            eventRepository.save(event);
        }

        // Assistências
        for (int i = 0; i < performance.getAssists(); i++) {
            int minute = 1 + random.nextInt(90);
            MatchEvent event = new MatchEvent(match, player, MatchEventType.ASSIST, minute, "Assistência!");
            eventRepository.save(event);
        }

        // Cartões amarelos
        for (int i = 0; i < performance.getYellowCards(); i++) {
            int minute = 1 + random.nextInt(90);
            MatchEvent event = new MatchEvent(match, player, MatchEventType.YELLOW_CARD, minute, "Cartão amarelo por falta");
            eventRepository.save(event);
        }

        // Cartões vermelhos
        for (int i = 0; i < performance.getRedCards(); i++) {
            int minute = 1 + random.nextInt(90);
            MatchEvent event = new MatchEvent(match, player, MatchEventType.RED_CARD, minute, "Expulso!");
            eventRepository.save(event);
        }
    }

    // CALCULAR XP GANHO

    private int calculateXPGained(MatchPerformance performance, String outcome) {
        int baseXP = 50; // XP base por jogar

        // Bônus por vitória/empate/derrota
        baseXP += switch (outcome) {
            case "WIN" -> 30;
            case "DRAW" -> 15;
            case "LOSS" -> 5;
            default -> 0;
        };

        double rating = performance.getRating().doubleValue();
        if (rating >= 9.0) baseXP += 50;
        else if (rating >= 8.0) baseXP += 30;
        else if (rating >= 7.0) baseXP += 15;

        baseXP += performance.getGoals() * 10;
        baseXP += performance.getAssists() * 7;

        if (performance.getManOfTheMatch()) {
            baseXP += 25;
        }

        return baseXP;
    }


    private MatchResultDTO buildMatchResult(Match match, MatchPerformance performance, int xpGained) {
        MatchPerformanceDTO perfDTO = MatchPerformanceDTO.builder()
                .performanceId(performance.getId())
                .matchId(match.getId())
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

        return MatchResultDTO.builder()
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
                .playerPerformance(perfDTO)
                .xpGained(xpGained)
                .build();
    }
}