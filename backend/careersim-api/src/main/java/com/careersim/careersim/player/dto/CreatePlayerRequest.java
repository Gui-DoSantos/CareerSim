package com.careersim.careersim.player.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CreatePlayerRequest {

    private String name;
    private String position;
    private UUID clubId;
}
