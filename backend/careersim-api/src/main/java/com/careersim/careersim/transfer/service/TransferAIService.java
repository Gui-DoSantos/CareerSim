package com.careersim.careersim.transfer.service;

import com.careersim.careersim.club.model.Club;
import com.careersim.careersim.club.repository.ClubRepository;
import com.careersim.careersim.event.model.EventType;
import com.careersim.careersim.event.service.EventService;
import com.careersim.careersim.player.model.Player;
import com.careersim.careersim.player.repository.PlayerRepository;
import com.careersim.careersim.transfer.model.TransferOffer;
import com.careersim.careersim.transfer.model.TransferOfferStatus;
import com.careersim.careersim.transfer.repository.TransferOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferAIService {

    private final TransferOfferRepository offerRepository;
    private final PlayerRepository playerRepository;
    private final ClubRepository clubRepository;
    private final EventService eventService;
    private final Random random = new Random();


    // GERAR OFERTAS AUTOMÁTICAS PRO PLAYER
    @Transactional
    public List<TransferOffer> generateOffersForPlayer(UUID playerId, int numberOfOffers) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player não encontrado"));


        List<Club> allClubs = clubRepository.findAll();


        List<Club> availableClubs = new ArrayList<>();
        for (Club club : allClubs) {
            if (player.getClubId() == null || !club.getId().equals(player.getClubId())) {
                availableClubs.add(club);
            }
        }

        if (availableClubs.isEmpty()) {
            return new ArrayList<>();
        }


        List<TransferOffer> offers = new ArrayList<>();
        int offersToGenerate = Math.min(numberOfOffers, availableClubs.size());

        for (int i = 0; i < offersToGenerate; i++) {

            int randomIndex = random.nextInt(availableClubs.size());
            Club toClub = availableClubs.get(randomIndex);
            availableClubs.remove(randomIndex);


            boolean alreadyHasOffer = offerRepository.existsByPlayerIdAndToClubIdAndStatus(
                    playerId, toClub.getId(), TransferOfferStatus.PENDING
            );

            if (alreadyHasOffer) {
                continue;
            }

            // Calcular valores da oferta
            long transferAmount = calculateTransferValue(player);
            long salary = calculateSalary(player, toClub);
            int contractYears = calculateContractYears(player);
            LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);


            Club fromClub = null;
            if (player.getClubId() != null) {
                fromClub = clubRepository.findById(player.getClubId()).orElse(null);
            }

            TransferOffer offer = new TransferOffer(
                    player,
                    fromClub,
                    toClub,
                    transferAmount,
                    salary,
                    contractYears,
                    expiresAt
            );

            TransferOffer savedOffer = offerRepository.save(offer);
            offers.add(savedOffer);

            String eventTitle = "Nova proposta de transferência!";
            String eventDescription = String.format(
                    "%s ofereceu $%,d pela sua contratação. Salário: $%,d/ano por %d ano(s). Oferta válida até %s.",
                    toClub.getName(),
                    transferAmount,
                    salary,
                    contractYears,
                    expiresAt.toLocalDate()
            );

            eventService.createSystemEvent(
                    playerId,
                    EventType.TRANSFER_OFFER,
                    eventTitle,
                    eventDescription,
                    savedOffer.getId().toString(),
                    "TransferOffer"
            );
        }

        return offers;
    }



    private long calculateTransferValue(Player player) {
        int overall = player.getOverall();
        int age = player.getAge();
        int potential = player.getPotential();


        long baseValue = (long) (Math.pow(overall, 2.5) * 10000);

        // Multiplicador por idade --------  (jogadores entre 23-27 valem mais)
        double ageMultiplier = 1.0;
        if (age <= 22) {
            ageMultiplier = 0.7 + (age * 0.015); // Jovens valem menos
        } else if (age <= 27) {
            ageMultiplier = 1.3; // Auge valem mais)
        } else if (age <= 30) {
            ageMultiplier = 1.1; // Ainda bom
        } else {
            ageMultiplier = 0.6 - ((age - 30) * 0.05); // Veteranos valem menos
        }

        double potentialMultiplier = 1.0 + ((potential - overall) * 0.01);

        // Variação aleatória (±20%)
        double randomVariation = 0.8 + (random.nextDouble() * 0.4);

        // Calcular valor final
        long finalValue = (long) (baseValue * ageMultiplier * potentialMultiplier * randomVariation);

        // valor mínimo
        return Math.max(100000, finalValue);
    }


    // CALCULAR SALÁRIO

    private long calculateSalary(Player player, Club club) {
        int overall = player.getOverall();

        // Salário base por overall
        long baseSalary = overall * 50000L;

        // Multiplicador por força do clube
        double clubMultiplier = 1.0 + (random.nextDouble() * 0.5);


        double randomVariation = 0.85 + (random.nextDouble() * 0.3);

        // Calcular salário final
        long finalSalary = (long) (baseSalary * clubMultiplier * randomVariation);

        // salário mínimo
        return Math.max(50000, finalSalary);
    }


    // CALCULAR ANOS DE CONTRATO
    private int calculateContractYears(Player player) {
        int age = player.getAge();

        // Jogadores jovens = contratos mais longos
        if (age <= 23) {
            return 4 + random.nextInt(2); // 4-5 anos
        }

        // Jogadores no auge = contratos médios
        if (age <= 28) {
            return 3 + random.nextInt(2); // 3-4 anos
        }

        // Jogadores mais velhos = contratos curtos
        if (age <= 32) {
            return 2 + random.nextInt(2); // 2-3 anos
        }

        // Veteranos: contratos  curtos
        return 1 + random.nextInt(2); // 1-2 anos
    }


    // GERAR OFERTA DE UM CLUBE ESPECÍFICO

    @Transactional
    public TransferOffer generateOfferFromClub(UUID playerId, UUID clubId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player não encontrados"));

        Club toClub = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Club não encontrados"));


        boolean alreadyHasOffer = offerRepository.existsByPlayerIdAndToClubIdAndStatus(
                playerId, clubId, TransferOfferStatus.PENDING
        );

        if (alreadyHasOffer) {
            throw new RuntimeException("O clube já tem uma proposta pendente por este jogador.");
        }


        long transferAmount = calculateTransferValue(player);
        long salary = calculateSalary(player, toClub);
        int contractYears = calculateContractYears(player);
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);


        Club fromClub = null;
        if (player.getClubId() != null) {
            fromClub = clubRepository.findById(player.getClubId()).orElse(null);
        }


        TransferOffer offer = new TransferOffer(
                player,
                fromClub,
                toClub,
                transferAmount,
                salary,
                contractYears,
                expiresAt
        );

        TransferOffer savedOffer = offerRepository.save(offer);


        String eventTitle = "Nova proposta de transferência!";
        String eventDescription = String.format(
                "%s ofereceu $%,d pela sua contratação. Salário: $%,d/ano por %d ano(s). Oferta válida até %s.",
                toClub.getName(),
                transferAmount,
                salary,
                contractYears,
                expiresAt.toLocalDate()
        );

        eventService.createSystemEvent(
                playerId,
                EventType.TRANSFER_OFFER,
                eventTitle,
                eventDescription,
                savedOffer.getId().toString(),
                "TransferOffer"
        );

        return savedOffer;
    }


    // SIMULAR INTERESSE DE CLUBES

    public boolean shouldGenerateOffer(Player player) {
        int overall = player.getOverall();

        // Chance de receber oferta baseada no overall
        int chancePercentage;

        if (overall >= 85) {
            chancePercentage = 80;
        } else if (overall >= 80) {
            chancePercentage = 60;
        } else if (overall >= 75) {
            chancePercentage = 40;
        } else if (overall >= 70) {
            chancePercentage = 20;
        } else {
            chancePercentage = 5;
        }

        return random.nextInt(100) < chancePercentage;
    }


    // GERAR OFERTAS AUTOMÁTICAS PERIÓDICAS

    @Transactional
    public void generatePeriodicOffers() {
        List<Player> allPlayers = playerRepository.findAll();

        for (Player player : allPlayers) {

            if (shouldGenerateOffer(player)) {

                int numberOfOffers = 1 + random.nextInt(3);
                generateOffersForPlayer(player.getId(), numberOfOffers);
            }
        }
    }
}