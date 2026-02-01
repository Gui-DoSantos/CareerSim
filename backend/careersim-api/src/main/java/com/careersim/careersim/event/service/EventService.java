package com.careersim.careersim.event.service;

import com.careersim.careersim.event.dto.CreateEventRequest;
import com.careersim.careersim.event.dto.EventDTO;
import com.careersim.careersim.event.dto.EventSummaryDTO;
import com.careersim.careersim.event.model.Event;
import com.careersim.careersim.event.model.EventType;
import com.careersim.careersim.event.repository.EventRepository;
import com.careersim.careersim.player.model.Player;
import com.careersim.careersim.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final PlayerRepository playerRepository;


    @Transactional
    public EventDTO createEvent(UUID playerId, CreateEventRequest request) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player não encontrado"));

        LocalDateTime occurredAt = request.getOccurredAt();
        if (occurredAt == null) {
            occurredAt = LocalDateTime.now();
        }

        Event event = new Event(
                player,
                request.getEventType(),
                request.getTitle(),
                request.getDescription(),
                request.getReferenceId(),
                request.getReferenceType(),
                occurredAt
        );

        Event savedEvent = eventRepository.save(event);

        return convertToDTO(savedEvent);
    }

    @Transactional
    public void createSystemEvent(UUID playerId, EventType eventType, String title, String description,
                                  String referenceId, String referenceType) {
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            return;
        }


        UUID refId = null;
        try {
            if (referenceId != null && !referenceId.isEmpty()) {
                refId = UUID.fromString(referenceId);
            }
        } catch (IllegalArgumentException e) {
            refId = null;
        }

        Event event = new Event(player, eventType, title, description, refId, referenceType, LocalDateTime.now());
        eventRepository.save(event);
    }


    @Transactional
    public void createSystemEvent(UUID playerId, EventType eventType, String title, String description) {
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            return;
        }

        Event event = new Event(player, eventType, title, description, LocalDateTime.now());
        eventRepository.save(event);
    }


    @Transactional
    public void createSystemEvent(UUID playerId, EventType eventType, String title, String description,
                                  UUID referenceId, String referenceType) {
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            return;
        }

        Event event = new Event(player, eventType, title, description, referenceId, referenceType, LocalDateTime.now());
        eventRepository.save(event);
    }


    @Transactional(readOnly = true)
    public List<EventDTO> getPlayerEvents(UUID playerId) {
        List<Event> events = eventRepository.findByPlayerIdOrderByOccurredAtDesc(playerId);

        List<EventDTO> result = new ArrayList<>();
        for (Event event : events) {
            result.add(convertToDTO(event));
        }

        return result;
    }


    @Transactional(readOnly = true)
    public List<EventDTO> getUnreadEvents(UUID playerId) {
        List<Event> events = eventRepository.findByPlayerIdAndIsReadFalseOrderByOccurredAtDesc(playerId);

        List<EventDTO> result = new ArrayList<>();
        for (Event event : events) {
            result.add(convertToDTO(event));
        }

        return result;
    }


    @Transactional(readOnly = true)
    public List<EventDTO> getEventsByType(UUID playerId, EventType eventType) {
        List<Event> events = eventRepository.findByPlayerIdAndEventTypeOrderByOccurredAtDesc(playerId, eventType);

        List<EventDTO> result = new ArrayList<>();
        for (Event event : events) {
            result.add(convertToDTO(event));
        }

        return result;
    }


    @Transactional(readOnly = true)
    public List<EventDTO> getRecentEvents(UUID playerId, int limit) {
        List<Event> events = eventRepository.findRecentEvents(playerId, limit);

        List<EventDTO> result = new ArrayList<>();
        for (Event event : events) {
            result.add(convertToDTO(event));
        }

        return result;
    }


    @Transactional
    public EventDTO markAsRead(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.markAsRead();
        Event updatedEvent = eventRepository.save(event);

        return convertToDTO(updatedEvent);
    }


    @Transactional
    public void markAllAsRead(UUID playerId) {
        List<Event> unreadEvents = eventRepository.findByPlayerIdAndIsReadFalseOrderByOccurredAtDesc(playerId);

        for (Event event : unreadEvents) {
            event.markAsRead();
            eventRepository.save(event);
        }
    }


    @Transactional(readOnly = true)
    public Long countUnreadEvents(UUID playerId) {
        return eventRepository.countByPlayerIdAndIsReadFalse(playerId);
    }

    @Transactional(readOnly = true)
    public EventSummaryDTO getEventSummary(UUID playerId) {
        List<Event> allEvents = eventRepository.findByPlayerIdOrderByOccurredAtDesc(playerId);

        long total = allEvents.size();
        long unread = 0;
        long matchEvents = 0;
        long progressionEvents = 0;
        long transferEvents = 0;
        long awardEvents = 0;

        for (Event event : allEvents) {
            if (!event.getIsRead()) {
                unread++;
            }

            EventType type = event.getEventType();

            if (type == EventType.MATCH_SCHEDULED || type == EventType.MATCH_RESULT) {
                matchEvents++;
            } else if (type == EventType.LEVEL_UP || type == EventType.SKILL_UNLOCKED) {
                progressionEvents++;
            } else if (type == EventType.TRANSFER_OFFER || type == EventType.TRANSFER_COMPLETED) {
                transferEvents++;
            } else if (type == EventType.AWARD || type == EventType.MILESTONE) {
                awardEvents++;
            }
        }

        return EventSummaryDTO.builder()
                .totalEvents(total)
                .unreadEvents(unread)
                .matchEvents(matchEvents)
                .progressionEvents(progressionEvents)
                .transferEvents(transferEvents)
                .awardEvents(awardEvents)
                .build();
    }

    @Transactional
    public void deleteEvent(UUID eventId) {
        eventRepository.deleteById(eventId);
    }

    @Transactional
    public void deleteOldEvents(UUID playerId, int daysOld) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        eventRepository.deleteByPlayerIdAndOccurredAtBefore(playerId, cutoffDate);
    }


    private EventDTO convertToDTO(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .playerId(event.getPlayer().getId())
                .eventType(event.getEventType())
                .title(event.getTitle())
                .description(event.getDescription())
                .referenceId(event.getReferenceId())
                .referenceType(event.getReferenceType())
                .isRead(event.getIsRead())
                .occurredAt(event.getOccurredAt())
                .createdAt(event.getCreatedAt())
                .build();
    }
}