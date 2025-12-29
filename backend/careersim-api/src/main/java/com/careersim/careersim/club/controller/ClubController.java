package com.careersim.careersim.club.controller;

import com.careersim.careersim.club.dto.CreateClubRequest;
import com.careersim.careersim.club.model.Club;
import com.careersim.careersim.club.service.ClubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubs")
public class ClubController {

    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @PostMapping
    public ResponseEntity<Club> create(@RequestBody CreateClubRequest request) {
        Club club = clubService.createClub(
                request.getName(),
                request.getTier()

        );

        return ResponseEntity.ok(club);
    }

    @GetMapping
    public ResponseEntity<List<Club>> listAll() {
        List<Club> clubs = clubService.findAll();
        return ResponseEntity.ok(clubs);
    }
}
