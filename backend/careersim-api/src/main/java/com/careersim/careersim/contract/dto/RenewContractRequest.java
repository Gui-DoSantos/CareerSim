package com.careersim.careersim.contract.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RenewContractRequest {

    @NotNull(message = "New salary is required")
    @Min(value = 0, message = "Salary must be positive")
    private Long newSalary;

    @NotNull(message = "Additional years is required")
    @Min(value = 1, message = "Must add at least 1 year")
    private Integer additionalYears;

    private Long signingBonus;

    private Long releaseClause;
}