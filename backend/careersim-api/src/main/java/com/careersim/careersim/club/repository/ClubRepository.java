package com.careersim.careersim.club.repository;

import com.careersim.careersim.club.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClubRepository extends JpaRepository<Club, UUID> {


    boolean existsByName(String name);
}