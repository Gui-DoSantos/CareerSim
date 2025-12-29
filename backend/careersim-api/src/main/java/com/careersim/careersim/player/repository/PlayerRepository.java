package com.careersim.careersim.player.repository;

import com.careersim.careersim.player.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlayerRepository  extends JpaRepository<Player, UUID> {

}
