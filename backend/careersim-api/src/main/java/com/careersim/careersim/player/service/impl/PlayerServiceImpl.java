package com.careersim.careersim.player.service.impl;

import com.careersim.careersim.player.model.Player;
import com.careersim.careersim.player.repository.PlayerRepository;
import com.careersim.careersim.player.service.PlayerService;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;

    }

    @Override
    public Player createPlayer(String name, String position, UUID clubId) {

        

        return null;
    }
}
