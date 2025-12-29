package com.careersim.careersim.club.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private ClubTier tier;


    private Club(String name, ClubTier tier) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.tier = tier;
    }

    public static Club create(String name, ClubTier tier) {
        return new Club(name, tier);
    }
}