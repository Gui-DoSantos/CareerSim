package com.careersim.careersim.league.controller;

import com.careersim.careersim.league.dto.LeagueResponseDTO;
import com.careersim.careersim.league.model.Country;
import com.careersim.careersim.league.service.LeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/leagues")
@RequiredArgsConstructor
public class LeagueController {

    private final LeagueService leagueService;

    // GET /leagues
    @GetMapping
    public List<LeagueResponseDTO> getAllLeagues() {
        return leagueService.getAllLeagues();
    }

    // GET /leagues/{id}
    @GetMapping("/{id}")
    public LeagueResponseDTO getLeagueById(@PathVariable UUID id) {
        return leagueService.getLeagueById(id);
    }

    // GET /leagues/country/{country}
    @GetMapping("/country/{country}")
    public List<LeagueResponseDTO> getLeaguesByCountry(@PathVariable Country country) {
        return leagueService.getLeaguesByCountry(country);
    }

    // GET /leagues/top
    @GetMapping("/top")
    public List<LeagueResponseDTO> getTopLeagues() {
        return leagueService.getTopLeagues();
    }
}