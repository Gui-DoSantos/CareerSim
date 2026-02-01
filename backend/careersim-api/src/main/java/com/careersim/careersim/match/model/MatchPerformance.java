package com.careersim.careersim.match.model;

import com.careersim.careersim.player.model.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "match_performances", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"match_id", "player_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "minutes_played", nullable = false)
    private Integer minutesPlayed = 0;

    @Column(nullable = false)
    private Integer goals = 0;

    @Column(nullable = false)
    private Integer assists = 0;

    @Column(nullable = false)
    private Integer shots = 0;

    @Column(name = "shots_on_target", nullable = false)
    private Integer shotsOnTarget = 0;

    @Column(name = "passes_completed", nullable = false)
    private Integer passesCompleted = 0;

    @Column(name = "passes_attempted", nullable = false)
    private Integer passesAttempted = 0;

    @Column(name = "tackles_won", nullable = false)
    private Integer tacklesWon = 0;

    @Column(name = "tackles_attempted", nullable = false)
    private Integer tacklesAttempted = 0;

    @Column(nullable = false)
    private Integer interceptions = 0;

    @Column(name = "fouls_committed", nullable = false)
    private Integer foulsCommitted = 0;

    @Column(name = "fouls_suffered", nullable = false)
    private Integer foulsSuffered = 0;

    @Column(name = "yellow_cards", nullable = false)
    private Integer yellowCards = 0;

    @Column(name = "red_cards", nullable = false)
    private Integer redCards = 0;

    @Column(precision = 3, scale = 1)
    private BigDecimal rating;

    @Column(name = "man_of_the_match", nullable = false)
    private Boolean manOfTheMatch = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Double getPassAccuracy() {
        if (passesAttempted == 0) {
            return 0.0;
        }
        return (passesCompleted * 100.0) / passesAttempted;
    }

    public Double getTackleSuccessRate() {
        if (tacklesAttempted == 0) {
            return 0.0;
        }
        return (tacklesWon * 100.0) / tacklesAttempted;
    }

    public Double getShotAccuracy() {
        if (shots == 0) {
            return 0.0;
        }
        return (shotsOnTarget * 100.0) / shots;
    }

    public boolean hasPlayedFullMatch() {
        if (minutesPlayed >= 90) {
            return true;
        } else {
            return false;
        }
    }
}