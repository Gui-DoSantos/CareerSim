package com.careersim.careersim.season.controller;

import com.careersim.careersim.season.dto.SeasonResponseDTO;
import com.careersim.careersim.season.model.Season;
import com.careersim.careersim.season.service.SeasonService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/seasons")
public class SeasonController {

    private final SeasonService seasonService;

    public SeasonController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SeasonResponseDTO createSeason(@RequestParam UUID playerId, @RequestParam Integer year) {
        return seasonService.createSeason(playerId, year);  // ✅ minúsculo
    }

    @GetMapping("/active")
    public SeasonResponseDTO getActiveSeason(@RequestParam UUID playerId) {
        return seasonService.getActiveSeason(playerId);  // ✅ retorna DTO
    }

    @PostMapping("/advance")
    public SeasonResponseDTO advanceEvent(@RequestParam UUID playerId) {
        return seasonService.advanceSeason(playerId);  // ✅ nome correto
    }
}