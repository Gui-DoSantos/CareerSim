package com.careersim.careersim.player.controller;

import com.careersim.careersim.player.dto.CreatePlayerRequest;
import com.careersim.careersim.player.model.Player;
import com.careersim.careersim.player.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public ResponseEntity<Player> create(@RequestBody CreatePlayerRequest request) {

        Player player = playerService.createPlayer(
                request.getName(),
                request.getPosition(),
                request.getClubId()
        );

        return ResponseEntity.ok(player);
    }
}
