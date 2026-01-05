package com.careersim.careersim.league.repository;

import com.careersim.careersim.league.model.Country;
import com.careersim.careersim.league.model.League;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeagueRepository extends JpaRepository<League, UUID> {

    List<League> findByCountry(Country country);

    Optional<League> findByName(String name);

    List<League> findAllByOrderByPrestigeDesc();
}