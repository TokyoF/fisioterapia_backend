package com.fisioterapia.fisioterapia_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroClinicoResponse {
    private Long id;
    private String codigo;
    private String pacienteNombre;
    private String pacienteCodigo;
    private String fisioterapeutaNombre;
    private LocalDate fechaRegistro;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;
    private LocalDate proximaSesion;
}
