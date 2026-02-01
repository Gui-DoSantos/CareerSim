package com.careersim.careersim.event.dto;

import com.careersim.careersim.event.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDTO {

    private UUID id;
    private UUID playerId;
    private EventType eventType;
    private String title;
    private String description;
    private UUID referenceId;
    private String referenceType;
    private Boolean isRead;
    private LocalDateTime occurredAt;
    private LocalDateTime createdAt;
}