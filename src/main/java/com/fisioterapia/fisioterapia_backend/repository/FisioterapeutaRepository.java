package com.fisioterapia.fisioterapia_backend.repository;

import com.fisioterapia.fisioterapia_backend.entity.Fisioterapeuta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FisioterapeutaRepository extends JpaRepository<Fisioterapeuta, Long> {
    Optional<Fisioterapeuta> findByUserId(Long userId);
    List<Fisioterapeuta> findByActivoTrue();
}
