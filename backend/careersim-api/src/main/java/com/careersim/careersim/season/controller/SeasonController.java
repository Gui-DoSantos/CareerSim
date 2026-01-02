package com.careersim.careersim.season.controller;

import com.careersim.careersim.season.model.Season;
import com.careersim.careersim.season.service.SeasonService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/seasons")
public class SeasonController {

    private final SeasonService seasonService;

    public SeasonController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    // POST /api/seasons? playerId=xxx&year=2025
    @PostMapping
    public Season createSeason(@RequestParam UUID playerId, @RequestParam Integer year) {
        return seasonService.CreateSeason(playerId, year);
    }

    // GET /api/seasons/active?playerId=xxx
    @GetMapping("/active")
    public Season getActiveSeason(@RequestParam UUID playerId) {
        return seasonService.getActiveSeason(playerId);
    }

    // POST /api/seasons/advance?playerId=xxx
    @PostMapping("/advance")
    public Season advanceEvent(@RequestParam UUID playerId) {
        return seasonService.advenceSeason(playerId);
    }
}