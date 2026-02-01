package com.careersim.careersim.stats.service;

import com.careersim.careersim.match.model.Match;
import com.careersim.careersim.match.model.MatchPerformance;
import com.careersim.careersim.match.model.MatchStatus;
import com.careersim.careersim.match.repository.MatchPerformanceRepository;
import com.careersim.careersim.match.repository.MatchRepository;
import com.careersim.careersim.player.model.Player;
import com.careersim.careersim.player.repository.PlayerRepository;
import com.careersim.careersim.statistics.dto.CareerStatsDTO;
import com.careersim.careersim.statistics.dto.CompetitionStatsDTO;
import com.careersim.careersim.statistics.dto.SeasonStatsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final MatchPerformanceRepository performanceRepository;


    // ESTATÍSTICAS DA CARREIRA TODA

    @Transactional(readOnly = true)
    public CareerStatsDTO getCareerStats(UUID playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));


        long wins = matchRepository.countWinsByPlayerId(playerId);
        long draws = matchRepository.countDrawsByPlayerId(playerId);
        long losses = matchRepository.countLossesByPlayerId(playerId);
        long totalMatches = wins + draws + losses;

        long matchesPlayed = performanceRepository.countMatchesPlayedByPlayerId(playerId);
        long totalGoals = performanceRepository.getTotalGoalsByPlayerId(playerId);
        long totalAssists = performanceRepository.getTotalAssistsByPlayerId(playerId);
        BigDecimal avgRating = performanceRepository.getAverageRatingByPlayerId(playerId);
        long manOfTheMatch = performanceRepository.countManOfTheMatchByPlayerId(playerId);
        long yellowCards = performanceRepository.getTotalYellowCardsByPlayerId(playerId);
        long redCards = performanceRepository.getTotalRedCardsByPlayerId(playerId);


        List<MatchPerformance> performances = performanceRepository.findByPlayerIdOrderByCreatedAtDesc(playerId);


        long matchesStarted = 0;
        long totalShots = 0;
        long shotsOnTarget = 0;
        long totalPasses = 0;
        long passesCompleted = 0;
        long totalTackles = 0;
        long tacklesWon = 0;
        long totalInterceptions = 0;
        long totalFoulsCommitted = 0;
        long totalFoulsSuffered = 0;
        long totalMinutesPlayed = 0;

        BigDecimal bestRating = BigDecimal.ZERO;
        BigDecimal worstRating = BigDecimal.valueOf(10);
        int mostGoalsInMatch = 0;
        int mostAssistsInMatch = 0;


        for (MatchPerformance perf : performances) {

            if (perf.getMinutesPlayed() >= 60) {
                matchesStarted++;
            }


            totalShots += perf.getShots();
            shotsOnTarget += perf.getShotsOnTarget();
            totalPasses += perf.getPassesAttempted();
            passesCompleted += perf.getPassesCompleted();
            totalTackles += perf.getTacklesAttempted();
            tacklesWon += perf.getTacklesWon();
            totalInterceptions += perf.getInterceptions();
            totalFoulsCommitted += perf.getFoulsCommitted();
            totalFoulsSuffered += perf.getFoulsSuffered();
            totalMinutesPlayed += perf.getMinutesPlayed();


            if (perf.getRating() != null) {
                if (perf.getRating().compareTo(bestRating) > 0) {
                    bestRating = perf.getRating();
                }


                if (perf.getRating().compareTo(worstRating) < 0) {
                    worstRating = perf.getRating();
                }
            }


            if (perf.getGoals() > mostGoalsInMatch) {
                mostGoalsInMatch = perf.getGoals();
            }


            if (perf.getAssists() > mostAssistsInMatch) {
                mostAssistsInMatch = perf.getAssists();
            }
        }


        double winRate = 0.0;
        if (totalMatches > 0) {
            winRate = (wins * 100.0) / totalMatches;
        }

        double shotAccuracy = 0.0;
        if (totalShots > 0) {
            shotAccuracy = (shotsOnTarget * 100.0) / totalShots;
        }

        double passAccuracy = 0.0;
        if (totalPasses > 0) {
            passAccuracy = (passesCompleted * 100.0) / totalPasses;
        }

        double tackleSuccessRate = 0.0;
        if (totalTackles > 0) {
            tackleSuccessRate = (tacklesWon * 100.0) / totalTackles;
        }

        double goalsPerMatch = 0.0;
        if (matchesPlayed > 0) {
            goalsPerMatch = (double) totalGoals / matchesPlayed;
        }

        double assistsPerMatch = 0.0;
        if (matchesPlayed > 0) {
            assistsPerMatch = (double) totalAssists / matchesPlayed;
        }

        double avgMinutesPerMatch = 0.0;
        if (matchesPlayed > 0) {
            avgMinutesPerMatch = (double) totalMinutesPlayed / matchesPlayed;
        }

        double manOfTheMatchRate = 0.0;
        if (matchesPlayed > 0) {
            manOfTheMatchRate = (manOfTheMatch * 100.0) / matchesPlayed;
        }


        winRate = arredondar(winRate, 2);
        shotAccuracy = arredondar(shotAccuracy, 2);
        passAccuracy = arredondar(passAccuracy, 2);
        tackleSuccessRate = arredondar(tackleSuccessRate, 2);
        goalsPerMatch = arredondar(goalsPerMatch, 2);
        assistsPerMatch = arredondar(assistsPerMatch, 2);
        avgMinutesPerMatch = arredondar(avgMinutesPerMatch, 2);
        manOfTheMatchRate = arredondar(manOfTheMatchRate, 2);


        String playerName = player.getName();


        if (avgRating == null) {
            avgRating = BigDecimal.ZERO;
        }
        if (bestRating.compareTo(BigDecimal.ZERO) == 0) {
            bestRating = BigDecimal.ZERO;
        }
        if (worstRating.compareTo(BigDecimal.valueOf(10)) == 0) {
            worstRating = BigDecimal.ZERO;
        }


        return CareerStatsDTO.builder()
                .playerId(playerId)
                .playerName(playerName)
                .totalMatches(totalMatches)
                .matchesPlayed(matchesPlayed)
                .matchesStarted(matchesStarted)
                .wins(wins)
                .draws(draws)
                .losses(losses)
                .winRate(winRate)
                .totalGoals(totalGoals)
                .totalAssists(totalAssists)
                .goalContributions(totalGoals + totalAssists)
                .goalsPerMatch(goalsPerMatch)
                .assistsPerMatch(assistsPerMatch)
                .totalShots(totalShots)
                .shotsOnTarget(shotsOnTarget)
                .shotAccuracy(shotAccuracy)
                .totalPasses(totalPasses)
                .passesCompleted(passesCompleted)
                .passAccuracy(passAccuracy)
                .totalTackles(totalTackles)
                .tacklesWon(tacklesWon)
                .tackleSuccessRate(tackleSuccessRate)
                .totalInterceptions(totalInterceptions)
                .totalYellowCards(yellowCards)
                .totalRedCards(redCards)
                .totalFoulsCommitted(totalFoulsCommitted)
                .totalFoulsSuffered(totalFoulsSuffered)
                .averageRating(avgRating.setScale(1, RoundingMode.HALF_UP))
                .bestRating(bestRating.setScale(1, RoundingMode.HALF_UP))
                .worstRating(worstRating.setScale(1, RoundingMode.HALF_UP))
                .manOfTheMatchCount(manOfTheMatch)
                .manOfTheMatchRate(manOfTheMatchRate)
                .totalMinutesPlayed(totalMinutesPlayed)
                .averageMinutesPerMatch(avgMinutesPerMatch)
                .mostGoalsInMatch(mostGoalsInMatch)
                .mostAssistsInMatch(mostAssistsInMatch)
                .build();
    }


    // ESTATÍSTICAS POR TEMPORADA

    @Transactional(readOnly = true)
    public List<SeasonStatsDTO> getSeasonStats(UUID playerId) {

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));


        List<Match> matches = matchRepository.findByPlayerIdAndStatusOrderByMatchDateDesc(playerId, MatchStatus.FINISHED);


        List<MatchPerformance> allPerformances = performanceRepository.findByPlayerIdOrderByCreatedAtDesc(playerId);


        Map<UUID, MatchPerformance> performanceMap = new HashMap<>();
        for (MatchPerformance perf : allPerformances) {
            performanceMap.put(perf.getMatch().getId(), perf);
        }


        Map<Integer, List<Match>> matchesPorAno = new HashMap<>();

        for (Match match : matches) {
            int ano = match.getMatchDate().getYear();


            if (!matchesPorAno.containsKey(ano)) {
                matchesPorAno.put(ano, new ArrayList<>());
            }


            matchesPorAno.get(ano).add(match);
        }


        List<SeasonStatsDTO> resultado = new ArrayList<>();

        for (Map.Entry<Integer, List<Match>> entry : matchesPorAno.entrySet()) {
            int ano = entry.getKey();
            List<Match> partidasDoAno = entry.getValue();


            long wins = 0;
            long draws = 0;
            long losses = 0;

            for (Match match : partidasDoAno) {
                String outcome = match.getMatchOutcome();

                if (outcome.equals("WIN")) {
                    wins++;
                } else if (outcome.equals("DRAW")) {
                    draws++;
                } else if (outcome.equals("LOSS")) {
                    losses++;
                }
            }


            List<MatchPerformance> performancesDoAno = new ArrayList<>();
            for (Match match : partidasDoAno) {
                MatchPerformance perf = performanceMap.get(match.getId());
                if (perf != null) {
                    performancesDoAno.add(perf);
                }
            }


            long totalGoals = 0;
            long totalAssists = 0;
            long totalMinutes = 0;
            long yellowCards = 0;
            long redCards = 0;
            long manOfTheMatch = 0;
            BigDecimal somaRatings = BigDecimal.ZERO;
            int countRatings = 0;

            for (MatchPerformance perf : performancesDoAno) {
                totalGoals += perf.getGoals();
                totalAssists += perf.getAssists();
                totalMinutes += perf.getMinutesPlayed();
                yellowCards += perf.getYellowCards();
                redCards += perf.getRedCards();

                if (perf.getManOfTheMatch()) {
                    manOfTheMatch++;
                }

                if (perf.getRating() != null) {
                    somaRatings = somaRatings.add(perf.getRating());
                    countRatings++;
                }
            }


            double goalsPerMatch = 0.0;
            double assistsPerMatch = 0.0;
            if (partidasDoAno.size() > 0) {
                goalsPerMatch = (double) totalGoals / partidasDoAno.size();
                assistsPerMatch = (double) totalAssists / partidasDoAno.size();
            }

            BigDecimal avgRating = BigDecimal.ZERO;
            if (countRatings > 0) {
                avgRating = somaRatings.divide(BigDecimal.valueOf(countRatings), 1, RoundingMode.HALF_UP);
            }

            SeasonStatsDTO seasonDTO = SeasonStatsDTO.builder()
                    .season(ano)
                    .totalMatches((long) partidasDoAno.size())
                    .wins(wins)
                    .draws(draws)
                    .losses(losses)
                    .totalGoals(totalGoals)
                    .totalAssists(totalAssists)
                    .goalsPerMatch(arredondar(goalsPerMatch, 2))
                    .assistsPerMatch(arredondar(assistsPerMatch, 2))
                    .averageRating(avgRating)
                    .manOfTheMatchCount(manOfTheMatch)
                    .totalMinutesPlayed(totalMinutes)
                    .yellowCards(yellowCards)
                    .redCards(redCards)
                    .build();

            resultado.add(seasonDTO);
        }


        resultado.sort((a, b) -> b.getSeason().compareTo(a.getSeason()));

        return resultado;
    }


    // ESTATÍSTICAS POR COMPETIÇÃO

    @Transactional(readOnly = true)
    public List<CompetitionStatsDTO> getCompetitionStats(UUID playerId) {

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));


        List<Match> matches = matchRepository.findByPlayerIdAndStatusOrderByMatchDateDesc(playerId, MatchStatus.FINISHED);


        List<MatchPerformance> allPerformances = performanceRepository.findByPlayerIdOrderByCreatedAtDesc(playerId);


        Map<UUID, MatchPerformance> performanceMap = new HashMap<>();
        for (MatchPerformance perf : allPerformances) {
            performanceMap.put(perf.getMatch().getId(), perf);
        }


        Map<String, List<Match>> matchesPorCompeticao = new HashMap<>();

        for (Match match : matches) {
            String competition = match.getCompetition();

            if (!matchesPorCompeticao.containsKey(competition)) {
                matchesPorCompeticao.put(competition, new ArrayList<>());
            }

            matchesPorCompeticao.get(competition).add(match);
        }


        List<CompetitionStatsDTO> resultado = new ArrayList<>();

        for (Map.Entry<String, List<Match>> entry : matchesPorCompeticao.entrySet()) {
            String competition = entry.getKey();
            List<Match> partidasDaCompeticao = entry.getValue();


            long wins = 0;
            long draws = 0;
            long losses = 0;

            for (Match match : partidasDaCompeticao) {
                String outcome = match.getMatchOutcome();

                if (outcome.equals("WIN")) {
                    wins++;
                } else if (outcome.equals("DRAW")) {
                    draws++;
                } else if (outcome.equals("LOSS")) {
                    losses++;
                }
            }


            List<MatchPerformance> performancesDaCompeticao = new ArrayList<>();
            for (Match match : partidasDaCompeticao) {
                MatchPerformance perf = performanceMap.get(match.getId());
                if (perf != null) {
                    performancesDaCompeticao.add(perf);
                }
            }


            long totalGoals = 0;
            long totalAssists = 0;
            long totalMinutes = 0;
            long manOfTheMatch = 0;
            BigDecimal somaRatings = BigDecimal.ZERO;
            int countRatings = 0;

            for (MatchPerformance perf : performancesDaCompeticao) {
                totalGoals += perf.getGoals();
                totalAssists += perf.getAssists();
                totalMinutes += perf.getMinutesPlayed();

                if (perf.getManOfTheMatch()) {
                    manOfTheMatch++;
                }

                if (perf.getRating() != null) {
                    somaRatings = somaRatings.add(perf.getRating());
                    countRatings++;
                }
            }


            double goalsPerMatch = 0.0;
            if (partidasDaCompeticao.size() > 0) {
                goalsPerMatch = (double) totalGoals / partidasDaCompeticao.size();
            }

            BigDecimal avgRating = BigDecimal.ZERO;
            if (countRatings > 0) {
                avgRating = somaRatings.divide(BigDecimal.valueOf(countRatings), 1, RoundingMode.HALF_UP);
            }


            CompetitionStatsDTO compDTO = CompetitionStatsDTO.builder()
                    .competition(competition)
                    .totalMatches((long) partidasDaCompeticao.size())
                    .wins(wins)
                    .draws(draws)
                    .losses(losses)
                    .totalGoals(totalGoals)
                    .totalAssists(totalAssists)
                    .goalsPerMatch(arredondar(goalsPerMatch, 2))
                    .averageRating(avgRating)
                    .manOfTheMatchCount(manOfTheMatch)
                    .totalMinutesPlayed(totalMinutes)
                    .build();

            resultado.add(compDTO);
        }


        resultado.sort((a, b) -> b.getTotalMatches().compareTo(a.getTotalMatches()));

        return resultado;
    }


    //  ARREDONDAR
    private double arredondar(double valor, int casasDecimais) {
        BigDecimal bd = BigDecimal.valueOf(valor);
        bd = bd.setScale(casasDecimais, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}