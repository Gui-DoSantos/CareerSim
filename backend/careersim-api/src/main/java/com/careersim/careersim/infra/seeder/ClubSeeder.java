package com.careersim.careersim.infra.seeder;

import com.careersim.careersim.club.model.Club;
import com.careersim.careersim.club.model.ClubTier;
import com.careersim.careersim.club.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class ClubSeeder implements CommandLineRunner {

    private final ClubRepository clubRepository;

    @Override
    public void run(String... args) {

        // BRASIL
        // Gigantes Atuais
        create("Flamengo", ClubTier.TIER_5);
        create("Palmeiras", ClubTier.TIER_5);

        // Fortes / G-6
        create("Atlético-MG", ClubTier.TIER_4);
        create("São Paulo", ClubTier.TIER_4);
        create("Botafogo", ClubTier.TIER_4);
        create("Grêmio", ClubTier.TIER_4);
        create("Internacional", ClubTier.TIER_4);
        create("Fluminense", ClubTier.TIER_4);

        // Tradicionais / Série A
        create("Santos", ClubTier.TIER_3); // (Considerando história/tamanho)
        create("Corinthians", ClubTier.TIER_3);
        create("Cruzeiro", ClubTier.TIER_3);
        create("Vasco", ClubTier.TIER_3);
        create("Athletico-PR", ClubTier.TIER_3);
        create("Bahia", ClubTier.TIER_3);
        create("Bragantino", ClubTier.TIER_3);

        // Luta contra Z-4 / Acesso
        create("Vitória", ClubTier.TIER_2);
        create("Coritiba", ClubTier.TIER_2);
        create("Chapecoense", ClubTier.TIER_2);

        // Divisões de Acesso (Série B/C)
        create("Mirassol", ClubTier.TIER_1);
        create("Remo", ClubTier.TIER_1);


        // 󠁢󠁥󠁮󠁧󠁿 PREMIER LEAGUE (INGLATERRA)
        // Big Six + New Money
        create("Manchester City", ClubTier.TIER_5);
        create("Liverpool", ClubTier.TIER_5);
        create("Arsenal", ClubTier.TIER_5);
        create("Chelsea", ClubTier.TIER_4);
        create("Manchester United", ClubTier.TIER_4);
        create("Tottenham", ClubTier.TIER_4);
        create("Newcastle", ClubTier.TIER_4);

        // Fortes / Meio de Tabela
        create("Aston Villa", ClubTier.TIER_3);
        create("Brighton", ClubTier.TIER_3);
        create("West Ham", ClubTier.TIER_3);
        create("Everton", ClubTier.TIER_3);

        // Premier League Meio de Tabela/Baixo de Tabela
        create("Wolverhampton", ClubTier.TIER_2);
        create("Fulham", ClubTier.TIER_2);
        create("Crystal Palace", ClubTier.TIER_2);
        create("Brentford", ClubTier.TIER_2);
        create("Bournemouth", ClubTier.TIER_2);
        create("Nottingham Forest", ClubTier.TIER_2);
        create("Leeds United", ClubTier.TIER_2);
        create("Burnley", ClubTier.TIER_2);

        // (Divisão inferior )
        create("Sunderland", ClubTier.TIER_1);


        //  LA LIGA (ESPANHA)
        // Os Gigantes
        create("Real Madrid", ClubTier.TIER_5);
        create("Barcelona", ClubTier.TIER_5);
        create("Atlético de Madrid", ClubTier.TIER_4);

        // Europeus / Fortes
        create("Sevilla", ClubTier.TIER_3);
        create("Real Sociedad", ClubTier.TIER_3);
        create("Athletic Bilbao", ClubTier.TIER_3);
        create("Villarreal", ClubTier.TIER_3);
        create("Betis", ClubTier.TIER_3);
        create("Girona", ClubTier.TIER_3);
        create("Valencia", ClubTier.TIER_3);

        // La Liga Mid/Low
        create("Celta", ClubTier.TIER_2);
        create("Osasuna", ClubTier.TIER_2);
        create("Mallorca", ClubTier.TIER_2);
        create("Rayo Vallecano", ClubTier.TIER_2);
        create("Getafe", ClubTier.TIER_2);
        create("Espanyol", ClubTier.TIER_2);
        create("Alavés", ClubTier.TIER_2);

        // Luta para não cair
        create("Elche", ClubTier.TIER_1);
        create("Levante", ClubTier.TIER_1);
        create("Real Oviedo", ClubTier.TIER_1);

        System.out.println(" ClubSeeder: Todos os clubes foram carregados!");
    }

    private void create(String name, ClubTier tier) {
        if (!clubRepository.existsByName(name)) {
            Club club = Club.create(name, tier);
            clubRepository.save(club);
            System.out.println(" Clube criado: " + name + " [" + tier + "]");
        }
    }
}