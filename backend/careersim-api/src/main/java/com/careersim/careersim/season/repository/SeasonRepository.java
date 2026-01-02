package com.careersim.careersim.season.repository;

import com.careersim.careersim.season.model.Season;
import com.careersim.careersim.season.model.SeasonStatus;
import org.springframework.data.jpa. repository.JpaRepository;
import java.util.UUID;

public interface SeasonRepository extends JpaRepository<Season, UUID> {

    Season findByPlayerIdAndStatus(UUID playerId, SeasonStatus status);
}