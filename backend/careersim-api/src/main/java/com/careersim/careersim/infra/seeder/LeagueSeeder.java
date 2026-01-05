package com.careersim.careersim.infra.seeder;

import com.careersim.careersim.league.model.Country;
import com.careersim.careersim.league.model.League;
import com.careersim.careersim.league.repository.LeagueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@RequiredArgsConstructor
public class LeagueSeeder implements CommandLineRunner {

    private final LeagueRepository leagueRepository;

    @Override
    public void run(String...  args) {
        if (leagueRepository.count() == 0) {
            seedLeagues();
        }
    }

    private void seedLeagues() {
        leagueRepository.save(new League("Premier League", Country.ENGLAND, 95));
        leagueRepository.save(new League("La Liga", Country.SPAIN, 94));
        leagueRepository.save(new League("Brasileirão Série A", Country. BRAZIL, 75));

        System.out.println("3 ligas inseridas com sucesso!");
    }
}