package com.careersim.careersim.match.model;

import com.careersim.careersim.player.model.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "match_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 30)
    private MatchEventType eventType;

    @Column(nullable = false)
    private Integer minute;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public MatchEvent(Match match, Player player, MatchEventType eventType, Integer minute, String description) {
        this.match = match;
        this.player = player;
        this.eventType = eventType;
        this.minute = minute;
        this.description = description;
    }

    public boolean isPositiveEvent() {
        return eventType == MatchEventType.GOAL ||
                eventType == MatchEventType.ASSIST ||
                eventType == MatchEventType. PENALTY_SCORED;
    }

    public boolean isNegativeEvent() {
        return eventType == MatchEventType.YELLOW_CARD ||
                eventType == MatchEventType.RED_CARD ||
                eventType == MatchEventType. PENALTY_MISSED ||
                eventType == MatchEventType.OWN_GOAL;
    }

    public String getEventDescription() {
        if (description != null && !description.isEmpty()) {
            return description;
        }

        String playerName =  player.getName();

        return switch (eventType) {
            case GOAL -> playerName + " marcou um gol!";
            case ASSIST -> playerName + " deu uma assistência!";
            case YELLOW_CARD -> playerName + " recebeu cartão amarelo";
            case RED_CARD -> playerName + " foi expulso!";
            case SUBSTITUTION_IN -> playerName + " entrou em campo";
            case SUBSTITUTION_OUT -> playerName + " saiu de campo";
            case INJURY -> playerName + " se machucou";
            case PENALTY_SCORED -> playerName + " converteu o pênalti! ";
            case PENALTY_MISSED -> playerName + " perdeu o pênalti!";
            case OWN_GOAL -> playerName + " fez gol contra";
        };
    }
}