package com.careersim.careersim.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferStatsDTO {

    private Long totalTransfers;
    private Long pendingOffers;
    private Long totalSpent;
    private Long totalReceived;
    private Long highestTransferValue;
    private String mostExpensiveTransferClub;
}