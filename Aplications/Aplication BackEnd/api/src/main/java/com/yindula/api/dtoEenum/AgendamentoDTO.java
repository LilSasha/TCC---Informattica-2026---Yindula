package com.yindula.api.dtoEenum;

import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record AgendamentoDTO(

        Long id,

        @NotBlank
        String cidadao,

        @Enumerated
        Servico servico,

        String numeroBI,

        @Future
        LocalDate data_agendada,

        String hora_agendada,

        Posto posto
) {
}