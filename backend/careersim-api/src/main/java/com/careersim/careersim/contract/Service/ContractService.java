package com.careersim.careersim.contract.Service;

import com.careersim.careersim.club.model.Club;
import com.careersim.careersim.club.repository.ClubRepository;
import com.careersim.careersim.contract.dto.ContractDTO;
import com.careersim.careersim.contract.dto.ContractHistoryDTO;
import com.careersim.careersim.contract.dto.CreateContractRequest;
import com.careersim.careersim.contract.dto.RenewContractRequest;
import com.careersim.careersim.contract.model.Contract;
import com.careersim.careersim.contract.model.ContractStatus;
import com.careersim.careersim.contract.repository.ContractRepository;
import com.careersim.careersim.event.model.EventType;
import com.careersim.careersim.event.service.EventService;
import com.careersim.careersim.player.model.Player;
import com.careersim.careersim.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final PlayerRepository playerRepository;
    private final ClubRepository clubRepository;
    private final EventService eventService;


    // BUSCAR CONTRATO ATIVO DO PLAYER

    @Transactional(readOnly = true)
    public Optional<ContractDTO> getActiveContract(UUID playerId) {
        Optional<Contract> contract = contractRepository.findByPlayerIdAndStatus(playerId, ContractStatus.ACTIVE);

        if (contract.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(convertToDTO(contract.get()));
    }


    // CRIAR CONTRATO
    @Transactional
    public ContractDTO createContract(UUID playerId, CreateContractRequest request) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player não encontrado"));

        Club club = clubRepository.findById(request.getClubId())
                .orElseThrow(() -> new RuntimeException("Club não encontrado"));


        Optional<Contract> existingContract = contractRepository.findByPlayerIdAndStatus(playerId, ContractStatus.ACTIVE);
        if (existingContract.isPresent()) {
            throw new RuntimeException("player ja tem um contrato ativo");
        }


        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new RuntimeException("A data de término deve ser posterior à data de início");
        }


        Contract contract = new Contract(
                player,
                club,
                request.getSalary(),
                request.getStartDate(),
                request.getEndDate(),
                request.getSigningBonus() != null ? request.getSigningBonus() : 0L,
                request.getReleaseClause()
        );

        Contract savedContract = contractRepository.save(contract);


        player.transferToClub(club.getId());
        playerRepository.save(player);

        String eventTitle = "Novo contrato assinado!";
        String eventDescription = String.format(
                "Você assinou contrato com %s até %s. Salário: $%,d/ano.",
                club.getName(),
                contract.getEndDate(),
                contract.getSalary()
        );

        eventService.createSystemEvent(
                playerId,
                EventType.CONTRACT_SIGNED,
                eventTitle,
                eventDescription,
                savedContract.getId().toString(),
                "Contract"
        );

        return convertToDTO(savedContract);
    }


    // CRIAR CONTRATO

    @Transactional
    public Contract createContractFromTransfer(Player player, Club club, Long salary,
                                               Integer years, Long signingBonus, Long releaseClause) {

        Optional<Contract> existingContract = contractRepository.findByPlayerIdAndStatus(
                player.getId(), ContractStatus.ACTIVE
        );

        if (existingContract.isPresent()) {
            Contract oldContract = existingContract.get();
            oldContract.terminate();
            contractRepository.save(oldContract);
        }


        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusYears(years);


        Contract contract = new Contract(
                player,
                club,
                salary,
                startDate,
                endDate,
                signingBonus != null ? signingBonus : 0L,
                releaseClause
        );

        Contract savedContract = contractRepository.save(contract);


        String eventTitle = "Novo contrato assinado!";
        String eventDescription = String.format(
                "Você assinou contrato com %s até %s. Salário: $%,d/ano.",
                club.getName(),
                endDate,
                salary
        );

        eventService.createSystemEvent(
                player.getId(),
                EventType.CONTRACT_SIGNED,
                eventTitle,
                eventDescription,
                savedContract.getId().toString(),
                "Contract"
        );

        return savedContract;
    }


    // RENOVAR CONTRATO

    @Transactional
    public ContractDTO renewContract(UUID playerId, RenewContractRequest request) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() ->  new RuntimeException("Player não encontrado"));

        Contract currentContract = contractRepository.findByPlayerIdAndStatus(playerId, ContractStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Nenhum contrato ativo encontrado"));

        if (!currentContract.canBeRenewed()) {
            throw new RuntimeException("O contrato ainda não pode ser renovado (deve ter menos de 1 ano restante).");
        }

        currentContract.renew();
        contractRepository.save(currentContract);


        LocalDate newStartDate = currentContract.getEndDate();
        LocalDate newEndDate = newStartDate.plusYears(request.getAdditionalYears());


        Contract newContract = new Contract(
                player,
                currentContract.getClub(),
                request.getNewSalary(),
                newStartDate,
                newEndDate,
                request.getSigningBonus() != null ? request.getSigningBonus() : 0L,
                request.getReleaseClause()
        );

        Contract savedContract = contractRepository.save(newContract);

        // Criar evento
        String eventTitle = "Contrato renovado!";
        String eventDescription = String.format(
                "Você renovou com %s até %s. Novo salário: $%,d/ano.",
                currentContract.getClub().getName(),
                newEndDate,
                request.getNewSalary()
        );

        eventService.createSystemEvent(
                playerId,
                EventType.CONTRACT_SIGNED,
                eventTitle,
                eventDescription,
                savedContract.getId().toString(),
                "Contract"
        );

        return convertToDTO(savedContract);
    }


    // RESCINDIR CONTRATO

    @Transactional
    public void terminateContract(UUID playerId) {
        Contract contract = contractRepository.findByPlayerIdAndStatus(playerId, ContractStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Nenhum contrato ativo encontrado"));

        if (!contract.canBeTerminated()) {
            throw new RuntimeException("O contrato não pode ser rescindido.");
        }


        contract.terminate();
        contractRepository.save(contract);


        Player player = contract.getPlayer();
        player.transferToClub(null);
        playerRepository.save(player);


        String eventTitle = "Contrato rescindido";
        String eventDescription = String.format(
                "Você rescindiu seu contrato com %s. Agora você é um agente livre.",
                contract.getClub().getName()
        );

        eventService.createSystemEvent(
                playerId,
                EventType.CONTRACT_SIGNED,
                eventTitle,
                eventDescription
        );
    }


    // HISTÓRICO DE CONTRATOS

    @Transactional(readOnly = true)
    public ContractHistoryDTO getContractHistory(UUID playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player não encontrado"));

        List<Contract> allContracts = contractRepository.findByPlayerIdOrderByStartDateDesc(playerId);

        // Separar contrato atual e histórico
        ContractDTO currentContractDTO = null;
        List<ContractDTO> pastContractDTOs = new ArrayList<>();
        long totalEarnings = 0;
        Long currentSalary = 0L;

        for (Contract contract : allContracts) {
            ContractDTO dto = convertToDTO(contract);

            if (contract.getStatus() == ContractStatus.ACTIVE) {
                currentContractDTO = dto;
                currentSalary = contract.getSalary();
            } else {
                pastContractDTOs.add(dto);
            }


            long contractYears = contract.getTotalYears();
            totalEarnings += (contract.getSalary() * contractYears) + contract.getSigningBonus();
        }

        String playerName = player.getName();

        return ContractHistoryDTO.builder()
                .playerId(playerId)
                .playerName(playerName)
                .totalContracts((long) allContracts.size())
                .totalEarnings(totalEarnings)
                .currentSalary(currentSalary)
                .currentContract(currentContractDTO)
                .pastContracts(pastContractDTOs)
                .build();
    }


    // EXPIRAR CONTRATOS AUTOMATICAMENTE

    @Transactional
    public void expireOldContracts() {
        List<Contract> expiredContracts = contractRepository.findExpiredContracts(LocalDate.now());

        for (Contract contract : expiredContracts) {
            contract.expire();
            contractRepository.save(contract);


            Player player = contract.getPlayer();
            player.transferToClub(null);
            playerRepository.save(player);


            String eventTitle = "Contrato expirado";
            String eventDescription = String.format(
                    "Seu contrato com %s expirou. Você agora é um agente livre.",
                    contract.getClub().getName()
            );

            eventService.createSystemEvent(
                    player.getId(),
                    EventType.CONTRACT_SIGNED,
                    eventTitle,
                    eventDescription
            );
        }
    }


    // VERIFICAR CONTRATOS EXPIRANDO EM BREVE

    @Transactional(readOnly = true)
    public List<ContractDTO> getContractsExpiringSoon(int daysThreshold) {
        LocalDate now = LocalDate.now();
        LocalDate threshold = now.plusDays(daysThreshold);

        List<Contract> contracts = contractRepository.findContractsExpiringSoon(now, threshold);

        List<ContractDTO> result = new ArrayList<>();
        for (Contract contract : contracts) {
            result.add(convertToDTO(contract));
        }

        return result;
    }



    private ContractDTO convertToDTO(Contract contract) {
        String playerName = contract.getPlayer().getName();
        String clubName = contract.getClub().getName();

        return ContractDTO.builder()
                .id(contract.getId())
                .playerId(contract.getPlayer().getId())
                .playerName(playerName)
                .clubId(contract.getClub().getId())
                .clubName(clubName)
                .salary(contract.getSalary())
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .status(contract.getStatus())
                .signingBonus(contract.getSigningBonus())
                .releaseClause(contract.getReleaseClause())
                .createdAt(contract.getCreatedAt())
                .updatedAt(contract.getUpdatedAt())
                .daysRemaining(contract.getDaysRemaining())
                .monthsRemaining(contract.getMonthsRemaining())
                .yearsRemaining(contract.getYearsRemaining())
                .totalYears(contract.getTotalYears())
                .totalValue(contract.getTotalValue())
                .isExpired(contract.isExpired())
                .isExpiringSoon(contract.isExpiringSoon())
                .canBeRenewed(contract.canBeRenewed())
                .canBeTerminated(contract.canBeTerminated())
                .contractSummary(contract.getContractSummary())
                .build();
    }
}