package com.yindula.api.dtoEenum;



import com.yindula.api.entity.AgendamentoEntity;

import java.time.LocalDate;

//Dados no DTO que serao mostrados na tela
public record ListarAgendamentos(
        Long id,
        String cidadao,
        String servico,
        String numeroBI,
        LocalDate data_agendada,
        String hora_agendada,
        Posto posto) {

    //Construtor dos dados para mostrar na lista
    public ListarAgendamentos(AgendamentoEntity agendamentoEntity) {
        this(
            agendamentoEntity.getId(),
            agendamentoEntity.getCidadao(),
            String.valueOf(agendamentoEntity.getServico()),
            agendamentoEntity.getNumeroBI(),
            agendamentoEntity.getDataAgendada(),
            agendamentoEntity.getHoraAgendada(),
            agendamentoEntity.getPosto()
        );
    }
}

