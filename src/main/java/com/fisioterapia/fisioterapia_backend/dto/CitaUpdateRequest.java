package com.fisioterapia.fisioterapia_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaUpdateRequest {
    // Opcional: solo se actualiza si se proporciona
    private Long fisioterapeutaId;

    @NotNull(message = "La fecha es requerida")
    private LocalDate fecha;

    @NotNull(message = "La hora es requerida")
    private LocalTime hora;

    private String motivo;
}
