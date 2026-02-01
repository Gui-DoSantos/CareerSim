package com.careersim.careersim.match.repository;

import com.careersim.careersim.match.model.MatchPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchPerformanceRepository extends JpaRepository<MatchPerformance, UUID> {

    // Buscar performance de um player em uma partida específica
    Optional<MatchPerformance> findByMatchIdAndPlayerId(UUID matchId, UUID playerId);

    // Buscar todas as performances de um player
    List<MatchPerformance> findByPlayerIdOrderByCreatedAtDesc(UUID playerId);

    // Buscar performances por partida
    List<MatchPerformance> findByMatchId(UUID matchId);

    // Buscar performances
    List<MatchPerformance> findByPlayerIdAndManOfTheMatchTrueOrderByCreatedAtDesc(UUID playerId);

    // Calcular total de gols de um player
    @Query("SELECT COALESCE(SUM(mp.goals), 0) FROM MatchPerformance mp WHERE mp.player.id = :playerId")
    Long getTotalGoalsByPlayerId(@Param("playerId") UUID playerId);

    // Calcular total de assistências de um player
    @Query("SELECT COALESCE(SUM(mp.assists), 0) FROM MatchPerformance mp WHERE mp.player.id = :playerId")
    Long getTotalAssistsByPlayerId(@Param("playerId") UUID playerId);

    // Calcular média de rating de um player
    @Query("SELECT AVG(mp.rating) FROM MatchPerformance mp WHERE mp.player.id = :playerId AND mp.rating IS NOT NULL")
    BigDecimal getAverageRatingByPlayerId(@Param("playerId") UUID playerId);

    // Contar partidas jogadas (pelo menos 1 minuto)
    @Query("SELECT COUNT(mp) FROM MatchPerformance mp WHERE mp.player.id = :playerId AND mp.minutesPlayed > 0")
    Long countMatchesPlayedByPlayerId(@Param("playerId") UUID playerId);

    // Contar o homem da partida
    @Query("SELECT COUNT(mp) FROM MatchPerformance mp WHERE mp.player.id = :playerId AND mp.manOfTheMatch = true")
    Long countManOfTheMatchByPlayerId(@Param("playerId") UUID playerId);

    // Buscar melhor performance (maior rating)
    @Query("SELECT mp FROM MatchPerformance mp WHERE mp.player.id = :playerId AND mp.rating IS NOT NULL ORDER BY mp.rating DESC")
    List<MatchPerformance> findTopPerformancesByPlayerId(@Param("playerId") UUID playerId);

    // Buscar performances recentes (últimas N partidas)
    @Query("SELECT mp FROM MatchPerformance mp WHERE mp.player.id = :playerId ORDER BY mp.createdAt DESC")
    List<MatchPerformance> findRecentPerformances(@Param("playerId") UUID playerId);

    // Contar cartões amarelos total
    @Query("SELECT COALESCE(SUM(mp.yellowCards), 0) FROM MatchPerformance mp WHERE mp.player.id = :playerId")
    Long getTotalYellowCardsByPlayerId(@Param("playerId") UUID playerId);

    // Contar cartões vermelhos total
    @Query("SELECT COALESCE(SUM(mp.redCards), 0) FROM MatchPerformance mp WHERE mp.player.id = :playerId")
    Long getTotalRedCardsByPlayerId(@Param("playerId") UUID playerId);
}