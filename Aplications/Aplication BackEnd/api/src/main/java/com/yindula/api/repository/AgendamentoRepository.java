package com.yindula.api.repository;

import com.yindula.api.dtoEenum.Posto;
import com.yindula.api.entity.AgendamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends JpaRepository<AgendamentoEntity, Long> {

    Optional<AgendamentoEntity> findById(Long id);

    List<AgendamentoEntity> findByCidadaoContainingIgnoreCase(String cidadao);


    boolean existsByDataAgendadaAndHoraAgendadaAndPosto(
            java.time.LocalDate data_agendada,
            String hora_agendada,
            com.yindula.api.dtoEenum.Posto posto);

    boolean existsByNumeroBIAndDataAgendada(
            String numeroBI,
            java.time.LocalDate data_agendada);

    Long countByDataAgendadaAndPosto(
            java.time.LocalDate data_agendada,
            com.yindula.api.dtoEenum.Posto posto);


    boolean existsByNumeroBI(String numeroBI);


    @Query("SELECT a.horaAgendada FROM AgendamentoEntity a WHERE a.dataAgendada = :data AND a.posto = :posto")
    List<String> findHorasOcupadas(@Param("data") LocalDate data, @Param("posto") Posto posto);


}


