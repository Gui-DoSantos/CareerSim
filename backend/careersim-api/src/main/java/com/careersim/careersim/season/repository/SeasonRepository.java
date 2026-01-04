package com.careersim.careersim.season.repository;

import com.careersim.careersim.season.model.Season;
import com.careersim.careersim.season.model.SeasonStatus;
import org.springframework.data.jpa. repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SeasonRepository extends JpaRepository<Season, UUID> {

    Optional<Season> findByPlayer_IdAndStatus(UUID playerId, SeasonStatus status);
}