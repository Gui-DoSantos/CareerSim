package com.careersim.careersim.event.model;

import com.careersim.careersim.player.model.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 50)
    private EventType eventType;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "reference_id")
    private UUID referenceId;

    @Column(name = "reference_type", length = 50)
    private String referenceType;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    public Event(Player player, EventType eventType, String title, String description, LocalDateTime occurredAt) {
        this.player = player;
        this.eventType = eventType;
        this.title = title;
        this.description = description;
        this.occurredAt = occurredAt;
        this.isRead = false;
    }

    public Event(Player player, EventType eventType, String title, String description,
                 UUID referenceId, String referenceType, LocalDateTime occurredAt) {
        this.player = player;
        this.eventType = eventType;
        this.title = title;
        this.description = description;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.occurredAt = occurredAt;
        this.isRead = false;
    }

    public void markAsRead() {
        this.isRead = true;
    }

    public void markAsUnread() {
        this.isRead = false;
    }

    public boolean hasReference() {
        return referenceId != null && referenceType != null;
    }
}