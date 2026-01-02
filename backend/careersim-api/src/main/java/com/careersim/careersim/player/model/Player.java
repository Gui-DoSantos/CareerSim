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
    private String position;

    @Column(nullable = false)
    private Integer overall;

    @Column(nullable = false)
    private Integer potential;

    @Column(name = "club_id", columnDefinition = "BINARY(16)")
    private UUID clubId;


    private Player(String name, Integer age, String position, Integer overall, Integer potential, UUID clubId) {
        this.id = UUID. randomUUID();
        this.name = name;
        this. age = age;
        this. position = position;
        this. overall = overall;
        this. potential = potential;
        this. clubId = clubId;
    }


    public static Player create(String name, Integer age, String position, Integer overall, Integer potential, UUID clubId) {
        return new Player(name, age, position, overall, potential, clubId);
    }
}