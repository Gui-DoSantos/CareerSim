package com.careersim.careersim.transfer.service;

import com.careersim.careersim.club.model.Club;
import com.careersim.careersim.club.repository.ClubRepository;
import com.careersim.careersim.event.model.EventType;
import com.careersim.careersim.event.service.EventService;
import com.careersim.careersim.player.model.Player;
import com.careersim.careersim.player.repository.PlayerRepository;
import com.careersim.careersim.transfer.dto.*;
import com.careersim.careersim.transfer.model.Transfer;
import com.careersim.careersim.transfer.model.TransferOffer;
import com.careersim.careersim.transfer.model.TransferOfferStatus;
import com.careersim.careersim.transfer.repository.TransferOfferRepository;
import com.careersim.careersim.transfer.repository.TransferRepository;
import com.careersim.careersim.contract.Service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferOfferRepository offerRepository;
    private final TransferRepository transferRepository;
    private final PlayerRepository playerRepository;
    private final ClubRepository clubRepository;
    private final EventService eventService;
    private final ContractService contractService;



    // LISTAR OFERTAS ATIVAS DO PLAYER

    @Transactional(readOnly = true)
    public List<TransferOfferDTO> getActiveOffers(UUID playerId) {
        List<TransferOffer> offers = offerRepository.findActivePendingOffers(playerId, LocalDateTime.now());

        List<TransferOfferDTO> result = new ArrayList<>();
        for (TransferOffer offer : offers) {
            result.add(convertOfferToDTO(offer));
        }

        return result;
    }


    // LISTAR TODAS AS OFERTAS DO PLAYER

    @Transactional(readOnly = true)
    public List<TransferOfferDTO> getAllOffers(UUID playerId) {
        List<TransferOffer> offers = offerRepository.findByPlayerIdOrderByCreatedAtDesc(playerId);

        List<TransferOfferDTO> result = new ArrayList<>();
        for (TransferOffer offer : offers) {
            result.add(convertOfferToDTO(offer));
        }

        return result;
    }


    // ACEITAR OFERTA

    @Transactional
    public TransferDTO acceptOffer(UUID playerId, UUID offerId) {

        TransferOffer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Oferta não encontrada"));

        //
        if (!offer.getPlayer().getId().equals(playerId)) {
            throw new RuntimeException("Esta oferta não é válida para este jogador.");
        }

        if (!offer.canBeAccepted()) {
            throw new RuntimeException("A oferta não pode ser aceita (expirada ou já respondida).");
        }


        offer.accept();
        offerRepository.save(offer);

        Transfer transfer = new Transfer(
                offer.getPlayer(),
                offer.getFromClub(),
                offer.getToClub(),
                offer.getOfferAmount(),
                offer.getSalaryOffered(),
                offer.getContractYears()
        );
        Transfer savedTransfer = transferRepository.save(transfer);

        Player player = offer.getPlayer();
        contractService.createContractFromTransfer(
                player,
                offer.getToClub(),
                offer.getSalaryOffered(),
                offer.getContractYears(),
                0L,
                null
        );

        List<TransferOffer> otherOffers = offerRepository.findActivePendingOffers(playerId, LocalDateTime.now());
        for (TransferOffer otherOffer : otherOffers) {
            if (!otherOffer.getId().equals(offerId)) {
                otherOffer.reject();
                offerRepository.save(otherOffer);
            }
        }

        String eventTitle = "Transferência concluída!";
        String eventDescription = String.format(
                "Você se transferiu para %s por $%,d. Contrato de %d ano(s). Salário: $%,d/ano.",
                offer.getToClub().getName(),
                offer.getOfferAmount(),
                offer.getContractYears(),
                offer.getSalaryOffered()
        );

        eventService.createSystemEvent(
                playerId,
                EventType.TRANSFER_COMPLETED,
                eventTitle,
                eventDescription,
                savedTransfer.getId().toString(),
                "Transfer"
        );

        return convertTransferToDTO(savedTransfer);
    }

    // REJEITAR OFERTA

    @Transactional
    public void rejectOffer(UUID playerId, UUID offerId) {
        TransferOffer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Oferta não encontrada"));

        if (!offer.getPlayer().getId().equals(playerId)) {
            throw new RuntimeException("Esta oferta não é válida para este jogador.");
        }

        if (!offer.isPending()) {
            throw new RuntimeException("A oferta não está pendente");
        }

        offer.reject();
        offerRepository.save(offer);
    }


    // HISTÓRICO DE TRANSFERÊNCIAS

    @Transactional(readOnly = true)
    public TransferHistoryDTO getTransferHistory(UUID playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player não encontrado"));

        List<Transfer> transfers = transferRepository.findByPlayerIdOrderByTransferDateDesc(playerId);

        long totalTransfers = transfers.size();
        long totalSpent = 0;
        long totalEarned = 0;

        List<TransferDTO> transferDTOs = new ArrayList<>();
        for (Transfer transfer : transfers) {
            transferDTOs.add(convertTransferToDTO(transfer));
            totalSpent += transfer.getTransferAmount();
        }

        String playerName = player.getName();

        return TransferHistoryDTO.builder()
                .playerId(playerId)
                .playerName(playerName)
                .totalTransfers(totalTransfers)
                .totalValueSpent(totalSpent)
                .totalValueEarned(totalEarned)
                .transfers(transferDTOs)
                .build();
    }

    // ESTATÍSTICAS DE TRANSFERÊNCIAS
    @Transactional(readOnly = true)
    public TransferStatsDTO getTransferStats(UUID playerId) {
        List<Transfer> transfers = transferRepository.findByPlayerIdOrderByTransferDateDesc(playerId);
        Long pendingOffers = offerRepository.countByPlayerIdAndStatus(playerId, TransferOfferStatus.PENDING);

        long totalTransfers = transfers.size();
        long totalSpent = 0;
        long totalReceived = 0;
        long highestValue = 0;
        String mostExpensiveClub = "N/A";

        for (Transfer transfer : transfers) {
            long amount = transfer.getTransferAmount();
            totalSpent += amount;

            if (amount > highestValue) {
                highestValue = amount;
                mostExpensiveClub = transfer.getToClub().getName();
            }
        }

        return TransferStatsDTO.builder()
                .totalTransfers(totalTransfers)
                .pendingOffers(pendingOffers)
                .totalSpent(totalSpent)
                .totalReceived(totalReceived)
                .highestTransferValue(highestValue)
                .mostExpensiveTransferClub(mostExpensiveClub)
                .build();
    }


    // EXPIRAR OFERTAS ANTIGAS
    @Transactional
    public void expireOldOffers() {
        List<TransferOffer> expiredOffers = offerRepository.findExpiredOffers(LocalDateTime.now());

        for (TransferOffer offer : expiredOffers) {
            offer.expire();
            offerRepository.save(offer);
        }
    }



    private TransferOfferDTO convertOfferToDTO(TransferOffer offer) {
        String playerName = offer.getPlayer().getName();

        String fromClubName = null;
        UUID fromClubId = null;
        if (offer.getFromClub() != null) {
            fromClubName = offer.getFromClub().getName();
            fromClubId = offer.getFromClub().getId();
        }

        return TransferOfferDTO.builder()
                .id(offer.getId())
                .playerId(offer.getPlayer().getId())
                .playerName(playerName)
                .fromClubId(fromClubId)
                .fromClubName(fromClubName)
                .toClubId(offer.getToClub().getId())
                .toClubName(offer.getToClub().getName())
                .offerAmount(offer.getOfferAmount())
                .salaryOffered(offer.getSalaryOffered())
                .contractYears(offer.getContractYears())
                .status(offer.getStatus())
                .expiresAt(offer.getExpiresAt())
                .respondedAt(offer.getRespondedAt())
                .createdAt(offer.getCreatedAt())
                .isExpired(offer.isExpired())
                .canBeAccepted(offer.canBeAccepted())
                .build();
    }

    private TransferDTO convertTransferToDTO(Transfer transfer) {
        String playerName = transfer.getPlayer().getName();

        String fromClubName = null;
        UUID fromClubId = null;
        if (transfer.getFromClub() != null) {
            fromClubName = transfer.getFromClub().getName();
            fromClubId = transfer.getFromClub().getId();
        }

        return TransferDTO.builder()
                .id(transfer.getId())
                .playerId(transfer.getPlayer().getId())
                .playerName(playerName)
                .fromClubId(fromClubId)
                .fromClubName(fromClubName)
                .toClubId(transfer.getToClub().getId())
                .toClubName(transfer.getToClub().getName())
                .transferAmount(transfer.getTransferAmount())
                .salary(transfer.getSalary())
                .contractYears(transfer.getContractYears())
                .transferDate(transfer.getTransferDate())
                .season(transfer.getSeason())
                .createdAt(transfer.getCreatedAt())
                .isFreeTransfer(transfer.isFreeTransfer())
                .transferSummary(transfer.getTransferSummary())
                .build();
    }
}