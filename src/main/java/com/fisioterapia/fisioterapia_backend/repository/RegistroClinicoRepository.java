package com.fisioterapia.fisioterapia_backend.repository;

import com.fisioterapia.fisioterapia_backend.entity.Paciente;
import com.fisioterapia.fisioterapia_backend.entity.RegistroClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroClinicoRepository extends JpaRepository<RegistroClinico, Long> {
    Optional<RegistroClinico> findByCodigo(String codigo);
    List<RegistroClinico> findByPacienteIdOrderByFechaRegistroDesc(Long pacienteId);
    List<RegistroClinico> findByFisioterapeutaId(Long fisioterapeutaId);
    List<RegistroClinico> findByPacienteAndFechaRegistroAndDiagnostico(Paciente paciente, LocalDate fechaRegistro, String diagnostico);
    boolean existsByCodigo(String codigo);
}
