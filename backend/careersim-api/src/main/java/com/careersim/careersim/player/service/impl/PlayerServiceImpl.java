package com.careersim.careersim.player.service.impl;

import com.careersim.careersim.player.dto.CreatePlayerRequestDTO;
import com.careersim.careersim.player.dto.PlayerResponseDTO;
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
    public PlayerResponseDTO createPlayer(CreatePlayerRequestDTO request) {

        Player player = Player.create(
                request.name(),
                request. age(),
                request.position(),
                request.overall(),
                request.potential(),
                request. clubId()
        );

        Player saved = playerRepository.save(player);

        return new PlayerResponseDTO(
                saved.getId(),
                saved. getName(),
                saved.getAge(),
                saved.getPosition(),
                saved.getOverall(),
                saved.getPotential(),
                saved.getClubId()
        );
    }
}
