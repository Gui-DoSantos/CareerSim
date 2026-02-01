package com.careersim.careersim.contract.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateContractRequest {

    @NotNull(message = "Club ID is required")
    private UUID clubId;

    @NotNull(message = "Salary is required")
    @Min(value = 0, message = "Salary must be positive")
    private Long salary;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private Long signingBonus;

    private Long releaseClause;
}