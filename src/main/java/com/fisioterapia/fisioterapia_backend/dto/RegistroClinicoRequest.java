package com.fisioterapia.fisioterapia_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroClinicoRequest {
    @NotNull(message = "El ID del paciente es requerido")
    private Long pacienteId;

    @NotNull(message = "La fecha de registro es requerida")
    private LocalDate fechaRegistro;

    @NotBlank(message = "El diagnóstico es requerido")
    private String diagnostico;

    @NotBlank(message = "El tratamiento es requerido")
    private String tratamiento;

    private String observaciones;
    private LocalDate proximaSesion;
}
