package com.careersim.careersim.player.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



import static com.careersim.careersim.player.model.Position.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerAttributes {

    // ========== ATTACKING (5) ==========
    @Column(nullable = false)
    private Integer finishing = 50;

    @Column(name = "shot_power", nullable = false)
    private Integer shotPower = 50;

    @Column(name = "long_shots", nullable = false)
    private Integer longShots = 50;

    @Column(nullable = false)
    private Integer positioning = 50;

    @Column(nullable = false)
    private Integer heading = 50;

    // ========== SKILL (4) ==========
    @Column(nullable = false)
    private Integer dribbling = 50;

    @Column(name = "ball_control", nullable = false)
    private Integer ballControl = 50;

    @Column(name = "short_passing", nullable = false)
    private Integer shortPassing = 50;

    @Column(name = "long_passing", nullable = false)
    private Integer longPassing = 50;

    // ========== MOVEMENT (3) ==========
    @Column(nullable = false)
    private Integer acceleration = 50;

    @Column(name = "sprint_speed", nullable = false)
    private Integer sprintSpeed = 50;

    @Column(nullable = false)
    private Integer agility = 50;

    // ========== DEFENDING (3) ==========
    @Column(nullable = false)
    private Integer marking = 50;

    @Column(name = "standing_tackle", nullable = false)
    private Integer standingTackle = 50;

    @Column(name = "sliding_tackle", nullable = false)
    private Integer slidingTackle = 50;

    // ========== PHYSICAL (3) ==========
    @Column(nullable = false)
    private Integer stamina = 50;

    @Column(nullable = false)
    private Integer strength = 50;

    @Column(nullable = false)
    private Integer jumping = 50;


    public Integer calculateOverall(Position position) {
        return switch (position) {
            case ST, CF -> calculateStrikerOverall();
            case CAM -> calculateCamOverall();
            case CM -> calculateCmOverall();
            case CB -> calculateCbOverall();
            case LB, RB -> calculateFullbackOverall();
            case GK -> calculateGkOverall();
    };
}

    private Integer calculateStrikerOverall() {
        return (int)  Math.round((
                finishing * 0.35 +
                        shotPower * 0.20 +
                        positioning * 0.20 +
                        longShots * 0.10 +
                        dribbling * 0.10 +
                        ballControl * 0.10 +
                        acceleration * 0.15 +
                        sprintSpeed * 0.10 +
                        agility * 0.08 +
                        strength * 0.07 +
                        heading * 0.12 +
                        stamina * 0.05 +
                        shortPassing * 0.05 +
                        marking * 0.01 +
                        standingTackle * 0.01
        ) / 1.69);
    }

    private Integer calculateCamOverall() {
        return (int) Math.round((
                shortPassing * 0.25 +
                        longPassing * 0.20 +
                        dribbling * 0.20 +
                        ballControl * 0.15 +
                        vision() * 0.15 +
                        finishing * 0.15 +
                        shotPower * 0.10 +
                        longShots * 0.10 +
                        agility * 0.15 +
                        acceleration * 0.10 +
                        stamina * 0.10 +
                        positioning * 0.10 +
                        strength * 0.05 +
                        marking * 0.02
        ) / 1.82);
    }

    private Integer calculateCmOverall() {
        return (int) Math.round((
                shortPassing * 0.25 +
                        longPassing * 0.20 +
                        ballControl * 0.15 +
                        marking * 0.15 +
                        standingTackle * 0.12 +
                        stamina * 0.20 +
                        strength * 0.10 +
                        agility * 0.10 +
                        acceleration * 0.08 +
                        dribbling * 0.10 +
                        finishing * 0.05 +
                        positioning * 0.05 +
                        slidingTackle * 0.08
        ) / 1.63);
    }

    private Integer calculateCbOverall() {
        return (int) Math.round((
                marking * 0.30 +
                        standingTackle * 0.25 +
                        slidingTackle * 0.20 +
                        strength * 0.20 +
                        jumping * 0.15 +
                        heading * 0.15 +
                        shortPassing * 0.10 +
                        acceleration * 0.08 +
                        agility * 0.07 +
                        stamina * 0.10 +
                        ballControl * 0.05 +
                        longPassing * 0.05 +
                        positioning * 0.02
        ) / 1.72);
    }

    private Integer calculateFullbackOverall() {
        return (int) Math.round((
                marking * 0.22 +
                        standingTackle * 0.20 +
                        acceleration * 0.18 +
                        sprintSpeed * 0.15 +
                        stamina * 0.18 +
                        shortPassing * 0.15 +
                        agility * 0.12 +
                        strength * 0.10 +
                        slidingTackle * 0.15 +
                        ballControl * 0.10 +
                        dribbling * 0.08 +
                        longPassing * 0.08 +
                        heading * 0.08 +
                        jumping * 0.08
        ) / 1.87);
    }

    private Integer calculateGkOverall() {

        return (int) Math.round((
                marking * 0.25 +
                        agility * 0.25 +
                        positioning * 0.20 +
                        strength * 0.15 +
                        acceleration * 0.10 +
                        jumping * 0.15 +
                        longPassing * 0.10
        ) / 1.20);
    }

    private Integer vision() {
        return (shortPassing + longPassing) / 2;
    }

    // variações de jogadores
    public static PlayerAttributes createForPosition(Position position, int baseLevel) {
        PlayerAttributes attrs = new PlayerAttributes();

        switch (position) {
            case ST, CF -> {
                attrs.finishing = randomize(baseLevel, 10);
                attrs.shotPower = randomize(baseLevel, 8);
                attrs.longShots = randomize(baseLevel, 5);
                attrs.positioning = randomize(baseLevel, 8);
                attrs.heading = randomize(baseLevel, 5);
                attrs.dribbling = randomize(baseLevel, 5);
                attrs.ballControl = randomize(baseLevel, 5);
                attrs.shortPassing = randomize(baseLevel, 0);
                attrs.longPassing = randomize(baseLevel, -5);
                attrs.acceleration = randomize(baseLevel, 5);
                attrs.sprintSpeed = randomize(baseLevel, 5);
                attrs.agility = randomize(baseLevel, 3);
                attrs.marking = randomize(baseLevel, -20);
                attrs.standingTackle = randomize(baseLevel, -20);
                attrs.slidingTackle = randomize(baseLevel, -20);
                attrs.stamina = randomize(baseLevel, 0);
                attrs.strength = randomize(baseLevel, 3);
                attrs.jumping = randomize(baseLevel, 5);
            }
            case CAM -> {
                attrs.finishing = randomize(baseLevel, 5);
                attrs.shotPower = randomize(baseLevel, 3);
                attrs.longShots = randomize(baseLevel, 8);
                attrs.positioning = randomize(baseLevel, 5);
                attrs.heading = randomize(baseLevel, -5);
                attrs.dribbling = randomize(baseLevel, 10);
                attrs.ballControl = randomize(baseLevel, 10);
                attrs.shortPassing = randomize(baseLevel, 10);
                attrs.longPassing = randomize(baseLevel, 8);
                attrs.acceleration = randomize(baseLevel, 5);
                attrs.sprintSpeed = randomize(baseLevel, 3);
                attrs.agility = randomize(baseLevel, 8);
                attrs.marking = randomize(baseLevel, -15);
                attrs.standingTackle = randomize(baseLevel, -15);
                attrs. slidingTackle = randomize(baseLevel, -15);
                attrs.stamina = randomize(baseLevel, 3);
                attrs.strength = randomize(baseLevel, -3);
                attrs.jumping = randomize(baseLevel, 0);
            }
            case CM -> {
                attrs.finishing = randomize(baseLevel, 0);
                attrs.shotPower = randomize(baseLevel, 0);
                attrs.longShots = randomize(baseLevel, 3);
                attrs.positioning = randomize(baseLevel, 3);
                attrs.heading = randomize(baseLevel, 0);
                attrs.dribbling = randomize(baseLevel, 5);
                attrs.ballControl = randomize(baseLevel, 5);
                attrs.shortPassing = randomize(baseLevel, 10);
                attrs.longPassing = randomize(baseLevel, 8);
                attrs.acceleration = randomize(baseLevel, 3);
                attrs.sprintSpeed = randomize(baseLevel, 3);
                attrs.agility = randomize(baseLevel, 5);
                attrs.marking = randomize(baseLevel, 5);
                attrs.standingTackle = randomize(baseLevel, 5);
                attrs. slidingTackle = randomize(baseLevel, 3);
                attrs.stamina = randomize(baseLevel, 8);
                attrs.strength = randomize(baseLevel, 3);
                attrs.jumping = randomize(baseLevel, 0);
            }
            case CB -> {
                attrs.finishing = randomize(baseLevel, -20);
                attrs.shotPower = randomize(baseLevel, -15);
                attrs.longShots = randomize(baseLevel, -20);
                attrs.positioning = randomize(baseLevel, -10);
                attrs.heading = randomize(baseLevel, 10);
                attrs. dribbling = randomize(baseLevel, -10);
                attrs.ballControl = randomize(baseLevel, -5);
                attrs.shortPassing = randomize(baseLevel, 0);
                attrs.longPassing = randomize(baseLevel, 0);
                attrs.acceleration = randomize(baseLevel, -5);
                attrs.sprintSpeed = randomize(baseLevel, -5);
                attrs.agility = randomize(baseLevel, -5);
                attrs.marking = randomize(baseLevel, 15);
                attrs.standingTackle = randomize(baseLevel, 12);
                attrs.slidingTackle = randomize(baseLevel, 10);
                attrs.stamina = randomize(baseLevel, 0);
                attrs.strength = randomize(baseLevel, 10);
                attrs.jumping = randomize(baseLevel, 10);
            }
            case LB, RB -> {
                attrs.finishing = randomize(baseLevel, -15);
                attrs.shotPower = randomize(baseLevel, -10);
                attrs.longShots = randomize(baseLevel, -10);
                attrs.positioning = randomize(baseLevel, -5);
                attrs.heading = randomize(baseLevel, 3);
                attrs.dribbling = randomize(baseLevel, 3);
                attrs.ballControl = randomize(baseLevel, 3);
                attrs.shortPassing = randomize(baseLevel, 5);
                attrs.longPassing = randomize(baseLevel, 3);
                attrs.acceleration = randomize(baseLevel, 8);
                attrs.sprintSpeed = randomize(baseLevel, 8);
                attrs.agility = randomize(baseLevel, 5);
                attrs.marking = randomize(baseLevel, 10);
                attrs.standingTackle = randomize(baseLevel, 10);
                attrs. slidingTackle = randomize(baseLevel, 8);
                attrs.stamina = randomize(baseLevel, 10);
                attrs.strength = randomize(baseLevel, 5);
                attrs.jumping = randomize(baseLevel, 5);
            }
            case GK -> {
                attrs.marking = randomize(baseLevel, 10);
                attrs. agility = randomize(baseLevel, 8);
                attrs.positioning = randomize(baseLevel, 10);
                attrs.strength = randomize(baseLevel, 5);
                attrs.acceleration = randomize(baseLevel, 0);
                attrs.jumping = randomize(baseLevel, 8);
                attrs.longPassing = randomize(baseLevel, 5);

                // irrelevante
                attrs.finishing = 30;
                attrs.shotPower = 30;
                attrs. longShots = 30;
                attrs.heading = 40;
                attrs.dribbling = 40;
                attrs. ballControl = 45;
                attrs.shortPassing = 50;
                attrs.sprintSpeed = 45;
                attrs.standingTackle = 40;
                attrs.slidingTackle = 35;
                attrs.stamina = 50;
            }
        }

        attrs.normalize();
        return attrs;
    }


    private static Integer randomize(int base, int modifier) {
        int value = base + modifier + (int) (Math.random() * 10 - 5);
        return Math.max(30, Math.min(85, value));
    }

    private void normalize() {
        this.finishing = clamp(this.finishing);
        this.shotPower = clamp(this.shotPower);
        this.longShots = clamp(this.longShots);
        this.positioning = clamp(this.positioning);
        this.heading = clamp(this.heading);
        this.dribbling = clamp(this.dribbling);
        this.ballControl = clamp(this.ballControl);
        this.shortPassing = clamp(this.shortPassing);
        this.longPassing = clamp(this.longPassing);
        this.acceleration = clamp(this.acceleration);
        this.sprintSpeed = clamp(this. sprintSpeed);
        this.agility = clamp(this. agility);
        this.marking = clamp(this.marking);
        this.standingTackle = clamp(this.standingTackle);
        this.slidingTackle = clamp(this.slidingTackle);
        this.stamina = clamp(this.stamina);
        this.strength = clamp(this.strength);
        this.jumping = clamp(this.jumping);
    }

    private Integer clamp(Integer value) {
        return Math.max(30, Math.min(99, value));
    }



    public void evolveAttribute(String attributeName, int growth) {
        growth = Math.max(1, Math.min(5, growth));  // Entre 1-5

        switch (attributeName. toLowerCase()) {
            case "finishing" -> this.finishing = clamp(this.finishing + growth);
            case "shotpower" -> this.shotPower = clamp(this.shotPower + growth);
            case "longshots" -> this.longShots = clamp(this.longShots + growth);
            case "positioning" -> this.positioning = clamp(this.positioning + growth);
            case "heading" -> this. heading = clamp(this.heading + growth);
            case "dribbling" -> this.dribbling = clamp(this. dribbling + growth);
            case "ballcontrol" -> this. ballControl = clamp(this. ballControl + growth);
            case "shortpassing" -> this. shortPassing = clamp(this.shortPassing + growth);
            case "longpassing" -> this.longPassing = clamp(this.longPassing + growth);
            case "acceleration" -> this.acceleration = clamp(this.acceleration + growth);
            case "sprintspeed" -> this.sprintSpeed = clamp(this.sprintSpeed + growth);
            case "agility" -> this.agility = clamp(this.agility + growth);
            case "marking" -> this. marking = clamp(this.marking + growth);
            case "standingtackle" -> this. standingTackle = clamp(this.standingTackle + growth);
            case "slidingtackle" -> this. slidingTackle = clamp(this.slidingTackle + growth);
            case "stamina" -> this.stamina = clamp(this.stamina + growth);
            case "strength" -> this.strength = clamp(this.strength + growth);
            case "jumping" -> this.jumping = clamp(this.jumping + growth);
        }
    }
}
