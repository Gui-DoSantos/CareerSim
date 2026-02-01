package com.careersim.careersim.event.controller;

import com.careersim.careersim.event.dto.CreateEventRequest;
import com.careersim.careersim.event.dto.EventDTO;
import com.careersim.careersim.event.dto.EventSummaryDTO;
import com.careersim.careersim.event.model.EventType;
import com.careersim.careersim.event.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/players/{playerId}/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;


    @PostMapping
    public ResponseEntity<EventDTO> createEvent(
            @PathVariable UUID playerId,
            @Valid @RequestBody CreateEventRequest request
    ) {
        EventDTO event = eventService.createEvent(playerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }


    @GetMapping
    public ResponseEntity<List<EventDTO>> getPlayerEvents(@PathVariable UUID playerId) {
        List<EventDTO> events = eventService.getPlayerEvents(playerId);
        return ResponseEntity.ok(events);
    }


    @GetMapping("/unread")
    public ResponseEntity<List<EventDTO>> getUnreadEvents(@PathVariable UUID playerId) {
        List<EventDTO> events = eventService.getUnreadEvents(playerId);
        return ResponseEntity.ok(events);
    }


    @GetMapping("/type/{eventType}")
    public ResponseEntity<List<EventDTO>> getEventsByType(
            @PathVariable UUID playerId,
            @PathVariable EventType eventType
    ) {
        List<EventDTO> events = eventService.getEventsByType(playerId, eventType);
        return ResponseEntity.ok(events);
    }


    @GetMapping("/recent")
    public ResponseEntity<List<EventDTO>> getRecentEvents(
            @PathVariable UUID playerId,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<EventDTO> events = eventService.getRecentEvents(playerId, limit);
        return ResponseEntity.ok(events);
    }


    @GetMapping("/unread/count")
    public ResponseEntity<Long> countUnreadEvents(@PathVariable UUID playerId) {
        Long count = eventService.countUnreadEvents(playerId);
        return ResponseEntity.ok(count);
    }


    @GetMapping("/summary")
    public ResponseEntity<EventSummaryDTO> getEventSummary(@PathVariable UUID playerId) {
        EventSummaryDTO summary = eventService.getEventSummary(playerId);
        return ResponseEntity.ok(summary);
    }


    @PatchMapping("/{eventId}/read")
    public ResponseEntity<EventDTO> markAsRead(@PathVariable UUID eventId) {
        EventDTO event = eventService.markAsRead(eventId);
        return ResponseEntity.ok(event);
    }


    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable UUID playerId) {
        eventService.markAllAsRead(playerId);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/cleanup")
    public ResponseEntity<Void> deleteOldEvents(
            @PathVariable UUID playerId,
            @RequestParam(defaultValue = "30") int daysOld
    ) {
        eventService.deleteOldEvents(playerId, daysOld);
        return ResponseEntity.noContent().build();
    }
}