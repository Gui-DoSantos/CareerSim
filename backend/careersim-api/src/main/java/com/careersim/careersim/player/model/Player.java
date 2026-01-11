package com.careersim.careersim.player.model;

import com.careersim.careersim.club.model.Club;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "players")
@Getter
@NoArgsConstructor
public class Player {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Integer level = 1;

    @Column(nullable = false)
    private Integer experience = 0;

    @Column(name = "training_points", nullable = false)
    private Integer trainingPoints = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Position position;

    @Column(nullable = false)
    private Integer overall;

    @Embedded
    private PlayerAttributes attributes;

    @Column(nullable = false)
    private Integer potential;

    @Column(name = "club_id", columnDefinition = "BINARY(16)")
    private UUID clubId;


    private Player(String name, Integer age, Position  position, Integer overall, Integer potential, UUID clubId) {
        this.id = UUID. randomUUID();
        this.name = name;
        this.age = age;
        this.position = position;
        this.overall = overall;
        this.potential = potential;
        this.clubId = clubId;
        this.level = 1;
        this. experience = 0;
        this.trainingPoints = 0;

        this.attributes = PlayerAttributes.createForPosition(position, overall);

        this.overall = this.attributes.calculateOverall(position);
    }


    public static Player create(String name, Integer age, Position position, Integer overall, Integer potential, UUID clubId) {
        return new Player(name, age, position, overall, potential, clubId);
    }

    @PreUpdate
    protected void onUpdate() {
        if (this.attributes != null && this.position != null) {
            this.overall = this.attributes.calculateOverall(this.position);
        }
    }

    public boolean gainExperience(int xp) {
        this.experience += xp;

        int xpNeeded = calculateXpForNextLevel();

        if (this.experience >= xpNeeded) {
            levelUp();
            return true;
        }
        return false;
    }

    private void levelUp() {
        this.level++;
        this.trainingPoints += 3;
        this.experience -= calculateXpForNextLevel();
    }

    public int calculateXpForNextLevel() {

        return (int) (100 * Math.pow(this.level, 1.5));
    }


    public boolean spendTrainingPoints(int cost) {
        if (this. trainingPoints >= cost) {
            this.trainingPoints -= cost;
            return true;
        }
        return false;
    }
}