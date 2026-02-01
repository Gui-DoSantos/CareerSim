package com.careersim.careersim.transfer.dto;

import com.careersim.careersim.transfer.model.TransferOfferStatus;
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
public class TransferOfferDTO {

    private UUID id;
    private UUID playerId;
    private String playerName;
    private UUID fromClubId;
    private String fromClubName;
    private UUID toClubId;
    private String toClubName;
    private Long offerAmount;
    private Long salaryOffered;
    private Integer contractYears;
    private TransferOfferStatus status;
    private LocalDateTime expiresAt;
    private LocalDateTime respondedAt;
    private LocalDateTime createdAt;
    private Boolean isExpired;
    private Boolean canBeAccepted;
}