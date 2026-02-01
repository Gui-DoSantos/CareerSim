package com.careersim.careersim.contract.controller;

import com.careersim.careersim.contract.Service.ContractService;
import com.careersim.careersim.contract.dto.ContractDTO;
import com.careersim.careersim.contract.dto.ContractHistoryDTO;
import com.careersim.careersim.contract.dto.CreateContractRequest;
import com.careersim.careersim.contract.dto.RenewContractRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/players/{playerId}/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;


    // BUSCA CONTRATO ATIVO

    @GetMapping("/current")
    public ResponseEntity<ContractDTO> getCurrentContract(@PathVariable UUID playerId) {
        Optional<ContractDTO> contract = contractService.getActiveContract(playerId);

        if (contract.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(contract.get());
    }


    // CRIA CONTRATO

    @PostMapping
    public ResponseEntity<ContractDTO> createContract(
            @PathVariable UUID playerId,
            @Valid @RequestBody CreateContractRequest request
    ) {
        ContractDTO contract = contractService.createContract(playerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(contract);
    }


    // RENOVA CONTRATO

    @PostMapping("/renew")
    public ResponseEntity<ContractDTO> renewContract(
            @PathVariable UUID playerId,
            @Valid @RequestBody RenewContractRequest request
    ) {
        ContractDTO contract = contractService.renewContract(playerId, request);
        return ResponseEntity.ok(contract);
    }


    // TERMINA CONTRATO
    @DeleteMapping("/terminate")
    public ResponseEntity<Void> terminateContract(@PathVariable UUID playerId) {
        contractService.terminateContract(playerId);
        return ResponseEntity.noContent().build();
    }


    // HISTÓRICO DE CONTRATOS

    @GetMapping("/history")
    public ResponseEntity<ContractHistoryDTO> getContractHistory(@PathVariable UUID playerId) {
        ContractHistoryDTO history = contractService.getContractHistory(playerId);
        return ResponseEntity.ok(history);
    }


    // CONTRATOS EXPIRANDO EM BREVE

    @GetMapping("/expiring-soon")
    public ResponseEntity<List<ContractDTO>> getContractsExpiringSoon(
            @RequestParam(defaultValue = "180") int daysThreshold
    ) {
        List<ContractDTO> contracts = contractService.getContractsExpiringSoon(daysThreshold);
        return ResponseEntity.ok(contracts);
    }
}