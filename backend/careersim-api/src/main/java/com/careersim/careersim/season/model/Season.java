package com.careersim.careersim.season.model;

import com.careersim.careersim.player.model.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "seasons")
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    private Integer year;

    @Column(name = "current_event", nullable = false)
    private Integer currentEvent = 0;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeasonStatus status = SeasonStatus.ACTIVE;

    public Season(Player player, Integer year) {
        this.player = player;
        this.year = year;
        this.currentEvent = 0;
        this.status = SeasonStatus.ACTIVE;
    }

    public static Season create(Player player, Integer year) {
        return new Season(player, year);
    }

    public void advance() {
        this.currentEvent++;
    }

    public void complete() {
        this.status = SeasonStatus.FINISHED;
    }

    public UUID getPlayerId() {
        return this.player != null ? this.player.getId() : null;
    }

}
