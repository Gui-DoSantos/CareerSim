package com.careersim.careersim.transfer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcceptOfferRequest {

    @NotNull(message = "Offer ID is required")
    private UUID offerId;
}