package com.careersim.careersim.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferDTO {

    private UUID id;
    private UUID playerId;
    private String playerName;
    private UUID fromClubId;
    private String fromClubName;
    private UUID toClubId;
    private String toClubName;
    private Long transferAmount;
    private Long salary;
    private Integer contractYears;
    private LocalDateTime transferDate;
    private Integer season;
    private LocalDateTime createdAt;
    private Boolean isFreeTransfer;
    private String transferSummary;
}