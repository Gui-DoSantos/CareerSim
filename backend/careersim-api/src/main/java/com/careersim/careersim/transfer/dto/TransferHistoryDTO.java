package com.careersim.careersim.transfer.dto;

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
public class TransferHistoryDTO {

    private UUID playerId;
    private String playerName;
    private Long totalTransfers;
    private Long totalValueSpent;
    private Long totalValueEarned;
    private List<TransferDTO> transfers;
}