package com.careersim.careersim.event.repository;

import com.careersim.careersim.event.model.Event;
import com.careersim.careersim.event.model.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    // Buscar eventos de um player (ordenado por data, mais recente primeiro)
    List<Event> findByPlayerIdOrderByOccurredAtDesc(UUID playerId);

    // Buscar eventos não lidos de um player
    List<Event> findByPlayerIdAndIsReadFalseOrderByOccurredAtDesc(UUID playerId);

    // Buscar eventos por tipo
    List<Event> findByPlayerIdAndEventTypeOrderByOccurredAtDesc(UUID playerId, EventType eventType);

    // Buscar eventos entre datas
    List<Event> findByPlayerIdAndOccurredAtBetweenOrderByOccurredAtDesc(
            UUID playerId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    // Contar eventos não lidos
    Long countByPlayerIdAndIsReadFalse(UUID playerId);

    // Buscar eventos por referência (exemplo: todos eventos de uma partida específica)
    List<Event> findByReferenceIdAndReferenceTypeOrderByOccurredAtDesc(UUID referenceId, String referenceType);

    // Buscar últimos N eventos
    @Query(value = "SELECT e FROM Event e WHERE e.player.id = :playerId ORDER BY e.occurredAt DESC LIMIT :limit")
    List<Event> findRecentEvents(@Param("playerId") UUID playerId, @Param("limit") int limit);

    // Marcar todos como lidos
    @Query("UPDATE Event e SET e.isRead = true WHERE e.player.id = :playerId AND e.isRead = false")
    void markAllAsReadByPlayerId(@Param("playerId") UUID playerId);

    // Deletar eventos antigos
    void deleteByPlayerIdAndOccurredAtBefore(UUID playerId, LocalDateTime date);
}