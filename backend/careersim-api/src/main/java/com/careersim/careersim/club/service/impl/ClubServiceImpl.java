package com.careersim.careersim.club.service.impl;

import com.careersim.careersim.club.model.Club;
import com.careersim.careersim.club.model.ClubTier;
import com.careersim.careersim.club.repository.ClubRepository;
import com.careersim.careersim.club.service.ClubService;
import com.careersim.careersim.player.model.Player;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;

    public ClubServiceImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Override
    public Club createClub(String name, ClubTier tier) {
    if (clubRepository.existsByName(name)) {
        throw new IllegalArgumentException("Já existe um clube registrado com o nome: " + name);
    }

    Club newClub = Club.create(name, tier);

        return clubRepository.save(newClub);
    }
    @Override
    public List<Club> findAll() {
        return clubRepository.findAll();
    }
}
