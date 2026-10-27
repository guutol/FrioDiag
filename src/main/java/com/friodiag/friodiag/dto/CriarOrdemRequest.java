package com.friodiag.friodiag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarOrdemRequest(
        @NotBlank(message = "Equipamento é obrigatório")
        @Size(max = 100, message = "Equipamento deve ter no máximo 100 caracteres")
        String equipamento,

        @NotBlank(message = "Sintomas são obrigatórios")
        @Size(min = 8, max = 2000,
                message = "Sintomas devem ter entre 10 e 2000 caracteres")
        String sintomas
) {}
