package com.yindula.api.entity;

import com.yindula.api.dtoEenum.AgendamentoDTO;
import com.yindula.api.dtoEenum.Posto;
import com.yindula.api.dtoEenum.Servico;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Agendamento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class AgendamentoEntity {

    public AgendamentoEntity(AgendamentoDTO dados) {
        this.id = dados.id();
        this.cidadao = dados.cidadao();
        this.servico = dados.servico();
        this.numeroBI = dados.numeroBI();
        this.horaAgendada = dados.hora_agendada();
        this.dataAgendada = dados.data_agendada();
        this.posto = dados.posto();
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "Cidadao", length = 255, nullable = false)
    private String cidadao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "Servico", length = 255, nullable = false)
    private Servico servico;


    @Column(name = "numeroBI")
    private String numeroBI;

    @Future
    @Column(name = "Data_Agendada", nullable = false)
    private LocalDate dataAgendada;

    @NotBlank
    @Column(name = "Hora_Agendada", nullable = false)
    private String horaAgendada;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "Posto", nullable = false)
    private Posto posto;
}
