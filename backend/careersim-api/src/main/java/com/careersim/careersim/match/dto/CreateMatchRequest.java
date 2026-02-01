package com.careersim.careersim.match.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMatchRequest {

    @NotNull(message = "O ID do clube é obrigatório") // clube do proprio jogador
    private UUID clubId;

    @NotBlank(message = "O nome do adversário é obrigatório")
    private String opponentName;

    @NotBlank(message = "A competição é obrigatória")
    private String competition;

    @NotNull(message = "A data da partida é obrigatória")
    private LocalDateTime matchDate;

    @NotNull(message = "É necessário informar se o jogo é em casa")
    private Boolean isHome;
}
