package com.fisioterapia.fisioterapia_backend.repository;

import com.fisioterapia.fisioterapia_backend.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    @Query("SELECT c FROM Cita c " +
           "JOIN FETCH c.paciente p " +
           "JOIN FETCH p.user " +
           "JOIN FETCH c.fisioterapeuta f " +
           "JOIN FETCH f.user " +
           "WHERE c.id = :id")
    Optional<Cita> findById(@Param("id") Long id);
    
    Optional<Cita> findByCodigo(String codigo);
    
    @Query("SELECT c FROM Cita c " +
           "JOIN FETCH c.paciente p " +
           "JOIN FETCH p.user " +
           "JOIN FETCH c.fisioterapeuta f " +
           "JOIN FETCH f.user " +
           "WHERE c.paciente.id = :pacienteId")
    List<Cita> findByPacienteId(@Param("pacienteId") Long pacienteId);
    
    @Query("SELECT c FROM Cita c " +
           "JOIN FETCH c.paciente p " +
           "JOIN FETCH p.user " +
           "JOIN FETCH c.fisioterapeuta f " +
           "JOIN FETCH f.user " +
           "WHERE c.fisioterapeuta.id = :fisioterapeutaId")
    List<Cita> findByFisioterapeutaId(@Param("fisioterapeutaId") Long fisioterapeutaId);
    
    @Query("SELECT c FROM Cita c " +
           "JOIN FETCH c.paciente p " +
           "JOIN FETCH p.user " +
           "JOIN FETCH c.fisioterapeuta f " +
           "JOIN FETCH f.user " +
           "WHERE c.paciente.id = :pacienteId AND c.estado = :estado")
    List<Cita> findByPacienteIdAndEstado(@Param("pacienteId") Long pacienteId, @Param("estado") Cita.EstadoCita estado);
    
    List<Cita> findByFisioterapeutaIdAndFecha(Long fisioterapeutaId, LocalDate fecha);
    boolean existsByCodigo(String codigo);
}
