package com.careersim.careersim.league.service;

import com.careersim.careersim.league.dto.LeagueResponseDTO;
import com.careersim.careersim.league.model.Country;
import com.careersim.careersim.league.model.League;
import com.careersim.careersim.league.repository.LeagueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeagueService {

    private final LeagueRepository leagueRepository;

    public List<LeagueResponseDTO> getAllLeagues() {
        return leagueRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public LeagueResponseDTO getLeagueById(UUID id) {
        League league = leagueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Liga não encontrada"));

        return toDTO(league);
    }

    public List<LeagueResponseDTO> getLeaguesByCountry(Country country) {
        return leagueRepository.findByCountry(country)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<LeagueResponseDTO> getTopLeagues() {
        return leagueRepository.findAllByOrderByPrestigeDesc()
                .stream()
                .map(this:: toDTO)
                .collect(Collectors.toList());
    }

    private LeagueResponseDTO toDTO(League league) {
        return new LeagueResponseDTO(
                league.getId(),
                league. getName(),
                league.getCountry(),
                league.getPrestige()
        );
    }
}