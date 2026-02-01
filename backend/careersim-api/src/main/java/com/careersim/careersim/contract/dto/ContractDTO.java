package com.careersim.careersim.contract.dto;

import com.careersim.careersim.contract.model.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractDTO {

    private UUID id;
    private UUID playerId;
    private String playerName;
    private UUID clubId;
    private String clubName;
    private Long salary;
    private LocalDate startDate;
    private LocalDate endDate;
    private ContractStatus status;
    private Long signingBonus;
    private Long releaseClause;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Campos calculados
    private Long daysRemaining;
    private Long monthsRemaining;
    private Long yearsRemaining;
    private Long totalYears;
    private Long totalValue;
    private Boolean isExpired;
    private Boolean isExpiringSoon;
    private Boolean canBeRenewed;
    private Boolean canBeTerminated;
    private String contractSummary;
}