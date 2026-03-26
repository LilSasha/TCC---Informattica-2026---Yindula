package com.yindula.api.service;

import com.yindula.api.dtoEenum.AgendamentoDTO;
import com.yindula.api.dtoEenum.Posto;
import com.yindula.api.dtoEenum.Servico;
import com.yindula.api.entity.AgendamentoEntity;
import com.yindula.api.repository.AgendamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    @Transactional
    public AgendamentoEntity salvarComValidacao(AgendamentoDTO dados) {

        // 1. LIMPEZA DOS DADOS (Transformar "" em null)
        // Isso evita o erro "Duplicate entry ''" no MySQL
        String biFinal = (dados.numeroBI() == null || dados.numeroBI().isBlank())
                ? null
                : dados.numeroBI();

        // 2. VALIDAÇÃO DE OBRIGARIEDADE
        if (dados.servico() != Servico.EMISSAO && biFinal == null) {
            throw new RuntimeException("Para " + dados.servico() + ", o número do B.I. é obrigatório.");
        }

        // 3. VALIDAÇÃO DE DUPLICADO (Apenas se o BI não for nulo)
        if (biFinal != null) {
            if (agendamentoRepository.existsByNumeroBI(biFinal)) {
                throw new RuntimeException("Já existe um agendamento activo para este número de B.I.");
            }
        }

        // 4. VALIDAÇÃO DE HORÁRIO (Sempre obrigatória)
        boolean horarioOcupado = agendamentoRepository.existsByDataAgendadaAndHoraAgendadaAndPosto(
                dados.data_agendada(),
                dados.hora_agendada(),
                dados.posto()
        );

        if (horarioOcupado) {
            throw new RuntimeException("Este horário já está preenchido no posto selecionado.");
        }

        // 5. CRIAÇÃO DA ENTIDADE E SALVAMENTO
        var agendamento = new AgendamentoEntity(dados);
        agendamento.setNumeroBI(biFinal); // Garante que o valor limpo (ou null) seja salvo

        return agendamentoRepository.save(agendamento);
    }

    public List<String> buscarHorasOcupadas(String dataStr, Posto posto) {
        LocalDate data = LocalDate.parse(dataStr); // Converte "2026-03-25" para objeto Date
        return agendamentoRepository.findHorasOcupadas(data, posto);
    }

}


/*
@Service
public class AgendamentoService {



        // REMOVE O STATIC AQUI
        private final AgendamentoRepository agendamentoRepository;

        // Injeção via construtor (melhor prática que @Autowired no campo)
        public AgendamentoService(AgendamentoRepository agendamentoRepository) {
            this.agendamentoRepository = agendamentoRepository;
        }

    @Transactional
    public AgendamentoEntity salvarComValidacao(AgendamentoDTO dados) {

        // 1. REGRA DE OBRIGARIEDADE
        if (dados.servico() != Servico.EMISSAO) {
            if (dados.numeroBI() == null || dados.numeroBI().isBlank()) {
                throw new RuntimeException("Para " +
                        dados.servico() + ", o número do B.I. é obrigatório.");
            }

            // 2. REGRA DE DUPLICAÇÃO (Só entra aqui se houver um número de BI)
            // Vamos verificar se já existe um agendamento para este BI específico
            boolean biJaAgendado = agendamentoRepository.existsByNumeroBI(dados.numeroBI());
            if (biJaAgendado) {
                throw new RuntimeException("Já existe um agendamento activo para este número de B.I.");
            }
        }

        // 3. REGRA DE HORÁRIO (Sempre obrigatória para todos)
        boolean horarioOcupado = agendamentoRepository.existsByDataAgendadaAndHoraAgendadaAndPosto(
                dados.data_agendada(),
                dados.hora_agendada(),
                dados.posto()
        );

        if (horarioOcupado) {
            throw new RuntimeException("Este horário já está preenchido no posto selecionado.");
        }

        // Se passou por tudo, salvamos
        var agendamento = new AgendamentoEntity(dados);
        return agendamentoRepository.save(agendamento);
    }


    }*/