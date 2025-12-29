package com.careersim.careersim.player.service;

import com.careersim.careersim.player.model.Player;

import java.util.UUID;

public interface PlayerService {
    Player createPlayer(String name, String position, UUID clubId);
}
