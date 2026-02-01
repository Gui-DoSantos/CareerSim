package com.careersim.careersim.match.repository;

import com.careersim.careersim.match.model.Match;
import com.careersim.careersim.match.model.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {

    // Buscar todas as partidas de um player
    List<Match> findByPlayerIdOrderByMatchDateDesc(UUID playerId);

    // Buscar partidas de um player por status
    List<Match> findByPlayerIdAndStatusOrderByMatchDateDesc(UUID playerId, MatchStatus status);

    // Buscar partidas agendadas de um player
    List<Match> findByPlayerIdAndStatus(UUID playerId, MatchStatus status);

    // Buscar partidas de um clube
    List<Match> findByClubIdOrderByMatchDateDesc(UUID clubId);

    // Buscar partidas entre duas datas
    List<Match> findByPlayerIdAndMatchDateBetweenOrderByMatchDateDesc(
            UUID playerId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    // Buscar próximas N partidas agendadas
    @Query("SELECT m FROM Match m WHERE m.player.id = :playerId AND m.status = 'SCHEDULED' ORDER BY m.matchDate ASC")
    List<Match> findUpcomingMatches(@Param("playerId") UUID playerId);

    // Buscar últimas N partidas finalizadas
    @Query("SELECT m FROM Match m WHERE m.player.id = :playerId AND m.status = 'FINISHED' ORDER BY m.matchDate DESC")
    List<Match> findRecentFinishedMatches(@Param("playerId") UUID playerId);

    // Contar vitórias de um player
    @Query("SELECT COUNT(m) FROM Match m WHERE m.player.id = :playerId AND m.status = 'FINISHED' AND m.playerTeamScore > m.opponentTeamScore")
    Long countWinsByPlayerId(@Param("playerId") UUID playerId);

    // Contar derrotas de um player
    @Query("SELECT COUNT(m) FROM Match m WHERE m.player.id = :playerId AND m.status = 'FINISHED' AND m.playerTeamScore < m.opponentTeamScore")
    Long countLossesByPlayerId(@Param("playerId") UUID playerId);

    // Contar empates de um player
    @Query("SELECT COUNT(m) FROM Match m WHERE m.player.id = :playerId AND m.status = 'FINISHED' AND m.playerTeamScore = m.opponentTeamScore")
    Long countDrawsByPlayerId(@Param("playerId") UUID playerId);

    // Buscar partidas por competição
    List<Match> findByPlayerIdAndCompetitionOrderByMatchDateDesc(UUID playerId, String competition);

    // Verificar se player tem partida agendada em uma data específica
    boolean existsByPlayerIdAndMatchDateAndStatus(UUID playerId, LocalDateTime matchDate, MatchStatus status);
}