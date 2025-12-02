package com.fisioterapia.fisioterapia_backend.repository;

import com.fisioterapia.fisioterapia_backend.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByCodigo(String codigo);
    Optional<Paciente> findByUserId(Long userId);
    boolean existsByCodigo(String codigo);
    Optional<Paciente> findTopByOrderByIdDesc();
}
