package com.careersim.careersim.player.model;

import com.careersim.careersim.club.model.Club;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false, length = 20)
    private  String position;

    @Column(nullable = false)
    private int  overall;

    @Column(nullable = false)
    private int potential;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;


    protected Player() {}
}
