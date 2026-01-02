package com.careersim.careersim.player.controller;

import com.careersim.careersim.player.dto.CreatePlayerRequestDTO;
import com.careersim.careersim.player.dto.PlayerResponseDTO;
import com.careersim.careersim.player.model.Player;
import com.careersim.careersim.player.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerResponseDTO create(@RequestBody CreatePlayerRequestDTO request) {
        return playerService.createPlayer(request);
    }
}
