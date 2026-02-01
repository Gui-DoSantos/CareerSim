package com.careersim.careersim.transfer.controller;

import com.careersim.careersim.transfer.dto.*;
import com.careersim.careersim.transfer.model.TransferOffer;
import com.careersim.careersim.transfer.service.TransferAIService;
import com.careersim.careersim.transfer.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/players/{playerId}/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;
    private final TransferAIService transferAIService;


    // LISTAR OFERTAS ATIVAS

    @GetMapping("/offers/active")
    public ResponseEntity<List<TransferOfferDTO>> getActiveOffers(@PathVariable UUID playerId) {
        List<TransferOfferDTO> offers = transferService.getActiveOffers(playerId);
        return ResponseEntity.ok(offers);
    }


    // LISTAR TODAS AS OFERTAS

    @GetMapping("/offers")
    public ResponseEntity<List<TransferOfferDTO>> getAllOffers(@PathVariable UUID playerId) {
        List<TransferOfferDTO> offers = transferService.getAllOffers(playerId);
        return ResponseEntity.ok(offers);
    }

    // ACEITAR OFERTA
    @PostMapping("/offers/accept")
    public ResponseEntity<TransferDTO> acceptOffer(
            @PathVariable UUID playerId,
            @Valid @RequestBody AcceptOfferRequest request
    ) {
        TransferDTO transfer = transferService.acceptOffer(playerId, request.getOfferId());
        return ResponseEntity.ok(transfer);
    }

    // REJEITAR OFERTA

    @PostMapping("/offers/reject")
    public ResponseEntity<Void> rejectOffer(
            @PathVariable UUID playerId,
            @Valid @RequestBody RejectOfferRequest request
    ) {
        transferService.rejectOffer(playerId, request.getOfferId());
        return ResponseEntity.noContent().build();
    }

    // HISTÓRICO DE TRANSFERÊNCIAS
    @GetMapping("/history")
    public ResponseEntity<TransferHistoryDTO> getTransferHistory(@PathVariable UUID playerId) {
        TransferHistoryDTO history = transferService.getTransferHistory(playerId);
        return ResponseEntity.ok(history);
    }


    // ESTATÍSTICAS DE TRANSFERÊNCIAS
    @GetMapping("/stats")
    public ResponseEntity<TransferStatsDTO> getTransferStats(@PathVariable UUID playerId) {
        TransferStatsDTO stats = transferService.getTransferStats(playerId);
        return ResponseEntity.ok(stats);
    }

    // GERAR OFERTAS ALEATÓRIAS (IA)
    @PostMapping("/offers/generate")
    public ResponseEntity<List<TransferOffer>> generateOffers(
            @PathVariable UUID playerId,
            @RequestParam(defaultValue = "3") int count
    ) {
        List<TransferOffer> offers = transferAIService.generateOffersForPlayer(playerId, count);
        return ResponseEntity.status(HttpStatus.CREATED).body(offers);
    }


    // GERAR OFERTA DE CLUBE ESPECÍFICO
    @PostMapping("/offers/generate/{clubId}")
    public ResponseEntity<TransferOffer> generateOfferFromClub(
            @PathVariable UUID playerId,
            @PathVariable UUID clubId
    ) {
        TransferOffer offer = transferAIService.generateOfferFromClub(playerId, clubId);
        return ResponseEntity.status(HttpStatus.CREATED).body(offer);
    }
}