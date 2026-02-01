package com.careersim.careersim.match.model;

import com.careersim.careersim.player.model.Player;
import com.careersim.careersim.club.model.Club;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Column(name = "opponent_name", nullable = false, length = 100)
    private String opponentName;

    @Column(nullable = false, length = 100)
    private String competition;

    @Column(name = "match_date", nullable = false)
    private LocalDateTime matchDate;

    @Column(name = "is_home", nullable = false)
    private Boolean isHome = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MatchStatus status = MatchStatus.SCHEDULED;

    @Column(name = "player_team_score")
    private Integer playerTeamScore;

    @Column(name = "opponent_team_score")
    private Integer opponentTeamScore;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchPerformance> performances = new ArrayList<>();

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchEvent> events = new ArrayList<>();

    // Métodos auxiliares
    public void addPerformance(MatchPerformance performance) {
        performances.add(performance);
        performance.setMatch(this);
    }

    public void addEvent(MatchEvent event) {
        events.add(event);
        event.setMatch(this);
    }

    public boolean isFinished() {
        return status == MatchStatus.FINISHED;
    }

    public String getResult() {
        if (playerTeamScore == null || opponentTeamScore == null) {
            return "N/A";
        }
        return playerTeamScore + " x " + opponentTeamScore;
    }

    public String getMatchOutcome() {
        if (!isFinished()) {
            return "NOT_FINISHED";
        }
        if (playerTeamScore > opponentTeamScore) {
            return "WIN";
        } else if (playerTeamScore < opponentTeamScore) {
            return "LOSS";
        } else {
            return "DRAW";
        }
    }
}