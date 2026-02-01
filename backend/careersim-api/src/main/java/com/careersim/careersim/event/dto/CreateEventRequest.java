package com.careersim.careersim.event.dto;

import com.careersim.careersim.event.model.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {

    @NotNull(message = "Event type is required")
    private EventType eventType;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private UUID referenceId;

    private String referenceType;

    private LocalDateTime occurredAt; // se nao informado usa o momento atual
}