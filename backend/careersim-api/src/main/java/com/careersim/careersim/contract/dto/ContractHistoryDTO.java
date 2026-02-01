package com.careersim.careersim.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractHistoryDTO {

    private UUID playerId;
    private String playerName;
    private Long totalContracts;
    private Long totalEarnings;
    private Long currentSalary;
    private ContractDTO currentContract;
    private List<ContractDTO> pastContracts;
}