package com.careersim.careersim.match.repository;

import com.careersim.careersim.match.model.MatchEvent;
import com.careersim.careersim.match.model.MatchEventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatchEventRepository extends JpaRepository<MatchEvent, UUID> {

    // Buscar todos os eventos de uma partida
    List<MatchEvent> findByMatchIdOrderByMinuteAsc(UUID matchId);

    // Buscar eventos de um player em uma partida
    List<MatchEvent> findByMatchIdAndPlayerIdOrderByMinuteAsc(UUID matchId, UUID playerId);

    // Buscar eventos de um player (todos)
    List<MatchEvent> findByPlayerIdOrderByCreatedAtDesc(UUID playerId);

    // Buscar eventos por tipo
    List<MatchEvent> findByMatchIdAndEventTypeOrderByMinuteAsc(UUID matchId, MatchEventType eventType);

    // Buscar gols de um player em uma partida
    @Query("SELECT e FROM MatchEvent e WHERE e.match.id = :matchId AND e.player.id = :playerId AND e.eventType = 'GOAL' ORDER BY e.minute ASC")
    List<MatchEvent> findGoalsByMatchAndPlayer(@Param("matchId") UUID matchId, @Param("playerId") UUID playerId);

    // Buscar assistências de um player em uma partida
    @Query("SELECT e FROM MatchEvent e WHERE e.match.id = :matchId AND e.player.id = :playerId AND e.eventType = 'ASSIST' ORDER BY e.minute ASC")
    List<MatchEvent> findAssistsByMatchAndPlayer(@Param("matchId") UUID matchId, @Param("playerId") UUID playerId);

    // Contar gols de um player (carreira toda)
    @Query("SELECT COUNT(e) FROM MatchEvent e WHERE e.player.id = :playerId AND e.eventType = 'GOAL'")
    Long countGoalsByPlayerId(@Param("playerId") UUID playerId);

    // Contar assistências de um player (carreira toda)
    @Query("SELECT COUNT(e) FROM MatchEvent e WHERE e.player.id = :playerId AND e.eventType = 'ASSIST'")
    Long countAssistsByPlayerId(@Param("playerId") UUID playerId);

    // Buscar eventos de uma partida por tipo
    List<MatchEvent> findByMatchIdAndEventType(UUID matchId, MatchEventType eventType);

    // Verificar se player fez hat trick
    @Query("SELECT COUNT(e) FROM MatchEvent e WHERE e.match.id = :matchId AND e.player.id = :playerId AND e.eventType = 'GOAL'")
    Long countGoalsInMatch(@Param("matchId") UUID matchId, @Param("playerId") UUID playerId);

    // Buscar último evento de uma partida
    @Query("SELECT e FROM MatchEvent e WHERE e.match.id = :matchId ORDER BY e.minute DESC, e.createdAt DESC")
    List<MatchEvent> findLastEventsByMatch(@Param("matchId") UUID matchId);
}