package com.fisioterapia.fisioterapia_backend.repository;

import com.fisioterapia.fisioterapia_backend.entity.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {
    List<Horario> findByFisioterapeutaId(Long fisioterapeutaId);
    List<Horario> findByFisioterapeutaIdAndDisponibleTrue(Long fisioterapeutaId);
    List<Horario> findByFisioterapeutaIdAndDiaSemana(Long fisioterapeutaId, String diaSemana);
}
