package com.careersim.careersim.season.service;

import com.careersim.careersim.player.model.Player;
import com.careersim.careersim.player.repository.PlayerRepository;
import com.careersim.careersim.season.model.Season;
import com.careersim.careersim.season.model.SeasonStatus;
import com.careersim.careersim.season.repository.SeasonRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SeasonService {

    private final SeasonRepository seasonRepository;
    private final PlayerRepository playerRepository;

    public SeasonService(SeasonRepository seasonRepository, PlayerRepository playerRepository) {
        this.seasonRepository = seasonRepository;
        this.playerRepository = playerRepository;
    }

    // Criar temporada
    public Season CreateSeason(UUID id, Integer year) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        Season season = new Season(player, year);
        return seasonRepository.save(season);
    }

    // Buscar Temporada

    public Season getActiveSeason(UUID id) {
        return seasonRepository.findByPlayerIdAndStatus(id, SeasonStatus.ACTIVE);
    }

    // Avançar

    public Season advenceSeason(UUID id) {
        Season season =  getActiveSeason(id);

        if (season.getCurrentEvent() >= 39) {
            throw new RuntimeException("Temporada já finalizada");
        }

        season.setCurrentEvent(season.getCurrentEvent() + 1);

        if (season.getCurrentEvent() == 39) {
            season. setStatus(SeasonStatus.FINISHED);
        }

        return seasonRepository.save(season);
    }
}
