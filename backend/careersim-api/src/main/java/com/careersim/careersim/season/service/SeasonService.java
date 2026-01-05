package com.careersim.careersim.season.service;

import com.careersim.careersim.player.model.Player;
import com.careersim.careersim.player.repository.PlayerRepository;
import com.careersim.careersim.season.dto.SeasonResponseDTO;
import com.careersim.careersim.season.model.Season;
import com.careersim.careersim.season.model.SeasonStatus;
import com.careersim.careersim.season.repository.SeasonRepository;
import jakarta.transaction.Transactional;
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
    @Transactional
    public SeasonResponseDTO createSeason(UUID playerId, Integer year) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        Season season = Season.create(player, year);
        Season saved = seasonRepository.save(season);

        return toDTO(saved);
    }


    // Buscar Temporada

    public SeasonResponseDTO getActiveSeason(UUID playerId) {
        Season season = seasonRepository.findByPlayer_IdAndStatus(playerId, SeasonStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Temporada ativa não encontrada"));

        return toDTO(season);
    // Avançar
}
        @Transactional
        public SeasonResponseDTO advanceSeason(UUID playerId) {
            Season season = seasonRepository.findByPlayer_IdAndStatus(playerId, SeasonStatus.ACTIVE)
                    .orElseThrow(() -> new RuntimeException("Temporada ativa não encontrada"));

            if (season.getCurrentEvent() >= 38) {
                throw new RuntimeException("Temporada já finalizada");
            }

            season.advance();

            if (season. getCurrentEvent() == 38) {
                season.complete();
            }

            Season saved = seasonRepository.save(season);
            return toDTO(saved);
    }

    private SeasonResponseDTO toDTO(Season season) {
        return new SeasonResponseDTO(
                season.getId(),
                season.getPlayerId(),
                season.getYear(),
                season.getCurrentEvent(),
                season.getStatus()
        );
    }
}
