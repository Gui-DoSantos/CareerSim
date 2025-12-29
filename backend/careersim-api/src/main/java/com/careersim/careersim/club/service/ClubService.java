package com.careersim.careersim.club.service;

import com.careersim.careersim.club.model.Club;
import com.careersim.careersim.club.model.ClubTier;


import java.util.List;
import java.util.UUID;

public interface ClubService {
    Club createClub(String name, ClubTier tier);
    List<Club> findAll();
}
