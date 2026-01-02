package com.careersim.careersim.player.service;

import com.careersim.careersim.player.dto.CreatePlayerRequestDTO;
import com.careersim.careersim.player.dto.PlayerResponseDTO;
import com.careersim.careersim.player.model.Player;

import java.util.UUID;

public interface PlayerService {
    PlayerResponseDTO createPlayer(CreatePlayerRequestDTO request);
}
